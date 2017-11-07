package com.gargoylesoftware.htmlunit.javascript.background;

public abstract interface JavaScriptJob
  extends Runnable, Comparable<JavaScriptJob>
{
  public abstract Integer getId();
  
  public abstract void setId(Integer paramInteger);
  
  public abstract long getTargetExecutionTime();
  
  public abstract void setTargetExecutionTime(long paramLong);
  
  public abstract Integer getPeriod();
  
  public abstract boolean isPeriodic();
  
  public abstract boolean isExecuteAsap();
}
