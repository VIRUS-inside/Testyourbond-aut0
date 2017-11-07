package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Formatter;
import java.util.Locale;
import org.seleniumhq.jetty9.io.EofException;
import org.seleniumhq.jetty9.io.RuntimeIOException;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
































public class ResponseWriter
  extends PrintWriter
{
  private static final Logger LOG = Log.getLogger(ResponseWriter.class);
  private static final String __lineSeparator = System.getProperty("line.separator");
  private static final String __trueln = "true" + __lineSeparator;
  private static final String __falseln = "false" + __lineSeparator;
  
  private final HttpWriter _httpWriter;
  private final Locale _locale;
  private final String _encoding;
  private IOException _ioException;
  private boolean _isClosed = false;
  private Formatter _formatter;
  
  public ResponseWriter(HttpWriter httpWriter, Locale locale, String encoding)
  {
    super(httpWriter, false);
    _httpWriter = httpWriter;
    _locale = locale;
    _encoding = encoding;
  }
  
  public boolean isFor(Locale locale, String encoding)
  {
    if ((_locale == null) && (locale != null))
      return false;
    if ((_encoding == null) && (encoding != null))
      return false;
    return (_encoding.equalsIgnoreCase(encoding)) && (_locale.equals(locale));
  }
  
  protected void reopen()
  {
    synchronized (lock)
    {
      _isClosed = false;
      clearError();
      out = _httpWriter;
    }
  }
  

  protected void clearError()
  {
    synchronized (lock)
    {
      _ioException = null;
      super.clearError();
    }
  }
  

  public boolean checkError()
  {
    synchronized (lock)
    {
      return (_ioException != null) || (super.checkError());
    }
  }
  
  private void setError(Throwable th)
  {
    super.setError();
    
    if ((th instanceof IOException)) {
      _ioException = ((IOException)th);
    }
    else {
      _ioException = new IOException(String.valueOf(th));
      _ioException.initCause(th);
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug(th);
    }
  }
  

  protected void setError()
  {
    setError(new IOException());
  }
  
  private void isOpen()
    throws IOException
  {
    if (_ioException != null) {
      throw new RuntimeIOException(_ioException);
    }
    if (_isClosed) {
      throw new EofException("Stream closed");
    }
  }
  
  public void flush()
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.flush();
      }
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void close()
  {
    try
    {
      synchronized (lock)
      {
        out.close();
        _isClosed = true;
      }
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void write(int c)
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(c);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void write(char[] buf, int off, int len)
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(buf, off, len);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void write(char[] buf)
  {
    write(buf, 0, buf.length);
  }
  

  public void write(String s, int off, int len)
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(s, off, len);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void write(String s)
  {
    write(s, 0, s.length());
  }
  

  public void print(boolean b)
  {
    write(b ? "true" : "false");
  }
  

  public void print(char c)
  {
    write(c);
  }
  

  public void print(int i)
  {
    write(String.valueOf(i));
  }
  

  public void print(long l)
  {
    write(String.valueOf(l));
  }
  

  public void print(float f)
  {
    write(String.valueOf(f));
  }
  

  public void print(double d)
  {
    write(String.valueOf(d));
  }
  

  public void print(char[] s)
  {
    write(s);
  }
  

  public void print(String s)
  {
    if (s == null)
      s = "null";
    write(s);
  }
  

  public void print(Object obj)
  {
    write(String.valueOf(obj));
  }
  

  public void println()
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(__lineSeparator);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void println(boolean b)
  {
    println(b ? __trueln : __falseln);
  }
  

  public void println(char c)
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(c);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void println(int x)
  {
    println(String.valueOf(x));
  }
  

  public void println(long x)
  {
    println(String.valueOf(x));
  }
  

  public void println(float x)
  {
    println(String.valueOf(x));
  }
  

  public void println(double x)
  {
    println(String.valueOf(x));
  }
  

  public void println(char[] s)
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(s, 0, s.length);
        out.write(__lineSeparator);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void println(String s)
  {
    if (s == null) {
      s = "null";
    }
    try
    {
      synchronized (lock)
      {
        isOpen();
        out.write(s, 0, s.length());
        out.write(__lineSeparator);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
  }
  

  public void println(Object x)
  {
    println(String.valueOf(x));
  }
  

  public PrintWriter printf(String format, Object... args)
  {
    return format(_locale, format, args);
  }
  

  public PrintWriter printf(Locale l, String format, Object... args)
  {
    return format(l, format, args);
  }
  

  public PrintWriter format(String format, Object... args)
  {
    return format(_locale, format, args);
  }
  

  public PrintWriter format(Locale l, String format, Object... args)
  {
    try
    {
      synchronized (lock)
      {
        isOpen();
        if ((_formatter == null) || (_formatter.locale() != l))
          _formatter = new Formatter(this, l);
        _formatter.format(l, format, args);
      }
    }
    catch (InterruptedIOException ex)
    {
      LOG.debug(ex);
      Thread.currentThread().interrupt();
    }
    catch (IOException ex)
    {
      setError(ex);
    }
    return this;
  }
}
