package org.openqa.selenium;











public class NoSuchElementException
  extends NotFoundException
{
  private static final String SUPPORT_URL = "http://seleniumhq.org/exceptions/no_such_element.html";
  










  public NoSuchElementException(String reason)
  {
    super(reason);
  }
  
  public NoSuchElementException(String reason, Throwable cause) {
    super(reason, cause);
  }
  
  public String getSupportUrl()
  {
    return "http://seleniumhq.org/exceptions/no_such_element.html";
  }
}
