package org.openqa.grid.web.servlet;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.internal.BaseRemoteProxy;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.JsonToBeanConverter;





















public class RegistrationServlet
  extends RegistryBasedServlet
{
  private static final long serialVersionUID = -8670670577712086527L;
  private static final Logger log = Logger.getLogger(RegistrationServlet.class.getName());
  
  public RegistrationServlet() {
    this(null);
  }
  
  public RegistrationServlet(Registry registry) {
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
    BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
    StringBuilder requestJsonString = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      requestJsonString.append(line);
    }
    rd.close();
    log.fine("getting the following registration request  : " + requestJsonString.toString());
    

    JsonObject json = new JsonParser().parse(requestJsonString.toString()).getAsJsonObject();
    
    if (!json.has("configuration"))
    {
      throw new GridConfigurationException("No configuration received for proxy.");
    }
    
    RegistrationRequest registrationRequest;
    if (isV2RegistrationRequestJson(json))
    {

      GridNodeConfiguration nodeConfiguration = mapV2Configuration(json.getAsJsonObject("configuration"));
      RegistrationRequest registrationRequest = new RegistrationRequest(nodeConfiguration);
      
      considerV2Json(registrationRequest.getConfiguration(), json);
    }
    else {
      registrationRequest = RegistrationRequest.fromJson(json);
    }
    
    final RemoteProxy proxy = BaseRemoteProxy.getNewInstance(registrationRequest, getRegistry());
    
    reply(response, "ok");
    
    new Thread(new Runnable() {
      public void run() {
        getRegistry().add(proxy);
        RegistrationServlet.log.fine("proxy added " + proxy.getRemoteHost());
      }
    })
    



      .start();
  }
  





  @Deprecated
  private GridNodeConfiguration mapV2Configuration(JsonObject json)
  {
    JsonElement servlets = json.has("servlets") ? json.get("servlets") : null;
    

    if ((servlets != null) && (servlets.isJsonPrimitive())) {
      json.remove("servlets");
    }
    


    GridNodeConfiguration pendingConfiguration = GridNodeConfiguration.loadFromJSON(json);
    

    if ((servlets != null) && (servlets.isJsonPrimitive()) && ((servlets == null) || 
      (servlets.isEmpty()))) {
      servlets = Lists.newArrayList(servlets.getAsString().split(","));
    }
    
    return pendingConfiguration;
  }
  




  @Deprecated
  private boolean isV2RegistrationRequestJson(JsonObject json)
  {
    return (json.has("capabilities")) && (json.has("configuration"));
  }
  








  @Deprecated
  private void considerV2Json(GridNodeConfiguration configuration, JsonObject json)
  {
    if (json.has("id")) {
      id = json.get("id").getAsString();
    }
    




    if (json.has("capabilities")) {
      capabilities.clear();
      JsonArray capabilities = json.get("capabilities").getAsJsonArray();
      for (int i = 0; i < capabilities.size(); i++)
      {
        DesiredCapabilities cap = (DesiredCapabilities)new JsonToBeanConverter().convert(DesiredCapabilities.class, capabilities.get(i));
        capabilities.add(cap);
      }
    }
  }
  
  protected void reply(HttpServletResponse response, String content) throws IOException {
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    response.getWriter().print(content);
  }
}
