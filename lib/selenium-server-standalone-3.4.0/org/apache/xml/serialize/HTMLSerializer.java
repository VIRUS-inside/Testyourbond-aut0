package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @deprecated
 */
public class HTMLSerializer
  extends BaseMarkupSerializer
{
  private boolean _xhtml;
  public static final String XHTMLNamespace = "http://www.w3.org/1999/xhtml";
  private String fUserXHTMLNamespace = null;
  
  protected HTMLSerializer(boolean paramBoolean, OutputFormat paramOutputFormat)
  {
    super(paramOutputFormat);
    _xhtml = paramBoolean;
  }
  
  public HTMLSerializer()
  {
    this(false, new OutputFormat("html", "ISO-8859-1", false));
  }
  
  public HTMLSerializer(OutputFormat paramOutputFormat)
  {
    this(false, paramOutputFormat != null ? paramOutputFormat : new OutputFormat("html", "ISO-8859-1", false));
  }
  
  public HTMLSerializer(Writer paramWriter, OutputFormat paramOutputFormat)
  {
    this(false, paramOutputFormat != null ? paramOutputFormat : new OutputFormat("html", "ISO-8859-1", false));
    setOutputCharStream(paramWriter);
  }
  
  public HTMLSerializer(OutputStream paramOutputStream, OutputFormat paramOutputFormat)
  {
    this(false, paramOutputFormat != null ? paramOutputFormat : new OutputFormat("html", "ISO-8859-1", false));
    setOutputByteStream(paramOutputStream);
  }
  
  public void setOutputFormat(OutputFormat paramOutputFormat)
  {
    super.setOutputFormat(paramOutputFormat != null ? paramOutputFormat : new OutputFormat("html", "ISO-8859-1", false));
  }
  
  public void setXHTMLNamespace(String paramString)
  {
    fUserXHTMLNamespace = paramString;
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    int j = 0;
    try
    {
      if (_printer == null) {
        throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null));
      }
      ElementState localElementState = getElementState();
      if (isDocumentState())
      {
        if (!_started) {
          startDocument((paramString2 == null) || (paramString2.length() == 0) ? paramString3 : paramString2);
        }
      }
      else
      {
        if (empty) {
          _printer.printText('>');
        }
        if ((_indenting) && (!preserveSpace) && ((empty) || (afterElement))) {
          _printer.breakLine();
        }
      }
      boolean bool = preserveSpace;
      int k = (paramString1 != null) && (paramString1.length() != 0) ? 1 : 0;
      Object localObject;
      if ((paramString3 == null) || (paramString3.length() == 0))
      {
        paramString3 = paramString2;
        if (k != 0)
        {
          localObject = getPrefix(paramString1);
          if ((localObject != null) && (((String)localObject).length() != 0)) {
            paramString3 = (String)localObject + ":" + paramString2;
          }
        }
        j = 1;
      }
      String str3;
      if (k == 0) {
        str3 = paramString3;
      } else if ((paramString1.equals("http://www.w3.org/1999/xhtml")) || ((fUserXHTMLNamespace != null) && (fUserXHTMLNamespace.equals(paramString1)))) {
        str3 = paramString2;
      } else {
        str3 = null;
      }
      _printer.printText('<');
      if (_xhtml) {
        _printer.printText(paramString3.toLowerCase(Locale.ENGLISH));
      } else {
        _printer.printText(paramString3);
      }
      _printer.indent();
      String str1;
      String str2;
      if (paramAttributes != null) {
        for (int i = 0; i < paramAttributes.getLength(); i++)
        {
          _printer.printSpace();
          str1 = paramAttributes.getQName(i).toLowerCase(Locale.ENGLISH);
          str2 = paramAttributes.getValue(i);
          if ((_xhtml) || (k != 0))
          {
            if (str2 == null)
            {
              _printer.printText(str1);
              _printer.printText("=\"\"");
            }
            else
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              printEscaped(str2);
              _printer.printText('"');
            }
          }
          else
          {
            if (str2 == null) {
              str2 = "";
            }
            if ((!_format.getPreserveEmptyAttributes()) && (str2.length() == 0))
            {
              _printer.printText(str1);
            }
            else if (HTMLdtd.isURI(paramString3, str1))
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              _printer.printText(escapeURI(str2));
              _printer.printText('"');
            }
            else if (HTMLdtd.isBoolean(paramString3, str1))
            {
              _printer.printText(str1);
            }
            else
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              printEscaped(str2);
              _printer.printText('"');
            }
          }
        }
      }
      if ((str3 != null) && (HTMLdtd.isPreserveSpace(str3))) {
        bool = true;
      }
      if (j != 0)
      {
        localObject = _prefixes.entrySet().iterator();
        while (((Iterator)localObject).hasNext())
        {
          _printer.printSpace();
          Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
          str2 = (String)localEntry.getKey();
          str1 = (String)localEntry.getValue();
          if (str1.length() == 0)
          {
            _printer.printText("xmlns=\"");
            printEscaped(str2);
            _printer.printText('"');
          }
          else
          {
            _printer.printText("xmlns:");
            _printer.printText(str1);
            _printer.printText("=\"");
            printEscaped(str2);
            _printer.printText('"');
          }
        }
      }
      localElementState = enterElementState(paramString1, paramString2, paramString3, bool);
      if ((str3 != null) && ((str3.equalsIgnoreCase("A")) || (str3.equalsIgnoreCase("TD"))))
      {
        empty = false;
        _printer.printText('>');
      }
      if ((str3 != null) && ((paramString3.equalsIgnoreCase("SCRIPT")) || (paramString3.equalsIgnoreCase("STYLE")))) {
        if (_xhtml) {
          doCData = true;
        } else {
          unescaped = true;
        }
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    try
    {
      endElementIO(paramString1, paramString2, paramString3);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void endElementIO(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    _printer.unindent();
    ElementState localElementState = getElementState();
    String str;
    if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
      str = rawName;
    } else if ((namespaceURI.equals("http://www.w3.org/1999/xhtml")) || ((fUserXHTMLNamespace != null) && (fUserXHTMLNamespace.equals(namespaceURI)))) {
      str = localName;
    } else {
      str = null;
    }
    if (_xhtml)
    {
      if (empty)
      {
        _printer.printText(" />");
      }
      else
      {
        if (inCData) {
          _printer.printText("]]>");
        }
        _printer.printText("</");
        _printer.printText(rawName.toLowerCase(Locale.ENGLISH));
        _printer.printText('>');
      }
    }
    else
    {
      if (empty) {
        _printer.printText('>');
      }
      if ((str == null) || (!HTMLdtd.isOnlyOpening(str)))
      {
        if ((_indenting) && (!preserveSpace) && (afterElement)) {
          _printer.breakLine();
        }
        if (inCData) {
          _printer.printText("]]>");
        }
        _printer.printText("</");
        _printer.printText(rawName);
        _printer.printText('>');
      }
    }
    localElementState = leaveElementState();
    if ((str == null) || ((!str.equalsIgnoreCase("A")) && (!str.equalsIgnoreCase("TD")))) {
      afterElement = true;
    }
    empty = false;
    if (isDocumentState()) {
      _printer.flush();
    }
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      ElementState localElementState = content();
      doCData = false;
      super.characters(paramArrayOfChar, paramInt1, paramInt2);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void startElement(String paramString, AttributeList paramAttributeList)
    throws SAXException
  {
    try
    {
      if (_printer == null) {
        throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null));
      }
      ElementState localElementState = getElementState();
      if (isDocumentState())
      {
        if (!_started) {
          startDocument(paramString);
        }
      }
      else
      {
        if (empty) {
          _printer.printText('>');
        }
        if ((_indenting) && (!preserveSpace) && ((empty) || (afterElement))) {
          _printer.breakLine();
        }
      }
      boolean bool = preserveSpace;
      _printer.printText('<');
      if (_xhtml) {
        _printer.printText(paramString.toLowerCase(Locale.ENGLISH));
      } else {
        _printer.printText(paramString);
      }
      _printer.indent();
      if (paramAttributeList != null) {
        for (int i = 0; i < paramAttributeList.getLength(); i++)
        {
          _printer.printSpace();
          String str1 = paramAttributeList.getName(i).toLowerCase(Locale.ENGLISH);
          String str2 = paramAttributeList.getValue(i);
          if (_xhtml)
          {
            if (str2 == null)
            {
              _printer.printText(str1);
              _printer.printText("=\"\"");
            }
            else
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              printEscaped(str2);
              _printer.printText('"');
            }
          }
          else
          {
            if (str2 == null) {
              str2 = "";
            }
            if ((!_format.getPreserveEmptyAttributes()) && (str2.length() == 0))
            {
              _printer.printText(str1);
            }
            else if (HTMLdtd.isURI(paramString, str1))
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              _printer.printText(escapeURI(str2));
              _printer.printText('"');
            }
            else if (HTMLdtd.isBoolean(paramString, str1))
            {
              _printer.printText(str1);
            }
            else
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              printEscaped(str2);
              _printer.printText('"');
            }
          }
        }
      }
      if (HTMLdtd.isPreserveSpace(paramString)) {
        bool = true;
      }
      localElementState = enterElementState(null, null, paramString, bool);
      if ((paramString.equalsIgnoreCase("A")) || (paramString.equalsIgnoreCase("TD")))
      {
        empty = false;
        _printer.printText('>');
      }
      if ((paramString.equalsIgnoreCase("SCRIPT")) || (paramString.equalsIgnoreCase("STYLE"))) {
        if (_xhtml) {
          doCData = true;
        } else {
          unescaped = true;
        }
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void endElement(String paramString)
    throws SAXException
  {
    endElement(null, null, paramString);
  }
  
  protected void startDocument(String paramString)
    throws IOException
  {
    _printer.leaveDTD();
    if (!_started)
    {
      if ((_docTypePublicId == null) && (_docTypeSystemId == null)) {
        if (_xhtml)
        {
          _docTypePublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
          _docTypeSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        }
        else
        {
          _docTypePublicId = "-//W3C//DTD HTML 4.01//EN";
          _docTypeSystemId = "http://www.w3.org/TR/html4/strict.dtd";
        }
      }
      if (!_format.getOmitDocumentType()) {
        if ((_docTypePublicId != null) && ((!_xhtml) || (_docTypeSystemId != null)))
        {
          if (_xhtml) {
            _printer.printText("<!DOCTYPE html PUBLIC ");
          } else {
            _printer.printText("<!DOCTYPE HTML PUBLIC ");
          }
          printDoctypeURL(_docTypePublicId);
          if (_docTypeSystemId != null)
          {
            if (_indenting)
            {
              _printer.breakLine();
              _printer.printText("                      ");
            }
            else
            {
              _printer.printText(' ');
            }
            printDoctypeURL(_docTypeSystemId);
          }
          _printer.printText('>');
          _printer.breakLine();
        }
        else if (_docTypeSystemId != null)
        {
          if (_xhtml) {
            _printer.printText("<!DOCTYPE html SYSTEM ");
          } else {
            _printer.printText("<!DOCTYPE HTML SYSTEM ");
          }
          printDoctypeURL(_docTypeSystemId);
          _printer.printText('>');
          _printer.breakLine();
        }
      }
    }
    _started = true;
    serializePreRoot();
  }
  
  protected void serializeElement(Element paramElement)
    throws IOException
  {
    String str3 = paramElement.getTagName();
    ElementState localElementState = getElementState();
    if (isDocumentState())
    {
      if (!_started) {
        startDocument(str3);
      }
    }
    else
    {
      if (empty) {
        _printer.printText('>');
      }
      if ((_indenting) && (!preserveSpace) && ((empty) || (afterElement))) {
        _printer.breakLine();
      }
    }
    boolean bool = preserveSpace;
    _printer.printText('<');
    if (_xhtml) {
      _printer.printText(str3.toLowerCase(Locale.ENGLISH));
    } else {
      _printer.printText(str3);
    }
    _printer.indent();
    NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
    if (localNamedNodeMap != null) {
      for (int i = 0; i < localNamedNodeMap.getLength(); i++)
      {
        Attr localAttr = (Attr)localNamedNodeMap.item(i);
        String str1 = localAttr.getName().toLowerCase(Locale.ENGLISH);
        String str2 = localAttr.getValue();
        if (localAttr.getSpecified())
        {
          _printer.printSpace();
          if (_xhtml)
          {
            if (str2 == null)
            {
              _printer.printText(str1);
              _printer.printText("=\"\"");
            }
            else
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              printEscaped(str2);
              _printer.printText('"');
            }
          }
          else
          {
            if (str2 == null) {
              str2 = "";
            }
            if ((!_format.getPreserveEmptyAttributes()) && (str2.length() == 0))
            {
              _printer.printText(str1);
            }
            else if (HTMLdtd.isURI(str3, str1))
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              _printer.printText(escapeURI(str2));
              _printer.printText('"');
            }
            else if (HTMLdtd.isBoolean(str3, str1))
            {
              _printer.printText(str1);
            }
            else
            {
              _printer.printText(str1);
              _printer.printText("=\"");
              printEscaped(str2);
              _printer.printText('"');
            }
          }
        }
      }
    }
    if (HTMLdtd.isPreserveSpace(str3)) {
      bool = true;
    }
    if ((paramElement.hasChildNodes()) || (!HTMLdtd.isEmptyTag(str3)))
    {
      localElementState = enterElementState(null, null, str3, bool);
      if ((str3.equalsIgnoreCase("A")) || (str3.equalsIgnoreCase("TD")))
      {
        empty = false;
        _printer.printText('>');
      }
      if ((str3.equalsIgnoreCase("SCRIPT")) || (str3.equalsIgnoreCase("STYLE"))) {
        if (_xhtml) {
          doCData = true;
        } else {
          unescaped = true;
        }
      }
      for (Node localNode = paramElement.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
        serializeNode(localNode);
      }
      endElementIO(null, null, str3);
    }
    else
    {
      _printer.unindent();
      if (_xhtml) {
        _printer.printText(" />");
      } else {
        _printer.printText('>');
      }
      afterElement = true;
      empty = false;
      if (isDocumentState()) {
        _printer.flush();
      }
    }
  }
  
  protected void characters(String paramString)
    throws IOException
  {
    content();
    super.characters(paramString);
  }
  
  protected String getEntityRef(int paramInt)
  {
    return HTMLdtd.fromChar(paramInt);
  }
  
  protected String escapeURI(String paramString)
  {
    int i = paramString.indexOf("\"");
    if (i >= 0) {
      return paramString.substring(0, i);
    }
    return paramString;
  }
}
