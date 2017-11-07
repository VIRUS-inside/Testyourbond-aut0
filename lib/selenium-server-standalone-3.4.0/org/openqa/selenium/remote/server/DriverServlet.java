package org.openqa.selenium.remote.server;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.selenium.logging.LoggingHandler;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;
import org.openqa.selenium.remote.server.xdrpc.CrossDomainRpc;
import org.openqa.selenium.remote.server.xdrpc.CrossDomainRpcLoader;


















public class DriverServlet
  extends HttpServlet
{
  public static final String SESSIONS_KEY = DriverServlet.class.getName() + ".sessions";
  
  public static final String SESSION_TIMEOUT_PARAMETER = "webdriver.server.session.timeout";
  
  public static final String BROWSER_TIMEOUT_PARAMETER = "webdriver.server.browser.timeout";
  private static final String CROSS_DOMAIN_RPC_PATH = "/xdrpc";
  private final StaticResourceHandler staticResourceHandler = new StaticResourceHandler(null);
  
  private final Supplier<DriverSessions> sessionsSupplier;
  private SessionCleaner sessionCleaner;
  private JsonHttpCommandHandler commandHandler;
  
  public DriverServlet()
  {
    sessionsSupplier = new DriverSessionsSupplier(null);
  }
  
  @VisibleForTesting
  DriverServlet(Supplier<DriverSessions> sessionsSupplier) {
    this.sessionsSupplier = sessionsSupplier;
  }
  
  public void init() throws ServletException
  {
    super.init();
    
    Logger logger = configureLogging();
    
    DriverSessions driverSessions = (DriverSessions)sessionsSupplier.get();
    commandHandler = new JsonHttpCommandHandler(driverSessions, logger);
    
    long sessionTimeOutInMs = getValueToUseInMs("webdriver.server.session.timeout", 1800L);
    long browserTimeoutInMs = getValueToUseInMs("webdriver.server.browser.timeout", 0L);
    
    if ((sessionTimeOutInMs > 0L) || (browserTimeoutInMs > 0L)) {
      createSessionCleaner(logger, driverSessions, sessionTimeOutInMs, browserTimeoutInMs);
    }
  }
  
  private synchronized Logger configureLogging() {
    Logger logger = getLogger();
    logger.addHandler(LoggingHandler.getInstance());
    
    Logger rootLogger = Logger.getLogger("");
    boolean sessionLoggerAttached = false;
    for (Handler handler : rootLogger.getHandlers()) {
      sessionLoggerAttached |= handler instanceof PerSessionLogHandler;
    }
    if (!sessionLoggerAttached) {
      rootLogger.addHandler(LoggingManager.perSessionLogHandler());
    }
    
    return logger;
  }
  
  @VisibleForTesting
  protected void createSessionCleaner(Logger logger, DriverSessions driverSessions, long sessionTimeOutInMs, long browserTimeoutInMs)
  {
    sessionCleaner = new SessionCleaner(driverSessions, logger, sessionTimeOutInMs, browserTimeoutInMs);
    sessionCleaner.start();
  }
  
  private long getValueToUseInMs(String propertyName, long defaultValue) {
    long time = defaultValue;
    String property = getServletContext().getInitParameter(propertyName);
    if (property != null) {
      time = Long.parseLong(property);
    }
    
    return TimeUnit.SECONDS.toMillis(time);
  }
  
  public void destroy()
  {
    getLogger().removeHandler(LoggingHandler.getInstance());
    if (sessionCleaner != null) {
      sessionCleaner.stopCleaner();
    }
  }
  
  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
  
  protected void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (request.getHeader("Origin") != null) {
      setAccessControlHeaders(response);
    }
    
    response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
    response.setHeader("Cache-Control", "no-cache");
    super.service(request, response);
  }
  






  private void setAccessControlHeaders(HttpServletResponse response)
  {
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "DELETE,GET,HEAD,POST");
    response.setHeader("Access-Control-Allow-Headers", "Accept,Content-Type");
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    if ((request.getPathInfo() == null) || ("/".equals(request.getPathInfo()))) {
      staticResourceHandler.redirectToHub(request, response);
    } else if (staticResourceHandler.isStaticResourceRequest(request)) {
      staticResourceHandler.service(request, response);
    } else {
      handleRequest(request, response);
    }
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    if ("/xdrpc".equalsIgnoreCase(request.getPathInfo())) {
      handleCrossDomainRpc(request, response);
    } else {
      handleRequest(request, response);
    }
  }
  
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    handleRequest(request, response);
  }
  

  private void handleCrossDomainRpc(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    throws ServletException, IOException
  {
    try
    {
      rpc = new CrossDomainRpcLoader().loadRpc(servletRequest);
    } catch (IllegalArgumentException e) { CrossDomainRpc rpc;
      servletResponse.setStatus(400);
      servletResponse.getOutputStream().println(e.getMessage());
      servletResponse.getOutputStream().flush(); return;
    }
    

    CrossDomainRpc rpc;
    
    HttpRequest request = new HttpRequest(HttpMethod.valueOf(rpc.getMethod()), rpc.getPath());
    request.setHeader("Content-Type", MediaType.JSON_UTF_8.toString());
    request.setContent(rpc.getContent());
    
    HttpResponse response = commandHandler.handleRequest(request);
    sendResponse(response, servletResponse);
  }
  
  protected void handleRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    throws ServletException, IOException
  {
    HttpRequest request = createInternalRequest(servletRequest);
    HttpResponse response = commandHandler.handleRequest(request);
    sendResponse(response, servletResponse);
  }
  
  private static HttpRequest createInternalRequest(HttpServletRequest servletRequest) throws IOException
  {
    String path = servletRequest.getPathInfo();
    if (Strings.isNullOrEmpty(path)) {
      path = "/";
    }
    
    request = new HttpRequest(HttpMethod.valueOf(servletRequest.getMethod().toUpperCase()), path);
    

    Enumeration<String> headerNames = servletRequest.getHeaderNames();
    for (Iterator localIterator1 = Collections.list(headerNames).iterator(); localIterator1.hasNext();) { name = (String)localIterator1.next();
      Enumeration<String> headerValues = servletRequest.getHeaders(name);
      for (String value : Collections.list(headerValues)) {
        request.setHeader(name, value);
      }
    }
    String name;
    Object stream = null;
    try {
      stream = servletRequest.getInputStream();
      request.setContent(ByteStreams.toByteArray((InputStream)stream));
      









      return request;
    }
    finally
    {
      if (stream != null) {
        try {
          ((InputStream)stream).close();
        }
        catch (IOException localIOException1) {}
      }
    }
  }
  


  private void sendResponse(HttpResponse response, HttpServletResponse servletResponse)
    throws IOException
  {
    servletResponse.setStatus(response.getStatus());
    for (Iterator localIterator1 = response.getHeaderNames().iterator(); localIterator1.hasNext();) { name = (String)localIterator1.next();
      for (String value : response.getHeaders(name))
        servletResponse.addHeader(name, value);
    }
    String name;
    Object output = servletResponse.getOutputStream();
    ((OutputStream)output).write(response.getContent());
    ((OutputStream)output).flush();
    ((OutputStream)output).close();
  }
  
  private class DriverSessionsSupplier implements Supplier<DriverSessions> { private DriverSessionsSupplier() {}
    
    public DriverSessions get() { Object attribute = getServletContext().getAttribute(DriverServlet.SESSIONS_KEY);
      if (attribute == null) {
        attribute = new DefaultDriverSessions();
      }
      return (DriverSessions)attribute;
    }
  }
  
  private static class StaticResourceHandler {
    private static final ImmutableMap<String, MediaType> MIME_TYPES = ImmutableMap.of("css", MediaType.CSS_UTF_8
      .withoutParameters(), "html", MediaType.HTML_UTF_8
      .withoutParameters(), "js", MediaType.JAVASCRIPT_UTF_8
      .withoutParameters());
    private static final String STATIC_RESOURCE_BASE_PATH = "/static/resource/";
    private static final String HUB_HTML_PATH = "/static/resource/hub.html";
    
    private StaticResourceHandler() {}
    
    public boolean isStaticResourceRequest(HttpServletRequest request) { return ("GET".equalsIgnoreCase(request.getMethod())) && 
        (Strings.nullToEmpty(request.getPathInfo()).startsWith("/static/resource/"));
    }
    
    public void redirectToHub(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      response.sendRedirect(request.getContextPath() + request.getServletPath() + "/static/resource/hub.html");
    }
    
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      Preconditions.checkArgument(isStaticResourceRequest(request));
      
      String path = String.format("/%s/%s", new Object[] {StaticResourceHandler.class
      
        .getPackage().getName().replace(".", "/"), request
        .getPathInfo().substring("/static/resource/".length()) });
      URL url = StaticResourceHandler.class.getResource(path);
      
      if (url == null) {
        response.sendError(404);
        return;
      }
      
      response.setStatus(200);
      
      String extension = Files.getFileExtension(path);
      if (MIME_TYPES.containsKey(extension)) {
        response.setContentType(((MediaType)MIME_TYPES.get(extension)).toString());
      }
      
      byte[] data = getResourceData(url);
      response.setContentLength(data.length);
      
      OutputStream output = response.getOutputStream();
      output.write(data);
      output.flush();
      output.close();
    }
    
    private byte[] getResourceData(URL url) throws IOException {
      InputStream stream = null;
      try {
        stream = url.openStream();
        return ByteStreams.toByteArray(stream);
      } finally {
        if (stream != null) {
          stream.close();
        }
      }
    }
  }
}
