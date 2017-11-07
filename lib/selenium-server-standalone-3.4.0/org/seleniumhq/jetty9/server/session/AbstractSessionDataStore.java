package org.seleniumhq.jetty9.server.session;

import java.util.Set;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;


























public abstract class AbstractSessionDataStore
  extends ContainerLifeCycle
  implements SessionDataStore
{
  protected SessionContext _context;
  protected int _gracePeriodSec = 3600;
  protected long _lastExpiryCheckTime = 0L;
  





  public AbstractSessionDataStore() {}
  




  public abstract void doStore(String paramString, SessionData paramSessionData, long paramLong)
    throws Exception;
  




  public abstract Set<String> doGetExpired(Set<String> paramSet);
  




  public void initialize(SessionContext context)
    throws Exception
  {
    if (isStarted())
      throw new IllegalStateException("Context set after SessionDataStore started");
    _context = context;
  }
  



  public void store(String id, SessionData data)
    throws Exception
  {
    long lastSave = data.getLastSaved();
    

    data.setLastSaved(System.currentTimeMillis());
    
    try
    {
      doStore(id, data, lastSave);
      data.setDirty(false);

    }
    catch (Exception e)
    {
      data.setLastSaved(lastSave);
      throw e;
    }
  }
  






  public Set<String> getExpired(Set<String> candidates)
  {
    try
    {
      return doGetExpired(candidates);
    }
    finally
    {
      _lastExpiryCheckTime = System.currentTimeMillis();
    }
  }
  







  public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs)
  {
    return new SessionData(id, _context.getCanonicalContextPath(), _context.getVhost(), created, accessed, lastAccessed, maxInactiveMs);
  }
  
  protected void checkStarted() throws IllegalStateException
  {
    if (isStarted()) {
      throw new IllegalStateException("Already started");
    }
  }
  
  protected void doStart() throws Exception
  {
    if (_context == null) {
      throw new IllegalStateException("No SessionContext");
    }
    super.doStart();
  }
  
  public int getGracePeriodSec()
  {
    return _gracePeriodSec;
  }
  
  public void setGracePeriodSec(int sec)
  {
    _gracePeriodSec = sec;
  }
  





  public String toString()
  {
    return String.format("%s@%x[passivating=%b,graceSec=%d]", new Object[] { getClass().getName(), Integer.valueOf(hashCode()), Boolean.valueOf(isPassivating()), Integer.valueOf(getGracePeriodSec()) });
  }
}
