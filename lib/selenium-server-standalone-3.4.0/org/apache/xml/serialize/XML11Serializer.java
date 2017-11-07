package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.w3c.dom.DOMErrorHandler;
import org.xml.sax.SAXException;

/**
 * @deprecated
 */
public class XML11Serializer
  extends XMLSerializer
{
  protected static final boolean DEBUG = false;
  protected NamespaceSupport fNSBinder;
  protected NamespaceSupport fLocalNSBinder;
  protected SymbolTable fSymbolTable;
  protected boolean fDOML1 = false;
  protected int fNamespaceCounter = 1;
  protected static final String PREFIX = "NS";
  protected boolean fNamespaces = false;
  
  public XML11Serializer()
  {
    _format.setVersion("1.1");
  }
  
  public XML11Serializer(OutputFormat paramOutputFormat)
  {
    super(paramOutputFormat);
    _format.setVersion("1.1");
  }
  
  public XML11Serializer(Writer paramWriter, OutputFormat paramOutputFormat)
  {
    super(paramWriter, paramOutputFormat);
    _format.setVersion("1.1");
  }
  
  public XML11Serializer(OutputStream paramOutputStream, OutputFormat paramOutputFormat)
  {
    super(paramOutputStream, paramOutputFormat != null ? paramOutputFormat : new OutputFormat("xml", null, false));
    _format.setVersion("1.1");
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      ElementState localElementState = content();
      int i;
      if ((inCData) || (doCData))
      {
        if (!inCData)
        {
          _printer.printText("<![CDATA[");
          inCData = true;
        }
        i = _printer.getNextIndent();
        _printer.setNextIndent(0);
        int j = paramInt1 + paramInt2;
        for (int k = paramInt1; k < j; k++)
        {
          char c = paramArrayOfChar[k];
          if ((c == ']') && (k + 2 < j) && (paramArrayOfChar[(k + 1)] == ']') && (paramArrayOfChar[(k + 2)] == '>'))
          {
            _printer.printText("]]]]><![CDATA[>");
            k += 2;
          }
          else if (!XML11Char.isXML11Valid(c))
          {
            k++;
            if (k < j) {
              surrogates(c, paramArrayOfChar[k], true);
            } else {
              fatalError("The character '" + c + "' is an invalid XML character");
            }
          }
          else if ((_encodingInfo.isPrintable(c)) && (XML11Char.isXML11ValidLiteral(c)))
          {
            _printer.printText(c);
          }
          else
          {
            _printer.printText("]]>&#x");
            _printer.printText(Integer.toHexString(c));
            _printer.printText(";<![CDATA[");
          }
        }
        _printer.setNextIndent(i);
      }
      else if (preserveSpace)
      {
        i = _printer.getNextIndent();
        _printer.setNextIndent(0);
        printText(paramArrayOfChar, paramInt1, paramInt2, true, unescaped);
        _printer.setNextIndent(i);
      }
      else
      {
        printText(paramArrayOfChar, paramInt1, paramInt2, false, unescaped);
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  protected void printEscaped(String paramString)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if (!XML11Char.isXML11Valid(k))
      {
        j++;
        if (j < i) {
          surrogates(k, paramString.charAt(j), false);
        } else {
          fatalError("The character '" + (char)k + "' is an invalid XML character");
        }
      }
      else if ((k == 10) || (k == 13) || (k == 9) || (k == 133) || (k == 8232))
      {
        printHex(k);
      }
      else if (k == 60)
      {
        _printer.printText("&lt;");
      }
      else if (k == 38)
      {
        _printer.printText("&amp;");
      }
      else if (k == 34)
      {
        _printer.printText("&quot;");
      }
      else if ((k >= 32) && (_encodingInfo.isPrintable((char)k)))
      {
        _printer.printText((char)k);
      }
      else
      {
        printHex(k);
      }
    }
  }
  
  protected final void printCDATAText(String paramString)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if ((c == ']') && (j + 2 < i) && (paramString.charAt(j + 1) == ']') && (paramString.charAt(j + 2) == '>'))
      {
        if (fDOMErrorHandler != null)
        {
          String str;
          if (((features & 0x10) == 0) && ((features & 0x2) == 0))
          {
            str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", null);
            modifyDOMError(str, (short)3, null, fCurrentNode);
            boolean bool = fDOMErrorHandler.handleError(fDOMError);
            if (!bool) {
              throw new IOException();
            }
          }
          else
          {
            str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SplittingCDATA", null);
            modifyDOMError(str, (short)1, null, fCurrentNode);
            fDOMErrorHandler.handleError(fDOMError);
          }
        }
        _printer.printText("]]]]><![CDATA[>");
        j += 2;
      }
      else if (!XML11Char.isXML11Valid(c))
      {
        j++;
        if (j < i) {
          surrogates(c, paramString.charAt(j), true);
        } else {
          fatalError("The character '" + c + "' is an invalid XML character");
        }
      }
      else if ((_encodingInfo.isPrintable(c)) && (XML11Char.isXML11ValidLiteral(c)))
      {
        _printer.printText(c);
      }
      else
      {
        _printer.printText("]]>&#x");
        _printer.printText(Integer.toHexString(c));
        _printer.printText(";<![CDATA[");
      }
    }
  }
  
  protected final void printXMLChar(int paramInt)
    throws IOException
  {
    if ((paramInt == 13) || (paramInt == 133) || (paramInt == 8232)) {
      printHex(paramInt);
    } else if (paramInt == 60) {
      _printer.printText("&lt;");
    } else if (paramInt == 38) {
      _printer.printText("&amp;");
    } else if (paramInt == 62) {
      _printer.printText("&gt;");
    } else if ((_encodingInfo.isPrintable((char)paramInt)) && (XML11Char.isXML11ValidLiteral(paramInt))) {
      _printer.printText((char)paramInt);
    } else {
      printHex(paramInt);
    }
  }
  
  protected final void surrogates(int paramInt1, int paramInt2, boolean paramBoolean)
    throws IOException
  {
    if (XMLChar.isHighSurrogate(paramInt1))
    {
      if (!XMLChar.isLowSurrogate(paramInt2))
      {
        fatalError("The character '" + (char)paramInt2 + "' is an invalid XML character");
      }
      else
      {
        int i = XMLChar.supplemental((char)paramInt1, (char)paramInt2);
        if (!XML11Char.isXML11Valid(i))
        {
          fatalError("The character '" + (char)i + "' is an invalid XML character");
        }
        else if ((paramBoolean) && (contentinCData))
        {
          _printer.printText("]]>&#x");
          _printer.printText(Integer.toHexString(i));
          _printer.printText(";<![CDATA[");
        }
        else
        {
          printHex(i);
        }
      }
    }
    else {
      fatalError("The character '" + (char)paramInt1 + "' is an invalid XML character");
    }
  }
  
  protected void printText(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    int j = paramString.length();
    int i;
    char c;
    if (paramBoolean1) {
      for (i = 0; i < j; i++)
      {
        c = paramString.charAt(i);
        if (!XML11Char.isXML11Valid(c))
        {
          i++;
          if (i < j) {
            surrogates(c, paramString.charAt(i), true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if ((paramBoolean2) && (XML11Char.isXML11ValidLiteral(c)))
        {
          _printer.printText(c);
        }
        else
        {
          printXMLChar(c);
        }
      }
    } else {
      for (i = 0; i < j; i++)
      {
        c = paramString.charAt(i);
        if (!XML11Char.isXML11Valid(c))
        {
          i++;
          if (i < j) {
            surrogates(c, paramString.charAt(i), true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if ((paramBoolean2) && (XML11Char.isXML11ValidLiteral(c)))
        {
          _printer.printText(c);
        }
        else
        {
          printXMLChar(c);
        }
      }
    }
  }
  
  protected void printText(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    if (paramBoolean1) {
      while (paramInt2-- > 0)
      {
        c = paramArrayOfChar[(paramInt1++)];
        if (!XML11Char.isXML11Valid(c))
        {
          if (paramInt2-- > 0) {
            surrogates(c, paramArrayOfChar[(paramInt1++)], true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if ((paramBoolean2) && (XML11Char.isXML11ValidLiteral(c))) {
          _printer.printText(c);
        } else {
          printXMLChar(c);
        }
      }
    } else {
      while (paramInt2-- > 0)
      {
        char c = paramArrayOfChar[(paramInt1++)];
        if (!XML11Char.isXML11Valid(c))
        {
          if (paramInt2-- > 0) {
            surrogates(c, paramArrayOfChar[(paramInt1++)], true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if ((paramBoolean2) && (XML11Char.isXML11ValidLiteral(c))) {
          _printer.printText(c);
        } else {
          printXMLChar(c);
        }
      }
    }
  }
  
  public boolean reset()
  {
    super.reset();
    return true;
  }
}
