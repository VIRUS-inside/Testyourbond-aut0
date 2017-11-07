package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.seleniumhq.jetty9.http.HttpCompliance;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpGenerator;
import org.seleniumhq.jetty9.http.HttpGenerator.Result;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpHeaderValue;
import org.seleniumhq.jetty9.http.HttpParser;
import org.seleniumhq.jetty9.http.HttpParser.RequestHandler;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.http.MetaData.Response;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.io.AbstractConnection;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.Connection.UpgradeFrom;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.io.EofException;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.IteratingCallback;
import org.seleniumhq.jetty9.util.IteratingCallback.Action;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Invocable.InvocationType;
















public class HttpConnection
  extends AbstractConnection
  implements Runnable, HttpTransport, Connection.UpgradeFrom
{
  private static final Logger LOG = Log.getLogger(HttpConnection.class);
  public static final HttpField CONNECTION_CLOSE = new PreEncodedHttpField(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString());
  public static final String UPGRADE_CONNECTION_ATTRIBUTE = "org.seleniumhq.jetty9.server.HttpConnection.UPGRADE";
  private static final boolean REQUEST_BUFFER_DIRECT = false;
  private static final boolean HEADER_BUFFER_DIRECT = false;
  private static final boolean CHUNK_BUFFER_DIRECT = false;
  private static final ThreadLocal<HttpConnection> __currentConnection = new ThreadLocal();
  
  private final HttpConfiguration _config;
  private final Connector _connector;
  private final ByteBufferPool _bufferPool;
  private final HttpInput _input;
  private final HttpGenerator _generator;
  private final HttpChannelOverHttp _channel;
  private final HttpParser _parser;
  private final AtomicInteger _contentBufferReferences = new AtomicInteger();
  private volatile ByteBuffer _requestBuffer = null;
  private volatile ByteBuffer _chunk = null;
  private final BlockingReadCallback _blockingReadCallback = new BlockingReadCallback(null);
  private final AsyncReadCallback _asyncReadCallback = new AsyncReadCallback(null);
  private final SendCallback _sendCallback = new SendCallback(null);
  



  private final boolean _recordHttpComplianceViolations;
  



  public static HttpConnection getCurrentConnection()
  {
    return (HttpConnection)__currentConnection.get();
  }
  
  protected static HttpConnection setCurrentConnection(HttpConnection connection)
  {
    HttpConnection last = (HttpConnection)__currentConnection.get();
    __currentConnection.set(connection);
    return last;
  }
  
  public HttpConnection(HttpConfiguration config, Connector connector, EndPoint endPoint, HttpCompliance compliance, boolean recordComplianceViolations)
  {
    super(endPoint, connector.getExecutor());
    _config = config;
    _connector = connector;
    _bufferPool = _connector.getByteBufferPool();
    _generator = newHttpGenerator();
    _channel = newHttpChannel();
    _input = _channel.getRequest().getHttpInput();
    _parser = newHttpParser(compliance);
    _recordHttpComplianceViolations = recordComplianceViolations;
    if (LOG.isDebugEnabled()) {
      LOG.debug("New HTTP Connection {}", new Object[] { this });
    }
  }
  
  public HttpConfiguration getHttpConfiguration() {
    return _config;
  }
  
  public boolean isRecordHttpComplianceViolations()
  {
    return _recordHttpComplianceViolations;
  }
  
  protected HttpGenerator newHttpGenerator()
  {
    return new HttpGenerator(_config.getSendServerVersion(), _config.getSendXPoweredBy());
  }
  
  protected HttpChannelOverHttp newHttpChannel()
  {
    return new HttpChannelOverHttp(this, _connector, _config, getEndPoint(), this);
  }
  
  protected HttpParser newHttpParser(HttpCompliance compliance)
  {
    return new HttpParser(newRequestHandler(), getHttpConfiguration().getRequestHeaderSize(), compliance);
  }
  
  protected HttpParser.RequestHandler newRequestHandler()
  {
    return _channel;
  }
  
  public Server getServer()
  {
    return _connector.getServer();
  }
  
  public Connector getConnector()
  {
    return _connector;
  }
  
  public HttpChannel getHttpChannel()
  {
    return _channel;
  }
  
  public HttpParser getParser()
  {
    return _parser;
  }
  
  public HttpGenerator getGenerator()
  {
    return _generator;
  }
  

  public boolean isOptimizedForDirectBuffers()
  {
    return getEndPoint().isOptimizedForDirectBuffers();
  }
  

  public long getMessagesIn()
  {
    return getHttpChannel().getRequests();
  }
  

  public long getMessagesOut()
  {
    return getHttpChannel().getRequests();
  }
  

  public ByteBuffer onUpgradeFrom()
  {
    if (BufferUtil.hasContent(_requestBuffer))
    {
      ByteBuffer buffer = _requestBuffer;
      _requestBuffer = null;
      return buffer;
    }
    return null;
  }
  
  void releaseRequestBuffer()
  {
    if ((_requestBuffer != null) && (!_requestBuffer.hasRemaining()))
    {
      if (LOG.isDebugEnabled())
        LOG.debug("releaseRequestBuffer {}", new Object[] { this });
      ByteBuffer buffer = _requestBuffer;
      _requestBuffer = null;
      _bufferPool.release(buffer);
    }
  }
  
  public ByteBuffer getRequestBuffer()
  {
    if (_requestBuffer == null)
      _requestBuffer = _bufferPool.acquire(getInputBufferSize(), false);
    return _requestBuffer;
  }
  
  public boolean isRequestBufferEmpty()
  {
    return BufferUtil.isEmpty(_requestBuffer);
  }
  
  /* Error */
  public void onFillable()
  {
    // Byte code:
    //   0: getstatic 185	org/seleniumhq/jetty9/server/HttpConnection:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   3: invokeinterface 191 1 0
    //   8: ifeq +42 -> 50
    //   11: getstatic 185	org/seleniumhq/jetty9/server/HttpConnection:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   14: ldc_w 309
    //   17: iconst_3
    //   18: anewarray 195	java/lang/Object
    //   21: dup
    //   22: iconst_0
    //   23: aload_0
    //   24: aastore
    //   25: dup
    //   26: iconst_1
    //   27: aload_0
    //   28: getfield 161	org/seleniumhq/jetty9/server/HttpConnection:_channel	Lorg/seleniumhq/jetty9/server/HttpChannelOverHttp;
    //   31: invokevirtual 313	org/seleniumhq/jetty9/server/HttpChannelOverHttp:getState	()Lorg/seleniumhq/jetty9/server/HttpChannelState;
    //   34: aastore
    //   35: dup
    //   36: iconst_2
    //   37: aload_0
    //   38: getfield 126	org/seleniumhq/jetty9/server/HttpConnection:_requestBuffer	Ljava/nio/ByteBuffer;
    //   41: invokestatic 317	org/seleniumhq/jetty9/util/BufferUtil:toDetailString	(Ljava/nio/ByteBuffer;)Ljava/lang/String;
    //   44: aastore
    //   45: invokeinterface 199 3 0
    //   50: aload_0
    //   51: invokestatic 319	org/seleniumhq/jetty9/server/HttpConnection:setCurrentConnection	(Lorg/seleniumhq/jetty9/server/HttpConnection;)Lorg/seleniumhq/jetty9/server/HttpConnection;
    //   54: astore_1
    //   55: aload_0
    //   56: invokevirtual 231	org/seleniumhq/jetty9/server/HttpConnection:getEndPoint	()Lorg/seleniumhq/jetty9/io/EndPoint;
    //   59: invokeinterface 322 1 0
    //   64: ifeq +119 -> 183
    //   67: aload_0
    //   68: invokespecial 325	org/seleniumhq/jetty9/server/HttpConnection:fillRequestBuffer	()I
    //   71: istore_2
    //   72: aload_0
    //   73: invokespecial 328	org/seleniumhq/jetty9/server/HttpConnection:parseRequestBuffer	()Z
    //   76: istore_3
    //   77: aload_0
    //   78: invokevirtual 231	org/seleniumhq/jetty9/server/HttpConnection:getEndPoint	()Lorg/seleniumhq/jetty9/io/EndPoint;
    //   81: invokeinterface 332 1 0
    //   86: aload_0
    //   87: if_acmpeq +6 -> 93
    //   90: goto +93 -> 183
    //   93: aload_0
    //   94: getfield 181	org/seleniumhq/jetty9/server/HttpConnection:_parser	Lorg/seleniumhq/jetty9/http/HttpParser;
    //   97: invokevirtual 335	org/seleniumhq/jetty9/http/HttpParser:isClose	()Z
    //   100: ifne +13 -> 113
    //   103: aload_0
    //   104: getfield 181	org/seleniumhq/jetty9/server/HttpConnection:_parser	Lorg/seleniumhq/jetty9/http/HttpParser;
    //   107: invokevirtual 338	org/seleniumhq/jetty9/http/HttpParser:isClosed	()Z
    //   110: ifeq +10 -> 120
    //   113: aload_0
    //   114: invokevirtual 341	org/seleniumhq/jetty9/server/HttpConnection:close	()V
    //   117: goto +66 -> 183
    //   120: iload_3
    //   121: ifeq +44 -> 165
    //   124: aload_0
    //   125: getfield 161	org/seleniumhq/jetty9/server/HttpConnection:_channel	Lorg/seleniumhq/jetty9/server/HttpChannelOverHttp;
    //   128: invokevirtual 344	org/seleniumhq/jetty9/server/HttpChannelOverHttp:handle	()Z
    //   131: ifne +7 -> 138
    //   134: iconst_1
    //   135: goto +4 -> 139
    //   138: iconst_0
    //   139: istore 4
    //   141: iload 4
    //   143: ifne +40 -> 183
    //   146: aload_0
    //   147: invokevirtual 231	org/seleniumhq/jetty9/server/HttpConnection:getEndPoint	()Lorg/seleniumhq/jetty9/io/EndPoint;
    //   150: invokeinterface 332 1 0
    //   155: aload_0
    //   156: if_acmpeq +6 -> 162
    //   159: goto +24 -> 183
    //   162: goto +18 -> 180
    //   165: iload_2
    //   166: ifgt +14 -> 180
    //   169: iload_2
    //   170: ifne +13 -> 183
    //   173: aload_0
    //   174: invokevirtual 347	org/seleniumhq/jetty9/server/HttpConnection:fillInterested	()V
    //   177: goto +6 -> 183
    //   180: goto -125 -> 55
    //   183: aload_1
    //   184: invokestatic 319	org/seleniumhq/jetty9/server/HttpConnection:setCurrentConnection	(Lorg/seleniumhq/jetty9/server/HttpConnection;)Lorg/seleniumhq/jetty9/server/HttpConnection;
    //   187: pop
    //   188: getstatic 185	org/seleniumhq/jetty9/server/HttpConnection:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   191: invokeinterface 191 1 0
    //   196: ifeq +105 -> 301
    //   199: getstatic 185	org/seleniumhq/jetty9/server/HttpConnection:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   202: ldc_w 349
    //   205: iconst_3
    //   206: anewarray 195	java/lang/Object
    //   209: dup
    //   210: iconst_0
    //   211: aload_0
    //   212: aastore
    //   213: dup
    //   214: iconst_1
    //   215: aload_0
    //   216: getfield 161	org/seleniumhq/jetty9/server/HttpConnection:_channel	Lorg/seleniumhq/jetty9/server/HttpChannelOverHttp;
    //   219: invokevirtual 313	org/seleniumhq/jetty9/server/HttpChannelOverHttp:getState	()Lorg/seleniumhq/jetty9/server/HttpChannelState;
    //   222: aastore
    //   223: dup
    //   224: iconst_2
    //   225: aload_0
    //   226: getfield 126	org/seleniumhq/jetty9/server/HttpConnection:_requestBuffer	Ljava/nio/ByteBuffer;
    //   229: invokestatic 317	org/seleniumhq/jetty9/util/BufferUtil:toDetailString	(Ljava/nio/ByteBuffer;)Ljava/lang/String;
    //   232: aastore
    //   233: invokeinterface 199 3 0
    //   238: goto +63 -> 301
    //   241: astore 5
    //   243: aload_1
    //   244: invokestatic 319	org/seleniumhq/jetty9/server/HttpConnection:setCurrentConnection	(Lorg/seleniumhq/jetty9/server/HttpConnection;)Lorg/seleniumhq/jetty9/server/HttpConnection;
    //   247: pop
    //   248: getstatic 185	org/seleniumhq/jetty9/server/HttpConnection:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   251: invokeinterface 191 1 0
    //   256: ifeq +42 -> 298
    //   259: getstatic 185	org/seleniumhq/jetty9/server/HttpConnection:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   262: ldc_w 349
    //   265: iconst_3
    //   266: anewarray 195	java/lang/Object
    //   269: dup
    //   270: iconst_0
    //   271: aload_0
    //   272: aastore
    //   273: dup
    //   274: iconst_1
    //   275: aload_0
    //   276: getfield 161	org/seleniumhq/jetty9/server/HttpConnection:_channel	Lorg/seleniumhq/jetty9/server/HttpChannelOverHttp;
    //   279: invokevirtual 313	org/seleniumhq/jetty9/server/HttpChannelOverHttp:getState	()Lorg/seleniumhq/jetty9/server/HttpChannelState;
    //   282: aastore
    //   283: dup
    //   284: iconst_2
    //   285: aload_0
    //   286: getfield 126	org/seleniumhq/jetty9/server/HttpConnection:_requestBuffer	Ljava/nio/ByteBuffer;
    //   289: invokestatic 317	org/seleniumhq/jetty9/util/BufferUtil:toDetailString	(Ljava/nio/ByteBuffer;)Ljava/lang/String;
    //   292: aastore
    //   293: invokeinterface 199 3 0
    //   298: aload 5
    //   300: athrow
    //   301: return
    // Line number table:
    //   Java source line #222	-> byte code offset #0
    //   Java source line #223	-> byte code offset #11
    //   Java source line #225	-> byte code offset #50
    //   Java source line #228	-> byte code offset #55
    //   Java source line #231	-> byte code offset #67
    //   Java source line #234	-> byte code offset #72
    //   Java source line #238	-> byte code offset #77
    //   Java source line #239	-> byte code offset #90
    //   Java source line #242	-> byte code offset #93
    //   Java source line #244	-> byte code offset #113
    //   Java source line #245	-> byte code offset #117
    //   Java source line #249	-> byte code offset #120
    //   Java source line #251	-> byte code offset #124
    //   Java source line #254	-> byte code offset #141
    //   Java source line #255	-> byte code offset #159
    //   Java source line #256	-> byte code offset #162
    //   Java source line #259	-> byte code offset #165
    //   Java source line #261	-> byte code offset #169
    //   Java source line #262	-> byte code offset #173
    //   Java source line #266	-> byte code offset #180
    //   Java source line #270	-> byte code offset #183
    //   Java source line #271	-> byte code offset #188
    //   Java source line #272	-> byte code offset #199
    //   Java source line #270	-> byte code offset #241
    //   Java source line #271	-> byte code offset #248
    //   Java source line #272	-> byte code offset #259
    //   Java source line #274	-> byte code offset #301
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	302	0	this	HttpConnection
    //   54	190	1	last	HttpConnection
    //   71	99	2	filled	int
    //   76	45	3	handle	boolean
    //   139	3	4	suspended	boolean
    //   241	58	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   55	183	241	finally
    //   241	243	241	finally
  }
  
  protected boolean fillAndParseForContent()
  {
    boolean handled = false;
    while (_parser.inContentState())
    {
      int filled = fillRequestBuffer();
      handled = parseRequestBuffer();
      if ((handled) || (filled <= 0) || (_channel.getRequest().getHttpInput().hasContent()))
        break;
    }
    return handled;
  }
  

  private int fillRequestBuffer()
  {
    if (_contentBufferReferences.get() > 0)
    {
      LOG.warn("{} fill with unconsumed content!", new Object[] { this });
      return 0;
    }
    
    if (BufferUtil.isEmpty(_requestBuffer))
    {

      if (getEndPoint().isInputShutdown())
      {

        _parser.atEOF();
        if (LOG.isDebugEnabled())
          LOG.debug("{} filled -1 {}", new Object[] { this, BufferUtil.toDetailString(_requestBuffer) });
        return -1;
      }
      



      _requestBuffer = getRequestBuffer();
      

      try
      {
        int filled = getEndPoint().fill(_requestBuffer);
        if (filled == 0) {
          filled = getEndPoint().fill(_requestBuffer);
        }
        
        if (filled < 0) {
          _parser.atEOF();
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} filled {} {}", new Object[] { this, Integer.valueOf(filled), BufferUtil.toDetailString(_requestBuffer) });
        }
        return filled;
      }
      catch (IOException e)
      {
        LOG.debug(e);
        return -1;
      }
    }
    return 0;
  }
  

  private boolean parseRequestBuffer()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} parse {} {}", new Object[] { this, BufferUtil.toDetailString(_requestBuffer) });
    }
    boolean handle = _parser.parseNext(_requestBuffer == null ? BufferUtil.EMPTY_BUFFER : _requestBuffer);
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} parsed {} {}", new Object[] { this, Boolean.valueOf(handle), _parser });
    }
    
    if (_contentBufferReferences.get() == 0) {
      releaseRequestBuffer();
    }
    return handle;
  }
  



  public void onCompleted()
  {
    if (_channel.getResponse().getStatus() == 101)
    {
      Connection connection = (Connection)_channel.getRequest().getAttribute("org.seleniumhq.jetty9.server.HttpConnection.UPGRADE");
      if (connection != null)
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Upgrade from {} to {}", new Object[] { this, connection });
        _channel.getState().upgrade();
        getEndPoint().upgrade(connection);
        _channel.recycle();
        _parser.reset();
        _generator.reset();
        if (_contentBufferReferences.get() == 0) {
          releaseRequestBuffer();
        }
        else {
          LOG.warn("{} lingering content references?!?!", new Object[] { this });
          _requestBuffer = null;
          _contentBufferReferences.set(0);
        }
        return;
      }
    }
    


    if (_channel.isExpecting100Continue())
    {

      _parser.close();
    }
    else if ((_parser.inContentState()) && (_generator.isPersistent()))
    {

      if (_channel.getRequest().getHttpInput().isAsync())
      {
        if (LOG.isDebugEnabled())
          LOG.debug("unconsumed async input {}", new Object[] { this });
        _channel.abort(new IOException("unconsumed input"));
      }
      else
      {
        if (LOG.isDebugEnabled()) {
          LOG.debug("unconsumed input {}", new Object[] { this });
        }
        if (!_channel.getRequest().getHttpInput().consumeAll()) {
          _channel.abort(new IOException("unconsumed input"));
        }
      }
    }
    
    _channel.recycle();
    if ((_generator.isPersistent()) && (!_parser.isClosed())) {
      _parser.reset();
    } else {
      _parser.close();
    }
    

    if (_chunk != null)
      _bufferPool.release(_chunk);
    _chunk = null;
    _generator.reset();
    

    if (getCurrentConnection() != this)
    {

      if (_parser.isStart())
      {

        if (BufferUtil.isEmpty(_requestBuffer))
        {

          fillInterested();

        }
        else if (getConnector().isRunning())
        {
          try
          {

            getExecutor().execute(this);
          }
          catch (RejectedExecutionException e)
          {
            if (getConnector().isRunning()) {
              LOG.warn(e);
            } else
              LOG.ignore(e);
            getEndPoint().close();
          }
          
        }
        else {
          getEndPoint().close();
        }
        
      }
      else if (getEndPoint().isOpen()) {
        fillInterested();
      }
    }
  }
  
  protected void onFillInterestedFailed(Throwable cause)
  {
    _parser.close();
    super.onFillInterestedFailed(cause);
  }
  

  public void onOpen()
  {
    super.onOpen();
    fillInterested();
  }
  

  public void onClose()
  {
    _sendCallback.close();
    super.onClose();
  }
  

  public void run()
  {
    onFillable();
  }
  

  public void send(MetaData.Response info, boolean head, ByteBuffer content, boolean lastContent, Callback callback)
  {
    if (info == null)
    {
      if ((!lastContent) && (BufferUtil.isEmpty(content)))
      {
        callback.succeeded();

      }
      


    }
    else if (_channel.isExpecting100Continue())
    {
      _generator.setPersistent(false);
    }
    
    if (_sendCallback.reset(info, head, content, lastContent, callback)) {
      _sendCallback.iterate();
    }
  }
  
  HttpInput.Content newContent(ByteBuffer c)
  {
    return new Content(c);
  }
  

  public void abort(Throwable failure)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("abort {} {}", new Object[] { this, failure });
    }
    
    getEndPoint().close();
  }
  

  public boolean isPushSupported()
  {
    return false;
  }
  

  public void push(MetaData.Request request)
  {
    LOG.debug("ignore push in {}", new Object[] { this });
  }
  
  public void asyncReadFillInterested()
  {
    getEndPoint().fillInterested(_asyncReadCallback);
  }
  
  public void blockingReadFillInterested()
  {
    getEndPoint().fillInterested(_blockingReadCallback);
  }
  
  public void blockingReadException(Throwable e)
  {
    _blockingReadCallback.failed(e);
  }
  

  public String toConnectionString()
  {
    return String.format("%s@%x[p=%s,g=%s]=>%s", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), _parser, _generator, _channel });
  }
  


  private class Content
    extends HttpInput.Content
  {
    public Content(ByteBuffer content)
    {
      super();
      _contentBufferReferences.incrementAndGet();
    }
    

    public void succeeded()
    {
      if (_contentBufferReferences.decrementAndGet() == 0) {
        releaseRequestBuffer();
      }
    }
    
    public void failed(Throwable x)
    {
      succeeded();
    }
  }
  
  private class BlockingReadCallback implements Callback
  {
    private BlockingReadCallback() {}
    
    public void succeeded() {
      _input.unblock();
    }
    

    public void failed(Throwable x)
    {
      _input.failed(x);
    }
    



    public Invocable.InvocationType getInvocationType()
    {
      return Invocable.InvocationType.NON_BLOCKING;
    }
  }
  
  private class AsyncReadCallback implements Callback
  {
    private AsyncReadCallback() {}
    
    public void succeeded() {
      if (fillAndParseForContent()) {
        _channel.handle();
      } else if (!_input.isFinished()) {
        asyncReadFillInterested();
      }
    }
    
    public void failed(Throwable x)
    {
      if (_input.failed(x)) {
        _channel.handle();
      }
    }
  }
  
  private class SendCallback extends IteratingCallback
  {
    private MetaData.Response _info;
    private boolean _head;
    private ByteBuffer _content;
    private boolean _lastContent;
    private Callback _callback;
    private ByteBuffer _header;
    private boolean _shutdownOut;
    
    private SendCallback() {
      super();
    }
    

    public Invocable.InvocationType getInvocationType()
    {
      return _callback.getInvocationType();
    }
    
    private boolean reset(MetaData.Response info, boolean head, ByteBuffer content, boolean last, Callback callback)
    {
      if (reset())
      {
        _info = info;
        _head = head;
        _content = content;
        _lastContent = last;
        _callback = callback;
        _header = null;
        _shutdownOut = false;
        return true;
      }
      
      if (isClosed()) {
        callback.failed(new EofException());
      } else
        callback.failed(new WritePendingException());
      return false;
    }
    
    public IteratingCallback.Action process()
      throws Exception
    {
      if (_callback == null) {
        throw new IllegalStateException();
      }
      ByteBuffer chunk = _chunk;
      for (;;)
      {
        HttpGenerator.Result result = _generator.generateResponse(_info, _head, _header, chunk, _content, _lastContent);
        if (HttpConnection.LOG.isDebugEnabled()) {
          HttpConnection.LOG.debug("{} generate: {} ({},{},{})@{}", new Object[] { this, result, 
          

            BufferUtil.toSummaryString(_header), 
            BufferUtil.toSummaryString(_content), 
            Boolean.valueOf(_lastContent), 
            _generator.getState() });
        }
        switch (HttpConnection.1.$SwitchMap$org$eclipse$jetty$http$HttpGenerator$Result[result.ordinal()])
        {
        case 1: 
          throw new EofException("request lifecycle violation");
        

        case 2: 
          _header = _bufferPool.acquire(_config.getResponseHeaderSize(), false);
          
          break;
        

        case 3: 
          chunk = _chunk = _bufferPool.acquire(12, false);
          break;
        


        case 4: 
          if ((_head) || (_generator.isNoContent()))
          {
            BufferUtil.clear(chunk);
            BufferUtil.clear(_content);
          }
          

          if (BufferUtil.hasContent(_header))
          {
            if (BufferUtil.hasContent(_content))
            {
              if (BufferUtil.hasContent(chunk)) {
                getEndPoint().write(this, new ByteBuffer[] { _header, chunk, _content });
              } else {
                getEndPoint().write(this, new ByteBuffer[] { _header, _content });
              }
            } else {
              getEndPoint().write(this, new ByteBuffer[] { _header });
            }
          } else if (BufferUtil.hasContent(chunk))
          {
            if (BufferUtil.hasContent(_content)) {
              getEndPoint().write(this, new ByteBuffer[] { chunk, _content });
            } else {
              getEndPoint().write(this, new ByteBuffer[] { chunk });
            }
          } else if (BufferUtil.hasContent(_content))
          {
            getEndPoint().write(this, new ByteBuffer[] { _content });
          }
          else
          {
            succeeded();
          }
          return IteratingCallback.Action.SCHEDULED;
        

        case 5: 
          _shutdownOut = true;
          break;
        

        case 6: 
          return IteratingCallback.Action.SUCCEEDED;
        

        case 7: 
          break;
        

        default: 
          throw new IllegalStateException("generateResponse=" + result);
        }
        
      }
    }
    
    private void releaseHeader()
    {
      ByteBuffer h = _header;
      _header = null;
      if (h != null) {
        _bufferPool.release(h);
      }
    }
    
    protected void onCompleteSuccess()
    {
      releaseHeader();
      _callback.succeeded();
      if (_shutdownOut) {
        getEndPoint().shutdownOutput();
      }
    }
    
    public void onCompleteFailure(Throwable x)
    {
      releaseHeader();
      failedCallback(_callback, x);
      if (_shutdownOut) {
        getEndPoint().shutdownOutput();
      }
    }
    
    public String toString()
    {
      return String.format("%s[i=%s,cb=%s]", new Object[] { super.toString(), _info, _callback });
    }
  }
}
