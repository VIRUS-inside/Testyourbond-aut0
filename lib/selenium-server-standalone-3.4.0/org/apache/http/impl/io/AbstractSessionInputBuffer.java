package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;




















































@Deprecated
public abstract class AbstractSessionInputBuffer
  implements SessionInputBuffer, BufferInfo
{
  private InputStream instream;
  private byte[] buffer;
  private ByteArrayBuffer linebuffer;
  private Charset charset;
  private boolean ascii;
  private int maxLineLen;
  private int minChunkLimit;
  private HttpTransportMetricsImpl metrics;
  private CodingErrorAction onMalformedCharAction;
  private CodingErrorAction onUnmappableCharAction;
  private int bufferpos;
  private int bufferlen;
  private CharsetDecoder decoder;
  private CharBuffer cbuf;
  
  public AbstractSessionInputBuffer() {}
  
  protected void init(InputStream instream, int buffersize, HttpParams params)
  {
    Args.notNull(instream, "Input stream");
    Args.notNegative(buffersize, "Buffer size");
    Args.notNull(params, "HTTP parameters");
    this.instream = instream;
    buffer = new byte[buffersize];
    bufferpos = 0;
    bufferlen = 0;
    linebuffer = new ByteArrayBuffer(buffersize);
    String charset = (String)params.getParameter("http.protocol.element-charset");
    this.charset = (charset != null ? Charset.forName(charset) : Consts.ASCII);
    ascii = this.charset.equals(Consts.ASCII);
    decoder = null;
    maxLineLen = params.getIntParameter("http.connection.max-line-length", -1);
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
  
  protected int fillBuffer() throws IOException
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
    int l = instream.read(buffer, off, len);
    if (l == -1) {
      return -1;
    }
    bufferlen = (off + l);
    metrics.incrementBytesTransferred(l);
    return l;
  }
  
  protected boolean hasBufferedData()
  {
    return bufferpos < bufferlen;
  }
  
  public int read() throws IOException
  {
    while (!hasBufferedData()) {
      int noRead = fillBuffer();
      if (noRead == -1) {
        return -1;
      }
    }
    return buffer[(bufferpos++)] & 0xFF;
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
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
      int read = instream.read(b, off, len);
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
  
  public int read(byte[] b) throws IOException
  {
    if (b == null) {
      return 0;
    }
    return read(b, 0, b.length);
  }
  
  private int locateLF() {
    for (int i = bufferpos; i < bufferlen; i++) {
      if (buffer[i] == 10) {
        return i;
      }
    }
    return -1;
  }
  













  public int readLine(CharArrayBuffer charbuffer)
    throws IOException
  {
    Args.notNull(charbuffer, "Char array buffer");
    int noRead = 0;
    boolean retry = true;
    while (retry)
    {
      int i = locateLF();
      if (i != -1)
      {
        if (linebuffer.isEmpty())
        {
          return lineFromReadBuffer(charbuffer, i);
        }
        retry = false;
        int len = i + 1 - bufferpos;
        linebuffer.append(buffer, bufferpos, len);
        bufferpos = (i + 1);
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
      if ((maxLineLen > 0) && (linebuffer.length() >= maxLineLen)) {
        throw new IOException("Maximum line length limit exceeded");
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
    
    if (ascii) {
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
    int off = bufferpos;
    int i = position;
    bufferpos = (i + 1);
    if ((i > off) && (buffer[(i - 1)] == 13))
    {
      i--;
    }
    int len = i - off;
    if (ascii) {
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
    if (decoder == null) {
      decoder = charset.newDecoder();
      decoder.onMalformedInput(onMalformedCharAction);
      decoder.onUnmappableCharacter(onUnmappableCharAction);
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
  
  public String readLine() throws IOException {
    CharArrayBuffer charbuffer = new CharArrayBuffer(64);
    int l = readLine(charbuffer);
    if (l != -1) {
      return charbuffer.toString();
    }
    return null;
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return metrics;
  }
}
