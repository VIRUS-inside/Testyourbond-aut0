package org.apache.html.dom;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLDOMImplementation;
import org.w3c.dom.html.HTMLDocument;

public class HTMLDOMImplementationImpl
  extends DOMImplementationImpl
  implements HTMLDOMImplementation
{
  private static final HTMLDOMImplementation _instance = new HTMLDOMImplementationImpl();
  
  private HTMLDOMImplementationImpl() {}
  
  public final HTMLDocument createHTMLDocument(String paramString)
    throws DOMException
  {
    if (paramString == null) {
      throw new NullPointerException("HTM014 Argument 'title' is null.");
    }
    HTMLDocumentImpl localHTMLDocumentImpl = new HTMLDocumentImpl();
    localHTMLDocumentImpl.setTitle(paramString);
    return localHTMLDocumentImpl;
  }
  
  public static HTMLDOMImplementation getHTMLDOMImplementation()
  {
    return _instance;
  }
}
