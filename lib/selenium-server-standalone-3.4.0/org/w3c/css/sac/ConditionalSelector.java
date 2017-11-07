package org.w3c.css.sac;

public abstract interface ConditionalSelector
  extends SimpleSelector
{
  public abstract SimpleSelector getSimpleSelector();
  
  public abstract Condition getCondition();
}
