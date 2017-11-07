package org.openqa.grid.internal.utils.configuration;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.openqa.grid.common.JSONConfigurationUtils;
import org.openqa.grid.common.exception.GridConfigurationException;




























public class StandaloneConfiguration
{
  public static final String DEFAULT_STANDALONE_CONFIG_FILE = "defaults/DefaultStandalone.json";
  static final Integer DEFAULT_TIMEOUT = Integer.valueOf(1800);
  



  static final Integer DEFAULT_BROWSER_TIMEOUT = Integer.valueOf(0);
  



  static final String DEFAULT_ROLE = "standalone";
  



  static final Integer DEFAULT_PORT = Integer.valueOf(4444);
  



  static final Boolean DEFAULT_DEBUG_TOGGLE = Boolean.valueOf(false);
  







  @Parameter(names={"--version", "-version"}, description="Displays the version and exits.")
  public boolean version;
  







  @Expose(serialize=false)
  @Parameter(names={"-avoidProxy"}, description="DO NOT USE: Hack to allow selenium 3.0 server run in SauceLabs", hidden=true)
  private boolean avoidProxy;
  







  @Expose(serialize=false)
  @Parameter(names={"-browserSideLog"}, description="DO NOT USE: Provided for compatibility with 2.0", hidden=true)
  private boolean browserSideLog;
  







  @Expose(serialize=false)
  @Parameter(names={"-captureLogsOnQuit"}, description="DO NOT USE: Provided for compatibility with 2.0", hidden=true)
  private boolean captureLogsOnQuit;
  







  @Expose(serialize=false)
  @Parameter(names={"--help", "-help", "-h"}, help=true, hidden=true, description="Displays this help.")
  public boolean help;
  






  @Expose
  @Parameter(names={"-browserTimeout"}, description="<Integer> in seconds : number of seconds a browser session is allowed to hang while a WebDriver command is running (example: driver.get(url)). If the timeout is reached while a WebDriver command is still processing, the session will quit. Minimum value is 60. An unspecified, zero, or negative value means wait indefinitely.")
  public Integer browserTimeout = DEFAULT_BROWSER_TIMEOUT;
  






  @Expose
  @Parameter(names={"-debug"}, description="<Boolean> : enables LogLevel.FINE.", arity=1)
  public Boolean debug = DEFAULT_DEBUG_TOGGLE;
  







  @Expose
  @Parameter(names={"-jettyThreads", "-jettyMaxThreads"}, description="<Integer> : max number of threads for Jetty. An unspecified, zero, or negative value means the Jetty default value (200) will be used.")
  public Integer jettyMaxThreads;
  






  @Expose
  @Parameter(names={"-log"}, description="<String> filename : the filename to use for logging. If omitted, will log to STDOUT")
  public String log;
  






  @Expose
  @Parameter(names={"-port"}, description="<Integer> : the port number the server will use.")
  public Integer port = DEFAULT_PORT;
  






  @Expose
  @Parameter(names={"-role"}, description="<String> options are [hub], [node], or [standalone].")
  public String role = "standalone";
  






  @Expose
  @Parameter(names={"-timeout", "-sessionTimeout"}, description="<Integer> in seconds : Specifies the timeout before the server automatically kills a session that hasn't had any activity in the last X seconds. The test slot will then be released for another test to use. This is typically used to take care of client crashes. For grid hub/node roles, cleanUpCycle must also be set.")
  public Integer timeout = DEFAULT_TIMEOUT;
  






  public StandaloneConfiguration() {}
  






  public static StandaloneConfiguration loadFromJSON(String filePath)
  {
    return loadFromJSON(JSONConfigurationUtils.loadJSON(filePath));
  }
  

  public static StandaloneConfiguration loadFromJSON(JsonObject json)
  {
    try
    {
      GsonBuilder builder = new GsonBuilder();
      
      return (StandaloneConfiguration)builder.excludeFieldsWithoutExposeAnnotation().create().fromJson(json, StandaloneConfiguration.class);
    }
    catch (Throwable e) {
      throw new GridConfigurationException("Error with the JSON of the config : " + e.getMessage(), e);
    }
  }
  




  public void merge(StandaloneConfiguration other)
  {
    if (other == null) {
      return;
    }
    
    if (isMergeAble(browserTimeout, browserTimeout)) {
      browserTimeout = browserTimeout;
    }
    if (isMergeAble(jettyMaxThreads, jettyMaxThreads)) {
      jettyMaxThreads = jettyMaxThreads;
    }
    if (isMergeAble(timeout, timeout)) {
      timeout = timeout;
    }
  }
  









  protected boolean isMergeAble(Object other, Object target)
  {
    if (other == null) {
      return false;
    }
    
    if (target == null) {
      return true;
    }
    





    if (!target.getClass().getSuperclass().getTypeName().equals(other.getClass().getSuperclass().getTypeName())) {
      return false;
    }
    
    if ((target instanceof Collection)) {
      return !((Collection)other).isEmpty();
    }
    
    if ((target instanceof Map)) {
      return !((Map)other).isEmpty();
    }
    
    return true;
  }
  
  public String toString(String format) {
    StringBuilder sb = new StringBuilder();
    sb.append(toString(format, "browserTimeout", browserTimeout));
    sb.append(toString(format, "debug", debug));
    sb.append(toString(format, "help", Boolean.valueOf(help)));
    sb.append(toString(format, "jettyMaxThreads", jettyMaxThreads));
    sb.append(toString(format, "log", log));
    sb.append(toString(format, "port", port));
    sb.append(toString(format, "role", role));
    sb.append(toString(format, "timeout", timeout));
    return sb.toString();
  }
  
  public String toString()
  {
    return toString(" -%1$s %2$s");
  }
  
  public StringBuilder toString(String format, String name, Object value) {
    StringBuilder sb = new StringBuilder();
    List<?> iterator;
    List<?> iterator; if ((value instanceof List)) {
      iterator = (List)value;
    } else {
      iterator = Arrays.asList(new Object[] { value });
    }
    for (Object v : iterator) {
      if ((v != null) && ((!(v instanceof Map)) || 
        (!((Map)v).isEmpty())) && ((!(v instanceof Collection)) || 
        (!((Collection)v).isEmpty()))) {
        sb.append(String.format(format, new Object[] { name, v }));
      }
    }
    return sb;
  }
  



  public JsonElement toJson()
  {
    GsonBuilder builder = new GsonBuilder();
    addJsonTypeAdapter(builder);
    
    return builder.excludeFieldsWithoutExposeAnnotation().create().toJsonTree(this);
  }
  
  protected void addJsonTypeAdapter(GsonBuilder builder) {}
}
