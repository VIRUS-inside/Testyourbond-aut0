package org.apache.xerces.util;

import org.apache.xerces.xni.XMLResourceIdentifier;

public class XMLResourceIdentifierImpl
  implements XMLResourceIdentifier
{
  protected String fPublicId;
  protected String fLiteralSystemId;
  protected String fBaseSystemId;
  protected String fExpandedSystemId;
  protected String fNamespace;
  
  public XMLResourceIdentifierImpl() {}
  
  public XMLResourceIdentifierImpl(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    setValues(paramString1, paramString2, paramString3, paramString4, null);
  }
  
  public XMLResourceIdentifierImpl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    setValues(paramString1, paramString2, paramString3, paramString4, paramString5);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    setValues(paramString1, paramString2, paramString3, paramString4, null);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    fPublicId = paramString1;
    fLiteralSystemId = paramString2;
    fBaseSystemId = paramString3;
    fExpandedSystemId = paramString4;
    fNamespace = paramString5;
  }
  
  public void clear()
  {
    fPublicId = null;
    fLiteralSystemId = null;
    fBaseSystemId = null;
    fExpandedSystemId = null;
    fNamespace = null;
  }
  
  public void setPublicId(String paramString)
  {
    fPublicId = paramString;
  }
  
  public void setLiteralSystemId(String paramString)
  {
    fLiteralSystemId = paramString;
  }
  
  public void setBaseSystemId(String paramString)
  {
    fBaseSystemId = paramString;
  }
  
  public void setExpandedSystemId(String paramString)
  {
    fExpandedSystemId = paramString;
  }
  
  public void setNamespace(String paramString)
  {
    fNamespace = paramString;
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public String getLiteralSystemId()
  {
    return fLiteralSystemId;
  }
  
  public String getBaseSystemId()
  {
    return fBaseSystemId;
  }
  
  public String getExpandedSystemId()
  {
    return fExpandedSystemId;
  }
  
  public String getNamespace()
  {
    return fNamespace;
  }
  
  public int hashCode()
  {
    int i = 0;
    if (fPublicId != null) {
      i += fPublicId.hashCode();
    }
    if (fLiteralSystemId != null) {
      i += fLiteralSystemId.hashCode();
    }
    if (fBaseSystemId != null) {
      i += fBaseSystemId.hashCode();
    }
    if (fExpandedSystemId != null) {
      i += fExpandedSystemId.hashCode();
    }
    if (fNamespace != null) {
      i += fNamespace.hashCode();
    }
    return i;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (fPublicId != null) {
      localStringBuffer.append(fPublicId);
    }
    localStringBuffer.append(':');
    if (fLiteralSystemId != null) {
      localStringBuffer.append(fLiteralSystemId);
    }
    localStringBuffer.append(':');
    if (fBaseSystemId != null) {
      localStringBuffer.append(fBaseSystemId);
    }
    localStringBuffer.append(':');
    if (fExpandedSystemId != null) {
      localStringBuffer.append(fExpandedSystemId);
    }
    localStringBuffer.append(':');
    if (fNamespace != null) {
      localStringBuffer.append(fNamespace);
    }
    return localStringBuffer.toString();
  }
}
