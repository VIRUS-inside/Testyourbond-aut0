package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.math.LongMath;
import java.util.concurrent.TimeUnit;


































































































































































































@GwtIncompatible
abstract class SmoothRateLimiter
  extends RateLimiter
{
  double storedPermits;
  double maxPermits;
  double stableIntervalMicros;
  
  static final class SmoothWarmingUp
    extends SmoothRateLimiter
  {
    private final long warmupPeriodMicros;
    private double slope;
    private double thresholdPermits;
    private double coldFactor;
    
    SmoothWarmingUp(RateLimiter.SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor)
    {
      super(null);
      warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
      this.coldFactor = coldFactor;
    }
    
    void doSetRate(double permitsPerSecond, double stableIntervalMicros)
    {
      double oldMaxPermits = maxPermits;
      double coldIntervalMicros = stableIntervalMicros * coldFactor;
      thresholdPermits = (0.5D * warmupPeriodMicros / stableIntervalMicros);
      maxPermits = (thresholdPermits + 2.0D * warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros));
      
      slope = ((coldIntervalMicros - stableIntervalMicros) / (maxPermits - thresholdPermits));
      if (oldMaxPermits == Double.POSITIVE_INFINITY)
      {
        storedPermits = 0.0D;
      } else {
        storedPermits = (oldMaxPermits == 0.0D ? maxPermits : storedPermits * maxPermits / oldMaxPermits);
      }
    }
    



    long storedPermitsToWaitTime(double storedPermits, double permitsToTake)
    {
      double availablePermitsAboveThreshold = storedPermits - thresholdPermits;
      long micros = 0L;
      
      if (availablePermitsAboveThreshold > 0.0D) {
        double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake);
        

        double length = permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
        micros = (permitsAboveThresholdToTake * length / 2.0D);
        permitsToTake -= permitsAboveThresholdToTake;
      }
      
      micros = (micros + stableIntervalMicros * permitsToTake);
      return micros;
    }
    
    private double permitsToTime(double permits) {
      return stableIntervalMicros + permits * slope;
    }
    
    double coolDownIntervalMicros()
    {
      return warmupPeriodMicros / maxPermits;
    }
  }
  


  static final class SmoothBursty
    extends SmoothRateLimiter
  {
    final double maxBurstSeconds;
    


    SmoothBursty(RateLimiter.SleepingStopwatch stopwatch, double maxBurstSeconds)
    {
      super(null);
      this.maxBurstSeconds = maxBurstSeconds;
    }
    
    void doSetRate(double permitsPerSecond, double stableIntervalMicros)
    {
      double oldMaxPermits = maxPermits;
      maxPermits = (maxBurstSeconds * permitsPerSecond);
      if (oldMaxPermits == Double.POSITIVE_INFINITY)
      {
        storedPermits = maxPermits;
      } else {
        storedPermits = (oldMaxPermits == 0.0D ? 0.0D : storedPermits * maxPermits / oldMaxPermits);
      }
    }
    



    long storedPermitsToWaitTime(double storedPermits, double permitsToTake)
    {
      return 0L;
    }
    
    double coolDownIntervalMicros()
    {
      return stableIntervalMicros;
    }
  }
  




















  private long nextFreeTicketMicros = 0L;
  
  private SmoothRateLimiter(RateLimiter.SleepingStopwatch stopwatch) {
    super(stopwatch);
  }
  
  final void doSetRate(double permitsPerSecond, long nowMicros)
  {
    resync(nowMicros);
    double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
    this.stableIntervalMicros = stableIntervalMicros;
    doSetRate(permitsPerSecond, stableIntervalMicros);
  }
  
  abstract void doSetRate(double paramDouble1, double paramDouble2);
  
  final double doGetRate()
  {
    return TimeUnit.SECONDS.toMicros(1L) / stableIntervalMicros;
  }
  
  final long queryEarliestAvailable(long nowMicros)
  {
    return nextFreeTicketMicros;
  }
  
  final long reserveEarliestAvailable(int requiredPermits, long nowMicros)
  {
    resync(nowMicros);
    long returnValue = nextFreeTicketMicros;
    double storedPermitsToSpend = Math.min(requiredPermits, storedPermits);
    double freshPermits = requiredPermits - storedPermitsToSpend;
    
    long waitMicros = storedPermitsToWaitTime(storedPermits, storedPermitsToSpend) + (freshPermits * stableIntervalMicros);
    

    nextFreeTicketMicros = LongMath.saturatedAdd(nextFreeTicketMicros, waitMicros);
    storedPermits -= storedPermitsToSpend;
    return returnValue;
  }
  





  abstract long storedPermitsToWaitTime(double paramDouble1, double paramDouble2);
  




  abstract double coolDownIntervalMicros();
  




  void resync(long nowMicros)
  {
    if (nowMicros > nextFreeTicketMicros) {
      double newPermits = (nowMicros - nextFreeTicketMicros) / coolDownIntervalMicros();
      storedPermits = Math.min(maxPermits, storedPermits + newPermits);
      nextFreeTicketMicros = nowMicros;
    }
  }
}
