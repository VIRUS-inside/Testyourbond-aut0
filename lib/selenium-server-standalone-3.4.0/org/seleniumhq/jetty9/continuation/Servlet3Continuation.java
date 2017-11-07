package org.seleniumhq.jetty9.continuation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.DispatcherType;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;




























public class Servlet3Continuation
  implements Continuation, AsyncListener
{
  private static final ContinuationThrowable __exception = new ContinuationThrowable();
  
  private final ServletRequest _request;
  private ServletResponse _response;
  private AsyncContext _context;
  private final List<ContinuationListener> _listeners = new ArrayList();
  private volatile boolean _initial = true;
  private volatile boolean _resumed = false;
  private volatile boolean _expired = false;
  private volatile boolean _responseWrapped = false;
  
  private long _timeoutMs = -1L;
  

  public Servlet3Continuation(ServletRequest request)
  {
    _request = request;
  }
  


  public void addContinuationListener(ContinuationListener listener)
  {
    _listeners.add(listener);
  }
  


  public void complete()
  {
    AsyncContext context = _context;
    if (context == null)
      throw new IllegalStateException();
    _context.complete();
  }
  


  public ServletResponse getServletResponse()
  {
    return _response;
  }
  


  public boolean isExpired()
  {
    return _expired;
  }
  


  public boolean isInitial()
  {
    return (_initial) && (_request.getDispatcherType() != DispatcherType.ASYNC);
  }
  


  public boolean isResumed()
  {
    return _resumed;
  }
  


  public boolean isSuspended()
  {
    if (_request.isAsyncStarted()) {
      return true;
    }
    try {
      return _request.getAsyncContext() != null;
    }
    catch (IllegalStateException localIllegalStateException) {}
    


    return false;
  }
  

  public void keepWrappers()
  {
    _responseWrapped = true;
  }
  


  public void resume()
  {
    AsyncContext context = _context;
    if (context == null)
      throw new IllegalStateException();
    _resumed = true;
    _context.dispatch();
  }
  


  public void setTimeout(long timeoutMs)
  {
    _timeoutMs = timeoutMs;
    if (_context != null) {
      _context.setTimeout(timeoutMs);
    }
  }
  

  public void suspend(ServletResponse response)
  {
    _response = response;
    _responseWrapped = (response instanceof ServletResponseWrapper);
    _resumed = false;
    _expired = false;
    _context = _request.startAsync();
    _context.setTimeout(_timeoutMs);
    _context.addListener(this);
  }
  


  public void suspend()
  {
    _resumed = false;
    _expired = false;
    _context = _request.startAsync();
    _context.setTimeout(_timeoutMs);
    _context.addListener(this);
  }
  


  public boolean isResponseWrapped()
  {
    return _responseWrapped;
  }
  





  public Object getAttribute(String name)
  {
    return _request.getAttribute(name);
  }
  





  public void removeAttribute(String name)
  {
    _request.removeAttribute(name);
  }
  





  public void setAttribute(String name, Object attribute)
  {
    _request.setAttribute(name, attribute);
  }
  





  public void undispatch()
  {
    if (isSuspended())
    {
      _initial = false;
      if (ContinuationFilter.__debug)
        throw new ContinuationThrowable();
      throw __exception;
    }
    throw new IllegalStateException("!suspended");
  }
  


  public void onComplete(AsyncEvent event)
    throws IOException
  {
    for (ContinuationListener listener : _listeners) {
      listener.onComplete(this);
    }
  }
  


  public void onError(AsyncEvent event)
    throws IOException
  {}
  

  public void onStartAsync(AsyncEvent event)
    throws IOException
  {}
  

  public void onTimeout(AsyncEvent event)
    throws IOException
  {
    _expired = true;
    for (ContinuationListener listener : _listeners)
      listener.onTimeout(this);
    if (event.getSuppliedRequest().isAsyncStarted()) {
      event.getAsyncContext().dispatch();
    }
  }
}
