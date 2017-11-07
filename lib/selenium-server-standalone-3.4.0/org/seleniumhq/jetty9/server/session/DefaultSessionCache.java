package org.seleniumhq.jetty9.server.session;

import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.statistic.CounterStatistic;


























public class DefaultSessionCache
  extends AbstractSessionCache
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  




  protected ConcurrentHashMap<String, Session> _sessions = new ConcurrentHashMap();
  
  private final CounterStatistic _stats = new CounterStatistic();
  





  public DefaultSessionCache(SessionHandler manager)
  {
    super(manager);
  }
  




  public long getSessionsCurrent()
  {
    return _stats.getCurrent();
  }
  




  public long getSessionsMax()
  {
    return _stats.getMax();
  }
  




  public long getSessionsTotal()
  {
    return _stats.getTotal();
  }
  



  public void resetStats()
  {
    _stats.reset();
  }
  





  public Session doGet(String id)
  {
    if (id == null) {
      return null;
    }
    Session session = (Session)_sessions.get(id);
    
    return session;
  }
  





  public Session doPutIfAbsent(String id, Session session)
  {
    Session s = (Session)_sessions.putIfAbsent(id, session);
    if ((s == null) && (!(session instanceof AbstractSessionCache.PlaceHolderSession)))
      _stats.increment();
    return s;
  }
  






  public Session doDelete(String id)
  {
    Session s = (Session)_sessions.remove(id);
    if ((s != null) && (!(s instanceof AbstractSessionCache.PlaceHolderSession)))
      _stats.decrement();
    return s;
  }
  







  public void shutdown()
  {
    int loop = 100;
    while ((!_sessions.isEmpty()) && (loop-- > 0))
    {
      for (Session session : _sessions.values())
      {

        if (_sessionDataStore != null)
        {
          if (session.getSessionData().isDirty())
          {
            session.willPassivate();
            try
            {
              _sessionDataStore.store(session.getId(), session.getSessionData());
            }
            catch (Exception e)
            {
              LOG.warn(e);
            }
          }
          doDelete(session.getId());

        }
        else
        {
          try
          {
            session.invalidate();
          }
          catch (Exception e)
          {
            LOG.ignore(e);
          }
        }
      }
    }
  }
  






  public Session newSession(HttpServletRequest request, SessionData data)
  {
    Session s = new Session(getSessionHandler(), request, data);
    return s;
  }
  







  public Session newSession(SessionData data)
  {
    Session s = new Session(getSessionHandler(), data);
    return s;
  }
  







  public boolean doReplace(String id, Session oldValue, Session newValue)
  {
    boolean result = _sessions.replace(id, oldValue, newValue);
    if ((result) && ((oldValue instanceof AbstractSessionCache.PlaceHolderSession)))
      _stats.increment();
    return result;
  }
}
