package org.w3c.dom.html;

public abstract interface HTMLIsIndexElement
  extends HTMLElement
{
  public abstract HTMLFormElement getForm();
  
  public abstract String getPrompt();
  
  public abstract void setPrompt(String paramString);
}
