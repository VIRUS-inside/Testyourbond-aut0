package org.apache.commons.exec;

import java.io.IOException;








































public class ExecuteException
  extends IOException
{
  private static final long serialVersionUID = 3256443620654331699L;
  private final Throwable cause;
  private final int exitValue;
  
  public ExecuteException(String message, int exitValue)
  {
    super(message + " (Exit value: " + exitValue + ")");
    cause = null;
    this.exitValue = exitValue;
  }
  








  public ExecuteException(String message, int exitValue, Throwable cause)
  {
    super(message + " (Exit value: " + exitValue + ". Caused by " + cause + ")");
    this.cause = cause;
    this.exitValue = exitValue;
  }
  



  public Throwable getCause()
  {
    return cause;
  }
  



  public int getExitValue()
  {
    return exitValue;
  }
}
