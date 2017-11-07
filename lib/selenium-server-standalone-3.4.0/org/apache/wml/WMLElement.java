package org.apache.wml;

import org.w3c.dom.Element;

public abstract interface WMLElement
  extends Element
{
  public abstract void setId(String paramString);
  
  public abstract String getId();
  
  public abstract void setClassName(String paramString);
  
  public abstract String getClassName();
}
