package org.xml.sax.ext;

import org.xml.sax.Locator;

public abstract interface Locator2
  extends Locator
{
  public abstract String getXMLVersion();
  
  public abstract String getEncoding();
}
