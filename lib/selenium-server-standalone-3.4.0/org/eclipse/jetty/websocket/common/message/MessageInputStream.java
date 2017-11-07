package org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;























public class MessageInputStream
  extends InputStream
  implements MessageAppender
{
  private static final Logger LOG = Log.getLogger(MessageInputStream.class);
  private static final ByteBuffer EOF = ByteBuffer.allocate(0).asReadOnlyBuffer();
  
  private final BlockingDeque<ByteBuffer> buffers = new LinkedBlockingDeque();
  private AtomicBoolean closed = new AtomicBoolean(false);
  private final long timeoutMs;
  private ByteBuffer activeBuffer = null;
  
  public MessageInputStream()
  {
    this(-1);
  }
  
  public MessageInputStream(int timeoutMs)
  {
    this.timeoutMs = timeoutMs;
  }
  
  public void appendFrame(ByteBuffer framePayload, boolean fin)
    throws IOException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Appending {} chunk: {}", new Object[] { fin ? "final" : "non-final", BufferUtil.toDetailString(framePayload) });
    }
    

    if (closed.get())
    {
      return;
    }
    



    try
    {
      if (framePayload == null)
      {

        return;
      }
      
      int capacity = framePayload.remaining();
      if (capacity <= 0)
      {

        return;
      }
      
      ByteBuffer copy = framePayload.isDirect() ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
      copy.put(framePayload).flip();
      buffers.put(copy);
    }
    catch (InterruptedException e)
    {
      throw new IOException(e);
    }
    finally
    {
      if (fin)
      {
        buffers.offer(EOF);
      }
    }
  }
  
  public void close()
    throws IOException
  {
    if (closed.compareAndSet(false, true))
    {
      buffers.offer(EOF);
      super.close();
    }
  }
  



  public void mark(int readlimit) {}
  


  public boolean markSupported()
  {
    return false;
  }
  

  public void messageComplete()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Message completed", new Object[0]);
    buffers.offer(EOF);
  }
  
  public int read()
    throws IOException
  {
    try
    {
      if (closed.get())
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Stream closed", new Object[0]);
        return -1;
      }
      

      while ((activeBuffer == null) || (!activeBuffer.hasRemaining()))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Waiting {} ms to read", timeoutMs);
        if (timeoutMs < 0L)
        {

          activeBuffer = ((ByteBuffer)buffers.take());

        }
        else
        {
          activeBuffer = ((ByteBuffer)buffers.poll(timeoutMs, TimeUnit.MILLISECONDS));
          if (activeBuffer == null)
          {
            throw new IOException(String.format("Read timeout: %,dms expired", new Object[] { Long.valueOf(timeoutMs) }));
          }
        }
        
        if (activeBuffer == EOF)
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Reached EOF", new Object[0]);
          }
          closed.set(true);
          
          buffers.clear();
          return -1;
        }
      }
      
      return activeBuffer.get() & 0xFF;
    }
    catch (InterruptedException x)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Interrupted while waiting to read", x);
      closed.set(true); }
    return -1;
  }
  

  public void reset()
    throws IOException
  {
    throw new IOException("reset() not supported");
  }
}
