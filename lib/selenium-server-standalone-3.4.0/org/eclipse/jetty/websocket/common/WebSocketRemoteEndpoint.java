package org.eclipse.jetty.websocket.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
import org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
import org.eclipse.jetty.websocket.common.frames.DataFrame;
import org.eclipse.jetty.websocket.common.frames.PingFrame;
import org.eclipse.jetty.websocket.common.frames.PongFrame;
import org.eclipse.jetty.websocket.common.frames.TextFrame;
import org.eclipse.jetty.websocket.common.io.FrameFlusher;
import org.eclipse.jetty.websocket.common.io.FutureWriteCallback;
import org.eclipse.jetty.websocket.common.io.IOState;






















public class WebSocketRemoteEndpoint
  implements RemoteEndpoint
{
  private static enum MsgType
  {
    BLOCKING, 
    ASYNC, 
    STREAMING, 
    PARTIAL_TEXT, 
    PARTIAL_BINARY;
    
    private MsgType() {} }
  private static final WriteCallback NOOP_CALLBACK = new WriteCallback()
  {
    public void writeSuccess() {}
    




    public void writeFailed(Throwable x) {}
  };
  


  private static final Logger LOG = Log.getLogger(WebSocketRemoteEndpoint.class);
  
  private static final int ASYNC_MASK = 65535;
  
  private static final int BLOCK_MASK = 65536;
  private static final int STREAM_MASK = 131072;
  private static final int PARTIAL_TEXT_MASK = 262144;
  private static final int PARTIAL_BINARY_MASK = 524288;
  private final LogicalConnection connection;
  private final OutgoingFrames outgoing;
  private final AtomicInteger msgState = new AtomicInteger();
  private final BlockingWriteCallback blocker = new BlockingWriteCallback();
  private volatile BatchMode batchMode;
  
  public WebSocketRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoing)
  {
    this(connection, outgoing, BatchMode.AUTO);
  }
  
  public WebSocketRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoing, BatchMode batchMode)
  {
    if (connection == null)
    {
      throw new IllegalArgumentException("LogicalConnection cannot be null");
    }
    this.connection = connection;
    this.outgoing = outgoing;
    this.batchMode = batchMode;
  }
  
  private void blockingWrite(WebSocketFrame frame) throws IOException
  {
    BlockingWriteCallback.WriteBlocker b = blocker.acquireWriteBlocker();Throwable localThrowable3 = null;
    try {
      uncheckedSendFrame(frame, b);
      b.block();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (b != null) { if (localThrowable3 != null) try { b.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { b.close();
        }
      }
    }
  }
  




  private boolean lockMsg(MsgType type)
  {
    for (;;)
    {
      int state = msgState.get();
      
      switch (2.$SwitchMap$org$eclipse$jetty$websocket$common$WebSocketRemoteEndpoint$MsgType[type.ordinal()])
      {
      case 1: 
        if ((state & 0xC0000) != 0)
          throw new IllegalStateException(String.format("Partial message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
        if ((state & 0x10000) != 0)
          throw new IllegalStateException(String.format("Blocking message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
        if (msgState.compareAndSet(state, state | 0x10000)) {
          return state == 0;
        }
        break;
      case 2: 
        if ((state & 0xC0000) != 0)
          throw new IllegalStateException(String.format("Partial message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
        if ((state & 0xFFFF) == 65535)
          throw new IllegalStateException(String.format("Too many async sends: %x", new Object[] { Integer.valueOf(state) }));
        if (msgState.compareAndSet(state, state + 1)) {
          return state == 0;
        }
        break;
      case 3: 
        if ((state & 0xC0000) != 0)
          throw new IllegalStateException(String.format("Partial message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
        if ((state & 0x20000) != 0)
          throw new IllegalStateException(String.format("Already streaming %x for %s", new Object[] { Integer.valueOf(state), type }));
        if (msgState.compareAndSet(state, state | 0x20000)) {
          return state == 0;
        }
        break;
      case 4: 
        if (state == 524288)
          return false;
        if (state == 0)
        {
          if (msgState.compareAndSet(0, state | 0x80000))
            return true;
        }
        throw new IllegalStateException(String.format("Cannot send %s in state %x", new Object[] { type, Integer.valueOf(state) }));
      
      case 5: 
        if (state == 262144)
          return false;
        if (state == 0)
        {
          if (msgState.compareAndSet(0, state | 0x40000))
            return true;
        }
        throw new IllegalStateException(String.format("Cannot send %s in state %x", new Object[] { type, Integer.valueOf(state) }));
      }
    }
  }
  
  private void unlockMsg(MsgType type)
  {
    for (;;)
    {
      int state = msgState.get();
      
      switch (2.$SwitchMap$org$eclipse$jetty$websocket$common$WebSocketRemoteEndpoint$MsgType[type.ordinal()])
      {
      case 1: 
        if ((state & 0x10000) == 0)
          throw new IllegalStateException(String.format("Not Blocking in state %x", new Object[] { Integer.valueOf(state) }));
        if (msgState.compareAndSet(state, state & 0xFFFEFFFF)) {
          return;
        }
        break;
      case 2: 
        if ((state & 0xFFFF) == 0)
          throw new IllegalStateException(String.format("Not Async in %x", new Object[] { Integer.valueOf(state) }));
        if (msgState.compareAndSet(state, state - 1)) {
          return;
        }
        break;
      case 3: 
        if ((state & 0x20000) == 0)
          throw new IllegalStateException(String.format("Not Streaming in state %x", new Object[] { Integer.valueOf(state) }));
        if (msgState.compareAndSet(state, state & 0xFFFDFFFF)) {
          return;
        }
        break;
      case 4: 
        if (msgState.compareAndSet(524288, 0))
          return;
        throw new IllegalStateException(String.format("Not Partial Binary in state %x", new Object[] { Integer.valueOf(state) }));
      
      case 5: 
        if (msgState.compareAndSet(262144, 0))
          return;
        throw new IllegalStateException(String.format("Not Partial Text in state %x", new Object[] { Integer.valueOf(state) }));
      }
      
    }
  }
  





  public InetSocketAddress getInetSocketAddress()
  {
    if (connection == null)
      return null;
    return connection.getRemoteAddress();
  }
  






  private Future<Void> sendAsyncFrame(WebSocketFrame frame)
  {
    FutureWriteCallback future = new FutureWriteCallback();
    uncheckedSendFrame(frame, future);
    return future;
  }
  



  public void sendBytes(ByteBuffer data)
    throws IOException
  {
    lockMsg(MsgType.BLOCKING);
    try
    {
      connection.getIOState().assertOutputOpen();
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendBytes with {}", new Object[] { BufferUtil.toDetailString(data) });
      }
      blockingWrite(new BinaryFrame().setPayload(data));
      


      unlockMsg(MsgType.BLOCKING); } finally { unlockMsg(MsgType.BLOCKING);
    }
  }
  

  public Future<Void> sendBytesByFuture(ByteBuffer data)
  {
    lockMsg(MsgType.ASYNC);
    try
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendBytesByFuture with {}", new Object[] { BufferUtil.toDetailString(data) });
      }
      return sendAsyncFrame(new BinaryFrame().setPayload(data));
    }
    finally
    {
      unlockMsg(MsgType.ASYNC);
    }
  }
  

  public void sendBytes(ByteBuffer data, WriteCallback callback)
  {
    lockMsg(MsgType.ASYNC);
    try
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendBytes({}, {})", new Object[] { BufferUtil.toDetailString(data), callback });
      }
      uncheckedSendFrame(new BinaryFrame().setPayload(data), callback == null ? NOOP_CALLBACK : callback);
      


      unlockMsg(MsgType.ASYNC); } finally { unlockMsg(MsgType.ASYNC);
    }
  }
  
  public void uncheckedSendFrame(WebSocketFrame frame, WriteCallback callback)
  {
    try
    {
      BatchMode batchMode = BatchMode.OFF;
      if (frame.isDataFrame())
        batchMode = getBatchMode();
      connection.getIOState().assertOutputOpen();
      outgoing.outgoingFrame(frame, callback, batchMode);
    }
    catch (IOException e)
    {
      callback.writeFailed(e);
    }
  }
  
  public void sendPartialBytes(ByteBuffer fragment, boolean isLast)
    throws IOException
  {
    boolean first = lockMsg(MsgType.PARTIAL_BINARY);
    try
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendPartialBytes({}, {})", new Object[] { BufferUtil.toDetailString(fragment), Boolean.valueOf(isLast) });
      }
      DataFrame frame = first ? new BinaryFrame() : new ContinuationFrame();
      frame.setPayload(fragment);
      frame.setFin(isLast);
      blockingWrite(frame);
    }
    finally
    {
      if (isLast) {
        unlockMsg(MsgType.PARTIAL_BINARY);
      }
    }
  }
  
  public void sendPartialString(String fragment, boolean isLast) throws IOException
  {
    boolean first = lockMsg(MsgType.PARTIAL_TEXT);
    try
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendPartialString({}, {})", new Object[] { fragment, Boolean.valueOf(isLast) });
      }
      DataFrame frame = first ? new TextFrame() : new ContinuationFrame();
      frame.setPayload(BufferUtil.toBuffer(fragment, StandardCharsets.UTF_8));
      frame.setFin(isLast);
      blockingWrite(frame);
    }
    finally
    {
      if (isLast) {
        unlockMsg(MsgType.PARTIAL_TEXT);
      }
    }
  }
  
  public void sendPing(ByteBuffer applicationData) throws IOException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("sendPing with {}", new Object[] { BufferUtil.toDetailString(applicationData) });
    }
    sendAsyncFrame(new PingFrame().setPayload(applicationData));
  }
  
  public void sendPong(ByteBuffer applicationData)
    throws IOException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("sendPong with {}", new Object[] { BufferUtil.toDetailString(applicationData) });
    }
    sendAsyncFrame(new PongFrame().setPayload(applicationData));
  }
  
  public void sendString(String text)
    throws IOException
  {
    lockMsg(MsgType.BLOCKING);
    try
    {
      WebSocketFrame frame = new TextFrame().setPayload(text);
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendString with {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
      }
      blockingWrite(frame);
      


      unlockMsg(MsgType.BLOCKING); } finally { unlockMsg(MsgType.BLOCKING);
    }
  }
  

  public Future<Void> sendStringByFuture(String text)
  {
    lockMsg(MsgType.ASYNC);
    try
    {
      TextFrame frame = new TextFrame().setPayload(text);
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendStringByFuture with {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
      }
      return sendAsyncFrame(frame);
    }
    finally
    {
      unlockMsg(MsgType.ASYNC);
    }
  }
  

  public void sendString(String text, WriteCallback callback)
  {
    lockMsg(MsgType.ASYNC);
    try
    {
      TextFrame frame = new TextFrame().setPayload(text);
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sendString({},{})", new Object[] { BufferUtil.toDetailString(frame.getPayload()), callback });
      }
      uncheckedSendFrame(frame, callback == null ? NOOP_CALLBACK : callback);
    }
    finally
    {
      unlockMsg(MsgType.ASYNC);
    }
  }
  

  public BatchMode getBatchMode()
  {
    return batchMode;
  }
  

  public void setBatchMode(BatchMode batchMode)
  {
    this.batchMode = batchMode;
  }
  
  public void flush()
    throws IOException
  {
    lockMsg(MsgType.ASYNC);
    try { BlockingWriteCallback.WriteBlocker b = blocker.acquireWriteBlocker();Throwable localThrowable3 = null;
      try {
        uncheckedSendFrame(FrameFlusher.FLUSH_FRAME, b);
        b.block();
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (b != null) if (localThrowable3 != null) try { b.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else b.close();
      }
    } finally {
      unlockMsg(MsgType.ASYNC);
    }
  }
  

  public String toString()
  {
    return String.format("%s@%x[batching=%b]", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), getBatchMode() });
  }
}
