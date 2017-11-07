package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import javax.annotation.Nullable;





























































@Beta
public abstract class HashCode
{
  HashCode() {}
  
  public abstract int bits();
  
  public abstract int asInt();
  
  public abstract long asLong();
  
  public abstract long padToLong();
  
  public abstract byte[] asBytes();
  
  @CanIgnoreReturnValue
  public int writeBytesTo(byte[] dest, int offset, int maxLength)
  {
    maxLength = Ints.min(new int[] { maxLength, bits() / 8 });
    Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
    writeBytesToImpl(dest, offset, maxLength);
    return maxLength;
  }
  


  abstract void writeBytesToImpl(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  


  byte[] getBytesInternal()
  {
    return asBytes();
  }
  





  abstract boolean equalsSameBits(HashCode paramHashCode);
  




  public static HashCode fromInt(int hash)
  {
    return new IntHashCode(hash);
  }
  
  private static final class IntHashCode extends HashCode implements Serializable {
    final int hash;
    private static final long serialVersionUID = 0L;
    
    IntHashCode(int hash) { this.hash = hash; }
    

    public int bits()
    {
      return 32;
    }
    
    public byte[] asBytes()
    {
      return new byte[] { (byte)hash, (byte)(hash >> 8), (byte)(hash >> 16), (byte)(hash >> 24) };
    }
    





    public int asInt()
    {
      return hash;
    }
    
    public long asLong()
    {
      throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
    }
    
    public long padToLong()
    {
      return UnsignedInts.toLong(hash);
    }
    
    void writeBytesToImpl(byte[] dest, int offset, int maxLength)
    {
      for (int i = 0; i < maxLength; i++) {
        dest[(offset + i)] = ((byte)(hash >> i * 8));
      }
    }
    
    boolean equalsSameBits(HashCode that)
    {
      return hash == that.asInt();
    }
  }
  







  public static HashCode fromLong(long hash)
  {
    return new LongHashCode(hash);
  }
  
  private static final class LongHashCode extends HashCode implements Serializable {
    final long hash;
    private static final long serialVersionUID = 0L;
    
    LongHashCode(long hash) { this.hash = hash; }
    

    public int bits()
    {
      return 64;
    }
    
    public byte[] asBytes()
    {
      return new byte[] { (byte)(int)hash, (byte)(int)(hash >> 8), (byte)(int)(hash >> 16), (byte)(int)(hash >> 24), (byte)(int)(hash >> 32), (byte)(int)(hash >> 40), (byte)(int)(hash >> 48), (byte)(int)(hash >> 56) };
    }
    









    public int asInt()
    {
      return (int)hash;
    }
    
    public long asLong()
    {
      return hash;
    }
    
    public long padToLong()
    {
      return hash;
    }
    
    void writeBytesToImpl(byte[] dest, int offset, int maxLength)
    {
      for (int i = 0; i < maxLength; i++) {
        dest[(offset + i)] = ((byte)(int)(hash >> i * 8));
      }
    }
    
    boolean equalsSameBits(HashCode that)
    {
      return hash == that.asLong();
    }
  }
  







  public static HashCode fromBytes(byte[] bytes)
  {
    Preconditions.checkArgument(bytes.length >= 1, "A HashCode must contain at least 1 byte.");
    return fromBytesNoCopy((byte[])bytes.clone());
  }
  



  static HashCode fromBytesNoCopy(byte[] bytes)
  {
    return new BytesHashCode(bytes);
  }
  
  private static final class BytesHashCode extends HashCode implements Serializable {
    final byte[] bytes;
    private static final long serialVersionUID = 0L;
    
    BytesHashCode(byte[] bytes) { this.bytes = ((byte[])Preconditions.checkNotNull(bytes)); }
    

    public int bits()
    {
      return bytes.length * 8;
    }
    
    public byte[] asBytes()
    {
      return (byte[])bytes.clone();
    }
    
    public int asInt()
    {
      Preconditions.checkState(bytes.length >= 4, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", bytes.length);
      


      return bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
    }
    



    public long asLong()
    {
      Preconditions.checkState(bytes.length >= 8, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", bytes.length);
      


      return padToLong();
    }
    
    public long padToLong()
    {
      long retVal = bytes[0] & 0xFF;
      for (int i = 1; i < Math.min(bytes.length, 8); i++) {
        retVal |= (bytes[i] & 0xFF) << i * 8;
      }
      return retVal;
    }
    
    void writeBytesToImpl(byte[] dest, int offset, int maxLength)
    {
      System.arraycopy(bytes, 0, dest, offset, maxLength);
    }
    
    byte[] getBytesInternal()
    {
      return bytes;
    }
    


    boolean equalsSameBits(HashCode that)
    {
      if (bytes.length != that.getBytesInternal().length) {
        return false;
      }
      
      boolean areEqual = true;
      for (int i = 0; i < bytes.length; i++) {
        areEqual &= bytes[i] == that.getBytesInternal()[i];
      }
      return areEqual;
    }
  }
  











  public static HashCode fromString(String string)
  {
    Preconditions.checkArgument(
      string.length() >= 2, "input string (%s) must have at least 2 characters", string);
    Preconditions.checkArgument(string
      .length() % 2 == 0, "input string (%s) must have an even number of characters", string);
    


    byte[] bytes = new byte[string.length() / 2];
    for (int i = 0; i < string.length(); i += 2) {
      int ch1 = decode(string.charAt(i)) << 4;
      int ch2 = decode(string.charAt(i + 1));
      bytes[(i / 2)] = ((byte)(ch1 + ch2));
    }
    return fromBytesNoCopy(bytes);
  }
  
  private static int decode(char ch) {
    if ((ch >= '0') && (ch <= '9')) {
      return ch - '0';
    }
    if ((ch >= 'a') && (ch <= 'f')) {
      return ch - 'a' + 10;
    }
    throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
  }
  







  public final boolean equals(@Nullable Object object)
  {
    if ((object instanceof HashCode)) {
      HashCode that = (HashCode)object;
      return (bits() == that.bits()) && (equalsSameBits(that));
    }
    return false;
  }
  







  public final int hashCode()
  {
    if (bits() >= 32) {
      return asInt();
    }
    
    byte[] bytes = getBytesInternal();
    int val = bytes[0] & 0xFF;
    for (int i = 1; i < bytes.length; i++) {
      val |= (bytes[i] & 0xFF) << i * 8;
    }
    return val;
  }
  











  public final String toString()
  {
    byte[] bytes = getBytesInternal();
    StringBuilder sb = new StringBuilder(2 * bytes.length);
    for (byte b : bytes) {
      sb.append(hexDigits[(b >> 4 & 0xF)]).append(hexDigits[(b & 0xF)]);
    }
    return sb.toString();
  }
  
  private static final char[] hexDigits = "0123456789abcdef".toCharArray();
}
