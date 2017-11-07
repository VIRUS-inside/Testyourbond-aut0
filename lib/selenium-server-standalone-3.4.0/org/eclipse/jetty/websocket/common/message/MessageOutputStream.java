package org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.BlockingWriteCallback;
import org.eclipse.jetty.websocket.common.BlockingWriteCallback.WriteBlocker;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.eclipse.jetty.websocket.common.frames.BinaryFrame;





















public class MessageOutputStream
  extends OutputStream
{
  private static final Logger LOG = Log.getLogger(MessageOutputStream.class);
  
  private final OutgoingFrames outgoing;
  private final ByteBufferPool bufferPool;
  private final BlockingWriteCallback blocker;
  private long frameCount;
  private BinaryFrame frame;
  private ByteBuffer buffer;
  private WriteCallback callback;
  private boolean closed;
  
  public MessageOutputStream(WebSocketSession session)
  {
    this(session.getOutgoingHandler(), session.getPolicy().getMaxBinaryMessageBufferSize(), session.getBufferPool());
  }
  
  public MessageOutputStream(OutgoingFrames outgoing, int bufferSize, ByteBufferPool bufferPool)
  {
    this.outgoing = outgoing;
    this.bufferPool = bufferPool;
    blocker = new BlockingWriteCallback();
    buffer = bufferPool.acquire(bufferSize, true);
    BufferUtil.flipToFill(buffer);
    frame = new BinaryFrame();
  }
  
  public void write(byte[] bytes, int off, int len)
    throws IOException
  {
    try
    {
      send(bytes, off, len);

    }
    catch (Throwable x)
    {
      notifyFailure(x);
      throw x;
    }
  }
  
  public void write(int b)
    throws IOException
  {
    try
    {
      send(new byte[] { (byte)b }, 0, 1);

    }
    catch (Throwable x)
    {
      notifyFailure(x);
      throw x;
    }
  }
  
  public void flush()
    throws IOException
  {
    try
    {
      flush(false);

    }
    catch (Throwable x)
    {
      notifyFailure(x);
      throw x;
    }
  }
  
  public void close()
    throws IOException
  {
    try
    {
      flush(true);
      bufferPool.release(buffer);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Stream closed, {} frames sent", frameCount);
      }
      notifySuccess();

    }
    catch (Throwable x)
    {
      notifyFailure(x);
      throw x;
    }
  }
  
  private void flush(boolean fin) throws IOException
  {
    synchronized (this)
    {
      if (closed) {
        throw new IOException("Stream is closed");
      }
      closed = fin;
      
      BufferUtil.flipToFlush(buffer, 0);
      frame.setPayload(buffer);
      frame.setFin(fin);
      
      BlockingWriteCallback.WriteBlocker b = blocker.acquireWriteBlocker();Throwable localThrowable3 = null;
      try {
        outgoing.outgoingFrame(frame, b, BatchMode.OFF);
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
      frameCount += 1L;
      
      frame.setIsContinuation();
      
      BufferUtil.flipToFill(buffer);
    }
  }
  
  private void send(byte[] bytes, int offset, int length) throws IOException
  {
    synchronized (this)
    {
      if (closed) {
        throw new IOException("Stream is closed");
      }
      while (length > 0)
      {


        int space = buffer.remaining();
        int size = Math.min(space, length);
        buffer.put(bytes, offset, size);
        offset += size;
        length -= size;
        if (length > 0)
        {


          flush(false);
        }
      }
    }
  }
  
  public void setCallback(WriteCallback callback)
  {
    synchronized (this)
    {
      this.callback = callback;
    }
  }
  
  private void notifySuccess()
  {
    WriteCallback callback;
    synchronized (this)
    {
      callback = this.callback; }
    WriteCallback callback;
    if (callback != null)
    {
      callback.writeSuccess();
    }
  }
  
  private void notifyFailure(Throwable failure)
  {
    WriteCallback callback;
    synchronized (this)
    {
      callback = this.callback; }
    WriteCallback callback;
    if (callback != null)
    {
      callback.writeFailed(failure);
    }
  }
}
