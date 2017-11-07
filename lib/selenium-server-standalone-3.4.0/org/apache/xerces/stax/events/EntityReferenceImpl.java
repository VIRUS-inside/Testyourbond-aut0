package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;

public final class EntityReferenceImpl
  extends XMLEventImpl
  implements EntityReference
{
  private final String fName;
  private final EntityDeclaration fDecl;
  
  public EntityReferenceImpl(EntityDeclaration paramEntityDeclaration, Location paramLocation)
  {
    this(paramEntityDeclaration != null ? paramEntityDeclaration.getName() : "", paramEntityDeclaration, paramLocation);
  }
  
  public EntityReferenceImpl(String paramString, EntityDeclaration paramEntityDeclaration, Location paramLocation)
  {
    super(9, paramLocation);
    fName = (paramString != null ? paramString : "");
    fDecl = paramEntityDeclaration;
  }
  
  public EntityDeclaration getDeclaration()
  {
    return fDecl;
  }
  
  public String getName()
  {
    return fName;
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write(38);
      paramWriter.write(fName);
      paramWriter.write(59);
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
