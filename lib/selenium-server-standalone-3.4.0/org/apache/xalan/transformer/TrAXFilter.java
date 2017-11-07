package org.apache.xalan.transformer;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.StylesheetRoot;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;
































public class TrAXFilter
  extends XMLFilterImpl
{
  private Templates m_templates;
  private TransformerImpl m_transformer;
  
  public TrAXFilter(Templates templates)
    throws TransformerConfigurationException
  {
    m_templates = templates;
    m_transformer = ((TransformerImpl)templates.newTransformer());
  }
  



  public TransformerImpl getTransformer()
  {
    return m_transformer;
  }
  












  public void setParent(XMLReader parent)
  {
    super.setParent(parent);
    
    if (null != parent.getContentHandler()) {
      setContentHandler(parent.getContentHandler());
    }
    


    setupParse();
  }
  











  public void parse(InputSource input)
    throws SAXException, IOException
  {
    if (null == getParent())
    {
      XMLReader reader = null;
      
      try
      {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        
        factory.setNamespaceAware(true);
        
        if (m_transformer.getStylesheet().isSecureProcessing()) {
          try {
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
          }
          catch (SAXException se) {}
        }
        
        SAXParser jaxpParser = factory.newSAXParser();
        
        reader = jaxpParser.getXMLReader();
      }
      catch (ParserConfigurationException ex) {
        throw new SAXException(ex);
      } catch (FactoryConfigurationError ex1) {
        throw new SAXException(ex1.toString());
      }
      catch (NoSuchMethodError ex2) {}catch (AbstractMethodError ame) {}
      
      XMLReader parent;
      XMLReader parent;
      if (reader == null) {
        parent = XMLReaderFactory.createXMLReader();
      } else {
        parent = reader;
      }
      try {
        parent.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      }
      catch (SAXException se) {}
      

      setParent(parent);

    }
    else
    {
      setupParse();
    }
    if (null == m_transformer.getContentHandler())
    {
      throw new SAXException(XSLMessages.createMessage("ER_CANNOT_CALL_PARSE", null));
    }
    
    getParent().parse(input);
    Exception e = m_transformer.getExceptionThrown();
    if (null != e)
    {
      if ((e instanceof SAXException)) {
        throw ((SAXException)e);
      }
      throw new SAXException(e);
    }
  }
  











  public void parse(String systemId)
    throws SAXException, IOException
  {
    parse(new InputSource(systemId));
  }
  








  private void setupParse()
  {
    XMLReader p = getParent();
    if (p == null) {
      throw new NullPointerException(XSLMessages.createMessage("ER_NO_PARENT_FOR_FILTER", null));
    }
    
    ContentHandler ch = m_transformer.getInputContentHandler();
    

    p.setContentHandler(ch);
    p.setEntityResolver(this);
    p.setDTDHandler(this);
    p.setErrorHandler(this);
  }
  








  public void setContentHandler(ContentHandler handler)
  {
    m_transformer.setContentHandler(handler);
  }
  

  public void setErrorListener(ErrorListener handler)
  {
    m_transformer.setErrorListener(handler);
  }
}
