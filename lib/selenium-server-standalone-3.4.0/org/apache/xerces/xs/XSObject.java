package org.apache.xerces.xs;

public abstract interface XSObject
{
  public abstract short getType();
  
  public abstract String getName();
  
  public abstract String getNamespace();
  
  public abstract XSNamespaceItem getNamespaceItem();
}
