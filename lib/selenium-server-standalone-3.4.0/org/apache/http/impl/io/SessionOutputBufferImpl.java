package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;







































public class SessionOutputBufferImpl
  implements SessionOutputBuffer, BufferInfo
{
  private static final byte[] CRLF = { 13, 10 };
  


  private final HttpTransportMetricsImpl metrics;
  


  private final ByteArrayBuffer buffer;
  


  private final int fragementSizeHint;
  

  private final CharsetEncoder encoder;
  

  private OutputStream outstream;
  

  private ByteBuffer bbuf;
  


  public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int fragementSizeHint, CharsetEncoder charencoder)
  {
    Args.positive(buffersize, "Buffer size");
    Args.notNull(metrics, "HTTP transport metrcis");
    this.metrics = metrics;
    buffer = new ByteArrayBuffer(buffersize);
    this.fragementSizeHint = (fragementSizeHint >= 0 ? fragementSizeHint : 0);
    encoder = charencoder;
  }
  

  public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize)
  {
    this(metrics, buffersize, buffersize, null);
  }
  
  public void bind(OutputStream outstream) {
    this.outstream = outstream;
  }
  
  public boolean isBound() {
    return outstream != null;
  }
  
  public int capacity()
  {
    return buffer.capacity();
  }
  
  public int length()
  {
    return buffer.length();
  }
  
  public int available()
  {
    return capacity() - length();
  }
  
  private void streamWrite(byte[] b, int off, int len) throws IOException {
    Asserts.notNull(outstream, "Output stream");
    outstream.write(b, off, len);
  }
  
  private void flushStream() throws IOException {
    if (outstream != null) {
      outstream.flush();
    }
  }
  
  private void flushBuffer() throws IOException {
    int len = buffer.length();
    if (len > 0) {
      streamWrite(buffer.buffer(), 0, len);
      buffer.clear();
      metrics.incrementBytesTransferred(len);
    }
  }
  
  public void flush() throws IOException
  {
    flushBuffer();
    flushStream();
  }
  
  public void write(byte[] b, int off, int len) throws IOException
  {
    if (b == null) {
      return;
    }
    


    if ((len > fragementSizeHint) || (len > buffer.capacity()))
    {
      flushBuffer();
      
      streamWrite(b, off, len);
      metrics.incrementBytesTransferred(len);
    }
    else {
      int freecapacity = buffer.capacity() - buffer.length();
      if (len > freecapacity)
      {
        flushBuffer();
      }
      
      buffer.append(b, off, len);
    }
  }
  
  public void write(byte[] b) throws IOException
  {
    if (b == null) {
      return;
    }
    write(b, 0, b.length);
  }
  
  public void write(int b) throws IOException
  {
    if (fragementSizeHint > 0) {
      if (buffer.isFull()) {
        flushBuffer();
      }
      buffer.append(b);
    } else {
      flushBuffer();
      outstream.write(b);
    }
  }
  








  public void writeLine(String s)
    throws IOException
  {
    if (s == null) {
      return;
    }
    if (s.length() > 0) {
      if (encoder == null) {
        for (int i = 0; i < s.length(); i++) {
          write(s.charAt(i));
        }
      } else {
        CharBuffer cbuf = CharBuffer.wrap(s);
        writeEncoded(cbuf);
      }
    }
    write(CRLF);
  }
  








  public void writeLine(CharArrayBuffer charbuffer)
    throws IOException
  {
    if (charbuffer == null) {
      return;
    }
    if (encoder == null) {
      int off = 0;
      int remaining = charbuffer.length();
      while (remaining > 0) {
        int chunk = buffer.capacity() - buffer.length();
        chunk = Math.min(chunk, remaining);
        if (chunk > 0) {
          buffer.append(charbuffer, off, chunk);
        }
        if (buffer.isFull()) {
          flushBuffer();
        }
        off += chunk;
        remaining -= chunk;
      }
    } else {
      CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
      writeEncoded(cbuf);
    }
    write(CRLF);
  }
  
  private void writeEncoded(CharBuffer cbuf) throws IOException {
    if (!cbuf.hasRemaining()) {
      return;
    }
    if (bbuf == null) {
      bbuf = ByteBuffer.allocate(1024);
    }
    encoder.reset();
    while (cbuf.hasRemaining()) {
      CoderResult result = encoder.encode(cbuf, bbuf, true);
      handleEncodingResult(result);
    }
    CoderResult result = encoder.flush(bbuf);
    handleEncodingResult(result);
    bbuf.clear();
  }
  
  private void handleEncodingResult(CoderResult result) throws IOException {
    if (result.isError()) {
      result.throwException();
    }
    bbuf.flip();
    while (bbuf.hasRemaining()) {
      write(bbuf.get());
    }
    bbuf.compact();
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return metrics;
  }
}
