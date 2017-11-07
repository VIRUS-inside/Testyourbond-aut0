package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.Connection.Listener;
import org.seleniumhq.jetty9.server.AbstractConnector;
import org.seleniumhq.jetty9.server.Connector;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannelState;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.DateCache;
import org.seleniumhq.jetty9.util.RolloverFileOutputStream;






















/**
 * @deprecated
 */
public class DebugHandler
  extends HandlerWrapper
  implements Connection.Listener
{
  private DateCache _date = new DateCache("HH:mm:ss", Locale.US);
  
  private OutputStream _out;
  
  private PrintStream _print;
  

  public DebugHandler() {}
  
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    Response base_response = baseRequest.getResponse();
    Thread thread = Thread.currentThread();
    String old_name = thread.getName();
    
    boolean suspend = false;
    boolean retry = false;
    String name = (String)request.getAttribute("org.seleniumhq.jetty9.thread.name");
    if (name == null) {
      name = old_name + ":" + baseRequest.getHttpURI();
    } else {
      retry = true;
    }
    String ex = null;
    try
    {
      if (retry) {
        print(name, "RESUME");
      } else
        print(name, "REQUEST " + baseRequest.getRemoteAddr() + " " + request.getMethod() + " " + baseRequest.getHeader("Cookie") + "; " + baseRequest.getHeader("User-Agent"));
      thread.setName(name);
      
      getHandler().handle(target, baseRequest, request, response);
    }
    catch (IOException ioe)
    {
      ex = ioe.toString();
      throw ioe;
    }
    catch (ServletException se)
    {
      ex = se.toString() + ":" + se.getCause();
      throw se;
    }
    catch (RuntimeException rte)
    {
      ex = rte.toString();
      throw rte;
    }
    catch (Error e)
    {
      ex = e.toString();
      throw e;
    }
    finally
    {
      thread.setName(old_name);
      suspend = baseRequest.getHttpChannelState().isSuspended();
      if (suspend)
      {
        request.setAttribute("org.seleniumhq.jetty9.thread.name", name);
        print(name, "SUSPEND");
      }
      else {
        print(name, "RESPONSE " + base_response.getStatus() + (ex == null ? "" : new StringBuilder().append("/").append(ex).toString()) + " " + base_response.getContentType());
      }
    }
  }
  
  private void print(String name, String message) {
    long now = System.currentTimeMillis();
    String d = _date.formatNow(now);
    int ms = (int)(now % 1000L);
    
    _print.println(d + (ms > 9 ? ".0" : ms > 99 ? "." : ".00") + ms + ":" + name + " " + message);
  }
  



  protected void doStart()
    throws Exception
  {
    if (_out == null)
      _out = new RolloverFileOutputStream("./logs/yyyy_mm_dd.debug.log", true);
    _print = new PrintStream(_out);
    
    for (Connector connector : getServer().getConnectors()) {
      if ((connector instanceof AbstractConnector))
        ((AbstractConnector)connector).addBean(this, false);
    }
    super.doStart();
  }
  



  protected void doStop()
    throws Exception
  {
    super.doStop();
    _print.close();
    for (Connector connector : getServer().getConnectors()) {
      if ((connector instanceof AbstractConnector)) {
        ((AbstractConnector)connector).removeBean(this);
      }
    }
  }
  

  public OutputStream getOutputStream()
  {
    return _out;
  }
  



  public void setOutputStream(OutputStream out)
  {
    _out = out;
  }
  

  public void onOpened(Connection connection)
  {
    print(Thread.currentThread().getName(), "OPENED " + connection.toString());
  }
  

  public void onClosed(Connection connection)
  {
    print(Thread.currentThread().getName(), "CLOSED " + connection.toString());
  }
}
