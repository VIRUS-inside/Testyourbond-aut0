package net.sourceforge.htmlunit.cyberneko.filters;

import net.sourceforge.htmlunit.cyberneko.HTMLComponent;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;





































public class DefaultFilter
  implements XMLDocumentFilter, HTMLComponent
{
  protected XMLDocumentHandler fDocumentHandler;
  protected XMLDocumentSource fDocumentSource;
  
  public DefaultFilter() {}
  
  public void setDocumentHandler(XMLDocumentHandler handler)
  {
    fDocumentHandler = handler;
  }
  



  public XMLDocumentHandler getDocumentHandler()
  {
    return fDocumentHandler;
  }
  

  public void setDocumentSource(XMLDocumentSource source)
  {
    fDocumentSource = source;
  }
  

  public XMLDocumentSource getDocumentSource()
  {
    return fDocumentSource;
  }
  








  public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      XercesBridge.getInstance().XMLDocumentHandler_startDocument(fDocumentHandler, locator, encoding, nscontext, augs);
    }
  }
  



  public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
    }
  }
  

  public void doctypeDecl(String root, String publicId, String systemId, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.doctypeDecl(root, publicId, systemId, augs);
    }
  }
  

  public void comment(XMLString text, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.comment(text, augs);
    }
  }
  

  public void processingInstruction(String target, XMLString data, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.processingInstruction(target, data, augs);
    }
  }
  

  public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.startElement(element, attributes, augs);
    }
  }
  

  public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.emptyElement(element, attributes, augs);
    }
  }
  

  public void characters(XMLString text, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.characters(text, augs);
    }
  }
  

  public void ignorableWhitespace(XMLString text, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.ignorableWhitespace(text, augs);
    }
  }
  

  public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.startGeneralEntity(name, id, encoding, augs);
    }
  }
  

  public void textDecl(String version, String encoding, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.textDecl(version, encoding, augs);
    }
  }
  

  public void endGeneralEntity(String name, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.endGeneralEntity(name, augs);
    }
  }
  
  public void startCDATA(Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.startCDATA(augs);
    }
  }
  
  public void endCDATA(Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.endCDATA(augs);
    }
  }
  

  public void endElement(QName element, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.endElement(element, augs);
    }
  }
  
  public void endDocument(Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.endDocument(augs);
    }
  }
  


  public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
    throws XNIException
  {
    startDocument(locator, encoding, null, augs);
  }
  
  public void startPrefixMapping(String prefix, String uri, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      XercesBridge.getInstance().XMLDocumentHandler_startPrefixMapping(fDocumentHandler, prefix, uri, augs);
    }
  }
  
  public void endPrefixMapping(String prefix, Augmentations augs)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      XercesBridge.getInstance().XMLDocumentHandler_endPrefixMapping(fDocumentHandler, prefix, augs);
    }
  }
  









  public String[] getRecognizedFeatures()
  {
    return null;
  }
  





  public Boolean getFeatureDefault(String featureId)
  {
    return null;
  }
  





  public String[] getRecognizedProperties()
  {
    return null;
  }
  





  public Object getPropertyDefault(String propertyId)
  {
    return null;
  }
  














  public void reset(XMLComponentManager componentManager)
    throws XMLConfigurationException
  {}
  













  public void setFeature(String featureId, boolean state)
    throws XMLConfigurationException
  {}
  













  public void setProperty(String propertyId, Object value)
    throws XMLConfigurationException
  {}
  













  protected static String[] merge(String[] array1, String[] array2)
  {
    if (array1 == array2) {
      return array1;
    }
    if (array1 == null) {
      return array2;
    }
    if (array2 == null) {
      return array1;
    }
    

    String[] array3 = new String[array1.length + array2.length];
    System.arraycopy(array1, 0, array3, 0, array1.length);
    System.arraycopy(array2, 0, array3, array1.length, array2.length);
    
    return array3;
  }
}
