package org.openqa.selenium.remote.server;

import java.util.Set;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

public abstract interface DriverSessions
{
  public abstract SessionId newSession(Capabilities paramCapabilities)
    throws Exception;
  
  public abstract Session get(SessionId paramSessionId);
  
  public abstract void deleteSession(SessionId paramSessionId);
  
  public abstract void registerDriver(Capabilities paramCapabilities, Class<? extends WebDriver> paramClass);
  
  public abstract Set<SessionId> getSessions();
}
