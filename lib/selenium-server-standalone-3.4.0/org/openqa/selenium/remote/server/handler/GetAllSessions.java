package org.openqa.selenium.remote.server.handler;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.DriverSessions;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.rest.RestishHandler;

















public class GetAllSessions
  implements RestishHandler<List<SessionInfo>>
{
  private final Response response = new Response();
  private volatile DriverSessions allSessions;
  
  public GetAllSessions(DriverSessions allSession) {
    allSessions = allSession;
  }
  
  public List<SessionInfo> handle() throws Exception
  {
    Set<SessionId> sessions = allSessions.getSessions();
    Iterable<SessionInfo> sessionInfo = Iterables.transform(sessions, toSessionInfo());
    return ImmutableList.copyOf(sessionInfo);
  }
  
  public Response getResponse() {
    return response;
  }
  
  private Function<SessionId, SessionInfo> toSessionInfo() {
    new Function() {
      public GetAllSessions.SessionInfo apply(SessionId id) {
        Map<String, ?> capabilities = allSessions.get(id).getCapabilities().asMap();
        return new GetAllSessions.SessionInfo(id, capabilities, null);
      }
    };
  }
  
  public static class SessionInfo
  {
    private final SessionId id;
    private final Map<String, ?> capabilities;
    
    private SessionInfo(SessionId id, Map<String, ?> capabilities) {
      this.id = id;
      this.capabilities = capabilities;
    }
    
    public String getId() {
      return id.toString();
    }
    
    public Map<String, ?> getCapabilities() {
      return capabilities;
    }
  }
}
