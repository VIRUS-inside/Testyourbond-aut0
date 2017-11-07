package org.seleniumhq.jetty9.server.session;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.util.component.LifeCycle;

public abstract interface SessionCache
  extends LifeCycle
{
  public static final int NEVER_EVICT = -1;
  public static final int EVICT_ON_SESSION_EXIT = 0;
  public static final int EVICT_ON_INACTIVITY = 1;
  
  public abstract void initialize(SessionContext paramSessionContext);
  
  public abstract void shutdown();
  
  public abstract SessionHandler getSessionHandler();
  
  public abstract Session newSession(HttpServletRequest paramHttpServletRequest, String paramString, long paramLong1, long paramLong2);
  
  public abstract Session newSession(SessionData paramSessionData);
  
  public abstract Session renewSessionId(String paramString1, String paramString2)
    throws Exception;
  
  public abstract Session get(String paramString)
    throws Exception;
  
  public abstract void put(String paramString, Session paramSession)
    throws Exception;
  
  public abstract boolean contains(String paramString)
    throws Exception;
  
  public abstract boolean exists(String paramString)
    throws Exception;
  
  public abstract Session delete(String paramString)
    throws Exception;
  
  public abstract Set<String> checkExpiration(Set<String> paramSet);
  
  public abstract void checkInactiveSession(Session paramSession);
  
  public abstract void setSessionDataStore(SessionDataStore paramSessionDataStore);
  
  public abstract SessionDataStore getSessionDataStore();
  
  public abstract void setEvictionPolicy(int paramInt);
  
  public abstract int getEvictionPolicy();
  
  public abstract void setSaveOnInactiveEviction(boolean paramBoolean);
  
  public abstract boolean isSaveOnInactiveEviction();
  
  public abstract void setSaveOnCreate(boolean paramBoolean);
  
  public abstract boolean isSaveOnCreate();
  
  public abstract void setRemoveUnloadableSessions(boolean paramBoolean);
  
  public abstract boolean isRemoveUnloadableSessions();
}
