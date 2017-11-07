package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;


































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class DefaultHttpRequestRetryHandler
  implements HttpRequestRetryHandler
{
  public static final DefaultHttpRequestRetryHandler INSTANCE = new DefaultHttpRequestRetryHandler();
  



  private final int retryCount;
  



  private final boolean requestSentRetryEnabled;
  



  private final Set<Class<? extends IOException>> nonRetriableClasses;
  




  protected DefaultHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled, Collection<Class<? extends IOException>> clazzes)
  {
    this.retryCount = retryCount;
    this.requestSentRetryEnabled = requestSentRetryEnabled;
    nonRetriableClasses = new HashSet();
    for (Class<? extends IOException> clazz : clazzes) {
      nonRetriableClasses.add(clazz);
    }
  }
  












  public DefaultHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled)
  {
    this(retryCount, requestSentRetryEnabled, Arrays.asList(new Class[] { InterruptedIOException.class, UnknownHostException.class, ConnectException.class, SSLException.class }));
  }
  













  public DefaultHttpRequestRetryHandler()
  {
    this(3, false);
  }
  






  public boolean retryRequest(IOException exception, int executionCount, HttpContext context)
  {
    Args.notNull(exception, "Exception parameter");
    Args.notNull(context, "HTTP context");
    if (executionCount > retryCount)
    {
      return false;
    }
    if (nonRetriableClasses.contains(exception.getClass())) {
      return false;
    }
    for (Class<? extends IOException> rejectException : nonRetriableClasses) {
      if (rejectException.isInstance(exception)) {
        return false;
      }
    }
    
    HttpClientContext clientContext = HttpClientContext.adapt(context);
    HttpRequest request = clientContext.getRequest();
    
    if (requestIsAborted(request)) {
      return false;
    }
    
    if (handleAsIdempotent(request))
    {
      return true;
    }
    
    if ((!clientContext.isRequestSent()) || (requestSentRetryEnabled))
    {

      return true;
    }
    
    return false;
  }
  



  public boolean isRequestSentRetryEnabled()
  {
    return requestSentRetryEnabled;
  }
  


  public int getRetryCount()
  {
    return retryCount;
  }
  


  protected boolean handleAsIdempotent(HttpRequest request)
  {
    return !(request instanceof HttpEntityEnclosingRequest);
  }
  




  @Deprecated
  protected boolean requestIsAborted(HttpRequest request)
  {
    HttpRequest req = request;
    if ((request instanceof RequestWrapper)) {
      req = ((RequestWrapper)request).getOriginal();
    }
    return ((req instanceof HttpUriRequest)) && (((HttpUriRequest)req).isAborted());
  }
}
