package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;






























































































































































































































































































































































































class PrefixForUriEnumerator
  implements Enumeration
{
  private Enumeration allPrefixes;
  private String uri;
  private String lookahead = null;
  
  private NamespaceSupport2 nsup;
  

  PrefixForUriEnumerator(NamespaceSupport2 nsup, String uri, Enumeration allPrefixes)
  {
    this.nsup = nsup;
    this.uri = uri;
    this.allPrefixes = allPrefixes;
  }
  
  public boolean hasMoreElements()
  {
    if (lookahead != null) {
      return true;
    }
    while (allPrefixes.hasMoreElements())
    {
      String prefix = (String)allPrefixes.nextElement();
      if (uri.equals(nsup.getURI(prefix)))
      {
        lookahead = prefix;
        return true;
      }
    }
    return false;
  }
  
  public Object nextElement()
  {
    if (hasMoreElements())
    {
      String tmp = lookahead;
      lookahead = null;
      return tmp;
    }
    
    throw new NoSuchElementException();
  }
}
