package org.apache.xerces.xni;

public class XMLString
{
  public char[] ch;
  public int offset;
  public int length;
  
  public XMLString() {}
  
  public XMLString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    setValues(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  public XMLString(XMLString paramXMLString)
  {
    setValues(paramXMLString);
  }
  
  public void setValues(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    ch = paramArrayOfChar;
    offset = paramInt1;
    length = paramInt2;
  }
  
  public void setValues(XMLString paramXMLString)
  {
    setValues(ch, offset, length);
  }
  
  public void clear()
  {
    ch = null;
    offset = 0;
    length = -1;
  }
  
  public boolean equals(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (paramArrayOfChar == null) {
      return false;
    }
    if (length != paramInt2) {
      return false;
    }
    for (int i = 0; i < paramInt2; i++) {
      if (ch[(offset + i)] != paramArrayOfChar[(paramInt1 + i)]) {
        return false;
      }
    }
    return true;
  }
  
  public boolean equals(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    if (length != paramString.length()) {
      return false;
    }
    for (int i = 0; i < length; i++) {
      if (ch[(offset + i)] != paramString.charAt(i)) {
        return false;
      }
    }
    return true;
  }
  
  public String toString()
  {
    return length > 0 ? new String(ch, offset, length) : "";
  }
}
