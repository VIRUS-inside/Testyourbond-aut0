package org.openqa.selenium.interactions;

import org.openqa.selenium.WebDriverException;



















public class MoveTargetOutOfBoundsException
  extends WebDriverException
{
  public MoveTargetOutOfBoundsException(String message)
  {
    super(message);
  }
  
  public MoveTargetOutOfBoundsException(Throwable cause) {
    super(cause);
  }
  
  public MoveTargetOutOfBoundsException(String message, Throwable cause) {
    super(message, cause);
  }
}
