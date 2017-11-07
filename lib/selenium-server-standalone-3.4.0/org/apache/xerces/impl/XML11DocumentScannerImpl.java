package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

public class XML11DocumentScannerImpl
  extends XMLDocumentScannerImpl
{
  private final XMLString fString = new XMLString();
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
  private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
  
  public XML11DocumentScannerImpl() {}
  
  protected int scanContent()
    throws IOException, XNIException
  {
    Object localObject = fString;
    int i = fEntityScanner.scanContent((XMLString)localObject);
    if ((i == 13) || (i == 133) || (i == 8232))
    {
      fEntityScanner.scanChar();
      fStringBuffer.clear();
      fStringBuffer.append(fString);
      fStringBuffer.append((char)i);
      localObject = fStringBuffer;
      i = -1;
    }
    if ((fDocumentHandler != null) && (length > 0)) {
      fDocumentHandler.characters((XMLString)localObject, null);
    }
    if ((i == 93) && (fString.length == 0))
    {
      fStringBuffer.clear();
      fStringBuffer.append((char)fEntityScanner.scanChar());
      fInScanContent = true;
      if (fEntityScanner.skipChar(93))
      {
        fStringBuffer.append(']');
        while (fEntityScanner.skipChar(93)) {
          fStringBuffer.append(']');
        }
        if (fEntityScanner.skipChar(62)) {
          reportFatalError("CDEndInContent", null);
        }
      }
      if ((fDocumentHandler != null) && (fStringBuffer.length != 0)) {
        fDocumentHandler.characters(fStringBuffer, null);
      }
      fInScanContent = false;
      i = -1;
    }
    return i;
  }
  
  protected boolean scanAttributeValue(XMLString paramXMLString1, XMLString paramXMLString2, String paramString1, boolean paramBoolean, String paramString2)
    throws IOException, XNIException
  {
    int i = fEntityScanner.peekChar();
    if ((i != 39) && (i != 34)) {
      reportFatalError("OpenQuoteExpected", new Object[] { paramString2, paramString1 });
    }
    fEntityScanner.scanChar();
    int j = fEntityDepth;
    int k = fEntityScanner.scanLiteral(i, paramXMLString1);
    int m = 0;
    int n;
    if ((k == i) && ((m = isUnchangedByNormalization(paramXMLString1)) == -1))
    {
      paramXMLString2.setValues(paramXMLString1);
      n = fEntityScanner.scanChar();
      if (n != i) {
        reportFatalError("CloseQuoteExpected", new Object[] { paramString2, paramString1 });
      }
      return true;
    }
    fStringBuffer2.clear();
    fStringBuffer2.append(paramXMLString1);
    normalizeWhitespace(paramXMLString1, m);
    if (k != i)
    {
      fScanningAttribute = true;
      fStringBuffer.clear();
      do
      {
        fStringBuffer.append(paramXMLString1);
        if (k == 38)
        {
          fEntityScanner.skipChar(38);
          if (j == fEntityDepth) {
            fStringBuffer2.append('&');
          }
          if (fEntityScanner.skipChar(35))
          {
            if (j == fEntityDepth) {
              fStringBuffer2.append('#');
            }
            n = scanCharReferenceValue(fStringBuffer, fStringBuffer2);
            if (n == -1) {}
          }
          else
          {
            String str = fEntityScanner.scanName();
            if (str == null) {
              reportFatalError("NameRequiredInReference", null);
            } else if (j == fEntityDepth) {
              fStringBuffer2.append(str);
            }
            if (!fEntityScanner.skipChar(59)) {
              reportFatalError("SemicolonRequiredInReference", new Object[] { str });
            } else if (j == fEntityDepth) {
              fStringBuffer2.append(';');
            }
            if (str == XMLScanner.fAmpSymbol)
            {
              fStringBuffer.append('&');
            }
            else if (str == XMLScanner.fAposSymbol)
            {
              fStringBuffer.append('\'');
            }
            else if (str == XMLScanner.fLtSymbol)
            {
              fStringBuffer.append('<');
            }
            else if (str == XMLScanner.fGtSymbol)
            {
              fStringBuffer.append('>');
            }
            else if (str == XMLScanner.fQuotSymbol)
            {
              fStringBuffer.append('"');
            }
            else if (fEntityManager.isExternalEntity(str))
            {
              reportFatalError("ReferenceToExternalEntity", new Object[] { str });
            }
            else
            {
              if (!fEntityManager.isDeclaredEntity(str)) {
                if (paramBoolean)
                {
                  if (fValidation) {
                    fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { str }, (short)1);
                  }
                }
                else {
                  reportFatalError("EntityNotDeclared", new Object[] { str });
                }
              }
              fEntityManager.startEntity(str, true);
            }
          }
        }
        else if (k == 60)
        {
          reportFatalError("LessthanInAttValue", new Object[] { paramString2, paramString1 });
          fEntityScanner.scanChar();
          if (j == fEntityDepth) {
            fStringBuffer2.append((char)k);
          }
        }
        else if ((k == 37) || (k == 93))
        {
          fEntityScanner.scanChar();
          fStringBuffer.append((char)k);
          if (j == fEntityDepth) {
            fStringBuffer2.append((char)k);
          }
        }
        else if ((k == 10) || (k == 13) || (k == 133) || (k == 8232))
        {
          fEntityScanner.scanChar();
          fStringBuffer.append(' ');
          if (j == fEntityDepth) {
            fStringBuffer2.append('\n');
          }
        }
        else if ((k != -1) && (XMLChar.isHighSurrogate(k)))
        {
          fStringBuffer3.clear();
          if (scanSurrogates(fStringBuffer3))
          {
            fStringBuffer.append(fStringBuffer3);
            if (j == fEntityDepth) {
              fStringBuffer2.append(fStringBuffer3);
            }
          }
        }
        else if ((k != -1) && (isInvalidLiteral(k)))
        {
          reportFatalError("InvalidCharInAttValue", new Object[] { paramString2, paramString1, Integer.toString(k, 16) });
          fEntityScanner.scanChar();
          if (j == fEntityDepth) {
            fStringBuffer2.append((char)k);
          }
        }
        k = fEntityScanner.scanLiteral(i, paramXMLString1);
        if (j == fEntityDepth) {
          fStringBuffer2.append(paramXMLString1);
        }
        normalizeWhitespace(paramXMLString1);
      } while ((k != i) || (j != fEntityDepth));
      fStringBuffer.append(paramXMLString1);
      paramXMLString1.setValues(fStringBuffer);
      fScanningAttribute = false;
    }
    paramXMLString2.setValues(fStringBuffer2);
    int i1 = fEntityScanner.scanChar();
    if (i1 != i) {
      reportFatalError("CloseQuoteExpected", new Object[] { paramString2, paramString1 });
    }
    return paramXMLString2.equals(ch, offset, length);
  }
  
  protected boolean scanPubidLiteral(XMLString paramXMLString)
    throws IOException, XNIException
  {
    int i = fEntityScanner.scanChar();
    if ((i != 39) && (i != 34))
    {
      reportFatalError("QuoteRequiredInPublicID", null);
      return false;
    }
    fStringBuffer.clear();
    int j = 1;
    boolean bool = true;
    for (;;)
    {
      int k = fEntityScanner.scanChar();
      if ((k == 32) || (k == 10) || (k == 13) || (k == 133) || (k == 8232))
      {
        if (j == 0)
        {
          fStringBuffer.append(' ');
          j = 1;
        }
      }
      else
      {
        if (k == i)
        {
          if (j != 0) {
            fStringBuffer.length -= 1;
          }
          paramXMLString.setValues(fStringBuffer);
          break;
        }
        if (XMLChar.isPubid(k))
        {
          fStringBuffer.append((char)k);
          j = 0;
        }
        else
        {
          if (k == -1)
          {
            reportFatalError("PublicIDUnterminated", null);
            return false;
          }
          bool = false;
          reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(k) });
        }
      }
    }
    return bool;
  }
  
  protected void normalizeWhitespace(XMLString paramXMLString)
  {
    int i = offset + length;
    for (int j = offset; j < i; j++)
    {
      int k = ch[j];
      if (XMLChar.isSpace(k)) {
        ch[j] = ' ';
      }
    }
  }
  
  protected void normalizeWhitespace(XMLString paramXMLString, int paramInt)
  {
    int i = offset + length;
    for (int j = offset + paramInt; j < i; j++)
    {
      int k = ch[j];
      if (XMLChar.isSpace(k)) {
        ch[j] = ' ';
      }
    }
  }
  
  protected int isUnchangedByNormalization(XMLString paramXMLString)
  {
    int i = offset + length;
    for (int j = offset; j < i; j++)
    {
      int k = ch[j];
      if (XMLChar.isSpace(k)) {
        return j - offset;
      }
    }
    return -1;
  }
  
  protected boolean isInvalid(int paramInt)
  {
    return XML11Char.isXML11Invalid(paramInt);
  }
  
  protected boolean isInvalidLiteral(int paramInt)
  {
    return !XML11Char.isXML11ValidLiteral(paramInt);
  }
  
  protected boolean isValidNameChar(int paramInt)
  {
    return XML11Char.isXML11Name(paramInt);
  }
  
  protected boolean isValidNameStartChar(int paramInt)
  {
    return XML11Char.isXML11NameStart(paramInt);
  }
  
  protected boolean isValidNCName(int paramInt)
  {
    return XML11Char.isXML11NCName(paramInt);
  }
  
  protected boolean isValidNameStartHighSurrogate(int paramInt)
  {
    return XML11Char.isXML11NameHighSurrogate(paramInt);
  }
  
  protected boolean versionSupported(String paramString)
  {
    return (paramString.equals("1.1")) || (paramString.equals("1.0"));
  }
  
  protected String getVersionNotSupportedKey()
  {
    return "VersionNotSupported11";
  }
}
