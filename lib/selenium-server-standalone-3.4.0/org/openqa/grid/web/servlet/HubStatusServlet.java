package org.openqa.grid.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;






































public class HubStatusServlet
  extends RegistryBasedServlet
{
  public HubStatusServlet()
  {
    super(null);
  }
  
  public HubStatusServlet(Registry registry) {
    super(registry);
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  

  protected void process(HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    try
    {
      JsonObject res = getResponse(request);
      response.getWriter().print(res);
      response.getWriter().close();
    } catch (JsonSyntaxException e) {
      throw new GridException(e.getMessage());
    }
    JsonObject res;
  }
  
  private JsonObject getResponse(HttpServletRequest request) throws IOException {
    JsonObject res = new JsonObject();
    res.addProperty("success", Boolean.valueOf(true));
    try {
      if (request.getInputStream() != null) {
        JsonObject requestJSON = getRequestJSON(request);
        List<String> keysToReturn = null;
        
        if ((request.getParameter("configuration") != null) && (!"".equals(request.getParameter("configuration")))) {
          keysToReturn = Arrays.asList(request.getParameter("configuration").split(","));
        } else if ((requestJSON != null) && (requestJSON.has("configuration"))) {
          keysToReturn = (List)new Gson().fromJson(requestJSON.getAsJsonArray("configuration"), ArrayList.class);
        }
        
        Registry registry = getRegistry();
        JsonElement config = registry.getConfiguration().toJson();
        for (Map.Entry<String, JsonElement> entry : config.getAsJsonObject().entrySet()) {
          if ((keysToReturn == null) || (keysToReturn.isEmpty()) || (keysToReturn.contains(entry.getKey()))) {
            res.add((String)entry.getKey(), (JsonElement)entry.getValue());
          }
        }
        if ((keysToReturn == null) || (keysToReturn.isEmpty()) || (keysToReturn.contains("newSessionRequestCount"))) {
          res.addProperty("newSessionRequestCount", Integer.valueOf(registry.getNewSessionRequestCount()));
        }
        
        if ((keysToReturn == null) || (keysToReturn.isEmpty()) || (keysToReturn.contains("slotCounts"))) {
          res.add("slotCounts", getSlotCounts());
        }
      }
    } catch (Exception e) {
      res.remove("success");
      res.addProperty("success", Boolean.valueOf(false));
      res.addProperty("msg", e.getMessage());
    }
    return res;
  }
  
  private JsonObject getSlotCounts()
  {
    int totalSlots = 0;
    int usedSlots = 0;
    
    for (RemoteProxy proxy : getRegistry().getAllProxies()) {
      totalSlots += Math.min(proxy.getMaxNumberOfConcurrentTestSessions(), proxy.getTestSlots().size());
      usedSlots += proxy.getTotalUsed();
    }
    JsonObject result = new JsonObject();
    result.addProperty("free", Integer.valueOf(totalSlots - usedSlots));
    result.addProperty("total", Integer.valueOf(totalSlots));
    return result;
  }
  
  private JsonObject getRequestJSON(HttpServletRequest request) throws IOException {
    JsonObject requestJSON = null;
    BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
    StringBuilder s = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      s.append(line);
    }
    rd.close();
    String json = s.toString();
    if (!"".equals(json)) {
      requestJSON = new JsonParser().parse(json).getAsJsonObject();
    }
    return requestJSON;
  }
}
