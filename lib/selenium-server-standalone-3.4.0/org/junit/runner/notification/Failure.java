package org.junit.runner.notification;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import org.junit.runner.Description;




















public class Failure
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Description fDescription;
  private final Throwable fThrownException;
  
  public Failure(Description description, Throwable thrownException)
  {
    fThrownException = thrownException;
    fDescription = description;
  }
  


  public String getTestHeader()
  {
    return fDescription.getDisplayName();
  }
  


  public Description getDescription()
  {
    return fDescription;
  }
  



  public Throwable getException()
  {
    return fThrownException;
  }
  
  public String toString()
  {
    return getTestHeader() + ": " + fThrownException.getMessage();
  }
  




  public String getTrace()
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    getException().printStackTrace(writer);
    return stringWriter.toString();
  }
  




  public String getMessage()
  {
    return getException().getMessage();
  }
}
