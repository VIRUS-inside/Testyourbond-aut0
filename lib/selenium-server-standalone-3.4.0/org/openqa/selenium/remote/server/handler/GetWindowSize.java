package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.server.Session;













public class GetWindowSize
  extends WebDriverHandler<Dimension>
{
  public GetWindowSize(Session session)
  {
    super(session);
  }
  
  public Dimension call() throws Exception
  {
    return getDriver().manage().window().getSize();
  }
  
  public String toString()
  {
    return "[get window size]";
  }
}
