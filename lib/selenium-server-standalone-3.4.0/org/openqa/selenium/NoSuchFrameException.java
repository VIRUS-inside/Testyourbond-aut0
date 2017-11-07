package org.openqa.selenium;




















public class NoSuchFrameException
  extends NotFoundException
{
  public NoSuchFrameException(String reason)
  {
    super(reason);
  }
  
  public NoSuchFrameException(String reason, Throwable cause) {
    super(reason, cause);
  }
}
