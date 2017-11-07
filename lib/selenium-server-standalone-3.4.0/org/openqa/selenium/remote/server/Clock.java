package org.openqa.selenium.remote.server;

public abstract interface Clock
{
  public abstract long now();
  
  public abstract void pass(long paramLong);
}
