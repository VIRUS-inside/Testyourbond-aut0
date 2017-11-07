package org.openqa.selenium.remote.http;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.net.MediaType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.net.Urls;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandCodec;
import org.openqa.selenium.remote.JsonToBeanConverter;
import org.openqa.selenium.remote.SessionId;




























































































public abstract class AbstractHttpCommandCodec
  implements CommandCodec<HttpRequest>
{
  private static final Splitter PATH_SPLITTER = Splitter.on('/').omitEmptyStrings();
  
  private static final String SESSION_ID_PARAM = "sessionId";
  private final ConcurrentHashMap<String, CommandSpec> nameToSpec = new ConcurrentHashMap();
  private final Map<String, String> aliases = new HashMap();
  private final BeanToJsonConverter beanToJsonConverter = new BeanToJsonConverter();
  private final JsonToBeanConverter jsonToBeanConverter = new JsonToBeanConverter();
  
  public AbstractHttpCommandCodec() {
    defineCommand("status", get("/status"));
    
    defineCommand("getAllSessions", get("/sessions"));
    defineCommand("newSession", post("/session"));
    defineCommand("getCapabilities", get("/session/:sessionId"));
    defineCommand("quit", delete("/session/:sessionId"));
    
    defineCommand("getSessionLogs", post("/logs"));
    defineCommand("getLog", post("/session/:sessionId/log"));
    defineCommand("getAvailableLogTypes", get("/session/:sessionId/log/types"));
    
    defineCommand("switchToFrame", post("/session/:sessionId/frame"));
    defineCommand("switchToParentFrame", post("/session/:sessionId/frame/parent"));
    
    defineCommand("close", delete("/session/:sessionId/window"));
    defineCommand("switchToWindow", post("/session/:sessionId/window"));
    
    defineCommand("fullscreenCurrentWindow", post("/session/:sessionId/window/fullscreen"));
    
    defineCommand("getCurrentUrl", get("/session/:sessionId/url"));
    defineCommand("get", post("/session/:sessionId/url"));
    defineCommand("goBack", post("/session/:sessionId/back"));
    defineCommand("goForward", post("/session/:sessionId/forward"));
    defineCommand("refresh", post("/session/:sessionId/refresh"));
    
    defineCommand("setAlertCredentials", post("/session/:sessionId/alert/credentials"));
    
    defineCommand("uploadFile", post("/session/:sessionId/file"));
    defineCommand("screenshot", get("/session/:sessionId/screenshot"));
    defineCommand("elementScreenshot", get("/session/:sessionId/screenshot/:id"));
    defineCommand("getTitle", get("/session/:sessionId/title"));
    
    defineCommand("findElement", post("/session/:sessionId/element"));
    defineCommand("findElements", post("/session/:sessionId/elements"));
    defineCommand("getElementProperty", get("/session/:sessionId/element/:id/property/:name"));
    defineCommand("clickElement", post("/session/:sessionId/element/:id/click"));
    defineCommand("clearElement", post("/session/:sessionId/element/:id/clear"));
    defineCommand("getElementValueOfCssProperty", 
    
      get("/session/:sessionId/element/:id/css/:propertyName"));
    defineCommand("findChildElement", post("/session/:sessionId/element/:id/element"));
    defineCommand("findChildElements", post("/session/:sessionId/element/:id/elements"));
    defineCommand("isElementEnabled", get("/session/:sessionId/element/:id/enabled"));
    defineCommand("elementEquals", get("/session/:sessionId/element/:id/equals/:other"));
    defineCommand("getElementRect", get("/session/:sessionId/element/:id/rect"));
    defineCommand("getElementLocation", get("/session/:sessionId/element/:id/location"));
    defineCommand("getElementTagName", get("/session/:sessionId/element/:id/name"));
    defineCommand("isElementSelected", get("/session/:sessionId/element/:id/selected"));
    defineCommand("getElementSize", get("/session/:sessionId/element/:id/size"));
    defineCommand("getElementText", get("/session/:sessionId/element/:id/text"));
    defineCommand("sendKeysToElement", post("/session/:sessionId/element/:id/value"));
    
    defineCommand("getCookies", get("/session/:sessionId/cookie"));
    defineCommand("getCookie", get("/session/:sessionId/cookie/:name"));
    defineCommand("addCookie", post("/session/:sessionId/cookie"));
    defineCommand("deleteAllCookies", delete("/session/:sessionId/cookie"));
    defineCommand("deleteCookie", delete("/session/:sessionId/cookie/:name"));
    
    defineCommand("setTimeout", post("/session/:sessionId/timeouts"));
    defineCommand("setScriptTimeout", post("/session/:sessionId/timeouts/async_script"));
    defineCommand("implicitlyWait", post("/session/:sessionId/timeouts/implicit_wait"));
    
    defineCommand("getStatus", get("/session/:sessionId/application_cache/status"));
    defineCommand("isBrowserOnline", get("/session/:sessionId/browser_connection"));
    defineCommand("setBrowserOnline", post("/session/:sessionId/browser_connection"));
    defineCommand("getLocation", get("/session/:sessionId/location"));
    defineCommand("setLocation", post("/session/:sessionId/location"));
    
    defineCommand("getScreenOrientation", get("/session/:sessionId/orientation"));
    defineCommand("setScreenOrientation", post("/session/:sessionId/orientation"));
    defineCommand("getScreenRotation", get("/session/:sessionId/rotation"));
    defineCommand("setScreenRotation", post("/session/:sessionId/rotation"));
    
    defineCommand("imeGetAvailableEngines", get("/session/:sessionId/ime/available_engines"));
    defineCommand("imeGetActiveEngine", get("/session/:sessionId/ime/active_engine"));
    defineCommand("imeIsActivated", get("/session/:sessionId/ime/activated"));
    defineCommand("imeDeactivate", post("/session/:sessionId/ime/deactivate"));
    defineCommand("imeActivateEngine", post("/session/:sessionId/ime/activate"));
    

    defineCommand("getNetworkConnection", get("/session/:sessionId/network_connection"));
    defineCommand("setNetworkConnection", post("/session/:sessionId/network_connection"));
    defineCommand("switchToContext", post("/session/:sessionId/context"));
    defineCommand("getCurrentContextHandle", get("/session/:sessionId/context"));
    defineCommand("getContextHandles", get("/session/:sessionId/contexts"));
  }
  
  public HttpRequest encode(Command command)
  {
    String name = (String)aliases.getOrDefault(command.getName(), command.getName());
    CommandSpec spec = (CommandSpec)nameToSpec.get(name);
    if (spec == null) {
      throw new UnsupportedCommandException(command.getName());
    }
    Map<String, ?> parameters = amendParameters(command.getName(), command.getParameters());
    String uri = buildUri(name, command.getSessionId(), parameters, spec);
    
    HttpRequest request = new HttpRequest(method, uri);
    
    if (HttpMethod.POST == method)
    {
      String content = beanToJsonConverter.convert(parameters);
      byte[] data = content.getBytes(Charsets.UTF_8);
      
      request.setHeader("Content-Length", String.valueOf(data.length));
      request.setHeader("Content-Type", MediaType.JSON_UTF_8.toString());
      request.setContent(data);
    }
    
    if (HttpMethod.GET == method) {
      request.setHeader("Cache-Control", "no-cache");
    }
    
    return request;
  }
  

  protected abstract Map<String, ?> amendParameters(String paramString, Map<String, ?> paramMap);
  
  public Command decode(HttpRequest encodedCommand)
  {
    String path = Strings.isNullOrEmpty(encodedCommand.getUri()) ? "/" : encodedCommand.getUri();
    ImmutableList<String> parts = ImmutableList.copyOf(PATH_SPLITTER.split(path));
    int minPathLength = Integer.MAX_VALUE;
    CommandSpec spec = null;
    String name = null;
    for (Map.Entry<String, CommandSpec> nameValue : nameToSpec.entrySet()) {
      if ((getValuepathSegments.size() < minPathLength) && 
        (((CommandSpec)nameValue.getValue()).isFor(encodedCommand.getMethod(), parts))) {
        name = (String)nameValue.getKey();
        spec = (CommandSpec)nameValue.getValue();
      }
    }
    if (name == null)
    {
      throw new UnsupportedCommandException(encodedCommand.getMethod() + " " + encodedCommand.getUri());
    }
    Object parameters = Maps.newHashMap();
    spec.parsePathParameters(parts, (Map)parameters);
    
    String content = encodedCommand.getContentString();
    if (!content.isEmpty())
    {
      HashMap<String, ?> tmp = (HashMap)jsonToBeanConverter.convert(HashMap.class, content);
      ((Map)parameters).putAll(tmp);
    }
    
    SessionId sessionId = null;
    if (((Map)parameters).containsKey("sessionId")) {
      String id = (String)((Map)parameters).remove("sessionId");
      if (id != null) {
        sessionId = new SessionId(id);
      }
    }
    
    return new Command(sessionId, name, (Map)parameters);
  }
  








  public void defineCommand(String name, HttpMethod method, String pathPattern)
  {
    defineCommand(name, new CommandSpec(method, pathPattern, null));
  }
  
  public void alias(String commandName, String isAnAliasFor)
  {
    aliases.put(commandName, isAnAliasFor);
  }
  
  protected void defineCommand(String name, CommandSpec spec) {
    Preconditions.checkNotNull(name, "null name");
    nameToSpec.put(name, spec);
  }
  
  protected static CommandSpec delete(String path) {
    return new CommandSpec(HttpMethod.DELETE, path, null);
  }
  
  protected static CommandSpec get(String path) {
    return new CommandSpec(HttpMethod.GET, path, null);
  }
  
  protected static CommandSpec post(String path) {
    return new CommandSpec(HttpMethod.POST, path, null);
  }
  



  private String buildUri(String commandName, SessionId sessionId, Map<String, ?> parameters, CommandSpec spec)
  {
    StringBuilder builder = new StringBuilder();
    for (UnmodifiableIterator localUnmodifiableIterator = pathSegments.iterator(); localUnmodifiableIterator.hasNext();) { String part = (String)localUnmodifiableIterator.next();
      if (!part.isEmpty())
      {


        builder.append("/");
        if (part.startsWith(":")) {
          builder.append(getParameter(part.substring(1), commandName, sessionId, parameters));
        } else
          builder.append(part);
      }
    }
    return builder.toString();
  }
  



  private String getParameter(String parameterName, String commandName, SessionId sessionId, Map<String, ?> parameters)
  {
    if ("sessionId".equals(parameterName)) {
      SessionId id = sessionId;
      Preconditions.checkArgument(id != null, "Session ID may not be null for command %s", commandName);
      return id.toString();
    }
    
    Object value = parameters.get(parameterName);
    Preconditions.checkArgument(value != null, "Missing required parameter \"%s\" for command %s", parameterName, commandName);
    
    return Urls.urlEncode(String.valueOf(value));
  }
  
  protected static class CommandSpec {
    private final HttpMethod method;
    private final String path;
    private final ImmutableList<String> pathSegments;
    
    private CommandSpec(HttpMethod method, String path) {
      this.method = ((HttpMethod)Preconditions.checkNotNull(method, "null method"));
      this.path = path;
      pathSegments = ImmutableList.copyOf(AbstractHttpCommandCodec.PATH_SPLITTER.split(path));
    }
    
    public boolean equals(Object o)
    {
      if ((o instanceof CommandSpec)) {
        CommandSpec that = (CommandSpec)o;
        return (method.equals(method)) && 
          (path.equals(path));
      }
      return false;
    }
    
    public int hashCode()
    {
      return Objects.hashCode(new Object[] { method, path });
    }
    






    boolean isFor(HttpMethod method, ImmutableList<String> parts)
    {
      if (!this.method.equals(method)) {
        return false;
      }
      
      if (parts.size() != pathSegments.size()) {
        return false;
      }
      
      for (int i = 0; i < parts.size(); i++) {
        String reqPart = (String)parts.get(i);
        String specPart = (String)pathSegments.get(i);
        if ((!specPart.startsWith(":")) && (!specPart.equals(reqPart))) {
          return false;
        }
      }
      
      return true;
    }
    
    void parsePathParameters(ImmutableList<String> parts, Map<String, Object> parameters) {
      for (int i = 0; i < parts.size(); i++) {
        if (((String)pathSegments.get(i)).startsWith(":")) {
          parameters.put(((String)pathSegments.get(i)).substring(1), parts.get(i));
        }
      }
    }
  }
}
