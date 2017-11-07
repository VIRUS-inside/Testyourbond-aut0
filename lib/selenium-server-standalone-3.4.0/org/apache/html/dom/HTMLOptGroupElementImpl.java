package org.apache.html.dom;

import org.w3c.dom.html.HTMLOptGroupElement;

public class HTMLOptGroupElementImpl
  extends HTMLElementImpl
  implements HTMLOptGroupElement
{
  private static final long serialVersionUID = -8807098641226171501L;
  
  public boolean getDisabled()
  {
    return getBinary("disabled");
  }
  
  public void setDisabled(boolean paramBoolean)
  {
    setAttribute("disabled", paramBoolean);
  }
  
  public String getLabel()
  {
    return capitalize(getAttribute("label"));
  }
  
  public void setLabel(String paramString)
  {
    setAttribute("label", paramString);
  }
  
  public HTMLOptGroupElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
