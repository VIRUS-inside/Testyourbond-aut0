package org.apache.xerces.impl.dv.util;

public final class Base64
{
  private static final int BASELENGTH = 128;
  private static final int LOOKUPLENGTH = 64;
  private static final int TWENTYFOURBITGROUP = 24;
  private static final int EIGHTBIT = 8;
  private static final int SIXTEENBIT = 16;
  private static final int SIXBIT = 6;
  private static final int FOURBYTE = 4;
  private static final int SIGN = -128;
  private static final char PAD = '=';
  private static final boolean fDebug = false;
  private static final byte[] base64Alphabet = new byte[''];
  private static final char[] lookUpBase64Alphabet = new char[64];
  
  public Base64() {}
  
  protected static boolean isWhiteSpace(char paramChar)
  {
    return (paramChar == ' ') || (paramChar == '\r') || (paramChar == '\n') || (paramChar == '\t');
  }
  
  protected static boolean isPad(char paramChar)
  {
    return paramChar == '=';
  }
  
  protected static boolean isData(char paramChar)
  {
    return (paramChar < '') && (base64Alphabet[paramChar] != -1);
  }
  
  protected static boolean isBase64(char paramChar)
  {
    return (isWhiteSpace(paramChar)) || (isPad(paramChar)) || (isData(paramChar));
  }
  
  public static String encode(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    int i = paramArrayOfByte.length * 8;
    if (i == 0) {
      return "";
    }
    int j = i % 24;
    int k = i / 24;
    int m = j != 0 ? k + 1 : k;
    char[] arrayOfChar = null;
    arrayOfChar = new char[m * 4];
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i8;
    int i9;
    for (int i7 = 0; i7 < k; i7++)
    {
      i2 = paramArrayOfByte[(i6++)];
      i3 = paramArrayOfByte[(i6++)];
      i4 = paramArrayOfByte[(i6++)];
      i1 = (byte)(i3 & 0xF);
      n = (byte)(i2 & 0x3);
      i8 = (i2 & 0xFFFFFF80) == 0 ? (byte)(i2 >> 2) : (byte)(i2 >> 2 ^ 0xC0);
      i9 = (i3 & 0xFFFFFF80) == 0 ? (byte)(i3 >> 4) : (byte)(i3 >> 4 ^ 0xF0);
      int i10 = (i4 & 0xFFFFFF80) == 0 ? (byte)(i4 >> 6) : (byte)(i4 >> 6 ^ 0xFC);
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[i8];
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[(i9 | n << 4)];
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[(i1 << 2 | i10)];
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[(i4 & 0x3F)];
    }
    if (j == 8)
    {
      i2 = paramArrayOfByte[i6];
      n = (byte)(i2 & 0x3);
      i8 = (i2 & 0xFFFFFF80) == 0 ? (byte)(i2 >> 2) : (byte)(i2 >> 2 ^ 0xC0);
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[i8];
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[(n << 4)];
      arrayOfChar[(i5++)] = '=';
      arrayOfChar[(i5++)] = '=';
    }
    else if (j == 16)
    {
      i2 = paramArrayOfByte[i6];
      i3 = paramArrayOfByte[(i6 + 1)];
      i1 = (byte)(i3 & 0xF);
      n = (byte)(i2 & 0x3);
      i8 = (i2 & 0xFFFFFF80) == 0 ? (byte)(i2 >> 2) : (byte)(i2 >> 2 ^ 0xC0);
      i9 = (i3 & 0xFFFFFF80) == 0 ? (byte)(i3 >> 4) : (byte)(i3 >> 4 ^ 0xF0);
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[i8];
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[(i9 | n << 4)];
      arrayOfChar[(i5++)] = lookUpBase64Alphabet[(i1 << 2)];
      arrayOfChar[(i5++)] = '=';
    }
    return new String(arrayOfChar);
  }
  
  public static byte[] decode(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    char[] arrayOfChar = paramString.toCharArray();
    int i = removeWhiteSpace(arrayOfChar);
    if (i % 4 != 0) {
      return null;
    }
    int j = i / 4;
    if (j == 0) {
      return new byte[0];
    }
    byte[] arrayOfByte1 = null;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    char c1 = '\000';
    char c2 = '\000';
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    arrayOfByte1 = new byte[j * 3];
    while (i4 < j - 1)
    {
      if ((!isData(i2 = arrayOfChar[(i6++)])) || (!isData(i3 = arrayOfChar[(i6++)])) || (!isData(c1 = arrayOfChar[(i6++)])) || (!isData(c2 = arrayOfChar[(i6++)]))) {
        return null;
      }
      k = base64Alphabet[i2];
      m = base64Alphabet[i3];
      n = base64Alphabet[c1];
      i1 = base64Alphabet[c2];
      arrayOfByte1[(i5++)] = ((byte)(k << 2 | m >> 4));
      arrayOfByte1[(i5++)] = ((byte)((m & 0xF) << 4 | n >> 2 & 0xF));
      arrayOfByte1[(i5++)] = ((byte)(n << 6 | i1));
      i4++;
    }
    if ((!isData(i2 = arrayOfChar[(i6++)])) || (!isData(i3 = arrayOfChar[(i6++)]))) {
      return null;
    }
    k = base64Alphabet[i2];
    m = base64Alphabet[i3];
    c1 = arrayOfChar[(i6++)];
    c2 = arrayOfChar[(i6++)];
    if ((!isData(c1)) || (!isData(c2)))
    {
      byte[] arrayOfByte2;
      if ((isPad(c1)) && (isPad(c2)))
      {
        if ((m & 0xF) != 0) {
          return null;
        }
        arrayOfByte2 = new byte[i4 * 3 + 1];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i4 * 3);
        arrayOfByte2[i5] = ((byte)(k << 2 | m >> 4));
        return arrayOfByte2;
      }
      if ((!isPad(c1)) && (isPad(c2)))
      {
        n = base64Alphabet[c1];
        if ((n & 0x3) != 0) {
          return null;
        }
        arrayOfByte2 = new byte[i4 * 3 + 2];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i4 * 3);
        arrayOfByte2[(i5++)] = ((byte)(k << 2 | m >> 4));
        arrayOfByte2[i5] = ((byte)((m & 0xF) << 4 | n >> 2 & 0xF));
        return arrayOfByte2;
      }
      return null;
    }
    n = base64Alphabet[c1];
    i1 = base64Alphabet[c2];
    arrayOfByte1[(i5++)] = ((byte)(k << 2 | m >> 4));
    arrayOfByte1[(i5++)] = ((byte)((m & 0xF) << 4 | n >> 2 & 0xF));
    arrayOfByte1[(i5++)] = ((byte)(n << 6 | i1));
    return arrayOfByte1;
  }
  
  protected static int removeWhiteSpace(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar == null) {
      return 0;
    }
    int i = 0;
    int j = paramArrayOfChar.length;
    for (int k = 0; k < j; k++) {
      if (!isWhiteSpace(paramArrayOfChar[k])) {
        paramArrayOfChar[(i++)] = paramArrayOfChar[k];
      }
    }
    return i;
  }
  
  static
  {
    for (int i = 0; i < 128; i++) {
      base64Alphabet[i] = -1;
    }
    for (int j = 90; j >= 65; j--) {
      base64Alphabet[j] = ((byte)(j - 65));
    }
    for (int k = 122; k >= 97; k--) {
      base64Alphabet[k] = ((byte)(k - 97 + 26));
    }
    for (int m = 57; m >= 48; m--) {
      base64Alphabet[m] = ((byte)(m - 48 + 52));
    }
    base64Alphabet[43] = 62;
    base64Alphabet[47] = 63;
    for (int n = 0; n <= 25; n++) {
      lookUpBase64Alphabet[n] = ((char)(65 + n));
    }
    int i1 = 26;
    for (int i2 = 0; i1 <= 51; i2++)
    {
      lookUpBase64Alphabet[i1] = ((char)(97 + i2));
      i1++;
    }
    int i3 = 52;
    for (int i4 = 0; i3 <= 61; i4++)
    {
      lookUpBase64Alphabet[i3] = ((char)(48 + i4));
      i3++;
    }
    lookUpBase64Alphabet[62] = '+';
    lookUpBase64Alphabet[63] = '/';
  }
}
