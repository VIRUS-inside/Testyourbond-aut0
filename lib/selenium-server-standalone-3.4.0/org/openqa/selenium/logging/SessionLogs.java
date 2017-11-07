package org.openqa.selenium.logging;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.openqa.selenium.Beta;




















@Beta
public class SessionLogs
{
  private final Map<String, LogEntries> logTypeToEntriesMap;
  
  public SessionLogs()
  {
    logTypeToEntriesMap = new HashMap();
  }
  
  public LogEntries getLogs(String logType) {
    if ((logType == null) || (!logTypeToEntriesMap.containsKey(logType))) {
      return new LogEntries(Collections.emptyList());
    }
    return (LogEntries)logTypeToEntriesMap.get(logType);
  }
  
  public void addLog(String logType, LogEntries logEntries) {
    logTypeToEntriesMap.put(logType, logEntries);
  }
  
  public Set<String> getLogTypes() {
    return logTypeToEntriesMap.keySet();
  }
  
  public Map<String, LogEntries> getAll() {
    return Collections.unmodifiableMap(logTypeToEntriesMap);
  }
  
  public static SessionLogs fromJSON(JsonObject rawSessionLogs) {
    SessionLogs sessionLogs = new SessionLogs();
    for (Map.Entry<String, JsonElement> entry : rawSessionLogs.entrySet()) {
      String logType = (String)entry.getKey();
      JsonArray rawLogEntries = ((JsonElement)entry.getValue()).getAsJsonArray();
      List<LogEntry> logEntries = new ArrayList();
      for (int index = 0; index < rawLogEntries.size(); index++) {
        JsonObject rawEntry = rawLogEntries.get(index).getAsJsonObject();
        logEntries.add(new LogEntry(LogLevelMapping.toLevel(rawEntry
          .get("level").getAsString()), rawEntry
          .get("timestamp").getAsLong(), rawEntry
          .get("message").getAsString()));
      }
      sessionLogs.addLog(logType, new LogEntries(logEntries));
    }
    return sessionLogs;
  }
}
