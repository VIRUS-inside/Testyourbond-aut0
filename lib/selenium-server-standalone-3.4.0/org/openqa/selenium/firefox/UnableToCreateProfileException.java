package org.openqa.selenium.firefox;

import org.openqa.selenium.WebDriverException;















public class UnableToCreateProfileException
  extends WebDriverException
{
  public UnableToCreateProfileException(Throwable e)
  {
    super(e);
  }
  
  public UnableToCreateProfileException(String message) {
    super(message);
  }
}
