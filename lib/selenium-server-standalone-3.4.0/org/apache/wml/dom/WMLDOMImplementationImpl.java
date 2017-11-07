package org.apache.wml.dom;

import org.apache.wml.WMLDOMImplementation;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;

public class WMLDOMImplementationImpl
  extends DOMImplementationImpl
  implements WMLDOMImplementation
{
  static final DOMImplementationImpl singleton = new WMLDOMImplementationImpl();
  
  public WMLDOMImplementationImpl() {}
  
  public static DOMImplementation getDOMImplementation()
  {
    return singleton;
  }
  
  protected CoreDocumentImpl createDocument(DocumentType paramDocumentType)
  {
    return new WMLDocumentImpl(paramDocumentType);
  }
}
