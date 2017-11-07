package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;

















public class CaptureScreenshot
  extends WebDriverHandler<String>
{
  public CaptureScreenshot(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    WebDriver driver = getUnwrappedDriver();
    
    return (String)((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
  }
  
  public String toString()
  {
    return "[take screenshot]";
  }
}
