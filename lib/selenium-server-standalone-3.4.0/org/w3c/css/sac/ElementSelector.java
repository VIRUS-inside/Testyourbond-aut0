package org.w3c.css.sac;

public abstract interface ElementSelector
  extends SimpleSelector
{
  public abstract String getNamespaceURI();
  
  public abstract String getLocalName();
}
