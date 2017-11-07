package org.xml.sax.ext;

import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

public class Locator2Impl
  extends LocatorImpl
  implements Locator2
{
  private String encoding;
  private String version;
  
  public Locator2Impl() {}
  
  public Locator2Impl(Locator paramLocator)
  {
    super(paramLocator);
    if ((paramLocator instanceof Locator2))
    {
      Locator2 localLocator2 = (Locator2)paramLocator;
      version = localLocator2.getXMLVersion();
      encoding = localLocator2.getEncoding();
    }
  }
  
  public String getXMLVersion()
  {
    return version;
  }
  
  public String getEncoding()
  {
    return encoding;
  }
  
  public void setXMLVersion(String paramString)
  {
    version = paramString;
  }
  
  public void setEncoding(String paramString)
  {
    encoding = paramString;
  }
}
