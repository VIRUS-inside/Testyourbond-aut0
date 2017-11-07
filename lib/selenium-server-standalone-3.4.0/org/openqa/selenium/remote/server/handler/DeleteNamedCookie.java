package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;















public class DeleteNamedCookie
  extends WebDriverHandler<Void>
{
  private volatile String name;
  
  public DeleteNamedCookie(Session session)
  {
    super(session);
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().deleteCookieNamed(name);
    return null;
  }
  
  public String toString()
  {
    return String.format("[delete cookie: %s]", new Object[] { name });
  }
}
