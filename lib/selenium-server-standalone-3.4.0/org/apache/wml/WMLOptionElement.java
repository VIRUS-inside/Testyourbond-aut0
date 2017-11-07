package org.apache.wml;

public abstract interface WMLOptionElement
  extends WMLElement
{
  public abstract void setValue(String paramString);
  
  public abstract String getValue();
  
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setOnPick(String paramString);
  
  public abstract String getOnPick();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
