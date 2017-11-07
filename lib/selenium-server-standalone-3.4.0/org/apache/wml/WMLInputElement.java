package org.apache.wml;

public abstract interface WMLInputElement
  extends WMLElement
{
  public abstract void setName(String paramString);
  
  public abstract String getName();
  
  public abstract void setValue(String paramString);
  
  public abstract String getValue();
  
  public abstract void setType(String paramString);
  
  public abstract String getType();
  
  public abstract void setFormat(String paramString);
  
  public abstract String getFormat();
  
  public abstract void setEmptyOk(boolean paramBoolean);
  
  public abstract boolean getEmptyOk();
  
  public abstract void setSize(int paramInt);
  
  public abstract int getSize();
  
  public abstract void setMaxLength(int paramInt);
  
  public abstract int getMaxLength();
  
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setTabIndex(int paramInt);
  
  public abstract int getTabIndex();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
