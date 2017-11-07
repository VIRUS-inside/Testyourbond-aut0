package org.apache.http.impl.client;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;


























class HttpRequestTaskCallable<V>
  implements Callable<V>
{
  private final HttpUriRequest request;
  private final HttpClient httpclient;
  private final AtomicBoolean cancelled = new AtomicBoolean(false);
  
  private final long scheduled = System.currentTimeMillis();
  private long started = -1L;
  private long ended = -1L;
  

  private final HttpContext context;
  

  private final ResponseHandler<V> responseHandler;
  
  private final FutureCallback<V> callback;
  
  private final FutureRequestExecutionMetrics metrics;
  

  HttpRequestTaskCallable(HttpClient httpClient, HttpUriRequest request, HttpContext context, ResponseHandler<V> responseHandler, FutureCallback<V> callback, FutureRequestExecutionMetrics metrics)
  {
    httpclient = httpClient;
    this.responseHandler = responseHandler;
    this.request = request;
    this.context = context;
    this.callback = callback;
    this.metrics = metrics;
  }
  
  public long getScheduled() {
    return scheduled;
  }
  
  public long getStarted() {
    return started;
  }
  
  public long getEnded() {
    return ended;
  }
  
  public V call() throws Exception
  {
    if (!cancelled.get()) {
      try {
        metrics.getActiveConnections().incrementAndGet();
        started = System.currentTimeMillis();
        try {
          metrics.getScheduledConnections().decrementAndGet();
          V result = httpclient.execute(request, responseHandler, context);
          ended = System.currentTimeMillis();
          metrics.getSuccessfulConnections().increment(started);
          if (callback != null) {
            callback.completed(result);
          }
          return result;
        } catch (Exception e) {
          metrics.getFailedConnections().increment(started);
          ended = System.currentTimeMillis();
          if (callback != null) {
            callback.failed(e);
          }
          throw e;
        }
        





        throw new IllegalStateException("call has been cancelled for request " + request.getURI());



















































      }
      finally
      {


















































        metrics.getRequests().increment(started);
        metrics.getTasks().increment(started);
        metrics.getActiveConnections().decrementAndGet();
      }
    }
  }
  

  public void cancel()
  {
    cancelled.set(true);
    if (callback != null) {
      callback.cancelled();
    }
  }
}
