package org.apache.wml;

public abstract interface WMLPostfieldElement
  extends WMLElement
{
  public abstract void setValue(String paramString);
  
  public abstract String getValue();
  
  public abstract void setName(String paramString);
  
  public abstract String getName();
}
