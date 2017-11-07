package org.apache.xerces.jaxp;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

class TeeXMLDocumentFilterImpl
  implements XMLDocumentFilter
{
  private XMLDocumentHandler next;
  private XMLDocumentHandler side;
  private XMLDocumentSource source;
  
  TeeXMLDocumentFilterImpl() {}
  
  public XMLDocumentHandler getSide()
  {
    return side;
  }
  
  public void setSide(XMLDocumentHandler paramXMLDocumentHandler)
  {
    side = paramXMLDocumentHandler;
  }
  
  public XMLDocumentSource getDocumentSource()
  {
    return source;
  }
  
  public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource)
  {
    source = paramXMLDocumentSource;
  }
  
  public XMLDocumentHandler getDocumentHandler()
  {
    return next;
  }
  
  public void setDocumentHandler(XMLDocumentHandler paramXMLDocumentHandler)
  {
    next = paramXMLDocumentHandler;
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    side.characters(paramXMLString, paramAugmentations);
    next.characters(paramXMLString, paramAugmentations);
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    side.comment(paramXMLString, paramAugmentations);
    next.comment(paramXMLString, paramAugmentations);
  }
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    side.doctypeDecl(paramString1, paramString2, paramString3, paramAugmentations);
    next.doctypeDecl(paramString1, paramString2, paramString3, paramAugmentations);
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    side.emptyElement(paramQName, paramXMLAttributes, paramAugmentations);
    next.emptyElement(paramQName, paramXMLAttributes, paramAugmentations);
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    side.endCDATA(paramAugmentations);
    next.endCDATA(paramAugmentations);
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {
    side.endDocument(paramAugmentations);
    next.endDocument(paramAugmentations);
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    side.endElement(paramQName, paramAugmentations);
    next.endElement(paramQName, paramAugmentations);
  }
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    side.endGeneralEntity(paramString, paramAugmentations);
    next.endGeneralEntity(paramString, paramAugmentations);
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    side.ignorableWhitespace(paramXMLString, paramAugmentations);
    next.ignorableWhitespace(paramXMLString, paramAugmentations);
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    side.processingInstruction(paramString, paramXMLString, paramAugmentations);
    next.processingInstruction(paramString, paramXMLString, paramAugmentations);
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    side.startCDATA(paramAugmentations);
    next.startCDATA(paramAugmentations);
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    side.startDocument(paramXMLLocator, paramString, paramNamespaceContext, paramAugmentations);
    next.startDocument(paramXMLLocator, paramString, paramNamespaceContext, paramAugmentations);
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    side.startElement(paramQName, paramXMLAttributes, paramAugmentations);
    next.startElement(paramQName, paramXMLAttributes, paramAugmentations);
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    side.startGeneralEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    next.startGeneralEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    side.textDecl(paramString1, paramString2, paramAugmentations);
    next.textDecl(paramString1, paramString2, paramAugmentations);
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    side.xmlDecl(paramString1, paramString2, paramString3, paramAugmentations);
    next.xmlDecl(paramString1, paramString2, paramString3, paramAugmentations);
  }
}
