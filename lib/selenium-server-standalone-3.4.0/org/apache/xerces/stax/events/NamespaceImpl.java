package org.apache.xerces.stax.events;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Namespace;

public final class NamespaceImpl
  extends AttributeImpl
  implements Namespace
{
  private final String fPrefix;
  private final String fNamespaceURI;
  
  public NamespaceImpl(String paramString1, String paramString2, Location paramLocation)
  {
    super(13, makeAttributeQName(paramString1), paramString2, null, true, paramLocation);
    fPrefix = (paramString1 == null ? "" : paramString1);
    fNamespaceURI = paramString2;
  }
  
  private static QName makeAttributeQName(String paramString)
  {
    if ((paramString == null) || (paramString.equals(""))) {
      return new QName("http://www.w3.org/2000/xmlns/", "xmlns", "");
    }
    return new QName("http://www.w3.org/2000/xmlns/", paramString, "xmlns");
  }
  
  public String getPrefix()
  {
    return fPrefix;
  }
  
  public String getNamespaceURI()
  {
    return fNamespaceURI;
  }
  
  public boolean isDefaultNamespaceDeclaration()
  {
    return fPrefix.length() == 0;
  }
}
