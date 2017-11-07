package org.w3c.dom;

public abstract interface DOMImplementationSource
{
  public abstract DOMImplementation getDOMImplementation(String paramString);
  
  public abstract DOMImplementationList getDOMImplementationList(String paramString);
}
