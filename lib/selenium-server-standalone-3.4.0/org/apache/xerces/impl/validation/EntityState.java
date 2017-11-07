package org.apache.xerces.impl.validation;

public abstract interface EntityState
{
  public abstract boolean isEntityDeclared(String paramString);
  
  public abstract boolean isEntityUnparsed(String paramString);
}
