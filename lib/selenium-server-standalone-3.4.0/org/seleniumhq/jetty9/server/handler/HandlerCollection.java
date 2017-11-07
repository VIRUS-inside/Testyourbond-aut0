package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HandlerContainer;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.util.ArrayUtil;
import org.seleniumhq.jetty9.util.MultiException;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;






























@ManagedObject("Handler of multiple handlers")
public class HandlerCollection
  extends AbstractHandlerContainer
{
  private final boolean _mutableWhenRunning;
  private volatile Handler[] _handlers;
  
  public HandlerCollection()
  {
    _mutableWhenRunning = false;
  }
  

  public HandlerCollection(boolean mutableWhenRunning)
  {
    _mutableWhenRunning = mutableWhenRunning;
  }
  





  @ManagedAttribute(value="Wrapped handlers", readonly=true)
  public Handler[] getHandlers()
  {
    return _handlers;
  }
  




  public void setHandlers(Handler[] handlers)
  {
    if ((!_mutableWhenRunning) && (isStarted())) {
      throw new IllegalStateException("STARTED");
    }
    if (handlers != null)
    {

      for (Handler handler : handlers) {
        if ((handler == this) || (((handler instanceof HandlerContainer)) && 
          (Arrays.asList(((HandlerContainer)handler).getChildHandlers()).contains(this)))) {
          throw new IllegalStateException("setHandler loop");
        }
      }
      for (Handler handler : handlers)
        if (handler.getServer() != getServer())
          handler.setServer(getServer());
    }
    Handler[] old = _handlers;
    _handlers = handlers;
    updateBeans(old, handlers);
  }
  





  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if ((_handlers != null) && (isStarted()))
    {
      MultiException mex = null;
      
      for (int i = 0; i < _handlers.length; i++)
      {
        try
        {
          _handlers[i].handle(target, baseRequest, request, response);
        }
        catch (IOException e)
        {
          throw e;
        }
        catch (RuntimeException e)
        {
          throw e;
        }
        catch (Exception e)
        {
          if (mex == null)
            mex = new MultiException();
          mex.add(e);
        }
      }
      if (mex != null)
      {
        if (mex.size() == 1) {
          throw new ServletException(mex.getThrowable(0));
        }
        throw new ServletException(mex);
      }
    }
  }
  






  public void addHandler(Handler handler)
  {
    setHandlers((Handler[])ArrayUtil.addToArray(getHandlers(), handler, Handler.class));
  }
  

  public void removeHandler(Handler handler)
  {
    Handler[] handlers = getHandlers();
    
    if ((handlers != null) && (handlers.length > 0)) {
      setHandlers((Handler[])ArrayUtil.removeFromArray(handlers, handler));
    }
  }
  

  protected void expandChildren(List<Handler> list, Class<?> byClass)
  {
    if (getHandlers() != null) {
      for (Handler h : getHandlers()) {
        expandHandler(h, list, byClass);
      }
    }
  }
  
  public void destroy()
  {
    if (!isStopped())
      throw new IllegalStateException("!STOPPED");
    Handler[] children = getChildHandlers();
    setHandlers(null);
    for (Handler child : children)
      child.destroy();
    super.destroy();
  }
  


  public String toString()
  {
    Handler[] handlers = getHandlers();
    return super.toString() + (handlers == null ? "[]" : Arrays.asList(getHandlers()).toString());
  }
}
