package org.w3c.dom;

public abstract interface NameList
{
  public abstract String getName(int paramInt);
  
  public abstract String getNamespaceURI(int paramInt);
  
  public abstract int getLength();
  
  public abstract boolean contains(String paramString);
  
  public abstract boolean containsNS(String paramString1, String paramString2);
}
