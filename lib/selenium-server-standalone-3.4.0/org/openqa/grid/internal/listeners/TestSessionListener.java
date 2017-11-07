package org.openqa.grid.internal.listeners;

import org.openqa.grid.internal.TestSession;

public abstract interface TestSessionListener
{
  public abstract void beforeSession(TestSession paramTestSession);
  
  public abstract void afterSession(TestSession paramTestSession);
}
