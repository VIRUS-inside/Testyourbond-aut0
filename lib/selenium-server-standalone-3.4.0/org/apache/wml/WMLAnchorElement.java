package org.apache.wml;

public abstract interface WMLAnchorElement
  extends WMLElement
{
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
