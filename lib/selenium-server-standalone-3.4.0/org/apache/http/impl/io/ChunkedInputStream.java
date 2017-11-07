package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;






















































public class ChunkedInputStream
  extends InputStream
{
  private static final int CHUNK_LEN = 1;
  private static final int CHUNK_DATA = 2;
  private static final int CHUNK_CRLF = 3;
  private static final int CHUNK_INVALID = Integer.MAX_VALUE;
  private static final int BUFFER_SIZE = 2048;
  private final SessionInputBuffer in;
  private final CharArrayBuffer buffer;
  private final MessageConstraints constraints;
  private int state;
  private long chunkSize;
  private long pos;
  private boolean eof = false;
  

  private boolean closed = false;
  
  private Header[] footers = new Header[0];
  









  public ChunkedInputStream(SessionInputBuffer in, MessageConstraints constraints)
  {
    this.in = ((SessionInputBuffer)Args.notNull(in, "Session input buffer"));
    pos = 0L;
    buffer = new CharArrayBuffer(16);
    this.constraints = (constraints != null ? constraints : MessageConstraints.DEFAULT);
    state = 1;
  }
  




  public ChunkedInputStream(SessionInputBuffer in)
  {
    this(in, null);
  }
  
  public int available() throws IOException
  {
    if ((in instanceof BufferInfo)) {
      int len = ((BufferInfo)in).length();
      return (int)Math.min(len, chunkSize - pos);
    }
    return 0;
  }
  












  public int read()
    throws IOException
  {
    if (closed) {
      throw new IOException("Attempted read from closed stream.");
    }
    if (eof) {
      return -1;
    }
    if (state != 2) {
      nextChunk();
      if (eof) {
        return -1;
      }
    }
    int b = in.read();
    if (b != -1) {
      pos += 1L;
      if (pos >= chunkSize) {
        state = 3;
      }
    }
    return b;
  }
  










  public int read(byte[] b, int off, int len)
    throws IOException
  {
    if (closed) {
      throw new IOException("Attempted read from closed stream.");
    }
    
    if (eof) {
      return -1;
    }
    if (state != 2) {
      nextChunk();
      if (eof) {
        return -1;
      }
    }
    int bytesRead = in.read(b, off, (int)Math.min(len, chunkSize - pos));
    if (bytesRead != -1) {
      pos += bytesRead;
      if (pos >= chunkSize) {
        state = 3;
      }
      return bytesRead;
    }
    eof = true;
    throw new TruncatedChunkException("Truncated chunk ( expected size: " + chunkSize + "; actual size: " + pos + ")");
  }
  









  public int read(byte[] b)
    throws IOException
  {
    return read(b, 0, b.length);
  }
  


  private void nextChunk()
    throws IOException
  {
    if (state == Integer.MAX_VALUE) {
      throw new MalformedChunkCodingException("Corrupt data stream");
    }
    try {
      chunkSize = getChunkSize();
      if (chunkSize < 0L) {
        throw new MalformedChunkCodingException("Negative chunk size");
      }
      state = 2;
      pos = 0L;
      if (chunkSize == 0L) {
        eof = true;
        parseTrailerHeaders();
      }
    } catch (MalformedChunkCodingException ex) {
      state = Integer.MAX_VALUE;
      throw ex;
    }
  }
  



  private long getChunkSize()
    throws IOException
  {
    int st = state;
    switch (st) {
    case 3: 
      buffer.clear();
      int bytesRead1 = in.readLine(buffer);
      if (bytesRead1 == -1) {
        throw new MalformedChunkCodingException("CRLF expected at end of chunk");
      }
      
      if (!buffer.isEmpty()) {
        throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
      }
      
      state = 1;
    
    case 1: 
      buffer.clear();
      int bytesRead2 = in.readLine(buffer);
      if (bytesRead2 == -1) {
        throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
      }
      
      int separator = buffer.indexOf(59);
      if (separator < 0) {
        separator = buffer.length();
      }
      String s = buffer.substringTrimmed(0, separator);
      try {
        return Long.parseLong(s, 16);
      } catch (NumberFormatException e) {
        throw new MalformedChunkCodingException("Bad chunk header: " + s);
      }
    }
    throw new IllegalStateException("Inconsistent codec state");
  }
  


  private void parseTrailerHeaders()
    throws IOException
  {
    try
    {
      footers = AbstractMessageParser.parseHeaders(in, constraints.getMaxHeaderCount(), constraints.getMaxLineLength(), null);

    }
    catch (HttpException ex)
    {
      IOException ioe = new MalformedChunkCodingException("Invalid footer: " + ex.getMessage());
      
      ioe.initCause(ex);
      throw ioe;
    }
  }
  





  public void close()
    throws IOException
  {
    if (!closed) {
      try {
        if ((!eof) && (state != Integer.MAX_VALUE))
        {
          byte[] buff = new byte['ࠀ'];
          while (read(buff) >= 0) {}
        }
      }
      finally {
        eof = true;
        closed = true;
      }
    }
  }
  
  public Header[] getFooters() {
    return (Header[])footers.clone();
  }
}
