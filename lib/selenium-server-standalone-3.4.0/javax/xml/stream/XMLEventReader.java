package javax.xml.stream;

import java.util.Iterator;
import javax.xml.stream.events.XMLEvent;

public abstract interface XMLEventReader
  extends Iterator
{
  public abstract void close()
    throws XMLStreamException;
  
  public abstract String getElementText()
    throws XMLStreamException;
  
  public abstract Object getProperty(String paramString)
    throws IllegalArgumentException;
  
  public abstract boolean hasNext();
  
  public abstract XMLEvent nextEvent()
    throws XMLStreamException;
  
  public abstract XMLEvent nextTag()
    throws XMLStreamException;
  
  public abstract XMLEvent peek()
    throws XMLStreamException;
}
