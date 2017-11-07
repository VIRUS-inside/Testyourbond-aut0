package org.seleniumhq.jetty9.server.session;






public class DefaultSessionCacheFactory
  implements SessionCacheFactory
{
  int _evictionPolicy;
  



  boolean _saveOnInactiveEvict;
  



  boolean _saveOnCreate;
  



  boolean _removeUnloadableSessions;
  




  public DefaultSessionCacheFactory() {}
  




  public boolean isSaveOnCreate()
  {
    return _saveOnCreate;
  }
  





  public void setSaveOnCreate(boolean saveOnCreate)
  {
    _saveOnCreate = saveOnCreate;
  }
  








  public boolean isRemoveUnloadableSessions()
  {
    return _removeUnloadableSessions;
  }
  






  public void setRemoveUnloadableSessions(boolean removeUnloadableSessions)
  {
    _removeUnloadableSessions = removeUnloadableSessions;
  }
  






  public int getEvictionPolicy()
  {
    return _evictionPolicy;
  }
  






  public void setEvictionPolicy(int evictionPolicy)
  {
    _evictionPolicy = evictionPolicy;
  }
  






  public boolean isSaveOnInactiveEvict()
  {
    return _saveOnInactiveEvict;
  }
  






  public void setSaveOnInactiveEvict(boolean saveOnInactiveEvict)
  {
    _saveOnInactiveEvict = saveOnInactiveEvict;
  }
  







  public SessionCache getSessionCache(SessionHandler handler)
  {
    DefaultSessionCache cache = new DefaultSessionCache(handler);
    cache.setEvictionPolicy(getEvictionPolicy());
    cache.setSaveOnInactiveEviction(isSaveOnInactiveEvict());
    cache.setSaveOnCreate(isSaveOnCreate());
    cache.setRemoveUnloadableSessions(isRemoveUnloadableSessions());
    return cache;
  }
}
