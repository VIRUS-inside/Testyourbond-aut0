package org.apache.html.dom;

import org.w3c.dom.html.HTMLBRElement;

public class HTMLBRElementImpl
  extends HTMLElementImpl
  implements HTMLBRElement
{
  private static final long serialVersionUID = 311960206282154750L;
  
  public String getClear()
  {
    return capitalize(getAttribute("clear"));
  }
  
  public void setClear(String paramString)
  {
    setAttribute("clear", paramString);
  }
  
  public HTMLBRElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
