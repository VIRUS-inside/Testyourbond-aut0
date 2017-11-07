package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;






































public class CharSequenceInputStream
  extends InputStream
{
  private static final int BUFFER_SIZE = 2048;
  private static final int NO_MARK = -1;
  private final CharsetEncoder encoder;
  private final CharBuffer cbuf;
  private final ByteBuffer bbuf;
  private int mark_cbuf;
  private int mark_bbuf;
  
  public CharSequenceInputStream(CharSequence cs, Charset charset, int bufferSize)
  {
    encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    


    float maxBytesPerChar = encoder.maxBytesPerChar();
    if (bufferSize < maxBytesPerChar) {
      throw new IllegalArgumentException("Buffer size " + bufferSize + " is less than maxBytesPerChar " + maxBytesPerChar);
    }
    
    bbuf = ByteBuffer.allocate(bufferSize);
    bbuf.flip();
    cbuf = CharBuffer.wrap(cs);
    mark_cbuf = -1;
    mark_bbuf = -1;
  }
  







  public CharSequenceInputStream(CharSequence cs, String charset, int bufferSize)
  {
    this(cs, Charset.forName(charset), bufferSize);
  }
  







  public CharSequenceInputStream(CharSequence cs, Charset charset)
  {
    this(cs, charset, 2048);
  }
  







  public CharSequenceInputStream(CharSequence cs, String charset)
  {
    this(cs, charset, 2048);
  }
  




  private void fillBuffer()
    throws CharacterCodingException
  {
    bbuf.compact();
    CoderResult result = encoder.encode(cbuf, bbuf, true);
    if (result.isError()) {
      result.throwException();
    }
    bbuf.flip();
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    if (b == null) {
      throw new NullPointerException("Byte array is null");
    }
    if ((len < 0) || (off + len > b.length)) {
      throw new IndexOutOfBoundsException("Array Size=" + b.length + ", offset=" + off + ", length=" + len);
    }
    
    if (len == 0) {
      return 0;
    }
    if ((!bbuf.hasRemaining()) && (!cbuf.hasRemaining())) {
      return -1;
    }
    int bytesRead = 0;
    while (len > 0) {
      if (bbuf.hasRemaining()) {
        int chunk = Math.min(bbuf.remaining(), len);
        bbuf.get(b, off, chunk);
        off += chunk;
        len -= chunk;
        bytesRead += chunk;
      } else {
        fillBuffer();
        if ((!bbuf.hasRemaining()) && (!cbuf.hasRemaining())) {
          break;
        }
      }
    }
    return (bytesRead == 0) && (!cbuf.hasRemaining()) ? -1 : bytesRead;
  }
  
  public int read() throws IOException
  {
    do {
      if (bbuf.hasRemaining()) {
        return bbuf.get() & 0xFF;
      }
      fillBuffer();
    } while ((bbuf.hasRemaining()) || (cbuf.hasRemaining()));
    return -1;
  }
  

  public int read(byte[] b)
    throws IOException
  {
    return read(b, 0, b.length);
  }
  


  public long skip(long n)
    throws IOException
  {
    long skipped = 0L;
    while ((n > 0L) && (available() > 0)) {
      read();
      n -= 1L;
      skipped += 1L;
    }
    return skipped;
  }
  









  public int available()
    throws IOException
  {
    return bbuf.remaining() + cbuf.remaining();
  }
  


  public void close()
    throws IOException
  {}
  


  public synchronized void mark(int readlimit)
  {
    mark_cbuf = cbuf.position();
    mark_bbuf = bbuf.position();
    cbuf.mark();
    bbuf.mark();
  }
  












  public synchronized void reset()
    throws IOException
  {
    if (mark_cbuf != -1)
    {
      if (cbuf.position() != 0) {
        encoder.reset();
        cbuf.rewind();
        bbuf.rewind();
        bbuf.limit(0);
        while (cbuf.position() < mark_cbuf) {
          bbuf.rewind();
          bbuf.limit(0);
          fillBuffer();
        }
      }
      if (cbuf.position() != mark_cbuf) {
        throw new IllegalStateException("Unexpected CharBuffer postion: actual=" + cbuf.position() + " " + "expected=" + mark_cbuf);
      }
      
      bbuf.position(mark_bbuf);
      mark_cbuf = -1;
      mark_bbuf = -1;
    }
  }
  
  public boolean markSupported()
  {
    return true;
  }
}
