package org.w3c.dom;

public abstract interface Notation
  extends Node
{
  public abstract String getPublicId();
  
  public abstract String getSystemId();
}
