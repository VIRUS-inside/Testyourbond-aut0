package javax.xml.stream.util;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StreamReaderDelegate
  implements XMLStreamReader
{
  private XMLStreamReader reader;
  
  public StreamReaderDelegate() {}
  
  public StreamReaderDelegate(XMLStreamReader paramXMLStreamReader)
  {
    reader = paramXMLStreamReader;
  }
  
  public void setParent(XMLStreamReader paramXMLStreamReader)
  {
    reader = paramXMLStreamReader;
  }
  
  public XMLStreamReader getParent()
  {
    return reader;
  }
  
  public int next()
    throws XMLStreamException
  {
    return reader.next();
  }
  
  public int nextTag()
    throws XMLStreamException
  {
    return reader.nextTag();
  }
  
  public String getElementText()
    throws XMLStreamException
  {
    return reader.getElementText();
  }
  
  public void require(int paramInt, String paramString1, String paramString2)
    throws XMLStreamException
  {
    reader.require(paramInt, paramString1, paramString2);
  }
  
  public boolean hasNext()
    throws XMLStreamException
  {
    return reader.hasNext();
  }
  
  public void close()
    throws XMLStreamException
  {
    reader.close();
  }
  
  public String getNamespaceURI(String paramString)
  {
    return reader.getNamespaceURI(paramString);
  }
  
  public NamespaceContext getNamespaceContext()
  {
    return reader.getNamespaceContext();
  }
  
  public boolean isStartElement()
  {
    return reader.isStartElement();
  }
  
  public boolean isEndElement()
  {
    return reader.isEndElement();
  }
  
  public boolean isCharacters()
  {
    return reader.isCharacters();
  }
  
  public boolean isWhiteSpace()
  {
    return reader.isWhiteSpace();
  }
  
  public String getAttributeValue(String paramString1, String paramString2)
  {
    return reader.getAttributeValue(paramString1, paramString2);
  }
  
  public int getAttributeCount()
  {
    return reader.getAttributeCount();
  }
  
  public QName getAttributeName(int paramInt)
  {
    return reader.getAttributeName(paramInt);
  }
  
  public String getAttributePrefix(int paramInt)
  {
    return reader.getAttributePrefix(paramInt);
  }
  
  public String getAttributeNamespace(int paramInt)
  {
    return reader.getAttributeNamespace(paramInt);
  }
  
  public String getAttributeLocalName(int paramInt)
  {
    return reader.getAttributeLocalName(paramInt);
  }
  
  public String getAttributeType(int paramInt)
  {
    return reader.getAttributeType(paramInt);
  }
  
  public String getAttributeValue(int paramInt)
  {
    return reader.getAttributeValue(paramInt);
  }
  
  public boolean isAttributeSpecified(int paramInt)
  {
    return reader.isAttributeSpecified(paramInt);
  }
  
  public int getNamespaceCount()
  {
    return reader.getNamespaceCount();
  }
  
  public String getNamespacePrefix(int paramInt)
  {
    return reader.getNamespacePrefix(paramInt);
  }
  
  public String getNamespaceURI(int paramInt)
  {
    return reader.getNamespaceURI(paramInt);
  }
  
  public int getEventType()
  {
    return reader.getEventType();
  }
  
  public String getText()
  {
    return reader.getText();
  }
  
  public int getTextCharacters(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
    throws XMLStreamException
  {
    return reader.getTextCharacters(paramInt1, paramArrayOfChar, paramInt2, paramInt3);
  }
  
  public char[] getTextCharacters()
  {
    return reader.getTextCharacters();
  }
  
  public int getTextStart()
  {
    return reader.getTextStart();
  }
  
  public int getTextLength()
  {
    return reader.getTextLength();
  }
  
  public String getEncoding()
  {
    return reader.getEncoding();
  }
  
  public boolean hasText()
  {
    return reader.hasText();
  }
  
  public Location getLocation()
  {
    return reader.getLocation();
  }
  
  public QName getName()
  {
    return reader.getName();
  }
  
  public String getLocalName()
  {
    return reader.getLocalName();
  }
  
  public boolean hasName()
  {
    return reader.hasName();
  }
  
  public String getNamespaceURI()
  {
    return reader.getNamespaceURI();
  }
  
  public String getPrefix()
  {
    return reader.getPrefix();
  }
  
  public String getVersion()
  {
    return reader.getVersion();
  }
  
  public boolean isStandalone()
  {
    return reader.isStandalone();
  }
  
  public boolean standaloneSet()
  {
    return reader.standaloneSet();
  }
  
  public String getCharacterEncodingScheme()
  {
    return reader.getCharacterEncodingScheme();
  }
  
  public String getPITarget()
  {
    return reader.getPITarget();
  }
  
  public String getPIData()
  {
    return reader.getPIData();
  }
  
  public Object getProperty(String paramString)
    throws IllegalArgumentException
  {
    return reader.getProperty(paramString);
  }
}
