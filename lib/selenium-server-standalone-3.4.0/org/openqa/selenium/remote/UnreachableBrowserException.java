package org.openqa.selenium.remote;

import org.openqa.selenium.WebDriverException;




























public class UnreachableBrowserException
  extends WebDriverException
{
  public UnreachableBrowserException(String message)
  {
    super(message);
  }
  
  public UnreachableBrowserException(String message, Throwable cause) {
    super(message, cause);
  }
}
