package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.Nullable;


























@GwtIncompatible
class AppendableWriter
  extends Writer
{
  private final Appendable target;
  private boolean closed;
  
  AppendableWriter(Appendable target)
  {
    this.target = ((Appendable)Preconditions.checkNotNull(target));
  }
  



  public void write(char[] cbuf, int off, int len)
    throws IOException
  {
    checkNotClosed();
    

    target.append(new String(cbuf, off, len));
  }
  
  public void flush() throws IOException
  {
    checkNotClosed();
    if ((target instanceof Flushable)) {
      ((Flushable)target).flush();
    }
  }
  
  public void close() throws IOException
  {
    closed = true;
    if ((target instanceof Closeable)) {
      ((Closeable)target).close();
    }
  }
  



  public void write(int c)
    throws IOException
  {
    checkNotClosed();
    target.append((char)c);
  }
  
  public void write(@Nullable String str) throws IOException
  {
    checkNotClosed();
    target.append(str);
  }
  
  public void write(@Nullable String str, int off, int len) throws IOException
  {
    checkNotClosed();
    
    target.append(str, off, off + len);
  }
  
  public Writer append(char c) throws IOException
  {
    checkNotClosed();
    target.append(c);
    return this;
  }
  
  public Writer append(@Nullable CharSequence charSeq) throws IOException
  {
    checkNotClosed();
    target.append(charSeq);
    return this;
  }
  
  public Writer append(@Nullable CharSequence charSeq, int start, int end) throws IOException
  {
    checkNotClosed();
    target.append(charSeq, start, end);
    return this;
  }
  
  private void checkNotClosed() throws IOException {
    if (closed) {
      throw new IOException("Cannot write to a closed writer.");
    }
  }
}
