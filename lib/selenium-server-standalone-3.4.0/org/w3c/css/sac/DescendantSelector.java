package org.w3c.css.sac;

public abstract interface DescendantSelector
  extends Selector
{
  public abstract Selector getAncestorSelector();
  
  public abstract SimpleSelector getSimpleSelector();
}
