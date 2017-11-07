package org.apache.wml;

public abstract interface WMLSelectElement
  extends WMLElement
{
  public abstract void setTabIndex(int paramInt);
  
  public abstract int getTabIndex();
  
  public abstract void setMultiple(boolean paramBoolean);
  
  public abstract boolean getMultiple();
  
  public abstract void setName(String paramString);
  
  public abstract String getName();
  
  public abstract void setValue(String paramString);
  
  public abstract String getValue();
  
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setIName(String paramString);
  
  public abstract String getIName();
  
  public abstract void setIValue(String paramString);
  
  public abstract String getIValue();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
