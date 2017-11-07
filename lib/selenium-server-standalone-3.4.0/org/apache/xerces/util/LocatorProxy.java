package org.apache.xerces.util;

import org.apache.xerces.xni.XMLLocator;
import org.xml.sax.ext.Locator2;

public class LocatorProxy
  implements Locator2
{
  private final XMLLocator fLocator;
  
  public LocatorProxy(XMLLocator paramXMLLocator)
  {
    fLocator = paramXMLLocator;
  }
  
  public String getPublicId()
  {
    return fLocator.getPublicId();
  }
  
  public String getSystemId()
  {
    return fLocator.getExpandedSystemId();
  }
  
  public int getLineNumber()
  {
    return fLocator.getLineNumber();
  }
  
  public int getColumnNumber()
  {
    return fLocator.getColumnNumber();
  }
  
  public String getXMLVersion()
  {
    return fLocator.getXMLVersion();
  }
  
  public String getEncoding()
  {
    return fLocator.getEncoding();
  }
}
