package org.apache.wml;

public abstract interface WMLAElement
  extends WMLElement
{
  public abstract void setHref(String paramString);
  
  public abstract String getHref();
  
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setId(String paramString);
  
  public abstract String getId();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
