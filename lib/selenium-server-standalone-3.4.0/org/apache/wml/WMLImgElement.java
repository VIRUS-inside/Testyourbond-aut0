package org.apache.wml;

public abstract interface WMLImgElement
  extends WMLElement
{
  public abstract void setAlt(String paramString);
  
  public abstract String getAlt();
  
  public abstract void setSrc(String paramString);
  
  public abstract String getSrc();
  
  public abstract void setLocalSrc(String paramString);
  
  public abstract String getLocalSrc();
  
  public abstract void setVspace(String paramString);
  
  public abstract String getVspace();
  
  public abstract void setHspace(String paramString);
  
  public abstract String getHspace();
  
  public abstract void setAlign(String paramString);
  
  public abstract String getAlign();
  
  public abstract void setWidth(String paramString);
  
  public abstract String getWidth();
  
  public abstract void setHeight(String paramString);
  
  public abstract String getHeight();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
