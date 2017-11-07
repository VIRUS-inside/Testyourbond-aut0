package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Request;



























public class AsyncDelayHandler
  extends HandlerWrapper
{
  public static final String AHW_ATTR = "o.e.j.s.h.AsyncHandlerWrapper";
  
  public AsyncDelayHandler() {}
  
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if ((!isStarted()) || (_handler == null)) {
      return;
    }
    
    DispatcherType ctype = baseRequest.getDispatcherType();
    DispatcherType dtype = (DispatcherType)baseRequest.getAttribute("o.e.j.s.h.AsyncHandlerWrapper");
    Object async_context_path = null;
    Object async_path_info = null;
    Object async_query_string = null;
    Object async_request_uri = null;
    Object async_servlet_path = null;
    

    boolean restart = false;
    if (dtype != null)
    {

      baseRequest.setAttribute("o.e.j.s.h.AsyncHandlerWrapper", null);
      baseRequest.setDispatcherType(dtype);
      restart = true;
      
      async_context_path = baseRequest.getAttribute("javax.servlet.async.context_path");
      baseRequest.setAttribute("javax.servlet.async.context_path", null);
      async_path_info = baseRequest.getAttribute("javax.servlet.async.path_info");
      baseRequest.setAttribute("javax.servlet.async.path_info", null);
      async_query_string = baseRequest.getAttribute("javax.servlet.async.query_string");
      baseRequest.setAttribute("javax.servlet.async.query_string", null);
      async_request_uri = baseRequest.getAttribute("javax.servlet.async.request_uri");
      baseRequest.setAttribute("javax.servlet.async.request_uri", null);
      async_servlet_path = baseRequest.getAttribute("javax.servlet.async.servlet_path");
      baseRequest.setAttribute("javax.servlet.async.servlet_path", null);
    }
    

    if (!startHandling(baseRequest, restart))
    {

      AsyncContext context = baseRequest.startAsync();
      baseRequest.setAttribute("o.e.j.s.h.AsyncHandlerWrapper", ctype);
      
      delayHandling(baseRequest, context);
      return;
    }
    

    try
    {
      _handler.handle(target, baseRequest, request, response);
    }
    finally
    {
      if (restart)
      {

        baseRequest.setDispatcherType(ctype);
        baseRequest.setAttribute("javax.servlet.async.context_path", async_context_path);
        baseRequest.setAttribute("javax.servlet.async.path_info", async_path_info);
        baseRequest.setAttribute("javax.servlet.async.query_string", async_query_string);
        baseRequest.setAttribute("javax.servlet.async.request_uri", async_request_uri);
        baseRequest.setAttribute("javax.servlet.async.servlet_path", async_servlet_path);
      }
      

      endHandling(baseRequest);
    }
  }
  







  protected boolean startHandling(Request request, boolean restart)
  {
    return true;
  }
  









  protected void delayHandling(Request request, AsyncContext context)
  {
    context.dispatch();
  }
  
  protected void endHandling(Request request) {}
}
