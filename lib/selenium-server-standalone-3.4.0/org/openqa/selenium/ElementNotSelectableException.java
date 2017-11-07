package org.openqa.selenium;



















public class ElementNotSelectableException
  extends InvalidElementStateException
{
  public ElementNotSelectableException(String message)
  {
    super(message);
  }
  
  public ElementNotSelectableException(String message, Throwable cause) {
    super(message, cause);
  }
}
