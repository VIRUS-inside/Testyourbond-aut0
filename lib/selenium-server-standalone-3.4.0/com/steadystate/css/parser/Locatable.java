package com.steadystate.css.parser;

import org.w3c.css.sac.Locator;

public abstract interface Locatable
{
  public abstract Locator getLocator();
  
  public abstract void setLocator(Locator paramLocator);
}
