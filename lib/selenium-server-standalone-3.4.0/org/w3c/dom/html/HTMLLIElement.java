package org.w3c.dom.html;

public abstract interface HTMLLIElement
  extends HTMLElement
{
  public abstract String getType();
  
  public abstract void setType(String paramString);
  
  public abstract int getValue();
  
  public abstract void setValue(int paramInt);
}
