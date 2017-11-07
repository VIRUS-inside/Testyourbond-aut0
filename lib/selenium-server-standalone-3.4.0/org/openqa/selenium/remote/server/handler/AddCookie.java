package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Cookie.Builder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;















public class AddCookie
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile Map<String, Object> rawCookie;
  
  public AddCookie(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Cookie cookie = createCookie();
    
    getDriver().manage().addCookie(cookie);
    
    return null;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    if (allParameters == null) {
      return;
    }
    rawCookie = Maps.newHashMap((Map)allParameters.get("cookie"));
  }
  
  protected Cookie createCookie() {
    if (rawCookie == null) {
      return null;
    }
    
    String name = (String)rawCookie.get("name");
    String value = (String)rawCookie.get("value");
    String path = (String)rawCookie.get("path");
    String domain = (String)rawCookie.get("domain");
    boolean secure = getBooleanFromRaw("secure");
    boolean httpOnly = getBooleanFromRaw("httpOnly");
    
    Number expiryNum = (Number)rawCookie.get("expiry");
    
    Date expiry = expiryNum == null ? null : new Date(TimeUnit.SECONDS.toMillis(expiryNum.longValue()));
    
    return new Cookie.Builder(name, value)
      .path(path)
      .domain(domain)
      .isSecure(secure)
      .expiresOn(expiry)
      .isHttpOnly(httpOnly)
      .build();
  }
  
  private boolean getBooleanFromRaw(String key) {
    if (rawCookie.containsKey(key)) {
      Object value = rawCookie.get(key);
      if ((value instanceof Boolean)) {
        return ((Boolean)value).booleanValue();
      }
      if ((value instanceof String)) {
        return ((String)value).equalsIgnoreCase("true");
      }
    }
    return false;
  }
  
  public String toString()
  {
    return "[add cookie: " + createCookie() + "]";
  }
}
