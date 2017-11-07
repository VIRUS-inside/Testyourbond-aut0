package org.openqa.selenium;











public class NoSuchSessionException
  extends WebDriverException
{
  public NoSuchSessionException() {}
  









  public NoSuchSessionException(String reason)
  {
    super(reason);
  }
  
  public NoSuchSessionException(String reason, Throwable cause) {
    super(reason, cause);
  }
}
