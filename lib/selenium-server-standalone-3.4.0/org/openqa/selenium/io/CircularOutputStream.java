package org.openqa.selenium.io;

import java.io.IOException;
import java.io.OutputStream;



















public class CircularOutputStream
  extends OutputStream
{
  private static final int DEFAULT_SIZE = 4096;
  private int start;
  private int end;
  private boolean filled = false;
  private byte[] buffer;
  
  public CircularOutputStream(int maxSize) {
    buffer = new byte[maxSize];
  }
  
  public CircularOutputStream() {
    this(4096);
  }
  
  public void write(int b) throws IOException
  {
    if (end == buffer.length) {
      filled = true;
      end = 0;
    }
    
    if ((filled) && (end == start)) {
      start = (start == buffer.length - 1 ? 0 : start + 1);
    }
    
    buffer[(end++)] = ((byte)b);
  }
  
  public String toString()
  {
    int size = filled ? buffer.length : end;
    byte[] toReturn = new byte[size];
    

    if (!filled) {
      System.arraycopy(buffer, 0, toReturn, 0, end);
      return new String(toReturn);
    }
    
    int copyStart = buffer.length - start;
    if (copyStart == buffer.length) {
      copyStart = 0;
    }
    
    System.arraycopy(buffer, start, toReturn, 0, copyStart);
    System.arraycopy(buffer, 0, toReturn, copyStart, end);
    return new String(toReturn);
  }
}
