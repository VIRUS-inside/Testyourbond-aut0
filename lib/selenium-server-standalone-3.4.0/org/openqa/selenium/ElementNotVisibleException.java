package org.openqa.selenium;



















public class ElementNotVisibleException
  extends ElementNotInteractableException
{
  public ElementNotVisibleException(String message)
  {
    super(message);
  }
  
  public ElementNotVisibleException(String message, Throwable cause) {
    super(message, cause);
  }
}
