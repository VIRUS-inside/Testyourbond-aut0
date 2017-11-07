package org.apache.html.dom;

import org.w3c.dom.html.HTMLIsIndexElement;

public class HTMLIsIndexElementImpl
  extends HTMLElementImpl
  implements HTMLIsIndexElement
{
  private static final long serialVersionUID = 3073521742049689699L;
  
  public String getPrompt()
  {
    return getAttribute("prompt");
  }
  
  public void setPrompt(String paramString)
  {
    setAttribute("prompt", paramString);
  }
  
  public HTMLIsIndexElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
