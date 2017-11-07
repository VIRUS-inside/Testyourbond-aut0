package org.openqa.grid.web;

import com.google.common.collect.Maps;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.servlet.Servlet;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.servlet.DisplayHelpServlet;
import org.openqa.grid.web.servlet.DriverServlet;
import org.openqa.grid.web.servlet.Grid1HeartbeatServlet;
import org.openqa.grid.web.servlet.HubStatusServlet;
import org.openqa.grid.web.servlet.LifecycleServlet;
import org.openqa.grid.web.servlet.ProxyStatusServlet;
import org.openqa.grid.web.servlet.RegistrationServlet;
import org.openqa.grid.web.servlet.ResourceServlet;
import org.openqa.grid.web.servlet.TestSessionStatusServlet;
import org.openqa.grid.web.servlet.beta.ConsoleServlet;
import org.openqa.grid.web.utils.ExtraServletUtil;
import org.openqa.selenium.net.NetworkUtils;
import org.seleniumhq.jetty9.server.ConnectionFactory;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.HttpConnectionFactory;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.ServerConnector;
import org.seleniumhq.jetty9.servlet.ServletContextHandler;
import org.seleniumhq.jetty9.util.thread.QueuedThreadPool;






















public class Hub
{
  private static final Logger log = Logger.getLogger(Hub.class.getName());
  
  private GridHubConfiguration config;
  private final Registry registry;
  private final Map<String, Class<? extends Servlet>> extraServlet = Maps.newHashMap();
  private Server server;
  
  private void addServlet(String key, Class<? extends Servlet> s)
  {
    extraServlet.put(key, s);
  }
  




  public Registry getRegistry()
  {
    return registry;
  }
  
  public Hub(GridHubConfiguration gridHubConfiguration) {
    registry = Registry.newInstance(this, gridHubConfiguration);
    
    config = gridHubConfiguration;
    NetworkUtils utils; if (config.host == null) {
      utils = new NetworkUtils();
      config.host = utils.getIp4NonLoopbackAddressOfThisMachine().getHostAddress();
    }
    
    if (config.port == null) {
      config.port = Integer.valueOf(4444);
    }
    
    if (config.servlets != null) {
      for (String s : config.servlets) {
        Class<? extends Servlet> servletClass = ExtraServletUtil.createServlet(s);
        if (servletClass != null) {
          String path = "/grid/admin/" + servletClass.getSimpleName() + "/*";
          log.info("binding " + servletClass.getCanonicalName() + " to " + path);
          addServlet(path, servletClass);
        }
      }
    }
  }
  
  private void addDefaultServlets(ServletContextHandler handler)
  {
    handler.addServlet(RegistrationServlet.class.getName(), "/grid/register/*");
    
    handler.addServlet(DriverServlet.class.getName(), "/wd/hub/*");
    handler.addServlet(DriverServlet.class.getName(), "/selenium-server/driver/*");
    
    handler.addServlet(ProxyStatusServlet.class.getName(), "/grid/api/proxy/*");
    
    handler.addServlet(HubStatusServlet.class.getName(), "/grid/api/hub/*");
    
    handler.addServlet(TestSessionStatusServlet.class.getName(), "/grid/api/testsession/*");
    

    if (!config.isWithOutServlet(ResourceServlet.class)) {
      handler.addServlet(ResourceServlet.class.getName(), "/grid/resources/*");
    }
    
    if (!config.isWithOutServlet(DisplayHelpServlet.class)) {
      handler.addServlet(DisplayHelpServlet.class.getName(), "/*");
      handler.setInitParameter("webdriver.server.displayhelpservlet.type", config.role);
    }
    
    if (!config.isWithOutServlet(ConsoleServlet.class)) {
      handler.addServlet(ConsoleServlet.class.getName(), "/grid/console/*");
      handler.setInitParameter("webdriver.server.consoleservlet.path", "/grid/console");
    }
    
    if (!config.isWithOutServlet(LifecycleServlet.class)) {
      handler.addServlet(LifecycleServlet.class.getName(), "/lifecycle-manager/*");
    }
    
    if (!config.isWithOutServlet(Grid1HeartbeatServlet.class)) {
      handler.addServlet(Grid1HeartbeatServlet.class.getName(), "/heartbeat");
    }
  }
  
  private void initServer() {
    try {
      if ((config.jettyMaxThreads != null) && (config.jettyMaxThreads.intValue() > 0)) {
        QueuedThreadPool pool = new QueuedThreadPool();
        pool.setMaxThreads(config.jettyMaxThreads.intValue());
        server = new Server(pool);
      } else {
        server = new Server();
      }
      
      HttpConfiguration httpConfig = new HttpConfiguration();
      httpConfig.setSecureScheme("https");
      httpConfig.setSecurePort(config.port.intValue());
      
      log.info("Will listen on " + config.port);
      
      ServerConnector http = new ServerConnector(server, new ConnectionFactory[] { new HttpConnectionFactory(httpConfig) });
      http.setPort(config.port.intValue());
      
      server.addConnector(http);
      
      root = new ServletContextHandler(1);
      root.setContextPath("/");
      server.setHandler(root);
      
      root.setAttribute(Registry.KEY, registry);
      
      addDefaultServlets(root);
      

      for (Map.Entry<String, Class<? extends Servlet>> entry : extraServlet.entrySet()) {
        root.addServlet(((Class)entry.getValue()).getName(), (String)entry.getKey());
      }
    } catch (Throwable e) {
      ServletContextHandler root;
      throw new RuntimeException("Error initializing the hub " + e.getMessage(), e);
    }
  }
  
  public GridHubConfiguration getConfiguration() {
    return config;
  }
  
  public void start() throws Exception {
    initServer();
    server.start();
  }
  
  public void stop() throws Exception {
    server.stop();
  }
  
  public URL getUrl() {
    return getUrl("");
  }
  
  public URL getUrl(String path) {
    try {
      return new URL("http://" + config.host + ":" + config.port + path);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
  
  public URL getRegistrationURL() {
    return getUrl("/grid/register/");
  }
  


  public URL getWebDriverHubRequestURL()
  {
    return getUrl("/wd/hub");
  }
  
  public URL getConsoleURL() {
    return getUrl("/grid/console");
  }
}
