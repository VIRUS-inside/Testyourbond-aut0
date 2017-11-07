package javax.xml.namespace;

import java.util.Iterator;

public abstract interface NamespaceContext
{
  public abstract String getNamespaceURI(String paramString);
  
  public abstract String getPrefix(String paramString);
  
  public abstract Iterator getPrefixes(String paramString);
}
