package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import org.openqa.selenium.logging.SessionLogs;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;
import org.openqa.selenium.remote.server.rest.RestishHandler;















public class GetSessionLogsHandler
  implements RestishHandler<Map<String, SessionLogs>>
{
  public GetSessionLogsHandler() {}
  
  private final Response response = new Response();
  
  public Response getResponse() {
    return response;
  }
  
  public Map<String, SessionLogs> handle()
    throws Exception
  {
    ImmutableMap.Builder<String, SessionLogs> builder = ImmutableMap.builder();
    for (SessionId sessionId : LoggingManager.perSessionLogHandler().getLoggedSessions()) {
      builder.put(sessionId.toString(), 
        LoggingManager.perSessionLogHandler().getAllLogsForSession(sessionId));
    }
    return builder.build();
  }
  
  public String toString()
  {
    return String.format("[fetching session logs]", new Object[0]);
  }
}
