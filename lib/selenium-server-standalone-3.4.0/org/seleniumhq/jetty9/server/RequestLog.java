package org.seleniumhq.jetty9.server;

public abstract interface RequestLog
{
  public abstract void log(Request paramRequest, Response paramResponse);
}
