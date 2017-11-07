package org.openqa.selenium.support.ui;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;




























public abstract class SlowLoadableComponent<T extends LoadableComponent<T>>
  extends LoadableComponent<T>
{
  private final Clock clock;
  private final long timeOutInSeconds;
  
  public SlowLoadableComponent(Clock clock, int timeOutInSeconds)
  {
    this.clock = clock;
    this.timeOutInSeconds = timeOutInSeconds;
  }
  
  public T get()
  {
    try
    {
      isLoaded();
      return this;
    } catch (Error e) {
      load();
      

      long end = clock.laterBy(TimeUnit.SECONDS.toMillis(timeOutInSeconds));
      
      while (clock.isNowBefore(end)) {
        try {
          isLoaded();
          return this;

        }
        catch (Error localError1)
        {
          isError();
        }
        waitFor();
      }
      
      isLoaded(); }
    return this;
  }
  




  protected void isError()
    throws Error
  {}
  



  protected long sleepFor()
  {
    return 200L;
  }
  
  private void waitFor() {
    try {
      Thread.sleep(sleepFor());
    } catch (InterruptedException e) {
      Assert.fail(e.getMessage());
    }
  }
}
