package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.util.BlockingArrayQueue;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
























public class AsyncNCSARequestLog
  extends NCSARequestLog
{
  private static final Logger LOG = Log.getLogger(AsyncNCSARequestLog.class);
  private final BlockingQueue<String> _queue;
  private transient WriterThread _thread;
  private boolean _warnedFull;
  
  public AsyncNCSARequestLog()
  {
    this(null, null);
  }
  
  public AsyncNCSARequestLog(BlockingQueue<String> queue)
  {
    this(null, queue);
  }
  
  public AsyncNCSARequestLog(String filename)
  {
    this(filename, null);
  }
  
  public AsyncNCSARequestLog(String filename, BlockingQueue<String> queue)
  {
    super(filename);
    if (queue == null)
      queue = new BlockingArrayQueue(1024);
    _queue = queue;
  }
  
  private class WriterThread extends Thread
  {
    WriterThread()
    {
      setName("AsyncNCSARequestLog@" + Integer.toString(hashCode(), 16));
    }
    

    public void run()
    {
      while (isRunning())
      {
        try
        {
          String log = (String)_queue.poll(10L, TimeUnit.SECONDS);
          if (log != null) {
            AsyncNCSARequestLog.this.write(log);
          }
          while (!_queue.isEmpty())
          {
            log = (String)_queue.poll();
            if (log != null) {
              AsyncNCSARequestLog.this.write(log);
            }
          }
        }
        catch (IOException e) {
          AsyncNCSARequestLog.LOG.warn(e);
        }
        catch (InterruptedException e)
        {
          AsyncNCSARequestLog.LOG.ignore(e);
        }
      }
    }
  }
  
  protected synchronized void doStart()
    throws Exception
  {
    super.doStart();
    _thread = new WriterThread();
    _thread.start();
  }
  
  protected void doStop()
    throws Exception
  {
    _thread.interrupt();
    _thread.join();
    super.doStop();
    _thread = null;
  }
  
  public void write(String log)
    throws IOException
  {
    if (!_queue.offer(log))
    {
      if (_warnedFull)
        LOG.warn("Log Queue overflow", new Object[0]);
      _warnedFull = true;
    }
  }
}
