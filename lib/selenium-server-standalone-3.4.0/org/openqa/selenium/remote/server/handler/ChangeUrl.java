package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;















public class ChangeUrl
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile String url;
  
  public ChangeUrl(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    url = ((String)allParameters.get("url"));
  }
  
  public Void call() throws Exception
  {
    getDriver().get(url);
    
    return null;
  }
  
  public String toString()
  {
    return "[get: " + url + "]";
  }
}
