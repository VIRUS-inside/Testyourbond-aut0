package org.openqa.grid.internal.utils.configuration;

import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.openqa.grid.common.JSONConfigurationUtils;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.internal.utils.configuration.converters.BrowserDesiredCapabilityConverter;
import org.openqa.grid.internal.utils.configuration.converters.NoOpParameterSplitter;
import org.openqa.grid.internal.utils.configuration.validators.FileExistsValueValidator;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.JsonToBeanConverter;































public class GridNodeConfiguration
  extends GridConfiguration
{
  public static final String DEFAULT_NODE_CONFIG_FILE = "defaults/DefaultNodeWebDriver.json";
  static final String DEFAULT_ROLE = "node";
  static final Integer DEFAULT_PORT = Integer.valueOf(5555);
  



  static final Integer DEFAULT_POLLING_INTERVAL = Integer.valueOf(5000);
  



  static final Integer DEFAULT_MAX_SESSION = Integer.valueOf(5);
  



  static final Integer DEFAULT_REGISTER_CYCLE = Integer.valueOf(5000);
  



  static final Boolean DEFAULT_REGISTER_TOGGLE = Boolean.valueOf(true);
  



  static final String DEFAULT_HUB = "http://localhost:4444";
  



  static final Integer DEFAULT_NODE_STATUS_CHECK_TIMEOUT = Integer.valueOf(5000);
  



  static final Integer DEFAULT_UNREGISTER_DELAY = Integer.valueOf(60000);
  



  static final Integer DEFAULT_DOWN_POLLING_LIMIT = Integer.valueOf(2);
  static final String DEFAULT_PROXY = "org.openqa.grid.selenium.proxy.DefaultRemoteProxy";
  @Parameter(names={"-nodeConfig"}, description="<String> filename : JSON configuration file for the node. Overrides default values", validateValueWith=FileExistsValueValidator.class)
  public String nodeConfigFile;
  @Expose(serialize=false)
  String remoteHost;
  
  static final class DefaultDesiredCapabilitiesBuilder
  {
    DefaultDesiredCapabilitiesBuilder() {}
    
    static final List<DesiredCapabilities> getCapabilities() {
      DesiredCapabilities chrome = new DesiredCapabilities();
      chrome.setBrowserName("chrome");
      chrome.setCapability("maxInstances", Integer.valueOf(5));
      chrome.setCapability("seleniumProtocol", "WebDriver");
      
      DesiredCapabilities firefox = new DesiredCapabilities();
      firefox.setBrowserName("firefox");
      firefox.setCapability("maxInstances", Integer.valueOf(5));
      firefox.setCapability("seleniumProtocol", "WebDriver");
      
      DesiredCapabilities ie = new DesiredCapabilities();
      ie.setBrowserName("internet explorer");
      ie.setCapability("maxInstances", Integer.valueOf(1));
      ie.setCapability("seleniumProtocol", "WebDriver");
      
      return Lists.newArrayList(new DesiredCapabilities[] { chrome, firefox, ie });
    }
  }
  












  @Expose(serialize=false)
  @Deprecated
  private Object configuration;
  











  @Expose
  @Parameter(names={"-hubHost"}, description="<String> IP or hostname : the host address of the hub we're attempting to register with. If -hub is specified the -hubHost is determined from it.")
  String hubHost;
  











  @Expose
  @Parameter(names={"-hubPort"}, description="<Integer> : the port of the hub we're attempting to register with. If -hub is specified the -hubPort is determined from it.")
  Integer hubPort;
  











  @Expose
  @Parameter(names={"-id"}, description="<String> : optional unique identifier for the node. Defaults to the url of the remoteHost, when not specified.")
  public String id;
  











  @Expose
  @Parameter(names={"-capabilities", "-browser"}, description="<String> : comma separated Capability values. Example: -capabilities browserName=firefox,platform=linux -capabilities browserName=chrome,platform=linux", listConverter=BrowserDesiredCapabilityConverter.class, converter=BrowserDesiredCapabilityConverter.class, splitter=NoOpParameterSplitter.class)
  public List<DesiredCapabilities> capabilities = DefaultDesiredCapabilitiesBuilder.getCapabilities();
  

  @Expose
  @Parameter(names={"-downPollingLimit"}, description="<Integer> : node is marked as \"down\" if the node hasn't responded after the number of checks specified in [downPollingLimit].")
  public Integer downPollingLimit = DEFAULT_DOWN_POLLING_LIMIT;
  






  @Expose
  @Parameter(names={"-hub"}, description="<String> : the url that will be used to post the registration request. This option takes precedence over -hubHost and -hubPort options.")
  public String hub = "http://localhost:4444";
  






  @Expose
  @Parameter(names={"-nodePolling"}, description="<Integer> in ms : specifies how often the hub will poll to see if the node is still responding.")
  public Integer nodePolling = DEFAULT_POLLING_INTERVAL;
  






  @Expose
  @Parameter(names={"-nodeStatusCheckTimeout"}, description="<Integer> in ms : connection/socket timeout, used for node \"nodePolling\" check.")
  public Integer nodeStatusCheckTimeout = DEFAULT_NODE_STATUS_CHECK_TIMEOUT;
  






  @Expose
  @Parameter(names={"-proxy"}, description="<String> : the class used to represent the node proxy. Default is [org.openqa.grid.selenium.proxy.DefaultRemoteProxy].")
  public String proxy = "org.openqa.grid.selenium.proxy.DefaultRemoteProxy";
  






  @Expose
  @Parameter(names={"-register"}, description="if specified, node will attempt to re-register itself automatically with its known grid hub if the hub becomes unavailable.", arity=1)
  public Boolean register = DEFAULT_REGISTER_TOGGLE;
  







  @Expose
  @Parameter(names={"-registerCycle"}, description="<Integer> in ms : specifies how often the node will try to register itself again. Allows administrator to restart the hub without restarting (or risk orphaning) registered nodes. Must be specified with the \"-register\" option.")
  public Integer registerCycle = DEFAULT_REGISTER_CYCLE;
  






  @Expose
  @Parameter(names={"-unregisterIfStillDownAfter"}, description="<Integer> in ms : if the node remains down for more than [unregisterIfStillDownAfter] ms, it will stop attempting to re-register from the hub.")
  public Integer unregisterIfStillDownAfter = DEFAULT_UNREGISTER_DELAY;
  








  public GridNodeConfiguration()
  {
    role = "node";
    port = DEFAULT_PORT;
    maxSession = DEFAULT_MAX_SESSION;
  }
  
  public String getHubHost() {
    if (hubHost == null) {
      if (hub == null) {
        throw new RuntimeException("You must specify either a hubHost or hub parameter.");
      }
      parseHubUrl();
    }
    return hubHost;
  }
  
  public Integer getHubPort() {
    if (hubPort == null) {
      if (hub == null) {
        throw new RuntimeException("You must specify either a hubPort or hub parameter.");
      }
      parseHubUrl();
    }
    return hubPort;
  }
  
  public String getRemoteHost() {
    if (remoteHost == null) {
      if (host == null) {
        host = "localhost";
      }
      if (port == null) {
        port = Integer.valueOf(5555);
      }
      remoteHost = ("http://" + host + ":" + port);
    }
    return remoteHost;
  }
  
  private void parseHubUrl() {
    try {
      URL u = new URL(hub);
      hubHost = u.getHost();
      hubPort = Integer.valueOf(u.getPort());
    } catch (MalformedURLException mURLe) {
      throw new RuntimeException("-hub must be a valid url: " + hub, mURLe);
    }
  }
  
  public void merge(GridNodeConfiguration other) {
    if (other == null) {
      return;
    }
    super.merge(other);
    
    if (isMergeAble(capabilities, capabilities)) {
      capabilities = capabilities;
    }
    if (isMergeAble(downPollingLimit, downPollingLimit)) {
      downPollingLimit = downPollingLimit;
    }
    if (isMergeAble(hub, hub)) {
      hub = hub;
    }
    if (isMergeAble(hubHost, hubHost)) {
      hubHost = hubHost;
    }
    if (isMergeAble(hubPort, hubPort)) {
      hubPort = hubPort;
    }
    if (isMergeAble(id, id)) {
      id = id;
    }
    if (isMergeAble(nodePolling, nodePolling)) {
      nodePolling = nodePolling;
    }
    if (isMergeAble(nodeStatusCheckTimeout, nodeStatusCheckTimeout)) {
      nodeStatusCheckTimeout = nodeStatusCheckTimeout;
    }
    if (isMergeAble(proxy, proxy)) {
      proxy = proxy;
    }
    if (isMergeAble(register, register)) {
      register = register;
    }
    if (isMergeAble(registerCycle, registerCycle)) {
      registerCycle = registerCycle;
    }
    if (isMergeAble(remoteHost, remoteHost)) {
      remoteHost = remoteHost;
    }
    if (isMergeAble(unregisterIfStillDownAfter, unregisterIfStillDownAfter)) {
      unregisterIfStillDownAfter = unregisterIfStillDownAfter;
    }
  }
  


  public String toString(String format)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString(format));
    sb.append(toString(format, "capabilities", capabilities));
    sb.append(toString(format, "downPollingLimit", downPollingLimit));
    sb.append(toString(format, "hub", hub));
    sb.append(toString(format, "id", id));
    sb.append(toString(format, "hubHost", hubHost));
    sb.append(toString(format, "hubPort", hubPort));
    sb.append(toString(format, "nodeConfigFile", nodeConfigFile));
    sb.append(toString(format, "nodePolling", nodePolling));
    sb.append(toString(format, "nodeStatusCheckTimeout", nodeStatusCheckTimeout));
    sb.append(toString(format, "proxy", proxy));
    sb.append(toString(format, "register", register));
    sb.append(toString(format, "registerCycle", registerCycle));
    sb.append(toString(format, "remoteHost", remoteHost));
    sb.append(toString(format, "unregisterIfStillDownAfter", unregisterIfStillDownAfter));
    return sb.toString();
  }
  


  public static GridNodeConfiguration loadFromJSON(String filePath)
  {
    return loadFromJSON(JSONConfigurationUtils.loadJSON(filePath));
  }
  

  public static GridNodeConfiguration loadFromJSON(JsonObject json)
  {
    try
    {
      GsonBuilder builder = new GsonBuilder();
      staticAddJsonTypeAdapter(builder);
      
      GridNodeConfiguration config = (GridNodeConfiguration)builder.excludeFieldsWithoutExposeAnnotation().create().fromJson(json, GridNodeConfiguration.class);
      
      if (configuration != null)
      {
        throw new GridConfigurationException("Deprecated -nodeConfig file encountered. Please update the file to work with Selenium 3. See https://github.com/SeleniumHQ/selenium/wiki/Grid2#configuring-the-nodes-by-json for more details.");
      }
      



      return config;
    } catch (Throwable e) {
      throw new GridConfigurationException("Error with the JSON of the config : " + e.getMessage(), e);
    }
  }
  

  protected void addJsonTypeAdapter(GsonBuilder builder)
  {
    super.addJsonTypeAdapter(builder);
    staticAddJsonTypeAdapter(builder);
  }
  
  protected static void staticAddJsonTypeAdapter(GsonBuilder builder) {
    builder.registerTypeAdapter(new TypeToken() {}.getType(), new CollectionOfDesiredCapabilitiesSerializer());
    
    builder.registerTypeAdapter(new TypeToken() {}.getType(), new CollectionOfDesiredCapabilitiesDeSerializer());
  }
  

  public static class CollectionOfDesiredCapabilitiesSerializer
    implements JsonSerializer<List<DesiredCapabilities>>
  {
    public CollectionOfDesiredCapabilitiesSerializer() {}
    
    public JsonElement serialize(List<DesiredCapabilities> desiredCapabilities, Type type, JsonSerializationContext jsonSerializationContext)
    {
      JsonArray capabilities = new JsonArray();
      BeanToJsonConverter converter = new BeanToJsonConverter();
      for (DesiredCapabilities dc : desiredCapabilities) {
        capabilities.add(converter.convertObject(dc));
      }
      return capabilities;
    }
  }
  
  public static class CollectionOfDesiredCapabilitiesDeSerializer
    implements JsonDeserializer<List<DesiredCapabilities>>
  {
    public CollectionOfDesiredCapabilitiesDeSerializer() {}
    
    public List<DesiredCapabilities> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException
    {
      if (jsonElement.isJsonArray()) {
        List<DesiredCapabilities> desiredCapabilities = new ArrayList();
        JsonToBeanConverter converter = new JsonToBeanConverter();
        for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
          desiredCapabilities.add(converter.convert(DesiredCapabilities.class, arrayElement));
        }
        return desiredCapabilities;
      }
      throw new JsonParseException("capabilities should be expressed as an array of objects.");
    }
  }
}
