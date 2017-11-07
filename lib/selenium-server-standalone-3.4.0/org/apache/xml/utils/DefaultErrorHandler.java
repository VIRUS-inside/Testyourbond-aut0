package org.apache.xml.utils;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.res.XMLMessages;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



































public class DefaultErrorHandler
  implements ErrorHandler, ErrorListener
{
  PrintWriter m_pw;
  boolean m_throwExceptionOnError = true;
  



  public DefaultErrorHandler(PrintWriter pw)
  {
    m_pw = pw;
  }
  



  public DefaultErrorHandler(PrintStream pw)
  {
    m_pw = new PrintWriter(pw, true);
  }
  



  public DefaultErrorHandler()
  {
    this(true);
  }
  




  public DefaultErrorHandler(boolean throwExceptionOnError)
  {
    m_throwExceptionOnError = throwExceptionOnError;
  }
  







  public PrintWriter getErrorWriter()
  {
    if (m_pw == null) {
      m_pw = new PrintWriter(System.err, true);
    }
    return m_pw;
  }
  















  public void warning(SAXParseException exception)
    throws SAXException
  {
    PrintWriter pw = getErrorWriter();
    
    printLocation(pw, exception);
    pw.println("Parser warning: " + exception.getMessage());
  }
  























  public void error(SAXParseException exception)
    throws SAXException
  {
    throw exception;
  }
  





















  public void fatalError(SAXParseException exception)
    throws SAXException
  {
    throw exception;
  }
  
















  public void warning(TransformerException exception)
    throws TransformerException
  {
    PrintWriter pw = getErrorWriter();
    
    printLocation(pw, exception);
    pw.println(exception.getMessage());
  }
  























  public void error(TransformerException exception)
    throws TransformerException
  {
    if (m_throwExceptionOnError) {
      throw exception;
    }
    
    PrintWriter pw = getErrorWriter();
    
    printLocation(pw, exception);
    pw.println(exception.getMessage());
  }
  






















  public void fatalError(TransformerException exception)
    throws TransformerException
  {
    if (m_throwExceptionOnError) {
      throw exception;
    }
    
    PrintWriter pw = getErrorWriter();
    
    printLocation(pw, exception);
    pw.println(exception.getMessage());
  }
  


  public static void ensureLocationSet(TransformerException exception)
  {
    SourceLocator locator = null;
    Throwable cause = exception;
    

    do
    {
      if ((cause instanceof SAXParseException))
      {
        locator = new SAXSourceLocator((SAXParseException)cause);
      }
      else if ((cause instanceof TransformerException))
      {
        SourceLocator causeLocator = ((TransformerException)cause).getLocator();
        if (null != causeLocator) {
          locator = causeLocator;
        }
      }
      if ((cause instanceof TransformerException)) {
        cause = ((TransformerException)cause).getCause();
      } else if ((cause instanceof SAXException)) {
        cause = ((SAXException)cause).getException();
      } else {
        cause = null;
      }
    } while (null != cause);
    
    exception.setLocator(locator);
  }
  
  public static void printLocation(PrintStream pw, TransformerException exception)
  {
    printLocation(new PrintWriter(pw), exception);
  }
  
  public static void printLocation(PrintStream pw, SAXParseException exception)
  {
    printLocation(new PrintWriter(pw), exception);
  }
  
  public static void printLocation(PrintWriter pw, Throwable exception)
  {
    SourceLocator locator = null;
    Throwable cause = exception;
    

    do
    {
      if ((cause instanceof SAXParseException))
      {
        locator = new SAXSourceLocator((SAXParseException)cause);
      }
      else if ((cause instanceof TransformerException))
      {
        SourceLocator causeLocator = ((TransformerException)cause).getLocator();
        if (null != causeLocator)
          locator = causeLocator;
      }
      if ((cause instanceof TransformerException)) {
        cause = ((TransformerException)cause).getCause();
      } else if ((cause instanceof WrappedRuntimeException)) {
        cause = ((WrappedRuntimeException)cause).getException();
      } else if ((cause instanceof SAXException)) {
        cause = ((SAXException)cause).getException();
      } else {
        cause = null;
      }
    } while (null != cause);
    
    if (null != locator)
    {

      String id = null != locator.getSystemId() ? locator.getSystemId() : null != locator.getPublicId() ? locator.getPublicId() : XMLMessages.createXMLMessage("ER_SYSTEMID_UNKNOWN", null);
      



      pw.print(id + "; " + XMLMessages.createXMLMessage("line", null) + locator.getLineNumber() + "; " + XMLMessages.createXMLMessage("column", null) + locator.getColumnNumber() + "; ");
    }
    else
    {
      pw.print("(" + XMLMessages.createXMLMessage("ER_LOCATION_UNKNOWN", null) + ")");
    }
  }
}
