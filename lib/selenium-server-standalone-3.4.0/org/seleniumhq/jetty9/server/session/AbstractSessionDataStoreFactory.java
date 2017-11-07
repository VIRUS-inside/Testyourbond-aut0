package org.seleniumhq.jetty9.server.session;










public abstract class AbstractSessionDataStoreFactory
  implements SessionDataStoreFactory
{
  int _gracePeriodSec;
  









  public AbstractSessionDataStoreFactory() {}
  









  public int getGracePeriodSec()
  {
    return _gracePeriodSec;
  }
  




  public void setGracePeriodSec(int gracePeriodSec)
  {
    _gracePeriodSec = gracePeriodSec;
  }
}
