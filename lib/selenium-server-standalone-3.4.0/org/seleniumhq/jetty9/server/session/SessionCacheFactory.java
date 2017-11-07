package org.seleniumhq.jetty9.server.session;

public abstract interface SessionCacheFactory
{
  public abstract SessionCache getSessionCache(SessionHandler paramSessionHandler);
}
