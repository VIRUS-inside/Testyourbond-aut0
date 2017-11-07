package org.seleniumhq.jetty9.server;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;




















public class AsyncContextState
  implements AsyncContext
{
  private final HttpChannel _channel;
  volatile HttpChannelState _state;
  
  public AsyncContextState(HttpChannelState state)
  {
    _state = state;
    _channel = _state.getHttpChannel();
  }
  
  public HttpChannel getHttpChannel()
  {
    return _channel;
  }
  
  HttpChannelState state()
  {
    HttpChannelState state = _state;
    if (state == null)
      throw new IllegalStateException("AsyncContext completed and/or Request lifecycle recycled");
    return state;
  }
  

  public void addListener(final AsyncListener listener, final ServletRequest request, final ServletResponse response)
  {
    AsyncListener wrap = new AsyncListener()
    {
      public void onTimeout(AsyncEvent event)
        throws IOException
      {
        listener.onTimeout(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
      }
      
      public void onStartAsync(AsyncEvent event)
        throws IOException
      {
        listener.onStartAsync(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
      }
      
      public void onError(AsyncEvent event)
        throws IOException
      {
        listener.onError(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
      }
      
      public void onComplete(AsyncEvent event)
        throws IOException
      {
        listener.onComplete(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
      }
    };
    state().addListener(wrap);
  }
  

  public void addListener(AsyncListener listener)
  {
    state().addListener(listener);
  }
  

  public void complete()
  {
    state().complete();
  }
  
  public <T extends AsyncListener> T createListener(Class<T> clazz)
    throws ServletException
  {
    ContextHandler contextHandler = state().getContextHandler();
    if (contextHandler != null) {
      return (AsyncListener)contextHandler.getServletContext().createListener(clazz);
    }
    try {
      return (AsyncListener)clazz.newInstance();
    }
    catch (Exception e)
    {
      throw new ServletException(e);
    }
  }
  

  public void dispatch()
  {
    state().dispatch(null, null);
  }
  

  public void dispatch(String path)
  {
    state().dispatch(null, path);
  }
  

  public void dispatch(ServletContext context, String path)
  {
    state().dispatch(context, path);
  }
  

  public ServletRequest getRequest()
  {
    return state().getAsyncContextEvent().getSuppliedRequest();
  }
  

  public ServletResponse getResponse()
  {
    return state().getAsyncContextEvent().getSuppliedResponse();
  }
  

  public long getTimeout()
  {
    return state().getTimeout();
  }
  

  public boolean hasOriginalRequestAndResponse()
  {
    HttpChannel channel = state().getHttpChannel();
    return (channel.getRequest() == getRequest()) && (channel.getResponse() == getResponse());
  }
  

  public void setTimeout(long arg0)
  {
    state().setTimeout(arg0);
  }
  

  public void start(final Runnable task)
  {
    final HttpChannel channel = state().getHttpChannel();
    channel.execute(new Runnable()
    {

      public void run()
      {
        state().getAsyncContextEvent().getContext().getContextHandler().handle(channel.getRequest(), task);
      }
    });
  }
  
  public void reset()
  {
    _state = null;
  }
  
  public HttpChannelState getHttpChannelState()
  {
    return state();
  }
}
