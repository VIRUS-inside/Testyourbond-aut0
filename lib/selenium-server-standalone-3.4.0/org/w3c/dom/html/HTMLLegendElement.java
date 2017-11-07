package org.w3c.dom.html;

public abstract interface HTMLLegendElement
  extends HTMLElement
{
  public abstract HTMLFormElement getForm();
  
  public abstract String getAccessKey();
  
  public abstract void setAccessKey(String paramString);
  
  public abstract String getAlign();
  
  public abstract void setAlign(String paramString);
}
