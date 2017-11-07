package org.openqa.selenium;


















public class NoAlertPresentException
  extends NotFoundException
{
  public NoAlertPresentException()
  {
    this("No alert was present");
  }
  
  public NoAlertPresentException(String message) {
    super(message);
  }
  
  public NoAlertPresentException(Throwable cause) {
    super(cause);
  }
  
  public NoAlertPresentException(String message, Throwable cause) {
    super(message, cause);
  }
}
