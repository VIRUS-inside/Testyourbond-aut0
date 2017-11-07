package org.eclipse.jetty.io;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.jetty.util.BufferUtil;











































public abstract interface ByteBufferPool
{
  public abstract ByteBuffer acquire(int paramInt, boolean paramBoolean);
  
  public abstract void release(ByteBuffer paramByteBuffer);
  
  public ByteBuffer newByteBuffer(int capacity, boolean direct)
  {
    return direct ? BufferUtil.allocateDirect(capacity) : BufferUtil.allocate(capacity);
  }
  
  public static class Lease
  {
    private final ByteBufferPool byteBufferPool;
    private final List<ByteBuffer> buffers;
    private final List<Boolean> recycles;
    
    public Lease(ByteBufferPool byteBufferPool)
    {
      this.byteBufferPool = byteBufferPool;
      buffers = new ArrayList();
      recycles = new ArrayList();
    }
    
    public ByteBuffer acquire(int capacity, boolean direct)
    {
      ByteBuffer buffer = byteBufferPool.acquire(capacity, direct);
      BufferUtil.clearToFill(buffer);
      return buffer;
    }
    
    public void append(ByteBuffer buffer, boolean recycle)
    {
      buffers.add(buffer);
      recycles.add(Boolean.valueOf(recycle));
    }
    
    public void insert(int index, ByteBuffer buffer, boolean recycle)
    {
      buffers.add(index, buffer);
      recycles.add(index, Boolean.valueOf(recycle));
    }
    
    public List<ByteBuffer> getByteBuffers()
    {
      return buffers;
    }
    
    public long getTotalLength()
    {
      long length = 0L;
      for (int i = 0; i < buffers.size(); i++)
        length += ((ByteBuffer)buffers.get(i)).remaining();
      return length;
    }
    
    public int getSize()
    {
      return buffers.size();
    }
    
    public void recycle()
    {
      for (int i = 0; i < buffers.size(); i++)
      {
        ByteBuffer buffer = (ByteBuffer)buffers.get(i);
        if (((Boolean)recycles.get(i)).booleanValue())
          byteBufferPool.release(buffer);
      }
      buffers.clear();
      recycles.clear();
    }
  }
  
  public static class Bucket
  {
    private final Lock _lock = new ReentrantLock();
    private final Queue<ByteBuffer> _queue = new ArrayDeque();
    private final ByteBufferPool _pool;
    private final int _capacity;
    private final AtomicInteger _space;
    
    public Bucket(ByteBufferPool pool, int bufferSize, int maxSize)
    {
      _pool = pool;
      _capacity = bufferSize;
      _space = (maxSize > 0 ? new AtomicInteger(maxSize) : null);
    }
    
    public ByteBuffer acquire(boolean direct)
    {
      ByteBuffer buffer = queuePoll();
      if (buffer == null)
        return _pool.newByteBuffer(_capacity, direct);
      if (_space != null)
        _space.incrementAndGet();
      return buffer;
    }
    
    public void release(ByteBuffer buffer)
    {
      BufferUtil.clear(buffer);
      if (_space == null) {
        queueOffer(buffer);
      } else if (_space.decrementAndGet() >= 0) {
        queueOffer(buffer);
      } else {
        _space.incrementAndGet();
      }
    }
    
    public void clear() {
      if (_space == null)
      {
        queueClear();
      }
      else
      {
        int s = _space.getAndSet(0);
        while (s-- > 0)
        {
          if (queuePoll() == null) {
            _space.incrementAndGet();
          }
        }
      }
    }
    
    private void queueOffer(ByteBuffer buffer) {
      Lock lock = _lock;
      lock.lock();
      try
      {
        _queue.offer(buffer);
        


        lock.unlock(); } finally { lock.unlock();
      }
    }
    
    private ByteBuffer queuePoll()
    {
      Lock lock = _lock;
      lock.lock();
      try
      {
        return (ByteBuffer)_queue.poll();
      }
      finally
      {
        lock.unlock();
      }
    }
    
    private void queueClear()
    {
      Lock lock = _lock;
      lock.lock();
      try
      {
        _queue.clear();
        


        lock.unlock(); } finally { lock.unlock();
      }
    }
    
    boolean isEmpty()
    {
      Lock lock = _lock;
      lock.lock();
      try
      {
        return _queue.isEmpty();
      }
      finally
      {
        lock.unlock();
      }
    }
    
    int size()
    {
      Lock lock = _lock;
      lock.lock();
      try
      {
        return _queue.size();
      }
      finally
      {
        lock.unlock();
      }
    }
    

    public String toString()
    {
      return String.format("Bucket@%x{%d/%d}", new Object[] { Integer.valueOf(hashCode()), Integer.valueOf(size()), Integer.valueOf(_capacity) });
    }
  }
}
