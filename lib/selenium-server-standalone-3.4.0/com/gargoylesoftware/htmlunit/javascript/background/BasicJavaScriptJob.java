package com.gargoylesoftware.htmlunit.javascript.background;







public abstract class BasicJavaScriptJob
  implements JavaScriptJob
{
  private Integer id_;
  





  private final int initialDelay_;
  





  private final Integer period_;
  




  private final boolean executeAsap_;
  




  private long targetExecutionTime_;
  





  public BasicJavaScriptJob()
  {
    this(0, null);
  }
  




  BasicJavaScriptJob(int initialDelay, Integer period)
  {
    initialDelay_ = initialDelay;
    period_ = period;
    setTargetExecutionTime(initialDelay + System.currentTimeMillis());
    executeAsap_ = (initialDelay == 0);
  }
  




  public void setId(Integer id)
  {
    id_ = id;
  }
  




  public Integer getId()
  {
    return id_;
  }
  



  public int getInitialDelay()
  {
    return initialDelay_;
  }
  




  public Integer getPeriod()
  {
    return period_;
  }
  




  public boolean isPeriodic()
  {
    return period_ != null;
  }
  




  public boolean isExecuteAsap()
  {
    return executeAsap_;
  }
  

  public String toString()
  {
    return "JavaScript Job " + id_;
  }
  

  public int compareTo(JavaScriptJob other)
  {
    boolean xhr1 = executeAsap_;
    boolean xhr2 = other.isExecuteAsap();
    
    if ((xhr1) && (xhr2)) {
      return getId().intValue() - other.getId().intValue();
    }
    
    if (xhr1) {
      return -1;
    }
    
    if (xhr2) {
      return 1;
    }
    
    return (int)(targetExecutionTime_ - other.getTargetExecutionTime());
  }
  




  public long getTargetExecutionTime()
  {
    return targetExecutionTime_;
  }
  




  public void setTargetExecutionTime(long targetExecutionTime)
  {
    targetExecutionTime_ = targetExecutionTime;
  }
}
