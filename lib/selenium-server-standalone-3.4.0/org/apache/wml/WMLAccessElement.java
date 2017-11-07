package org.apache.wml;

public abstract interface WMLAccessElement
  extends WMLElement
{
  public abstract void setDomain(String paramString);
  
  public abstract String getDomain();
  
  public abstract void setPath(String paramString);
  
  public abstract String getPath();
}
