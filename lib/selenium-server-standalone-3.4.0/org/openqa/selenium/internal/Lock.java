package org.openqa.selenium.internal;

public abstract interface Lock
{
  public abstract void lock(long paramLong);
  
  public abstract void unlock();
}
