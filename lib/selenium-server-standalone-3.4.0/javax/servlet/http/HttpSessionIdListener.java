package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionIdListener
  extends EventListener
{
  public abstract void sessionIdChanged(HttpSessionEvent paramHttpSessionEvent, String paramString);
}
