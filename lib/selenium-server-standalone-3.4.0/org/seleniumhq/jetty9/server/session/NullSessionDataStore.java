package org.seleniumhq.jetty9.server.session;

import java.util.Set;


























public class NullSessionDataStore
  extends AbstractSessionDataStore
{
  public NullSessionDataStore() {}
  
  public SessionData load(String id)
    throws Exception
  {
    return null;
  }
  





  public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs)
  {
    return new SessionData(id, _context.getCanonicalContextPath(), _context.getVhost(), created, accessed, lastAccessed, maxInactiveMs);
  }
  



  public boolean delete(String id)
    throws Exception
  {
    return true;
  }
  






  public void doStore(String id, SessionData data, long lastSaveTime)
    throws Exception
  {}
  





  public Set<String> doGetExpired(Set<String> candidates)
  {
    return candidates;
  }
  





  public boolean isPassivating()
  {
    return false;
  }
  





  public boolean exists(String id)
  {
    return false;
  }
}
