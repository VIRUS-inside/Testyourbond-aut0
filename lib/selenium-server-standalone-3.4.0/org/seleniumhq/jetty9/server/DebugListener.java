package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.server.handler.ContextHandler.ContextScopeListener;
import org.seleniumhq.jetty9.util.DateCache;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;























@ManagedObject("Debug Listener")
public class DebugListener
  extends AbstractLifeCycle
  implements ServletContextListener
{
  private static final Logger LOG = Log.getLogger(DebugListener.class);
  private static final DateCache __date = new DateCache("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  
  private final String _attr = String.format("__R%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(System.identityHashCode(this)) });
  
  private final PrintStream _out;
  private boolean _renameThread;
  private boolean _showHeaders;
  private boolean _dumpContext;
  
  public DebugListener()
  {
    this(null, false, false, false);
  }
  
  public DebugListener(@Name("renameThread") boolean renameThread, @Name("showHeaders") boolean showHeaders, @Name("dumpContext") boolean dumpContext)
  {
    this(null, renameThread, showHeaders, dumpContext);
  }
  
  public DebugListener(@Name("outputStream") OutputStream out, @Name("renameThread") boolean renameThread, @Name("showHeaders") boolean showHeaders, @Name("dumpContext") boolean dumpContext)
  {
    _out = (out == null ? null : new PrintStream(out));
    _renameThread = renameThread;
    _showHeaders = showHeaders;
    _dumpContext = dumpContext;
  }
  
  @ManagedAttribute("Rename thread within context scope")
  public boolean isRenameThread()
  {
    return _renameThread;
  }
  
  public void setRenameThread(boolean renameThread)
  {
    _renameThread = renameThread;
  }
  
  @ManagedAttribute("Show request headers")
  public boolean isShowHeaders()
  {
    return _showHeaders;
  }
  
  public void setShowHeaders(boolean showHeaders)
  {
    _showHeaders = showHeaders;
  }
  
  @ManagedAttribute("Dump contexts at start")
  public boolean isDumpContext()
  {
    return _dumpContext;
  }
  
  public void setDumpContext(boolean dumpContext)
  {
    _dumpContext = dumpContext;
  }
  

  public void contextInitialized(ServletContextEvent sce)
  {
    sce.getServletContext().addListener(_servletRequestListener);
    ContextHandler handler = ContextHandler.getContextHandler(sce.getServletContext());
    handler.addEventListener(_contextScopeListener);
    String cname = findContextName(sce.getServletContext());
    log("^  ctx=%s %s", new Object[] { cname, sce.getServletContext() });
    if (_dumpContext)
    {
      if (_out == null) {
        handler.dumpStdErr();
      }
      else {
        try
        {
          handler.dump(_out);
        }
        catch (Exception e)
        {
          LOG.warn(e);
        }
      }
    }
  }
  

  public void contextDestroyed(ServletContextEvent sce)
  {
    String cname = findContextName(sce.getServletContext());
    log("v  ctx=%s %s", new Object[] { cname, sce.getServletContext() });
  }
  
  protected String findContextName(ServletContext context)
  {
    if (context == null)
      return null;
    String n = (String)context.getAttribute(_attr);
    if (n == null)
    {
      n = String.format("%s@%x", new Object[] { context.getContextPath(), Integer.valueOf(context.hashCode()) });
      context.setAttribute(_attr, n);
    }
    return n;
  }
  
  protected String findRequestName(ServletRequest request)
  {
    if (request == null)
      return null;
    HttpServletRequest r = (HttpServletRequest)request;
    String n = (String)request.getAttribute(_attr);
    if (n == null)
    {
      n = String.format("%s@%x", new Object[] { r.getRequestURI(), Integer.valueOf(request.hashCode()) });
      request.setAttribute(_attr, n);
    }
    return n;
  }
  
  protected void log(String format, Object... arg)
  {
    if (!isRunning()) {
      return;
    }
    String s = String.format(format, arg);
    
    long now = System.currentTimeMillis();
    long ms = now % 1000L;
    if (_out != null)
      _out.printf("%s.%03d:%s%n", new Object[] { __date.formatNow(now), Long.valueOf(ms), s });
    if (LOG.isDebugEnabled())
      LOG.info(s, new Object[0]);
  }
  
  final AsyncListener _asyncListener = new AsyncListener()
  {
    public void onTimeout(AsyncEvent event)
      throws IOException
    {
      String cname = findContextName(((AsyncContextEvent)event).getServletContext());
      String rname = findRequestName(event.getAsyncContext().getRequest());
      log("!  ctx=%s r=%s onTimeout %s", new Object[] { cname, rname, ((AsyncContextEvent)event).getHttpChannelState() });
    }
    
    public void onStartAsync(AsyncEvent event)
      throws IOException
    {
      String cname = findContextName(((AsyncContextEvent)event).getServletContext());
      String rname = findRequestName(event.getAsyncContext().getRequest());
      log("!  ctx=%s r=%s onStartAsync %s", new Object[] { cname, rname, ((AsyncContextEvent)event).getHttpChannelState() });
    }
    
    public void onError(AsyncEvent event)
      throws IOException
    {
      String cname = findContextName(((AsyncContextEvent)event).getServletContext());
      String rname = findRequestName(event.getAsyncContext().getRequest());
      log("!! ctx=%s r=%s onError %s %s", new Object[] { cname, rname, event.getThrowable(), ((AsyncContextEvent)event).getHttpChannelState() });
    }
    
    public void onComplete(AsyncEvent event)
      throws IOException
    {
      AsyncContextEvent ace = (AsyncContextEvent)event;
      String cname = findContextName(ace.getServletContext());
      String rname = findRequestName(ace.getAsyncContext().getRequest());
      
      Request br = Request.getBaseRequest(ace.getAsyncContext().getRequest());
      Response response = br.getResponse();
      String headers = _showHeaders ? "\n" + response.getHttpFields().toString() : "";
      
      log("!  ctx=%s r=%s onComplete %s %d%s", new Object[] { cname, rname, ace.getHttpChannelState(), Integer.valueOf(response.getStatus()), headers });
    }
  };
  
  final ServletRequestListener _servletRequestListener = new ServletRequestListener()
  {

    public void requestInitialized(ServletRequestEvent sre)
    {
      String cname = findContextName(sre.getServletContext());
      HttpServletRequest r = (HttpServletRequest)sre.getServletRequest();
      
      String rname = findRequestName(r);
      DispatcherType d = r.getDispatcherType();
      if (d == DispatcherType.REQUEST)
      {
        Request br = Request.getBaseRequest(r);
        
        String headers = _showHeaders ? "\n" + br.getMetaData().getFields().toString() : "";
        

        StringBuffer url = r.getRequestURL();
        if (r.getQueryString() != null)
          url.append('?').append(r.getQueryString());
        log(">> %s ctx=%s r=%s %s %s %s %s %s%s", new Object[] { d, cname, rname, d, r
        


          .getMethod(), url
          .toString(), r
          .getProtocol(), br
          .getHttpChannel(), headers });
      }
      else
      {
        log(">> %s ctx=%s r=%s", new Object[] { d, cname, rname });
      }
    }
    
    public void requestDestroyed(ServletRequestEvent sre)
    {
      String cname = findContextName(sre.getServletContext());
      HttpServletRequest r = (HttpServletRequest)sre.getServletRequest();
      String rname = findRequestName(r);
      DispatcherType d = r.getDispatcherType();
      if (sre.getServletRequest().isAsyncStarted())
      {
        sre.getServletRequest().getAsyncContext().addListener(_asyncListener);
        log("<< %s ctx=%s r=%s async=true", new Object[] { d, cname, rname });
      }
      else
      {
        Request br = Request.getBaseRequest(r);
        String headers = _showHeaders ? "\n" + br.getResponse().getHttpFields().toString() : "";
        log("<< %s ctx=%s r=%s async=false %d%s", new Object[] { d, cname, rname, Integer.valueOf(Request.getBaseRequest(r).getResponse().getStatus()), headers });
      }
    }
  };
  
  final ContextHandler.ContextScopeListener _contextScopeListener = new ContextHandler.ContextScopeListener()
  {

    public void enterScope(ContextHandler.Context context, Request request, Object reason)
    {
      String cname = findContextName(context);
      if (request == null) {
        log(">  ctx=%s %s", new Object[] { cname, reason });
      }
      else {
        String rname = findRequestName(request);
        
        if (_renameThread)
        {
          Thread thread = Thread.currentThread();
          thread.setName(String.format("%s#%s", new Object[] { thread.getName(), rname }));
        }
        
        log(">  ctx=%s r=%s %s", new Object[] { cname, rname, reason });
      }
    }
    


    public void exitScope(ContextHandler.Context context, Request request)
    {
      String cname = findContextName(context);
      if (request == null) {
        log("<  ctx=%s", new Object[] { cname });
      }
      else {
        String rname = findRequestName(request);
        
        log("<  ctx=%s r=%s", new Object[] { cname, rname });
        if (_renameThread)
        {
          Thread thread = Thread.currentThread();
          if (thread.getName().endsWith(rname)) {
            thread.setName(thread.getName().substring(0, thread.getName().length() - rname.length() - 1));
          }
        }
      }
    }
  };
}
