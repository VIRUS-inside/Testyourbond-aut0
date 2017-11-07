package org.w3c.dom.html;

public abstract interface HTMLTableSectionElement
  extends HTMLElement
{
  public abstract String getAlign();
  
  public abstract void setAlign(String paramString);
  
  public abstract String getCh();
  
  public abstract void setCh(String paramString);
  
  public abstract String getChOff();
  
  public abstract void setChOff(String paramString);
  
  public abstract String getVAlign();
  
  public abstract void setVAlign(String paramString);
  
  public abstract HTMLCollection getRows();
  
  public abstract HTMLElement insertRow(int paramInt);
  
  public abstract void deleteRow(int paramInt);
}
