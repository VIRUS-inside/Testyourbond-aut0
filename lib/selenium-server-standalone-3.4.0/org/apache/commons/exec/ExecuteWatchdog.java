package org.apache.commons.exec;

import org.apache.commons.exec.util.DebugUtils;


































































public class ExecuteWatchdog
  implements TimeoutObserver
{
  public static final long INFINITE_TIMEOUT = -1L;
  private Process process;
  private final boolean hasWatchdog;
  private boolean watch;
  private Exception caught;
  private boolean killedProcess;
  private final Watchdog watchdog;
  private volatile boolean processStarted;
  
  public ExecuteWatchdog(long timeout)
  {
    killedProcess = false;
    watch = false;
    hasWatchdog = (timeout != -1L);
    processStarted = false;
    if (hasWatchdog) {
      watchdog = new Watchdog(timeout);
      watchdog.addTimeoutObserver(this);
    }
    else {
      watchdog = null;
    }
  }
  








  public synchronized void start(Process processToMonitor)
  {
    if (processToMonitor == null) {
      throw new NullPointerException("process is null.");
    }
    if (process != null) {
      throw new IllegalStateException("Already running.");
    }
    caught = null;
    killedProcess = false;
    watch = true;
    process = processToMonitor;
    processStarted = true;
    notifyAll();
    if (hasWatchdog) {
      watchdog.start();
    }
  }
  



  public synchronized void stop()
  {
    if (hasWatchdog) {
      watchdog.stop();
    }
    watch = false;
    process = null;
  }
  


  public synchronized void destroyProcess()
  {
    ensureStarted();
    timeoutOccured(null);
    stop();
  }
  


  public synchronized void timeoutOccured(Watchdog w)
  {
    try
    {
      try
      {
        if (process != null) {
          process.exitValue();
        }
      }
      catch (IllegalThreadStateException itse)
      {
        if (watch) {
          killedProcess = true;
          process.destroy();
        }
      }
    } catch (Exception e) {
      caught = e;
      DebugUtils.handleException("Getting the exit value of the process failed", e);
    } finally {
      cleanUp();
    }
  }
  









  public synchronized void checkException()
    throws Exception
  {
    if (caught != null) {
      throw caught;
    }
  }
  





  public synchronized boolean isWatching()
  {
    ensureStarted();
    return watch;
  }
  





  public synchronized boolean killedProcess()
  {
    return killedProcess;
  }
  


  protected synchronized void cleanUp()
  {
    watch = false;
    process = null;
  }
  
  void setProcessNotStarted() {
    processStarted = false;
  }
  



  private void ensureStarted()
  {
    while (!processStarted) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }
}
