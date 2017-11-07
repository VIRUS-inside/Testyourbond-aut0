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
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;




































@ManagedObject("Handler wrapping another Handler")
public class HandlerWrapper
  extends AbstractHandlerContainer
{
  protected Handler _handler;
  
  public HandlerWrapper() {}
  
  @ManagedAttribute(value="Wrapped Handler", readonly=true)
  public Handler getHandler()
  {
    return _handler;
  }
  





  public Handler[] getHandlers()
  {
    if (_handler == null)
      return new Handler[0];
    return new Handler[] { _handler };
  }
  




  public void setHandler(Handler handler)
  {
    if (isStarted()) {
      throw new IllegalStateException("STARTED");
    }
    
    if ((handler == this) || (((handler instanceof HandlerContainer)) && 
      (Arrays.asList(((HandlerContainer)handler).getChildHandlers()).contains(this)))) {
      throw new IllegalStateException("setHandler loop");
    }
    if (handler != null) {
      handler.setServer(getServer());
    }
    Handler old = _handler;
    _handler = handler;
    updateBean(old, _handler, true);
  }
  












  public void insertHandler(HandlerWrapper wrapper)
  {
    if (wrapper == null) {
      throw new IllegalArgumentException();
    }
    HandlerWrapper tail = wrapper;
    while ((tail.getHandler() instanceof HandlerWrapper))
      tail = (HandlerWrapper)tail.getHandler();
    if (tail.getHandler() != null) {
      throw new IllegalArgumentException("bad tail of inserted wrapper chain");
    }
    Handler next = getHandler();
    setHandler(wrapper);
    tail.setHandler(next);
  }
  

  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    Handler handler = _handler;
    if (handler != null) {
      handler.handle(target, baseRequest, request, response);
    }
  }
  

  protected void expandChildren(List<Handler> list, Class<?> byClass)
  {
    expandHandler(_handler, list, byClass);
  }
  


  public void destroy()
  {
    if (!isStopped())
      throw new IllegalStateException("!STOPPED");
    Handler child = getHandler();
    if (child != null)
    {
      setHandler(null);
      child.destroy();
    }
    super.destroy();
  }
}
