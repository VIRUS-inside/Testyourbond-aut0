package org.openqa.selenium;











public class ScriptTimeoutException
  extends WebDriverException
{
  public ScriptTimeoutException() {}
  










  public ScriptTimeoutException(String message)
  {
    super(message);
  }
  
  public ScriptTimeoutException(Throwable cause) {
    super(cause);
  }
  
  public ScriptTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}
