package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;




































































































@ThreadSafe
@Beta
@GwtIncompatible
public abstract class RateLimiter
{
  private final SleepingStopwatch stopwatch;
  private volatile Object mutexDoNotUseDirectly;
  
  public static RateLimiter create(double permitsPerSecond)
  {
    return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond);
  }
  



  @VisibleForTesting
  static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond)
  {
    RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
    rateLimiter.setRate(permitsPerSecond);
    return rateLimiter;
  }
  























  public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit)
  {
    Preconditions.checkArgument(warmupPeriod >= 0L, "warmupPeriod must not be negative: %s", warmupPeriod);
    return create(
      SleepingStopwatch.createFromSystemTimer(), permitsPerSecond, warmupPeriod, unit, 3.0D);
  }
  




  @VisibleForTesting
  static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor)
  {
    RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
    rateLimiter.setRate(permitsPerSecond);
    return rateLimiter;
  }
  








  private Object mutex()
  {
    Object mutex = mutexDoNotUseDirectly;
    if (mutex == null) {
      synchronized (this) {
        mutex = mutexDoNotUseDirectly;
        if (mutex == null) {
          mutexDoNotUseDirectly = (mutex = new Object());
        }
      }
    }
    return mutex;
  }
  
  RateLimiter(SleepingStopwatch stopwatch) {
    this.stopwatch = ((SleepingStopwatch)Preconditions.checkNotNull(stopwatch));
  }
  

















  public final void setRate(double permitsPerSecond)
  {
    Preconditions.checkArgument((permitsPerSecond > 0.0D) && 
      (!Double.isNaN(permitsPerSecond)), "rate must be positive");
    synchronized (mutex()) {
      doSetRate(permitsPerSecond, stopwatch.readMicros());
    }
  }
  



  abstract void doSetRate(double paramDouble, long paramLong);
  


  public final double getRate()
  {
    synchronized (mutex()) {
      return doGetRate();
    }
  }
  




  abstract double doGetRate();
  




  @CanIgnoreReturnValue
  public double acquire()
  {
    return acquire(1);
  }
  








  @CanIgnoreReturnValue
  public double acquire(int permits)
  {
    long microsToWait = reserve(permits);
    stopwatch.sleepMicrosUninterruptibly(microsToWait);
    return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
  }
  





  final long reserve(int permits)
  {
    checkPermits(permits);
    synchronized (mutex()) {
      return reserveAndGetWaitLength(permits, stopwatch.readMicros());
    }
  }
  











  public boolean tryAcquire(long timeout, TimeUnit unit)
  {
    return tryAcquire(1, timeout, unit);
  }
  









  public boolean tryAcquire(int permits)
  {
    return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
  }
  








  public boolean tryAcquire()
  {
    return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
  }
  










  public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
  {
    long timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
    checkPermits(permits);
    long microsToWait;
    synchronized (mutex()) {
      long nowMicros = stopwatch.readMicros();
      if (!canAcquire(nowMicros, timeoutMicros)) {
        return false;
      }
      microsToWait = reserveAndGetWaitLength(permits, nowMicros);
    }
    long microsToWait;
    stopwatch.sleepMicrosUninterruptibly(microsToWait);
    return true;
  }
  
  private boolean canAcquire(long nowMicros, long timeoutMicros) {
    return queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros;
  }
  




  final long reserveAndGetWaitLength(int permits, long nowMicros)
  {
    long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
    return Math.max(momentAvailable - nowMicros, 0L);
  }
  





  abstract long queryEarliestAvailable(long paramLong);
  




  abstract long reserveEarliestAvailable(int paramInt, long paramLong);
  




  public String toString()
  {
    return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
  }
  

  static abstract class SleepingStopwatch
  {
    protected SleepingStopwatch() {}
    

    protected abstract long readMicros();
    

    protected abstract void sleepMicrosUninterruptibly(long paramLong);
    

    public static final SleepingStopwatch createFromSystemTimer()
    {
      new SleepingStopwatch() {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        
        protected long readMicros()
        {
          return stopwatch.elapsed(TimeUnit.MICROSECONDS);
        }
        
        protected void sleepMicrosUninterruptibly(long micros)
        {
          if (micros > 0L) {
            Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
          }
        }
      };
    }
  }
  
  private static void checkPermits(int permits) {
    Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
  }
}
