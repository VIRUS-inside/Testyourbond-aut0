package org.eclipse.jetty.io;

import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.Locker;
import org.eclipse.jetty.util.thread.Locker.Lock;
import org.eclipse.jetty.util.thread.Scheduler;






















public class ByteArrayEndPoint
  extends AbstractEndPoint
{
  static final Logger LOG = Log.getLogger(ByteArrayEndPoint.class);
  static final InetAddress NOIP;
  static final InetSocketAddress NOIPPORT;
  
  static
  {
    InetAddress noip = null;
    try
    {
      noip = Inet4Address.getByName("0.0.0.0");
    }
    catch (UnknownHostException e)
    {
      LOG.warn(e);
    }
    finally
    {
      NOIP = noip;
      NOIPPORT = new InetSocketAddress(NOIP, 0);
    }
  }
  

  private static final ByteBuffer EOF = BufferUtil.allocate(0);
  
  private final Runnable _runFillable = new Runnable()
  {

    public void run()
    {
      getFillInterest().fillable();
    }
  };
  
  private final Locker _locker = new Locker();
  private final Condition _hasOutput = _locker.newCondition();
  private final Queue<ByteBuffer> _inQ = new ArrayDeque();
  

  private ByteBuffer _out;
  
  private boolean _growOutput;
  

  public ByteArrayEndPoint()
  {
    this(null, 0L, null, null);
  }
  





  public ByteArrayEndPoint(byte[] input, int outputSize)
  {
    this(null, 0L, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
  }
  





  public ByteArrayEndPoint(String input, int outputSize)
  {
    this(null, 0L, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
  }
  

  public ByteArrayEndPoint(Scheduler scheduler, long idleTimeoutMs)
  {
    this(scheduler, idleTimeoutMs, null, null);
  }
  

  public ByteArrayEndPoint(Scheduler timer, long idleTimeoutMs, byte[] input, int outputSize)
  {
    this(timer, idleTimeoutMs, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
  }
  

  public ByteArrayEndPoint(Scheduler timer, long idleTimeoutMs, String input, int outputSize)
  {
    this(timer, idleTimeoutMs, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
  }
  

  public ByteArrayEndPoint(Scheduler timer, long idleTimeoutMs, ByteBuffer input, ByteBuffer output)
  {
    super(timer);
    if (BufferUtil.hasContent(input))
      addInput(input);
    _out = (output == null ? BufferUtil.allocate(1024) : output);
    setIdleTimeout(idleTimeoutMs);
    onOpen();
  }
  


  public void doShutdownOutput()
  {
    super.doShutdownOutput();
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      _hasOutput.signalAll();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
    }
  }
  
  public void doClose()
  {
    super.doClose();
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      _hasOutput.signalAll();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
    }
  }
  
  public InetSocketAddress getLocalAddress()
  {
    return NOIPPORT;
  }
  


  public InetSocketAddress getRemoteAddress()
  {
    return NOIPPORT;
  }
  








  protected void execute(Runnable task)
  {
    new Thread(task, "BAEPoint-" + Integer.toHexString(hashCode())).start();
  }
  

  protected void needsFillInterest()
    throws IOException
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (!isOpen()) {
        throw new ClosedChannelException();
      }
      ByteBuffer in = (ByteBuffer)_inQ.peek();
      if ((BufferUtil.hasContent(in)) || (in == EOF)) {
        execute(_runFillable);
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


    }
    finally
    {


      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  
  public void addInputEOF()
  {
    addInput((ByteBuffer)null);
  }
  




  public void addInput(ByteBuffer in)
  {
    boolean fillable = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (_inQ.peek() == EOF)
        throw new RuntimeIOException(new EOFException());
      boolean was_empty = _inQ.isEmpty();
      if (in == null)
      {
        _inQ.add(EOF);
        fillable = true;
      }
      if (BufferUtil.hasContent(in))
      {
        _inQ.add(in);
        fillable = was_empty;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;






    }
    finally
    {





      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    if (fillable) {
      _runFillable.run();
    }
  }
  
  public void addInputAndExecute(ByteBuffer in)
  {
    boolean fillable = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (_inQ.peek() == EOF)
        throw new RuntimeIOException(new EOFException());
      boolean was_empty = _inQ.isEmpty();
      if (in == null)
      {
        _inQ.add(EOF);
        fillable = true;
      }
      if (BufferUtil.hasContent(in))
      {
        _inQ.add(in);
        fillable = was_empty;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;






    }
    finally
    {





      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    if (fillable) {
      execute(_runFillable);
    }
  }
  
  public void addInput(String s)
  {
    addInput(BufferUtil.toBuffer(s, StandardCharsets.UTF_8));
  }
  

  public void addInput(String s, Charset charset)
  {
    addInput(BufferUtil.toBuffer(s, charset));
  }
  




  public ByteBuffer getOutput()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _out;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  

  public String getOutputString()
  {
    return getOutputString(StandardCharsets.UTF_8);
  }
  





  public String getOutputString(Charset charset)
  {
    return BufferUtil.toString(_out, charset);
  }
  






  public ByteBuffer takeOutput()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      ByteBuffer b = _out;
      _out = BufferUtil.allocate(b.capacity());
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    ByteBuffer b; getWriteFlusher().completeWrite();
    return b;
  }
  








  public ByteBuffer waitForOutput(long time, TimeUnit unit)
    throws InterruptedException
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      while ((BufferUtil.isEmpty(_out)) && (!isOutputShutdown()))
      {
        _hasOutput.await(time, unit);
      }
      ByteBuffer b = _out;
      _out = BufferUtil.allocate(b.capacity());
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


    }
    finally
    {


      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    ByteBuffer b; getWriteFlusher().completeWrite();
    return b;
  }
  




  public String takeOutputString()
  {
    return takeOutputString(StandardCharsets.UTF_8);
  }
  





  public String takeOutputString(Charset charset)
  {
    ByteBuffer buffer = takeOutput();
    return BufferUtil.toString(buffer, charset);
  }
  




  public void setOutput(ByteBuffer out)
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      _out = out;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    getWriteFlusher().completeWrite();
  }
  




  public boolean hasMore()
  {
    return getOutput().position() > 0;
  }
  




  public int fill(ByteBuffer buffer)
    throws IOException
  {
    int filled = 0;
    Locker.Lock lock = _locker.lock();Throwable localThrowable4 = null;
    try
    {
      for (;;) {
        if (!isOpen()) {
          throw new EofException("CLOSED");
        }
        if (isInputShutdown()) {
          return -1;
        }
        if (_inQ.isEmpty()) {
          break;
        }
        ByteBuffer in = (ByteBuffer)_inQ.peek();
        if (in == EOF)
        {
          filled = -1;
          break;
        }
        
        if (BufferUtil.hasContent(in))
        {
          filled = BufferUtil.append(buffer, in);
          if (!BufferUtil.isEmpty(in)) break;
          _inQ.poll(); break;
        }
        
        _inQ.poll();
      }
    }
    catch (Throwable localThrowable6)
    {
      localThrowable4 = localThrowable6;throw localThrowable6;













    }
    finally
    {












      if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else lock.close();
    }
    if (filled > 0) {
      notIdle();
    } else if (filled < 0)
      shutdownInput();
    return filled;
  }
  




  public boolean flush(ByteBuffer... buffers)
    throws IOException
  {
    boolean flushed = true;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (!isOpen())
        throw new IOException("CLOSED");
      if (isOutputShutdown()) {
        throw new IOException("OSHUT");
      }
      boolean idle = true;
      
      for (ByteBuffer b : buffers)
      {
        if (BufferUtil.hasContent(b))
        {
          if ((_growOutput) && (b.remaining() > BufferUtil.space(_out)))
          {
            BufferUtil.compact(_out);
            if (b.remaining() > BufferUtil.space(_out))
            {
              ByteBuffer n = BufferUtil.allocate(_out.capacity() + b.remaining() * 2);
              BufferUtil.append(n, _out);
              _out = n;
            }
          }
          
          if (BufferUtil.append(_out, b) > 0) {
            idle = false;
          }
          if (BufferUtil.hasContent(b))
          {
            flushed = false;
            break;
          }
        }
      }
      if (!idle)
      {
        notIdle();
        _hasOutput.signalAll();
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


















    }
    finally
    {

















      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    return flushed;
  }
  




  public void reset()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      _inQ.clear();
      _hasOutput.signalAll();
      BufferUtil.clear(_out);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    super.reset();
  }
  





  public Object getTransport()
  {
    return null;
  }
  




  public boolean isGrowOutput()
  {
    return _growOutput;
  }
  




  public void setGrowOutput(boolean growOutput)
  {
    _growOutput = growOutput;
  }
  





  public String toString()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      int q = _inQ.size();
      ByteBuffer b = (ByteBuffer)_inQ.peek();
      o = BufferUtil.toDetailString(_out);
    }
    catch (Throwable localThrowable1)
    {
      String o;
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    String o; ByteBuffer b; int q; return String.format("%s[q=%d,q[0]=%s,o=%s]", new Object[] { super.toString(), Integer.valueOf(q), b, o });
  }
  
  protected void onIncompleteFlush() {}
}
