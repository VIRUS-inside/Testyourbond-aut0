package org.eclipse.jetty.websocket.common.io;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.jetty.io.AbstractConnection;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.Connection.UpgradeTo;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.Scheduler;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.CloseException;
import org.eclipse.jetty.websocket.api.SuspendToken;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.common.CloseInfo;
import org.eclipse.jetty.websocket.common.ConnectionState;
import org.eclipse.jetty.websocket.common.Generator;
import org.eclipse.jetty.websocket.common.LogicalConnection;
import org.eclipse.jetty.websocket.common.Parser;





















public abstract class AbstractWebSocketConnection
  extends AbstractConnection
  implements LogicalConnection, Connection.UpgradeTo, IOState.ConnectionStateListener, Dumpable
{
  private final AtomicBoolean closed = new AtomicBoolean();
  
  private class Flusher extends FrameFlusher
  {
    private Flusher(ByteBufferPool bufferPool, Generator generator, EndPoint endpoint)
    {
      super(generator, endpoint, getPolicy().getMaxBinaryMessageBufferSize(), 8);
    }
    

    protected void onFailure(Throwable x)
    {
      AbstractWebSocketConnection.this.notifyError(x);
      
      if (ioState.wasAbnormalClose())
      {
        AbstractWebSocketConnection.LOG.ignore(x);
        return;
      }
      
      if (AbstractWebSocketConnection.LOG.isDebugEnabled())
        AbstractWebSocketConnection.LOG.debug("Write flush failure", x);
      ioState.onWriteFailure(x);
    }
  }
  
  public class OnDisconnectCallback implements WriteCallback
  {
    private final boolean outputOnly;
    
    public OnDisconnectCallback(boolean outputOnly)
    {
      this.outputOnly = outputOnly;
    }
    

    public void writeFailed(Throwable x)
    {
      AbstractWebSocketConnection.this.disconnect(outputOnly);
    }
    

    public void writeSuccess()
    {
      AbstractWebSocketConnection.this.disconnect(outputOnly);
    }
  }
  
  public class OnCloseLocalCallback implements WriteCallback
  {
    private final WriteCallback callback;
    private final CloseInfo close;
    
    public OnCloseLocalCallback(WriteCallback callback, CloseInfo close)
    {
      this.callback = callback;
      this.close = close;
    }
    
    public OnCloseLocalCallback(CloseInfo close)
    {
      this(null, close);
    }
    

    public void writeFailed(Throwable x)
    {
      try
      {
        if (callback != null)
        {
          callback.writeFailed(x);
        }
        


        onLocalClose(); } finally { onLocalClose();
      }
    }
    

    public void writeSuccess()
    {
      try
      {
        if (callback != null)
        {
          callback.writeSuccess();
        }
        


        onLocalClose(); } finally { onLocalClose();
      }
    }
    
    private void onLocalClose()
    {
      if (AbstractWebSocketConnection.LOG_CLOSE.isDebugEnabled())
        AbstractWebSocketConnection.LOG_CLOSE.debug("Local Close Confirmed {}", new Object[] { close });
      if (close.isAbnormal())
      {
        ioState.onAbnormalClose(close);
      }
      else
      {
        ioState.onCloseLocal(close);
      }
    }
  }
  
  public static class Stats
  {
    private AtomicLong countFillInterestedEvents = new AtomicLong(0L);
    private AtomicLong countOnFillableEvents = new AtomicLong(0L);
    private AtomicLong countFillableErrors = new AtomicLong(0L);
    
    public Stats() {}
    
    public long getFillableErrorCount() { return countFillableErrors.get(); }
    

    public long getFillInterestedCount()
    {
      return countFillInterestedEvents.get();
    }
    
    public long getOnFillableCount()
    {
      return countOnFillableEvents.get();
    }
  }
  
  private static enum ReadMode
  {
    PARSE, 
    DISCARD, 
    EOF;
    
    private ReadMode() {} }
  private static final Logger LOG = Log.getLogger(AbstractWebSocketConnection.class);
  private static final Logger LOG_OPEN = Log.getLogger(AbstractWebSocketConnection.class.getName() + "_OPEN");
  private static final Logger LOG_CLOSE = Log.getLogger(AbstractWebSocketConnection.class.getName() + "_CLOSE");
  
  private static final int MIN_BUFFER_SIZE = 28;
  
  private final ByteBufferPool bufferPool;
  
  private final Scheduler scheduler;
  
  private final Generator generator;
  
  private final Parser parser;
  private final WebSocketPolicy policy;
  private final AtomicBoolean suspendToken;
  private final FrameFlusher flusher;
  private final String id;
  private List<ExtensionConfig> extensions;
  private boolean isFilling;
  private ByteBuffer prefillBuffer;
  private ReadMode readMode = ReadMode.PARSE;
  private IOState ioState;
  private Stats stats = new Stats();
  
  public AbstractWebSocketConnection(EndPoint endp, Executor executor, Scheduler scheduler, WebSocketPolicy policy, ByteBufferPool bufferPool)
  {
    super(endp, executor);
    id = String.format("%s:%d->%s:%d", new Object[] {endp
      .getLocalAddress().getAddress().getHostAddress(), 
      Integer.valueOf(endp.getLocalAddress().getPort()), endp
      .getRemoteAddress().getAddress().getHostAddress(), 
      Integer.valueOf(endp.getRemoteAddress().getPort()) });
    this.policy = policy;
    this.bufferPool = bufferPool;
    generator = new Generator(policy, bufferPool);
    parser = new Parser(policy, bufferPool);
    this.scheduler = scheduler;
    extensions = new ArrayList();
    suspendToken = new AtomicBoolean(false);
    ioState = new IOState();
    ioState.addListener(this);
    flusher = new Flusher(bufferPool, generator, endp, null);
    setInputBufferSize(policy.getInputBufferSize());
    setMaxIdleTimeout(policy.getIdleTimeout());
  }
  

  public Executor getExecutor()
  {
    return super.getExecutor();
  }
  




  public void close()
  {
    if (LOG_CLOSE.isDebugEnabled())
      LOG_CLOSE.debug("close()", new Object[0]);
    close(new CloseInfo());
  }
  













  public void close(int statusCode, String reason)
  {
    if (LOG_CLOSE.isDebugEnabled())
      LOG_CLOSE.debug("close({},{})", new Object[] { Integer.valueOf(statusCode), reason });
    close(new CloseInfo(statusCode, reason));
  }
  
  private void close(CloseInfo closeInfo)
  {
    if (closed.compareAndSet(false, true)) {
      outgoingFrame(closeInfo.asFrame(), new OnCloseLocalCallback(closeInfo), BatchMode.OFF);
    }
  }
  
  public void disconnect()
  {
    if (LOG_CLOSE.isDebugEnabled())
      LOG_CLOSE.debug("{} disconnect()", new Object[] { policy.getBehavior() });
    disconnect(false);
  }
  
  private void disconnect(boolean onlyOutput)
  {
    if (LOG_CLOSE.isDebugEnabled()) {
      LOG_CLOSE.debug("{} disconnect({})", new Object[] { policy.getBehavior(), onlyOutput ? "outputOnly" : "both" });
    }
    flusher.close();
    EndPoint endPoint = getEndPoint();
    

    if (LOG_CLOSE.isDebugEnabled())
      LOG_CLOSE.debug("Shutting down output {}", new Object[] { endPoint });
    endPoint.shutdownOutput();
    if (!onlyOutput)
    {
      if (LOG_CLOSE.isDebugEnabled())
        LOG_CLOSE.debug("Closing {}", new Object[] { endPoint });
      endPoint.close();
    }
  }
  
  protected void execute(Runnable task)
  {
    try
    {
      getExecutor().execute(task);
    }
    catch (RejectedExecutionException e)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Job not dispatched: {}", new Object[] { task });
      }
    }
  }
  
  public void fillInterested()
  {
    stats.countFillInterestedEvents.incrementAndGet();
    super.fillInterested();
  }
  

  public ByteBufferPool getBufferPool()
  {
    return bufferPool;
  }
  







  public List<ExtensionConfig> getExtensions()
  {
    return extensions;
  }
  
  public Generator getGenerator()
  {
    return generator;
  }
  

  public String getId()
  {
    return id;
  }
  

  public long getIdleTimeout()
  {
    return getEndPoint().getIdleTimeout();
  }
  

  public IOState getIOState()
  {
    return ioState;
  }
  

  public long getMaxIdleTimeout()
  {
    return getEndPoint().getIdleTimeout();
  }
  
  public Parser getParser()
  {
    return parser;
  }
  

  public WebSocketPolicy getPolicy()
  {
    return policy;
  }
  

  public InetSocketAddress getRemoteAddress()
  {
    return getEndPoint().getRemoteAddress();
  }
  
  public Scheduler getScheduler()
  {
    return scheduler;
  }
  
  public Stats getStats()
  {
    return stats;
  }
  

  public boolean isOpen()
  {
    return !closed.get();
  }
  

  public boolean isReading()
  {
    return isFilling;
  }
  






  public void onClose()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("{} onClose()", new Object[] { policy.getBehavior() });
    super.onClose();
    ioState.onDisconnected();
    flusher.close();
  }
  

  public void onConnectionStateChange(ConnectionState state)
  {
    if (LOG_CLOSE.isDebugEnabled()) {
      LOG_CLOSE.debug("{} Connection State Change: {}", new Object[] { policy.getBehavior(), state });
    }
    switch (1.$SwitchMap$org$eclipse$jetty$websocket$common$ConnectionState[state.ordinal()])
    {
    case 1: 
      if (BufferUtil.hasContent(prefillBuffer))
      {
        if (LOG.isDebugEnabled())
        {
          LOG.debug("Parsing Upgrade prefill buffer ({} remaining)", prefillBuffer.remaining());
        }
        parser.parse(prefillBuffer);
      }
      if (LOG.isDebugEnabled())
      {
        LOG.debug("OPEN: normal fillInterested", new Object[0]);
      }
      

      fillInterested();
      break;
    case 2: 
      if (LOG_CLOSE.isDebugEnabled())
        LOG_CLOSE.debug("CLOSED - wasAbnormalClose: {}", new Object[] { Boolean.valueOf(ioState.wasAbnormalClose()) });
      if (ioState.wasAbnormalClose())
      {

        CloseInfo abnormal = new CloseInfo(1001, "Abnormal Close - " + ioState.getCloseInfo().getReason());
        outgoingFrame(abnormal.asFrame(), new OnDisconnectCallback(false), BatchMode.OFF);

      }
      else
      {
        disconnect(false);
      }
      break;
    case 3: 
      if (LOG_CLOSE.isDebugEnabled()) {
        LOG_CLOSE.debug("CLOSING - wasRemoteCloseInitiated: {}", new Object[] { Boolean.valueOf(ioState.wasRemoteCloseInitiated()) });
      }
      if (ioState.wasRemoteCloseInitiated())
      {
        CloseInfo close = ioState.getCloseInfo();
        
        outgoingFrame(close.asFrame(), new OnCloseLocalCallback(new OnDisconnectCallback(true), close), BatchMode.OFF);
      }
      
      break;
    }
    
  }
  
  public void onFillable()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("{} onFillable()", new Object[] { policy.getBehavior() });
    stats.countOnFillableEvents.incrementAndGet();
    
    ByteBuffer buffer = bufferPool.acquire(getInputBufferSize(), true);
    
    try
    {
      isFilling = true;
      
      if (readMode == ReadMode.PARSE)
      {
        readMode = readParse(buffer);
      }
      else
      {
        readMode = readDiscard(buffer);
      }
      


      bufferPool.release(buffer); } finally { bufferPool.release(buffer);
    }
    
    if (!suspendToken.get())
    {
      fillInterested();
    }
    else
    {
      isFilling = false;
    }
  }
  

  protected void onFillInterestedFailed(Throwable cause)
  {
    LOG.ignore(cause);
    stats.countFillInterestedEvents.incrementAndGet();
    super.onFillInterestedFailed(cause);
  }
  






  protected void setInitialBuffer(ByteBuffer prefilled)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("set Initial Buffer - {}", new Object[] { BufferUtil.toDetailString(prefilled) });
    }
    prefillBuffer = prefilled;
  }
  
  private void notifyError(Throwable t)
  {
    getParser().getIncomingFramesHandler().incomingError(t);
  }
  

  public void onOpen()
  {
    if (LOG_OPEN.isDebugEnabled())
      LOG_OPEN.debug("[{}] {}.onOpened()", new Object[] { policy.getBehavior(), getClass().getSimpleName() });
    super.onOpen();
    ioState.onOpened();
  }
  




  protected boolean onReadTimeout()
  {
    IOState state = getIOState();
    ConnectionState cstate = state.getConnectionState();
    if (LOG_CLOSE.isDebugEnabled()) {
      LOG_CLOSE.debug("{} Read Timeout - {}", new Object[] { policy.getBehavior(), cstate });
    }
    if (cstate == ConnectionState.CLOSED)
    {
      if (LOG_CLOSE.isDebugEnabled()) {
        LOG_CLOSE.debug("onReadTimeout - Connection Already CLOSED", new Object[0]);
      }
      
      return true;
    }
    
    try
    {
      notifyError(new SocketTimeoutException("Timeout on Read"));
      



      close(1001, "Idle Timeout"); } finally { close(1001, "Idle Timeout");
    }
    
    return false;
  }
  




  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("outgoingFrame({}, {})", new Object[] { frame, callback });
    }
    
    flusher.enqueue(frame, callback, batchMode);
  }
  
  private ReadMode readDiscard(ByteBuffer buffer)
  {
    EndPoint endPoint = getEndPoint();
    try
    {
      for (;;)
      {
        int filled = endPoint.fill(buffer);
        if (filled == 0)
        {
          return ReadMode.DISCARD;
        }
        if (filled < 0)
        {
          if (LOG_CLOSE.isDebugEnabled())
            LOG_CLOSE.debug("read - EOF Reached (remote: {})", new Object[] { getRemoteAddress() });
          return ReadMode.EOF;
        }
        

        if (LOG_CLOSE.isDebugEnabled()) {
          LOG_CLOSE.debug("Discarded {} bytes - {}", new Object[] { Integer.valueOf(filled), BufferUtil.toDetailString(buffer) });
        }
      }
      








      return ReadMode.DISCARD;
    }
    catch (IOException e)
    {
      LOG.ignore(e);
      return ReadMode.EOF;
    }
    catch (Throwable t)
    {
      LOG.ignore(t);
    }
  }
  

  private ReadMode readParse(ByteBuffer buffer)
  {
    EndPoint endPoint = getEndPoint();
    
    try
    {
      for (;;)
      {
        int filled = endPoint.fill(buffer);
        if (filled < 0)
        {
          LOG.debug("read - EOF Reached (remote: {})", new Object[] { getRemoteAddress() });
          ioState.onReadFailure(new EOFException("Remote Read EOF"));
          return ReadMode.EOF;
        }
        if (filled == 0)
        {

          return ReadMode.PARSE;
        }
        
        if (LOG.isDebugEnabled())
        {
          LOG.debug("Filled {} bytes - {}", new Object[] { Integer.valueOf(filled), BufferUtil.toDetailString(buffer) });
        }
        parser.parse(buffer);
      }
      

















      return ReadMode.DISCARD;
    }
    catch (IOException e)
    {
      LOG.warn(e);
      close(1002, e.getMessage());
      return ReadMode.DISCARD;
    }
    catch (CloseException e)
    {
      LOG.debug(e);
      close(e.getStatusCode(), e.getMessage());
      return ReadMode.DISCARD;
    }
    catch (Throwable t)
    {
      LOG.warn(t);
      close(1006, t.getMessage());
    }
  }
  



  public void resume()
  {
    if (suspendToken.getAndSet(false))
    {
      if (!isReading())
      {
        fillInterested();
      }
    }
  }
  








  public void setExtensions(List<ExtensionConfig> extensions)
  {
    this.extensions = extensions;
  }
  

  public void setInputBufferSize(int inputBufferSize)
  {
    if (inputBufferSize < 28)
    {
      throw new IllegalArgumentException("Cannot have buffer size less than 28");
    }
    super.setInputBufferSize(inputBufferSize);
  }
  

  public void setMaxIdleTimeout(long ms)
  {
    getEndPoint().setIdleTimeout(ms);
  }
  

  public SuspendToken suspend()
  {
    suspendToken.set(true);
    return this;
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(toString()).append(System.lineSeparator());
  }
  

  public String toConnectionString()
  {
    return String.format("%s@%x[ios=%s,f=%s,g=%s,p=%s]", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), ioState, flusher, generator, parser });
  }
  


  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    
    EndPoint endp = getEndPoint();
    if (endp != null)
    {
      result = 31 * result + endp.getLocalAddress().hashCode();
      result = 31 * result + endp.getRemoteAddress().hashCode();
    }
    return result;
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractWebSocketConnection other = (AbstractWebSocketConnection)obj;
    EndPoint endp = getEndPoint();
    EndPoint otherEndp = other.getEndPoint();
    if (endp == null)
    {
      if (otherEndp != null) {
        return false;
      }
    } else if (!endp.equals(otherEndp))
      return false;
    return true;
  }
  






  public void onUpgradeTo(ByteBuffer prefilled)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("onUpgradeTo({})", new Object[] { BufferUtil.toDetailString(prefilled) });
    }
    
    setInitialBuffer(prefilled);
  }
}
