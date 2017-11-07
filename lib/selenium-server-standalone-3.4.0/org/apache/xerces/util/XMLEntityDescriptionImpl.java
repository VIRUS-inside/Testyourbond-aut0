package org.apache.xerces.util;

import org.apache.xerces.impl.XMLEntityDescription;

public class XMLEntityDescriptionImpl
  extends XMLResourceIdentifierImpl
  implements XMLEntityDescription
{
  protected String fEntityName;
  
  public XMLEntityDescriptionImpl() {}
  
  public XMLEntityDescriptionImpl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    setDescription(paramString1, paramString2, paramString3, paramString4, paramString5);
  }
  
  public XMLEntityDescriptionImpl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    setDescription(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
  }
  
  public void setEntityName(String paramString)
  {
    fEntityName = paramString;
  }
  
  public String getEntityName()
  {
    return fEntityName;
  }
  
  public void setDescription(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    setDescription(paramString1, paramString2, paramString3, paramString4, paramString5, null);
  }
  
  public void setDescription(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    fEntityName = paramString1;
    setValues(paramString2, paramString3, paramString4, paramString5, paramString6);
  }
  
  public void clear()
  {
    super.clear();
    fEntityName = null;
  }
  
  public int hashCode()
  {
    int i = super.hashCode();
    if (fEntityName != null) {
      i += fEntityName.hashCode();
    }
    return i;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (fEntityName != null) {
      localStringBuffer.append(fEntityName);
    }
    localStringBuffer.append(':');
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
