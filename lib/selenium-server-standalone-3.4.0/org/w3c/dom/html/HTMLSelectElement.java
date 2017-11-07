package org.w3c.dom.html;

public abstract interface HTMLSelectElement
  extends HTMLElement
{
  public abstract String getType();
  
  public abstract int getSelectedIndex();
  
  public abstract void setSelectedIndex(int paramInt);
  
  public abstract String getValue();
  
  public abstract void setValue(String paramString);
  
  public abstract int getLength();
  
  public abstract HTMLFormElement getForm();
  
  public abstract HTMLCollection getOptions();
  
  public abstract boolean getDisabled();
  
  public abstract void setDisabled(boolean paramBoolean);
  
  public abstract boolean getMultiple();
  
  public abstract void setMultiple(boolean paramBoolean);
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract int getSize();
  
  public abstract void setSize(int paramInt);
  
  public abstract int getTabIndex();
  
  public abstract void setTabIndex(int paramInt);
  
  public abstract void add(HTMLElement paramHTMLElement1, HTMLElement paramHTMLElement2);
  
  public abstract void remove(int paramInt);
  
  public abstract void blur();
  
  public abstract void focus();
}
