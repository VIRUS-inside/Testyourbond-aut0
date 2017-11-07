package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

public abstract interface XMLStreamReader
  extends XMLStreamConstants
{
  public abstract void close()
    throws XMLStreamException;
  
  public abstract int getAttributeCount();
  
  public abstract String getAttributeLocalName(int paramInt);
  
  public abstract QName getAttributeName(int paramInt);
  
  public abstract String getAttributeNamespace(int paramInt);
  
  public abstract String getAttributePrefix(int paramInt);
  
  public abstract String getAttributeType(int paramInt);
  
  public abstract String getAttributeValue(int paramInt);
  
  public abstract String getAttributeValue(String paramString1, String paramString2);
  
  public abstract String getCharacterEncodingScheme();
  
  public abstract String getElementText()
    throws XMLStreamException;
  
  public abstract String getEncoding();
  
  public abstract int getEventType();
  
  public abstract String getLocalName();
  
  public abstract Location getLocation();
  
  public abstract QName getName();
  
  public abstract NamespaceContext getNamespaceContext();
  
  public abstract int getNamespaceCount();
  
  public abstract String getNamespacePrefix(int paramInt);
  
  public abstract String getNamespaceURI();
  
  public abstract String getNamespaceURI(int paramInt);
  
  public abstract String getNamespaceURI(String paramString);
  
  public abstract String getPIData();
  
  public abstract String getPITarget();
  
  public abstract String getPrefix();
  
  public abstract Object getProperty(String paramString)
    throws IllegalArgumentException;
  
  public abstract String getText();
  
  public abstract char[] getTextCharacters();
  
  public abstract int getTextCharacters(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
    throws XMLStreamException;
  
  public abstract int getTextLength();
  
  public abstract int getTextStart();
  
  public abstract String getVersion();
  
  public abstract boolean hasName();
  
  public abstract boolean hasNext()
    throws XMLStreamException;
  
  public abstract boolean hasText();
  
  public abstract boolean isAttributeSpecified(int paramInt);
  
  public abstract boolean isCharacters();
  
  public abstract boolean isEndElement();
  
  public abstract boolean isStandalone();
  
  public abstract boolean isStartElement();
  
  public abstract boolean isWhiteSpace();
  
  public abstract int next()
    throws XMLStreamException;
  
  public abstract int nextTag()
    throws XMLStreamException;
  
  public abstract void require(int paramInt, String paramString1, String paramString2)
    throws XMLStreamException;
  
  public abstract boolean standaloneSet();
}
