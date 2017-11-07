package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;











































@Deprecated
public abstract class AbstractSessionOutputBuffer
  implements SessionOutputBuffer, BufferInfo
{
  private static final byte[] CRLF = { 13, 10 };
  
  private OutputStream outstream;
  
  private ByteArrayBuffer buffer;
  
  private Charset charset;
  
  private boolean ascii;
  
  private int minChunkLimit;
  
  private HttpTransportMetricsImpl metrics;
  
  private CodingErrorAction onMalformedCharAction;
  
  private CodingErrorAction onUnmappableCharAction;
  private CharsetEncoder encoder;
  private ByteBuffer bbuf;
  
  protected AbstractSessionOutputBuffer(OutputStream outstream, int buffersize, Charset charset, int minChunkLimit, CodingErrorAction malformedCharAction, CodingErrorAction unmappableCharAction)
  {
    Args.notNull(outstream, "Input stream");
    Args.notNegative(buffersize, "Buffer size");
    this.outstream = outstream;
    buffer = new ByteArrayBuffer(buffersize);
    this.charset = (charset != null ? charset : Consts.ASCII);
    ascii = this.charset.equals(Consts.ASCII);
    encoder = null;
    this.minChunkLimit = (minChunkLimit >= 0 ? minChunkLimit : 512);
    metrics = createTransportMetrics();
    onMalformedCharAction = (malformedCharAction != null ? malformedCharAction : CodingErrorAction.REPORT);
    
    onUnmappableCharAction = (unmappableCharAction != null ? unmappableCharAction : CodingErrorAction.REPORT);
  }
  

  public AbstractSessionOutputBuffer() {}
  
  protected void init(OutputStream outstream, int buffersize, HttpParams params)
  {
    Args.notNull(outstream, "Input stream");
    Args.notNegative(buffersize, "Buffer size");
    Args.notNull(params, "HTTP parameters");
    this.outstream = outstream;
    buffer = new ByteArrayBuffer(buffersize);
    String charset = (String)params.getParameter("http.protocol.element-charset");
    this.charset = (charset != null ? Charset.forName(charset) : Consts.ASCII);
    ascii = this.charset.equals(Consts.ASCII);
    encoder = null;
    minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
    metrics = createTransportMetrics();
    CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
    
    onMalformedCharAction = (a1 != null ? a1 : CodingErrorAction.REPORT);
    CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
    
    onUnmappableCharAction = (a2 != null ? a2 : CodingErrorAction.REPORT);
  }
  


  protected HttpTransportMetricsImpl createTransportMetrics()
  {
    return new HttpTransportMetricsImpl();
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
  
  protected void flushBuffer() throws IOException {
    int len = buffer.length();
    if (len > 0) {
      outstream.write(buffer.buffer(), 0, len);
      buffer.clear();
      metrics.incrementBytesTransferred(len);
    }
  }
  
  public void flush() throws IOException {
    flushBuffer();
    outstream.flush();
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    if (b == null) {
      return;
    }
    


    if ((len > minChunkLimit) || (len > buffer.capacity()))
    {
      flushBuffer();
      
      outstream.write(b, off, len);
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
  
  public void write(byte[] b) throws IOException {
    if (b == null) {
      return;
    }
    write(b, 0, b.length);
  }
  
  public void write(int b) throws IOException {
    if (buffer.isFull()) {
      flushBuffer();
    }
    buffer.append(b);
  }
  







  public void writeLine(String s)
    throws IOException
  {
    if (s == null) {
      return;
    }
    if (s.length() > 0) {
      if (ascii) {
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
    if (ascii) {
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
    if (encoder == null) {
      encoder = charset.newEncoder();
      encoder.onMalformedInput(onMalformedCharAction);
      encoder.onUnmappableCharacter(onUnmappableCharAction);
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
  
  public HttpTransportMetrics getMetrics() {
    return metrics;
  }
}
