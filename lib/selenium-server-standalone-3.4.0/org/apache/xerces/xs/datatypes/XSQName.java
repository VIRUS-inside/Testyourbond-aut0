package org.apache.xerces.xs.datatypes;

public abstract interface XSQName
{
  public abstract org.apache.xerces.xni.QName getXNIQName();
  
  public abstract javax.xml.namespace.QName getJAXPQName();
}
