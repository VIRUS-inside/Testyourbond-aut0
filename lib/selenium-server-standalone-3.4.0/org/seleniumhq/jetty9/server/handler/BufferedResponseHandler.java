package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.pathmap.PathSpecSet;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.HttpOutput;
import org.seleniumhq.jetty9.server.HttpOutput.Interceptor;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.IncludeExclude;
import org.seleniumhq.jetty9.util.IteratingCallback;
import org.seleniumhq.jetty9.util.IteratingCallback.Action;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



































public class BufferedResponseHandler
  extends HandlerWrapper
{
  static final Logger LOG = Log.getLogger(BufferedResponseHandler.class);
  
  private final IncludeExclude<String> _methods = new IncludeExclude();
  private final IncludeExclude<String> _paths = new IncludeExclude(PathSpecSet.class);
  private final IncludeExclude<String> _mimeTypes = new IncludeExclude();
  



  public BufferedResponseHandler()
  {
    _methods.include(HttpMethod.GET.asString());
    
    for (String type : MimeTypes.getKnownMimeTypes())
    {
      if ((type.startsWith("image/")) || 
        (type.startsWith("audio/")) || 
        (type.startsWith("video/")))
        _mimeTypes.exclude(type);
    }
    LOG.debug("{} mime types {}", new Object[] { this, _mimeTypes });
  }
  

  public IncludeExclude<String> getMethodIncludeExclude()
  {
    return _methods;
  }
  

  public IncludeExclude<String> getPathIncludeExclude()
  {
    return _paths;
  }
  

  public IncludeExclude<String> getMimeIncludeExclude()
  {
    return _mimeTypes;
  }
  




  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    ServletContext context = baseRequest.getServletContext();
    String path = context == null ? baseRequest.getRequestURI() : URIUtil.addPaths(baseRequest.getServletPath(), baseRequest.getPathInfo());
    LOG.debug("{} handle {} in {}", new Object[] { this, baseRequest, context });
    
    HttpOutput out = baseRequest.getResponse().getHttpOutput();
    

    HttpOutput.Interceptor interceptor = out.getInterceptor();
    while (interceptor != null)
    {
      if ((interceptor instanceof BufferedInterceptor))
      {
        LOG.debug("{} already intercepting {}", new Object[] { this, request });
        _handler.handle(target, baseRequest, request, response);
        return;
      }
      interceptor = interceptor.getNextInterceptor();
    }
    

    if (!_methods.test(baseRequest.getMethod()))
    {
      LOG.debug("{} excluded by method {}", new Object[] { this, request });
      _handler.handle(target, baseRequest, request, response);
      return;
    }
    


    if (!isPathBufferable(path))
    {
      LOG.debug("{} excluded by path {}", new Object[] { this, request });
      _handler.handle(target, baseRequest, request, response);
      return;
    }
    

    String mimeType = context == null ? MimeTypes.getDefaultMimeByExtension(path) : context.getMimeType(path);
    if (mimeType != null)
    {
      mimeType = MimeTypes.getContentTypeWithoutCharset(mimeType);
      if (!isMimeTypeBufferable(mimeType))
      {
        LOG.debug("{} excluded by path suffix mime type {}", new Object[] { this, request });
        
        _handler.handle(target, baseRequest, request, response);
        return;
      }
    }
    

    out.setInterceptor(new BufferedInterceptor(baseRequest.getHttpChannel(), out.getInterceptor()));
    
    if (_handler != null) {
      _handler.handle(target, baseRequest, request, response);
    }
  }
  
  protected boolean isMimeTypeBufferable(String mimetype)
  {
    return _mimeTypes.test(mimetype);
  }
  

  protected boolean isPathBufferable(String requestURI)
  {
    if (requestURI == null) {
      return true;
    }
    return _paths.test(requestURI);
  }
  

  private class BufferedInterceptor
    implements HttpOutput.Interceptor
  {
    final HttpOutput.Interceptor _next;
    
    final HttpChannel _channel;
    final Queue<ByteBuffer> _buffers = new ConcurrentLinkedQueue();
    Boolean _aggregating;
    ByteBuffer _aggregate;
    
    public BufferedInterceptor(HttpChannel httpChannel, HttpOutput.Interceptor interceptor)
    {
      _next = interceptor;
      _channel = httpChannel;
    }
    

    public void resetBuffer()
    {
      _buffers.clear();
      _aggregating = null;
      _aggregate = null;
    }
    

    public void write(ByteBuffer content, boolean last, Callback callback)
    {
      if (BufferedResponseHandler.LOG.isDebugEnabled()) {
        BufferedResponseHandler.LOG.debug("{} write last={} {}", new Object[] { this, Boolean.valueOf(last), BufferUtil.toDetailString(content) });
      }
      if (_aggregating == null)
      {
        Response response = _channel.getResponse();
        int sc = response.getStatus();
        if ((sc > 0) && ((sc < 200) || (sc == 204) || (sc == 205) || (sc >= 300))) {
          _aggregating = Boolean.FALSE;
        }
        else {
          String ct = response.getContentType();
          if (ct == null) {
            _aggregating = Boolean.TRUE;
          }
          else {
            ct = MimeTypes.getContentTypeWithoutCharset(ct);
            _aggregating = Boolean.valueOf(isMimeTypeBufferable(StringUtil.asciiToLowerCase(ct)));
          }
        }
      }
      

      if (!_aggregating.booleanValue())
      {
        getNextInterceptor().write(content, last, callback);
        return;
      }
      

      if (last)
      {

        if (BufferUtil.length(content) > 0) {
          _buffers.add(content);
        }
        if (BufferedResponseHandler.LOG.isDebugEnabled())
          BufferedResponseHandler.LOG.debug("{} committing {}", new Object[] { this, Integer.valueOf(_buffers.size()) });
        commit(_buffers, callback);
      }
      else
      {
        if (BufferedResponseHandler.LOG.isDebugEnabled()) {
          BufferedResponseHandler.LOG.debug("{} aggregating", new Object[] { this });
        }
        
        while (BufferUtil.hasContent(content))
        {

          if (BufferUtil.space(_aggregate) == 0)
          {
            int size = Math.max(_channel.getHttpConfiguration().getOutputBufferSize(), BufferUtil.length(content));
            _aggregate = BufferUtil.allocate(size);
            _buffers.add(_aggregate);
          }
          
          BufferUtil.append(_aggregate, content);
        }
        callback.succeeded();
      }
    }
    

    public HttpOutput.Interceptor getNextInterceptor()
    {
      return _next;
    }
    

    public boolean isOptimizedForDirectBuffers()
    {
      return false;
    }
    

    protected void commit(Queue<ByteBuffer> buffers, final Callback callback)
    {
      if (_buffers.size() == 0) {
        getNextInterceptor().write(BufferUtil.EMPTY_BUFFER, true, callback);
      } else if (_buffers.size() == 1)
      {
        getNextInterceptor().write((ByteBuffer)_buffers.remove(), true, callback);
      }
      else
      {
        IteratingCallback icb = new IteratingCallback()
        {
          protected IteratingCallback.Action process()
            throws Exception
          {
            ByteBuffer buffer = (ByteBuffer)_buffers.poll();
            if (buffer == null) {
              return IteratingCallback.Action.SUCCEEDED;
            }
            getNextInterceptor().write(buffer, _buffers.isEmpty(), this);
            return IteratingCallback.Action.SCHEDULED;
          }
          


          protected void onCompleteSuccess()
          {
            callback.succeeded();
          }
          


          protected void onCompleteFailure(Throwable cause)
          {
            callback.failed(cause);
          }
        };
        icb.iterate();
      }
    }
  }
}
