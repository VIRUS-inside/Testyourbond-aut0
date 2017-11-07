package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;






























@Contract(threading=ThreadingBehavior.SAFE)
public class FutureRequestExecutionService
  implements Closeable
{
  private final HttpClient httpclient;
  private final ExecutorService executorService;
  private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
  private final AtomicBoolean closed = new AtomicBoolean(false);
  














  public FutureRequestExecutionService(HttpClient httpclient, ExecutorService executorService)
  {
    this.httpclient = httpclient;
    this.executorService = executorService;
  }
  













  public <T> HttpRequestFutureTask<T> execute(HttpUriRequest request, HttpContext context, ResponseHandler<T> responseHandler)
  {
    return execute(request, context, responseHandler, null);
  }
  



















  public <T> HttpRequestFutureTask<T> execute(HttpUriRequest request, HttpContext context, ResponseHandler<T> responseHandler, FutureCallback<T> callback)
  {
    if (closed.get()) {
      throw new IllegalStateException("Close has been called on this httpclient instance.");
    }
    metrics.getScheduledConnections().incrementAndGet();
    HttpRequestTaskCallable<T> callable = new HttpRequestTaskCallable(httpclient, request, context, responseHandler, callback, metrics);
    
    HttpRequestFutureTask<T> httpRequestFutureTask = new HttpRequestFutureTask(request, callable);
    
    executorService.execute(httpRequestFutureTask);
    
    return httpRequestFutureTask;
  }
  



  public FutureRequestExecutionMetrics metrics()
  {
    return metrics;
  }
  
  public void close() throws IOException
  {
    closed.set(true);
    executorService.shutdownNow();
    if ((httpclient instanceof Closeable)) {
      ((Closeable)httpclient).close();
    }
  }
}
