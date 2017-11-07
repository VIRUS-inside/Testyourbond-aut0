package org.xml.sax;

public abstract interface ErrorHandler
{
  public abstract void warning(SAXParseException paramSAXParseException)
    throws SAXException;
  
  public abstract void error(SAXParseException paramSAXParseException)
    throws SAXException;
  
  public abstract void fatalError(SAXParseException paramSAXParseException)
    throws SAXException;
}
