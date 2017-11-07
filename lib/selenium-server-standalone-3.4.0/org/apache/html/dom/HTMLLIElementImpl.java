package org.apache.html.dom;

import org.w3c.dom.html.HTMLLIElement;

public class HTMLLIElementImpl
  extends HTMLElementImpl
  implements HTMLLIElement
{
  private static final long serialVersionUID = -8987309345926701831L;
  
  public String getType()
  {
    return getAttribute("type");
  }
  
  public void setType(String paramString)
  {
    setAttribute("type", paramString);
  }
  
  public int getValue()
  {
    return getInteger(getAttribute("value"));
  }
  
  public void setValue(int paramInt)
  {
    setAttribute("value", String.valueOf(paramInt));
  }
  
  public HTMLLIElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
