package org.openqa.grid.web.servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.TestSlot;
















public class TestSessionStatusServlet
  extends RegistryBasedServlet
{
  private static final long serialVersionUID = 4325112892618707612L;
  
  public TestSessionStatusServlet()
  {
    super(null);
  }
  
  public TestSessionStatusServlet(Registry registry) {
    super(registry);
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
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
  
  private JsonObject getResponse(HttpServletRequest request) throws IOException { JsonObject requestJSON = null;
    if (request.getInputStream() != null) {
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
    }
    

    JsonObject res = new JsonObject();
    res.addProperty("success", Boolean.valueOf(false));
    
    String session;
    String session;
    if (requestJSON == null) {
      session = request.getParameter("session");
    } else {
      if (!requestJSON.has("session")) {
        res.addProperty("msg", "you need to specify at least a session or internalKey when call the test slot status service.");
        
        return res;
      }
      session = requestJSON.get("session").getAsString();
    }
    
    TestSession testSession = getRegistry().getSession(ExternalSessionKey.fromString(session));
    
    if (testSession == null) {
      res.addProperty("msg", "Cannot find test slot running session " + session + " in the registry.");
      return res;
    }
    res.addProperty("msg", "slot found !");
    res.remove("success");
    res.addProperty("success", Boolean.valueOf(true));
    res.addProperty("session", testSession.getExternalKey().getKey());
    res.addProperty("internalKey", testSession.getInternalKey());
    res.addProperty("inactivityTime", Long.valueOf(testSession.getInactivityTime()));
    RemoteProxy p = testSession.getSlot().getProxy();
    res.addProperty("proxyId", p.getId());
    return res;
  }
}
