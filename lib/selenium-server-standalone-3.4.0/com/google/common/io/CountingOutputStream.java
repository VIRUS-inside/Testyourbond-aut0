package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

























@Beta
@GwtIncompatible
public final class CountingOutputStream
  extends FilterOutputStream
{
  private long count;
  
  public CountingOutputStream(OutputStream out)
  {
    super((OutputStream)Preconditions.checkNotNull(out));
  }
  
  public long getCount()
  {
    return count;
  }
  
  public void write(byte[] b, int off, int len) throws IOException
  {
    out.write(b, off, len);
    count += len;
  }
  
  public void write(int b) throws IOException
  {
    out.write(b);
    count += 1L;
  }
  


  public void close()
    throws IOException
  {
    out.close();
  }
}
