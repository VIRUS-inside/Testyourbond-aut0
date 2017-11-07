package org.openqa.selenium.support.ui;








public class SystemClock
  implements Clock
{
  public SystemClock() {}
  







  public long laterBy(long durationInMillis)
  {
    return System.currentTimeMillis() + durationInMillis;
  }
  
  public boolean isNowBefore(long endInMillis) {
    return System.currentTimeMillis() < endInMillis;
  }
  
  public long now() {
    return System.currentTimeMillis();
  }
}
