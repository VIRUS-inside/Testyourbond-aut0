package org.eclipse.jetty.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;


























public class WriterOutputStream
  extends OutputStream
{
  protected final Writer _writer;
  protected final Charset _encoding;
  private final byte[] _buf = new byte[1];
  

  public WriterOutputStream(Writer writer, String encoding)
  {
    _writer = writer;
    _encoding = (encoding == null ? null : Charset.forName(encoding));
  }
  

  public WriterOutputStream(Writer writer)
  {
    _writer = writer;
    _encoding = null;
  }
  


  public void close()
    throws IOException
  {
    _writer.close();
  }
  


  public void flush()
    throws IOException
  {
    _writer.flush();
  }
  


  public void write(byte[] b)
    throws IOException
  {
    if (_encoding == null) {
      _writer.write(new String(b));
    } else {
      _writer.write(new String(b, _encoding));
    }
  }
  

  public void write(byte[] b, int off, int len)
    throws IOException
  {
    if (_encoding == null) {
      _writer.write(new String(b, off, len));
    } else {
      _writer.write(new String(b, off, len, _encoding));
    }
  }
  

  public synchronized void write(int b)
    throws IOException
  {
    _buf[0] = ((byte)b);
    write(_buf);
  }
}
