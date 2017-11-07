package org.openqa.selenium.logging;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Set;





















public abstract class LocalLogs
  implements Logs
{
  private static final LocalLogs NULL_LOGGER = new LocalLogs() {
    public LogEntries get(String logType) {
      return new LogEntries(ImmutableList.of());
    }
    
    public Set<String> getAvailableLogTypes() {
      return ImmutableSet.of();
    }
    



    public void addEntry(String logType, LogEntry entry) {}
  };
  


  public static LocalLogs getNullLogger()
  {
    return NULL_LOGGER;
  }
  
  public static LocalLogs getStoringLoggerInstance(Set<String> logTypesToIgnore) {
    return new StoringLocalLogs(logTypesToIgnore);
  }
  
  public static LocalLogs getHandlerBasedLoggerInstance(LoggingHandler loggingHandler, Set<String> logTypesToInclude)
  {
    return new HandlerBasedLocalLogs(loggingHandler, logTypesToInclude);
  }
  







  public static LocalLogs getCombinedLogsHolder(LocalLogs predefinedTypeLogger, LocalLogs allTypesLogger)
  {
    return new CompositeLocalLogs(predefinedTypeLogger, allTypesLogger);
  }
  
  protected LocalLogs() {}
  
  public abstract LogEntries get(String paramString);
  
  public abstract void addEntry(String paramString, LogEntry paramLogEntry);
}
