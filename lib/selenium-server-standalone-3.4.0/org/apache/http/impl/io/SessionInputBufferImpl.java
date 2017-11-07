package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.apache.http.MessageConstraintException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;




























































public class SessionInputBufferImpl
  implements SessionInputBuffer, BufferInfo
{
  private final HttpTransportMetricsImpl metrics;
  private final byte[] buffer;
  private final ByteArrayBuffer linebuffer;
  private final int minChunkLimit;
  private final MessageConstraints constraints;
  private final CharsetDecoder decoder;
  private InputStream instream;
  private int bufferpos;
  private int bufferlen;
  private CharBuffer cbuf;
  
  public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int minChunkLimit, MessageConstraints constraints, CharsetDecoder chardecoder)
  {
    Args.notNull(metrics, "HTTP transport metrcis");
    Args.positive(buffersize, "Buffer size");
    this.metrics = metrics;
    buffer = new byte[buffersize];
    bufferpos = 0;
    bufferlen = 0;
    this.minChunkLimit = (minChunkLimit >= 0 ? minChunkLimit : 512);
    this.constraints = (constraints != null ? constraints : MessageConstraints.DEFAULT);
    linebuffer = new ByteArrayBuffer(buffersize);
    decoder = chardecoder;
  }
  

  public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize)
  {
    this(metrics, buffersize, buffersize, null, null);
  }
  
  public void bind(InputStream instream) {
    this.instream = instream;
  }
  
  public boolean isBound() {
    return instream != null;
  }
  
  public int capacity()
  {
    return buffer.length;
  }
  
  public int length()
  {
    return bufferlen - bufferpos;
  }
  
  public int available()
  {
    return capacity() - length();
  }
  
  private int streamRead(byte[] b, int off, int len) throws IOException {
    Asserts.notNull(instream, "Input stream");
    return instream.read(b, off, len);
  }
  
  public int fillBuffer() throws IOException
  {
    if (bufferpos > 0) {
      int len = bufferlen - bufferpos;
      if (len > 0) {
        System.arraycopy(buffer, bufferpos, buffer, 0, len);
      }
      bufferpos = 0;
      bufferlen = len;
    }
    
    int off = bufferlen;
    int len = buffer.length - off;
    int l = streamRead(buffer, off, len);
    if (l == -1) {
      return -1;
    }
    bufferlen = (off + l);
    metrics.incrementBytesTransferred(l);
    return l;
  }
  
  public boolean hasBufferedData()
  {
    return bufferpos < bufferlen;
  }
  
  public void clear() {
    bufferpos = 0;
    bufferlen = 0;
  }
  
  public int read()
    throws IOException
  {
    while (!hasBufferedData()) {
      int noRead = fillBuffer();
      if (noRead == -1) {
        return -1;
      }
    }
    return buffer[(bufferpos++)] & 0xFF;
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    if (b == null) {
      return 0;
    }
    if (hasBufferedData()) {
      int chunk = Math.min(len, bufferlen - bufferpos);
      System.arraycopy(buffer, bufferpos, b, off, chunk);
      bufferpos += chunk;
      return chunk;
    }
    

    if (len > minChunkLimit) {
      int read = streamRead(b, off, len);
      if (read > 0) {
        metrics.incrementBytesTransferred(read);
      }
      return read;
    }
    
    while (!hasBufferedData()) {
      int noRead = fillBuffer();
      if (noRead == -1) {
        return -1;
      }
    }
    int chunk = Math.min(len, bufferlen - bufferpos);
    System.arraycopy(buffer, bufferpos, b, off, chunk);
    bufferpos += chunk;
    return chunk;
  }
  
  public int read(byte[] b)
    throws IOException
  {
    if (b == null) {
      return 0;
    }
    return read(b, 0, b.length);
  }
  














  public int readLine(CharArrayBuffer charbuffer)
    throws IOException
  {
    Args.notNull(charbuffer, "Char array buffer");
    int maxLineLen = constraints.getMaxLineLength();
    int noRead = 0;
    boolean retry = true;
    while (retry)
    {
      int pos = -1;
      for (int i = bufferpos; i < bufferlen; i++) {
        if (buffer[i] == 10) {
          pos = i;
          break;
        }
      }
      
      if (maxLineLen > 0) {
        int currentLen = linebuffer.length() + (pos > 0 ? pos : bufferlen) - bufferpos;
        
        if (currentLen >= maxLineLen) {
          throw new MessageConstraintException("Maximum line length limit exceeded");
        }
      }
      
      if (pos != -1)
      {
        if (linebuffer.isEmpty())
        {
          return lineFromReadBuffer(charbuffer, pos);
        }
        retry = false;
        int len = pos + 1 - bufferpos;
        linebuffer.append(buffer, bufferpos, len);
        bufferpos = (pos + 1);
      }
      else {
        if (hasBufferedData()) {
          int len = bufferlen - bufferpos;
          linebuffer.append(buffer, bufferpos, len);
          bufferpos = bufferlen;
        }
        noRead = fillBuffer();
        if (noRead == -1) {
          retry = false;
        }
      }
    }
    if ((noRead == -1) && (linebuffer.isEmpty()))
    {
      return -1;
    }
    return lineFromLineBuffer(charbuffer);
  }
  













  private int lineFromLineBuffer(CharArrayBuffer charbuffer)
    throws IOException
  {
    int len = linebuffer.length();
    if (len > 0) {
      if (linebuffer.byteAt(len - 1) == 10) {
        len--;
      }
      
      if ((len > 0) && 
        (linebuffer.byteAt(len - 1) == 13)) {
        len--;
      }
    }
    
    if (decoder == null) {
      charbuffer.append(linebuffer, 0, len);
    } else {
      ByteBuffer bbuf = ByteBuffer.wrap(linebuffer.buffer(), 0, len);
      len = appendDecoded(charbuffer, bbuf);
    }
    linebuffer.clear();
    return len;
  }
  
  private int lineFromReadBuffer(CharArrayBuffer charbuffer, int position) throws IOException
  {
    int pos = position;
    int off = bufferpos;
    
    bufferpos = (pos + 1);
    if ((pos > off) && (buffer[(pos - 1)] == 13))
    {
      pos--;
    }
    int len = pos - off;
    if (decoder == null) {
      charbuffer.append(buffer, off, len);
    } else {
      ByteBuffer bbuf = ByteBuffer.wrap(buffer, off, len);
      len = appendDecoded(charbuffer, bbuf);
    }
    return len;
  }
  
  private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException
  {
    if (!bbuf.hasRemaining()) {
      return 0;
    }
    if (cbuf == null) {
      cbuf = CharBuffer.allocate(1024);
    }
    decoder.reset();
    int len = 0;
    while (bbuf.hasRemaining()) {
      CoderResult result = decoder.decode(bbuf, cbuf, true);
      len += handleDecodingResult(result, charbuffer, bbuf);
    }
    CoderResult result = decoder.flush(cbuf);
    len += handleDecodingResult(result, charbuffer, bbuf);
    cbuf.clear();
    return len;
  }
  

  private int handleDecodingResult(CoderResult result, CharArrayBuffer charbuffer, ByteBuffer bbuf)
    throws IOException
  {
    if (result.isError()) {
      result.throwException();
    }
    cbuf.flip();
    int len = cbuf.remaining();
    while (cbuf.hasRemaining()) {
      charbuffer.append(cbuf.get());
    }
    cbuf.compact();
    return len;
  }
  
  public String readLine() throws IOException
  {
    CharArrayBuffer charbuffer = new CharArrayBuffer(64);
    int l = readLine(charbuffer);
    if (l != -1) {
      return charbuffer.toString();
    }
    return null;
  }
  
  public boolean isDataAvailable(int timeout)
    throws IOException
  {
    return hasBufferedData();
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return metrics;
  }
}
