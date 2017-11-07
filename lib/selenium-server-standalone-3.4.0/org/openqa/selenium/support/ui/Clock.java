package org.openqa.selenium.support.ui;

public abstract interface Clock
{
  public abstract long now();
  
  public abstract long laterBy(long paramLong);
  
  public abstract boolean isNowBefore(long paramLong);
}
