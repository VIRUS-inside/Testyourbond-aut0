package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

























public class DefaultJavaScriptExecutor
  implements JavaScriptExecutor
{
  private final transient WeakReference<WebClient> webClient_;
  private transient List<WeakReference<JavaScriptJobManager>> jobManagerList_ = new LinkedList();
  
  private volatile boolean shutdown_ = false;
  
  private transient Thread eventLoopThread_ = null;
  

  private static final Log LOG = LogFactory.getLog(DefaultJavaScriptExecutor.class);
  



  public DefaultJavaScriptExecutor(WebClient webClient)
  {
    webClient_ = new WeakReference(webClient);
  }
  


  protected void startThreadIfNeeded()
  {
    if (eventLoopThread_ == null) {
      eventLoopThread_ = new Thread(this, getThreadName());
      eventLoopThread_.setDaemon(true);
      eventLoopThread_.start();
    }
  }
  



  protected String getThreadName()
  {
    return "JS executor for " + webClient_.get();
  }
  
  private void killThread()
  {
    if (eventLoopThread_ == null) {
      return;
    }
    try {
      eventLoopThread_.interrupt();
      eventLoopThread_.join(10000L);
    }
    catch (InterruptedException e) {
      LOG.warn("InterruptedException while waiting for the eventLoop thread to join ", e);
    }
    
    if (eventLoopThread_.isAlive()) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Event loop thread " + 
          eventLoopThread_.getName() + 
          " still alive at " + 
          System.currentTimeMillis());
        LOG.warn("Event loop thread will be stopped");
      }
      

      eventLoopThread_.stop();
    }
  }
  



  protected JavaScriptJobManager getJobManagerWithEarliestJob()
  {
    JavaScriptJobManager javaScriptJobManager = null;
    JavaScriptJob earliestJob = null;
    
    for (WeakReference<JavaScriptJobManager> weakReference : jobManagerList_) {
      JavaScriptJobManager jobManager = (JavaScriptJobManager)weakReference.get();
      if (jobManager != null) {
        JavaScriptJob newJob = jobManager.getEarliestJob();
        if ((newJob != null) && ((earliestJob == null) || (earliestJob.compareTo(newJob) > 0))) {
          earliestJob = newJob;
          javaScriptJobManager = jobManager;
        }
      }
    }
    return javaScriptJobManager;
  }
  






  public int pumpEventLoop(long timeoutMillis)
  {
    return 0;
  }
  

  public void run()
  {
    boolean trace = LOG.isTraceEnabled();
    

    long sleepInterval = 10L;
    while ((!shutdown_) && (!Thread.currentThread().isInterrupted()) && (webClient_.get() != null)) {
      JavaScriptJobManager jobManager = getJobManagerWithEarliestJob();
      
      if (jobManager != null) {
        JavaScriptJob earliestJob = jobManager.getEarliestJob();
        if (earliestJob != null) {
          long waitTime = earliestJob.getTargetExecutionTime() - System.currentTimeMillis();
          

          if (waitTime < 1L)
          {
            if (trace) {
              LOG.trace("started executing job at " + System.currentTimeMillis());
            }
            jobManager.runSingleJob(earliestJob);
            if (!trace) continue;
            LOG.trace("stopped executing job at " + System.currentTimeMillis());
            


            continue;
          }
        }
      }
      

      if ((shutdown_) || (Thread.currentThread().isInterrupted()) || (webClient_.get() == null)) {
        break;
      }
      
      try
      {
        Thread.sleep(10L);
      }
      catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
  




  public void addWindow(WebWindow newWindow)
  {
    JavaScriptJobManager jobManager = newWindow.getJobManager();
    if (jobManager != null) {
      updateJobMangerList(jobManager);
      startThreadIfNeeded();
    }
  }
  
  private synchronized void updateJobMangerList(JavaScriptJobManager newJobManager) { JavaScriptJobManager manager;
    for (WeakReference<JavaScriptJobManager> weakReference : jobManagerList_) {
      manager = (JavaScriptJobManager)weakReference.get();
      if (newJobManager == manager) {
        return;
      }
    }
    
    List<WeakReference<JavaScriptJobManager>> managers = new LinkedList();
    for (Object weakReference : jobManagerList_) {
      JavaScriptJobManager manager = (JavaScriptJobManager)((WeakReference)weakReference).get();
      if (manager != null) {
        managers.add(weakReference);
      }
    }
    managers.add(new WeakReference(newJobManager));
    jobManagerList_ = managers;
  }
  

  public void shutdown()
  {
    shutdown_ = true;
    killThread();
    
    webClient_.clear();
    jobManagerList_.clear();
  }
}
