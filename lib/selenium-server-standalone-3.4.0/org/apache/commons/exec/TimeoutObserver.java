package org.apache.commons.exec;

public abstract interface TimeoutObserver
{
  public abstract void timeoutOccured(Watchdog paramWatchdog);
}
