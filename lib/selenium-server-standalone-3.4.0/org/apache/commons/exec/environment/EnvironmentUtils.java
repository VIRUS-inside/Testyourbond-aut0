package org.apache.commons.exec.environment;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

































public class EnvironmentUtils
{
  private static final DefaultProcessingEnvironment PROCESSING_ENVIRONMENT_IMPLEMENTATION = new DefaultProcessingEnvironment();
  







  private EnvironmentUtils() {}
  






  public static String[] toStrings(Map<String, String> environment)
  {
    if (environment == null) {
      return null;
    }
    String[] result = new String[environment.size()];
    int i = 0;
    for (Map.Entry<String, String> entry : environment.entrySet()) {
      String key = entry.getKey() == null ? "" : ((String)entry.getKey()).toString();
      String value = entry.getValue() == null ? "" : ((String)entry.getValue()).toString();
      result[i] = (key + "=" + value);
      i++;
    }
    return result;
  }
  







  public static Map<String, String> getProcEnvironment()
    throws IOException
  {
    return PROCESSING_ENVIRONMENT_IMPLEMENTATION.getProcEnvironment();
  }
  






  public static void addVariableToEnvironment(Map<String, String> environment, String keyAndValue)
  {
    String[] parsedVariable = parseEnvironmentVariable(keyAndValue);
    environment.put(parsedVariable[0], parsedVariable[1]);
  }
  






  private static String[] parseEnvironmentVariable(String keyAndValue)
  {
    int index = keyAndValue.indexOf('=');
    if (index == -1) {
      throw new IllegalArgumentException("Environment variable for this platform must contain an equals sign ('=')");
    }
    


    String[] result = new String[2];
    result[0] = keyAndValue.substring(0, index);
    result[1] = keyAndValue.substring(index + 1);
    
    return result;
  }
}
