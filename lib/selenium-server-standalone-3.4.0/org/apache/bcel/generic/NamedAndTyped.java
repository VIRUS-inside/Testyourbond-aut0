package org.apache.bcel.generic;

public abstract interface NamedAndTyped
{
  public abstract String getName();
  
  public abstract Type getType();
  
  public abstract void setName(String paramString);
  
  public abstract void setType(Type paramType);
}
