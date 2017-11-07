package javax.xml.transform.stax;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

public class StAXResult
  implements Result
{
  public static final String FEATURE = "http://javax.xml.transform.stax.StAXResult/feature";
  private final XMLStreamWriter xmlStreamWriter;
  private final XMLEventWriter xmlEventWriter;
  
  public StAXResult(XMLStreamWriter paramXMLStreamWriter)
  {
    if (paramXMLStreamWriter == null) {
      throw new IllegalArgumentException("XMLStreamWriter cannot be null.");
    }
    xmlStreamWriter = paramXMLStreamWriter;
    xmlEventWriter = null;
  }
  
  public StAXResult(XMLEventWriter paramXMLEventWriter)
  {
    if (paramXMLEventWriter == null) {
      throw new IllegalArgumentException("XMLEventWriter cannot be null.");
    }
    xmlStreamWriter = null;
    xmlEventWriter = paramXMLEventWriter;
  }
  
  public XMLStreamWriter getXMLStreamWriter()
  {
    return xmlStreamWriter;
  }
  
  public XMLEventWriter getXMLEventWriter()
  {
    return xmlEventWriter;
  }
  
  public String getSystemId()
  {
    return null;
  }
  
  public void setSystemId(String paramString)
  {
    throw new UnsupportedOperationException("Setting systemId is not supported.");
  }
}
