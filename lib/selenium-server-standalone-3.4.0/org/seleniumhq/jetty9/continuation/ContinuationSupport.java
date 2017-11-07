package org.seleniumhq.jetty9.continuation;

import java.lang.reflect.Constructor;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;

























public class ContinuationSupport
{
  static final boolean __servlet3;
  static final Class<?> __waitingContinuation;
  static final Constructor<? extends Continuation> __newServlet3Continuation;
  
  static
  {
    boolean servlet3Support = false;
    Constructor<? extends Continuation> s3cc = null;
    try
    {
      boolean servlet3 = ServletRequest.class.getMethod("startAsync", new Class[0]) != null;
      if (servlet3)
      {
        Class<? extends Continuation> s3c = ContinuationSupport.class.getClassLoader().loadClass("org.seleniumhq.jetty9.continuation.Servlet3Continuation").asSubclass(Continuation.class);
        s3cc = s3c.getConstructor(new Class[] { ServletRequest.class });
        servlet3Support = true;
      }
      

    }
    catch (Exception localException) {}finally
    {
      __servlet3 = servlet3Support;
      __newServlet3Continuation = s3cc;
    }
    
    Class<?> waiting = null;
    try
    {
      waiting = ContinuationSupport.class.getClassLoader().loadClass("org.mortbay.util.ajax.WaitingContinuation");


    }
    catch (Exception localException1) {}finally
    {

      __waitingContinuation = waiting;
    }
  }
  











  public static Continuation getContinuation(ServletRequest request)
  {
    Continuation continuation = (Continuation)request.getAttribute("org.seleniumhq.jetty9.continuation");
    if (continuation != null) {
      return continuation;
    }
    while ((request instanceof ServletRequestWrapper)) {
      request = ((ServletRequestWrapper)request).getRequest();
    }
    if (__servlet3)
    {
      try
      {
        continuation = (Continuation)__newServlet3Continuation.newInstance(new Object[] { request });
        request.setAttribute("org.seleniumhq.jetty9.continuation", continuation);
        return continuation;
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
    
    throw new IllegalStateException("!(Jetty || Servlet 3.0 || ContinuationFilter)");
  }
  







  @Deprecated
  public static Continuation getContinuation(ServletRequest request, ServletResponse response)
  {
    return getContinuation(request);
  }
  
  public ContinuationSupport() {}
}
