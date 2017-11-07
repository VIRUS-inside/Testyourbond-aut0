package org.apache.xerces.util;

final class XMLErrorCode
{
  private String fDomain;
  private String fKey;
  
  public XMLErrorCode(String paramString1, String paramString2)
  {
    fDomain = paramString1;
    fKey = paramString2;
  }
  
  public void setValues(String paramString1, String paramString2)
  {
    fDomain = paramString1;
    fKey = paramString2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof XMLErrorCode)) {
      return false;
    }
    XMLErrorCode localXMLErrorCode = (XMLErrorCode)paramObject;
    return (fDomain.equals(fDomain)) && (fKey.equals(fKey));
  }
  
  public int hashCode()
  {
    return fDomain.hashCode() + fKey.hashCode();
  }
}
