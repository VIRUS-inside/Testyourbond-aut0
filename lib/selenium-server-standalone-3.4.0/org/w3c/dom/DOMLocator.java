package org.w3c.dom;

public abstract interface DOMLocator
{
  public abstract int getLineNumber();
  
  public abstract int getColumnNumber();
  
  public abstract int getByteOffset();
  
  public abstract int getUtf16Offset();
  
  public abstract Node getRelatedNode();
  
  public abstract String getUri();
}
