package org.apache.wml.dom;

import org.apache.wml.WMLRefreshElement;

public class WMLRefreshElementImpl
  extends WMLElementImpl
  implements WMLRefreshElement
{
  private static final long serialVersionUID = 8781837880806459398L;
  
  public WMLRefreshElementImpl(WMLDocumentImpl paramWMLDocumentImpl, String paramString)
  {
    super(paramWMLDocumentImpl, paramString);
  }
  
  public void setClassName(String paramString)
  {
    setAttribute("class", paramString);
  }
  
  public String getClassName()
  {
    return getAttribute("class");
  }
  
  public void setId(String paramString)
  {
    setAttribute("id", paramString);
  }
  
  public String getId()
  {
    return getAttribute("id");
  }
}
