package org.openqa.selenium.logging;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;






















public class SessionLogHandler
{
  public SessionLogHandler() {}
  
  public static Map<String, SessionLogs> getSessionLogs(JsonObject rawSessionMap)
  {
    Map<String, SessionLogs> sessionLogsMap = new HashMap();
    for (Map.Entry<String, JsonElement> entry : rawSessionMap.entrySet()) {
      String sessionId = (String)entry.getKey();
      SessionLogs sessionLogs = SessionLogs.fromJSON(((JsonElement)entry.getValue()).getAsJsonObject());
      sessionLogsMap.put(sessionId, sessionLogs);
    }
    return sessionLogsMap;
  }
}
