package org.seleniumhq.jetty9.server;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.server.session.HouseKeeper;
import org.seleniumhq.jetty9.server.session.SessionHandler;
import org.seleniumhq.jetty9.util.component.LifeCycle;

public abstract interface SessionIdManager
  extends LifeCycle
{
  public abstract boolean isIdInUse(String paramString);
  
  public abstract void expireAll(String paramString);
  
  public abstract void invalidateAll(String paramString);
  
  public abstract String newSessionId(HttpServletRequest paramHttpServletRequest, long paramLong);
  
  public abstract String getWorkerName();
  
  public abstract String getId(String paramString);
  
  public abstract String getExtendedId(String paramString, HttpServletRequest paramHttpServletRequest);
  
  public abstract String renewSessionId(String paramString1, String paramString2, HttpServletRequest paramHttpServletRequest);
  
  public abstract Set<SessionHandler> getSessionHandlers();
  
  public abstract void setSessionHouseKeeper(HouseKeeper paramHouseKeeper);
  
  public abstract HouseKeeper getSessionHouseKeeper();
}
