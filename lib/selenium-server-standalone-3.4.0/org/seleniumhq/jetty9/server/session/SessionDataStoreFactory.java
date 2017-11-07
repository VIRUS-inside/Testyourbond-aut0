package org.seleniumhq.jetty9.server.session;

public abstract interface SessionDataStoreFactory
{
  public abstract SessionDataStore getSessionDataStore(SessionHandler paramSessionHandler)
    throws Exception;
}
