package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpStatus;
import org.seleniumhq.jetty9.http.MimeTypes.Type;
import org.seleniumhq.jetty9.server.Dispatcher;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


























public class ErrorHandler
  extends AbstractHandler
{
  private static final Logger LOG = Log.getLogger(ErrorHandler.class);
  
  public static final String ERROR_PAGE = "org.seleniumhq.jetty9.server.error_page";
  boolean _showStacks = true;
  boolean _showMessageInTitle = true;
  String _cacheControl = "must-revalidate,no-cache,no-store";
  




  public ErrorHandler() {}
  



  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    doError(target, baseRequest, request, response);
  }
  
  public void doError(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    String method = request.getMethod();
    if ((!HttpMethod.GET.is(method)) && (!HttpMethod.POST.is(method)) && (!HttpMethod.HEAD.is(method)))
    {
      baseRequest.setHandled(true);
      return;
    }
    
    if ((this instanceof ErrorPageMapper))
    {
      String error_page = ((ErrorPageMapper)this).getErrorPage(request);
      if (error_page != null)
      {
        String old_error_page = (String)request.getAttribute("org.seleniumhq.jetty9.server.error_page");
        ServletContext servlet_context = request.getServletContext();
        if (servlet_context == null)
          servlet_context = ContextHandler.getCurrentContext();
        if (servlet_context == null)
        {
          LOG.warn("No ServletContext for error page {}", new Object[] { error_page });
        }
        else if ((old_error_page != null) && (old_error_page.equals(error_page)))
        {
          LOG.warn("Error page loop {}", new Object[] { error_page });
        }
        else
        {
          request.setAttribute("org.seleniumhq.jetty9.server.error_page", error_page);
          
          Dispatcher dispatcher = (Dispatcher)servlet_context.getRequestDispatcher(error_page);
          try
          {
            if (LOG.isDebugEnabled())
              LOG.debug("error page dispatch {}->{}", new Object[] { error_page, dispatcher });
            if (dispatcher != null)
            {
              dispatcher.error(request, response);
              return;
            }
            LOG.warn("No error page found " + error_page, new Object[0]);
          }
          catch (ServletException e)
          {
            LOG.warn("EXCEPTION ", e);
            return;
          }
          
        }
        
      }
      else if (LOG.isDebugEnabled())
      {
        LOG.debug("No Error Page mapping for request({} {}) (using default)", new Object[] { request.getMethod(), request.getRequestURI() });
      }
    }
    

    if (_cacheControl != null)
      response.setHeader(HttpHeader.CACHE_CONTROL.asString(), _cacheControl);
    generateAcceptableResponse(baseRequest, request, response, response.getStatus(), baseRequest.getResponse().getReason());
  }
  












  protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message)
    throws IOException
  {
    List<String> acceptable = baseRequest.getHttpFields().getQualityCSV(HttpHeader.ACCEPT);
    
    if ((acceptable.isEmpty()) && (!baseRequest.getHttpFields().contains(HttpHeader.ACCEPT))) {
      generateAcceptableResponse(baseRequest, request, response, code, message, MimeTypes.Type.TEXT_HTML.asString());
    }
    else {
      for (String mimeType : acceptable)
      {
        generateAcceptableResponse(baseRequest, request, response, code, message, mimeType);
        if (baseRequest.isHandled())
          return;
      }
    }
    baseRequest.setHandled(true);
    baseRequest.getResponse().closeOutput();
  }
  

















  protected Writer getAcceptableWriter(Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    List<String> acceptable = baseRequest.getHttpFields().getQualityCSV(HttpHeader.ACCEPT_CHARSET);
    if (acceptable.isEmpty())
    {
      response.setCharacterEncoding(StandardCharsets.ISO_8859_1.name());
      return response.getWriter();
    }
    
    for (String charset : acceptable)
    {
      try
      {
        if ("*".equals(charset)) {
          response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } else
          response.setCharacterEncoding(Charset.forName(charset).name());
        return response.getWriter();
      }
      catch (Exception e)
      {
        LOG.ignore(e);
      }
    }
    return null;
  }
  












  protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message, String mimeType)
    throws IOException
  {
    switch (mimeType)
    {

    case "text/html": 
    case "text/*": 
    case "*/*": 
      baseRequest.setHandled(true);
      Writer writer = getAcceptableWriter(baseRequest, request, response);
      if (writer != null)
      {
        response.setContentType(MimeTypes.Type.TEXT_HTML.asString());
        handleErrorPage(request, writer, code, message);
      }
      break;
    }
    
  }
  
  protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message)
    throws IOException
  {
    writeErrorPage(request, writer, code, message, _showStacks);
  }
  

  protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks)
    throws IOException
  {
    if (message == null) {
      message = HttpStatus.getMessage(code);
    }
    writer.write("<html>\n<head>\n");
    writeErrorPageHead(request, writer, code, message);
    writer.write("</head>\n<body>");
    writeErrorPageBody(request, writer, code, message, showStacks);
    writer.write("\n</body>\n</html>\n");
  }
  

  protected void writeErrorPageHead(HttpServletRequest request, Writer writer, int code, String message)
    throws IOException
  {
    writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"/>\n");
    writer.write("<title>Error ");
    writer.write(Integer.toString(code));
    
    if (_showMessageInTitle)
    {
      writer.write(32);
      write(writer, message);
    }
    writer.write("</title>\n");
  }
  

  protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks)
    throws IOException
  {
    String uri = request.getRequestURI();
    
    writeErrorPageMessage(request, writer, code, message, uri);
    if (showStacks) {
      writeErrorPageStacks(request, writer);
    }
    
    Request.getBaseRequest(request).getHttpChannel().getHttpConfiguration().writePoweredBy(writer, "<hr>", "<hr/>\n");
  }
  

  protected void writeErrorPageMessage(HttpServletRequest request, Writer writer, int code, String message, String uri)
    throws IOException
  {
    writer.write("<h2>HTTP ERROR ");
    writer.write(Integer.toString(code));
    writer.write("</h2>\n<p>Problem accessing ");
    write(writer, uri);
    writer.write(". Reason:\n<pre>    ");
    write(writer, message);
    writer.write("</pre></p>");
  }
  

  protected void writeErrorPageStacks(HttpServletRequest request, Writer writer)
    throws IOException
  {
    Throwable th = (Throwable)request.getAttribute("javax.servlet.error.exception");
    while (th != null)
    {
      writer.write("<h3>Caused by:</h3><pre>");
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      th.printStackTrace(pw);
      pw.flush();
      write(writer, sw.getBuffer().toString());
      writer.write("</pre>\n");
      
      th = th.getCause();
    }
  }
  












  public ByteBuffer badMessageError(int status, String reason, HttpFields fields)
  {
    if (reason == null)
      reason = HttpStatus.getMessage(status);
    fields.put(HttpHeader.CONTENT_TYPE, MimeTypes.Type.TEXT_HTML_8859_1.asString());
    return BufferUtil.toBuffer("<h1>Bad Message " + status + "</h1><pre>reason: " + reason + "</pre>");
  }
  




  public String getCacheControl()
  {
    return _cacheControl;
  }
  




  public void setCacheControl(String cacheControl)
  {
    _cacheControl = cacheControl;
  }
  




  public boolean isShowStacks()
  {
    return _showStacks;
  }
  




  public void setShowStacks(boolean showStacks)
  {
    _showStacks = showStacks;
  }
  




  public void setShowMessageInTitle(boolean showMessageInTitle)
  {
    _showMessageInTitle = showMessageInTitle;
  }
  


  public boolean getShowMessageInTitle()
  {
    return _showMessageInTitle;
  }
  

  protected void write(Writer writer, String string)
    throws IOException
  {
    if (string == null) {
      return;
    }
    writer.write(StringUtil.sanitizeXmlString(string));
  }
  







  public static ErrorHandler getErrorHandler(Server server, ContextHandler context)
  {
    ErrorHandler error_handler = null;
    if (context != null)
      error_handler = context.getErrorHandler();
    if ((error_handler == null) && (server != null))
      error_handler = (ErrorHandler)server.getBean(ErrorHandler.class);
    return error_handler;
  }
  
  public static abstract interface ErrorPageMapper
  {
    public abstract String getErrorPage(HttpServletRequest paramHttpServletRequest);
  }
}
