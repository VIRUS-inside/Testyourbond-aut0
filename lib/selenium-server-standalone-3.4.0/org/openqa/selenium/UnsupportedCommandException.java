package org.openqa.selenium;












public class UnsupportedCommandException
  extends WebDriverException
{
  public UnsupportedCommandException() {}
  










  public UnsupportedCommandException(String message)
  {
    super(message);
  }
  
  public UnsupportedCommandException(Throwable cause) {
    super(cause);
  }
  
  public UnsupportedCommandException(String message, Throwable cause) {
    super(message, cause);
  }
}
