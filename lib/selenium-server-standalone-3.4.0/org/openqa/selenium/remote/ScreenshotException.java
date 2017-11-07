package org.openqa.selenium.remote;

import org.openqa.selenium.WebDriverException;
















public class ScreenshotException
  extends WebDriverException
{
  private final String screenshot;
  
  public ScreenshotException(String screenGrab)
  {
    super("Screen shot has been taken");
    screenshot = screenGrab;
  }
  
  public ScreenshotException(String screenGrab, Throwable cause) {
    super("Screen shot has been taken", cause);
    screenshot = screenGrab;
  }
  
  public String getBase64EncodedScreenshot() {
    return screenshot;
  }
}
