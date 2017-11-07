package org.apache.xerces.xni;

import java.util.Enumeration;

public abstract interface NamespaceContext
{
  public static final String XML_URI = "http://www.w3.org/XML/1998/namespace".intern();
  public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();
  
  public abstract void pushContext();
  
  public abstract void popContext();
  
  public abstract boolean declarePrefix(String paramString1, String paramString2);
  
  public abstract String getURI(String paramString);
  
  public abstract String getPrefix(String paramString);
  
  public abstract int getDeclaredPrefixCount();
  
  public abstract String getDeclaredPrefixAt(int paramInt);
  
  public abstract Enumeration getAllPrefixes();
  
  public abstract void reset();
}
