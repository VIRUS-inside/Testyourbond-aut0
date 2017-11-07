package javax.xml.stream.util;

import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class EventReaderDelegate
  implements XMLEventReader
{
  private XMLEventReader reader;
  
  public EventReaderDelegate() {}
  
  public EventReaderDelegate(XMLEventReader paramXMLEventReader)
  {
    reader = paramXMLEventReader;
  }
  
  public void setParent(XMLEventReader paramXMLEventReader)
  {
    reader = paramXMLEventReader;
  }
  
  public XMLEventReader getParent()
  {
    return reader;
  }
  
  public void close()
    throws XMLStreamException
  {
    reader.close();
  }
  
  public String getElementText()
    throws XMLStreamException
  {
    return reader.getElementText();
  }
  
  public Object getProperty(String paramString)
    throws IllegalArgumentException
  {
    return reader.getProperty(paramString);
  }
  
  public boolean hasNext()
  {
    return reader.hasNext();
  }
  
  public Object next()
  {
    return reader.next();
  }
  
  public XMLEvent nextEvent()
    throws XMLStreamException
  {
    return reader.nextEvent();
  }
  
  public XMLEvent nextTag()
    throws XMLStreamException
  {
    return reader.nextTag();
  }
  
  public XMLEvent peek()
    throws XMLStreamException
  {
    return reader.peek();
  }
  
  public void remove()
  {
    reader.remove();
  }
}
