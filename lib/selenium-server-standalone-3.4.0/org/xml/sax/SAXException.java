package org.xml.sax;

public class SAXException
  extends Exception
{
  private Exception exception;
  static final long serialVersionUID = 583241635256073760L;
  
  public SAXException()
  {
    exception = null;
  }
  
  public SAXException(String paramString)
  {
    super(paramString);
    exception = null;
  }
  
  public SAXException(Exception paramException)
  {
    exception = paramException;
  }
  
  public SAXException(String paramString, Exception paramException)
  {
    super(paramString);
    exception = paramException;
  }
  
  public String getMessage()
  {
    String str = super.getMessage();
    if ((str == null) && (exception != null)) {
      return exception.getMessage();
    }
    return str;
  }
  
  public Exception getException()
  {
    return exception;
  }
  
  public String toString()
  {
    if (exception != null) {
      return exception.toString();
    }
    return super.toString();
  }
}
