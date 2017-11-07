package org.apache.xerces.util;

import org.apache.xerces.xni.XMLString;

public class XMLStringBuffer
  extends XMLString
{
  public static final int DEFAULT_SIZE = 32;
  
  public XMLStringBuffer()
  {
    this(32);
  }
  
  public XMLStringBuffer(int paramInt)
  {
    ch = new char[paramInt];
  }
  
  public XMLStringBuffer(char paramChar)
  {
    this(1);
    append(paramChar);
  }
  
  public XMLStringBuffer(String paramString)
  {
    this(paramString.length());
    append(paramString);
  }
  
  public XMLStringBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    this(paramInt2);
    append(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  public XMLStringBuffer(XMLString paramXMLString)
  {
    this(length);
    append(paramXMLString);
  }
  
  public void clear()
  {
    offset = 0;
    length = 0;
  }
  
  public void append(char paramChar)
  {
    if (length + 1 > ch.length)
    {
      int i = ch.length * 2;
      if (i < ch.length + 32) {
        i = ch.length + 32;
      }
      char[] arrayOfChar = new char[i];
      System.arraycopy(ch, 0, arrayOfChar, 0, length);
      ch = arrayOfChar;
    }
    ch[length] = paramChar;
    length += 1;
  }
  
  public void append(String paramString)
  {
    int i = paramString.length();
    if (length + i > ch.length)
    {
      int j = ch.length * 2;
      if (j < length + i + 32) {
        j = ch.length + i + 32;
      }
      char[] arrayOfChar = new char[j];
      System.arraycopy(ch, 0, arrayOfChar, 0, length);
      ch = arrayOfChar;
    }
    paramString.getChars(0, i, ch, length);
    length += i;
  }
  
  public void append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (length + paramInt2 > ch.length)
    {
      char[] arrayOfChar = new char[ch.length + paramInt2 + 32];
      System.arraycopy(ch, 0, arrayOfChar, 0, length);
      ch = arrayOfChar;
    }
    System.arraycopy(paramArrayOfChar, paramInt1, ch, length, paramInt2);
    length += paramInt2;
  }
  
  public void append(XMLString paramXMLString)
  {
    append(ch, offset, length);
  }
}
