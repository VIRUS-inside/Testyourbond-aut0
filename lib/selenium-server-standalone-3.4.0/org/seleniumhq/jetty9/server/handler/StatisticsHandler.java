package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.AsyncContextEvent;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannelState;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.util.FutureCallback;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.component.Graceful;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.statistic.CounterStatistic;
import org.seleniumhq.jetty9.util.statistic.SampleStatistic;



















@ManagedObject("Request Statistics Gathering")
public class StatisticsHandler
  extends HandlerWrapper
  implements Graceful
{
  private static final Logger LOG = Log.getLogger(StatisticsHandler.class);
  private final AtomicLong _statsStartedAt = new AtomicLong();
  
  private final CounterStatistic _requestStats = new CounterStatistic();
  private final SampleStatistic _requestTimeStats = new SampleStatistic();
  private final CounterStatistic _dispatchedStats = new CounterStatistic();
  private final SampleStatistic _dispatchedTimeStats = new SampleStatistic();
  private final CounterStatistic _asyncWaitStats = new CounterStatistic();
  
  private final LongAdder _asyncDispatches = new LongAdder();
  private final LongAdder _expires = new LongAdder();
  
  private final LongAdder _responses1xx = new LongAdder();
  private final LongAdder _responses2xx = new LongAdder();
  private final LongAdder _responses3xx = new LongAdder();
  private final LongAdder _responses4xx = new LongAdder();
  private final LongAdder _responses5xx = new LongAdder();
  private final LongAdder _responsesTotalBytes = new LongAdder();
  
  private final AtomicReference<FutureCallback> _shutdown = new AtomicReference();
  
  private final AtomicBoolean _wrapWarning = new AtomicBoolean();
  
  private final AsyncListener _onCompletion = new AsyncListener()
  {
    public void onTimeout(AsyncEvent event)
      throws IOException
    {
      _expires.increment();
    }
    
    public void onStartAsync(AsyncEvent event)
      throws IOException
    {
      event.getAsyncContext().addListener(this);
    }
    

    public void onError(AsyncEvent event)
      throws IOException
    {}
    
    public void onComplete(AsyncEvent event)
      throws IOException
    {
      HttpChannelState state = ((AsyncContextEvent)event).getHttpChannelState();
      
      Request request = state.getBaseRequest();
      long elapsed = System.currentTimeMillis() - request.getTimeStamp();
      
      long d = _requestStats.decrement();
      _requestTimeStats.set(elapsed);
      
      updateResponse(request);
      
      _asyncWaitStats.decrement();
      

      if (d == 0L)
      {
        FutureCallback shutdown = (FutureCallback)_shutdown.get();
        if (shutdown != null) {
          shutdown.succeeded();
        }
      }
    }
  };
  
  public StatisticsHandler() {}
  
  @ManagedOperation(value="resets statistics", impact="ACTION")
  public void statsReset()
  {
    _statsStartedAt.set(System.currentTimeMillis());
    
    _requestStats.reset();
    _requestTimeStats.reset();
    _dispatchedStats.reset();
    _dispatchedTimeStats.reset();
    _asyncWaitStats.reset();
    
    _asyncDispatches.reset();
    _expires.reset();
    _responses1xx.reset();
    _responses2xx.reset();
    _responses3xx.reset();
    _responses4xx.reset();
    _responses5xx.reset();
    _responsesTotalBytes.reset();
  }
  
  public void handle(String path, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    _dispatchedStats.increment();
    

    HttpChannelState state = baseRequest.getHttpChannelState();
    long start; long start; if (state.isInitial())
    {

      _requestStats.increment();
      start = baseRequest.getTimeStamp();

    }
    else
    {
      start = System.currentTimeMillis();
      _asyncDispatches.increment();
    }
    
    try
    {
      Handler handler = getHandler();
      if ((handler != null) && (_shutdown.get() == null) && (isStarted())) {
        handler.handle(path, baseRequest, request, response);
      } else if (baseRequest.isHandled())
      {
        if (_wrapWarning.compareAndSet(false, true)) {
          LOG.warn("Bad statistics configuration. Latencies will be incorrect in {}", new Object[] { this });
        }
      }
      else {
        baseRequest.setHandled(true);
        response.sendError(503);
      } } finally { long now;
      long dispatched;
      long d;
      FutureCallback shutdown;
      long now = System.currentTimeMillis();
      long dispatched = now - start;
      
      _dispatchedStats.decrement();
      _dispatchedTimeStats.set(dispatched);
      
      if (state.isSuspended())
      {
        if (state.isInitial())
        {
          state.addListener(_onCompletion);
          _asyncWaitStats.increment();
        }
      }
      else if (state.isInitial())
      {
        long d = _requestStats.decrement();
        _requestTimeStats.set(dispatched);
        updateResponse(baseRequest);
        

        FutureCallback shutdown = (FutureCallback)_shutdown.get();
        if (shutdown != null)
        {
          response.flushBuffer();
          if (d == 0L) {
            shutdown.succeeded();
          }
        }
      }
    }
  }
  
  protected void updateResponse(Request request)
  {
    Response response = request.getResponse();
    if (request.isHandled())
    {
      switch (response.getStatus() / 100)
      {
      case 1: 
        _responses1xx.increment();
        break;
      case 2: 
        _responses2xx.increment();
        break;
      case 3: 
        _responses3xx.increment();
        break;
      case 4: 
        _responses4xx.increment();
        break;
      case 5: 
        _responses5xx.increment();
        break;
      


      }
      
    } else
      _responses4xx.increment();
    _responsesTotalBytes.add(response.getContentCount());
  }
  
  protected void doStart()
    throws Exception
  {
    _shutdown.set(null);
    super.doStart();
    statsReset();
  }
  

  protected void doStop()
    throws Exception
  {
    super.doStop();
    FutureCallback shutdown = (FutureCallback)_shutdown.get();
    if ((shutdown != null) && (!shutdown.isDone())) {
      shutdown.failed(new TimeoutException());
    }
  }
  





  @ManagedAttribute("number of requests")
  public int getRequests()
  {
    return (int)_requestStats.getTotal();
  }
  




  @ManagedAttribute("number of requests currently active")
  public int getRequestsActive()
  {
    return (int)_requestStats.getCurrent();
  }
  




  @ManagedAttribute("maximum number of active requests")
  public int getRequestsActiveMax()
  {
    return (int)_requestStats.getMax();
  }
  




  @ManagedAttribute("maximum time spend handling requests (in ms)")
  public long getRequestTimeMax()
  {
    return _requestTimeStats.getMax();
  }
  




  @ManagedAttribute("total time spend in all request handling (in ms)")
  public long getRequestTimeTotal()
  {
    return _requestTimeStats.getTotal();
  }
  






  @ManagedAttribute("mean time spent handling requests (in ms)")
  public double getRequestTimeMean()
  {
    return _requestTimeStats.getMean();
  }
  






  @ManagedAttribute("standard deviation for request handling (in ms)")
  public double getRequestTimeStdDev()
  {
    return _requestTimeStats.getStdDev();
  }
  





  @ManagedAttribute("number of dispatches")
  public int getDispatched()
  {
    return (int)_dispatchedStats.getTotal();
  }
  





  @ManagedAttribute("number of dispatches currently active")
  public int getDispatchedActive()
  {
    return (int)_dispatchedStats.getCurrent();
  }
  





  @ManagedAttribute("maximum number of active dispatches being handled")
  public int getDispatchedActiveMax()
  {
    return (int)_dispatchedStats.getMax();
  }
  




  @ManagedAttribute("maximum time spend in dispatch handling")
  public long getDispatchedTimeMax()
  {
    return _dispatchedTimeStats.getMax();
  }
  




  @ManagedAttribute("total time spent in dispatch handling (in ms)")
  public long getDispatchedTimeTotal()
  {
    return _dispatchedTimeStats.getTotal();
  }
  






  @ManagedAttribute("mean time spent in dispatch handling (in ms)")
  public double getDispatchedTimeMean()
  {
    return _dispatchedTimeStats.getMean();
  }
  






  @ManagedAttribute("standard deviation for dispatch handling (in ms)")
  public double getDispatchedTimeStdDev()
  {
    return _dispatchedTimeStats.getStdDev();
  }
  






  @ManagedAttribute("total number of async requests")
  public int getAsyncRequests()
  {
    return (int)_asyncWaitStats.getTotal();
  }
  




  @ManagedAttribute("currently waiting async requests")
  public int getAsyncRequestsWaiting()
  {
    return (int)_asyncWaitStats.getCurrent();
  }
  




  @ManagedAttribute("maximum number of waiting async requests")
  public int getAsyncRequestsWaitingMax()
  {
    return (int)_asyncWaitStats.getMax();
  }
  



  @ManagedAttribute("number of requested that have been asynchronously dispatched")
  public int getAsyncDispatches()
  {
    return _asyncDispatches.intValue();
  }
  




  @ManagedAttribute("number of async requests requests that have expired")
  public int getExpires()
  {
    return _expires.intValue();
  }
  




  @ManagedAttribute("number of requests with 1xx response status")
  public int getResponses1xx()
  {
    return _responses1xx.intValue();
  }
  




  @ManagedAttribute("number of requests with 2xx response status")
  public int getResponses2xx()
  {
    return _responses2xx.intValue();
  }
  




  @ManagedAttribute("number of requests with 3xx response status")
  public int getResponses3xx()
  {
    return _responses3xx.intValue();
  }
  




  @ManagedAttribute("number of requests with 4xx response status")
  public int getResponses4xx()
  {
    return _responses4xx.intValue();
  }
  




  @ManagedAttribute("number of requests with 5xx response status")
  public int getResponses5xx()
  {
    return _responses5xx.intValue();
  }
  



  @ManagedAttribute("time in milliseconds stats have been collected for")
  public long getStatsOnMs()
  {
    return System.currentTimeMillis() - _statsStartedAt.get();
  }
  



  @ManagedAttribute("total number of bytes across all responses")
  public long getResponsesBytesTotal()
  {
    return _responsesTotalBytes.longValue();
  }
  
  public String toStatsHTML()
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append("<h1>Statistics:</h1>\n");
    sb.append("Statistics gathering started ").append(getStatsOnMs()).append("ms ago").append("<br />\n");
    
    sb.append("<h2>Requests:</h2>\n");
    sb.append("Total requests: ").append(getRequests()).append("<br />\n");
    sb.append("Active requests: ").append(getRequestsActive()).append("<br />\n");
    sb.append("Max active requests: ").append(getRequestsActiveMax()).append("<br />\n");
    sb.append("Total requests time: ").append(getRequestTimeTotal()).append("<br />\n");
    sb.append("Mean request time: ").append(getRequestTimeMean()).append("<br />\n");
    sb.append("Max request time: ").append(getRequestTimeMax()).append("<br />\n");
    sb.append("Request time standard deviation: ").append(getRequestTimeStdDev()).append("<br />\n");
    

    sb.append("<h2>Dispatches:</h2>\n");
    sb.append("Total dispatched: ").append(getDispatched()).append("<br />\n");
    sb.append("Active dispatched: ").append(getDispatchedActive()).append("<br />\n");
    sb.append("Max active dispatched: ").append(getDispatchedActiveMax()).append("<br />\n");
    sb.append("Total dispatched time: ").append(getDispatchedTimeTotal()).append("<br />\n");
    sb.append("Mean dispatched time: ").append(getDispatchedTimeMean()).append("<br />\n");
    sb.append("Max dispatched time: ").append(getDispatchedTimeMax()).append("<br />\n");
    sb.append("Dispatched time standard deviation: ").append(getDispatchedTimeStdDev()).append("<br />\n");
    

    sb.append("Total requests suspended: ").append(getAsyncRequests()).append("<br />\n");
    sb.append("Total requests expired: ").append(getExpires()).append("<br />\n");
    sb.append("Total requests resumed: ").append(getAsyncDispatches()).append("<br />\n");
    
    sb.append("<h2>Responses:</h2>\n");
    sb.append("1xx responses: ").append(getResponses1xx()).append("<br />\n");
    sb.append("2xx responses: ").append(getResponses2xx()).append("<br />\n");
    sb.append("3xx responses: ").append(getResponses3xx()).append("<br />\n");
    sb.append("4xx responses: ").append(getResponses4xx()).append("<br />\n");
    sb.append("5xx responses: ").append(getResponses5xx()).append("<br />\n");
    sb.append("Bytes sent total: ").append(getResponsesBytesTotal()).append("<br />\n");
    
    return sb.toString();
  }
  


  public Future<Void> shutdown()
  {
    FutureCallback shutdown = new FutureCallback(false);
    _shutdown.compareAndSet(null, shutdown);
    shutdown = (FutureCallback)_shutdown.get();
    if (_dispatchedStats.getCurrent() == 0L)
      shutdown.succeeded();
    return shutdown;
  }
}
