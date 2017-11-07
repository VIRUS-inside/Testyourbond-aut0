package org.openqa.selenium;













public class InvalidSelectorException
  extends NoSuchElementException
{
  private static final String SUPPORT_URL = "http://seleniumhq.org/exceptions/invalid_selector_exception.html";
  












  public InvalidSelectorException(String reason)
  {
    super(reason);
  }
  
  public InvalidSelectorException(String reason, Throwable cause) {
    super(reason, cause);
  }
  
  public String getSupportUrl()
  {
    return "http://seleniumhq.org/exceptions/invalid_selector_exception.html";
  }
}
