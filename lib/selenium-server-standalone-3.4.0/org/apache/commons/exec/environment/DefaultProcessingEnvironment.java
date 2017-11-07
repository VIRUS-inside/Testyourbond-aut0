package org.apache.commons.exec.environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.OS;



































public class DefaultProcessingEnvironment
{
  protected Map<String, String> procEnvironment;
  
  public DefaultProcessingEnvironment() {}
  
  public synchronized Map<String, String> getProcEnvironment()
    throws IOException
  {
    if (procEnvironment == null) {
      procEnvironment = createProcEnvironment();
    }
    



    Map<String, String> copy = createEnvironmentMap();
    copy.putAll(procEnvironment);
    return copy;
  }
  




  protected Map<String, String> createProcEnvironment()
    throws IOException
  {
    if (procEnvironment == null) {
      Map<String, String> env = System.getenv();
      procEnvironment = createEnvironmentMap();
      procEnvironment.putAll(env);
    }
    





























    return procEnvironment;
  }
  











  @Deprecated
  protected BufferedReader runProcEnvCommand()
    throws IOException
  {
    return null;
  }
  











































  @Deprecated
  protected CommandLine getProcEnvCommand()
  {
    CommandLine commandLine = null;
    



    return commandLine;
  }
  

































  private Map<String, String> createEnvironmentMap()
  {
    if (OS.isFamilyWindows()) {
      new TreeMap(new Comparator() {
        public int compare(String key0, String key1) {
          return key0.compareToIgnoreCase(key1);
        }
      });
    }
    return new HashMap();
  }
}
