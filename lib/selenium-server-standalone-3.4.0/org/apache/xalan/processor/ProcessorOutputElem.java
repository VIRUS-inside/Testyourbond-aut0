package org.apache.xalan.processor;

import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;































class ProcessorOutputElem
  extends XSLTElementProcessor
{
  static final long serialVersionUID = 3513742319582547590L;
  private OutputProperties m_outputProperties;
  
  ProcessorOutputElem() {}
  
  public void setCdataSectionElements(Vector newValue)
  {
    m_outputProperties.setQNameProperties("cdata-section-elements", newValue);
  }
  





  public void setDoctypePublic(String newValue)
  {
    m_outputProperties.setProperty("doctype-public", newValue);
  }
  





  public void setDoctypeSystem(String newValue)
  {
    m_outputProperties.setProperty("doctype-system", newValue);
  }
  





  public void setEncoding(String newValue)
  {
    m_outputProperties.setProperty("encoding", newValue);
  }
  





  public void setIndent(boolean newValue)
  {
    m_outputProperties.setBooleanProperty("indent", newValue);
  }
  





  public void setMediaType(String newValue)
  {
    m_outputProperties.setProperty("media-type", newValue);
  }
  





  public void setMethod(QName newValue)
  {
    m_outputProperties.setQNameProperty("method", newValue);
  }
  





  public void setOmitXmlDeclaration(boolean newValue)
  {
    m_outputProperties.setBooleanProperty("omit-xml-declaration", newValue);
  }
  





  public void setStandalone(boolean newValue)
  {
    m_outputProperties.setBooleanProperty("standalone", newValue);
  }
  





  public void setVersion(String newValue)
  {
    m_outputProperties.setProperty("version", newValue);
  }
  




  public void setForeignAttr(String attrUri, String attrLocalName, String attrRawName, String attrValue)
  {
    QName key = new QName(attrUri, attrLocalName);
    m_outputProperties.setProperty(key, attrValue);
  }
  




  public void addLiteralResultAttribute(String attrUri, String attrLocalName, String attrRawName, String attrValue)
  {
    QName key = new QName(attrUri, attrLocalName);
    m_outputProperties.setProperty(key, attrValue);
  }
  




















  public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
    throws SAXException
  {
    m_outputProperties = new OutputProperties();
    
    m_outputProperties.setDOMBackPointer(handler.getOriginatingNode());
    m_outputProperties.setLocaterInfo(handler.getLocator());
    m_outputProperties.setUid(handler.nextUid());
    setPropertiesFromAttributes(handler, rawName, attributes, this);
    


    String entitiesFileName = (String)m_outputProperties.getProperties().get("{http://xml.apache.org/xalan}entities");
    

    if (null != entitiesFileName)
    {
      try
      {
        String absURL = SystemIDResolver.getAbsoluteURI(entitiesFileName, handler.getBaseIdentifier());
        
        m_outputProperties.getProperties().put("{http://xml.apache.org/xalan}entities", absURL);
      }
      catch (TransformerException te)
      {
        handler.error(te.getMessage(), te);
      }
    }
    
    handler.getStylesheet().setOutput(m_outputProperties);
    
    ElemTemplateElement parent = handler.getElemTemplateElement();
    parent.appendChild(m_outputProperties);
    
    m_outputProperties = null;
  }
}
