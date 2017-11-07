package org.apache.wml;

public abstract interface WMLDoElement
  extends WMLElement
{
  public abstract void setOptional(String paramString);
  
  public abstract String getOptional();
  
  public abstract void setLabel(String paramString);
  
  public abstract String getLabel();
  
  public abstract void setType(String paramString);
  
  public abstract String getType();
  
  public abstract void setName(String paramString);
  
  public abstract String getName();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
