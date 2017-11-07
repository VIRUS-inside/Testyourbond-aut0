package org.apache.xerces.impl;

public class XML11NamespaceBinder
  extends XMLNamespaceBinder
{
  public XML11NamespaceBinder() {}
  
  protected boolean prefixBoundToNullURI(String paramString1, String paramString2)
  {
    return false;
  }
}
