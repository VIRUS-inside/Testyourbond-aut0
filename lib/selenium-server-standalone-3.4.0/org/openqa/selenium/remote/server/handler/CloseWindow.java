package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;

















public class CloseWindow
  extends WebDriverHandler<Void>
{
  public CloseWindow(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    WebDriver driver = getDriver();
    driver.close();
    return null;
  }
  
  public String toString()
  {
    return "[close window]";
  }
}
