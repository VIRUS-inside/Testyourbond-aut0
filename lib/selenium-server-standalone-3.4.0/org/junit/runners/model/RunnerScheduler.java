package org.junit.runners.model;

public abstract interface RunnerScheduler
{
  public abstract void schedule(Runnable paramRunnable);
  
  public abstract void finished();
}
