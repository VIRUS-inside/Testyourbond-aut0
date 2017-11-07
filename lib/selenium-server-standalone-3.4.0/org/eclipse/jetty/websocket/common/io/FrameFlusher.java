package org.eclipse.jetty.websocket.common.io;

import java.io.EOFException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.IteratingCallback;
import org.eclipse.jetty.util.IteratingCallback.Action;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.Generator;
import org.eclipse.jetty.websocket.common.frames.BinaryFrame;





















public class FrameFlusher
{
  private class Flusher
    extends IteratingCallback
  {
    private final List<FrameFlusher.FrameEntry> entries;
    private final List<ByteBuffer> buffers;
    private ByteBuffer aggregate;
    private BatchMode batchMode;
    
    public Flusher(int maxGather)
    {
      entries = new ArrayList(maxGather);
      buffers = new ArrayList(maxGather * 2 + 1);
    }
    
    private IteratingCallback.Action batch()
    {
      if (aggregate == null)
      {
        aggregate = bufferPool.acquire(bufferSize, true);
        if (FrameFlusher.LOG.isDebugEnabled())
        {
          FrameFlusher.LOG.debug("{} acquired aggregate buffer {}", new Object[] { FrameFlusher.this, aggregate });
        }
      }
      

      for (int i = 0; i < entries.size(); i++)
      {
        FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry)entries.get(i);
        
        entry.generateHeaderBytes(aggregate);
        
        ByteBuffer payload = frame.getPayload();
        if (BufferUtil.hasContent(payload))
        {
          BufferUtil.append(aggregate, payload);
        }
      }
      if (FrameFlusher.LOG.isDebugEnabled())
      {
        FrameFlusher.LOG.debug("{} aggregated {} frames: {}", new Object[] { FrameFlusher.this, Integer.valueOf(entries.size()), entries });
      }
      succeeded();
      return IteratingCallback.Action.SCHEDULED;
    }
    



    protected void onCompleteSuccess() {}
    


    public void onCompleteFailure(Throwable x)
    {
      for (FrameFlusher.FrameEntry entry : entries)
      {
        notifyCallbackFailure(callback, x);
        entry.release();
      }
      entries.clear();
      failure = x;
      onFailure(x);
    }
    
    private IteratingCallback.Action flush()
    {
      if (!BufferUtil.isEmpty(aggregate))
      {
        buffers.add(aggregate);
        if (FrameFlusher.LOG.isDebugEnabled())
        {
          FrameFlusher.LOG.debug("{} flushing aggregate {}", new Object[] { FrameFlusher.this, aggregate });
        }
      }
      

      for (int i = 0; i < entries.size(); i++)
      {
        FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry)entries.get(i);
        
        if (frame != FrameFlusher.FLUSH_FRAME)
        {


          buffers.add(entry.generateHeaderBytes());
          ByteBuffer payload = frame.getPayload();
          if (BufferUtil.hasContent(payload))
          {
            buffers.add(payload);
          }
        }
      }
      if (FrameFlusher.LOG.isDebugEnabled())
      {
        FrameFlusher.LOG.debug("{} flushing {} frames: {}", new Object[] { FrameFlusher.this, Integer.valueOf(entries.size()), entries });
      }
      
      if (buffers.isEmpty())
      {
        releaseAggregate();
        
        succeedEntries();
        return IteratingCallback.Action.IDLE;
      }
      
      endpoint.write(this, (ByteBuffer[])buffers.toArray(new ByteBuffer[buffers.size()]));
      buffers.clear();
      return IteratingCallback.Action.SCHEDULED;
    }
    
    protected IteratingCallback.Action process()
      throws Exception
    {
      int space = aggregate == null ? bufferSize : BufferUtil.space(aggregate);
      BatchMode currentBatchMode = BatchMode.AUTO;
      synchronized (lock)
      {
        while ((entries.size() <= maxGather) && (!queue.isEmpty()))
        {
          FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry)queue.poll();
          currentBatchMode = BatchMode.max(currentBatchMode, batchMode);
          

          if (frame == FrameFlusher.FLUSH_FRAME)
          {
            currentBatchMode = BatchMode.OFF;
          }
          
          int payloadLength = BufferUtil.length(frame.getPayload());
          int approxFrameLength = 28 + payloadLength;
          

          if (approxFrameLength > bufferSize >> 2)
          {
            currentBatchMode = BatchMode.OFF;
          }
          

          space -= approxFrameLength;
          if (space <= 0)
          {
            currentBatchMode = BatchMode.OFF;
          }
          
          entries.add(entry);
        }
      }
      
      if (FrameFlusher.LOG.isDebugEnabled())
      {
        FrameFlusher.LOG.debug("{} processing {} entries: {}", new Object[] { FrameFlusher.this, Integer.valueOf(entries.size()), entries });
      }
      
      if (entries.isEmpty())
      {
        if (batchMode != BatchMode.AUTO)
        {


          releaseAggregate();
          return IteratingCallback.Action.IDLE;
        }
        
        FrameFlusher.LOG.debug("{} auto flushing", new Object[] { FrameFlusher.this });
        return flush();
      }
      
      batchMode = currentBatchMode;
      
      return currentBatchMode == BatchMode.OFF ? flush() : batch();
    }
    
    private void releaseAggregate()
    {
      if ((aggregate != null) && (BufferUtil.isEmpty(aggregate)))
      {
        bufferPool.release(aggregate);
        aggregate = null;
      }
    }
    

    public void succeeded()
    {
      succeedEntries();
      super.succeeded();
    }
    

    private void succeedEntries()
    {
      for (int i = 0; i < entries.size(); i++)
      {
        FrameFlusher.FrameEntry entry = (FrameFlusher.FrameEntry)entries.get(i);
        notifyCallbackSuccess(callback);
        entry.release();
      }
      entries.clear();
    }
  }
  
  private class FrameEntry
  {
    private final Frame frame;
    private final WriteCallback callback;
    private final BatchMode batchMode;
    private ByteBuffer headerBuffer;
    
    private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode)
    {
      this.frame = ((Frame)Objects.requireNonNull(frame));
      this.callback = callback;
      this.batchMode = batchMode;
    }
    
    private ByteBuffer generateHeaderBytes()
    {
      return this.headerBuffer = generator.generateHeaderBytes(frame);
    }
    
    private void generateHeaderBytes(ByteBuffer buffer)
    {
      generator.generateHeaderBytes(frame, buffer);
    }
    
    private void release()
    {
      if (headerBuffer != null)
      {
        generator.getBufferPool().release(headerBuffer);
        headerBuffer = null;
      }
    }
    

    public String toString()
    {
      return String.format("%s[%s,%s,%s,%s]", new Object[] { getClass().getSimpleName(), frame, callback, batchMode, failure });
    }
  }
  
  public static final BinaryFrame FLUSH_FRAME = new BinaryFrame();
  private static final Logger LOG = Log.getLogger(FrameFlusher.class);
  private final ByteBufferPool bufferPool;
  private final EndPoint endpoint;
  private final int bufferSize;
  private final Generator generator;
  private final int maxGather;
  private final Object lock = new Object();
  private final Deque<FrameEntry> queue = new ArrayDeque();
  private final Flusher flusher;
  private final AtomicBoolean closed = new AtomicBoolean();
  private volatile Throwable failure;
  
  public FrameFlusher(ByteBufferPool bufferPool, Generator generator, EndPoint endpoint, int bufferSize, int maxGather)
  {
    this.bufferPool = bufferPool;
    this.endpoint = endpoint;
    this.bufferSize = bufferSize;
    this.generator = ((Generator)Objects.requireNonNull(generator));
    this.maxGather = maxGather;
    flusher = new Flusher(maxGather);
  }
  
  public void close() {
    EOFException eof;
    if (closed.compareAndSet(false, true))
    {
      LOG.debug("{} closing {}", new Object[] { this });
      eof = new EOFException("Connection has been closed locally");
      flusher.failed(eof);
      

      List<FrameEntry> entries = new ArrayList();
      synchronized (lock)
      {
        entries.addAll(queue);
        queue.clear();
      }
      
      for (??? = entries.iterator(); ???.hasNext();) { FrameEntry entry = (FrameEntry)???.next();
        
        notifyCallbackFailure(callback, eof);
      }
    }
  }
  
  public void enqueue(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    if (closed.get())
    {
      notifyCallbackFailure(callback, new EOFException("Connection has been closed locally"));
      return;
    }
    if (flusher.isFailed())
    {
      notifyCallbackFailure(callback, failure);
      return;
    }
    
    FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
    
    synchronized (lock)
    {
      switch (frame.getOpCode())
      {


      case 9: 
        queue.offerFirst(entry);
        break;
      




      case 8: 
        closed.set(true);
        queue.offer(entry);
        break;
      

      default: 
        queue.offer(entry);
      }
      
    }
    

    if (LOG.isDebugEnabled())
    {
      LOG.debug("{} queued {}", new Object[] { this, entry });
    }
    
    flusher.iterate();
  }
  
  protected void notifyCallbackFailure(WriteCallback callback, Throwable failure)
  {
    try
    {
      if (callback != null)
      {
        callback.writeFailed(failure);
      }
    }
    catch (Throwable x)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exception while notifying failure of callback " + callback, x);
      }
    }
  }
  
  protected void notifyCallbackSuccess(WriteCallback callback)
  {
    try {
      if (callback != null)
      {
        callback.writeSuccess();
      }
    }
    catch (Throwable x)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exception while notifying success of callback " + callback, x);
      }
    }
  }
  
  protected void onFailure(Throwable x) {
    LOG.warn(x);
  }
  

  public String toString()
  {
    ByteBuffer aggregate = flusher.aggregate;
    return String.format("%s[queueSize=%d,aggregateSize=%d,failure=%s]", new Object[] { getClass().getSimpleName(), Integer.valueOf(queue.size()), Integer.valueOf(aggregate == null ? 0 : aggregate.position()), failure });
  }
}
