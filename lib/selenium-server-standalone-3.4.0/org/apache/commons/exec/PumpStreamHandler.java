package org.apache.commons.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import org.apache.commons.exec.util.DebugUtils;





































public class PumpStreamHandler
  implements ExecuteStreamHandler
{
  private static final long STOP_TIMEOUT_ADDITION = 2000L;
  private Thread outputThread;
  private Thread errorThread;
  private Thread inputThread;
  private final OutputStream out;
  private final OutputStream err;
  private final InputStream input;
  private InputStreamPumper inputStreamPumper;
  private long stopTimeout;
  private IOException caught = null;
  


  public PumpStreamHandler()
  {
    this(System.out, System.err);
  }
  




  public PumpStreamHandler(OutputStream outAndErr)
  {
    this(outAndErr, outAndErr);
  }
  





  public PumpStreamHandler(OutputStream out, OutputStream err)
  {
    this(out, err, null);
  }
  






  public PumpStreamHandler(OutputStream out, OutputStream err, InputStream input)
  {
    this.out = out;
    this.err = err;
    this.input = input;
  }
  





  public void setStopTimeout(long timeout)
  {
    stopTimeout = timeout;
  }
  





  public void setProcessOutputStream(InputStream is)
  {
    if (out != null) {
      createProcessOutputPump(is, out);
    }
  }
  





  public void setProcessErrorStream(InputStream is)
  {
    if (err != null) {
      createProcessErrorPump(is, err);
    }
  }
  





  public void setProcessInputStream(OutputStream os)
  {
    if (input != null) {
      if (input == System.in) {
        inputThread = createSystemInPump(input, os);
      } else {
        inputThread = createPump(input, os, true);
      }
    } else {
      try {
        os.close();
      } catch (IOException e) {
        String msg = "Got exception while closing output stream";
        DebugUtils.handleException("Got exception while closing output stream", e);
      }
    }
  }
  


  public void start()
  {
    if (outputThread != null) {
      outputThread.start();
    }
    if (errorThread != null) {
      errorThread.start();
    }
    if (inputThread != null) {
      inputThread.start();
    }
  }
  



  public void stop()
    throws IOException
  {
    if (inputStreamPumper != null) {
      inputStreamPumper.stopProcessing();
    }
    
    stopThread(outputThread, stopTimeout);
    stopThread(errorThread, stopTimeout);
    stopThread(inputThread, stopTimeout);
    
    if ((err != null) && (err != out)) {
      try {
        err.flush();
      } catch (IOException e) {
        String msg = "Got exception while flushing the error stream : " + e.getMessage();
        DebugUtils.handleException(msg, e);
      }
    }
    
    if (out != null) {
      try {
        out.flush();
      } catch (IOException e) {
        String msg = "Got exception while flushing the output stream";
        DebugUtils.handleException("Got exception while flushing the output stream", e);
      }
    }
    
    if (caught != null) {
      throw caught;
    }
  }
  




  protected OutputStream getErr()
  {
    return err;
  }
  




  protected OutputStream getOut()
  {
    return out;
  }
  





  protected void createProcessOutputPump(InputStream is, OutputStream os)
  {
    outputThread = createPump(is, os);
  }
  





  protected void createProcessErrorPump(InputStream is, OutputStream os)
  {
    errorThread = createPump(is, os);
  }
  








  protected Thread createPump(InputStream is, OutputStream os)
  {
    boolean closeWhenExhausted = (os instanceof PipedOutputStream);
    return createPump(is, os, closeWhenExhausted);
  }
  








  protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted)
  {
    Thread result = new Thread(new StreamPumper(is, os, closeWhenExhausted), "Exec Stream Pumper");
    result.setDaemon(true);
    return result;
  }
  









  protected void stopThread(Thread thread, long timeout)
  {
    if (thread != null) {
      try {
        if (timeout == 0L) {
          thread.join();
        } else {
          long timeToWait = timeout + 2000L;
          long startTime = System.currentTimeMillis();
          thread.join(timeToWait);
          if (System.currentTimeMillis() >= startTime + timeToWait) {
            String msg = "The stop timeout of " + timeout + " ms was exceeded";
            caught = new ExecuteException(msg, -559038737);
          }
        }
      } catch (InterruptedException e) {
        thread.interrupt();
      }
    }
  }
  







  private Thread createSystemInPump(InputStream is, OutputStream os)
  {
    inputStreamPumper = new InputStreamPumper(is, os);
    Thread result = new Thread(inputStreamPumper, "Exec Input Stream Pumper");
    result.setDaemon(true);
    return result;
  }
}
