package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLString;

public class XML11EntityScanner
  extends XMLEntityScanner
{
  public XML11EntityScanner() {}
  
  public int peekChar()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.ch[fCurrentEntity.position];
    if (fCurrentEntity.isExternal()) {
      return (i != 13) && (i != 133) && (i != 8232) ? i : 10;
    }
    return i;
  }
  
  public int scanChar()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.ch[(fCurrentEntity.position++)];
    boolean bool = false;
    if ((i == 10) || (((i == 13) || (i == 133) || (i == 8232)) && ((bool = fCurrentEntity.isExternal()))))
    {
      fCurrentEntity.lineNumber += 1;
      fCurrentEntity.columnNumber = 1;
      if (fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = ((char)i);
        load(1, false);
      }
      if ((i == 13) && (bool))
      {
        int j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if ((j != 10) && (j != 133)) {
          fCurrentEntity.position -= 1;
        }
      }
      i = 10;
    }
    fCurrentEntity.columnNumber += 1;
    return i;
  }
  
  public String scanNmtoken()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    for (;;)
    {
      char c = fCurrentEntity.ch[fCurrentEntity.position];
      int k;
      char[] arrayOfChar1;
      if (XML11Char.isXML11Name(c))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          k = fCurrentEntity.position - i;
          if (k == fCurrentEntity.ch.length)
          {
            arrayOfChar1 = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar1, 0, k);
            fCurrentEntity.ch = arrayOfChar1;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, k);
          }
          i = 0;
          if (load(k, false)) {
            break;
          }
        }
      }
      else if (XML11Char.isXML11NameHighSurrogate(c))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          k = fCurrentEntity.position - i;
          if (k == fCurrentEntity.ch.length)
          {
            arrayOfChar1 = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar1, 0, k);
            fCurrentEntity.ch = arrayOfChar1;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, k);
          }
          i = 0;
          if (load(k, false))
          {
            fCurrentEntity.startPosition -= 1;
            fCurrentEntity.position -= 1;
            break;
          }
        }
        k = fCurrentEntity.ch[fCurrentEntity.position];
        if ((!XMLChar.isLowSurrogate(k)) || (!XML11Char.isXML11Name(XMLChar.supplemental(c, k))))
        {
          fCurrentEntity.position -= 1;
        }
        else if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          int m = fCurrentEntity.position - i;
          if (m == fCurrentEntity.ch.length)
          {
            char[] arrayOfChar2 = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar2, 0, m);
            fCurrentEntity.ch = arrayOfChar2;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, m);
          }
          i = 0;
          if (load(m, false)) {
            break;
          }
        }
      }
    }
    int j = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += j;
    String str = null;
    if (j > 0) {
      str = fSymbolTable.addSymbol(fCurrentEntity.ch, i, j);
    }
    return str;
  }
  
  public String scanName()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    char c1 = fCurrentEntity.ch[i];
    Object localObject;
    if (XML11Char.isXML11NameStart(c1))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.columnNumber += 1;
          String str1 = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
          return str1;
        }
      }
    }
    else if (XML11Char.isXML11NameHighSurrogate(c1))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.position -= 1;
          fCurrentEntity.startPosition -= 1;
          return null;
        }
      }
      char c2 = fCurrentEntity.ch[fCurrentEntity.position];
      if ((!XMLChar.isLowSurrogate(c2)) || (!XML11Char.isXML11NameStart(XMLChar.supplemental(c1, c2))))
      {
        fCurrentEntity.position -= 1;
        return null;
      }
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        fCurrentEntity.ch[1] = c2;
        i = 0;
        if (load(2, false))
        {
          fCurrentEntity.columnNumber += 2;
          localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 2);
          return localObject;
        }
      }
    }
    else
    {
      return null;
    }
    for (;;)
    {
      c1 = fCurrentEntity.ch[fCurrentEntity.position];
      int j;
      if (XML11Char.isXML11Name(c1))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          j = fCurrentEntity.position - i;
          if (j == fCurrentEntity.ch.length)
          {
            localObject = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, localObject, 0, j);
            fCurrentEntity.ch = ((char[])localObject);
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, j);
          }
          i = 0;
          if (load(j, false)) {
            break;
          }
        }
      }
      else if (XML11Char.isXML11NameHighSurrogate(c1))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          j = fCurrentEntity.position - i;
          if (j == fCurrentEntity.ch.length)
          {
            localObject = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, localObject, 0, j);
            fCurrentEntity.ch = ((char[])localObject);
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, j);
          }
          i = 0;
          if (load(j, false))
          {
            fCurrentEntity.position -= 1;
            fCurrentEntity.startPosition -= 1;
            break;
          }
        }
        j = fCurrentEntity.ch[fCurrentEntity.position];
        if ((!XMLChar.isLowSurrogate(j)) || (!XML11Char.isXML11Name(XMLChar.supplemental(c1, j))))
        {
          fCurrentEntity.position -= 1;
        }
        else if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          int m = fCurrentEntity.position - i;
          if (m == fCurrentEntity.ch.length)
          {
            char[] arrayOfChar = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar, 0, m);
            fCurrentEntity.ch = arrayOfChar;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, m);
          }
          i = 0;
          if (load(m, false)) {
            break;
          }
        }
      }
    }
    int k = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += k;
    String str2 = null;
    if (k > 0) {
      str2 = fSymbolTable.addSymbol(fCurrentEntity.ch, i, k);
    }
    return str2;
  }
  
  public String scanNCName()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    char c1 = fCurrentEntity.ch[i];
    Object localObject;
    if (XML11Char.isXML11NCNameStart(c1))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.columnNumber += 1;
          String str1 = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
          return str1;
        }
      }
    }
    else if (XML11Char.isXML11NameHighSurrogate(c1))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.position -= 1;
          fCurrentEntity.startPosition -= 1;
          return null;
        }
      }
      char c2 = fCurrentEntity.ch[fCurrentEntity.position];
      if ((!XMLChar.isLowSurrogate(c2)) || (!XML11Char.isXML11NCNameStart(XMLChar.supplemental(c1, c2))))
      {
        fCurrentEntity.position -= 1;
        return null;
      }
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        fCurrentEntity.ch[1] = c2;
        i = 0;
        if (load(2, false))
        {
          fCurrentEntity.columnNumber += 2;
          localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 2);
          return localObject;
        }
      }
    }
    else
    {
      return null;
    }
    for (;;)
    {
      c1 = fCurrentEntity.ch[fCurrentEntity.position];
      int j;
      if (XML11Char.isXML11NCName(c1))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          j = fCurrentEntity.position - i;
          if (j == fCurrentEntity.ch.length)
          {
            localObject = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, localObject, 0, j);
            fCurrentEntity.ch = ((char[])localObject);
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, j);
          }
          i = 0;
          if (load(j, false)) {
            break;
          }
        }
      }
      else if (XML11Char.isXML11NameHighSurrogate(c1))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          j = fCurrentEntity.position - i;
          if (j == fCurrentEntity.ch.length)
          {
            localObject = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, localObject, 0, j);
            fCurrentEntity.ch = ((char[])localObject);
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, j);
          }
          i = 0;
          if (load(j, false))
          {
            fCurrentEntity.startPosition -= 1;
            fCurrentEntity.position -= 1;
            break;
          }
        }
        j = fCurrentEntity.ch[fCurrentEntity.position];
        if ((!XMLChar.isLowSurrogate(j)) || (!XML11Char.isXML11NCName(XMLChar.supplemental(c1, j))))
        {
          fCurrentEntity.position -= 1;
        }
        else if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          int m = fCurrentEntity.position - i;
          if (m == fCurrentEntity.ch.length)
          {
            char[] arrayOfChar = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar, 0, m);
            fCurrentEntity.ch = arrayOfChar;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, m);
          }
          i = 0;
          if (load(m, false)) {
            break;
          }
        }
      }
    }
    int k = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += k;
    String str2 = null;
    if (k > 0) {
      str2 = fSymbolTable.addSymbol(fCurrentEntity.ch, i, k);
    }
    return str2;
  }
  
  public boolean scanQName(QName paramQName)
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    char c1 = fCurrentEntity.ch[i];
    if (XML11Char.isXML11NCNameStart(c1))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.columnNumber += 1;
          String str1 = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
          paramQName.setValues(null, str1, str1, null);
          return true;
        }
      }
    }
    else if (XML11Char.isXML11NameHighSurrogate(c1))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.startPosition -= 1;
          fCurrentEntity.position -= 1;
          return false;
        }
      }
      char c2 = fCurrentEntity.ch[fCurrentEntity.position];
      if ((!XMLChar.isLowSurrogate(c2)) || (!XML11Char.isXML11NCNameStart(XMLChar.supplemental(c1, c2))))
      {
        fCurrentEntity.position -= 1;
        return false;
      }
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = c1;
        fCurrentEntity.ch[1] = c2;
        i = 0;
        if (load(2, false))
        {
          fCurrentEntity.columnNumber += 2;
          String str2 = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 2);
          paramQName.setValues(null, str2, str2, null);
          return true;
        }
      }
    }
    else
    {
      return false;
    }
    int j = -1;
    int k = 0;
    Object localObject;
    for (;;)
    {
      c1 = fCurrentEntity.ch[fCurrentEntity.position];
      int m;
      char[] arrayOfChar;
      if (XML11Char.isXML11Name(c1))
      {
        if (c1 == ':')
        {
          if (j == -1) {
            j = fCurrentEntity.position;
          }
        }
        else if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          m = fCurrentEntity.position - i;
          if (m == fCurrentEntity.ch.length)
          {
            arrayOfChar = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar, 0, m);
            fCurrentEntity.ch = arrayOfChar;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, m);
          }
          if (j != -1) {
            j -= i;
          }
          i = 0;
          if (load(m, false)) {
            break;
          }
        }
      }
      else if (XML11Char.isXML11NameHighSurrogate(c1))
      {
        if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          m = fCurrentEntity.position - i;
          if (m == fCurrentEntity.ch.length)
          {
            arrayOfChar = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, arrayOfChar, 0, m);
            fCurrentEntity.ch = arrayOfChar;
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, m);
          }
          if (j != -1) {
            j -= i;
          }
          i = 0;
          if (load(m, false))
          {
            k = 1;
            fCurrentEntity.startPosition -= 1;
            fCurrentEntity.position -= 1;
            break;
          }
        }
        m = fCurrentEntity.ch[fCurrentEntity.position];
        if ((!XMLChar.isLowSurrogate(m)) || (!XML11Char.isXML11Name(XMLChar.supplemental(c1, m))))
        {
          k = 1;
          fCurrentEntity.position -= 1;
        }
        else if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          int i1 = fCurrentEntity.position - i;
          if (i1 == fCurrentEntity.ch.length)
          {
            localObject = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, localObject, 0, i1);
            fCurrentEntity.ch = ((char[])localObject);
          }
          else
          {
            System.arraycopy(fCurrentEntity.ch, i, fCurrentEntity.ch, 0, i1);
          }
          if (j != -1) {
            j -= i;
          }
          i = 0;
          if (load(i1, false)) {
            break;
          }
        }
      }
    }
    int n = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += n;
    if (n > 0)
    {
      String str3 = null;
      localObject = null;
      String str4 = fSymbolTable.addSymbol(fCurrentEntity.ch, i, n);
      if (j != -1)
      {
        int i2 = j - i;
        str3 = fSymbolTable.addSymbol(fCurrentEntity.ch, i, i2);
        int i3 = n - i2 - 1;
        int i4 = j + 1;
        if ((!XML11Char.isXML11NCNameStart(fCurrentEntity.ch[i4])) && ((!XML11Char.isXML11NameHighSurrogate(fCurrentEntity.ch[i4])) || (k != 0))) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", null, (short)2);
        }
        localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, j + 1, i3);
      }
      else
      {
        localObject = str4;
      }
      paramQName.setValues(str3, (String)localObject, str4, null);
      return true;
    }
    return false;
  }
  
  public int scanContent(XMLString paramXMLString)
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count)
    {
      load(0, true);
    }
    else if (fCurrentEntity.position == fCurrentEntity.count - 1)
    {
      fCurrentEntity.ch[0] = fCurrentEntity.ch[(fCurrentEntity.count - 1)];
      load(1, false);
      fCurrentEntity.position = 0;
      fCurrentEntity.startPosition = 0;
    }
    int i = fCurrentEntity.position;
    int j = fCurrentEntity.ch[i];
    int k = 0;
    boolean bool = fCurrentEntity.isExternal();
    if ((j == 10) || (((j == 13) || (j == 133) || (j == 8232)) && (bool)))
    {
      do
      {
        j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if ((j == 13) && (bool))
        {
          k++;
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count)
          {
            i = 0;
            fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
            fCurrentEntity.position = k;
            fCurrentEntity.startPosition = k;
            if (load(k, false)) {
              break;
            }
          }
          m = fCurrentEntity.ch[fCurrentEntity.position];
          if ((m == 10) || (m == 133))
          {
            fCurrentEntity.position += 1;
            i++;
          }
          else
          {
            k++;
          }
        }
        else if ((j == 10) || (((j == 133) || (j == 8232)) && (bool)))
        {
          k++;
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count)
          {
            i = 0;
            fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
            fCurrentEntity.position = k;
            fCurrentEntity.startPosition = k;
            if (load(k, false)) {
              break;
            }
          }
        }
        else
        {
          fCurrentEntity.position -= 1;
          break;
        }
      } while (fCurrentEntity.position < fCurrentEntity.count - 1);
      for (m = i; m < fCurrentEntity.position; m++) {
        fCurrentEntity.ch[m] = '\n';
      }
      int n = fCurrentEntity.position - i;
      if (fCurrentEntity.position == fCurrentEntity.count - 1)
      {
        paramXMLString.setValues(fCurrentEntity.ch, i, n);
        return -1;
      }
    }
    if (bool) {
      while (fCurrentEntity.position < fCurrentEntity.count)
      {
        j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if ((!XML11Char.isXML11Content(j)) || (j == 133) || (j == 8232))
        {
          fCurrentEntity.position -= 1;
          break;
        }
      }
    } else {
      while (fCurrentEntity.position < fCurrentEntity.count)
      {
        j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if (!XML11Char.isXML11InternalEntityContent(j))
        {
          fCurrentEntity.position -= 1;
          break;
        }
      }
    }
    int m = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += m - k;
    paramXMLString.setValues(fCurrentEntity.ch, i, m);
    if (fCurrentEntity.position != fCurrentEntity.count)
    {
      j = fCurrentEntity.ch[fCurrentEntity.position];
      if (((j == 13) || (j == 133) || (j == 8232)) && (bool)) {
        j = 10;
      }
    }
    else
    {
      j = -1;
    }
    return j;
  }
  
  public int scanLiteral(int paramInt, XMLString paramXMLString)
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count)
    {
      load(0, true);
    }
    else if (fCurrentEntity.position == fCurrentEntity.count - 1)
    {
      fCurrentEntity.ch[0] = fCurrentEntity.ch[(fCurrentEntity.count - 1)];
      load(1, false);
      fCurrentEntity.startPosition = 0;
      fCurrentEntity.position = 0;
    }
    int i = fCurrentEntity.position;
    int j = fCurrentEntity.ch[i];
    int k = 0;
    boolean bool = fCurrentEntity.isExternal();
    if ((j == 10) || (((j == 13) || (j == 133) || (j == 8232)) && (bool)))
    {
      do
      {
        j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if ((j == 13) && (bool))
        {
          k++;
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count)
          {
            i = 0;
            fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
            fCurrentEntity.position = k;
            fCurrentEntity.startPosition = k;
            if (load(k, false)) {
              break;
            }
          }
          m = fCurrentEntity.ch[fCurrentEntity.position];
          if ((m == 10) || (m == 133))
          {
            fCurrentEntity.position += 1;
            i++;
          }
          else
          {
            k++;
          }
        }
        else if ((j == 10) || (((j == 133) || (j == 8232)) && (bool)))
        {
          k++;
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count)
          {
            i = 0;
            fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
            fCurrentEntity.position = k;
            fCurrentEntity.startPosition = k;
            if (load(k, false)) {
              break;
            }
          }
        }
        else
        {
          fCurrentEntity.position -= 1;
          break;
        }
      } while (fCurrentEntity.position < fCurrentEntity.count - 1);
      for (m = i; m < fCurrentEntity.position; m++) {
        fCurrentEntity.ch[m] = '\n';
      }
      int n = fCurrentEntity.position - i;
      if (fCurrentEntity.position == fCurrentEntity.count - 1)
      {
        paramXMLString.setValues(fCurrentEntity.ch, i, n);
        return -1;
      }
    }
    if (bool) {
      while (fCurrentEntity.position < fCurrentEntity.count)
      {
        j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if ((j == paramInt) || (j == 37) || (!XML11Char.isXML11Content(j)) || (j == 133) || (j == 8232))
        {
          fCurrentEntity.position -= 1;
          break;
        }
      }
    } else {
      while (fCurrentEntity.position < fCurrentEntity.count)
      {
        j = fCurrentEntity.ch[(fCurrentEntity.position++)];
        if (((j == paramInt) && (!fCurrentEntity.literal)) || (j == 37) || (!XML11Char.isXML11InternalEntityContent(j)))
        {
          fCurrentEntity.position -= 1;
          break;
        }
      }
    }
    int m = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += m - k;
    paramXMLString.setValues(fCurrentEntity.ch, i, m);
    if (fCurrentEntity.position != fCurrentEntity.count)
    {
      j = fCurrentEntity.ch[fCurrentEntity.position];
      if ((j == paramInt) && (fCurrentEntity.literal)) {
        j = -1;
      }
    }
    else
    {
      j = -1;
    }
    return j;
  }
  
  public boolean scanData(String paramString, XMLStringBuffer paramXMLStringBuffer)
    throws IOException
  {
    int i = 0;
    int j = paramString.length();
    int k = paramString.charAt(0);
    boolean bool1 = fCurrentEntity.isExternal();
    label1381:
    do
    {
      if (fCurrentEntity.position == fCurrentEntity.count) {
        load(0, true);
      }
      boolean bool2 = false;
      while ((fCurrentEntity.position >= fCurrentEntity.count - j) && (!bool2))
      {
        System.arraycopy(fCurrentEntity.ch, fCurrentEntity.position, fCurrentEntity.ch, 0, fCurrentEntity.count - fCurrentEntity.position);
        bool2 = load(fCurrentEntity.count - fCurrentEntity.position, false);
        fCurrentEntity.position = 0;
        fCurrentEntity.startPosition = 0;
      }
      if (fCurrentEntity.position >= fCurrentEntity.count - j)
      {
        m = fCurrentEntity.count - fCurrentEntity.position;
        paramXMLStringBuffer.append(fCurrentEntity.ch, fCurrentEntity.position, m);
        fCurrentEntity.columnNumber += fCurrentEntity.count;
        fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
        fCurrentEntity.position = fCurrentEntity.count;
        fCurrentEntity.startPosition = fCurrentEntity.count;
        load(0, true);
        return false;
      }
      int m = fCurrentEntity.position;
      int n = fCurrentEntity.ch[m];
      int i1 = 0;
      int i3;
      if ((n == 10) || (((n == 13) || (n == 133) || (n == 8232)) && (bool1)))
      {
        do
        {
          n = fCurrentEntity.ch[(fCurrentEntity.position++)];
          if ((n == 13) && (bool1))
          {
            i1++;
            fCurrentEntity.lineNumber += 1;
            fCurrentEntity.columnNumber = 1;
            if (fCurrentEntity.position == fCurrentEntity.count)
            {
              m = 0;
              fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
              fCurrentEntity.position = i1;
              fCurrentEntity.startPosition = i1;
              if (load(i1, false)) {
                break;
              }
            }
            i2 = fCurrentEntity.ch[fCurrentEntity.position];
            if ((i2 == 10) || (i2 == 133))
            {
              fCurrentEntity.position += 1;
              m++;
            }
            else
            {
              i1++;
            }
          }
          else if ((n == 10) || (((n == 133) || (n == 8232)) && (bool1)))
          {
            i1++;
            fCurrentEntity.lineNumber += 1;
            fCurrentEntity.columnNumber = 1;
            if (fCurrentEntity.position == fCurrentEntity.count)
            {
              m = 0;
              fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
              fCurrentEntity.position = i1;
              fCurrentEntity.startPosition = i1;
              fCurrentEntity.count = i1;
              if (load(i1, false)) {
                break;
              }
            }
          }
          else
          {
            fCurrentEntity.position -= 1;
            break;
          }
        } while (fCurrentEntity.position < fCurrentEntity.count - 1);
        for (i2 = m; i2 < fCurrentEntity.position; i2++) {
          fCurrentEntity.ch[i2] = '\n';
        }
        i3 = fCurrentEntity.position - m;
        if (fCurrentEntity.position == fCurrentEntity.count - 1)
        {
          paramXMLStringBuffer.append(fCurrentEntity.ch, m, i3);
          return true;
        }
      }
      if (bool1) {
        while (fCurrentEntity.position < fCurrentEntity.count)
        {
          n = fCurrentEntity.ch[(fCurrentEntity.position++)];
          if (n == k)
          {
            i2 = fCurrentEntity.position - 1;
            for (i3 = 1; i3 < j; i3++)
            {
              if (fCurrentEntity.position == fCurrentEntity.count)
              {
                fCurrentEntity.position -= i3;
                break label1381;
              }
              n = fCurrentEntity.ch[(fCurrentEntity.position++)];
              if (paramString.charAt(i3) != n)
              {
                fCurrentEntity.position -= 1;
                break;
              }
            }
            if (fCurrentEntity.position == i2 + j)
            {
              i = 1;
              break;
            }
          }
          else
          {
            if ((n == 10) || (n == 13) || (n == 133) || (n == 8232))
            {
              fCurrentEntity.position -= 1;
              break;
            }
            if (!XML11Char.isXML11ValidLiteral(n))
            {
              fCurrentEntity.position -= 1;
              i2 = fCurrentEntity.position - m;
              fCurrentEntity.columnNumber += i2 - i1;
              paramXMLStringBuffer.append(fCurrentEntity.ch, m, i2);
              return true;
            }
          }
        }
      } else {
        while (fCurrentEntity.position < fCurrentEntity.count)
        {
          n = fCurrentEntity.ch[(fCurrentEntity.position++)];
          if (n == k)
          {
            i2 = fCurrentEntity.position - 1;
            for (i3 = 1; i3 < j; i3++)
            {
              if (fCurrentEntity.position == fCurrentEntity.count)
              {
                fCurrentEntity.position -= i3;
                break label1381;
              }
              n = fCurrentEntity.ch[(fCurrentEntity.position++)];
              if (paramString.charAt(i3) != n)
              {
                fCurrentEntity.position -= 1;
                break;
              }
            }
            if (fCurrentEntity.position == i2 + j)
            {
              i = 1;
              break;
            }
          }
          else
          {
            if (n == 10)
            {
              fCurrentEntity.position -= 1;
              break;
            }
            if (!XML11Char.isXML11Valid(n))
            {
              fCurrentEntity.position -= 1;
              i2 = fCurrentEntity.position - m;
              fCurrentEntity.columnNumber += i2 - i1;
              paramXMLStringBuffer.append(fCurrentEntity.ch, m, i2);
              return true;
            }
          }
        }
      }
      int i2 = fCurrentEntity.position - m;
      fCurrentEntity.columnNumber += i2 - i1;
      if (i != 0) {
        i2 -= j;
      }
      paramXMLStringBuffer.append(fCurrentEntity.ch, m, i2);
    } while (i == 0);
    return i == 0;
  }
  
  public boolean skipChar(int paramInt)
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.ch[fCurrentEntity.position];
    if (i == paramInt)
    {
      fCurrentEntity.position += 1;
      if (paramInt == 10)
      {
        fCurrentEntity.lineNumber += 1;
        fCurrentEntity.columnNumber = 1;
      }
      else
      {
        fCurrentEntity.columnNumber += 1;
      }
      return true;
    }
    if ((paramInt == 10) && ((i == 8232) || (i == 133)) && (fCurrentEntity.isExternal()))
    {
      fCurrentEntity.position += 1;
      fCurrentEntity.lineNumber += 1;
      fCurrentEntity.columnNumber = 1;
      return true;
    }
    if ((paramInt == 10) && (i == 13) && (fCurrentEntity.isExternal()))
    {
      if (fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = ((char)i);
        load(1, false);
      }
      int j = fCurrentEntity.ch[(++fCurrentEntity.position)];
      if ((j == 10) || (j == 133)) {
        fCurrentEntity.position += 1;
      }
      fCurrentEntity.lineNumber += 1;
      fCurrentEntity.columnNumber = 1;
      return true;
    }
    return false;
  }
  
  public boolean skipSpaces()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.ch[fCurrentEntity.position];
    boolean bool;
    if (fCurrentEntity.isExternal())
    {
      if (XML11Char.isXML11Space(i))
      {
        do
        {
          bool = false;
          if ((i == 10) || (i == 13) || (i == 133) || (i == 8232))
          {
            fCurrentEntity.lineNumber += 1;
            fCurrentEntity.columnNumber = 1;
            if (fCurrentEntity.position == fCurrentEntity.count - 1)
            {
              fCurrentEntity.ch[0] = ((char)i);
              bool = load(1, true);
              if (!bool)
              {
                fCurrentEntity.startPosition = 0;
                fCurrentEntity.position = 0;
              }
            }
            if (i == 13)
            {
              int j = fCurrentEntity.ch[(++fCurrentEntity.position)];
              if ((j != 10) && (j != 133)) {
                fCurrentEntity.position -= 1;
              }
            }
          }
          else
          {
            fCurrentEntity.columnNumber += 1;
          }
          if (!bool) {
            fCurrentEntity.position += 1;
          }
          if (fCurrentEntity.position == fCurrentEntity.count) {
            load(0, true);
          }
        } while (XML11Char.isXML11Space(i = fCurrentEntity.ch[fCurrentEntity.position]));
        return true;
      }
    }
    else if (XMLChar.isSpace(i))
    {
      do
      {
        bool = false;
        if (i == 10)
        {
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count - 1)
          {
            fCurrentEntity.ch[0] = ((char)i);
            bool = load(1, true);
            if (!bool)
            {
              fCurrentEntity.startPosition = 0;
              fCurrentEntity.position = 0;
            }
          }
        }
        else
        {
          fCurrentEntity.columnNumber += 1;
        }
        if (!bool) {
          fCurrentEntity.position += 1;
        }
        if (fCurrentEntity.position == fCurrentEntity.count) {
          load(0, true);
        }
      } while (XMLChar.isSpace(i = fCurrentEntity.ch[fCurrentEntity.position]));
      return true;
    }
    return false;
  }
  
  public boolean skipString(String paramString)
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = fCurrentEntity.ch[(fCurrentEntity.position++)];
      if (k != paramString.charAt(j))
      {
        fCurrentEntity.position -= j + 1;
        return false;
      }
      if ((j < i - 1) && (fCurrentEntity.position == fCurrentEntity.count))
      {
        System.arraycopy(fCurrentEntity.ch, fCurrentEntity.count - j - 1, fCurrentEntity.ch, 0, j + 1);
        if (load(j + 1, false))
        {
          fCurrentEntity.startPosition -= j + 1;
          fCurrentEntity.position -= j + 1;
          return false;
        }
      }
    }
    fCurrentEntity.columnNumber += i;
    return true;
  }
}
