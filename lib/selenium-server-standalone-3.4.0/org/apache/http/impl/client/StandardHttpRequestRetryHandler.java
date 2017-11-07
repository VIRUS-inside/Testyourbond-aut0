package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;








































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class StandardHttpRequestRetryHandler
  extends DefaultHttpRequestRetryHandler
{
  private final Map<String, Boolean> idempotentMethods;
  
  public StandardHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled)
  {
    super(retryCount, requestSentRetryEnabled);
    idempotentMethods = new ConcurrentHashMap();
    idempotentMethods.put("GET", Boolean.TRUE);
    idempotentMethods.put("HEAD", Boolean.TRUE);
    idempotentMethods.put("PUT", Boolean.TRUE);
    idempotentMethods.put("DELETE", Boolean.TRUE);
    idempotentMethods.put("OPTIONS", Boolean.TRUE);
    idempotentMethods.put("TRACE", Boolean.TRUE);
  }
  


  public StandardHttpRequestRetryHandler()
  {
    this(3, false);
  }
  
  protected boolean handleAsIdempotent(HttpRequest request)
  {
    String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
    Boolean b = (Boolean)idempotentMethods.get(method);
    return (b != null) && (b.booleanValue());
  }
}
