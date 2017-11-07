package org.seleniumhq.jetty9.server.session;









public class CachingSessionDataStoreFactory
  extends AbstractSessionDataStoreFactory
{
  protected SessionDataStoreFactory _sessionStoreFactory;
  







  protected SessionDataMapFactory _mapFactory;
  








  public CachingSessionDataStoreFactory() {}
  







  public SessionDataMapFactory getMapFactory()
  {
    return _mapFactory;
  }
  



  public void setSessionDataMapFactory(SessionDataMapFactory mapFactory)
  {
    _mapFactory = mapFactory;
  }
  




  public void setSessionStoreFactory(SessionDataStoreFactory factory)
  {
    _sessionStoreFactory = factory;
  }
  



  public SessionDataStore getSessionDataStore(SessionHandler handler)
    throws Exception
  {
    return new CachingSessionDataStore(_mapFactory.getSessionDataMap(), _sessionStoreFactory.getSessionDataStore(handler));
  }
}
