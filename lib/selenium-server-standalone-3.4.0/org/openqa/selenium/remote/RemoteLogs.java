package org.openqa.selenium.remote;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Beta;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LogCombiner;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogLevelMapping;
import org.openqa.selenium.logging.Logs;




















@Beta
public class RemoteLogs
  implements Logs
{
  private static final String LEVEL = "level";
  private static final String TIMESTAMP = "timestamp";
  private static final String MESSAGE = "message";
  private static final Logger logger = Logger.getLogger(RemoteLogs.class.getName());
  protected ExecuteMethod executeMethod;
  @VisibleForTesting
  public static final String TYPE_KEY = "type";
  private final LocalLogs localLogs;
  
  public RemoteLogs(ExecuteMethod executeMethod, LocalLogs localLogs)
  {
    this.executeMethod = executeMethod;
    this.localLogs = localLogs;
  }
  
  public LogEntries get(String logType) {
    if ("profiler".equals(logType)) {
      LogEntries remoteEntries = new LogEntries(new ArrayList());
      try {
        remoteEntries = getRemoteEntries(logType);
      }
      catch (WebDriverException e)
      {
        logger.log(Level.WARNING, "Remote profiler logs are not available and have been omitted.", e);
      }
      

      return LogCombiner.combine(new LogEntries[] { remoteEntries, getLocalEntries(logType) });
    }
    if ("client".equals(logType)) {
      return getLocalEntries(logType);
    }
    return getRemoteEntries(logType);
  }
  
  private LogEntries getRemoteEntries(String logType) {
    Object raw = executeMethod.execute("getLog", ImmutableMap.of("type", logType));
    
    List<Map<String, Object>> rawList = (List)raw;
    List<LogEntry> remoteEntries = Lists.newArrayListWithCapacity(rawList.size());
    
    for (Map<String, Object> obj : rawList) {
      remoteEntries.add(new LogEntry(LogLevelMapping.toLevel((String)obj.get("level")), 
        ((Long)obj.get("timestamp")).longValue(), 
        (String)obj.get("message")));
    }
    return new LogEntries(remoteEntries);
  }
  
  private LogEntries getLocalEntries(String logType) {
    return localLogs.get(logType);
  }
  
  private Set<String> getAvailableLocalLogs() {
    return localLogs.getAvailableLogTypes();
  }
  
  public Set<String> getAvailableLogTypes() {
    Object raw = executeMethod.execute("getAvailableLogTypes", null);
    
    List<String> rawList = (List)raw;
    ImmutableSet.Builder<String> builder = new ImmutableSet.Builder();
    for (String logType : rawList) {
      builder.add(logType);
    }
    builder.addAll(getAvailableLocalLogs());
    return builder.build();
  }
}
