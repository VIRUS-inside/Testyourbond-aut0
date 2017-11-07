package org.openqa.selenium.logging;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;





















class StoringLocalLogs
  extends LocalLogs
{
  private final Map<String, List<LogEntry>> localLogs = Maps.newHashMap();
  private final Set<String> logTypesToInclude;
  
  public StoringLocalLogs(Set<String> logTypesToInclude) {
    this.logTypesToInclude = logTypesToInclude;
  }
  
  public LogEntries get(String logType) {
    return new LogEntries(getLocalLogs(logType));
  }
  
  private Iterable<LogEntry> getLocalLogs(String logType) {
    if (localLogs.containsKey(logType)) {
      List<LogEntry> entries = (List)localLogs.get(logType);
      localLogs.put(logType, Lists.newArrayList());
      return entries;
    }
    
    return Lists.newArrayList();
  }
  





  public void addEntry(String logType, LogEntry entry)
  {
    if (!logTypesToInclude.contains(logType)) {
      return;
    }
    
    if (!localLogs.containsKey(logType)) {
      localLogs.put(logType, Lists.newArrayList(new LogEntry[] { entry }));
    } else {
      ((List)localLogs.get(logType)).add(entry);
    }
  }
  
  public Set<String> getAvailableLogTypes() {
    return localLogs.keySet();
  }
}
