package org.openqa.grid.web.servlet.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.Registry;
import org.openqa.selenium.remote.JsonToBeanConverter;



















public class WebDriverRequest
  extends SeleniumBasedRequest
{
  public WebDriverRequest(HttpServletRequest httpServletRequest, Registry registry)
  {
    super(httpServletRequest, registry);
  }
  
  public RequestType extractRequestType()
  {
    if ("/session".equals(getPathInfo()))
      return RequestType.START_SESSION;
    if (getMethod().equalsIgnoreCase("DELETE")) {
      ExternalSessionKey externalKey = ExternalSessionKey.fromWebDriverRequest(getPathInfo());
      if ((externalKey != null) && (getPathInfo().endsWith("/session/" + externalKey.getKey()))) {
        return RequestType.STOP_SESSION;
      }
    }
    return RequestType.REGULAR;
  }
  
  public ExternalSessionKey extractSession()
  {
    if (getRequestType() == RequestType.START_SESSION) {
      throw new IllegalAccessError("Cannot call that method of a new session request.");
    }
    String path = getPathInfo();
    return ExternalSessionKey.fromWebDriverRequest(path);
  }
  
  public Map<String, Object> extractDesiredCapability()
  {
    String json = getBody();
    try {
      JsonObject map = new JsonParser().parse(json).getAsJsonObject();
      

      if (map.has("capabilities")) {
        return (Map)new JsonToBeanConverter().convert(Map.class, map.getAsJsonObject("capabilities").getAsJsonObject("desiredCapabilities"));
      }
      JsonObject dc = map.get("desiredCapabilities").getAsJsonObject();
      return (Map)new JsonToBeanConverter().convert(Map.class, dc);
    }
    catch (Exception e) {
      throw new GridException("Cannot extract a capabilities from the request: " + json, e);
    }
  }
}
