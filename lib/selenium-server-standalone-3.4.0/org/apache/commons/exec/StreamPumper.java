package org.apache.commons.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.exec.util.DebugUtils;










































public class StreamPumper
  implements Runnable
{
  private static final int DEFAULT_SIZE = 1024;
  private final InputStream is;
  private final OutputStream os;
  private final int size;
  private boolean finished;
  private final boolean closeWhenExhausted;
  
  public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted)
  {
    this.is = is;
    this.os = os;
    size = 1024;
    this.closeWhenExhausted = closeWhenExhausted;
  }
  








  public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted, int size)
  {
    this.is = is;
    this.os = os;
    this.size = (size > 0 ? size : 1024);
    this.closeWhenExhausted = closeWhenExhausted;
  }
  





  public StreamPumper(InputStream is, OutputStream os)
  {
    this(is, os, false);
  }
  



  public void run()
  {
    synchronized (this)
    {
      finished = false;
    }
    
    byte[] buf = new byte[size];
    try
    {
      int length;
      while ((length = is.read(buf)) > 0) {
        os.write(buf, 0, length);
      }
    } catch (Exception e) {}finally {
      String msg;
      String msg;
      if (closeWhenExhausted) {
        try {
          os.close();
        } catch (IOException e) {
          String msg = "Got exception while closing exhausted output stream";
          DebugUtils.handleException("Got exception while closing exhausted output stream", e);
        }
      }
      synchronized (this) {
        finished = true;
        notifyAll();
      }
    }
  }
  




  public synchronized boolean isFinished()
  {
    return finished;
  }
  






  public synchronized void waitFor()
    throws InterruptedException
  {
    while (!isFinished()) {
      wait();
    }
  }
}
