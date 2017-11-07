package org.apache.commons.exec;








public class DefaultExecuteResultHandler
  implements ExecuteResultHandler
{
  private static final int SLEEP_TIME_MS = 50;
  






  private volatile boolean hasResult;
  





  private volatile int exitValue;
  





  private volatile ExecuteException exception;
  






  public DefaultExecuteResultHandler()
  {
    hasResult = false;
    exitValue = -559038737;
  }
  


  public void onProcessComplete(int exitValue)
  {
    this.exitValue = exitValue;
    exception = null;
    hasResult = true;
  }
  


  public void onProcessFailed(ExecuteException e)
  {
    exitValue = e.getExitValue();
    exception = e;
    hasResult = true;
  }
  






  public ExecuteException getException()
  {
    if (!hasResult) {
      throw new IllegalStateException("The process has not exited yet therefore no result is available ...");
    }
    
    return exception;
  }
  






  public int getExitValue()
  {
    if (!hasResult) {
      throw new IllegalStateException("The process has not exited yet therefore no result is available ...");
    }
    
    return exitValue;
  }
  




  public boolean hasResult()
  {
    return hasResult;
  }
  











  public void waitFor()
    throws InterruptedException
  {
    while (!hasResult()) {
      Thread.sleep(50L);
    }
  }
  












  public void waitFor(long timeout)
    throws InterruptedException
  {
    long until = System.currentTimeMillis() + timeout;
    
    while ((!hasResult()) && (System.currentTimeMillis() < until)) {
      Thread.sleep(50L);
    }
  }
}
