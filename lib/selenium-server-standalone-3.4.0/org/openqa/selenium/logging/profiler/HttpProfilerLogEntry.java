package org.openqa.selenium.logging.profiler;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.util.Map;

















public class HttpProfilerLogEntry
  extends ProfilerLogEntry
{
  public HttpProfilerLogEntry(String commandName, boolean isStart)
  {
    super(EventType.HTTP_COMMAND, constructMessage(EventType.HTTP_COMMAND, commandName, isStart));
  }
  
  private static String constructMessage(EventType eventType, String commandName, boolean isStart) {
    Map<String, ?> map = ImmutableMap.of("event", eventType
      .toString(), "command", commandName, "startorend", isStart ? "start" : "end");
    

    return new Gson().toJson(map);
  }
}
