package org.apache.wml;

public abstract interface WMLTimerElement
  extends WMLElement
{
  public abstract void setName(String paramString);
  
  public abstract String getName();
  
  public abstract void setValue(String paramString);
  
  public abstract String getValue();
}
