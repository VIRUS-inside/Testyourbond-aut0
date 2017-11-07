package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;















public class CompactData
{
  private String prefix;
  private List<String> arguments = new ArrayList();
  private Map<String, String> properties = new HashMap();
  
  public CompactData(String prefix) {
    this.prefix = prefix;
  }
  
  public String getPrefix() {
    return prefix;
  }
  
  public Map<String, String> getProperties() {
    return properties;
  }
  
  public List<String> getArguments() {
    return arguments;
  }
  
  public String toString()
  {
    return "CompactData: " + prefix + " " + properties;
  }
}
