package org.apache.wml.dom;

import org.apache.wml.WMLTrElement;

public class WMLTrElementImpl
  extends WMLElementImpl
  implements WMLTrElement
{
  private static final long serialVersionUID = -4304021232051604343L;
  
  public WMLTrElementImpl(WMLDocumentImpl paramWMLDocumentImpl, String paramString)
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
