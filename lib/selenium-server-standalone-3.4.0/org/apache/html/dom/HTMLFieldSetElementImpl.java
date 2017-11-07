package org.apache.html.dom;

import org.w3c.dom.html.HTMLFieldSetElement;

public class HTMLFieldSetElementImpl
  extends HTMLElementImpl
  implements HTMLFieldSetElement, HTMLFormControl
{
  private static final long serialVersionUID = 1146145578073441343L;
  
  public HTMLFieldSetElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
