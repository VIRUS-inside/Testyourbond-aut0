package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.DispatcherType;
import org.seleniumhq.jetty9.http.BadMessageException;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpGenerator;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpStatus;
import org.seleniumhq.jetty9.http.HttpVersion;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.http.MetaData.Response;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.ChannelEndPoint;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.io.QuietException;
import org.seleniumhq.jetty9.io.RuntimeIOException;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ErrorHandler;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.Callback.Nested;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Scheduler;


































public class HttpChannel
  implements Runnable, HttpOutput.Interceptor
{
  private static final Logger LOG = Log.getLogger(HttpChannel.class);
  private final AtomicBoolean _committed = new AtomicBoolean();
  private final AtomicLong _requests = new AtomicLong();
  
  private final Connector _connector;
  
  private final Executor _executor;
  private final HttpConfiguration _configuration;
  private final EndPoint _endPoint;
  private final HttpTransport _transport;
  private final HttpChannelState _state;
  private final Request _request;
  private final Response _response;
  private MetaData.Response _committedMetaData;
  private RequestLog _requestLog;
  private long _oldIdleTimeout;
  private long _written;
  
  public HttpChannel(Connector connector, HttpConfiguration configuration, EndPoint endPoint, HttpTransport transport)
  {
    _connector = connector;
    _configuration = configuration;
    _endPoint = endPoint;
    _transport = transport;
    
    _state = new HttpChannelState(this);
    _request = new Request(this, newHttpInput(_state));
    _response = new Response(this, newHttpOutput());
    
    _executor = (connector == null ? null : connector.getServer().getThreadPool());
    _requestLog = (connector == null ? null : connector.getServer().getRequestLog());
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("new {} -> {},{},{}", new Object[] { this, _endPoint, _endPoint.getConnection(), _state });
    }
  }
  
  protected HttpInput newHttpInput(HttpChannelState state) {
    return new HttpInput(state);
  }
  
  protected HttpOutput newHttpOutput()
  {
    return new HttpOutput(this);
  }
  
  public HttpChannelState getState()
  {
    return _state;
  }
  
  public long getBytesWritten()
  {
    return _written;
  }
  



  public long getRequests()
  {
    return _requests.get();
  }
  
  public Connector getConnector()
  {
    return _connector;
  }
  
  public HttpTransport getHttpTransport()
  {
    return _transport;
  }
  
  public RequestLog getRequestLog()
  {
    return _requestLog;
  }
  
  public void setRequestLog(RequestLog requestLog)
  {
    _requestLog = requestLog;
  }
  
  public void addRequestLog(RequestLog requestLog)
  {
    if (_requestLog == null) {
      _requestLog = requestLog;
    } else if ((_requestLog instanceof RequestLogCollection)) {
      ((RequestLogCollection)_requestLog).add(requestLog);
    } else {
      _requestLog = new RequestLogCollection(new RequestLog[] { _requestLog, requestLog });
    }
  }
  
  public MetaData.Response getCommittedMetaData() {
    return _committedMetaData;
  }
  






  public long getIdleTimeout()
  {
    return _endPoint.getIdleTimeout();
  }
  






  public void setIdleTimeout(long timeoutMs)
  {
    _endPoint.setIdleTimeout(timeoutMs);
  }
  
  public ByteBufferPool getByteBufferPool()
  {
    return _connector.getByteBufferPool();
  }
  
  public HttpConfiguration getHttpConfiguration()
  {
    return _configuration;
  }
  

  public boolean isOptimizedForDirectBuffers()
  {
    return getHttpTransport().isOptimizedForDirectBuffers();
  }
  
  public Server getServer()
  {
    return _connector.getServer();
  }
  
  public Request getRequest()
  {
    return _request;
  }
  
  public Response getResponse()
  {
    return _response;
  }
  
  public EndPoint getEndPoint()
  {
    return _endPoint;
  }
  
  public InetSocketAddress getLocalAddress()
  {
    return _endPoint.getLocalAddress();
  }
  
  public InetSocketAddress getRemoteAddress()
  {
    return _endPoint.getRemoteAddress();
  }
  







  public void continue100(int available)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  
  public void recycle()
  {
    _committed.set(false);
    _request.recycle();
    _response.recycle();
    _committedMetaData = null;
    _requestLog = (_connector == null ? null : _connector.getServer().getRequestLog());
    _written = 0L;
  }
  


  public void asyncReadFillInterested() {}
  

  public void run()
  {
    handle();
  }
  



  public boolean handle()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} handle {} ", new Object[] { this, _request.getHttpURI() });
    }
    HttpChannelState.Action action = _state.handling();
    




    while (!getServer().isStopped())
    {
      try
      {
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} action {}", new Object[] { this, action });
        }
        switch (1.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$Action[action.ordinal()])
        {
        case 1: 
        case 2: 
          break;
        

        case 3: 
          if (!_request.hasMetaData())
            throw new IllegalStateException("state=" + _state);
          _request.setHandled(false);
          _response.getHttpOutput().reopen();
          
          try
          {
            _request.setDispatcherType(DispatcherType.REQUEST);
            
            List<HttpConfiguration.Customizer> customizers = _configuration.getCustomizers();
            if (!customizers.isEmpty())
            {
              for (HttpConfiguration.Customizer customizer : customizers)
              {
                customizer.customize(getConnector(), _configuration, _request);
                if (_request.isHandled()) {
                  break;
                }
              }
            }
            if (!_request.isHandled()) {
              getServer().handle(this);
            }
          }
          finally {
            _request.setDispatcherType(null);
          }
          break;
        


        case 4: 
          _request.setHandled(false);
          _response.getHttpOutput().reopen();
          
          try
          {
            _request.setDispatcherType(DispatcherType.ASYNC);
            getServer().handleAsync(this);
          }
          finally
          {
            _request.setDispatcherType(null);
          }
          break;
        


        case 5: 
          if (_response.isCommitted())
          {
            if (LOG.isDebugEnabled())
              LOG.debug("Could not perform Error Dispatch because the response is already committed, aborting", new Object[0]);
            _transport.abort((Throwable)_request.getAttribute("javax.servlet.error.exception"));
          }
          else
          {
            _response.reset();
            Integer icode = (Integer)_request.getAttribute("javax.servlet.error.status_code");
            int code = icode != null ? icode.intValue() : 500;
            _response.setStatus(code);
            _request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(code));
            if (icode == null)
              _request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(code));
            _request.setHandled(false);
            _response.getHttpOutput().reopen();
            
            try
            {
              _request.setDispatcherType(DispatcherType.ERROR);
              getServer().handle(this);
            }
            finally
            {
              _request.setDispatcherType(null);
            }
          }
          break;
        


        case 6: 
          throw _state.getAsyncContextEvent().getThrowable();
        


        case 7: 
          ContextHandler handler = _state.getContextHandler();
          if (handler != null) {
            handler.handle(_request, _request.getHttpInput());
          } else
            _request.getHttpInput().run();
          break;
        


        case 8: 
          ContextHandler handler = _state.getContextHandler();
          if (handler != null) {
            handler.handle(_request, _response.getHttpOutput());
          } else
            _response.getHttpOutput().run();
          break;
        


        case 9: 
          if ((!_response.isCommitted()) && (!_request.isHandled()))
          {
            _response.sendError(404);

          }
          else
          {
            int status = _response.getStatus();
            

            boolean hasContent = (!_request.isHead()) && ((!HttpMethod.CONNECT.is(_request.getMethod())) || (status != 200)) && (!HttpStatus.isInformational(status)) && (status != 204) && (status != 304);
            

            if ((hasContent) && (!_response.isContentComplete(_response.getHttpOutput().getWritten())))
              _transport.abort(new IOException("insufficient content written"));
          }
          _response.closeOutput();
          _request.setHandled(true);
          
          _state.onComplete();
          
          onCompleted();
          
          break;
        


        default: 
          throw new IllegalStateException("state=" + _state);
        }
        
      }
      catch (Throwable failure)
      {
        if ("org.seleniumhq.jetty9.continuation.ContinuationThrowable".equals(failure.getClass().getName())) {
          LOG.ignore(failure);
        } else {
          handleException(failure);
        }
      }
      action = _state.unhandle();
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} handle exit, result {}", new Object[] { this, action });
    }
    boolean suspended = action == HttpChannelState.Action.WAIT;
    return !suspended;
  }
  
  protected void sendError(int code, String reason)
  {
    try
    {
      _response.sendError(code, reason);
    }
    catch (Throwable x)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Could not send error " + code + " " + reason, x);
      }
    }
    finally {
      _state.errorComplete();
    }
  }
  










  protected void handleException(Throwable failure)
  {
    if ((failure instanceof RuntimeIOException)) {
      failure = failure.getCause();
    }
    if (((failure instanceof QuietException)) || (!getServer().isRunning()))
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug(_request.getRequestURI(), failure);
      }
    } else if ((failure instanceof BadMessageException))
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug(_request.getRequestURI(), failure);
      } else {
        LOG.warn("{} {}", new Object[] { _request.getRequestURI(), failure });
      }
    }
    else {
      LOG.warn(_request.getRequestURI(), failure);
    }
    
    try
    {
      try
      {
        _state.onError(failure);
      }
      catch (Exception e)
      {
        LOG.warn(e);
        
        if (_response.isCommitted())
        {
          LOG.warn("ERROR Dispatch failed: ", failure);
          _transport.abort(failure);

        }
        else
        {
          Integer code = (Integer)_request.getAttribute("javax.servlet.error.status_code");
          _response.reset();
          _response.setStatus(code == null ? 500 : code.intValue());
          _response.flushBuffer();
        }
      }
    }
    catch (Exception e)
    {
      failure.addSuppressed(e);
      LOG.warn("ERROR Dispatch failed: ", failure);
      _transport.abort(failure);
    }
  }
  
  public boolean isExpecting100Continue()
  {
    return false;
  }
  
  public boolean isExpecting102Processing()
  {
    return false;
  }
  

  public String toString()
  {
    return String.format("%s@%x{r=%s,c=%b,a=%s,uri=%s}", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), _requests, 
      
      Boolean.valueOf(_committed.get()), _state
      .getState(), _request
      .getHttpURI() });
  }
  
  public void onRequest(MetaData.Request request)
  {
    _requests.incrementAndGet();
    _request.setTimeStamp(System.currentTimeMillis());
    HttpFields fields = _response.getHttpFields();
    if ((_configuration.getSendDateHeader()) && (!fields.contains(HttpHeader.DATE))) {
      fields.put(_connector.getServer().getDateField());
    }
    long idleTO = _configuration.getIdleTimeout();
    _oldIdleTimeout = getIdleTimeout();
    if ((idleTO >= 0L) && (_oldIdleTimeout != idleTO)) {
      setIdleTimeout(idleTO);
    }
    _request.setMetaData(request);
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("REQUEST for {} on {}{}{} {} {}{}{}", new Object[] { request.getURIString(), this, System.lineSeparator(), request
        .getMethod(), request.getURIString(), request.getHttpVersion(), System.lineSeparator(), request
        .getFields() });
    }
  }
  
  public boolean onContent(HttpInput.Content content) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} content {}", new Object[] { this, content });
    }
    return _request.getHttpInput().addContent(content);
  }
  
  public boolean onRequestComplete()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("{} onRequestComplete", new Object[] { this });
    return _request.getHttpInput().eof();
  }
  
  public void onCompleted()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("COMPLETE for {} written={}", new Object[] { getRequest().getRequestURI(), Long.valueOf(getBytesWritten()) });
    }
    if (_requestLog != null) {
      _requestLog.log(_request, _response);
    }
    long idleTO = _configuration.getIdleTimeout();
    if ((idleTO >= 0L) && (getIdleTimeout() != _oldIdleTimeout)) {
      setIdleTimeout(_oldIdleTimeout);
    }
    _transport.onCompleted();
  }
  
  public boolean onEarlyEOF()
  {
    return _request.getHttpInput().earlyEOF();
  }
  
  public void onBadMessage(int status, String reason)
  {
    if ((status < 400) || (status > 599)) {
      status = 400;
    }
    
    try
    {
      action = _state.handling();
    }
    catch (IllegalStateException e)
    {
      HttpChannelState.Action action;
      
      abort(e);
      throw new BadMessageException(status, reason);
    }
    try
    {
      HttpChannelState.Action action;
      if (action == HttpChannelState.Action.DISPATCH)
      {
        ByteBuffer content = null;
        HttpFields fields = new HttpFields();
        
        ErrorHandler handler = (ErrorHandler)getServer().getBean(ErrorHandler.class);
        if (handler != null) {
          content = handler.badMessageError(status, reason, fields);
        }
        sendResponse(new MetaData.Response(HttpVersion.HTTP_1_1, status, reason, fields, BufferUtil.length(content)), content, true);
      }
      










      throw new IllegalStateException();
      onCompleted();
    }
    catch (IOException e)
    {
      LOG.debug(e); throw 
      






        new IllegalStateException();
      onCompleted();
    }
    finally
    {
      if (_state.unhandle() == HttpChannelState.Action.COMPLETE) {
        _state.onComplete();
      }
    }
    onCompleted();throw localObject;
  }
  

  protected boolean sendResponse(MetaData.Response info, ByteBuffer content, boolean complete, Callback callback)
  {
    boolean committing = _committed.compareAndSet(false, true);
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("sendResponse info={} content={} complete={} committing={} callback={}", new Object[] { info, 
      
        BufferUtil.toDetailString(content), 
        Boolean.valueOf(complete), 
        Boolean.valueOf(committing), callback });
    }
    
    if (committing)
    {

      if (info == null)
        info = _response.newResponseMetaData();
      commit(info);
      

      int status = info.getStatus();
      Callback committed = (status < 200) && (status >= 100) ? new Commit100Callback(callback, null) : new CommitCallback(callback, null);
      

      _transport.send(info, _request.isHead(), content, complete, committed);
    }
    else if (info == null)
    {

      _transport.send(null, _request.isHead(), content, complete, callback);
    }
    else
    {
      callback.failed(new IllegalStateException("committed"));
    }
    return committing;
  }
  
  /* Error */
  protected boolean sendResponse(MetaData.Response info, ByteBuffer content, boolean complete)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 130	org/seleniumhq/jetty9/server/HttpChannel:_response	Lorg/seleniumhq/jetty9/server/Response;
    //   4: invokevirtual 327	org/seleniumhq/jetty9/server/Response:getHttpOutput	()Lorg/seleniumhq/jetty9/server/HttpOutput;
    //   7: invokevirtual 818	org/seleniumhq/jetty9/server/HttpOutput:acquireWriteBlockingCallback	()Lorg/seleniumhq/jetty9/util/SharedBlockingCallback$Blocker;
    //   10: astore 4
    //   12: aconst_null
    //   13: astore 5
    //   15: aload_0
    //   16: aload_1
    //   17: aload_2
    //   18: iload_3
    //   19: aload 4
    //   21: invokevirtual 820	org/seleniumhq/jetty9/server/HttpChannel:sendResponse	(Lorg/seleniumhq/jetty9/http/MetaData$Response;Ljava/nio/ByteBuffer;ZLorg/seleniumhq/jetty9/util/Callback;)Z
    //   24: istore 6
    //   26: aload 4
    //   28: invokevirtual 823	org/seleniumhq/jetty9/util/SharedBlockingCallback$Blocker:block	()V
    //   31: iload 6
    //   33: istore 7
    //   35: aload 4
    //   37: ifnull +33 -> 70
    //   40: aload 5
    //   42: ifnull +23 -> 65
    //   45: aload 4
    //   47: invokevirtual 826	org/seleniumhq/jetty9/util/SharedBlockingCallback$Blocker:close	()V
    //   50: goto +20 -> 70
    //   53: astore 8
    //   55: aload 5
    //   57: aload 8
    //   59: invokevirtual 596	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   62: goto +8 -> 70
    //   65: aload 4
    //   67: invokevirtual 826	org/seleniumhq/jetty9/util/SharedBlockingCallback$Blocker:close	()V
    //   70: iload 7
    //   72: ireturn
    //   73: astore 6
    //   75: aload 6
    //   77: astore 5
    //   79: aload 6
    //   81: athrow
    //   82: astore 9
    //   84: aload 4
    //   86: ifnull +33 -> 119
    //   89: aload 5
    //   91: ifnull +23 -> 114
    //   94: aload 4
    //   96: invokevirtual 826	org/seleniumhq/jetty9/util/SharedBlockingCallback$Blocker:close	()V
    //   99: goto +20 -> 119
    //   102: astore 10
    //   104: aload 5
    //   106: aload 10
    //   108: invokevirtual 596	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   111: goto +8 -> 119
    //   114: aload 4
    //   116: invokevirtual 826	org/seleniumhq/jetty9/util/SharedBlockingCallback$Blocker:close	()V
    //   119: aload 9
    //   121: athrow
    //   122: astore 4
    //   124: getstatic 160	org/seleniumhq/jetty9/server/HttpChannel:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   127: invokeinterface 166 1 0
    //   132: ifeq +13 -> 145
    //   135: getstatic 160	org/seleniumhq/jetty9/server/HttpChannel:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   138: aload 4
    //   140: invokeinterface 771 2 0
    //   145: aload_0
    //   146: aload 4
    //   148: invokevirtual 728	org/seleniumhq/jetty9/server/HttpChannel:abort	(Ljava/lang/Throwable;)V
    //   151: aload 4
    //   153: athrow
    // Line number table:
    //   Java source line #703	-> byte code offset #0
    //   Java source line #705	-> byte code offset #15
    //   Java source line #706	-> byte code offset #26
    //   Java source line #707	-> byte code offset #31
    //   Java source line #708	-> byte code offset #35
    //   Java source line #707	-> byte code offset #70
    //   Java source line #703	-> byte code offset #73
    //   Java source line #708	-> byte code offset #82
    //   Java source line #709	-> byte code offset #122
    //   Java source line #711	-> byte code offset #124
    //   Java source line #712	-> byte code offset #135
    //   Java source line #713	-> byte code offset #145
    //   Java source line #714	-> byte code offset #151
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	154	0	this	HttpChannel
    //   0	154	1	info	MetaData.Response
    //   0	154	2	content	ByteBuffer
    //   0	154	3	complete	boolean
    //   10	105	4	blocker	org.seleniumhq.jetty9.util.SharedBlockingCallback.Blocker
    //   122	30	4	failure	Throwable
    //   13	92	5	localThrowable4	Throwable
    //   24	8	6	committing	boolean
    //   73	7	6	localThrowable2	Throwable
    //   53	5	8	localThrowable1	Throwable
    //   82	38	9	localObject	Object
    //   102	5	10	localThrowable3	Throwable
    // Exception table:
    //   from	to	target	type
    //   45	50	53	java/lang/Throwable
    //   15	35	73	java/lang/Throwable
    //   15	35	82	finally
    //   73	84	82	finally
    //   94	99	102	java/lang/Throwable
    //   0	70	122	java/lang/Throwable
    //   73	122	122	java/lang/Throwable
  }
  
  protected void commit(MetaData.Response info)
  {
    _committedMetaData = info;
    if (LOG.isDebugEnabled()) {
      LOG.debug("COMMIT for {} on {}{}{} {} {}{}{}", new Object[] { getRequest().getRequestURI(), this, System.lineSeparator(), 
        Integer.valueOf(info.getStatus()), info.getReason(), info.getHttpVersion(), System.lineSeparator(), info
        .getFields() });
    }
  }
  
  public boolean isCommitted() {
    return _committed.get();
  }
  








  public void write(ByteBuffer content, boolean complete, Callback callback)
  {
    _written += BufferUtil.length(content);
    sendResponse(null, content, complete, callback);
  }
  

  public void resetBuffer()
  {
    if (isCommitted()) {
      throw new IllegalStateException("Committed");
    }
  }
  
  public HttpOutput.Interceptor getNextInterceptor() {
    return null;
  }
  
  protected void execute(Runnable task)
  {
    _executor.execute(task);
  }
  
  public Scheduler getScheduler()
  {
    return _connector.getScheduler();
  }
  



  public boolean useDirectBuffers()
  {
    return getEndPoint() instanceof ChannelEndPoint;
  }
  








  public void abort(Throwable failure)
  {
    _transport.abort(failure);
  }
  
  private class CommitCallback extends Callback.Nested
  {
    private CommitCallback(Callback callback)
    {
      super();
    }
    

    public void failed(final Throwable x)
    {
      if (HttpChannel.LOG.isDebugEnabled()) {
        HttpChannel.LOG.debug("Commit failed", x);
      }
      if ((x instanceof BadMessageException))
      {
        _transport.send(HttpGenerator.RESPONSE_500_INFO, false, null, true, new Callback.Nested(this)
        {

          public void succeeded()
          {
            super.failed(x);
            _response.getHttpOutput().closed();
          }
          

          public void failed(Throwable th)
          {
            _transport.abort(x);
            super.failed(x);
          }
        });
      }
      else
      {
        _transport.abort(x);
        super.failed(x);
      }
    }
  }
  
  private class Commit100Callback extends HttpChannel.CommitCallback
  {
    private Commit100Callback(Callback callback)
    {
      super(callback, null);
    }
    

    public void succeeded()
    {
      if (_committed.compareAndSet(true, false)) {
        super.succeeded();
      } else {
        super.failed(new IllegalStateException());
      }
    }
  }
}
