package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.Args;


































































public class EofSensorInputStream
  extends InputStream
  implements ConnectionReleaseTrigger
{
  protected InputStream wrappedStream;
  private boolean selfClosed;
  private final EofSensorWatcher eofWatcher;
  
  public EofSensorInputStream(InputStream in, EofSensorWatcher watcher)
  {
    Args.notNull(in, "Wrapped stream");
    wrappedStream = in;
    selfClosed = false;
    eofWatcher = watcher;
  }
  
  boolean isSelfClosed() {
    return selfClosed;
  }
  
  InputStream getWrappedStream() {
    return wrappedStream;
  }
  







  protected boolean isReadAllowed()
    throws IOException
  {
    if (selfClosed) {
      throw new IOException("Attempted read on closed stream.");
    }
    return wrappedStream != null;
  }
  
  public int read() throws IOException
  {
    int l = -1;
    
    if (isReadAllowed()) {
      try {
        l = wrappedStream.read();
        checkEOF(l);
      } catch (IOException ex) {
        checkAbort();
        throw ex;
      }
    }
    
    return l;
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    int l = -1;
    
    if (isReadAllowed()) {
      try {
        l = wrappedStream.read(b, off, len);
        checkEOF(l);
      } catch (IOException ex) {
        checkAbort();
        throw ex;
      }
    }
    
    return l;
  }
  
  public int read(byte[] b) throws IOException
  {
    return read(b, 0, b.length);
  }
  
  public int available() throws IOException
  {
    int a = 0;
    
    if (isReadAllowed()) {
      try {
        a = wrappedStream.available();
      }
      catch (IOException ex) {
        checkAbort();
        throw ex;
      }
    }
    
    return a;
  }
  
  public void close()
    throws IOException
  {
    selfClosed = true;
    checkClose();
  }
  
















  protected void checkEOF(int eof)
    throws IOException
  {
    InputStream toCheckStream = wrappedStream;
    if ((toCheckStream != null) && (eof < 0)) {
      try {
        boolean scws = true;
        if (eofWatcher != null) {
          scws = eofWatcher.eofDetected(toCheckStream);
        }
        if (scws) {
          toCheckStream.close();
        }
      } finally {
        wrappedStream = null;
      }
    }
  }
  










  protected void checkClose()
    throws IOException
  {
    InputStream toCloseStream = wrappedStream;
    if (toCloseStream != null) {
      try {
        boolean scws = true;
        if (eofWatcher != null) {
          scws = eofWatcher.streamClosed(toCloseStream);
        }
        if (scws) {
          toCloseStream.close();
        }
      } finally {
        wrappedStream = null;
      }
    }
  }
  












  protected void checkAbort()
    throws IOException
  {
    InputStream toAbortStream = wrappedStream;
    if (toAbortStream != null) {
      try {
        boolean scws = true;
        if (eofWatcher != null) {
          scws = eofWatcher.streamAbort(toAbortStream);
        }
        if (scws) {
          toAbortStream.close();
        }
      } finally {
        wrappedStream = null;
      }
    }
  }
  


  public void releaseConnection()
    throws IOException
  {
    close();
  }
  







  public void abortConnection()
    throws IOException
  {
    selfClosed = true;
    checkAbort();
  }
}
