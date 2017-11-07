package org.openqa.selenium.opera;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.remote.DesiredCapabilities;
















































public class OperaOptions
{
  public static final String CAPABILITY = "operaOptions";
  private String binary;
  private List<String> args = Lists.newArrayList();
  private List<File> extensionFiles = Lists.newArrayList();
  private List<String> extensions = Lists.newArrayList();
  private Map<String, Object> experimentalOptions = Maps.newHashMap();
  


  public OperaOptions() {}
  


  public void setBinary(File path)
  {
    binary = ((File)Preconditions.checkNotNull(path)).getPath();
  }
  






  public void setBinary(String path)
  {
    binary = ((String)Preconditions.checkNotNull(path));
  }
  



  public void addArguments(String... arguments)
  {
    addArguments(ImmutableList.copyOf(arguments));
  }
  














  public void addArguments(List<String> arguments)
  {
    args.addAll(arguments);
  }
  



  public void addExtensions(File... paths)
  {
    addExtensions(ImmutableList.copyOf(paths));
  }
  





  public void addExtensions(List<File> paths)
  {
    for (File path : paths) {
      Preconditions.checkNotNull(path);
      Preconditions.checkArgument(path.exists(), "%s does not exist", path.getAbsolutePath());
      Preconditions.checkArgument(!path.isDirectory(), "%s is a directory", path
        .getAbsolutePath());
    }
    extensionFiles.addAll(paths);
  }
  



  public void addEncodedExtensions(String... encoded)
  {
    addEncodedExtensions(ImmutableList.copyOf(encoded));
  }
  





  public void addEncodedExtensions(List<String> encoded)
  {
    for (String extension : encoded) {
      Preconditions.checkNotNull(extension);
    }
    extensions.addAll(encoded);
  }
  







  public void setExperimentalOption(String name, Object value)
  {
    experimentalOptions.put(Preconditions.checkNotNull(name), value);
  }
  





  public Object getExperimentalOption(String name)
  {
    return experimentalOptions.get(Preconditions.checkNotNull(name));
  }
  





  public JsonElement toJson()
    throws IOException
  {
    Map<String, Object> options = Maps.newHashMap();
    
    for (Iterator localIterator = experimentalOptions.keySet().iterator(); localIterator.hasNext();) { key = (String)localIterator.next();
      options.put(key, experimentalOptions.get(key));
    }
    String key;
    if (binary != null) {
      options.put("binary", binary);
    }
    
    options.put("args", ImmutableList.copyOf(args));
    
    Object encoded_extensions = Lists.newArrayListWithExpectedSize(extensionFiles
      .size() + extensions.size());
    for (File path : extensionFiles) {
      String encoded = Base64.getEncoder().encodeToString(Files.toByteArray(path));
      ((List)encoded_extensions).add(encoded);
    }
    ((List)encoded_extensions).addAll(extensions);
    options.put("extensions", encoded_extensions);
    
    return new Gson().toJsonTree(options);
  }
  






  DesiredCapabilities toCapabilities()
  {
    DesiredCapabilities capabilities = DesiredCapabilities.operaBlink();
    capabilities.setCapability("operaOptions", this);
    return capabilities;
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof OperaOptions)) {
      return false;
    }
    OperaOptions that = (OperaOptions)other;
    return (Objects.equal(binary, binary)) && 
      (Objects.equal(args, args)) && 
      (Objects.equal(extensionFiles, extensionFiles)) && 
      (Objects.equal(experimentalOptions, experimentalOptions)) && 
      (Objects.equal(extensions, extensions));
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { binary, args, extensionFiles, experimentalOptions, extensions });
  }
}
