package org.openqa.grid.internal.listeners;

import org.openqa.grid.internal.TestSession;

public abstract interface TimeoutListener
{
  public abstract void beforeRelease(TestSession paramTestSession);
}
