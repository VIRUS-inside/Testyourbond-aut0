package org.w3c.css.sac;

public abstract interface SiblingSelector
  extends Selector
{
  public static final short ANY_NODE = 201;
  
  public abstract short getNodeType();
  
  public abstract Selector getSelector();
  
  public abstract SimpleSelector getSiblingSelector();
}
