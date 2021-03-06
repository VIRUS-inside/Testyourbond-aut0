package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;




















@Beta
@GwtIncompatible
public final class FileBackedOutputStream
  extends OutputStream
{
  private final int fileThreshold;
  private final boolean resetOnFinalize;
  private final ByteSource source;
  private OutputStream out;
  private MemoryOutput memory;
  private File file;
  
  private static class MemoryOutput
    extends ByteArrayOutputStream
  {
    private MemoryOutput() {}
    
    byte[] getBuffer()
    {
      return buf;
    }
    
    int getCount() {
      return count;
    }
  }
  
  @VisibleForTesting
  synchronized File getFile()
  {
    return file;
  }
  





  public FileBackedOutputStream(int fileThreshold)
  {
    this(fileThreshold, false);
  }
  







  public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize)
  {
    this.fileThreshold = fileThreshold;
    this.resetOnFinalize = resetOnFinalize;
    memory = new MemoryOutput(null);
    out = memory;
    
    if (resetOnFinalize) {
      source = new ByteSource()
      {
        public InputStream openStream() throws IOException
        {
          return FileBackedOutputStream.this.openInputStream();
        }
        
        protected void finalize()
        {
          try {
            reset();
          } catch (Throwable t) {
            t.printStackTrace(System.err);
          }
        }
      };
    } else {
      source = new ByteSource()
      {
        public InputStream openStream() throws IOException
        {
          return FileBackedOutputStream.this.openInputStream();
        }
      };
    }
  }
  




  public ByteSource asByteSource()
  {
    return source;
  }
  
  private synchronized InputStream openInputStream() throws IOException {
    if (file != null) {
      return new FileInputStream(file);
    }
    return new ByteArrayInputStream(memory.getBuffer(), 0, memory.getCount());
  }
  




  public synchronized void reset()
    throws IOException
  {
    try
    {
      close();
      
      if (memory == null) {
        memory = new MemoryOutput(null);
      } else {
        memory.reset();
      }
      out = memory;
      if (file != null) {
        File deleteMe = file;
        file = null;
        if (!deleteMe.delete()) {
          throw new IOException("Could not delete: " + deleteMe);
        }
      }
    }
    finally
    {
      if (memory == null) {
        memory = new MemoryOutput(null);
      } else {
        memory.reset();
      }
      out = memory;
      if (file != null) {
        File deleteMe = file;
        file = null;
        if (!deleteMe.delete()) {
          throw new IOException("Could not delete: " + deleteMe);
        }
      }
    }
  }
  
  public synchronized void write(int b) throws IOException
  {
    update(1);
    out.write(b);
  }
  
  public synchronized void write(byte[] b) throws IOException
  {
    write(b, 0, b.length);
  }
  
  public synchronized void write(byte[] b, int off, int len) throws IOException
  {
    update(len);
    out.write(b, off, len);
  }
  
  public synchronized void close() throws IOException
  {
    out.close();
  }
  
  public synchronized void flush() throws IOException
  {
    out.flush();
  }
  


  private void update(int len)
    throws IOException
  {
    if ((file == null) && (memory.getCount() + len > fileThreshold)) {
      File temp = File.createTempFile("FileBackedOutputStream", null);
      if (resetOnFinalize)
      {

        temp.deleteOnExit();
      }
      FileOutputStream transfer = new FileOutputStream(temp);
      transfer.write(memory.getBuffer(), 0, memory.getCount());
      transfer.flush();
      

      out = transfer;
      file = temp;
      memory = null;
    }
  }
}
