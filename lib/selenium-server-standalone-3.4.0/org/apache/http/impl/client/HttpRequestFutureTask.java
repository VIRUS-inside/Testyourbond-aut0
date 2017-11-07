package org.apache.http.impl.client;

import java.util.concurrent.FutureTask;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;

































public class HttpRequestFutureTask<V>
  extends FutureTask<V>
{
  private final HttpUriRequest request;
  private final HttpRequestTaskCallable<V> callable;
  
  public HttpRequestFutureTask(HttpUriRequest request, HttpRequestTaskCallable<V> httpCallable)
  {
    super(httpCallable);
    this.request = request;
    callable = httpCallable;
  }
  




  public boolean cancel(boolean mayInterruptIfRunning)
  {
    callable.cancel();
    if (mayInterruptIfRunning) {
      request.abort();
    }
    return super.cancel(mayInterruptIfRunning);
  }
  


  public long scheduledTime()
  {
    return callable.getScheduled();
  }
  


  public long startedTime()
  {
    return callable.getStarted();
  }
  


  public long endedTime()
  {
    if (isDone()) {
      return callable.getEnded();
    }
    throw new IllegalStateException("Task is not done yet");
  }
  




  public long requestDuration()
  {
    if (isDone()) {
      return endedTime() - startedTime();
    }
    throw new IllegalStateException("Task is not done yet");
  }
  



  public long taskDuration()
  {
    if (isDone()) {
      return endedTime() - scheduledTime();
    }
    throw new IllegalStateException("Task is not done yet");
  }
  

  public String toString()
  {
    return request.getRequestLine().getUri();
  }
}
