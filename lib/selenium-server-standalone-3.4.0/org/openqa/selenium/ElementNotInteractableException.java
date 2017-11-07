package org.openqa.selenium;



















public class ElementNotInteractableException
  extends InvalidElementStateException
{
  public ElementNotInteractableException(String message)
  {
    super(message);
  }
  
  public ElementNotInteractableException(String message, Throwable cause) {
    super(message, cause);
  }
}
