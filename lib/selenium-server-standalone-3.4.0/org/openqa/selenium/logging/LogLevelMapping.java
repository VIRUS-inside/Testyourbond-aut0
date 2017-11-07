package org.openqa.selenium.logging;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.logging.Level;






















public class LogLevelMapping
{
  private static final String DEBUG = "DEBUG";
  private static ImmutableMap<Integer, Level> levelMap;
  
  static
  {
    Level[] supportedLevels = { Level.ALL, Level.FINE, Level.INFO, Level.WARNING, Level.SEVERE, Level.OFF };
    






    ImmutableMap.Builder<Integer, Level> builder = ImmutableMap.builder();
    for (Level level : supportedLevels) {
      builder.put(Integer.valueOf(level.intValue()), level);
    }
    levelMap = builder.build();
  }
  





  public static Level normalize(Level level)
  {
    if (levelMap.containsKey(Integer.valueOf(level.intValue())))
      return (Level)levelMap.get(Integer.valueOf(level.intValue()));
    if (level.intValue() >= Level.SEVERE.intValue())
      return Level.SEVERE;
    if (level.intValue() >= Level.WARNING.intValue())
      return Level.WARNING;
    if (level.intValue() >= Level.INFO.intValue()) {
      return Level.INFO;
    }
    return Level.FINE;
  }
  






  public static String getName(Level level)
  {
    Level normalized = normalize(level);
    return normalized == Level.FINE ? "DEBUG" : normalized.getName();
  }
  
  public static Level toLevel(String logLevelName) {
    if (Strings.isNullOrEmpty(logLevelName))
    {
      return Level.INFO;
    }
    
    if (logLevelName.equals("DEBUG")) {
      return Level.FINE;
    }
    return (Level)levelMap.get(Integer.valueOf(Level.parse(logLevelName).intValue()));
  }
  
  public LogLevelMapping() {}
}
