package org.w3c.css.sac;

public abstract interface PositionalCondition
  extends Condition
{
  public abstract int getPosition();
  
  public abstract boolean getTypeNode();
  
  public abstract boolean getType();
}
