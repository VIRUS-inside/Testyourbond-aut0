package org.w3c.css.sac;

public abstract interface Locator
{
  public abstract String getURI();
  
  public abstract int getLineNumber();
  
  public abstract int getColumnNumber();
}
