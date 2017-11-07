package org.apache.xerces.stax.events;

import java.io.StringWriter;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.xerces.stax.EmptyLocation;
import org.apache.xerces.stax.ImmutableLocation;

abstract class XMLEventImpl
  implements XMLEvent
{
  private int fEventType;
  private Location fLocation;
  
  XMLEventImpl(int paramInt, Location paramLocation)
  {
    fEventType = paramInt;
    if (paramLocation != null) {
      fLocation = new ImmutableLocation(paramLocation);
    } else {
      fLocation = EmptyLocation.getInstance();
    }
  }
  
  public final int getEventType()
  {
    return fEventType;
  }
  
  public final Location getLocation()
  {
    return fLocation;
  }
  
  public final boolean isStartElement()
  {
    return 1 == fEventType;
  }
  
  public final boolean isAttribute()
  {
    return 10 == fEventType;
  }
  
  public final boolean isNamespace()
  {
    return 13 == fEventType;
  }
  
  public final boolean isEndElement()
  {
    return 2 == fEventType;
  }
  
  public final boolean isEntityReference()
  {
    return 9 == fEventType;
  }
  
  public final boolean isProcessingInstruction()
  {
    return 3 == fEventType;
  }
  
  public final boolean isCharacters()
  {
    return (4 == fEventType) || (12 == fEventType) || (6 == fEventType);
  }
  
  public final boolean isStartDocument()
  {
    return 7 == fEventType;
  }
  
  public final boolean isEndDocument()
  {
    return 8 == fEventType;
  }
  
  public final StartElement asStartElement()
  {
    return (StartElement)this;
  }
  
  public final EndElement asEndElement()
  {
    return (EndElement)this;
  }
  
  public final Characters asCharacters()
  {
    return (Characters)this;
  }
  
  public final QName getSchemaType()
  {
    return null;
  }
  
  public final String toString()
  {
    StringWriter localStringWriter = new StringWriter();
    try
    {
      writeAsEncodedUnicode(localStringWriter);
    }
    catch (XMLStreamException localXMLStreamException) {}
    return localStringWriter.toString();
  }
  
  public abstract void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException;
}
