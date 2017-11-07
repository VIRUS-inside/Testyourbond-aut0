package org.w3c.dom.html;

public abstract interface HTMLMapElement
  extends HTMLElement
{
  public abstract HTMLCollection getAreas();
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
}
