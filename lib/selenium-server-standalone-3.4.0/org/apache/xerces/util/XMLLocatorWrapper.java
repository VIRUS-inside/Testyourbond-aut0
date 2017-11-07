package org.apache.xerces.util;

import org.apache.xerces.xni.XMLLocator;

public final class XMLLocatorWrapper
  implements XMLLocator
{
  private XMLLocator fLocator = null;
  
  public XMLLocatorWrapper() {}
  
  public void setLocator(XMLLocator paramXMLLocator)
  {
    fLocator = paramXMLLocator;
  }
  
  public XMLLocator getLocator()
  {
    return fLocator;
  }
  
  public String getPublicId()
  {
    if (fLocator != null) {
      return fLocator.getPublicId();
    }
    return null;
  }
  
  public String getLiteralSystemId()
  {
    if (fLocator != null) {
      return fLocator.getLiteralSystemId();
    }
    return null;
  }
  
  public String getBaseSystemId()
  {
    if (fLocator != null) {
      return fLocator.getBaseSystemId();
    }
    return null;
  }
  
  public String getExpandedSystemId()
  {
    if (fLocator != null) {
      return fLocator.getExpandedSystemId();
    }
    return null;
  }
  
  public int getLineNumber()
  {
    if (fLocator != null) {
      return fLocator.getLineNumber();
    }
    return -1;
  }
  
  public int getColumnNumber()
  {
    if (fLocator != null) {
      return fLocator.getColumnNumber();
    }
    return -1;
  }
  
  public int getCharacterOffset()
  {
    if (fLocator != null) {
      return fLocator.getCharacterOffset();
    }
    return -1;
  }
  
  public String getEncoding()
  {
    if (fLocator != null) {
      return fLocator.getEncoding();
    }
    return null;
  }
  
  public String getXMLVersion()
  {
    if (fLocator != null) {
      return fLocator.getXMLVersion();
    }
    return null;
  }
}
