package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;



































/**
 * @deprecated
 */
public final class ToHTMLSAXHandler
  extends ToSAXHandler
{
  private boolean m_dtdHandled = false;
  



  protected boolean m_escapeSetting = true;
  





  public Properties getOutputFormat()
  {
    return null;
  }
  





  public OutputStream getOutputStream()
  {
    return null;
  }
  





  public Writer getWriter()
  {
    return null;
  }
  






  public void indent(int n)
    throws SAXException
  {}
  





  public void serialize(Node node)
    throws IOException
  {}
  





  public boolean setEscaping(boolean escape)
    throws SAXException
  {
    boolean oldEscapeSetting = m_escapeSetting;
    m_escapeSetting = escape;
    
    if (escape) {
      processingInstruction("javax.xml.transform.enable-output-escaping", "");
    } else {
      processingInstruction("javax.xml.transform.disable-output-escaping", "");
    }
    
    return oldEscapeSetting;
  }
  










  public void setIndent(boolean indent) {}
  










  public void setOutputFormat(Properties format) {}
  










  public void setOutputStream(OutputStream output) {}
  










  public void setWriter(Writer writer) {}
  










  public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
    throws SAXException
  {}
  










  public void elementDecl(String name, String model)
    throws SAXException
  {}
  










  public void externalEntityDecl(String arg0, String arg1, String arg2)
    throws SAXException
  {}
  










  public void internalEntityDecl(String name, String value)
    throws SAXException
  {}
  









  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    flushPending();
    m_saxHandler.endElement(uri, localName, qName);
    

    if (m_tracer != null) {
      super.fireEndElem(qName);
    }
  }
  









  public void endPrefixMapping(String prefix)
    throws SAXException
  {}
  









  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {}
  









  public void processingInstruction(String target, String data)
    throws SAXException
  {
    flushPending();
    m_saxHandler.processingInstruction(target, data);
    


    if (m_tracer != null) {
      super.fireEscapingEvent(target, data);
    }
  }
  













  public void setDocumentLocator(Locator arg0) {}
  













  public void skippedEntity(String arg0)
    throws SAXException
  {}
  












  public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws SAXException
  {
    flushPending();
    super.startElement(namespaceURI, localName, qName, atts);
    m_saxHandler.startElement(namespaceURI, localName, qName, atts);
    m_elemContext.m_startTagOpen = false;
  }
  









  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    flushPending();
    if (m_lexHandler != null) {
      m_lexHandler.comment(ch, start, length);
    }
    
    if (m_tracer != null) {
      super.fireCommentEvent(ch, start, length);
    }
  }
  







  public void endCDATA()
    throws SAXException
  {}
  







  public void endDTD()
    throws SAXException
  {}
  






  public void startCDATA()
    throws SAXException
  {}
  






  public void startEntity(String arg0)
    throws SAXException
  {}
  






  public void endDocument()
    throws SAXException
  {
    flushPending();
    

    m_saxHandler.endDocument();
    
    if (m_tracer != null) {
      super.fireEndDoc();
    }
  }
  



  protected void closeStartTag()
    throws SAXException
  {
    m_elemContext.m_startTagOpen = false;
    

    m_saxHandler.startElement("", m_elemContext.m_elementName, m_elemContext.m_elementName, m_attributes);
    



    m_attributes.clear();
  }
  








  public void close() {}
  








  public void characters(String chars)
    throws SAXException
  {
    int length = chars.length();
    if (length > m_charsBuff.length)
    {
      m_charsBuff = new char[length * 2 + 1];
    }
    chars.getChars(0, length, m_charsBuff, 0);
    characters(m_charsBuff, 0, length);
  }
  






  public ToHTMLSAXHandler(ContentHandler handler, String encoding)
  {
    super(handler, encoding);
  }
  








  public ToHTMLSAXHandler(ContentHandler handler, LexicalHandler lex, String encoding)
  {
    super(handler, lex, encoding);
  }
  














  public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
    throws SAXException
  {
    super.startElement(elementNamespaceURI, elementLocalName, elementName);
    
    flushPending();
    

    if (!m_dtdHandled)
    {
      String doctypeSystem = getDoctypeSystem();
      String doctypePublic = getDoctypePublic();
      if (((doctypeSystem != null) || (doctypePublic != null)) && 
        (m_lexHandler != null)) {
        m_lexHandler.startDTD(elementName, doctypePublic, doctypeSystem);
      }
      


      m_dtdHandled = true;
    }
    m_elemContext = m_elemContext.push(elementNamespaceURI, elementLocalName, elementName);
  }
  





  public void startElement(String elementName)
    throws SAXException
  {
    startElement(null, null, elementName);
  }
  







  public void endElement(String elementName)
    throws SAXException
  {
    flushPending();
    m_saxHandler.endElement("", elementName, elementName);
    

    if (m_tracer != null) {
      super.fireEndElem(elementName);
    }
  }
  




























  public void characters(char[] ch, int off, int len)
    throws SAXException
  {
    flushPending();
    m_saxHandler.characters(ch, off, len);
    

    if (m_tracer != null) {
      super.fireCharEvent(ch, off, len);
    }
  }
  


  public void flushPending()
    throws SAXException
  {
    if (m_needToCallStartDocument)
    {
      startDocumentInternal();
      m_needToCallStartDocument = false;
    }
    
    if (m_elemContext.m_startTagOpen)
    {
      closeStartTag();
      m_elemContext.m_startTagOpen = false;
    }
  }
  






















  public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
    throws SAXException
  {
    if (shouldFlush)
      flushPending();
    m_saxHandler.startPrefixMapping(prefix, uri);
    return false;
  }
  















  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    startPrefixMapping(prefix, uri, true);
  }
  














  public void namespaceAfterStartElement(String prefix, String uri)
    throws SAXException
  {
    if (m_elemContext.m_elementURI == null)
    {
      String prefix1 = getPrefixPart(m_elemContext.m_elementName);
      if ((prefix1 == null) && ("".equals(prefix)))
      {




        m_elemContext.m_elementURI = uri;
      }
    }
    startPrefixMapping(prefix, uri, false);
  }
  








  public boolean reset()
  {
    boolean wasReset = false;
    if (super.reset())
    {
      resetToHTMLSAXHandler();
      wasReset = true;
    }
    return wasReset;
  }
  




  private void resetToHTMLSAXHandler()
  {
    m_dtdHandled = false;
    m_escapeSetting = true;
  }
}
