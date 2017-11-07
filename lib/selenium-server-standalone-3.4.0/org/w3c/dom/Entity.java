package org.w3c.dom;

public abstract interface Entity
  extends Node
{
  public abstract String getPublicId();
  
  public abstract String getSystemId();
  
  public abstract String getNotationName();
  
  public abstract String getInputEncoding();
  
  public abstract String getXmlEncoding();
  
  public abstract String getXmlVersion();
}
