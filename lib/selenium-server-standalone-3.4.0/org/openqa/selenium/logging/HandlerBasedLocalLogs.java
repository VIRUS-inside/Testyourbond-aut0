package org.openqa.selenium.logging;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;




















class HandlerBasedLocalLogs
  extends LocalLogs
{
  final LoggingHandler loggingHandler;
  final Set<String> logTypesToInclude;
  
  protected HandlerBasedLocalLogs(LoggingHandler loggingHandler, Set<String> logTypesToInclude)
  {
    this.loggingHandler = loggingHandler;
    this.logTypesToInclude = logTypesToInclude;
  }
  
  public LogEntries get(String logType) {
    if (("client".equals(logType)) && (logTypesToInclude.contains(logType))) {
      List<LogEntry> entries = loggingHandler.getRecords();
      loggingHandler.flush();
      return new LogEntries(entries);
    }
    return new LogEntries(Lists.newArrayList());
  }
  
  public Set<String> getAvailableLogTypes() {
    return ImmutableSet.of("client");
  }
  
  public void addEntry(String logType, LogEntry entry) {
    throw new RuntimeException("Log to this instance of LocalLogs using standard Java logging.");
  }
}
