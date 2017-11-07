package org.seleniumhq.jetty9.server.session;

import java.util.Set;

public abstract interface SessionDataStore
  extends SessionDataMap
{
  public abstract SessionData newSessionData(String paramString, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public abstract Set<String> getExpired(Set<String> paramSet);
  
  public abstract boolean isPassivating();
  
  public abstract boolean exists(String paramString)
    throws Exception;
}
