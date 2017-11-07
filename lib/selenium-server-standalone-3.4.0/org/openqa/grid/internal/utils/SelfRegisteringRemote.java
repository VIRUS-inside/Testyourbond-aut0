package org.openqa.grid.internal.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.Servlet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.shared.GridNodeServer;
import org.openqa.grid.web.servlet.DisplayHelpServlet;
import org.openqa.grid.web.servlet.ResourceServlet;
import org.openqa.grid.web.utils.ExtraServletUtil;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.internal.HttpClientFactory;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;

















public class SelfRegisteringRemote
{
  private static final Logger LOG = Logger.getLogger(SelfRegisteringRemote.class.getName());
  
  private final RegistrationRequest registrationRequest;
  
  private final HttpClientFactory httpClientFactory;
  private final Map<String, Class<? extends Servlet>> nodeServlets;
  private boolean hasId;
  private GridNodeServer server;
  
  public SelfRegisteringRemote(RegistrationRequest request)
  {
    registrationRequest = request;
    httpClientFactory = new HttpClientFactory();
    nodeServlets = new HashMap();
    
    registrationRequest.validate();
    try
    {
      GridHubConfiguration hubConfiguration = getHubConfiguration();
      
      if ((timeout != null) && (timeout.intValue() >= 0)) {
        registrationRequest.getConfiguration().timeout = timeout;
      }
      if ((browserTimeout != null) && (browserTimeout.intValue() >= 0)) {
        registrationRequest.getConfiguration().browserTimeout = browserTimeout;
      }
    } catch (Exception e) {
      LOG.warning("error getting the parameters from the hub. The node may end up with wrong timeouts." + e
      
        .getMessage());
    }
    

    if (!registrationRequest.getConfiguration().isWithOutServlet(ResourceServlet.class)) {
      nodeServlets.put("/resources/*", ResourceServlet.class);
    }
    

    if (!registrationRequest.getConfiguration().isWithOutServlet(DisplayHelpServlet.class)) {
      nodeServlets.put("/*", DisplayHelpServlet.class);
    }
    

    addExtraServlets(registrationRequest.getConfiguration().servlets);
  }
  
  public URL getRemoteURL()
  {
    String host = registrationRequest.getConfiguration().host;
    Integer port = registrationRequest.getConfiguration().port;
    String url = "http://" + host + ":" + port;
    try
    {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new GridConfigurationException("error building the node url " + e.getMessage(), e);
    }
  }
  

  public void setRemoteServer(GridNodeServer server)
  {
    this.server = server;
  }
  
  public void startRemoteServer() throws Exception {
    if (server == null) {
      throw new GridConfigurationException("no server set to register to the hub");
    }
    server.setExtraServlets(nodeServlets);
    server.boot();
  }
  
  public void stopRemoteServer() {
    if (server != null) {
      server.stop();
    }
  }
  
  public void deleteAllBrowsers() {
    registrationRequest.getConfiguration().capabilities.clear();
  }
  






  public void addBrowser(DesiredCapabilities cap, int instances)
  {
    String s = cap.getBrowserName();
    if ((s == null) || ("".equals(s))) {
      throw new InvalidParameterException(cap + " does seems to be a valid browser.");
    }
    if (cap.getPlatform() == null) {
      cap.setPlatform(Platform.getCurrent());
    }
    cap.setCapability("maxInstances", Integer.valueOf(instances));
    registrationRequest.getConfiguration().capabilities.add(cap);
  }
  



  public void sendRegistrationRequest()
  {
    registerToHub(false);
  }
  






  public void startRegistrationProcess()
  {
    fixUpId();
    LOG.fine("Using the json request : " + registrationRequest.toJson());
    
    Boolean register = registrationRequest.getConfiguration().register;
    if (register == null) {
      register = Boolean.valueOf(false);
    }
    
    if (!register.booleanValue()) {
      LOG.info("No registration sent ( register = false )");
    }
    else {
      final int registerCycleInterval = registrationRequest.getConfiguration().registerCycle != null ? registrationRequest.getConfiguration().registerCycle.intValue() : 0;
      if (registerCycleInterval > 0)
      {

























        new Thread(new Runnable()
        {
          public void run()
          {
            boolean first = true;
            SelfRegisteringRemote.LOG.info("Starting auto registration thread. Will try to register every " + registerCycleInterval + " ms.");
            for (;;)
            {
              try {
                boolean checkForPresence = true;
                if (first) {
                  first = false;
                  checkForPresence = false;
                }
                SelfRegisteringRemote.this.registerToHub(checkForPresence);
              } catch (GridException e) {
                SelfRegisteringRemote.LOG.info("Couldn't register this node: " + e.getMessage());
              }
              try {
                Thread.sleep(registerCycleInterval);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              
              LoggingManager.perSessionLogHandler().clearThreadTempLogs();
            }
          }
        })
        
























          .start();
      } else {
        registerToHub(false);
      }
    }
    LoggingManager.perSessionLogHandler().clearThreadTempLogs();
  }
  
  public void setTimeout(int timeout, int cycle) {
    registrationRequest.getConfiguration().timeout = Integer.valueOf(timeout);
    registrationRequest.getConfiguration().cleanUpCycle = Integer.valueOf(cycle);
  }
  
  public void setMaxConcurrent(int max) {
    registrationRequest.getConfiguration().maxSession = Integer.valueOf(max);
  }
  
  public GridNodeConfiguration getConfiguration() {
    return registrationRequest.getConfiguration();
  }
  


  protected GridNodeServer getServer()
  {
    return server;
  }
  


  protected Map<String, Class<? extends Servlet>> getNodeServlets()
  {
    return nodeServlets;
  }
  
  private void registerToHub(boolean checkPresenceFirst) {
    if ((!checkPresenceFirst) || (!isAlreadyRegistered(registrationRequest)))
    {

      String tmp = "http://" + registrationRequest.getConfiguration().getHubHost() + ":" + registrationRequest.getConfiguration().getHubPort() + "/grid/register";
      
      HttpClient client = httpClientFactory.getHttpClient();
      try {
        URL registration = new URL(tmp);
        LOG.info("Registering the node to the hub: " + registration);
        

        BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", registration.toExternalForm());
        updateConfigWithRealPort();
        String json = registrationRequest.toJson().toString();
        r.setEntity(new StringEntity(json, "UTF-8"));
        
        HttpHost host = new HttpHost(registration.getHost(), registration.getPort());
        HttpResponse response = client.execute(host, r);
        if (response.getStatusLine().getStatusCode() != 200) {
          throw new GridException(String.format("The hub responded with %s:%s", new Object[] {
            Integer.valueOf(response.getStatusLine().getStatusCode()), response
            .getStatusLine().getReasonPhrase() }));
        }
        LOG.info("The node is registered to the hub and ready to use");
      } catch (Exception e) {
        throw new GridException("Error sending the registration request: " + e.getMessage());
      }
    } else {
      LOG.fine("The node is already present on the hub. Skipping registration.");
    }
  }
  
  private void addExtraServlets(List<String> servlets)
  {
    if ((servlets == null) || (servlets.size() == 0)) {
      return;
    }
    
    for (String s : servlets) {
      Class<? extends Servlet> servletClass = ExtraServletUtil.createServlet(s);
      if (servletClass != null) {
        String path = "/extra/" + servletClass.getSimpleName() + "/*";
        LOG.info("binding " + servletClass.getCanonicalName() + " to " + path);
        nodeServlets.put(path, servletClass);
      }
    }
  }
  
  private void fixUpId() {
    if (hasId) {
      return;
    }
    

    if ((registrationRequest.getConfiguration().id == null) || 
      (registrationRequest.getConfiguration().id.isEmpty()))
    {
      registrationRequest.getConfiguration().id = registrationRequest.getConfiguration().getRemoteHost();
    }
    
    hasId = true;
  }
  
  void updateConfigWithRealPort() throws MalformedURLException {
    if (registrationRequest.getConfiguration().port.intValue() != 0) {
      return;
    }
    registrationRequest.getConfiguration().port = Integer.valueOf(server.getRealPort());
  }
  





  private GridHubConfiguration getHubConfiguration()
    throws Exception
  {
    String hubApi = "http://" + registrationRequest.getConfiguration().getHubHost() + ":" + registrationRequest.getConfiguration().getHubPort() + "/grid/api/hub";
    
    HttpClient client = httpClientFactory.getHttpClient();
    
    URL api = new URL(hubApi);
    HttpHost host = new HttpHost(api.getHost(), api.getPort());
    String url = api.toExternalForm();
    BasicHttpRequest r = new BasicHttpRequest("GET", url);
    
    HttpResponse response = client.execute(host, r);
    return GridHubConfiguration.loadFromJSON(extractObject(response));
  }
  
  private boolean isAlreadyRegistered(RegistrationRequest node)
  {
    HttpClient client = httpClientFactory.getHttpClient();
    
    try
    {
      String tmp = "http://" + node.getConfiguration().getHubHost() + ":" + node.getConfiguration().getHubPort() + "/grid/api/proxy";
      URL api = new URL(tmp);
      HttpHost host = new HttpHost(api.getHost(), api.getPort());
      
      String id = getConfigurationid;
      if (id == null) {
        id = node.getConfiguration().getRemoteHost();
      }
      BasicHttpRequest r = new BasicHttpRequest("GET", api.toExternalForm() + "?id=" + id);
      
      HttpResponse response = client.execute(host, r);
      if (response.getStatusLine().getStatusCode() != 200) {
        throw new GridException(String.format("The hub responded with %s:%s", new Object[] {
          Integer.valueOf(response.getStatusLine().getStatusCode()), response
          .getStatusLine().getReasonPhrase() }));
      }
      JsonObject o = extractObject(response);
      return o.get("success").getAsBoolean();
    } catch (Exception e) {
      throw new GridException("The hub is down or not responding: " + e.getMessage());
    }
  }
  
  private static JsonObject extractObject(HttpResponse resp) throws IOException {
    BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
    StringBuilder s = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      s.append(line);
    }
    rd.close();
    return new JsonParser().parse(s.toString()).getAsJsonObject();
  }
}
