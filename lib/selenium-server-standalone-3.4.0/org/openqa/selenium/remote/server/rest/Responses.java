package org.openqa.selenium.remote.server.rest;

import com.google.common.base.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;





















class Responses
{
  private static final ErrorCodes ERROR_CODES = new ErrorCodes();
  



  private Responses() {}
  



  public static Response success(SessionId sessionId, Object value)
  {
    Response response = new Response();
    response.setSessionId(sessionId != null ? sessionId.toString() : null);
    response.setValue(value);
    response.setStatus(Integer.valueOf(0));
    response.setState("success");
    return response;
  }
  






  public static Response failure(SessionId sessionId, Throwable reason)
  {
    Response response = new Response();
    response.setSessionId(sessionId != null ? sessionId.toString() : null);
    response.setValue(reason);
    response.setStatus(Integer.valueOf(ERROR_CODES.toStatusCode(reason)));
    response.setState(ERROR_CODES.toState(response.getStatus()));
    return response;
  }
  








  public static Response failure(SessionId sessionId, Throwable reason, Optional<String> screenshot)
  {
    Response response = new Response();
    response.setSessionId(sessionId != null ? sessionId.toString() : null);
    response.setStatus(Integer.valueOf(ERROR_CODES.toStatusCode(reason)));
    response.setState(ERROR_CODES.toState(response.getStatus()));
    
    if (reason != null) {
      JsonObject json = new BeanToJsonConverter().convertObject(reason).getAsJsonObject();
      json.addProperty("screen", (String)screenshot.orNull());
      response.setValue(json);
    }
    return response;
  }
}
