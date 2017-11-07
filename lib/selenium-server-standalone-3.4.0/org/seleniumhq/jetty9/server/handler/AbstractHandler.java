package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




































@ManagedObject("Jetty Handler")
public abstract class AbstractHandler
  extends ContainerLifeCycle
  implements Handler
{
  private static final Logger LOG = Log.getLogger(AbstractHandler.class);
  






  private Server _server;
  







  public AbstractHandler() {}
  







  public abstract void handle(String paramString, Request paramRequest, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException, ServletException;
  







  protected void doError(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    Object o = request.getAttribute("javax.servlet.error.status_code");
    int code = o != null ? Integer.valueOf(o.toString()).intValue() : (o instanceof Integer) ? ((Integer)o).intValue() : 500;
    o = request.getAttribute("javax.servlet.error.message");
    String reason = o != null ? o.toString() : null;
    
    response.sendError(code, reason);
  }
  



  protected void doStart()
    throws Exception
  {
    if (LOG.isDebugEnabled())
      LOG.debug("starting {}", new Object[] { this });
    if (_server == null)
      LOG.warn("No Server set for {}", new Object[] { this });
    super.doStart();
  }
  



  protected void doStop()
    throws Exception
  {
    if (LOG.isDebugEnabled())
      LOG.debug("stopping {}", new Object[] { this });
    super.doStop();
  }
  

  public void setServer(Server server)
  {
    if (_server == server)
      return;
    if (isStarted())
      throw new IllegalStateException("STARTED");
    _server = server;
  }
  

  public Server getServer()
  {
    return _server;
  }
  

  public void destroy()
  {
    if (!isStopped())
      throw new IllegalStateException("!STOPPED");
    super.destroy();
  }
  
  public void dumpThis(Appendable out)
    throws IOException
  {
    out.append(toString()).append(" - ").append(getState()).append('\n');
  }
  



  public static abstract class ErrorDispatchHandler
    extends AbstractHandler
  {
    public ErrorDispatchHandler() {}
    


    public final void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
    {
      if (baseRequest.getDispatcherType() == DispatcherType.ERROR) {
        doError(target, baseRequest, request, response);
      } else {
        doNonErrorHandle(target, baseRequest, request, response);
      }
    }
    
    protected abstract void doNonErrorHandle(String paramString, Request paramRequest, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
      throws IOException, ServletException;
  }
}
