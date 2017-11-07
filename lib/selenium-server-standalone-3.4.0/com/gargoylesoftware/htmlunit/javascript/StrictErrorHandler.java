package com.gargoylesoftware.htmlunit.javascript;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;






















public class StrictErrorHandler
  implements ErrorHandler
{
  public StrictErrorHandler() {}
  
  public void warning(SAXParseException rethrow)
    throws SAXParseException
  {
    throw rethrow;
  }
  





  public void error(SAXParseException rethrow)
    throws SAXParseException
  {
    throw rethrow;
  }
  





  public void fatalError(SAXParseException rethrow)
    throws SAXParseException
  {
    throw rethrow;
  }
}
