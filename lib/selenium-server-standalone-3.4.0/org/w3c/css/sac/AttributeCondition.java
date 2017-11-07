package org.w3c.css.sac;

public abstract interface AttributeCondition
  extends Condition
{
  public abstract String getNamespaceURI();
  
  public abstract String getLocalName();
  
  public abstract boolean getSpecified();
  
  public abstract String getValue();
}
