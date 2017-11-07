package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;















public class GetTitle
  extends WebDriverHandler<String>
{
  public GetTitle(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getDriver().getTitle();
  }
  
  public String toString()
  {
    return "[get title]";
  }
}
