package org.openqa.grid.web.servlet.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.web.utils.BrowserNameUtils;


















public class LegacySeleniumRequest
  extends SeleniumBasedRequest
{
  public LegacySeleniumRequest(HttpServletRequest httpServletRequest, Registry registry)
  {
    super(httpServletRequest, registry);
  }
  

  public RequestType extractRequestType()
  {
    if (getBody().contains("cmd=getNewBrowserSession"))
      return RequestType.START_SESSION;
    if (getBody().contains("cmd=testComplete")) {
      return RequestType.STOP_SESSION;
    }
    return RequestType.REGULAR;
  }
  


  public ExternalSessionKey extractSession()
  {
    if (getRequestType() == RequestType.START_SESSION) {
      throw new IllegalAccessError("Cannot call that method of a new session request.");
    }
    

    String command = getBody();
    String[] pieces = command.split("&");
    
    for (String piece : pieces) {
      ExternalSessionKey externalSessionKey = ExternalSessionKey.fromSe1Request(piece);
      if (externalSessionKey != null) {
        return externalSessionKey;
      }
    }
    return null;
  }
  
  public Map<String, Object> extractDesiredCapability()
  {
    if (getRequestType() != RequestType.START_SESSION) {
      throw new Error("the desired capability is only present in the new session requests.");
    }
    String[] pieces = getBody().split("&");
    for (String piece : pieces) {
      try {
        piece = URLDecoder.decode(piece, "UTF-8");
      } catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
      if (piece.startsWith("1=")) {
        String envt = piece.replace("1=", "");
        Map<String, Object> cap = new HashMap();
        

        cap.putAll(BrowserNameUtils.parseGrid2Environment(envt));
        
        return cap;
      }
    }
    
    throw new RuntimeException("Error");
  }
  
  public String getBody()
  {
    String postBody = super.getBody();
    return (postBody != null) && (!postBody.equals("")) ? postBody : getQueryString();
  }
}
