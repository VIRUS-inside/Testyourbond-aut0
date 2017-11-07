package org.w3c.dom.html;

public abstract interface HTMLLabelElement
  extends HTMLElement
{
  public abstract HTMLFormElement getForm();
  
  public abstract String getAccessKey();
  
  public abstract void setAccessKey(String paramString);
  
  public abstract String getHtmlFor();
  
  public abstract void setHtmlFor(String paramString);
}
