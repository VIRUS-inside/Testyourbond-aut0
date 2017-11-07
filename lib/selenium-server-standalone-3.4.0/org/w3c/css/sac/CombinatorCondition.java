package org.w3c.css.sac;

public abstract interface CombinatorCondition
  extends Condition
{
  public abstract Condition getFirstCondition();
  
  public abstract Condition getSecondCondition();
}
