package org.seleniumhq.jetty9.server.session;

import org.seleniumhq.jetty9.util.component.LifeCycle;

public abstract interface SessionDataMap
  extends LifeCycle
{
  public abstract void initialize(SessionContext paramSessionContext)
    throws Exception;
  
  public abstract SessionData load(String paramString)
    throws Exception;
  
  public abstract void store(String paramString, SessionData paramSessionData)
    throws Exception;
  
  public abstract boolean delete(String paramString)
    throws Exception;
}
