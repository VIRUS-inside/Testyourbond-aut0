package org.seleniumhq.jetty9.server.session;








public class NullSessionCacheFactory
  implements SessionCacheFactory
{
  boolean _saveOnCreate;
  





  boolean _removeUnloadableSessions;
  






  public NullSessionCacheFactory() {}
  






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
  






  public SessionCache getSessionCache(SessionHandler handler)
  {
    NullSessionCache cache = new NullSessionCache(handler);
    cache.setSaveOnCreate(isSaveOnCreate());
    cache.setRemoveUnloadableSessions(isRemoveUnloadableSessions());
    return cache;
  }
}
