package org.w3c.dom;

public abstract interface ProcessingInstruction
  extends Node
{
  public abstract String getTarget();
  
  public abstract String getData();
  
  public abstract void setData(String paramString)
    throws DOMException;
}
