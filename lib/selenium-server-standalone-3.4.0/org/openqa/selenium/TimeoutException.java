package org.openqa.selenium;











public class TimeoutException
  extends WebDriverException
{
  public TimeoutException() {}
  










  public TimeoutException(String message)
  {
    super(message);
  }
  
  public TimeoutException(Throwable cause) {
    super(cause);
  }
  
  public TimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}
