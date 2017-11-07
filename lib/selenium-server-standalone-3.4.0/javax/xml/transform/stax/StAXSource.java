package javax.xml.transform.stax;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;

public class StAXSource
  implements Source
{
  public static final String FEATURE = "http://javax.xml.transform.stax.StAXSource/feature";
  private final XMLStreamReader xmlStreamReader;
  private final XMLEventReader xmlEventReader;
  private final String systemId;
  
  public StAXSource(XMLStreamReader paramXMLStreamReader)
  {
    if (paramXMLStreamReader == null) {
      throw new IllegalArgumentException("XMLStreamReader cannot be null.");
    }
    int i = paramXMLStreamReader.getEventType();
    if ((i != 7) && (i != 1)) {
      throw new IllegalStateException("The state of the XMLStreamReader must be START_DOCUMENT or START_ELEMENT");
    }
    xmlStreamReader = paramXMLStreamReader;
    xmlEventReader = null;
    systemId = paramXMLStreamReader.getLocation().getSystemId();
  }
  
  public StAXSource(XMLEventReader paramXMLEventReader)
    throws XMLStreamException
  {
    if (paramXMLEventReader == null) {
      throw new IllegalArgumentException("XMLEventReader cannot be null.");
    }
    XMLEvent localXMLEvent = paramXMLEventReader.peek();
    if ((!localXMLEvent.isStartDocument()) && (!localXMLEvent.isStartElement())) {
      throw new IllegalStateException("The state of the XMLEventReader must be START_DOCUMENT or START_ELEMENT");
    }
    xmlStreamReader = null;
    xmlEventReader = paramXMLEventReader;
    systemId = localXMLEvent.getLocation().getSystemId();
  }
  
  public XMLStreamReader getXMLStreamReader()
  {
    return xmlStreamReader;
  }
  
  public XMLEventReader getXMLEventReader()
  {
    return xmlEventReader;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
  
  public void setSystemId(String paramString)
  {
    throw new UnsupportedOperationException("Setting systemId is not supported.");
  }
}
