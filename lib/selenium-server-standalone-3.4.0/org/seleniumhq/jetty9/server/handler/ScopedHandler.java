package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Request;




























































































public abstract class ScopedHandler
  extends HandlerWrapper
{
  private static final ThreadLocal<ScopedHandler> __outerScope = new ThreadLocal();
  
  protected ScopedHandler _outerScope;
  
  protected ScopedHandler _nextScope;
  
  public ScopedHandler() {}
  
  protected void doStart()
    throws Exception
  {
    try
    {
      _outerScope = ((ScopedHandler)__outerScope.get());
      if (_outerScope == null) {
        __outerScope.set(this);
      }
      super.doStart();
      
      _nextScope = ((ScopedHandler)getChildHandlerByClass(ScopedHandler.class));
      



      if (_outerScope == null) {
        __outerScope.set(null);
      }
    }
    finally
    {
      if (_outerScope == null) {
        __outerScope.set(null);
      }
    }
  }
  


  public final void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (isStarted())
    {
      if (_outerScope == null) {
        doScope(target, baseRequest, request, response);
      } else {
        doHandle(target, baseRequest, request, response);
      }
    }
  }
  




  public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    nextScope(target, baseRequest, request, response);
  }
  




  public final void nextScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (_nextScope != null) {
      _nextScope.doScope(target, baseRequest, request, response);
    } else if (_outerScope != null) {
      _outerScope.doHandle(target, baseRequest, request, response);
    } else {
      doHandle(target, baseRequest, request, response);
    }
  }
  




  public abstract void doHandle(String paramString, Request paramRequest, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException, ServletException;
  



  public final void nextHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if ((_nextScope != null) && (_nextScope == _handler)) {
      _nextScope.doHandle(target, baseRequest, request, response);
    } else if (_handler != null) {
      super.handle(target, baseRequest, request, response);
    }
  }
}
