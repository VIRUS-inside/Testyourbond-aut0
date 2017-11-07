package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import org.apache.xerces.util.XMLChar;

public final class CharactersImpl
  extends XMLEventImpl
  implements Characters
{
  private final String fData;
  
  public CharactersImpl(String paramString, int paramInt, Location paramLocation)
  {
    super(paramInt, paramLocation);
    fData = (paramString != null ? paramString : "");
  }
  
  public String getData()
  {
    return fData;
  }
  
  public boolean isWhiteSpace()
  {
    int i = fData != null ? fData.length() : 0;
    if (i == 0) {
      return false;
    }
    for (int j = 0; j < i; j++) {
      if (!XMLChar.isSpace(fData.charAt(j))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isCData()
  {
    return 12 == getEventType();
  }
  
  public boolean isIgnorableWhiteSpace()
  {
    return 6 == getEventType();
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write(fData);
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
