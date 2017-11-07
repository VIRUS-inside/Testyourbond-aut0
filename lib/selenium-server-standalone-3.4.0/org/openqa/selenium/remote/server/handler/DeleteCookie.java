package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;















public class DeleteCookie
  extends WebDriverHandler<Void>
{
  public DeleteCookie(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().deleteAllCookies();
    return null;
  }
  
  public String toString()
  {
    return "[delete all cookies]";
  }
}
