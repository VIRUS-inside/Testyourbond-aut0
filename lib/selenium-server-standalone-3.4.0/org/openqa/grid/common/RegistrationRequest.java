package org.openqa.grid.common;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.net.InetAddress;
import java.util.List;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration.CollectionOfDesiredCapabilitiesDeSerializer;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration.CollectionOfDesiredCapabilitiesSerializer;
import org.openqa.selenium.Platform;
import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.remote.DesiredCapabilities;





















public class RegistrationRequest
{
  public static final String MAX_INSTANCES = "maxInstances";
  public static final String SELENIUM_PROTOCOL = "seleniumProtocol";
  public static final String PATH = "path";
  @SerializedName("class")
  @Expose(deserialize=false)
  private final String clazz = RegistrationRequest.class
  
    .getCanonicalName();
  
  @Expose
  private String name;
  
  @Expose
  private String description;
  
  @Expose
  private GridNodeConfiguration configuration;
  
  public RegistrationRequest()
  {
    this(new GridNodeConfiguration());
  }
  






  public RegistrationRequest(GridNodeConfiguration configuration)
  {
    this(configuration, null, null);
  }
  







  public RegistrationRequest(GridNodeConfiguration configuration, String name)
  {
    this(configuration, name, null);
  }
  









  public RegistrationRequest(GridNodeConfiguration configuration, String name, String description)
  {
    this.configuration = (configuration == null ? new GridNodeConfiguration() : configuration);
    this.name = name;
    this.description = description;
    

    fixUpHost();
    
    fixUpCapabilities();
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public GridNodeConfiguration getConfiguration() {
    return configuration;
  }
  
  public JsonObject toJson() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(new TypeToken() {}.getType(), new GridNodeConfiguration.CollectionOfDesiredCapabilitiesSerializer());
    


    return builder.serializeNulls().excludeFieldsWithoutExposeAnnotation().create()
      .toJsonTree(this, RegistrationRequest.class).getAsJsonObject();
  }
  




  public static RegistrationRequest fromJson(JsonObject json)
    throws JsonSyntaxException
  {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(new TypeToken() {}.getType(), new GridNodeConfiguration.CollectionOfDesiredCapabilitiesDeSerializer());
    


    RegistrationRequest request = (RegistrationRequest)builder.excludeFieldsWithoutExposeAnnotation().create().fromJson(json, RegistrationRequest.class);
    
    return request;
  }
  




  public static RegistrationRequest fromJson(String json)
    throws JsonSyntaxException
  {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(new TypeToken() {}.getType(), new GridNodeConfiguration.CollectionOfDesiredCapabilitiesDeSerializer());
    


    RegistrationRequest request = (RegistrationRequest)builder.excludeFieldsWithoutExposeAnnotation().create().fromJson(json, RegistrationRequest.class);
    
    return request;
  }
  



  public static RegistrationRequest build()
  {
    return build(new GridNodeConfiguration(), null, null);
  }
  









  public static RegistrationRequest build(GridNodeConfiguration configuration)
  {
    return build(configuration, null, null);
  }
  










  public static RegistrationRequest build(GridNodeConfiguration configuration, String name)
  {
    return build(configuration, name, null);
  }
  











  public static RegistrationRequest build(GridNodeConfiguration configuration, String name, String description)
  {
    RegistrationRequest pendingRequest = new RegistrationRequest(configuration, name, description);
    GridNodeConfiguration pendingConfiguration = configuration;
    
    if (nodeConfigFile != null) {
      configuration = GridNodeConfiguration.loadFromJSON(nodeConfigFile);
    }
    
    configuration.merge(pendingConfiguration);
    
    if (host != null) {
      configuration.host = host;
    }
    if (port != null) {
      configuration.port = port;
    }
    

    pendingRequest.fixUpHost();
    
    pendingRequest.fixUpCapabilities();
    
    return pendingRequest;
  }
  
  private void fixUpCapabilities() {
    if (configuration.capabilities == null) {
      return;
    }
    
    Platform current = Platform.getCurrent();
    for (DesiredCapabilities cap : configuration.capabilities) {
      if (cap.getPlatform() == null) {
        cap.setPlatform(current);
      }
      if (cap.getCapability("seleniumProtocol") == null) {
        cap.setCapability("seleniumProtocol", SeleniumProtocol.WebDriver.toString());
      }
    }
  }
  
  private void fixUpHost() {
    if ((configuration.host == null) || ("ip".equalsIgnoreCase(configuration.host))) {
      NetworkUtils util = new NetworkUtils();
      configuration.host = util.getIp4NonLoopbackAddressOfThisMachine().getHostAddress();
    } else if ("host".equalsIgnoreCase(configuration.host)) {
      NetworkUtils util = new NetworkUtils();
      configuration.host = util.getIp4NonLoopbackAddressOfThisMachine().getHostName();
    }
  }
  



  public void validate()
    throws GridConfigurationException
  {
    try
    {
      configuration.getHubHost();
      configuration.getHubPort();
    } catch (RuntimeException e) {
      throw new GridConfigurationException(e.getMessage());
    }
  }
}
