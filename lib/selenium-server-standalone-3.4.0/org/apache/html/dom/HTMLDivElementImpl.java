package org.apache.html.dom;

import org.w3c.dom.html.HTMLDivElement;

public class HTMLDivElementImpl
  extends HTMLElementImpl
  implements HTMLDivElement
{
  private static final long serialVersionUID = 2327098984177358833L;
  
  public String getAlign()
  {
    return capitalize(getAttribute("align"));
  }
  
  public void setAlign(String paramString)
  {
    setAttribute("align", paramString);
  }
  
  public HTMLDivElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
