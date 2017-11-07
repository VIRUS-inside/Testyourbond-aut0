package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface DOMImplementationAS
{
  public abstract ASModel createAS(boolean paramBoolean);
  
  public abstract DOMASBuilder createDOMASBuilder();
  
  public abstract DOMASWriter createDOMASWriter();
}
