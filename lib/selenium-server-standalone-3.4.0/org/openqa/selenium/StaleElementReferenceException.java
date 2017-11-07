package org.openqa.selenium;












public class StaleElementReferenceException
  extends WebDriverException
{
  private static final String SUPPORT_URL = "http://seleniumhq.org/exceptions/stale_element_reference.html";
  










  public StaleElementReferenceException(String message)
  {
    super(message);
  }
  
  public StaleElementReferenceException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public String getSupportUrl()
  {
    return "http://seleniumhq.org/exceptions/stale_element_reference.html";
  }
}
