package org.apache.xml.utils;

import java.util.EmptyStackException;
import java.util.Enumeration;
import org.xml.sax.helpers.NamespaceSupport;

































































public class NamespaceSupport2
  extends NamespaceSupport
{
  private Context2 currentContext;
  public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
  
  public NamespaceSupport2()
  {
    reset();
  }
  















  public void reset()
  {
    currentContext = new Context2(null);
    currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
  }
  




















  public void pushContext()
  {
    Context2 parentContext = currentContext;
    currentContext = parentContext.getChild();
    if (currentContext == null) {
      currentContext = new Context2(parentContext);

    }
    else
    {
      currentContext.setParent(parentContext);
    }
  }
  














  public void popContext()
  {
    Context2 parentContext = currentContext.getParent();
    if (parentContext == null) {
      throw new EmptyStackException();
    }
    currentContext = parentContext;
  }
  



































  public boolean declarePrefix(String prefix, String uri)
  {
    if ((prefix.equals("xml")) || (prefix.equals("xmlns"))) {
      return false;
    }
    currentContext.declarePrefix(prefix, uri);
    return true;
  }
  









































  public String[] processName(String qName, String[] parts, boolean isAttribute)
  {
    String[] name = currentContext.processName(qName, isAttribute);
    if (name == null) {
      return null;
    }
    

    System.arraycopy(name, 0, parts, 0, 3);
    return parts;
  }
  













  public String getURI(String prefix)
  {
    return currentContext.getURI(prefix);
  }
  














  public Enumeration getPrefixes()
  {
    return currentContext.getPrefixes();
  }
  



















  public String getPrefix(String uri)
  {
    return currentContext.getPrefix(uri);
  }
  































  public Enumeration getPrefixes(String uri)
  {
    return new PrefixForUriEnumerator(this, uri, getPrefixes());
  }
  













  public Enumeration getDeclaredPrefixes()
  {
    return currentContext.getDeclaredPrefixes();
  }
}
