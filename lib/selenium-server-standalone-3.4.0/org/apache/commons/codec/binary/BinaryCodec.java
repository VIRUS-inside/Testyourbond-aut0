package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
































public class BinaryCodec
  implements BinaryDecoder, BinaryEncoder
{
  private static final char[] EMPTY_CHAR_ARRAY = new char[0];
  

  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  

  private static final int BIT_0 = 1;
  

  private static final int BIT_1 = 2;
  

  private static final int BIT_2 = 4;
  

  private static final int BIT_3 = 8;
  

  private static final int BIT_4 = 16;
  

  private static final int BIT_5 = 32;
  

  private static final int BIT_6 = 64;
  

  private static final int BIT_7 = 128;
  
  private static final int[] BITS = { 1, 2, 4, 8, 16, 32, 64, 128 };
  



  public BinaryCodec() {}
  



  public byte[] encode(byte[] raw)
  {
    return toAsciiBytes(raw);
  }
  









  public Object encode(Object raw)
    throws EncoderException
  {
    if (!(raw instanceof byte[])) {
      throw new EncoderException("argument not a byte array");
    }
    return toAsciiChars((byte[])raw);
  }
  









  public Object decode(Object ascii)
    throws DecoderException
  {
    if (ascii == null) {
      return EMPTY_BYTE_ARRAY;
    }
    if ((ascii instanceof byte[])) {
      return fromAscii((byte[])ascii);
    }
    if ((ascii instanceof char[])) {
      return fromAscii((char[])ascii);
    }
    if ((ascii instanceof String)) {
      return fromAscii(((String)ascii).toCharArray());
    }
    throw new DecoderException("argument not a byte array");
  }
  








  public byte[] decode(byte[] ascii)
  {
    return fromAscii(ascii);
  }
  







  public byte[] toByteArray(String ascii)
  {
    if (ascii == null) {
      return EMPTY_BYTE_ARRAY;
    }
    return fromAscii(ascii.toCharArray());
  }
  











  public static byte[] fromAscii(char[] ascii)
  {
    if ((ascii == null) || (ascii.length == 0)) {
      return EMPTY_BYTE_ARRAY;
    }
    
    byte[] l_raw = new byte[ascii.length >> 3];
    



    int ii = 0; for (int jj = ascii.length - 1; ii < l_raw.length; jj -= 8) {
      for (int bits = 0; bits < BITS.length; bits++) {
        if (ascii[(jj - bits)] == '1') {
          int tmp58_57 = ii; byte[] tmp58_56 = l_raw;tmp58_56[tmp58_57] = ((byte)(tmp58_56[tmp58_57] | BITS[bits]));
        }
      }
      ii++;
    }
    




    return l_raw;
  }
  






  public static byte[] fromAscii(byte[] ascii)
  {
    if (isEmpty(ascii)) {
      return EMPTY_BYTE_ARRAY;
    }
    
    byte[] l_raw = new byte[ascii.length >> 3];
    



    int ii = 0; for (int jj = ascii.length - 1; ii < l_raw.length; jj -= 8) {
      for (int bits = 0; bits < BITS.length; bits++) {
        if (ascii[(jj - bits)] == 49) {
          int tmp56_55 = ii; byte[] tmp56_54 = l_raw;tmp56_54[tmp56_55] = ((byte)(tmp56_54[tmp56_55] | BITS[bits]));
        }
      }
      ii++;
    }
    




    return l_raw;
  }
  






  private static boolean isEmpty(byte[] array)
  {
    return (array == null) || (array.length == 0);
  }
  








  public static byte[] toAsciiBytes(byte[] raw)
  {
    if (isEmpty(raw)) {
      return EMPTY_BYTE_ARRAY;
    }
    
    byte[] l_ascii = new byte[raw.length << 3];
    



    int ii = 0; for (int jj = l_ascii.length - 1; ii < raw.length; jj -= 8) {
      for (int bits = 0; bits < BITS.length; bits++) {
        if ((raw[ii] & BITS[bits]) == 0) {
          l_ascii[(jj - bits)] = 48;
        } else {
          l_ascii[(jj - bits)] = 49;
        }
      }
      ii++;
    }
    






    return l_ascii;
  }
  







  public static char[] toAsciiChars(byte[] raw)
  {
    if (isEmpty(raw)) {
      return EMPTY_CHAR_ARRAY;
    }
    
    char[] l_ascii = new char[raw.length << 3];
    



    int ii = 0; for (int jj = l_ascii.length - 1; ii < raw.length; jj -= 8) {
      for (int bits = 0; bits < BITS.length; bits++) {
        if ((raw[ii] & BITS[bits]) == 0) {
          l_ascii[(jj - bits)] = '0';
        } else {
          l_ascii[(jj - bits)] = '1';
        }
      }
      ii++;
    }
    






    return l_ascii;
  }
  







  public static String toAsciiString(byte[] raw)
  {
    return new String(toAsciiChars(raw));
  }
}
