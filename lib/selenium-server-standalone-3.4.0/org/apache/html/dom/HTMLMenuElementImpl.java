package org.apache.html.dom;

import org.w3c.dom.html.HTMLMenuElement;

public class HTMLMenuElementImpl
  extends HTMLElementImpl
  implements HTMLMenuElement
{
  private static final long serialVersionUID = -1489696654903916901L;
  
  public boolean getCompact()
  {
    return getBinary("compact");
  }
  
  public void setCompact(boolean paramBoolean)
  {
    setAttribute("compact", paramBoolean);
  }
  
  public HTMLMenuElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
