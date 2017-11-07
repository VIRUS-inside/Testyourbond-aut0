package org.apache.wml;

public abstract interface WMLTableElement
  extends WMLElement
{
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setAlign(String paramString);
  
  public abstract String getAlign();
  
  public abstract void setColumns(int paramInt);
  
  public abstract int getColumns();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
