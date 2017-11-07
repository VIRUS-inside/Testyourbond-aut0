package org.openqa.selenium;









public class NotFoundException
  extends WebDriverException
{
  public NotFoundException() {}
  








  public NotFoundException(String message)
  {
    super(message);
  }
  
  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public NotFoundException(Throwable cause) {
    super(cause);
  }
}
