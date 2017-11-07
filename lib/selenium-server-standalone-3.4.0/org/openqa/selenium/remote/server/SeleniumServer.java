package org.openqa.selenium.remote.server;

import com.beust.jcommander.JCommander;
import java.io.PrintStream;
import java.util.Map;
import javax.servlet.Servlet;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.grid.shared.GridNodeServer;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.seleniumhq.jetty9.server.ConnectionFactory;
import org.seleniumhq.jetty9.server.Connector;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.HttpConnectionFactory;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.ServerConnector;
import org.seleniumhq.jetty9.servlet.ServletContextHandler;
import org.seleniumhq.jetty9.util.thread.QueuedThreadPool;






























public class SeleniumServer
  implements GridNodeServer
{
  private Server server;
  private DefaultDriverSessions driverSessions;
  private StandaloneConfiguration configuration;
  private Map<String, Class<? extends Servlet>> extraServlets;
  private Thread shutDownHook;
  private final Object shutdownLock = new Object();
  private static final int MAX_SHUTDOWN_RETRIES = 8;
  
  public SeleniumServer(StandaloneConfiguration configuration)
  {
    this.configuration = configuration;
  }
  
  public int getRealPort() {
    if (server.isStarted()) {
      ServerConnector socket = (ServerConnector)server.getConnectors()[0];
      return socket.getPort();
    }
    return configuration.port.intValue();
  }
  



  private void addRcSupport(ServletContextHandler handler)
  {
    try
    {
      Class<? extends Servlet> rcServlet = Class.forName("com.thoughtworks.selenium.webdriven.WebDriverBackedSeleniumServlet", false, getClass().getClassLoader()).asSubclass(Servlet.class);
      handler.addServlet(rcServlet, "/selenium-server/driver/");
    }
    catch (ClassNotFoundException localClassNotFoundException) {}
  }
  
  private void addExtraServlets(ServletContextHandler handler)
  {
    if ((extraServlets != null) && (extraServlets.size() > 0)) {
      for (String path : extraServlets.keySet()) {
        handler.addServlet((Class)extraServlets.get(path), path);
      }
    }
  }
  
  public void setConfiguration(StandaloneConfiguration configuration) {
    this.configuration = configuration;
  }
  
  public void setExtraServlets(Map<String, Class<? extends Servlet>> extraServlets) {
    this.extraServlets = extraServlets;
  }
  
  public void boot() {
    if ((configuration.jettyMaxThreads != null) && (configuration.jettyMaxThreads.intValue() > 0)) {
      server = new Server(new QueuedThreadPool(configuration.jettyMaxThreads.intValue()));
    } else {
      server = new Server();
    }
    
    ServletContextHandler handler = new ServletContextHandler();
    
    driverSessions = new DefaultDriverSessions();
    handler.setAttribute(DriverServlet.SESSIONS_KEY, driverSessions);
    handler.setContextPath("/");
    handler.addServlet(DriverServlet.class, "/wd/hub/*");
    handler.setInitParameter("webdriver.server.consoleservlet.path", "/wd/hub");
    
    handler.setInitParameter("webdriver.server.displayhelpservlet.type", configuration.role);
    
    if ((configuration.browserTimeout != null) && (configuration.browserTimeout.intValue() >= 0)) {
      handler.setInitParameter("webdriver.server.browser.timeout", 
        String.valueOf(configuration.browserTimeout));
    }
    if ((configuration.timeout != null) && (configuration.timeout.intValue() >= 0)) {
      handler.setInitParameter("webdriver.server.session.timeout", 
        String.valueOf(configuration.timeout));
    }
    
    addRcSupport(handler);
    addExtraServlets(handler);
    
    server.setHandler(handler);
    
    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setSecureScheme("https");
    
    ServerConnector http = new ServerConnector(server, new ConnectionFactory[] { new HttpConnectionFactory(httpConfig) });
    if (configuration.port == null) {
      configuration.port = Integer.valueOf(4444);
    }
    http.setPort(configuration.port.intValue());
    http.setIdleTimeout(500000L);
    
    server.setConnectors(new Connector[] { http });
    try
    {
      server.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private class ShutDownHook implements Runnable {
    private final SeleniumServer selenium;
    
    ShutDownHook(SeleniumServer selenium) {
      this.selenium = selenium;
    }
    
    public void run() {
      selenium.stop();
    }
  }
  


  public void stop()
  {
    int numTries = 0;
    Exception shutDownException = null;
    


    try
    {
      if (shutDownHook != null) {
        Runtime.getRuntime().removeShutdownHook(shutDownHook);
      }
    }
    catch (IllegalStateException localIllegalStateException) {}
    

    while (numTries <= 8) {
      numTries++;
      try
      {
        synchronized (shutdownLock) {
          server.stop();
        }
        

      }
      catch (Exception ex)
      {
        shutDownException = ex;
      }
    }
    


    stopAllBrowsers();
    
    if ((numTries > 8) && 
      (null != shutDownException)) {
      throw new RuntimeException(shutDownException);
    }
  }
  
  private void stopAllBrowsers()
  {
    for (SessionId sessionId : driverSessions.getSessions()) {
      Session session = driverSessions.get(sessionId);
      DeleteSession deleteSession = new DeleteSession(session);
      try {
        deleteSession.call();
        driverSessions.deleteSession(sessionId);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  public static void main(String[] argv) {
    StandaloneConfiguration configuration = new StandaloneConfiguration();
    JCommander jCommander = new JCommander(configuration, argv);
    jCommander.setProgramName("selenium-3-server");
    
    if (help) {
      StringBuilder message = new StringBuilder();
      jCommander.usage(message);
      System.err.println(message.toString());
      return;
    }
    
    SeleniumServer server = new SeleniumServer(configuration);
    server.boot();
  }
  
  public static void usage(String msg) {
    if (msg != null) {
      System.out.println(msg);
    }
    StandaloneConfiguration args = new StandaloneConfiguration();
    JCommander jCommander = new JCommander(args);
    jCommander.usage();
  }
}
