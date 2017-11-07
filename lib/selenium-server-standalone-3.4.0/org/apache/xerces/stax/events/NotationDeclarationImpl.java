package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.NotationDeclaration;

public final class NotationDeclarationImpl
  extends XMLEventImpl
  implements NotationDeclaration
{
  private final String fSystemId;
  private final String fPublicId;
  private final String fName;
  
  public NotationDeclarationImpl(String paramString1, String paramString2, String paramString3, Location paramLocation)
  {
    super(14, paramLocation);
    fName = paramString1;
    fPublicId = paramString2;
    fSystemId = paramString3;
  }
  
  public String getName()
  {
    return fName;
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public String getSystemId()
  {
    return fSystemId;
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write("<!NOTATION ");
      if (fPublicId != null)
      {
        paramWriter.write("PUBLIC \"");
        paramWriter.write(fPublicId);
        paramWriter.write(34);
        if (fSystemId != null)
        {
          paramWriter.write(" \"");
          paramWriter.write(fSystemId);
          paramWriter.write(34);
        }
      }
      else
      {
        paramWriter.write("SYSTEM \"");
        paramWriter.write(fSystemId);
        paramWriter.write(34);
      }
      paramWriter.write(fName);
      paramWriter.write(62);
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
