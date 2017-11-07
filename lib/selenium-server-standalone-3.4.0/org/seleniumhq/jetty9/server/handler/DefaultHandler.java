package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.MimeTypes.Type;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.ByteArrayISO8859Writer;
import org.seleniumhq.jetty9.util.IO;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;




























public class DefaultHandler
  extends AbstractHandler
{
  private static final Logger LOG = Log.getLogger(DefaultHandler.class);
  
  final long _faviconModified = System.currentTimeMillis() / 1000L * 1000L;
  byte[] _favicon;
  boolean _serveIcon = true;
  boolean _showContexts = true;
  
  public DefaultHandler()
  {
    try
    {
      URL fav = getClass().getClassLoader().getResource("org/seleniumhq/jetty9/favicon.ico");
      if (fav != null)
      {
        Resource r = Resource.newResource(fav);
        _favicon = IO.readBytes(r.getInputStream());
      }
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
  




  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if ((response.isCommitted()) || (baseRequest.isHandled())) {
      return;
    }
    baseRequest.setHandled(true);
    
    String method = request.getMethod();
    

    if ((_serveIcon) && (_favicon != null) && (HttpMethod.GET.is(method)) && (request.getRequestURI().equals("/favicon.ico")))
    {
      if (request.getDateHeader(HttpHeader.IF_MODIFIED_SINCE.toString()) == _faviconModified) {
        response.setStatus(304);
      }
      else {
        response.setStatus(200);
        response.setContentType("image/x-icon");
        response.setContentLength(_favicon.length);
        response.setDateHeader(HttpHeader.LAST_MODIFIED.toString(), _faviconModified);
        response.setHeader(HttpHeader.CACHE_CONTROL.toString(), "max-age=360000,public");
        response.getOutputStream().write(_favicon);
      }
      return;
    }
    

    if ((!_showContexts) || (!HttpMethod.GET.is(method)) || (!request.getRequestURI().equals("/")))
    {
      response.sendError(404);
      return;
    }
    
    response.setStatus(404);
    response.setContentType(MimeTypes.Type.TEXT_HTML.toString());
    
    ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(1500);Throwable localThrowable6 = null;
    try {
      writer.write("<HTML>\n<HEAD>\n<TITLE>Error 404 - Not Found");
      writer.write("</TITLE>\n<BODY>\n<H2>Error 404 - Not Found.</H2>\n");
      writer.write("No context on this server matched or handled this request.<BR>");
      writer.write("Contexts known to this server are: <ul>");
      
      Server server = getServer();
      Handler[] handlers = server == null ? null : server.getChildHandlersByClass(ContextHandler.class);
      
      for (int i = 0; (handlers != null) && (i < handlers.length); i++)
      {
        context = (ContextHandler)handlers[i];
        if (context.isRunning())
        {
          writer.write("<li><a href=\"");
          if ((context.getVirtualHosts() != null) && (context.getVirtualHosts().length > 0))
            writer.write(request.getScheme() + "://" + context.getVirtualHosts()[0] + ":" + request.getLocalPort());
          writer.write(context.getContextPath());
          if ((context.getContextPath().length() > 1) && (context.getContextPath().endsWith("/")))
            writer.write("/");
          writer.write("\">");
          writer.write(context.getContextPath());
          if ((context.getVirtualHosts() != null) && (context.getVirtualHosts().length > 0))
            writer.write("&nbsp;@&nbsp;" + context.getVirtualHosts()[0] + ":" + request.getLocalPort());
          writer.write("&nbsp;--->&nbsp;");
          writer.write(context.toString());
          writer.write("</a></li>\n");
        }
        else
        {
          writer.write("<li>");
          writer.write(context.getContextPath());
          if ((context.getVirtualHosts() != null) && (context.getVirtualHosts().length > 0))
            writer.write("&nbsp;@&nbsp;" + context.getVirtualHosts()[0] + ":" + request.getLocalPort());
          writer.write("&nbsp;--->&nbsp;");
          writer.write(context.toString());
          if (context.isFailed())
            writer.write(" [failed]");
          if (context.isStopped())
            writer.write(" [stopped]");
          writer.write("</li>\n");
        }
      }
      
      writer.write("</ul><hr>");
      
      baseRequest.getHttpChannel().getHttpConfiguration()
        .writePoweredBy(writer, "<a href=\"http://eclipse.org/jetty\"><img border=0 src=\"/favicon.ico\"/></a>&nbsp;", "<hr/>\n");
      
      writer.write("\n</BODY>\n</HTML>\n");
      writer.flush();
      response.setContentLength(writer.size());
      OutputStream out = response.getOutputStream();ContextHandler context = null;
      try {
        writer.writeTo(out);
      }
      catch (Throwable localThrowable1)
      {
        context = localThrowable1;throw localThrowable1;
      }
      finally {}
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;



























    }
    finally
    {


























      if (writer != null) { if (localThrowable6 != null) try { writer.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else { writer.close();
        }
      }
    }
  }
  

  public boolean getServeIcon()
  {
    return _serveIcon;
  }
  




  public void setServeIcon(boolean serveIcon)
  {
    _serveIcon = serveIcon;
  }
  
  public boolean getShowContexts()
  {
    return _showContexts;
  }
  
  public void setShowContexts(boolean show)
  {
    _showContexts = show;
  }
}
