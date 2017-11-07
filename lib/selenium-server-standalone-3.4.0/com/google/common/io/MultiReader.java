package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import javax.annotation.Nullable;

















@GwtIncompatible
class MultiReader
  extends Reader
{
  private final Iterator<? extends CharSource> it;
  private Reader current;
  
  MultiReader(Iterator<? extends CharSource> readers)
    throws IOException
  {
    it = readers;
    advance();
  }
  

  private void advance()
    throws IOException
  {
    close();
    if (it.hasNext()) {
      current = ((CharSource)it.next()).openStream();
    }
  }
  
  public int read(@Nullable char[] cbuf, int off, int len) throws IOException
  {
    if (current == null) {
      return -1;
    }
    int result = current.read(cbuf, off, len);
    if (result == -1) {
      advance();
      return read(cbuf, off, len);
    }
    return result;
  }
  
  public long skip(long n) throws IOException
  {
    Preconditions.checkArgument(n >= 0L, "n is negative");
    if (n > 0L) {
      while (current != null) {
        long result = current.skip(n);
        if (result > 0L) {
          return result;
        }
        advance();
      }
    }
    return 0L;
  }
  
  public boolean ready() throws IOException
  {
    return (current != null) && (current.ready());
  }
  
  public void close() throws IOException
  {
    if (current != null) {
      try {
        current.close();
        
        current = null; } finally { current = null;
      }
    }
  }
}
