package org.seleniumhq.jetty9.server.session;

import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.util.log.Logger;





























public class NullSessionCache
  extends AbstractSessionCache
{
  public NullSessionCache(SessionHandler handler)
  {
    super(handler);
    super.setEvictionPolicy(0);
  }
  





  public void shutdown() {}
  





  public Session newSession(SessionData data)
  {
    return new Session(getSessionHandler(), data);
  }
  




  public Session newSession(HttpServletRequest request, SessionData data)
  {
    return new Session(getSessionHandler(), request, data);
  }
  





  public Session doGet(String id)
  {
    return null;
  }
  





  public Session doPutIfAbsent(String id, Session session)
  {
    return null;
  }
  





  public boolean doReplace(String id, Session oldValue, Session newValue)
  {
    return true;
  }
  




  public Session doDelete(String id)
  {
    return null;
  }
  




  public void setEvictionPolicy(int evictionTimeout)
  {
    LOG.warn("Ignoring eviction setting:" + evictionTimeout, new Object[0]);
  }
}
