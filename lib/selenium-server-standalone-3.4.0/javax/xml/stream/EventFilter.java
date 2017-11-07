package javax.xml.stream;

import javax.xml.stream.events.XMLEvent;

public abstract interface EventFilter
{
  public abstract boolean accept(XMLEvent paramXMLEvent);
}
