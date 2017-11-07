package org.apache.xerces.impl;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.impl.io.UCSReader;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;

public class XMLEntityScanner
  implements XMLLocator
{
  private static final boolean DEBUG_ENCODINGS = false;
  private static final boolean DEBUG_BUFFER = false;
  private static final EOFException END_OF_DOCUMENT_ENTITY = new EOFException()
  {
    private static final long serialVersionUID = 980337771224675268L;
    
    public Throwable fillInStackTrace()
    {
      return this;
    }
  };
  private XMLEntityManager fEntityManager = null;
  protected XMLEntityManager.ScannedEntity fCurrentEntity = null;
  protected SymbolTable fSymbolTable = null;
  protected int fBufferSize = 2048;
  protected XMLErrorReporter fErrorReporter;
  
  public XMLEntityScanner() {}
  
  public final String getBaseSystemId()
  {
    return (fCurrentEntity != null) && (fCurrentEntity.entityLocation != null) ? fCurrentEntity.entityLocation.getExpandedSystemId() : null;
  }
  
  public final void setEncoding(String paramString)
    throws IOException
  {
    if ((fCurrentEntity.stream != null) && ((fCurrentEntity.encoding == null) || (!fCurrentEntity.encoding.equals(paramString))))
    {
      if ((fCurrentEntity.encoding != null) && (fCurrentEntity.encoding.startsWith("UTF-16")))
      {
        String str = paramString.toUpperCase(Locale.ENGLISH);
        if (str.equals("UTF-16")) {
          return;
        }
        if (str.equals("ISO-10646-UCS-4"))
        {
          if (fCurrentEntity.encoding.equals("UTF-16BE")) {
            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, (short)8);
          } else {
            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, (short)4);
          }
          return;
        }
        if (str.equals("ISO-10646-UCS-2"))
        {
          if (fCurrentEntity.encoding.equals("UTF-16BE")) {
            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, (short)2);
          } else {
            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, (short)1);
          }
          return;
        }
      }
      fCurrentEntity.setReader(fCurrentEntity.stream, paramString, null);
      fCurrentEntity.encoding = paramString;
    }
  }
  
  public final void setXMLVersion(String paramString)
  {
    fCurrentEntity.xmlVersion = paramString;
  }
  
  public final boolean isExternal()
  {
    return fCurrentEntity.isExternal();
  }
  
  public int peekChar()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.ch[fCurrentEntity.position];
    if (fCurrentEntity.isExternal()) {
      return i != 13 ? i : 10;
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
    if ((i == 10) || ((i == 13) && ((bool = fCurrentEntity.isExternal()))))
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
        if (fCurrentEntity.ch[(fCurrentEntity.position++)] != '\n') {
          fCurrentEntity.position -= 1;
        }
        i = 10;
      }
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
    while (XMLChar.isName(fCurrentEntity.ch[fCurrentEntity.position])) {
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
    int j = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += j;
    Object localObject = null;
    if (j > 0) {
      localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, i, j);
    }
    return localObject;
  }
  
  public String scanName()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    if (XMLChar.isNameStart(fCurrentEntity.ch[i]))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = fCurrentEntity.ch[i];
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.columnNumber += 1;
          str = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
          return str;
        }
      }
      while (XMLChar.isName(fCurrentEntity.ch[fCurrentEntity.position]))
      {
        String str;
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
    }
    int j = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += j;
    Object localObject = null;
    if (j > 0) {
      localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, i, j);
    }
    return localObject;
  }
  
  public String scanNCName()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    if (XMLChar.isNCNameStart(fCurrentEntity.ch[i]))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = fCurrentEntity.ch[i];
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.columnNumber += 1;
          str = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
          return str;
        }
      }
      while (XMLChar.isNCName(fCurrentEntity.ch[fCurrentEntity.position]))
      {
        String str;
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
    }
    int j = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += j;
    Object localObject = null;
    if (j > 0) {
      localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, i, j);
    }
    return localObject;
  }
  
  public boolean scanQName(QName paramQName)
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.position;
    if (XMLChar.isNCNameStart(fCurrentEntity.ch[i]))
    {
      if (++fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = fCurrentEntity.ch[i];
        i = 0;
        if (load(1, false))
        {
          fCurrentEntity.columnNumber += 1;
          String str1 = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
          paramQName.setValues(null, str1, str1, null);
          return true;
        }
      }
      int j = -1;
      Object localObject;
      while (XMLChar.isName(fCurrentEntity.ch[fCurrentEntity.position]))
      {
        k = fCurrentEntity.ch[fCurrentEntity.position];
        if (k == 58)
        {
          if (j == -1) {
            j = fCurrentEntity.position;
          }
        }
        else if (++fCurrentEntity.position == fCurrentEntity.count)
        {
          int m = fCurrentEntity.position - i;
          if (m == fCurrentEntity.ch.length)
          {
            localObject = new char[fCurrentEntity.ch.length << 1];
            System.arraycopy(fCurrentEntity.ch, i, localObject, 0, m);
            fCurrentEntity.ch = ((char[])localObject);
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
      int k = fCurrentEntity.position - i;
      fCurrentEntity.columnNumber += k;
      if (k > 0)
      {
        String str2 = null;
        localObject = null;
        String str3 = fSymbolTable.addSymbol(fCurrentEntity.ch, i, k);
        if (j != -1)
        {
          int n = j - i;
          str2 = fSymbolTable.addSymbol(fCurrentEntity.ch, i, n);
          int i1 = k - n - 1;
          int i2 = j + 1;
          if (!XMLChar.isNCNameStart(fCurrentEntity.ch[i2])) {
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", null, (short)2);
          }
          localObject = fSymbolTable.addSymbol(fCurrentEntity.ch, i2, i1);
        }
        else
        {
          localObject = str3;
        }
        paramQName.setValues(str2, (String)localObject, str3, null);
        return true;
      }
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
    if ((j == 10) || ((j == 13) && (bool)))
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
          if (fCurrentEntity.ch[fCurrentEntity.position] == '\n')
          {
            fCurrentEntity.position += 1;
            i++;
          }
          else
          {
            k++;
          }
        }
        else if (j == 10)
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
    while (fCurrentEntity.position < fCurrentEntity.count)
    {
      j = fCurrentEntity.ch[(fCurrentEntity.position++)];
      if (!XMLChar.isContent(j))
      {
        fCurrentEntity.position -= 1;
        break;
      }
    }
    int m = fCurrentEntity.position - i;
    fCurrentEntity.columnNumber += m - k;
    paramXMLString.setValues(fCurrentEntity.ch, i, m);
    if (fCurrentEntity.position != fCurrentEntity.count)
    {
      j = fCurrentEntity.ch[fCurrentEntity.position];
      if ((j == 13) && (bool)) {
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
      fCurrentEntity.position = 0;
      fCurrentEntity.startPosition = 0;
    }
    int i = fCurrentEntity.position;
    int j = fCurrentEntity.ch[i];
    int k = 0;
    boolean bool = fCurrentEntity.isExternal();
    if ((j == 10) || ((j == 13) && (bool)))
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
          if (fCurrentEntity.ch[fCurrentEntity.position] == '\n')
          {
            fCurrentEntity.position += 1;
            i++;
          }
          else
          {
            k++;
          }
        }
        else if (j == 10)
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
    while (fCurrentEntity.position < fCurrentEntity.count)
    {
      j = fCurrentEntity.ch[(fCurrentEntity.position++)];
      if (((j == paramInt) && ((!fCurrentEntity.literal) || (bool))) || (j == 37) || (!XMLChar.isContent(j)))
      {
        fCurrentEntity.position -= 1;
        break;
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
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    boolean bool2 = false;
    while ((fCurrentEntity.position > fCurrentEntity.count - j) && (!bool2))
    {
      System.arraycopy(fCurrentEntity.ch, fCurrentEntity.position, fCurrentEntity.ch, 0, fCurrentEntity.count - fCurrentEntity.position);
      bool2 = load(fCurrentEntity.count - fCurrentEntity.position, false);
      fCurrentEntity.position = 0;
      fCurrentEntity.startPosition = 0;
    }
    if (fCurrentEntity.position > fCurrentEntity.count - j)
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
    if ((n == 10) || ((n == 13) && (bool1)))
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
          if (fCurrentEntity.ch[fCurrentEntity.position] == '\n')
          {
            fCurrentEntity.position += 1;
            m++;
          }
          else
          {
            i1++;
          }
        }
        else if (n == 10)
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
            break label1040;
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
        if ((n == 10) || ((bool1) && (n == 13)))
        {
          fCurrentEntity.position -= 1;
          break;
        }
        if (XMLChar.isInvalid(n))
        {
          fCurrentEntity.position -= 1;
          i2 = fCurrentEntity.position - m;
          fCurrentEntity.columnNumber += i2 - i1;
          paramXMLStringBuffer.append(fCurrentEntity.ch, m, i2);
          return true;
        }
      }
    }
    label1040:
    int i2 = fCurrentEntity.position - m;
    fCurrentEntity.columnNumber += i2 - i1;
    if (i != 0) {
      i2 -= j;
    }
    paramXMLStringBuffer.append(fCurrentEntity.ch, m, i2);
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
    if ((paramInt == 10) && (i == 13) && (fCurrentEntity.isExternal()))
    {
      if (fCurrentEntity.position == fCurrentEntity.count)
      {
        fCurrentEntity.ch[0] = ((char)i);
        load(1, false);
      }
      fCurrentEntity.position += 1;
      if (fCurrentEntity.ch[fCurrentEntity.position] == '\n') {
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
    if (XMLChar.isSpace(i))
    {
      boolean bool1 = fCurrentEntity.isExternal();
      do
      {
        boolean bool2 = false;
        if ((i == 10) || ((bool1) && (i == 13)))
        {
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count - 1)
          {
            fCurrentEntity.ch[0] = ((char)i);
            bool2 = load(1, true);
            if (!bool2)
            {
              fCurrentEntity.position = 0;
              fCurrentEntity.startPosition = 0;
            }
          }
          if ((i == 13) && (bool1)) {
            if (fCurrentEntity.ch[(++fCurrentEntity.position)] != '\n') {
              fCurrentEntity.position -= 1;
            }
          }
        }
        else
        {
          fCurrentEntity.columnNumber += 1;
        }
        if (!bool2) {
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
  
  public final boolean skipDeclSpaces()
    throws IOException
  {
    if (fCurrentEntity.position == fCurrentEntity.count) {
      load(0, true);
    }
    int i = fCurrentEntity.ch[fCurrentEntity.position];
    if (XMLChar.isSpace(i))
    {
      boolean bool1 = fCurrentEntity.isExternal();
      do
      {
        boolean bool2 = false;
        if ((i == 10) || ((bool1) && (i == 13)))
        {
          fCurrentEntity.lineNumber += 1;
          fCurrentEntity.columnNumber = 1;
          if (fCurrentEntity.position == fCurrentEntity.count - 1)
          {
            fCurrentEntity.ch[0] = ((char)i);
            bool2 = load(1, true);
            if (!bool2)
            {
              fCurrentEntity.position = 0;
              fCurrentEntity.startPosition = 0;
            }
          }
          if ((i == 13) && (bool1)) {
            if (fCurrentEntity.ch[(++fCurrentEntity.position)] != '\n') {
              fCurrentEntity.position -= 1;
            }
          }
        }
        else
        {
          fCurrentEntity.columnNumber += 1;
        }
        if (!bool2) {
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
  
  public final String getPublicId()
  {
    return (fCurrentEntity != null) && (fCurrentEntity.entityLocation != null) ? fCurrentEntity.entityLocation.getPublicId() : null;
  }
  
  public final String getExpandedSystemId()
  {
    if (fCurrentEntity != null)
    {
      if ((fCurrentEntity.entityLocation != null) && (fCurrentEntity.entityLocation.getExpandedSystemId() != null)) {
        return fCurrentEntity.entityLocation.getExpandedSystemId();
      }
      return fCurrentEntity.getExpandedSystemId();
    }
    return null;
  }
  
  public final String getLiteralSystemId()
  {
    if (fCurrentEntity != null)
    {
      if ((fCurrentEntity.entityLocation != null) && (fCurrentEntity.entityLocation.getLiteralSystemId() != null)) {
        return fCurrentEntity.entityLocation.getLiteralSystemId();
      }
      return fCurrentEntity.getLiteralSystemId();
    }
    return null;
  }
  
  public final int getLineNumber()
  {
    if (fCurrentEntity != null)
    {
      if (fCurrentEntity.isExternal()) {
        return fCurrentEntity.lineNumber;
      }
      return fCurrentEntity.getLineNumber();
    }
    return -1;
  }
  
  public final int getColumnNumber()
  {
    if (fCurrentEntity != null)
    {
      if (fCurrentEntity.isExternal()) {
        return fCurrentEntity.columnNumber;
      }
      return fCurrentEntity.getColumnNumber();
    }
    return -1;
  }
  
  public final int getCharacterOffset()
  {
    if (fCurrentEntity != null)
    {
      if (fCurrentEntity.isExternal()) {
        return fCurrentEntity.baseCharOffset + (fCurrentEntity.position - fCurrentEntity.startPosition);
      }
      return fCurrentEntity.getCharacterOffset();
    }
    return -1;
  }
  
  public final String getEncoding()
  {
    if (fCurrentEntity != null)
    {
      if (fCurrentEntity.isExternal()) {
        return fCurrentEntity.encoding;
      }
      return fCurrentEntity.getEncoding();
    }
    return null;
  }
  
  public final String getXMLVersion()
  {
    if (fCurrentEntity != null)
    {
      if (fCurrentEntity.isExternal()) {
        return fCurrentEntity.xmlVersion;
      }
      return fCurrentEntity.getXMLVersion();
    }
    return null;
  }
  
  public final void setCurrentEntity(XMLEntityManager.ScannedEntity paramScannedEntity)
  {
    fCurrentEntity = paramScannedEntity;
  }
  
  public final void setBufferSize(int paramInt)
  {
    fBufferSize = paramInt;
  }
  
  public final void reset(SymbolTable paramSymbolTable, XMLEntityManager paramXMLEntityManager, XMLErrorReporter paramXMLErrorReporter)
  {
    fCurrentEntity = null;
    fSymbolTable = paramSymbolTable;
    fEntityManager = paramXMLEntityManager;
    fErrorReporter = paramXMLErrorReporter;
  }
  
  final boolean load(int paramInt, boolean paramBoolean)
    throws IOException
  {
    fCurrentEntity.baseCharOffset += fCurrentEntity.position - fCurrentEntity.startPosition;
    int i = fCurrentEntity.ch.length - paramInt;
    if ((!fCurrentEntity.mayReadChunks) && (i > 64)) {
      i = 64;
    }
    int j = fCurrentEntity.reader.read(fCurrentEntity.ch, paramInt, i);
    boolean bool = false;
    if (j != -1)
    {
      if (j != 0)
      {
        fCurrentEntity.count = (j + paramInt);
        fCurrentEntity.position = paramInt;
        fCurrentEntity.startPosition = paramInt;
      }
    }
    else
    {
      fCurrentEntity.count = paramInt;
      fCurrentEntity.position = paramInt;
      fCurrentEntity.startPosition = paramInt;
      bool = true;
      if (paramBoolean)
      {
        fEntityManager.endEntity();
        if (fCurrentEntity == null) {
          throw END_OF_DOCUMENT_ENTITY;
        }
        if (fCurrentEntity.position == fCurrentEntity.count) {
          load(0, true);
        }
      }
    }
    return bool;
  }
}
