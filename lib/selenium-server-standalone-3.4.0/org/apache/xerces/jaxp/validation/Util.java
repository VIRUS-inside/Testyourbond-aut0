package org.apache.xerces.jaxp.validation;

import javax.xml.transform.stream.StreamSource;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

final class Util
{
  Util() {}
  
  public static final XMLInputSource toXMLInputSource(StreamSource paramStreamSource)
  {
    if (paramStreamSource.getReader() != null) {
      return new XMLInputSource(paramStreamSource.getPublicId(), paramStreamSource.getSystemId(), paramStreamSource.getSystemId(), paramStreamSource.getReader(), null);
    }
    if (paramStreamSource.getInputStream() != null) {
      return new XMLInputSource(paramStreamSource.getPublicId(), paramStreamSource.getSystemId(), paramStreamSource.getSystemId(), paramStreamSource.getInputStream(), null);
    }
    return new XMLInputSource(paramStreamSource.getPublicId(), paramStreamSource.getSystemId(), paramStreamSource.getSystemId());
  }
  
  public static SAXException toSAXException(XNIException paramXNIException)
  {
    if ((paramXNIException instanceof XMLParseException)) {
      return toSAXParseException((XMLParseException)paramXNIException);
    }
    if ((paramXNIException.getException() instanceof SAXException)) {
      return (SAXException)paramXNIException.getException();
    }
    return new SAXException(paramXNIException.getMessage(), paramXNIException.getException());
  }
  
  public static SAXParseException toSAXParseException(XMLParseException paramXMLParseException)
  {
    if ((paramXMLParseException.getException() instanceof SAXParseException)) {
      return (SAXParseException)paramXMLParseException.getException();
    }
    return new SAXParseException(paramXMLParseException.getMessage(), paramXMLParseException.getPublicId(), paramXMLParseException.getExpandedSystemId(), paramXMLParseException.getLineNumber(), paramXMLParseException.getColumnNumber(), paramXMLParseException.getException());
  }
}
