package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

abstract interface WriterChain
{
  public abstract void write(int paramInt)
    throws IOException;
  
  public abstract void write(char[] paramArrayOfChar)
    throws IOException;
  
  public abstract void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract void write(String paramString)
    throws IOException;
  
  public abstract void write(String paramString, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract void flush()
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public abstract Writer getWriter();
  
  public abstract OutputStream getOutputStream();
}
