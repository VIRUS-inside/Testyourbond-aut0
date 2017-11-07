package org.openqa.selenium.logging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;






























public class LoggingPreferences
  implements Serializable
{
  private static final long serialVersionUID = 6708028456766320675L;
  private final Map<String, Level> prefs = new HashMap();
  

  public LoggingPreferences() {}
  

  public void enable(String logType, Level level)
  {
    prefs.put(logType, level);
  }
  


  public Set<String> getEnabledLogTypes()
  {
    return new HashSet(prefs.keySet());
  }
  




  public Level getLevel(String logType)
  {
    return prefs.get(logType) == null ? Level.OFF : (Level)prefs.get(logType);
  }
  






  public LoggingPreferences addPreferences(LoggingPreferences prefs)
  {
    if (prefs == null) {
      return this;
    }
    for (String logType : prefs.getEnabledLogTypes()) {
      enable(logType, prefs.getLevel(logType));
    }
    return this;
  }
}
