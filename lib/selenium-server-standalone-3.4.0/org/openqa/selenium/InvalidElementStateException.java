package org.openqa.selenium;












public class InvalidElementStateException
  extends WebDriverException
{
  public InvalidElementStateException() {}
  










  public InvalidElementStateException(String message)
  {
    super(message);
  }
  
  public InvalidElementStateException(Throwable cause) {
    super(cause);
  }
  
  public InvalidElementStateException(String message, Throwable cause) {
    super(message, cause);
  }
}
