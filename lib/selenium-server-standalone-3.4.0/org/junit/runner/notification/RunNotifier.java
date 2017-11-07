package org.junit.runner.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.runner.Description;
import org.junit.runner.Result;






public class RunNotifier
{
  private final List<RunListener> listeners;
  private volatile boolean pleaseStop;
  
  public RunNotifier()
  {
    listeners = new CopyOnWriteArrayList();
    pleaseStop = false;
  }
  

  public void addListener(RunListener listener)
  {
    if (listener == null) {
      throw new NullPointerException("Cannot add a null listener");
    }
    listeners.add(wrapIfNotThreadSafe(listener));
  }
  


  public void removeListener(RunListener listener)
  {
    if (listener == null) {
      throw new NullPointerException("Cannot remove a null listener");
    }
    listeners.remove(wrapIfNotThreadSafe(listener));
  }
  



  RunListener wrapIfNotThreadSafe(RunListener listener)
  {
    return listener.getClass().isAnnotationPresent(RunListener.ThreadSafe.class) ? listener : new SynchronizedRunListener(listener, this);
  }
  
  private abstract class SafeNotifier
  {
    private final List<RunListener> currentListeners;
    
    SafeNotifier()
    {
      this(listeners);
    }
    
    SafeNotifier() {
      this.currentListeners = currentListeners;
    }
    
    void run() {
      int capacity = currentListeners.size();
      ArrayList<RunListener> safeListeners = new ArrayList(capacity);
      ArrayList<Failure> failures = new ArrayList(capacity);
      for (RunListener listener : currentListeners) {
        try {
          notifyListener(listener);
          safeListeners.add(listener);
        } catch (Exception e) {
          failures.add(new Failure(Description.TEST_MECHANISM, e));
        }
      }
      RunNotifier.this.fireTestFailures(safeListeners, failures);
    }
    

    protected abstract void notifyListener(RunListener paramRunListener)
      throws Exception;
  }
  
  public void fireTestRunStarted(final Description description)
  {
    new SafeNotifier(description)
    {
      protected void notifyListener(RunListener each) throws Exception {
        each.testRunStarted(description);
      }
    }.run();
  }
  


  public void fireTestRunFinished(final Result result)
  {
    new SafeNotifier(result)
    {
      protected void notifyListener(RunListener each) throws Exception {
        each.testRunFinished(result);
      }
    }.run();
  }
  




  public void fireTestStarted(final Description description)
    throws StoppedByUserException
  {
    if (pleaseStop) {
      throw new StoppedByUserException();
    }
    new SafeNotifier(description)
    {
      protected void notifyListener(RunListener each) throws Exception {
        each.testStarted(description);
      }
    }.run();
  }
  




  public void fireTestFailure(Failure failure)
  {
    fireTestFailures(listeners, Arrays.asList(new Failure[] { failure }));
  }
  
  private void fireTestFailures(List<RunListener> listeners, final List<Failure> failures)
  {
    if (!failures.isEmpty()) {
      new SafeNotifier(listeners, failures)
      {
        protected void notifyListener(RunListener listener) throws Exception {
          for (Failure each : failures) {
            listener.testFailure(each);
          }
        }
      }.run();
    }
  }
  






  public void fireTestAssumptionFailed(final Failure failure)
  {
    new SafeNotifier(failure)
    {
      protected void notifyListener(RunListener each) throws Exception {
        each.testAssumptionFailure(failure);
      }
    }.run();
  }
  




  public void fireTestIgnored(final Description description)
  {
    new SafeNotifier(description)
    {
      protected void notifyListener(RunListener each) throws Exception {
        each.testIgnored(description);
      }
    }.run();
  }
  






  public void fireTestFinished(final Description description)
  {
    new SafeNotifier(description)
    {
      protected void notifyListener(RunListener each) throws Exception {
        each.testFinished(description);
      }
    }.run();
  }
  





  public void pleaseStop()
  {
    pleaseStop = true;
  }
  


  public void addFirstListener(RunListener listener)
  {
    if (listener == null) {
      throw new NullPointerException("Cannot add a null listener");
    }
    listeners.add(0, wrapIfNotThreadSafe(listener));
  }
}
