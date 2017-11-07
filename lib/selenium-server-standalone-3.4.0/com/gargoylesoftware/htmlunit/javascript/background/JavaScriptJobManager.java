package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.Page;
import java.io.Serializable;

public abstract interface JavaScriptJobManager
  extends Serializable
{
  public abstract int getJobCount();
  
  public abstract int getJobCount(JavaScriptJobFilter paramJavaScriptJobFilter);
  
  public abstract int addJob(JavaScriptJob paramJavaScriptJob, Page paramPage);
  
  public abstract void removeJob(int paramInt);
  
  public abstract void removeAllJobs();
  
  public abstract void stopJob(int paramInt);
  
  public abstract int waitForJobs(long paramLong);
  
  public abstract int waitForJobsStartingBefore(long paramLong);
  
  public abstract int waitForJobsStartingBefore(long paramLong, JavaScriptJobFilter paramJavaScriptJobFilter);
  
  public abstract void shutdown();
  
  public abstract JavaScriptJob getEarliestJob();
  
  public abstract JavaScriptJob getEarliestJob(JavaScriptJobFilter paramJavaScriptJobFilter);
  
  public abstract boolean runSingleJob(JavaScriptJob paramJavaScriptJob);
  
  public abstract String jobStatusDump(JavaScriptJobFilter paramJavaScriptJobFilter);
  
  public static abstract interface JavaScriptJobFilter
  {
    public abstract boolean passes(JavaScriptJob paramJavaScriptJob);
  }
}
