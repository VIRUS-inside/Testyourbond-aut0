package org.xml.sax;

import java.io.IOException;

public abstract interface EntityResolver
{
  public abstract InputSource resolveEntity(String paramString1, String paramString2)
    throws SAXException, IOException;
}
