package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicLong;
































































public class ThresholdCircuitBreaker
  extends AbstractCircuitBreaker<Long>
{
  private static final long INITIAL_COUNT = 0L;
  private final long threshold;
  private final AtomicLong used;
  
  public ThresholdCircuitBreaker(long threshold)
  {
    used = new AtomicLong(0L);
    this.threshold = threshold;
  }
  




  public long getThreshold()
  {
    return threshold;
  }
  


  public boolean checkState()
    throws CircuitBreakingException
  {
    return isOpen();
  }
  





  public void close()
  {
    super.close();
    used.set(0L);
  }
  




  public boolean incrementAndCheckState(Long increment)
    throws CircuitBreakingException
  {
    if (threshold == 0L) {
      open();
    }
    
    long used = this.used.addAndGet(increment.longValue());
    if (used > threshold) {
      open();
    }
    
    return checkState();
  }
}
