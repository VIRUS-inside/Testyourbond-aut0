package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Comment;

public final class CommentImpl
  extends XMLEventImpl
  implements Comment
{
  private final String fText;
  
  public CommentImpl(String paramString, Location paramLocation)
  {
    super(5, paramLocation);
    fText = (paramString != null ? paramString : "");
  }
  
  public String getText()
  {
    return fText;
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write("<!--");
      paramWriter.write(fText);
      paramWriter.write("-->");
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
