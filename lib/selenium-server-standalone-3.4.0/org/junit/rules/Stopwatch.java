package org.junit.rules;

import java.util.concurrent.TimeUnit;
import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;





































































public abstract class Stopwatch
  implements TestRule
{
  private final Clock clock;
  private volatile long startNanos;
  private volatile long endNanos;
  
  public Stopwatch()
  {
    this(new Clock());
  }
  
  Stopwatch(Clock clock) {
    this.clock = clock;
  }
  





  public long runtime(TimeUnit unit)
  {
    return unit.convert(getNanos(), TimeUnit.NANOSECONDS);
  }
  



  protected void succeeded(long nanos, Description description) {}
  



  protected void failed(long nanos, Throwable e, Description description) {}
  



  protected void skipped(long nanos, AssumptionViolatedException e, Description description) {}
  



  protected void finished(long nanos, Description description) {}
  



  private long getNanos()
  {
    if (startNanos == 0L) {
      throw new IllegalStateException("Test has not started");
    }
    long currentEndNanos = endNanos;
    if (currentEndNanos == 0L) {
      currentEndNanos = clock.nanoTime();
    }
    
    return currentEndNanos - startNanos;
  }
  
  private void starting() {
    startNanos = clock.nanoTime();
    endNanos = 0L;
  }
  
  private void stopping() {
    endNanos = clock.nanoTime();
  }
  
  public final Statement apply(Statement base, Description description) {
    return new InternalWatcher(null).apply(base, description);
  }
  
  private class InternalWatcher extends TestWatcher {
    private InternalWatcher() {}
    
    protected void starting(Description description) { Stopwatch.this.starting(); }
    
    protected void finished(Description description)
    {
      finished(Stopwatch.this.getNanos(), description);
    }
    
    protected void succeeded(Description description) {
      Stopwatch.this.stopping();
      succeeded(Stopwatch.this.getNanos(), description);
    }
    
    protected void failed(Throwable e, Description description) {
      Stopwatch.this.stopping();
      failed(Stopwatch.this.getNanos(), e, description);
    }
    
    protected void skipped(AssumptionViolatedException e, Description description) {
      Stopwatch.this.stopping();
      skipped(Stopwatch.this.getNanos(), e, description);
    }
  }
  
  static class Clock {
    Clock() {}
    
    public long nanoTime() { return System.nanoTime(); }
  }
}
