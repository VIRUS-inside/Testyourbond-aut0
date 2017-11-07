package org.openqa.selenium.remote.server.log;

import java.io.File;
import java.util.logging.Level;


















public class LoggingOptions
{
  public LoggingOptions() {}
  
  public static String getDefaultLogOutFile()
  {
    String logOutFileProperty = System.getProperty("selenium.LOGGER");
    if (null == logOutFileProperty) {
      return null;
    }
    return new File(logOutFileProperty).getAbsolutePath();
  }
  
  public static Level getDefaultLogLevel() {
    String logLevelProperty = System.getProperty("selenium.LOGGER.level");
    if (null == logLevelProperty) {
      return null;
    }
    return Level.parse(logLevelProperty);
  }
}
