package org.openqa.selenium.remote.server.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;






















public class LoggingManager
{
  private static PerSessionLogHandler perSessionLogHandler = new PerSessionLogHandler(4000, new TerseFormatter(), false);
  
  public LoggingManager() {}
  
  public static synchronized void configureLogging(boolean debugMode)
  {
    Logger currentLogger = Logger.getLogger("");
    overrideSimpleFormatterWithTerseOneForConsoleHandler(currentLogger, debugMode);
    if (debugMode) {
      currentLogger.setLevel(Level.FINE);
    }
  }
  


  public static synchronized PerSessionLogHandler perSessionLogHandler()
  {
    return perSessionLogHandler;
  }
  

  public static void overrideSimpleFormatterWithTerseOneForConsoleHandler(Logger logger, boolean debugMode)
  {
    for (Handler handler : logger.getHandlers()) {
      if ((handler instanceof ConsoleHandler))
      {

        Formatter formatter = handler.getFormatter();
        if ((formatter instanceof SimpleFormatter))
        {





          Level originalLevel = handler.getLevel();
          handler.setFormatter(new TerseFormatter());
          handler.setLevel(Level.WARNING);
          



          StdOutHandler stdOutHandler = new StdOutHandler();
          stdOutHandler.setFormatter(new TerseFormatter());
          stdOutHandler.setFilter(new MaxLevelFilter(Level.INFO));
          stdOutHandler.setLevel(originalLevel);
          logger.addHandler(stdOutHandler);
          if ((debugMode) && 
            (originalLevel.intValue() > Level.FINE.intValue())) {
            stdOutHandler.setLevel(Level.FINE);
          }
        }
      }
    }
  }
}
