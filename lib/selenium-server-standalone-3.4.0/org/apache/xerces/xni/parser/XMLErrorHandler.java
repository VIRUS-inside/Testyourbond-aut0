package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XNIException;

public abstract interface XMLErrorHandler
{
  public abstract void warning(String paramString1, String paramString2, XMLParseException paramXMLParseException)
    throws XNIException;
  
  public abstract void error(String paramString1, String paramString2, XMLParseException paramXMLParseException)
    throws XNIException;
  
  public abstract void fatalError(String paramString1, String paramString2, XMLParseException paramXMLParseException)
    throws XNIException;
}
