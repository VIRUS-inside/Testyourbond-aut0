package org.openqa.selenium;


















public class SessionNotCreatedException
  extends WebDriverException
{
  public SessionNotCreatedException(String msg)
  {
    super(msg);
  }
  
  public SessionNotCreatedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
