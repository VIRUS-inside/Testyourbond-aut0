package org.openqa.selenium.remote.server.xdrpc;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;



























public class CrossDomainRpcLoader
{
  public CrossDomainRpcLoader() {}
  
  public CrossDomainRpc loadRpc(HttpServletRequest request)
    throws IOException
  {
    InputStream stream = null;
    try {
      stream = request.getInputStream();
      byte[] data = ByteStreams.toByteArray(stream);
      json = new JsonParser().parse(new String(data, Charsets.UTF_8)).getAsJsonObject();
    } catch (JsonSyntaxException e) {
      JsonObject json;
      throw new IllegalArgumentException("Failed to parse JSON request: " + e.getMessage(), e);
    } finally {
      if (stream != null) {
        stream.close();
      }
    }
    JsonObject json;
    return new CrossDomainRpc(
      getField(json, "method"), 
      getField(json, "path"), 
      getField(json, "data"));
  }
  
  private String getField(JsonObject json, String key) {
    if ((!json.has(key)) || (json.get(key).isJsonNull())) {
      throw new IllegalArgumentException("Missing required parameter: " + key);
    }
    
    if ((json.get(key).isJsonPrimitive()) && (json.get(key).getAsJsonPrimitive().isString())) {
      return json.get(key).getAsString();
    }
    return json.get(key).toString();
  }
  
  private static class Field
  {
    public static final String METHOD = "method";
    public static final String PATH = "path";
    public static final String DATA = "data";
    
    private Field() {}
  }
}
