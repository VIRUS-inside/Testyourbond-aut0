package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.server.Session;













public class GetWindowPosition
  extends WebDriverHandler<Point>
{
  public GetWindowPosition(Session session)
  {
    super(session);
  }
  
  public Point call() throws Exception
  {
    return getDriver().manage().window().getPosition();
  }
  
  public String toString()
  {
    return "[get window position]";
  }
}
