package javax.xml.stream.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public abstract interface XMLEventConsumer
{
  public abstract void add(XMLEvent paramXMLEvent)
    throws XMLStreamException;
}
