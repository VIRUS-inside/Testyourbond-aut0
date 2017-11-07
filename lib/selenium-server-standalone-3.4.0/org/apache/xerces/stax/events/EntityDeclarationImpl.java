package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EntityDeclaration;

public final class EntityDeclarationImpl
  extends XMLEventImpl
  implements EntityDeclaration
{
  private final String fPublicId;
  private final String fSystemId;
  private final String fName;
  private final String fNotationName;
  
  public EntityDeclarationImpl(String paramString1, String paramString2, String paramString3, String paramString4, Location paramLocation)
  {
    super(15, paramLocation);
    fPublicId = paramString1;
    fSystemId = paramString2;
    fName = paramString3;
    fNotationName = paramString4;
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public String getSystemId()
  {
    return fSystemId;
  }
  
  public String getName()
  {
    return fName;
  }
  
  public String getNotationName()
  {
    return fNotationName;
  }
  
  public String getReplacementText()
  {
    return null;
  }
  
  public String getBaseURI()
  {
    return null;
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write("<!ENTITY ");
      paramWriter.write(fName);
      if (fPublicId != null)
      {
        paramWriter.write(" PUBLIC \"");
        paramWriter.write(fPublicId);
        paramWriter.write("\" \"");
        paramWriter.write(fSystemId);
        paramWriter.write(34);
      }
      else
      {
        paramWriter.write(" SYSTEM \"");
        paramWriter.write(fSystemId);
        paramWriter.write(34);
      }
      if (fNotationName != null)
      {
        paramWriter.write(" NDATA ");
        paramWriter.write(fNotationName);
      }
      paramWriter.write(62);
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
