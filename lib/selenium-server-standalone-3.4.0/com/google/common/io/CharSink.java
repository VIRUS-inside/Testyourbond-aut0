package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;



























































@GwtIncompatible
public abstract class CharSink
{
  protected CharSink() {}
  
  public abstract Writer openStream()
    throws IOException;
  
  public Writer openBufferedStream()
    throws IOException
  {
    Writer writer = openStream();
    return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
  }
  





  public void write(CharSequence charSequence)
    throws IOException
  {
    Preconditions.checkNotNull(charSequence);
    
    Closer closer = Closer.create();
    try {
      Writer out = (Writer)closer.register(openStream());
      out.append(charSequence);
      out.flush();
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  





  public void writeLines(Iterable<? extends CharSequence> lines)
    throws IOException
  {
    writeLines(lines, System.getProperty("line.separator"));
  }
  





  public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator)
    throws IOException
  {
    Preconditions.checkNotNull(lines);
    Preconditions.checkNotNull(lineSeparator);
    
    Closer closer = Closer.create();
    try {
      Writer out = (Writer)closer.register(openBufferedStream());
      for (CharSequence line : lines) {
        out.append(line).append(lineSeparator);
      }
      out.flush();
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  






  @CanIgnoreReturnValue
  public long writeFrom(Readable readable)
    throws IOException
  {
    Preconditions.checkNotNull(readable);
    
    Closer closer = Closer.create();
    try {
      Writer out = (Writer)closer.register(openStream());
      long written = CharStreams.copy(readable, out);
      out.flush();
      return written;
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
}
