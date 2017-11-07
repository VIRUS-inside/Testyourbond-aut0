package org.apache.commons.lang3.time;

import java.util.concurrent.TimeUnit;






























































public class StopWatch
{
  private static final long NANO_2_MILLIS = 1000000L;
  
  public static StopWatch createStarted()
  {
    StopWatch sw = new StopWatch();
    sw.start();
    return sw;
  }
  

  public StopWatch() {}
  
  private static abstract enum State
  {
    UNSTARTED, 
    



    RUNNING, 
    



    STOPPED, 
    



    SUSPENDED;
    







    private State() {}
    






    abstract boolean isStarted();
    






    abstract boolean isStopped();
    






    abstract boolean isSuspended();
  }
  






  private static enum SplitState
  {
    SPLIT, 
    UNSPLIT;
    
    private SplitState() {}
  }
  
  private State runningState = State.UNSTARTED;
  



  private SplitState splitState = SplitState.UNSPLIT;
  








  private long startTime;
  








  private long startTimeMillis;
  







  private long stopTime;
  








  public void start()
  {
    if (runningState == State.STOPPED) {
      throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
    }
    if (runningState != State.UNSTARTED) {
      throw new IllegalStateException("Stopwatch already started. ");
    }
    startTime = System.nanoTime();
    startTimeMillis = System.currentTimeMillis();
    runningState = State.RUNNING;
  }
  












  public void stop()
  {
    if ((runningState != State.RUNNING) && (runningState != State.SUSPENDED)) {
      throw new IllegalStateException("Stopwatch is not running. ");
    }
    if (runningState == State.RUNNING) {
      stopTime = System.nanoTime();
    }
    runningState = State.STOPPED;
  }
  








  public void reset()
  {
    runningState = State.UNSTARTED;
    splitState = SplitState.UNSPLIT;
  }
  












  public void split()
  {
    if (runningState != State.RUNNING) {
      throw new IllegalStateException("Stopwatch is not running. ");
    }
    stopTime = System.nanoTime();
    splitState = SplitState.SPLIT;
  }
  












  public void unsplit()
  {
    if (splitState != SplitState.SPLIT) {
      throw new IllegalStateException("Stopwatch has not been split. ");
    }
    splitState = SplitState.UNSPLIT;
  }
  












  public void suspend()
  {
    if (runningState != State.RUNNING) {
      throw new IllegalStateException("Stopwatch must be running to suspend. ");
    }
    stopTime = System.nanoTime();
    runningState = State.SUSPENDED;
  }
  












  public void resume()
  {
    if (runningState != State.SUSPENDED) {
      throw new IllegalStateException("Stopwatch must be suspended to resume. ");
    }
    startTime += System.nanoTime() - stopTime;
    runningState = State.RUNNING;
  }
  











  public long getTime()
  {
    return getNanoTime() / 1000000L;
  }
  















  public long getTime(TimeUnit timeUnit)
  {
    return timeUnit.convert(getNanoTime(), TimeUnit.NANOSECONDS);
  }
  












  public long getNanoTime()
  {
    if ((runningState == State.STOPPED) || (runningState == State.SUSPENDED))
      return stopTime - startTime;
    if (runningState == State.UNSTARTED)
      return 0L;
    if (runningState == State.RUNNING) {
      return System.nanoTime() - startTime;
    }
    throw new RuntimeException("Illegal running state has occurred.");
  }
  














  public long getSplitTime()
  {
    return getSplitNanoTime() / 1000000L;
  }
  













  public long getSplitNanoTime()
  {
    if (splitState != SplitState.SPLIT) {
      throw new IllegalStateException("Stopwatch must be split to get the split time. ");
    }
    return stopTime - startTime;
  }
  







  public long getStartTime()
  {
    if (runningState == State.UNSTARTED) {
      throw new IllegalStateException("Stopwatch has not been started");
    }
    
    return startTimeMillis;
  }
  











  public String toString()
  {
    return DurationFormatUtils.formatDurationHMS(getTime());
  }
  











  public String toSplitString()
  {
    return DurationFormatUtils.formatDurationHMS(getSplitTime());
  }
  









  public boolean isStarted()
  {
    return runningState.isStarted();
  }
  








  public boolean isSuspended()
  {
    return runningState.isSuspended();
  }
  










  public boolean isStopped()
  {
    return runningState.isStopped();
  }
}
