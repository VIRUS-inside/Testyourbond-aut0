package javax.xml.stream.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public abstract interface XMLEventAllocator
{
  public abstract XMLEvent allocate(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;
  
  public abstract void allocate(XMLStreamReader paramXMLStreamReader, XMLEventConsumer paramXMLEventConsumer)
    throws XMLStreamException;
  
  public abstract XMLEventAllocator newInstance();
}
