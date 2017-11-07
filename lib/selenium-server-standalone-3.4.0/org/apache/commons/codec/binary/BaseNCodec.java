package org.apache.commons.codec.binary;

import java.util.Arrays;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;





































































public abstract class BaseNCodec
  implements BinaryEncoder, BinaryDecoder
{
  static final int EOF = -1;
  public static final int MIME_CHUNK_SIZE = 76;
  public static final int PEM_CHUNK_SIZE = 64;
  private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
  private static final int DEFAULT_BUFFER_SIZE = 8192;
  protected static final int MASK_8BITS = 255;
  protected static final byte PAD_DEFAULT = 61;
  
  static class Context
  {
    int ibitWorkArea;
    long lbitWorkArea;
    byte[] buffer;
    int pos;
    int readPos;
    boolean eof;
    int currentLinePos;
    int modulus;
    
    Context() {}
    
    public String toString()
    {
      return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", new Object[] { getClass().getSimpleName(), Arrays.toString(buffer), Integer.valueOf(currentLinePos), Boolean.valueOf(eof), Integer.valueOf(ibitWorkArea), Long.valueOf(lbitWorkArea), Integer.valueOf(modulus), Integer.valueOf(pos), Integer.valueOf(readPos) });
    }
  }
  



















































  @Deprecated
  protected final byte PAD = 61;
  



  protected final byte pad;
  



  private final int unencodedBlockSize;
  



  private final int encodedBlockSize;
  



  protected final int lineLength;
  



  private final int chunkSeparatorLength;
  




  protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength)
  {
    this(unencodedBlockSize, encodedBlockSize, lineLength, chunkSeparatorLength, (byte)61);
  }
  









  protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength, byte pad)
  {
    this.unencodedBlockSize = unencodedBlockSize;
    this.encodedBlockSize = encodedBlockSize;
    boolean useChunking = (lineLength > 0) && (chunkSeparatorLength > 0);
    this.lineLength = (useChunking ? lineLength / encodedBlockSize * encodedBlockSize : 0);
    this.chunkSeparatorLength = chunkSeparatorLength;
    
    this.pad = pad;
  }
  





  boolean hasData(Context context)
  {
    return buffer != null;
  }
  





  int available(Context context)
  {
    return buffer != null ? pos - readPos : 0;
  }
  




  protected int getDefaultBufferSize()
  {
    return 8192;
  }
  



  private byte[] resizeBuffer(Context context)
  {
    if (buffer == null) {
      buffer = new byte[getDefaultBufferSize()];
      pos = 0;
      readPos = 0;
    } else {
      byte[] b = new byte[buffer.length * 2];
      System.arraycopy(buffer, 0, b, 0, buffer.length);
      buffer = b;
    }
    return buffer;
  }
  






  protected byte[] ensureBufferSize(int size, Context context)
  {
    if ((buffer == null) || (buffer.length < pos + size)) {
      return resizeBuffer(context);
    }
    return buffer;
  }
  















  int readResults(byte[] b, int bPos, int bAvail, Context context)
  {
    if (buffer != null) {
      int len = Math.min(available(context), bAvail);
      System.arraycopy(buffer, readPos, b, bPos, len);
      readPos += len;
      if (readPos >= pos) {
        buffer = null;
      }
      return len;
    }
    return eof ? -1 : 0;
  }
  






  protected static boolean isWhiteSpace(byte byteToCheck)
  {
    switch (byteToCheck) {
    case 9: 
    case 10: 
    case 13: 
    case 32: 
      return true;
    }
    return false;
  }
  










  public Object encode(Object obj)
    throws EncoderException
  {
    if (!(obj instanceof byte[])) {
      throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
    }
    return encode((byte[])obj);
  }
  







  public String encodeToString(byte[] pArray)
  {
    return StringUtils.newStringUtf8(encode(pArray));
  }
  






  public String encodeAsString(byte[] pArray)
  {
    return StringUtils.newStringUtf8(encode(pArray));
  }
  










  public Object decode(Object obj)
    throws DecoderException
  {
    if ((obj instanceof byte[]))
      return decode((byte[])obj);
    if ((obj instanceof String)) {
      return decode((String)obj);
    }
    throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
  }
  







  public byte[] decode(String pArray)
  {
    return decode(StringUtils.getBytesUtf8(pArray));
  }
  







  public byte[] decode(byte[] pArray)
  {
    if ((pArray == null) || (pArray.length == 0)) {
      return pArray;
    }
    Context context = new Context();
    decode(pArray, 0, pArray.length, context);
    decode(pArray, 0, -1, context);
    byte[] result = new byte[pos];
    readResults(result, 0, result.length, context);
    return result;
  }
  







  public byte[] encode(byte[] pArray)
  {
    if ((pArray == null) || (pArray.length == 0)) {
      return pArray;
    }
    Context context = new Context();
    encode(pArray, 0, pArray.length, context);
    encode(pArray, 0, -1, context);
    byte[] buf = new byte[pos - readPos];
    readResults(buf, 0, buf.length, context);
    return buf;
  }
  





  abstract void encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Context paramContext);
  





  abstract void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Context paramContext);
  





  protected abstract boolean isInAlphabet(byte paramByte);
  




  public boolean isInAlphabet(byte[] arrayOctet, boolean allowWSPad)
  {
    for (int i = 0; i < arrayOctet.length; i++) {
      if ((!isInAlphabet(arrayOctet[i])) && ((!allowWSPad) || ((arrayOctet[i] != pad) && (!isWhiteSpace(arrayOctet[i])))))
      {
        return false;
      }
    }
    return true;
  }
  








  public boolean isInAlphabet(String basen)
  {
    return isInAlphabet(StringUtils.getBytesUtf8(basen), true);
  }
  








  protected boolean containsAlphabetOrPad(byte[] arrayOctet)
  {
    if (arrayOctet == null) {
      return false;
    }
    for (byte element : arrayOctet) {
      if ((pad == element) || (isInAlphabet(element))) {
        return true;
      }
    }
    return false;
  }
  









  public long getEncodedLength(byte[] pArray)
  {
    long len = (pArray.length + unencodedBlockSize - 1) / unencodedBlockSize * encodedBlockSize;
    if (lineLength > 0)
    {
      len += (len + lineLength - 1L) / lineLength * chunkSeparatorLength;
    }
    return len;
  }
}
