package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebClient;


























public class GAEJavaScriptExecutor
  extends DefaultJavaScriptExecutor
{
  public GAEJavaScriptExecutor(WebClient webClient)
  {
    super(webClient);
  }
  






  protected void startThreadIfNeeded() {}
  





  public int pumpEventLoop(long timeoutMillis)
  {
    int count = 0;
    long currentTime = System.currentTimeMillis();
    long expirationTime = currentTime + timeoutMillis;
    
    while (currentTime < expirationTime) {
      JavaScriptJobManager jobManager = getJobManagerWithEarliestJob();
      if (jobManager == null) {
        break;
      }
      
      JavaScriptJob earliestJob = jobManager.getEarliestJob();
      if (earliestJob == null) {
        break;
      }
      if (expirationTime < earliestJob.getTargetExecutionTime()) {
        break;
      }
      

      long sleepTime = earliestJob.getTargetExecutionTime() - currentTime;
      if (sleepTime > 0L) {
        try {
          Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      boolean ran = jobManager.runSingleJob(earliestJob);
      if (ran) {
        count++;
      }
      currentTime = System.currentTimeMillis();
    }
    return count;
  }
}
