package org.apache.commons.codec.binary;














public class Base32
  extends BaseNCodec
{
  private static final int BITS_PER_ENCODED_BYTE = 5;
  












  private static final int BYTES_PER_ENCODED_BLOCK = 8;
  












  private static final int BYTES_PER_UNENCODED_BLOCK = 5;
  











  private static final byte[] CHUNK_SEPARATOR = { 13, 10 };
  





  private static final byte[] DECODE_TABLE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
  












  private static final byte[] ENCODE_TABLE = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };
  









  private static final byte[] HEX_DECODE_TABLE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
  












  private static final byte[] HEX_ENCODE_TABLE = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86 };
  






  private static final int MASK_5BITS = 31;
  





  private final int decodeSize;
  





  private final byte[] decodeTable;
  





  private final int encodeSize;
  





  private final byte[] encodeTable;
  





  private final byte[] lineSeparator;
  






  public Base32()
  {
    this(false);
  }
  






  public Base32(byte pad)
  {
    this(false, pad);
  }
  






  public Base32(boolean useHex)
  {
    this(0, null, useHex, (byte)61);
  }
  







  public Base32(boolean useHex, byte pad)
  {
    this(0, null, useHex, pad);
  }
  










  public Base32(int lineLength)
  {
    this(lineLength, CHUNK_SEPARATOR);
  }
  

















  public Base32(int lineLength, byte[] lineSeparator)
  {
    this(lineLength, lineSeparator, false, (byte)61);
  }
  




















  public Base32(int lineLength, byte[] lineSeparator, boolean useHex)
  {
    this(lineLength, lineSeparator, useHex, (byte)61);
  }
  





















  public Base32(int lineLength, byte[] lineSeparator, boolean useHex, byte pad)
  {
    super(5, 8, lineLength, lineSeparator == null ? 0 : lineSeparator.length, pad);
    
    if (useHex) {
      encodeTable = HEX_ENCODE_TABLE;
      decodeTable = HEX_DECODE_TABLE;
    } else {
      encodeTable = ENCODE_TABLE;
      decodeTable = DECODE_TABLE;
    }
    if (lineLength > 0) {
      if (lineSeparator == null) {
        throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
      }
      
      if (containsAlphabetOrPad(lineSeparator)) {
        String sep = StringUtils.newStringUtf8(lineSeparator);
        throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + sep + "]");
      }
      encodeSize = (8 + lineSeparator.length);
      this.lineSeparator = new byte[lineSeparator.length];
      System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
    } else {
      encodeSize = 8;
      this.lineSeparator = null;
    }
    decodeSize = (encodeSize - 1);
    
    if ((isInAlphabet(pad)) || (isWhiteSpace(pad))) {
      throw new IllegalArgumentException("pad must not be in alphabet or whitespace");
    }
  }
  
























  void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context)
  {
    if (eof) {
      return;
    }
    if (inAvail < 0) {
      eof = true;
    }
    for (int i = 0; i < inAvail; i++) {
      byte b = in[(inPos++)];
      if (b == pad)
      {
        eof = true;
        break;
      }
      byte[] buffer = ensureBufferSize(decodeSize, context);
      if ((b >= 0) && (b < decodeTable.length)) {
        int result = decodeTable[b];
        if (result >= 0) {
          modulus = ((modulus + 1) % 8);
          
          lbitWorkArea = ((lbitWorkArea << 5) + result);
          if (modulus == 0) {
            buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 32 & 0xFF));
            buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 24 & 0xFF));
            buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 16 & 0xFF));
            buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 8 & 0xFF));
            buffer[(pos++)] = ((byte)(int)(lbitWorkArea & 0xFF));
          }
        }
      }
    }
    




    if ((eof) && (modulus >= 2)) {
      byte[] buffer = ensureBufferSize(decodeSize, context);
      

      switch (modulus) {
      case 2: 
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 2 & 0xFF));
        break;
      case 3: 
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 7 & 0xFF));
        break;
      case 4: 
        lbitWorkArea >>= 4;
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 8 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea & 0xFF));
        break;
      case 5: 
        lbitWorkArea >>= 1;
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 16 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 8 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea & 0xFF));
        break;
      case 6: 
        lbitWorkArea >>= 6;
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 16 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 8 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea & 0xFF));
        break;
      case 7: 
        lbitWorkArea >>= 3;
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 24 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 16 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea >> 8 & 0xFF));
        buffer[(pos++)] = ((byte)(int)(lbitWorkArea & 0xFF));
        break;
      
      default: 
        throw new IllegalStateException("Impossible modulus " + modulus);
      }
      
    }
  }
  
















  void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context)
  {
    if (eof) {
      return;
    }
    

    if (inAvail < 0) {
      eof = true;
      if ((0 == modulus) && (lineLength == 0)) {
        return;
      }
      byte[] buffer = ensureBufferSize(encodeSize, context);
      int savedPos = pos;
      switch (modulus) {
      case 0: 
        break;
      case 1: 
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 3) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea << 2) & 0x1F)];
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        break;
      case 2: 
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 11) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 6) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 1) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea << 4) & 0x1F)];
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        break;
      case 3: 
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 19) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 14) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 9) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 4) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea << 1) & 0x1F)];
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        buffer[(pos++)] = pad;
        break;
      case 4: 
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 27) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 22) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 17) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 12) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 7) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 2) & 0x1F)];
        buffer[(pos++)] = encodeTable[((int)(lbitWorkArea << 3) & 0x1F)];
        buffer[(pos++)] = pad;
        break;
      default: 
        throw new IllegalStateException("Impossible modulus " + modulus);
      }
      currentLinePos += pos - savedPos;
      
      if ((lineLength > 0) && (currentLinePos > 0)) {
        System.arraycopy(lineSeparator, 0, buffer, pos, lineSeparator.length);
        pos += lineSeparator.length;
      }
    } else {
      for (int i = 0; i < inAvail; i++) {
        byte[] buffer = ensureBufferSize(encodeSize, context);
        modulus = ((modulus + 1) % 5);
        int b = in[(inPos++)];
        if (b < 0) {
          b += 256;
        }
        lbitWorkArea = ((lbitWorkArea << 8) + b);
        if (0 == modulus) {
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 35) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 30) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 25) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 20) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 15) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 10) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)(lbitWorkArea >> 5) & 0x1F)];
          buffer[(pos++)] = encodeTable[((int)lbitWorkArea & 0x1F)];
          currentLinePos += 8;
          if ((lineLength > 0) && (lineLength <= currentLinePos)) {
            System.arraycopy(lineSeparator, 0, buffer, pos, lineSeparator.length);
            pos += lineSeparator.length;
            currentLinePos = 0;
          }
        }
      }
    }
  }
  







  public boolean isInAlphabet(byte octet)
  {
    return (octet >= 0) && (octet < decodeTable.length) && (decodeTable[octet] != -1);
  }
}
