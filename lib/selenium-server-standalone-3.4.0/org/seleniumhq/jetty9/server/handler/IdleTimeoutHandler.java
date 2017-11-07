package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.Request;
































public class IdleTimeoutHandler
  extends HandlerWrapper
{
  private long _idleTimeoutMs = 1000L;
  private boolean _applyToAsync = false;
  
  public IdleTimeoutHandler() {}
  
  public boolean isApplyToAsync() { return _applyToAsync; }
  





  public void setApplyToAsync(boolean applyToAsync)
  {
    _applyToAsync = applyToAsync;
  }
  
  public long getIdleTimeoutMs()
  {
    return _idleTimeoutMs;
  }
  



  public void setIdleTimeoutMs(long idleTimeoutMs)
  {
    _idleTimeoutMs = idleTimeoutMs;
  }
  

  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    final HttpChannel channel = baseRequest.getHttpChannel();
    final long idle_timeout = baseRequest.getHttpChannel().getIdleTimeout();
    channel.setIdleTimeout(_idleTimeoutMs);
    
    try
    {
      super.handle(target, baseRequest, request, response);
    }
    finally
    {
      if ((_applyToAsync) && (request.isAsyncStarted()))
      {
        request.getAsyncContext().addListener(new AsyncListener()
        {
          public void onTimeout(AsyncEvent event)
            throws IOException
          {}
          

          public void onStartAsync(AsyncEvent event)
            throws IOException
          {}
          

          public void onError(AsyncEvent event)
            throws IOException
          {
            channel.setIdleTimeout(idle_timeout);
          }
          
          public void onComplete(AsyncEvent event)
            throws IOException
          {
            channel.setIdleTimeout(idle_timeout);
          }
          
        });
      } else {
        channel.setIdleTimeout(idle_timeout);
      }
    }
  }
}
