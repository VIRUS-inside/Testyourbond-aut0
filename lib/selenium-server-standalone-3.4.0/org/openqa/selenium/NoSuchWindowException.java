package org.openqa.selenium;




















public class NoSuchWindowException
  extends NotFoundException
{
  public NoSuchWindowException(String reason)
  {
    super(reason);
  }
  
  public NoSuchWindowException(String reason, Throwable cause) {
    super(reason, cause);
  }
}
