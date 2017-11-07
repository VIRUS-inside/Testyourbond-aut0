package org.apache.xml.serializer.dom3;

import org.w3c.dom.DOMError;
import org.w3c.dom.DOMLocator;
































final class DOMErrorImpl
  implements DOMError
{
  private short fSeverity = 1;
  

  private String fMessage = null;
  

  private String fType;
  

  private Exception fException = null;
  

  private Object fRelatedData;
  

  private DOMLocatorImpl fLocation = new DOMLocatorImpl();
  







  DOMErrorImpl() {}
  






  DOMErrorImpl(short severity, String message, String type)
  {
    fSeverity = severity;
    fMessage = message;
    fType = type;
  }
  






  DOMErrorImpl(short severity, String message, String type, Exception exception)
  {
    fSeverity = severity;
    fMessage = message;
    fType = type;
    fException = exception;
  }
  








  DOMErrorImpl(short severity, String message, String type, Exception exception, Object relatedData, DOMLocatorImpl location)
  {
    fSeverity = severity;
    fMessage = message;
    fType = type;
    fException = exception;
    fRelatedData = relatedData;
    fLocation = location;
  }
  






  public short getSeverity()
  {
    return fSeverity;
  }
  




  public String getMessage()
  {
    return fMessage;
  }
  




  public DOMLocator getLocation()
  {
    return fLocation;
  }
  




  public Object getRelatedException()
  {
    return fException;
  }
  




  public String getType()
  {
    return fType;
  }
  




  public Object getRelatedData()
  {
    return fRelatedData;
  }
  
  public void reset() {
    fSeverity = 1;
    fException = null;
    fMessage = null;
    fType = null;
    fRelatedData = null;
    fLocation = null;
  }
}
