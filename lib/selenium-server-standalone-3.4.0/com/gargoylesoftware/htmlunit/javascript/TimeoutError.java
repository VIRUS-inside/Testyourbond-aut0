package com.gargoylesoftware.htmlunit.javascript;







public class TimeoutError
  extends Error
{
  private final long allowedTime_;
  





  private final long executionTime_;
  






  TimeoutError(long allowedTime, long executionTime)
  {
    super("Javascript execution takes too long (allowed: " + allowedTime + ", already elapsed: " + executionTime + ")");
    allowedTime_ = allowedTime;
    executionTime_ = executionTime;
  }
  



  long getAllowedTime()
  {
    return allowedTime_;
  }
  



  long getExecutionTime()
  {
    return executionTime_;
  }
}
