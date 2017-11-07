package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;

public final class StartDocumentImpl
  extends XMLEventImpl
  implements StartDocument
{
  private final String fCharEncoding;
  private final boolean fEncodingSet;
  private final String fVersion;
  private final boolean fIsStandalone;
  private final boolean fStandaloneSet;
  
  public StartDocumentImpl(String paramString1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString2, Location paramLocation)
  {
    super(7, paramLocation);
    fCharEncoding = paramString1;
    fEncodingSet = paramBoolean1;
    fIsStandalone = paramBoolean2;
    fStandaloneSet = paramBoolean3;
    fVersion = paramString2;
  }
  
  public String getSystemId()
  {
    return getLocation().getSystemId();
  }
  
  public String getCharacterEncodingScheme()
  {
    return fCharEncoding;
  }
  
  public boolean encodingSet()
  {
    return fEncodingSet;
  }
  
  public boolean isStandalone()
  {
    return fIsStandalone;
  }
  
  public boolean standaloneSet()
  {
    return fStandaloneSet;
  }
  
  public String getVersion()
  {
    return fVersion;
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write("<?xml version=\"");
      paramWriter.write((fVersion != null) && (fVersion.length() > 0) ? fVersion : "1.0");
      paramWriter.write(34);
      if (encodingSet())
      {
        paramWriter.write(" encoding=\"");
        paramWriter.write(fCharEncoding);
        paramWriter.write(34);
      }
      if (standaloneSet())
      {
        paramWriter.write(" standalone=\"");
        paramWriter.write(fIsStandalone ? "yes" : "no");
        paramWriter.write(34);
      }
      paramWriter.write("?>");
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
