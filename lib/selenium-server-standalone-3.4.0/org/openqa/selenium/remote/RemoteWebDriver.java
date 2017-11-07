package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Beta;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Cookie.Builder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LoggingHandler;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;


















@Augmentable
public class RemoteWebDriver
  implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText, FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities, Interactive, TakesScreenshot
{
  private static final Logger logger = Logger.getLogger(RemoteWebDriver.class.getName());
  private Level level = Level.FINE;
  
  private ErrorHandler errorHandler = new ErrorHandler();
  private CommandExecutor executor;
  private Capabilities capabilities;
  private SessionId sessionId;
  private FileDetector fileDetector = new UselessFileDetector();
  
  private ExecuteMethod executeMethod;
  
  private JsonToWebElementConverter converter;
  private RemoteKeyboard keyboard;
  private RemoteMouse mouse;
  private Logs remoteLogs;
  private LocalLogs localLogs;
  
  protected RemoteWebDriver()
  {
    init(new DesiredCapabilities());
  }
  
  public RemoteWebDriver(Capabilities desiredCapabilities) {
    this(new HttpCommandExecutor(null), desiredCapabilities);
  }
  
  public RemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
    this.executor = executor;
    
    init(desiredCapabilities);
    
    if ((executor instanceof NeedsLocalLogs)) {
      ((NeedsLocalLogs)executor).setLocalLogs(localLogs);
    }
    try
    {
      startClient(desiredCapabilities);
    } catch (RuntimeException e) {
      try {
        stopClient(desiredCapabilities);
      }
      catch (Exception localException) {}
      

      throw e;
    }
    try
    {
      startSession(desiredCapabilities);
    } catch (RuntimeException e) {
      try {
        quit();
      }
      catch (Exception localException1) {}
      

      throw e;
    }
  }
  





  @Deprecated
  public RemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities, Capabilities requiredCapabilities)
  {
    this(executor, desiredCapabilities.merge(requiredCapabilities));
  }
  






  @Deprecated
  public RemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities, Capabilities requiredCapabilities)
  {
    this(new HttpCommandExecutor(remoteAddress), desiredCapabilities, requiredCapabilities);
  }
  
  public RemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities)
  {
    this(new HttpCommandExecutor(remoteAddress), desiredCapabilities);
  }
  
  private void init(Capabilities capabilities) {
    capabilities = capabilities == null ? new DesiredCapabilities() : capabilities;
    
    logger.addHandler(LoggingHandler.getInstance());
    
    converter = new JsonToWebElementConverter(this);
    executeMethod = new RemoteExecuteMethod(this);
    keyboard = new RemoteKeyboard(executeMethod);
    mouse = new RemoteMouse(executeMethod);
    
    ImmutableSet.Builder<String> builder = new ImmutableSet.Builder();
    
    boolean isProfilingEnabled = capabilities.is("webdriver.logging.profiler.enabled");
    if (isProfilingEnabled) {
      builder.add("profiler");
    }
    
    LoggingPreferences mergedLoggingPrefs = new LoggingPreferences();
    mergedLoggingPrefs.addPreferences((LoggingPreferences)capabilities.getCapability("loggingPrefs"));
    
    if ((!mergedLoggingPrefs.getEnabledLogTypes().contains("client")) || 
      (mergedLoggingPrefs.getLevel("client") != Level.OFF)) {
      builder.add("client");
    }
    
    Set<String> logTypesToInclude = builder.build();
    
    LocalLogs performanceLogger = LocalLogs.getStoringLoggerInstance(logTypesToInclude);
    LocalLogs clientLogs = LocalLogs.getHandlerBasedLoggerInstance(LoggingHandler.getInstance(), logTypesToInclude);
    
    localLogs = LocalLogs.getCombinedLogsHolder(clientLogs, performanceLogger);
    remoteLogs = new RemoteLogs(executeMethod, localLogs);
  }
  








  public void setFileDetector(FileDetector detector)
  {
    if (detector == null) {
      throw new WebDriverException("You may not set a file detector that is null");
    }
    fileDetector = detector;
  }
  
  public SessionId getSessionId() {
    return sessionId;
  }
  
  protected void setSessionId(String opaqueKey) {
    sessionId = new SessionId(opaqueKey);
  }
  
  protected void startSession(Capabilities desiredCapabilities) {
    startSession(desiredCapabilities, null);
  }
  

  protected void startSession(Capabilities desiredCapabilities, Capabilities requiredCapabilities)
  {
    ImmutableMap.Builder<String, Capabilities> paramBuilder = new ImmutableMap.Builder();
    
    paramBuilder.put("desiredCapabilities", desiredCapabilities);
    if (requiredCapabilities != null) {
      paramBuilder.put("requiredCapabilities", requiredCapabilities);
    }
    Map<String, ?> parameters = paramBuilder.build();
    
    Response response = execute("newSession", parameters);
    
    Map<String, Object> rawCapabilities = (Map)response.getValue();
    DesiredCapabilities returnedCapabilities = new DesiredCapabilities();
    for (Map.Entry<String, Object> entry : rawCapabilities.entrySet())
    {
      if (!"platform".equals(entry.getKey()))
      {

        returnedCapabilities.setCapability((String)entry.getKey(), entry.getValue()); }
    }
    String platformString = (String)rawCapabilities.get("platform");
    Platform platform;
    try { Platform platform;
      if ((platformString == null) || ("".equals(platformString))) {
        platform = Platform.ANY;
      } else {
        platform = Platform.valueOf(platformString);
      }
    }
    catch (IllegalArgumentException e) {
      Platform platform;
      platform = Platform.extractFromSysProperty(platformString);
    }
    returnedCapabilities.setPlatform(platform);
    
    if (rawCapabilities.containsKey("javascriptEnabled")) {
      Object raw = rawCapabilities.get("javascriptEnabled");
      if ((raw instanceof String)) {
        returnedCapabilities.setCapability("javascriptEnabled", Boolean.parseBoolean((String)raw));
      } else if ((raw instanceof Boolean)) {
        returnedCapabilities.setCapability("javascriptEnabled", ((Boolean)raw).booleanValue());
      }
    } else {
      returnedCapabilities.setCapability("javascriptEnabled", true);
    }
    
    capabilities = returnedCapabilities;
    sessionId = new SessionId(response.getSessionId());
  }
  




  protected void startClient() {}
  




  protected void startClient(Capabilities desiredCapabilities)
  {
    startClient();
  }
  





  @Deprecated
  protected void startClient(Capabilities desiredCapabilities, Capabilities requiredCapabilities)
  {
    startClient(desiredCapabilities.merge(requiredCapabilities));
  }
  




  protected void stopClient() {}
  




  protected void stopClient(Capabilities desiredCapbilities)
  {
    stopClient();
  }
  





  @Deprecated
  protected void stopClient(Capabilities desiredCapabilities, Capabilities requiredCapabilities)
  {
    stopClient(desiredCapabilities.merge(requiredCapabilities));
  }
  
  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }
  
  public void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }
  
  public CommandExecutor getCommandExecutor() {
    return executor;
  }
  
  protected void setCommandExecutor(CommandExecutor executor) {
    this.executor = executor;
  }
  
  public Capabilities getCapabilities() {
    return capabilities;
  }
  
  public void get(String url) {
    execute("get", ImmutableMap.of("url", url));
  }
  
  public String getTitle() {
    Response response = execute("getTitle");
    Object value = response.getValue();
    return value == null ? "" : value.toString();
  }
  
  public String getCurrentUrl() {
    Response response = execute("getCurrentUrl");
    if ((response == null) || (response.getValue() == null)) {
      throw new WebDriverException("Remote browser did not respond to getCurrentUrl");
    }
    return response.getValue().toString();
  }
  
  public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
    Response response = execute("screenshot");
    Object result = response.getValue();
    if ((result instanceof String)) {
      String base64EncodedPng = (String)result;
      return outputType.convertFromBase64Png(base64EncodedPng); }
    if ((result instanceof byte[])) {
      String base64EncodedPng = new String((byte[])result);
      return outputType.convertFromBase64Png(base64EncodedPng);
    }
    throw new RuntimeException(String.format("Unexpected result for %s command: %s", new Object[] { "screenshot", result
    
      .getClass().getName() + " instance" }));
  }
  
  public List<WebElement> findElements(By by)
  {
    return by.findElements(this);
  }
  
  public WebElement findElement(By by) {
    return by.findElement(this);
  }
  
  protected WebElement findElement(String by, String using) {
    if (using == null) {
      throw new IllegalArgumentException("Cannot find elements when the selector is null.");
    }
    
    Response response = execute("findElement", 
      ImmutableMap.of("using", by, "value", using));
    Object value = response.getValue();
    try
    {
      element = (WebElement)value;
    } catch (ClassCastException ex) { WebElement element;
      throw new WebDriverException("Returned value cannot be converted to WebElement: " + value, ex); }
    WebElement element;
    setFoundBy(this, element, by, using);
    return element;
  }
  
  protected void setFoundBy(SearchContext context, WebElement element, String by, String using) {
    if ((element instanceof RemoteWebElement)) {
      RemoteWebElement remoteElement = (RemoteWebElement)element;
      remoteElement.setFoundBy(context, by, using);
      remoteElement.setFileDetector(getFileDetector());
    }
  }
  
  protected List<WebElement> findElements(String by, String using)
  {
    if (using == null) {
      throw new IllegalArgumentException("Cannot find elements when the selector is null.");
    }
    
    Response response = execute("findElements", 
      ImmutableMap.of("using", by, "value", using));
    Object value = response.getValue();
    try
    {
      allElements = (List)value;
    } catch (ClassCastException ex) { List<WebElement> allElements;
      throw new WebDriverException("Returned value cannot be converted to List<WebElement>: " + value, ex); }
    List<WebElement> allElements;
    for (WebElement element : allElements) {
      setFoundBy(this, element, by, using);
    }
    return allElements;
  }
  
  public WebElement findElementById(String using) {
    return findElement("id", using);
  }
  
  public List<WebElement> findElementsById(String using) {
    return findElements("id", using);
  }
  
  public WebElement findElementByLinkText(String using) {
    return findElement("link text", using);
  }
  
  public List<WebElement> findElementsByLinkText(String using) {
    return findElements("link text", using);
  }
  
  public WebElement findElementByPartialLinkText(String using) {
    return findElement("partial link text", using);
  }
  
  public List<WebElement> findElementsByPartialLinkText(String using) {
    return findElements("partial link text", using);
  }
  
  public WebElement findElementByTagName(String using) {
    return findElement("tag name", using);
  }
  
  public List<WebElement> findElementsByTagName(String using) {
    return findElements("tag name", using);
  }
  
  public WebElement findElementByName(String using) {
    return findElement("name", using);
  }
  
  public List<WebElement> findElementsByName(String using) {
    return findElements("name", using);
  }
  
  public WebElement findElementByClassName(String using) {
    return findElement("class name", using);
  }
  
  public List<WebElement> findElementsByClassName(String using) {
    return findElements("class name", using);
  }
  
  public WebElement findElementByCssSelector(String using) {
    return findElement("css selector", using);
  }
  
  public List<WebElement> findElementsByCssSelector(String using) {
    return findElements("css selector", using);
  }
  
  public WebElement findElementByXPath(String using) {
    return findElement("xpath", using);
  }
  
  public List<WebElement> findElementsByXPath(String using) {
    return findElements("xpath", using);
  }
  

  public String getPageSource()
  {
    return (String)execute("getPageSource").getValue();
  }
  
  public void close() {
    execute("close");
  }
  
  public void quit()
  {
    if (sessionId == null) {
      return;
    }
    try
    {
      execute("quit");
      
      sessionId = null;
      stopClient();
    }
    finally
    {
      sessionId = null;
      stopClient();
    }
  }
  
  public Set<String> getWindowHandles()
  {
    Response response = execute("getWindowHandles");
    Object value = response.getValue();
    try {
      List<String> returnedValues = (List)value;
      return new LinkedHashSet(returnedValues);
    } catch (ClassCastException ex) {
      throw new WebDriverException("Returned value cannot be converted to List<String>: " + value, ex);
    }
  }
  
  public String getWindowHandle()
  {
    return String.valueOf(execute("getCurrentWindowHandle").getValue());
  }
  
  public Object executeScript(String script, Object... args) {
    if (!capabilities.is("javascriptEnabled")) {
      throw new UnsupportedOperationException("You must be using an underlying instance of WebDriver that supports executing javascript");
    }
    


    script = script.replaceAll("\"", "\\\"");
    
    Iterable<Object> convertedArgs = Iterables.transform(
      Lists.newArrayList(args), new WebElementToJsonConverter());
    
    Map<String, ?> params = ImmutableMap.of("script", script, "args", 
    
      Lists.newArrayList(convertedArgs));
    
    return execute("executeScript", params).getValue();
  }
  
  public Object executeAsyncScript(String script, Object... args) {
    if (!isJavascriptEnabled()) {
      throw new UnsupportedOperationException("You must be using an underlying instance of WebDriver that supports executing javascript");
    }
    


    script = script.replaceAll("\"", "\\\"");
    
    Iterable<Object> convertedArgs = Iterables.transform(
      Lists.newArrayList(args), new WebElementToJsonConverter());
    
    Map<String, ?> params = ImmutableMap.of("script", script, "args", 
      Lists.newArrayList(convertedArgs));
    
    return execute("executeAsyncScript", params).getValue();
  }
  
  private boolean isJavascriptEnabled() {
    return capabilities.is("javascriptEnabled");
  }
  
  public WebDriver.TargetLocator switchTo() {
    return new RemoteTargetLocator();
  }
  
  public WebDriver.Navigation navigate() {
    return new RemoteNavigation(null);
  }
  
  public WebDriver.Options manage() {
    return new RemoteWebDriverOptions();
  }
  
  protected void setElementConverter(JsonToWebElementConverter converter) {
    this.converter = converter;
  }
  
  protected JsonToWebElementConverter getElementConverter() {
    return converter;
  }
  




  public void setLogLevel(Level level)
  {
    this.level = level;
  }
  
  protected Response execute(String driverCommand, Map<String, ?> parameters) {
    Command command = new Command(sessionId, driverCommand, parameters);
    

    long start = System.currentTimeMillis();
    String currentName = Thread.currentThread().getName();
    Thread.currentThread().setName(
      String.format("Forwarding %s on session %s to remote", new Object[] { driverCommand, sessionId }));
    try {
      log(sessionId, command.getName(), command, When.BEFORE);
      Response response = executor.execute(command);
      log(sessionId, command.getName(), command, When.AFTER);
      
      if (response == null) {
        return null;
      }
      


      Object value = converter.apply(response.getValue());
      response.setValue(value);
    } catch (WebDriverException e) {
      throw e;
    } catch (Exception e) {
      log(sessionId, command.getName(), command, When.EXCEPTION);
      String errorMessage = "Error communicating with the remote browser. It may have died.";
      
      if (driverCommand.equals("newSession")) {
        errorMessage = "Could not start a new session. Possible causes are invalid address of the remote server or browser start-up failure.";
      }
      
      UnreachableBrowserException ube = new UnreachableBrowserException(errorMessage, e);
      if (getSessionId() != null) {
        ube.addInfo("Session ID", getSessionId().toString());
      }
      if (getCapabilities() != null) {
        ube.addInfo("Capabilities", getCapabilities().toString());
      }
      throw ube;
    } finally {
      Thread.currentThread().setName(currentName);
    }
    Response response;
    try {
      errorHandler.throwIfResponseFailed(response, System.currentTimeMillis() - start);
    } catch (WebDriverException ex) {
      if ((parameters != null) && (parameters.containsKey("using")) && (parameters.containsKey("value"))) {
        ex.addInfo("*** Element info", 
        
          String.format("{Using=%s, value=%s}", new Object[] {parameters
          
          .get("using"), parameters
          .get("value") }));
      }
      ex.addInfo("Driver info", getClass().getName());
      if (getSessionId() != null) {
        ex.addInfo("Session ID", getSessionId().toString());
      }
      if (getCapabilities() != null) {
        ex.addInfo("Capabilities", getCapabilities().toString());
      }
      throw ex;
    }
    return response;
  }
  
  protected Response execute(String command) {
    return execute(command, ImmutableMap.of());
  }
  
  protected ExecuteMethod getExecuteMethod() {
    return executeMethod;
  }
  
  public void perform(Collection<Sequence> actions)
  {
    execute("actions", ImmutableMap.of("actions", actions));
  }
  
  public void resetInputState()
  {
    execute("clearActionState");
  }
  
  public Keyboard getKeyboard() {
    return keyboard;
  }
  
  public Mouse getMouse() {
    return mouse;
  }
  







  protected void log(SessionId sessionId, String commandName, Object toLog, When when)
  {
    String text = "" + toLog;
    if (((commandName.equals("executeScript")) || 
      (commandName.equals("executeAsyncScript"))) && 
      (text.length() > 100) && (Boolean.getBoolean("webdriver.remote.shorten_log_messages"))) {
      text = text.substring(0, 100) + "...";
    }
    
    switch (1.$SwitchMap$org$openqa$selenium$remote$RemoteWebDriver$When[when.ordinal()]) {
    case 1: 
      logger.log(level, "Executing: " + commandName + " " + text);
      break;
    case 2: 
      logger.log(level, "Executed: " + text);
      break;
    case 3: 
      logger.log(level, "Exception: " + text);
      break;
    default: 
      logger.log(level, text);
    }
  }
  
  public FileDetector getFileDetector()
  {
    return fileDetector;
  }
  
  protected class RemoteWebDriverOptions implements WebDriver.Options {
    protected RemoteWebDriverOptions() {}
    
    @Beta
    public Logs logs() { return remoteLogs; }
    
    public void addCookie(Cookie cookie)
    {
      cookie.validate();
      execute("addCookie", ImmutableMap.of("cookie", cookie));
    }
    
    public void deleteCookieNamed(String name) {
      execute("deleteCookie", ImmutableMap.of("name", name));
    }
    
    public void deleteCookie(Cookie cookie) {
      deleteCookieNamed(cookie.getName());
    }
    
    public void deleteAllCookies() {
      execute("deleteAllCookies");
    }
    
    public Set<Cookie> getCookies()
    {
      Object returned = execute("getCookies").getValue();
      
      Set<Cookie> toReturn = new HashSet();
      

      List<Map<String, Object>> cookies = (List)new JsonToBeanConverter().convert(List.class, returned);
      if (cookies == null) {
        return toReturn;
      }
      
      for (Map<String, Object> rawCookie : cookies) {
        String name = (String)rawCookie.get("name");
        String value = (String)rawCookie.get("value");
        String path = (String)rawCookie.get("path");
        String domain = (String)rawCookie.get("domain");
        boolean secure = (rawCookie.containsKey("secure")) && (((Boolean)rawCookie.get("secure")).booleanValue());
        boolean httpOnly = (rawCookie.containsKey("httpOnly")) && (((Boolean)rawCookie.get("httpOnly")).booleanValue());
        
        Number expiryNum = (Number)rawCookie.get("expiry");
        
        Date expiry = expiryNum == null ? null : new Date(TimeUnit.SECONDS.toMillis(expiryNum.longValue()));
        
        toReturn.add(new Cookie.Builder(name, value)
          .path(path)
          .domain(domain)
          .isSecure(secure)
          .isHttpOnly(httpOnly)
          .expiresOn(expiry)
          .build());
      }
      
      return toReturn;
    }
    
    public Cookie getCookieNamed(String name) {
      Set<Cookie> allCookies = getCookies();
      for (Cookie cookie : allCookies) {
        if (cookie.getName().equals(name)) {
          return cookie;
        }
      }
      return null;
    }
    
    public WebDriver.Timeouts timeouts() {
      return new RemoteTimeouts();
    }
    
    public WebDriver.ImeHandler ime() {
      return new RemoteInputMethodManager();
    }
    
    @Beta
    public WebDriver.Window window() {
      return new RemoteWindow();
    }
    
    protected class RemoteInputMethodManager implements WebDriver.ImeHandler {
      protected RemoteInputMethodManager() {}
      
      public List<String> getAvailableEngines() {
        Response response = execute("imeGetAvailableEngines");
        return (List)response.getValue();
      }
      
      public String getActiveEngine() {
        Response response = execute("imeGetActiveEngine");
        return (String)response.getValue();
      }
      
      public boolean isActivated() {
        Response response = execute("imeIsActivated");
        return ((Boolean)response.getValue()).booleanValue();
      }
      
      public void deactivate() {
        execute("imeDeactivate");
      }
      

      public void activateEngine(String engine) { execute("imeActivateEngine", ImmutableMap.of("engine", engine)); }
    }
    
    protected class RemoteTimeouts implements WebDriver.Timeouts {
      protected RemoteTimeouts() {}
      
      public WebDriver.Timeouts implicitlyWait(long time, TimeUnit unit) {
        execute("setTimeout", ImmutableMap.of("implicit", 
          Long.valueOf(TimeUnit.MILLISECONDS.convert(time, unit))));
        return this;
      }
      
      public WebDriver.Timeouts setScriptTimeout(long time, TimeUnit unit) {
        execute("setTimeout", ImmutableMap.of("script", 
          Long.valueOf(TimeUnit.MILLISECONDS.convert(time, unit))));
        return this;
      }
      
      public WebDriver.Timeouts pageLoadTimeout(long time, TimeUnit unit) {
        execute("setTimeout", ImmutableMap.of("page load", 
          Long.valueOf(TimeUnit.MILLISECONDS.convert(time, unit))));
        return this;
      } }
    
    @Beta
    protected class RemoteWindow implements WebDriver.Window { Map<String, Object> rawPoint;
      
      protected RemoteWindow() {}
      
      public void setSize(Dimension targetSize) { execute("setCurrentWindowSize", 
          ImmutableMap.of("width", Integer.valueOf(width), "height", Integer.valueOf(height)));
      }
      
      public void setPosition(Point targetPosition) {
        execute("setWindowPosition", 
          ImmutableMap.of("x", Integer.valueOf(x), "y", Integer.valueOf(y)));
      }
      
      public Dimension getSize()
      {
        Response response = execute("getCurrentWindowSize");
        
        Map<String, Object> rawSize = (Map)response.getValue();
        
        int width = ((Number)rawSize.get("width")).intValue();
        int height = ((Number)rawSize.get("height")).intValue();
        
        return new Dimension(width, height);
      }
      

      public Point getPosition()
      {
        Response response = execute("getWindowPosition", 
          ImmutableMap.of("windowHandle", "current"));
        rawPoint = ((Map)response.getValue());
        
        int x = ((Number)rawPoint.get("x")).intValue();
        int y = ((Number)rawPoint.get("y")).intValue();
        
        return new Point(x, y);
      }
      
      public void maximize() {
        execute("maximizeCurrentWindow");
      }
      
      public void fullscreen() {
        execute("fullscreenCurrentWindow");
      }
    }
  }
  
  private class RemoteNavigation implements WebDriver.Navigation {
    private RemoteNavigation() {}
    
    public void back() { execute("goBack"); }
    
    public void forward()
    {
      execute("goForward");
    }
    
    public void to(String url) {
      get(url);
    }
    
    public void to(URL url) {
      get(String.valueOf(url));
    }
    

    public void refresh() { execute("refresh"); }
  }
  
  protected class RemoteTargetLocator implements WebDriver.TargetLocator {
    protected RemoteTargetLocator() {}
    
    public WebDriver frame(int frameIndex) {
      execute("switchToFrame", ImmutableMap.of("id", Integer.valueOf(frameIndex)));
      return RemoteWebDriver.this;
    }
    
    public WebDriver frame(String frameName) {
      String name = frameName.replaceAll("(['\"\\\\#.:;,!?+<>=~*^$|%&@`{}\\-/\\[\\]\\(\\)])", "\\\\$1");
      List<WebElement> frameElements = findElements(
        By.cssSelector("frame[name='" + name + "'],iframe[name='" + name + "']"));
      if (frameElements.size() == 0) {
        frameElements = findElements(
          By.cssSelector("frame#" + name + ",iframe#" + name));
      }
      if (frameElements.size() == 0) {
        throw new NoSuchFrameException("No frame element found by name or id " + frameName);
      }
      return frame((WebElement)frameElements.get(0));
    }
    
    public WebDriver frame(WebElement frameElement) {
      Object elementAsJson = new WebElementToJsonConverter().apply(frameElement);
      execute("switchToFrame", ImmutableMap.of("id", elementAsJson));
      return RemoteWebDriver.this;
    }
    
    public WebDriver parentFrame() {
      execute("switchToParentFrame");
      return RemoteWebDriver.this;
    }
    
    public WebDriver window(String windowHandleOrName) {
      try {
        execute("switchToWindow", ImmutableMap.of("handle", windowHandleOrName));
        return RemoteWebDriver.this;
      }
      catch (NoSuchWindowException nsw) {
        String original = getWindowHandle();
        for (String handle : getWindowHandles()) {
          switchTo().window(handle);
          if (windowHandleOrName.equals(executeScript("return window.name", new Object[0]))) {
            return RemoteWebDriver.this;
          }
        }
        switchTo().window(original);
        throw nsw;
      }
    }
    
    public WebDriver defaultContent() {
      Map<String, Object> frameId = Maps.newHashMap();
      frameId.put("id", null);
      execute("switchToFrame", frameId);
      return RemoteWebDriver.this;
    }
    
    public WebElement activeElement() {
      Response response = execute("getActiveElement");
      return (WebElement)response.getValue();
    }
    
    public Alert alert() {
      execute("getAlertText");
      return new RemoteWebDriver.RemoteAlert(RemoteWebDriver.this);
    }
  }
  
  private class RemoteAlert implements Alert
  {
    public RemoteAlert() {}
    
    public void dismiss()
    {
      execute("dismissAlert");
    }
    
    public void accept() {
      execute("acceptAlert");
    }
    
    public String getText() {
      return (String)execute("getAlertText").getValue();
    }
    
    public void sendKeys(String keysToSend) {
      execute("setAlertValue", ImmutableMap.of("text", keysToSend));
    }
    
    @Beta
    public void setCredentials(Credentials credentials) {
      if (!(credentials instanceof UserAndPassword)) {
        throw new RuntimeException("Unsupported credentials: " + credentials);
      }
      
      UserAndPassword userAndPassword = (UserAndPassword)credentials;
      execute("setAlertCredentials", 
      
        ImmutableMap.of("username", userAndPassword
        .getUsername(), "password", userAndPassword
        .getPassword()));
    }
    







    @Beta
    public void authenticateUsing(Credentials credentials)
    {
      setCredentials(credentials);
      accept();
    }
  }
  
  public static enum When {
    BEFORE, 
    AFTER, 
    EXCEPTION;
    
    private When() {}
  }
  
  public String toString() { Capabilities caps = getCapabilities();
    if (caps == null) {
      return super.toString();
    }
    return String.format("%s: %s on %s (%s)", new Object[] { getClass().getSimpleName(), caps
      .getBrowserName(), caps.getPlatform(), getSessionId() });
  }
}
