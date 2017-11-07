package org.openqa.grid.internal;

public abstract interface TimeSource
{
  public abstract long currentTimeInMillis();
}
