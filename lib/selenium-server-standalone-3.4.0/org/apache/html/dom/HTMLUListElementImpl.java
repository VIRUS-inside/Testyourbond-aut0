package org.apache.html.dom;

import org.w3c.dom.html.HTMLUListElement;

public class HTMLUListElementImpl
  extends HTMLElementImpl
  implements HTMLUListElement
{
  private static final long serialVersionUID = -3220401442015109211L;
  
  public boolean getCompact()
  {
    return getBinary("compact");
  }
  
  public void setCompact(boolean paramBoolean)
  {
    setAttribute("compact", paramBoolean);
  }
  
  public String getType()
  {
    return getAttribute("type");
  }
  
  public void setType(String paramString)
  {
    setAttribute("type", paramString);
  }
  
  public HTMLUListElementImpl(HTMLDocumentImpl paramHTMLDocumentImpl, String paramString)
  {
    super(paramHTMLDocumentImpl, paramString);
  }
}
