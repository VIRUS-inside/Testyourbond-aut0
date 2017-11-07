package org.openqa.grid.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.openqa.grid.internal.exception.NewSessionException;

















public class ExternalSessionKey
{
  private final String key;
  
  public ExternalSessionKey(String key)
  {
    this.key = key;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    
    ExternalSessionKey that = (ExternalSessionKey)o;
    
    return key.equals(key);
  }
  
  public String getKey()
  {
    return key;
  }
  
  public String toString()
  {
    return getKey();
  }
  
  public int hashCode()
  {
    return key.hashCode();
  }
  
  public static ExternalSessionKey fromSe1Request(String piece) {
    if (piece.startsWith("sessionId=")) {
      return new ExternalSessionKey(piece.replace("sessionId=", ""));
    }
    return null;
  }
  





  public static ExternalSessionKey fromWebDriverRequest(String path)
  {
    int sessionIndex = path.indexOf("/session/");
    if (sessionIndex != -1) {
      sessionIndex += "/session/".length();
      int nextSlash = path.indexOf("/", sessionIndex);
      String session;
      String session; if (nextSlash != -1) {
        session = path.substring(sessionIndex, nextSlash);
      } else {
        session = path.substring(sessionIndex, path.length());
      }
      if ("".equals(session)) {
        return null;
      }
      return new ExternalSessionKey(session);
    }
    return null;
  }
  




  public static ExternalSessionKey fromJsonResponseBody(String responseBody)
  {
    try
    {
      JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();
      if ((!json.has("sessionId")) || (json.get("sessionId").isJsonNull())) {
        return null;
      }
      return new ExternalSessionKey(json.get("sessionId").getAsString());
    } catch (JsonSyntaxException e) {}
    return null;
  }
  





  public static ExternalSessionKey fromResponseBody(String responseBody)
    throws NewSessionException
  {
    if ((responseBody != null) && (responseBody.startsWith("OK,"))) {
      return new ExternalSessionKey(responseBody.replace("OK,", ""));
    }
    throw new NewSessionException("The server returned an error : " + responseBody);
  }
  
  public static ExternalSessionKey fromString(String keyString) {
    return new ExternalSessionKey(keyString);
  }
  
  public static ExternalSessionKey fromJSON(String keyString) {
    return new ExternalSessionKey(keyString);
  }
}
