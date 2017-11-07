package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


















abstract class AbstractNonStreamingHashFunction
  implements HashFunction
{
  AbstractNonStreamingHashFunction() {}
  
  public Hasher newHasher()
  {
    return new BufferingHasher(32);
  }
  
  public Hasher newHasher(int expectedInputSize)
  {
    Preconditions.checkArgument(expectedInputSize >= 0);
    return new BufferingHasher(expectedInputSize);
  }
  
  public <T> HashCode hashObject(T instance, Funnel<? super T> funnel)
  {
    return newHasher().putObject(instance, funnel).hash();
  }
  
  public HashCode hashUnencodedChars(CharSequence input)
  {
    int len = input.length();
    Hasher hasher = newHasher(len * 2);
    for (int i = 0; i < len; i++) {
      hasher.putChar(input.charAt(i));
    }
    return hasher.hash();
  }
  
  public HashCode hashString(CharSequence input, Charset charset)
  {
    return hashBytes(input.toString().getBytes(charset));
  }
  
  public HashCode hashInt(int input)
  {
    return newHasher(4).putInt(input).hash();
  }
  
  public HashCode hashLong(long input)
  {
    return newHasher(8).putLong(input).hash();
  }
  
  public HashCode hashBytes(byte[] input)
  {
    return hashBytes(input, 0, input.length);
  }
  
  private final class BufferingHasher
    extends AbstractHasher
  {
    final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;
    static final int BOTTOM_BYTE = 255;
    
    BufferingHasher(int expectedInputSize)
    {
      stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize);
    }
    
    public Hasher putByte(byte b)
    {
      stream.write(b);
      return this;
    }
    
    public Hasher putBytes(byte[] bytes)
    {
      try {
        stream.write(bytes);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return this;
    }
    
    public Hasher putBytes(byte[] bytes, int off, int len)
    {
      stream.write(bytes, off, len);
      return this;
    }
    
    public Hasher putShort(short s)
    {
      stream.write(s & 0xFF);
      stream.write(s >>> 8 & 0xFF);
      return this;
    }
    
    public Hasher putInt(int i)
    {
      stream.write(i & 0xFF);
      stream.write(i >>> 8 & 0xFF);
      stream.write(i >>> 16 & 0xFF);
      stream.write(i >>> 24 & 0xFF);
      return this;
    }
    
    public Hasher putLong(long l)
    {
      for (int i = 0; i < 64; i += 8) {
        stream.write((byte)(int)(l >>> i & 0xFF));
      }
      return this;
    }
    
    public Hasher putChar(char c)
    {
      stream.write(c & 0xFF);
      stream.write(c >>> '\b' & 0xFF);
      return this;
    }
    
    public <T> Hasher putObject(T instance, Funnel<? super T> funnel)
    {
      funnel.funnel(instance, this);
      return this;
    }
    
    public HashCode hash()
    {
      return hashBytes(stream.byteArray(), 0, stream.length());
    }
  }
  
  private static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream
  {
    ExposedByteArrayOutputStream(int expectedInputSize) {
      super();
    }
    
    byte[] byteArray() {
      return buf;
    }
    
    int length() {
      return count;
    }
  }
}
