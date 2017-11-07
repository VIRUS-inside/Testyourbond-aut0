package org.openqa.selenium.logging;

import com.google.common.collect.Sets;
import java.util.Set;


























class CompositeLocalLogs
  extends LocalLogs
{
  private LocalLogs predefinedTypeLogger;
  private LocalLogs allTypesLogger;
  
  protected CompositeLocalLogs(LocalLogs predefinedTypeLogger, LocalLogs allTypesLogger)
  {
    this.predefinedTypeLogger = predefinedTypeLogger;
    this.allTypesLogger = allTypesLogger;
  }
  
  public LogEntries get(String logType)
  {
    if (predefinedTypeLogger.getAvailableLogTypes().contains(logType)) {
      return predefinedTypeLogger.get(logType);
    }
    
    return allTypesLogger.get(logType);
  }
  
  public Set<String> getAvailableLogTypes() {
    return Sets.union(predefinedTypeLogger.getAvailableLogTypes(), allTypesLogger.getAvailableLogTypes());
  }
  
  public void addEntry(String logType, LogEntry entry)
  {
    if (predefinedTypeLogger.getAvailableLogTypes().contains(logType)) {
      predefinedTypeLogger.addEntry(logType, entry);
    } else {
      allTypesLogger.addEntry(logType, entry);
    }
  }
}
