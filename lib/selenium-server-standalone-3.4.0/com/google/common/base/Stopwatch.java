package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.TimeUnit;








































































@GwtCompatible
public final class Stopwatch
{
  private final Ticker ticker;
  private boolean isRunning;
  private long elapsedNanos;
  private long startTick;
  
  public static Stopwatch createUnstarted()
  {
    return new Stopwatch();
  }
  




  public static Stopwatch createUnstarted(Ticker ticker)
  {
    return new Stopwatch(ticker);
  }
  




  public static Stopwatch createStarted()
  {
    return new Stopwatch().start();
  }
  




  public static Stopwatch createStarted(Ticker ticker)
  {
    return new Stopwatch(ticker).start();
  }
  
  Stopwatch() {
    ticker = Ticker.systemTicker();
  }
  
  Stopwatch(Ticker ticker) {
    this.ticker = ((Ticker)Preconditions.checkNotNull(ticker, "ticker"));
  }
  




  public boolean isRunning()
  {
    return isRunning;
  }
  





  @CanIgnoreReturnValue
  public Stopwatch start()
  {
    Preconditions.checkState(!isRunning, "This stopwatch is already running.");
    isRunning = true;
    startTick = ticker.read();
    return this;
  }
  






  @CanIgnoreReturnValue
  public Stopwatch stop()
  {
    long tick = ticker.read();
    Preconditions.checkState(isRunning, "This stopwatch is already stopped.");
    isRunning = false;
    elapsedNanos += tick - startTick;
    return this;
  }
  




  @CanIgnoreReturnValue
  public Stopwatch reset()
  {
    elapsedNanos = 0L;
    isRunning = false;
    return this;
  }
  
  private long elapsedNanos() {
    return isRunning ? ticker.read() - startTick + elapsedNanos : elapsedNanos;
  }
  








  public long elapsed(TimeUnit desiredUnit)
  {
    return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
  }
  



  public String toString()
  {
    long nanos = elapsedNanos();
    
    TimeUnit unit = chooseUnit(nanos);
    double value = nanos / TimeUnit.NANOSECONDS.convert(1L, unit);
    

    return Platform.formatCompact4Digits(value) + " " + abbreviate(unit);
  }
  
  private static TimeUnit chooseUnit(long nanos) {
    if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
      return TimeUnit.DAYS;
    }
    if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
      return TimeUnit.HOURS;
    }
    if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
      return TimeUnit.MINUTES;
    }
    if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
      return TimeUnit.SECONDS;
    }
    if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
      return TimeUnit.MILLISECONDS;
    }
    if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
      return TimeUnit.MICROSECONDS;
    }
    return TimeUnit.NANOSECONDS;
  }
  
  private static String abbreviate(TimeUnit unit) {
    switch (1.$SwitchMap$java$util$concurrent$TimeUnit[unit.ordinal()]) {
    case 1: 
      return "ns";
    case 2: 
      return "μs";
    case 3: 
      return "ms";
    case 4: 
      return "s";
    case 5: 
      return "min";
    case 6: 
      return "h";
    case 7: 
      return "d";
    }
    throw new AssertionError();
  }
}
