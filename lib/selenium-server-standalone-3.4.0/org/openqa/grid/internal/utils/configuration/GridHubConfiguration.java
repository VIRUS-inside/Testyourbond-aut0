package org.openqa.grid.internal.utils.configuration;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.openqa.grid.common.JSONConfigurationUtils;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.internal.listeners.Prioritizer;
import org.openqa.grid.internal.utils.CapabilityMatcher;
import org.openqa.grid.internal.utils.DefaultCapabilityMatcher;
import org.openqa.grid.internal.utils.configuration.converters.StringToClassConverter.CapabilityMatcherStringConverter;
import org.openqa.grid.internal.utils.configuration.converters.StringToClassConverter.PrioritizerStringConverter;
import org.openqa.grid.internal.utils.configuration.validators.FileExistsValueValidator;






























public class GridHubConfiguration
  extends GridConfiguration
{
  public static final String DEFAULT_HUB_CONFIG_FILE = "defaults/DefaultHub.json";
  static final String DEFAULT_ROLE = "hub";
  static final Integer DEFAULT_PORT = Integer.valueOf(4444);
  



  static final Integer DEFAULT_CLEANUP_CYCLE = Integer.valueOf(5000);
  



  static final Integer DEFAULT_NEW_SESSION_WAIT_TIMEOUT = Integer.valueOf(-1);
  



  static final Boolean DEFAULT_THROW_ON_CAPABILITY_NOT_PRESENT_TOGGLE = Boolean.valueOf(true);
  



  @Parameter(names={"-hubConfig"}, description="<String> filename: a JSON file (following grid2 format), which defines the hub properties", validateValueWith=FileExistsValueValidator.class)
  public String hubConfig;
  




  protected static class PrioritizerAdapter
    extends GridHubConfiguration.SimpleClassNameAdapter<Prioritizer>
  {
    protected PrioritizerAdapter() {}
  }
  



  @Expose
  @Parameter(names={"-matcher", "-capabilityMatcher"}, description="<String> class name : a class implementing the CapabilityMatcher interface. Specifies the logic the hub will follow to define whether a request can be assigned to a node. For example, if you want to have the matching process use regular expressions instead of exact match when specifying browser version. ALL nodes of a grid ecosystem would then use the same capabilityMatcher, as defined here.", converter=StringToClassConverter.CapabilityMatcherStringConverter.class)
  public CapabilityMatcher capabilityMatcher = new DefaultCapabilityMatcher();
  







  @Expose
  @Parameter(names={"-newSessionWaitTimeout"}, description="<Integer> in ms : The time after which a new test waiting for a node to become available will time out. When that happens, the test will throw an exception before attempting to start a browser. An unspecified, zero, or negative value means wait indefinitely.")
  public Integer newSessionWaitTimeout = DEFAULT_NEW_SESSION_WAIT_TIMEOUT;
  







  @Expose
  @Parameter(names={"-prioritizer"}, description="<String> class name : a class implementing the Prioritizer interface. Specify a custom Prioritizer if you want to sort the order in which new session requests are processed when there is a queue. Default to null ( no priority = FIFO )", converter=StringToClassConverter.PrioritizerStringConverter.class)
  public Prioritizer prioritizer;
  






  @Expose
  @Parameter(names={"-throwOnCapabilityNotPresent"}, description="<Boolean> true or false : If true, the hub will reject all test requests if no compatible proxy is currently registered. If set to false, the request will queue until a node supporting the capability is registered with the grid.", arity=1)
  public Boolean throwOnCapabilityNotPresent = DEFAULT_THROW_ON_CAPABILITY_NOT_PRESENT_TOGGLE;
  









  public GridHubConfiguration()
  {
    role = "hub";
    port = DEFAULT_PORT;
    cleanUpCycle = DEFAULT_CLEANUP_CYCLE;
  }
  


  public static GridHubConfiguration loadFromJSON(String filePath)
  {
    return loadFromJSON(JSONConfigurationUtils.loadJSON(filePath));
  }
  

  public static GridHubConfiguration loadFromJSON(JsonObject json)
  {
    try
    {
      GsonBuilder builder = new GsonBuilder();
      staticAddJsonTypeAdapter(builder);
      return 
        (GridHubConfiguration)builder.excludeFieldsWithoutExposeAnnotation().create().fromJson(json, GridHubConfiguration.class);
    } catch (Throwable e) {
      throw new GridConfigurationException("Error with the JSON of the config : " + e.getMessage(), e);
    }
  }
  




  public void merge(GridNodeConfiguration other)
  {
    super.merge(other);
  }
  



  public void merge(GridHubConfiguration other)
  {
    if (other == null) {
      return;
    }
    super.merge(other);
    
    if (isMergeAble(capabilityMatcher, capabilityMatcher)) {
      capabilityMatcher = capabilityMatcher;
    }
    if (isMergeAble(newSessionWaitTimeout, newSessionWaitTimeout)) {
      newSessionWaitTimeout = newSessionWaitTimeout;
    }
    if (isMergeAble(prioritizer, prioritizer)) {
      prioritizer = prioritizer;
    }
    if (isMergeAble(throwOnCapabilityNotPresent, throwOnCapabilityNotPresent)) {
      throwOnCapabilityNotPresent = throwOnCapabilityNotPresent;
    }
  }
  
  public String toString(String format)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString(format));
    sb.append(toString(format, "hubConfig", hubConfig));
    sb.append(toString(format, "capabilityMatcher", capabilityMatcher.getClass().getCanonicalName()));
    sb.append(toString(format, "newSessionWaitTimeout", newSessionWaitTimeout));
    sb.append(toString(format, "prioritizer", prioritizer != null ? prioritizer.getClass().getCanonicalName() : null));
    sb.append(toString(format, "throwOnCapabilityNotPresent", throwOnCapabilityNotPresent));
    return sb.toString();
  }
  
  protected void addJsonTypeAdapter(GsonBuilder builder)
  {
    super.addJsonTypeAdapter(builder);
    staticAddJsonTypeAdapter(builder);
  }
  
  protected static void staticAddJsonTypeAdapter(GsonBuilder builder) { builder.registerTypeAdapter(CapabilityMatcher.class, new CapabilityMatcherAdapter().nullSafe());
    builder.registerTypeAdapter(Prioritizer.class, new PrioritizerAdapter().nullSafe()); }
  
  protected static class CapabilityMatcherAdapter extends GridHubConfiguration.SimpleClassNameAdapter<CapabilityMatcher> { protected CapabilityMatcherAdapter() {} }
  
  protected static class SimpleClassNameAdapter<T> extends TypeAdapter<T> { protected SimpleClassNameAdapter() {}
    
    public void write(JsonWriter out, T value) throws IOException { out.value(value.getClass().getCanonicalName()); }
    
    public T read(JsonReader in) throws IOException
    {
      String value = in.nextString();
      try {
        return Class.forName(value).newInstance();
      } catch (Exception e) {
        throw new RuntimeException(String.format("String %s could not be coerced to class: %s", new Object[] { value, Class.class.getName() }), e);
      }
    }
  }
}
