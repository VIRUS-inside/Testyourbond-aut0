package org.openqa.selenium.remote;

import java.util.HashMap;
import java.util.Map;

















public class Command
{
  private SessionId sessionId;
  private String name;
  private Map<String, ?> parameters;
  
  public Command(SessionId sessionId, String name)
  {
    this(sessionId, name, new HashMap());
  }
  
  public Command(SessionId sessionId, String name, Map<String, ?> parameters) {
    this.sessionId = sessionId;
    this.parameters = parameters;
    this.name = name;
  }
  
  public SessionId getSessionId() {
    return sessionId;
  }
  
  public String getName() {
    return name;
  }
  
  public Map<String, ?> getParameters() {
    return parameters == null ? new HashMap() : parameters;
  }
  
  public String toString()
  {
    return "[" + sessionId + ", " + name + " " + parameters + "]";
  }
}
