package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






































class JavaScriptJobManagerImpl
  implements JavaScriptJobManager
{
  private final transient WeakReference<WebWindow> window_;
  private transient PriorityQueue<JavaScriptJob> scheduledJobsQ_ = new PriorityQueue();
  
  private transient ArrayList<Integer> cancelledJobs_ = new ArrayList();
  
  private transient JavaScriptJob currentlyRunningJob_ = null;
  

  private static final AtomicInteger NEXT_JOB_ID_ = new AtomicInteger(1);
  

  private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerImpl.class);
  




  JavaScriptJobManagerImpl(WebWindow window)
  {
    window_ = new WeakReference(window);
  }
  

  public synchronized int getJobCount()
  {
    return scheduledJobsQ_.size() + (currentlyRunningJob_ != null ? 1 : 0);
  }
  

  public synchronized int getJobCount(JavaScriptJobManager.JavaScriptJobFilter filter)
  {
    if (filter == null) {
      return scheduledJobsQ_.size() + (currentlyRunningJob_ != null ? 1 : 0);
    }
    
    int count = 0;
    if ((currentlyRunningJob_ != null) && (filter.passes(currentlyRunningJob_))) {
      count++;
    }
    for (JavaScriptJob job : scheduledJobsQ_) {
      if (filter.passes(job)) {
        count++;
      }
    }
    return count;
  }
  

  public int addJob(JavaScriptJob job, Page page)
  {
    WebWindow w = getWindow();
    if (w == null)
    {



      return 0;
    }
    if (w.getEnclosedPage() != page)
    {



      return 0;
    }
    int id = NEXT_JOB_ID_.getAndIncrement();
    job.setId(Integer.valueOf(id));
    
    synchronized (this) {
      scheduledJobsQ_.add(job);
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("job added to queue");
        LOG.debug("    window is: " + w);
        LOG.debug("    added job: " + job.toString());
        LOG.debug("after adding job to the queue, the queue is: ");
        printQueue();
      }
      
      notify();
    }
    
    return id;
  }
  

  public synchronized void removeJob(int id)
  {
    for (JavaScriptJob job : scheduledJobsQ_) {
      int jobId = job.getId().intValue();
      if (jobId == id) {
        scheduledJobsQ_.remove(job);
        break;
      }
    }
    cancelledJobs_.add(Integer.valueOf(id));
    notify();
  }
  

  public synchronized void stopJob(int id)
  {
    for (JavaScriptJob job : scheduledJobsQ_) {
      int jobId = job.getId().intValue();
      if (jobId == id) {
        scheduledJobsQ_.remove(job);
        
        break;
      }
    }
    cancelledJobs_.add(Integer.valueOf(id));
    notify();
  }
  

  public synchronized void removeAllJobs()
  {
    if (currentlyRunningJob_ != null) {
      cancelledJobs_.add(currentlyRunningJob_.getId());
    }
    for (JavaScriptJob job : scheduledJobsQ_) {
      cancelledJobs_.add(job.getId());
    }
    scheduledJobsQ_.clear();
    notify();
  }
  

  public int waitForJobs(long timeoutMillis)
  {
    boolean debug = LOG.isDebugEnabled();
    if (debug) {
      LOG.debug("Waiting for all jobs to finish (will wait max " + timeoutMillis + " millis).");
    }
    if (timeoutMillis > 0L) {
      long now = System.currentTimeMillis();
      long end = now + timeoutMillis;
      
      synchronized (this) {
        do {
          try {
            wait(end - now);
          }
          catch (InterruptedException e) {
            LOG.error("InterruptedException while in waitForJobs", e);
          }
          

          now = System.currentTimeMillis();
          if (getJobCount() <= 0) break; } while (now < end);
      }
    }
    









    int jobs = getJobCount();
    if (debug) {
      LOG.debug("Finished waiting for all jobs to finish (final job count is " + jobs + ").");
    }
    return jobs;
  }
  

  public int waitForJobsStartingBefore(long delayMillis)
  {
    return waitForJobsStartingBefore(delayMillis, null);
  }
  

  public int waitForJobsStartingBefore(long delayMillis, JavaScriptJobManager.JavaScriptJobFilter filter)
  {
    boolean debug = LOG.isDebugEnabled();
    
    long latestExecutionTime = System.currentTimeMillis() + delayMillis;
    if (debug) {
      LOG.debug("Waiting for all jobs that have execution time before " + 
        delayMillis + " (" + latestExecutionTime + ") to finish");
    }
    
    long interval = Math.max(40L, delayMillis);
    synchronized (this) {
      JavaScriptJob earliestJob = getEarliestJob(filter);
      boolean pending = (earliestJob != null) && (earliestJob.getTargetExecutionTime() < latestExecutionTime);
      if (!pending) {} for (pending = 
          
            (currentlyRunningJob_ != null) && 
            ((filter == null) || (filter.passes(currentlyRunningJob_))) && (
            currentlyRunningJob_.getTargetExecutionTime() < latestExecutionTime); 
          

          pending; 
          








          pending = 
          
            (currentlyRunningJob_ != null) && 
            ((filter == null) || (filter.passes(currentlyRunningJob_))) && (
            currentlyRunningJob_.getTargetExecutionTime() < latestExecutionTime))
      {
        try
        {
          wait(interval);
        }
        catch (InterruptedException e) {
          LOG.error("InterruptedException while in waitForJobsStartingBefore", e);
        }
        
        earliestJob = getEarliestJob(filter);
        pending = (earliestJob != null) && (earliestJob.getTargetExecutionTime() < latestExecutionTime);
        if (pending) {}
      }
    }
    





    int jobs = getJobCount(filter);
    if (debug) {
      LOG.debug("Finished waiting for all jobs that have target execution time earlier than " + 
        latestExecutionTime + ", final job count is " + jobs);
    }
    return jobs;
  }
  

  public synchronized void shutdown()
  {
    scheduledJobsQ_.clear();
    notify();
  }
  






  private WebWindow getWindow()
  {
    return (WebWindow)window_.get();
  }
  


  private void printQueue()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("------ printing JavaScript job queue -----");
      LOG.debug("  number of jobs on the queue: " + scheduledJobsQ_.size());
      int count = 1;
      for (JavaScriptJob job : scheduledJobsQ_) {
        LOG.debug("  " + count + ")  Job target execution time: " + job.getTargetExecutionTime());
        LOG.debug("      job to string: " + job.toString());
        LOG.debug("      job id: " + job.getId());
        if (job.isPeriodic()) {
          LOG.debug("      period: " + job.getPeriod().intValue());
        }
        count++;
      }
      LOG.debug("------------------------------------------");
    }
  }
  



  public synchronized String jobStatusDump(JavaScriptJobManager.JavaScriptJobFilter filter)
  {
    StringBuilder status = new StringBuilder();
    String lineSeparator = System.lineSeparator();
    status.append("------ JavaScript job status -----");
    status.append(lineSeparator);
    if ((currentlyRunningJob_ != null) && ((filter == null) || (filter.passes(currentlyRunningJob_)))) {
      status.append("  current running job: ").append(currentlyRunningJob_.toString());
      status.append("      job id: " + currentlyRunningJob_.getId());
      status.append(lineSeparator);
      status.append(lineSeparator);
      status.append(lineSeparator);
    }
    status.append("  number of jobs on the queue: " + scheduledJobsQ_.size());
    status.append(lineSeparator);
    int count = 1;
    for (JavaScriptJob job : scheduledJobsQ_) {
      if ((filter == null) || (filter.passes(job))) {
        long now = System.currentTimeMillis();
        long execTime = job.getTargetExecutionTime();
        status.append("  " + count);
        status.append(")  Job target execution time: " + execTime);
        status.append(" (should start in " + (execTime - now) / 1000.0D + "s)");
        status.append(lineSeparator);
        status.append("      job to string: ").append(job.toString());
        status.append(lineSeparator);
        status.append("      job id: " + job.getId());
        status.append(lineSeparator);
        if (job.isPeriodic()) {
          status.append("      period: " + job.getPeriod().intValue());
          status.append(lineSeparator);
        }
        count++;
      }
    }
    status.append("------------------------------------------");
    status.append(lineSeparator);
    
    return status.toString();
  }
  



  public JavaScriptJob getEarliestJob()
  {
    return (JavaScriptJob)scheduledJobsQ_.peek();
  }
  



  public synchronized JavaScriptJob getEarliestJob(JavaScriptJobManager.JavaScriptJobFilter filter)
  {
    if (filter == null) {
      return (JavaScriptJob)scheduledJobsQ_.peek();
    }
    
    for (JavaScriptJob job : scheduledJobsQ_) {
      if (filter.passes(job)) {
        return job;
      }
    }
    return null;
  }
  



  public boolean runSingleJob(JavaScriptJob givenJob)
  {
    assert (givenJob != null);
    JavaScriptJob job = getEarliestJob();
    if (job != givenJob) {
      return false;
    }
    
    long currentTime = System.currentTimeMillis();
    if (job.getTargetExecutionTime() > currentTime) {
      return false;
    }
    synchronized (this) {
      if (scheduledJobsQ_.remove(job)) {
        currentlyRunningJob_ = job;
      }
    }
    

    boolean debug = LOG.isDebugEnabled();
    boolean isPeriodicJob = job.isPeriodic();
    if (isPeriodicJob) {
      long jobPeriod = job.getPeriod().longValue();
      

      long timeDifference = currentTime - job.getTargetExecutionTime();
      timeDifference = timeDifference / jobPeriod * jobPeriod + jobPeriod;
      job.setTargetExecutionTime(job.getTargetExecutionTime() + timeDifference);
      

      synchronized (this) {
        if (!cancelledJobs_.contains(job.getId())) {
          if (debug) {
            LOG.debug("Reschedulling job " + job);
          }
          scheduledJobsQ_.add(job);
          notify();
        }
      }
    }
    if (debug) {
      String periodicJob = isPeriodicJob ? "interval " : "";
      LOG.debug("Starting " + periodicJob + "job " + job);
    }
    try {
      job.run();
    }
    catch (RuntimeException e) {
      LOG.error("Job run failed with unexpected RuntimeException: " + e.getMessage(), e);
      

      synchronized (this) {
        if (job == currentlyRunningJob_) {
          currentlyRunningJob_ = null;
        }
        notify();
      }
    }
    finally
    {
      synchronized (this) {
        if (job == currentlyRunningJob_) {
          currentlyRunningJob_ = null;
        }
        notify();
      }
    }
    if (debug) {
      String periodicJob = isPeriodicJob ? "interval " : "";
      LOG.debug("Finished " + periodicJob + "job " + job);
    }
    return true;
  }
  




  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    

    scheduledJobsQ_ = new PriorityQueue();
    cancelledJobs_ = new ArrayList();
    currentlyRunningJob_ = null;
  }
}
