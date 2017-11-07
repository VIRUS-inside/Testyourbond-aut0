package org.apache.wml;

public abstract interface WMLPElement
  extends WMLElement
{
  public abstract void setMode(String paramString);
  
  public abstract String getMode();
  
  public abstract void setAlign(String paramString);
  
  public abstract String getAlign();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
