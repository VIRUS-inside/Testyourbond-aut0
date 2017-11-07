package org.openqa.selenium.remote.server;








public class SystemClock
  implements Clock
{
  public SystemClock() {}
  







  public long now()
  {
    return System.currentTimeMillis();
  }
  
  public void pass(long durationInMillis) {
    try {
      Thread.sleep(durationInMillis);
    }
    catch (InterruptedException localInterruptedException) {}
  }
}
