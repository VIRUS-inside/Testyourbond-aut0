package org.apache.html.dom;

import org.w3c.dom.html.HTMLHeadElement;

public class HTMLHeadElementImpl
  extends HTMLElementImpl
  implements HTMLHeadElement
{
  private static final long serialVersionUID = 6438668473721292232L;
  
  public String getProfile()
  {
    return getAttribute("profile");
  }
  
  public void setProfile(String paramString)
  {
    setAttribute("profile", paramString);
  }
  
  public HTMLHeadElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
