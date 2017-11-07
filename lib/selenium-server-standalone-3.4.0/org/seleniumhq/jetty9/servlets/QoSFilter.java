package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





















































@ManagedObject("Quality of Service Filter")
public class QoSFilter
  implements Filter
{
  private static final Logger LOG = Log.getLogger(QoSFilter.class);
  
  static final int __DEFAULT_MAX_PRIORITY = 10;
  
  static final int __DEFAULT_PASSES = 10;
  
  static final int __DEFAULT_WAIT_MS = 50;
  static final long __DEFAULT_TIMEOUT_MS = -1L;
  static final String MANAGED_ATTR_INIT_PARAM = "managedAttr";
  static final String MAX_REQUESTS_INIT_PARAM = "maxRequests";
  static final String MAX_PRIORITY_INIT_PARAM = "maxPriority";
  static final String MAX_WAIT_INIT_PARAM = "waitMs";
  static final String SUSPEND_INIT_PARAM = "suspendMs";
  private final String _suspended = "QoSFilter@" + Integer.toHexString(hashCode()) + ".SUSPENDED";
  private final String _resumed = "QoSFilter@" + Integer.toHexString(hashCode()) + ".RESUMED";
  private long _waitMs;
  private long _suspendMs;
  private int _maxRequests;
  private Semaphore _passes;
  private Queue<AsyncContext>[] _queues;
  private AsyncListener[] _listeners;
  
  public QoSFilter() {}
  
  public void init(FilterConfig filterConfig) { int max_priority = 10;
    if (filterConfig.getInitParameter("maxPriority") != null)
      max_priority = Integer.parseInt(filterConfig.getInitParameter("maxPriority"));
    _queues = new Queue[max_priority + 1];
    _listeners = new AsyncListener[_queues.length];
    for (int p = 0; p < _queues.length; p++)
    {
      _queues[p] = new ConcurrentLinkedQueue();
      _listeners[p] = new QoSAsyncListener(p);
    }
    
    int maxRequests = 10;
    if (filterConfig.getInitParameter("maxRequests") != null)
      maxRequests = Integer.parseInt(filterConfig.getInitParameter("maxRequests"));
    _passes = new Semaphore(maxRequests, true);
    _maxRequests = maxRequests;
    
    long wait = 50L;
    if (filterConfig.getInitParameter("waitMs") != null)
      wait = Integer.parseInt(filterConfig.getInitParameter("waitMs"));
    _waitMs = wait;
    
    long suspend = -1L;
    if (filterConfig.getInitParameter("suspendMs") != null)
      suspend = Integer.parseInt(filterConfig.getInitParameter("suspendMs"));
    _suspendMs = suspend;
    
    ServletContext context = filterConfig.getServletContext();
    if ((context != null) && (Boolean.parseBoolean(filterConfig.getInitParameter("managedAttr")))) {
      context.setAttribute(filterConfig.getFilterName(), this);
    }
  }
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    boolean accepted = false;
    try
    {
      Boolean suspended = (Boolean)request.getAttribute(_suspended);
      int p; AsyncContext asyncContext; ServletRequest candidate; Boolean suspended; if (suspended == null)
      {
        accepted = _passes.tryAcquire(getWaitMs(), TimeUnit.MILLISECONDS);
        if (accepted)
        {
          request.setAttribute(_suspended, Boolean.FALSE);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Accepted {}", new Object[] { request });
          }
        }
        else {
          request.setAttribute(_suspended, Boolean.TRUE);
          int priority = getPriority(request);
          AsyncContext asyncContext = request.startAsync();
          long suspendMs = getSuspendMs();
          if (suspendMs > 0L)
            asyncContext.setTimeout(suspendMs);
          asyncContext.addListener(_listeners[priority]);
          _queues[priority].add(asyncContext);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Suspended {}", new Object[] { request });
          }
          
        }
        
      }
      else if (suspended.booleanValue())
      {
        request.setAttribute(_suspended, Boolean.FALSE);
        Boolean resumed = (Boolean)request.getAttribute(_resumed);
        if (resumed == Boolean.TRUE)
        {
          _passes.acquire();
          accepted = true;
          if (LOG.isDebugEnabled()) {
            LOG.debug("Resumed {}", new Object[] { request });
          }
        }
        else
        {
          accepted = _passes.tryAcquire(getWaitMs(), TimeUnit.MILLISECONDS);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Timeout {}", new Object[] { request });
          }
        }
      }
      else
      {
        _passes.acquire();
        accepted = true;
        if (LOG.isDebugEnabled()) {
          LOG.debug("Passthrough {}", new Object[] { request });
        }
      }
      
      if (accepted)
      {
        chain.doFilter(request, response);
      }
      else
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Rejected {}", new Object[] { request });
        ((HttpServletResponse)response).sendError(503);
      } } catch (InterruptedException e) { int p;
      AsyncContext asyncContext;
      ServletRequest candidate;
      Boolean suspended;
      ((HttpServletResponse)response).sendError(503); } finally { int p;
      AsyncContext asyncContext;
      ServletRequest candidate;
      Boolean suspended;
      if (accepted)
      {
        for (int p = _queues.length - 1; p >= 0; p--)
        {
          AsyncContext asyncContext = (AsyncContext)_queues[p].poll();
          if (asyncContext != null)
          {
            ServletRequest candidate = asyncContext.getRequest();
            Boolean suspended = (Boolean)candidate.getAttribute(_suspended);
            if (suspended == Boolean.TRUE)
            {
              candidate.setAttribute(_resumed, Boolean.TRUE);
              asyncContext.dispatch();
              break;
            }
          }
        }
        _passes.release();
      }
    }
  }
  














  protected int getPriority(ServletRequest request)
  {
    HttpServletRequest baseRequest = (HttpServletRequest)request;
    if (baseRequest.getUserPrincipal() != null)
    {
      return 2;
    }
    

    HttpSession session = baseRequest.getSession(false);
    if ((session != null) && (!session.isNew())) {
      return 1;
    }
    return 0;
  }
  





  public void destroy() {}
  




  @ManagedAttribute("(short) amount of time filter will wait before suspending request (in ms)")
  public long getWaitMs()
  {
    return _waitMs;
  }
  






  public void setWaitMs(long value)
  {
    _waitMs = value;
  }
  






  @ManagedAttribute("amount of time filter will suspend a request for while waiting for the semaphore to become available (in ms)")
  public long getSuspendMs()
  {
    return _suspendMs;
  }
  






  public void setSuspendMs(long value)
  {
    _suspendMs = value;
  }
  






  @ManagedAttribute("maximum number of requests to allow processing of at the same time")
  public int getMaxRequests()
  {
    return _maxRequests;
  }
  






  public void setMaxRequests(int value)
  {
    _passes = new Semaphore(value - getMaxRequests() + _passes.availablePermits(), true);
    _maxRequests = value;
  }
  
  private class QoSAsyncListener implements AsyncListener
  {
    private final int priority;
    
    public QoSAsyncListener(int priority)
    {
      this.priority = priority;
    }
    


    public void onStartAsync(AsyncEvent event)
      throws IOException
    {}
    

    public void onComplete(AsyncEvent event)
      throws IOException
    {}
    

    public void onTimeout(AsyncEvent event)
      throws IOException
    {
      AsyncContext asyncContext = event.getAsyncContext();
      _queues[priority].remove(asyncContext);
      asyncContext.dispatch();
    }
    
    public void onError(AsyncEvent event)
      throws IOException
    {}
  }
}
