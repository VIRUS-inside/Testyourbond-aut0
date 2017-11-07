package org.apache.bcel.generic;

public abstract interface LoadClass
{
  public abstract ObjectType getLoadClassType(ConstantPoolGen paramConstantPoolGen);
  
  public abstract Type getType(ConstantPoolGen paramConstantPoolGen);
}
