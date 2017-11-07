package org.apache.xml.utils;

import org.w3c.dom.Node;

public abstract interface PrefixResolver
{
  public abstract String getNamespaceForPrefix(String paramString);
  
  public abstract String getNamespaceForPrefix(String paramString, Node paramNode);
  
  public abstract String getBaseIdentifier();
  
  public abstract boolean handlesNullPrefixes();
}
