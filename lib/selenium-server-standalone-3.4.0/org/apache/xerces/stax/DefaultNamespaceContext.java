package org.apache.xerces.stax;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.xml.namespace.NamespaceContext;

public final class DefaultNamespaceContext
  implements NamespaceContext
{
  private static final DefaultNamespaceContext DEFAULT_NAMESPACE_CONTEXT_INSTANCE = new DefaultNamespaceContext();
  
  private DefaultNamespaceContext() {}
  
  public static DefaultNamespaceContext getInstance()
  {
    return DEFAULT_NAMESPACE_CONTEXT_INSTANCE;
  }
  
  public String getNamespaceURI(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Prefix cannot be null.");
    }
    if ("xml".equals(paramString)) {
      return "http://www.w3.org/XML/1998/namespace";
    }
    if ("xmlns".equals(paramString)) {
      return "http://www.w3.org/2000/xmlns/";
    }
    return "";
  }
  
  public String getPrefix(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Namespace URI cannot be null.");
    }
    if ("http://www.w3.org/XML/1998/namespace".equals(paramString)) {
      return "xml";
    }
    if ("http://www.w3.org/2000/xmlns/".equals(paramString)) {
      return "xmlns";
    }
    return null;
  }
  
  public Iterator getPrefixes(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Namespace URI cannot be null.");
    }
    if ("http://www.w3.org/XML/1998/namespace".equals(paramString)) {
      new Iterator()
      {
        boolean more = true;
        
        public boolean hasNext()
        {
          return more;
        }
        
        public Object next()
        {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          more = false;
          return "xml";
        }
        
        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      };
    }
    if ("http://www.w3.org/2000/xmlns/".equals(paramString)) {
      new Iterator()
      {
        boolean more = true;
        
        public boolean hasNext()
        {
          return more;
        }
        
        public Object next()
        {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          more = false;
          return "xmlns";
        }
        
        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      };
    }
    return Collections.EMPTY_LIST.iterator();
  }
}
