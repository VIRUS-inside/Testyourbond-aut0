package org.junit.runner.notification;

import org.junit.runner.Description;
import org.junit.runner.Result;














@RunListener.ThreadSafe
final class SynchronizedRunListener
  extends RunListener
{
  private final RunListener listener;
  private final Object monitor;
  
  SynchronizedRunListener(RunListener listener, Object monitor)
  {
    this.listener = listener;
    this.monitor = monitor;
  }
  
  public void testRunStarted(Description description) throws Exception
  {
    synchronized (monitor) {
      listener.testRunStarted(description);
    }
  }
  
  public void testRunFinished(Result result) throws Exception
  {
    synchronized (monitor) {
      listener.testRunFinished(result);
    }
  }
  
  public void testStarted(Description description) throws Exception
  {
    synchronized (monitor) {
      listener.testStarted(description);
    }
  }
  
  public void testFinished(Description description) throws Exception
  {
    synchronized (monitor) {
      listener.testFinished(description);
    }
  }
  
  public void testFailure(Failure failure) throws Exception
  {
    synchronized (monitor) {
      listener.testFailure(failure);
    }
  }
  
  public void testAssumptionFailure(Failure failure)
  {
    synchronized (monitor) {
      listener.testAssumptionFailure(failure);
    }
  }
  
  public void testIgnored(Description description) throws Exception
  {
    synchronized (monitor) {
      listener.testIgnored(description);
    }
  }
  
  public int hashCode()
  {
    return listener.hashCode();
  }
  
  public boolean equals(Object other)
  {
    if (this == other) {
      return true;
    }
    if (!(other instanceof SynchronizedRunListener)) {
      return false;
    }
    SynchronizedRunListener that = (SynchronizedRunListener)other;
    
    return listener.equals(listener);
  }
  
  public String toString()
  {
    return listener.toString() + " (with synchronization wrapper)";
  }
}
