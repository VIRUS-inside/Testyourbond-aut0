package org.w3c.dom.html;

public abstract interface HTMLTableRowElement
  extends HTMLElement
{
  public abstract int getRowIndex();
  
  public abstract void setRowIndex(int paramInt);
  
  public abstract int getSectionRowIndex();
  
  public abstract void setSectionRowIndex(int paramInt);
  
  public abstract HTMLCollection getCells();
  
  public abstract void setCells(HTMLCollection paramHTMLCollection);
  
  public abstract String getAlign();
  
  public abstract void setAlign(String paramString);
  
  public abstract String getBgColor();
  
  public abstract void setBgColor(String paramString);
  
  public abstract String getCh();
  
  public abstract void setCh(String paramString);
  
  public abstract String getChOff();
  
  public abstract void setChOff(String paramString);
  
  public abstract String getVAlign();
  
  public abstract void setVAlign(String paramString);
  
  public abstract HTMLElement insertCell(int paramInt);
  
  public abstract void deleteCell(int paramInt);
}
