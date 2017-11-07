package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.ProcessingInstruction;

public final class ProcessingInstructionImpl
  extends XMLEventImpl
  implements ProcessingInstruction
{
  private final String fTarget;
  private final String fData;
  
  public ProcessingInstructionImpl(String paramString1, String paramString2, Location paramLocation)
  {
    super(3, paramLocation);
    fTarget = (paramString1 != null ? paramString1 : "");
    fData = paramString2;
  }
  
  public String getTarget()
  {
    return fTarget;
  }
  
  public String getData()
  {
    return fData;
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write("<?");
      paramWriter.write(fTarget);
      if ((fData != null) && (fData.length() > 0))
      {
        paramWriter.write(32);
        paramWriter.write(fData);
      }
      paramWriter.write("?>");
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
