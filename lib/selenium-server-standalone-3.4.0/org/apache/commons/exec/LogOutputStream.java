package org.apache.commons.exec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


































public abstract class LogOutputStream
  extends OutputStream
{
  private static final int INTIAL_SIZE = 132;
  private static final int CR = 13;
  private static final int LF = 10;
  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(132);
  

  private boolean skip = false;
  

  private final int level;
  


  public LogOutputStream()
  {
    this(999);
  }
  




  public LogOutputStream(int level)
  {
    this.level = level;
  }
  






  public void write(int cc)
    throws IOException
  {
    byte c = (byte)cc;
    if ((c == 10) || (c == 13)) {
      if (!skip) {
        processBuffer();
      }
    } else {
      buffer.write(cc);
    }
    skip = (c == 13);
  }
  





  public void flush()
  {
    if (buffer.size() > 0) {
      processBuffer();
    }
  }
  




  public void close()
    throws IOException
  {
    if (buffer.size() > 0) {
      processBuffer();
    }
    super.close();
  }
  


  public int getMessageLevel()
  {
    return level;
  }
  










  public void write(byte[] b, int off, int len)
    throws IOException
  {
    int offset = off;
    int blockStartOffset = offset;
    int remaining = len;
    while (remaining > 0) {
      while ((remaining > 0) && (b[offset] != 10) && (b[offset] != 13)) {
        offset++;
        remaining--;
      }
      
      int blockLength = offset - blockStartOffset;
      if (blockLength > 0) {
        buffer.write(b, blockStartOffset, blockLength);
      }
      while ((remaining > 0) && ((b[offset] == 10) || (b[offset] == 13))) {
        write(b[offset]);
        offset++;
        remaining--;
      }
      blockStartOffset = offset;
    }
  }
  


  protected void processBuffer()
  {
    processLine(buffer.toString());
    buffer.reset();
  }
  





  protected void processLine(String line)
  {
    processLine(line, level);
  }
  
  protected abstract void processLine(String paramString, int paramInt);
}
