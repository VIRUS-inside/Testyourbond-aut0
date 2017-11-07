package org.openqa.selenium;















public class InvalidArgumentException
  extends WebDriverException
{
  public InvalidArgumentException(String message)
  {
    super(message);
  }
  
  public InvalidArgumentException(String message, Throwable cause) { super(message, cause); }
}
