package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Connector;
import org.seleniumhq.jetty9.server.NetworkConnector;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


























































public class ShutdownHandler
  extends HandlerWrapper
{
  private static final Logger LOG = Log.getLogger(ShutdownHandler.class);
  
  private final String _shutdownToken;
  private boolean _sendShutdownAtStart;
  private boolean _exitJvm = false;
  









  @Deprecated
  public ShutdownHandler(Server server, String shutdownToken)
  {
    this(shutdownToken);
  }
  
  public ShutdownHandler(String shutdownToken)
  {
    this(shutdownToken, false, false);
  }
  








  public ShutdownHandler(String shutdownToken, boolean exitJVM, boolean sendShutdownAtStart)
  {
    _shutdownToken = shutdownToken;
    setExitJvm(exitJVM);
    setSendShutdownAtStart(sendShutdownAtStart);
  }
  
  public void sendShutdown()
    throws IOException
  {
    URL url = new URL(getServerUrl() + "/shutdown?token=" + _shutdownToken);
    try
    {
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      connection.getResponseCode();
      LOG.info("Shutting down " + url + ": " + connection.getResponseCode() + " " + connection.getResponseMessage(), new Object[0]);
    }
    catch (SocketException e)
    {
      LOG.debug("Not running", new Object[0]);

    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  

  private String getServerUrl()
  {
    NetworkConnector connector = null;
    for (Connector c : getServer().getConnectors())
    {
      if ((c instanceof NetworkConnector))
      {
        connector = (NetworkConnector)c;
        break;
      }
    }
    
    if (connector == null) {
      return "http://localhost";
    }
    return "http://localhost:" + connector.getPort();
  }
  

  protected void doStart()
    throws Exception
  {
    super.doStart();
    if (_sendShutdownAtStart) {
      sendShutdown();
    }
  }
  
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    if (!target.equals("/shutdown"))
    {
      super.handle(target, baseRequest, request, response);
      return;
    }
    
    if (!request.getMethod().equals("POST"))
    {
      response.sendError(400);
      return;
    }
    if (!hasCorrectSecurityToken(request))
    {
      LOG.warn("Unauthorized tokenless shutdown attempt from " + request.getRemoteAddr(), new Object[0]);
      response.sendError(401);
      return;
    }
    if (!requestFromLocalhost(baseRequest))
    {
      LOG.warn("Unauthorized non-loopback shutdown attempt from " + request.getRemoteAddr(), new Object[0]);
      response.sendError(401);
      return;
    }
    
    LOG.info("Shutting down by request from " + request.getRemoteAddr(), new Object[0]);
    doShutdown(baseRequest, response);
  }
  
  protected void doShutdown(Request baseRequest, HttpServletResponse response) throws IOException
  {
    for (Connector connector : getServer().getConnectors())
    {
      connector.shutdown();
    }
    
    response.sendError(200, "Connectors closed, commencing full shutdown");
    baseRequest.setHandled(true);
    
    final Server server = getServer();
    new Thread()
    {

      public void run()
      {
        try
        {
          ShutdownHandler.this.shutdownServer(server);
        }
        catch (InterruptedException e)
        {
          ShutdownHandler.LOG.ignore(e);
        }
        catch (Exception e)
        {
          throw new RuntimeException("Shutting down server", e);
        }
      }
    }.start();
  }
  
  private boolean requestFromLocalhost(Request request)
  {
    InetSocketAddress addr = request.getRemoteInetSocketAddress();
    if (addr == null)
    {
      return false;
    }
    return addr.getAddress().isLoopbackAddress();
  }
  
  private boolean hasCorrectSecurityToken(HttpServletRequest request)
  {
    String tok = request.getParameter("token");
    if (LOG.isDebugEnabled())
      LOG.debug("Token: {}", new Object[] { tok });
    return _shutdownToken.equals(tok);
  }
  
  private void shutdownServer(Server server) throws Exception
  {
    server.stop();
    
    if (_exitJvm)
    {
      System.exit(0);
    }
  }
  
  public void setExitJvm(boolean exitJvm)
  {
    _exitJvm = exitJvm;
  }
  
  public boolean isSendShutdownAtStart()
  {
    return _sendShutdownAtStart;
  }
  
  public void setSendShutdownAtStart(boolean sendShutdownAtStart)
  {
    _sendShutdownAtStart = sendShutdownAtStart;
  }
  
  public String getShutdownToken()
  {
    return _shutdownToken;
  }
  
  public boolean isExitJvm()
  {
    return _exitJvm;
  }
}
