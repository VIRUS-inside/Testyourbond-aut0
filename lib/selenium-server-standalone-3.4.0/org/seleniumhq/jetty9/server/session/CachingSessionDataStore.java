package org.seleniumhq.jetty9.server.session;

import java.util.Set;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;







































public class CachingSessionDataStore
  extends ContainerLifeCycle
  implements SessionDataStore
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  




  protected SessionDataStore _store;
  



  protected SessionDataMap _cache;
  




  public CachingSessionDataStore(SessionDataMap cache, SessionDataStore store)
  {
    _cache = cache;
    addBean(_cache, true);
    _store = store;
    addBean(_store, true);
  }
  




  public SessionDataStore getSessionStore()
  {
    return _store;
  }
  





  public SessionDataMap getSessionDataMap()
  {
    return _cache;
  }
  




  public SessionData load(String id)
    throws Exception
  {
    SessionData d = null;
    


    try
    {
      d = _cache.load(id);
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
    
    if (d != null) {
      return d;
    }
    
    d = _store.load(id);
    
    return d;
  }
  





  public boolean delete(String id)
    throws Exception
  {
    boolean deleted = _store.delete(id);
    
    _cache.delete(id);
    
    return deleted;
  }
  





  public Set<String> getExpired(Set<String> candidates)
  {
    return _store.getExpired(candidates);
  }
  






  public void store(String id, SessionData data)
    throws Exception
  {
    _store.store(id, data);
    
    _cache.store(id, data);
  }
  


  protected void doStart()
    throws Exception
  {
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    super.doStop();
  }
  




  public boolean isPassivating()
  {
    return _store.isPassivating();
  }
  




  public boolean exists(String id)
    throws Exception
  {
    try
    {
      SessionData data = _cache.load(id);
      if (data != null) {
        return true;
      }
    }
    catch (Exception e) {
      LOG.warn(e);
    }
    

    return _store.exists(id);
  }
  





  public void initialize(SessionContext context)
    throws Exception
  {
    _store.initialize(context);
    _cache.initialize(context);
  }
  




  public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs)
  {
    return _store.newSessionData(id, created, accessed, lastAccessed, maxInactiveMs);
  }
}
