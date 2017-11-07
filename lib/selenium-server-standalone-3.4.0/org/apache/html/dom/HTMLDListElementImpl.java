package org.apache.html.dom;

import org.w3c.dom.html.HTMLDListElement;

public class HTMLDListElementImpl
  extends HTMLElementImpl
  implements HTMLDListElement
{
  private static final long serialVersionUID = -2130005642453038604L;
  
  public boolean getCompact()
  {
    return getBinary("compact");
  }
  
  public void setCompact(boolean paramBoolean)
  {
    setAttribute("compact", paramBoolean);
  }
  
  public HTMLDListElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
