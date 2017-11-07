package org.openqa.selenium.support.ui;

import java.util.concurrent.TimeUnit;





















public abstract interface Sleeper
{
  public static final Sleeper SYSTEM_SLEEPER = new Sleeper() {
    public void sleep(Duration duration) throws InterruptedException {
      Thread.sleep(duration.in(TimeUnit.MILLISECONDS));
    }
  };
  
  public abstract void sleep(Duration paramDuration)
    throws InterruptedException;
}
