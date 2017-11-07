package org.w3c.dom;

public abstract interface DocumentType
  extends Node
{
  public abstract String getName();
  
  public abstract NamedNodeMap getEntities();
  
  public abstract NamedNodeMap getNotations();
  
  public abstract String getPublicId();
  
  public abstract String getSystemId();
  
  public abstract String getInternalSubset();
}
