package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @deprecated
 */
public class XMLSerializer
  extends BaseMarkupSerializer
{
  protected static final boolean DEBUG = false;
  protected NamespaceSupport fNSBinder;
  protected NamespaceSupport fLocalNSBinder;
  protected SymbolTable fSymbolTable;
  protected static final String PREFIX = "NS";
  protected boolean fNamespaces = false;
  protected boolean fNamespacePrefixes = true;
  private boolean fPreserveSpace;
  
  public XMLSerializer()
  {
    super(new OutputFormat("xml", null, false));
  }
  
  public XMLSerializer(OutputFormat paramOutputFormat)
  {
    super(paramOutputFormat != null ? paramOutputFormat : new OutputFormat("xml", null, false));
    _format.setMethod("xml");
  }
  
  public XMLSerializer(Writer paramWriter, OutputFormat paramOutputFormat)
  {
    super(paramOutputFormat != null ? paramOutputFormat : new OutputFormat("xml", null, false));
    _format.setMethod("xml");
    setOutputCharStream(paramWriter);
  }
  
  public XMLSerializer(OutputStream paramOutputStream, OutputFormat paramOutputFormat)
  {
    super(paramOutputFormat != null ? paramOutputFormat : new OutputFormat("xml", null, false));
    _format.setMethod("xml");
    setOutputByteStream(paramOutputStream);
  }
  
  public void setOutputFormat(OutputFormat paramOutputFormat)
  {
    super.setOutputFormat(paramOutputFormat != null ? paramOutputFormat : new OutputFormat("xml", null, false));
  }
  
  public void setNamespaces(boolean paramBoolean)
  {
    fNamespaces = paramBoolean;
    if (fNSBinder == null)
    {
      fNSBinder = new NamespaceSupport();
      fLocalNSBinder = new NamespaceSupport();
      fSymbolTable = new SymbolTable();
    }
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    try
    {
      Object localObject1;
      if (_printer == null)
      {
        localObject1 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
        throw new IllegalStateException((String)localObject1);
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
        if (inCData)
        {
          _printer.printText("]]>");
          inCData = false;
        }
        if ((_indenting) && (!preserveSpace) && ((empty) || (afterElement) || (afterComment))) {
          _printer.breakLine();
        }
      }
      boolean bool = preserveSpace;
      paramAttributes = extractNamespaces(paramAttributes);
      if ((paramString3 == null) || (paramString3.length() == 0))
      {
        if (paramString2 == null)
        {
          localObject1 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoName", null);
          throw new SAXException((String)localObject1);
        }
        if ((paramString1 != null) && (!paramString1.equals("")))
        {
          localObject1 = getPrefix(paramString1);
          if ((localObject1 != null) && (((String)localObject1).length() > 0)) {
            paramString3 = (String)localObject1 + ":" + paramString2;
          } else {
            paramString3 = paramString2;
          }
        }
        else
        {
          paramString3 = paramString2;
        }
      }
      _printer.printText('<');
      _printer.printText(paramString3);
      _printer.indent();
      Object localObject2;
      String str2;
      if (paramAttributes != null) {
        for (int i = 0; i < paramAttributes.getLength(); i++)
        {
          _printer.printSpace();
          str1 = paramAttributes.getQName(i);
          if ((str1 != null) && (str1.length() == 0))
          {
            str1 = paramAttributes.getLocalName(i);
            localObject2 = paramAttributes.getURI(i);
            if ((localObject2 != null) && (((String)localObject2).length() != 0) && ((paramString1 == null) || (paramString1.length() == 0) || (!((String)localObject2).equals(paramString1))))
            {
              localObject1 = getPrefix((String)localObject2);
              if ((localObject1 != null) && (((String)localObject1).length() > 0)) {
                str1 = (String)localObject1 + ":" + str1;
              }
            }
          }
          str2 = paramAttributes.getValue(i);
          if (str2 == null) {
            str2 = "";
          }
          _printer.printText(str1);
          _printer.printText("=\"");
          printEscaped(str2);
          _printer.printText('"');
          if (str1.equals("xml:space")) {
            if (str2.equals("preserve")) {
              bool = true;
            } else {
              bool = _format.getPreserveSpace();
            }
          }
        }
      }
      if (_prefixes != null)
      {
        localObject1 = _prefixes.entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          _printer.printSpace();
          localObject2 = (Map.Entry)((Iterator)localObject1).next();
          str2 = (String)((Map.Entry)localObject2).getKey();
          str1 = (String)((Map.Entry)localObject2).getValue();
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
      String str1 = paramString1 + "^" + paramString2;
      doCData = _format.isCDataElement(str1);
      unescaped = _format.isNonEscapingElement(str1);
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
    if (empty)
    {
      _printer.printText("/>");
    }
    else
    {
      if (inCData) {
        _printer.printText("]]>");
      }
      if ((_indenting) && (!preserveSpace) && ((afterElement) || (afterComment))) {
        _printer.breakLine();
      }
      _printer.printText("</");
      _printer.printText(rawName);
      _printer.printText('>');
    }
    localElementState = leaveElementState();
    afterElement = true;
    afterComment = false;
    empty = false;
    if (isDocumentState()) {
      _printer.flush();
    }
  }
  
  public void startElement(String paramString, AttributeList paramAttributeList)
    throws SAXException
  {
    try
    {
      if (_printer == null)
      {
        String str3 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
        throw new IllegalStateException(str3);
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
        if (inCData)
        {
          _printer.printText("]]>");
          inCData = false;
        }
        if ((_indenting) && (!preserveSpace) && ((empty) || (afterElement) || (afterComment))) {
          _printer.breakLine();
        }
      }
      boolean bool = preserveSpace;
      _printer.printText('<');
      _printer.printText(paramString);
      _printer.indent();
      if (paramAttributeList != null) {
        for (int i = 0; i < paramAttributeList.getLength(); i++)
        {
          _printer.printSpace();
          String str1 = paramAttributeList.getName(i);
          String str2 = paramAttributeList.getValue(i);
          if (str2 != null)
          {
            _printer.printText(str1);
            _printer.printText("=\"");
            printEscaped(str2);
            _printer.printText('"');
          }
          if (str1.equals("xml:space")) {
            if (str2.equals("preserve")) {
              bool = true;
            } else {
              bool = _format.getPreserveSpace();
            }
          }
        }
      }
      localElementState = enterElementState(null, null, paramString, bool);
      doCData = _format.isCDataElement(paramString);
      unescaped = _format.isNonEscapingElement(paramString);
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
    String str1 = _printer.leaveDTD();
    if (!_started)
    {
      if (!_format.getOmitXMLDeclaration())
      {
        StringBuffer localStringBuffer = new StringBuffer("<?xml version=\"");
        if (_format.getVersion() != null) {
          localStringBuffer.append(_format.getVersion());
        } else {
          localStringBuffer.append("1.0");
        }
        localStringBuffer.append('"');
        String str2 = _format.getEncoding();
        if (str2 != null)
        {
          localStringBuffer.append(" encoding=\"");
          localStringBuffer.append(str2);
          localStringBuffer.append('"');
        }
        if ((_format.getStandalone()) && (_docTypeSystemId == null) && (_docTypePublicId == null)) {
          localStringBuffer.append(" standalone=\"yes\"");
        }
        localStringBuffer.append("?>");
        _printer.printText(localStringBuffer);
        _printer.breakLine();
      }
      if (!_format.getOmitDocumentType()) {
        if (_docTypeSystemId != null)
        {
          _printer.printText("<!DOCTYPE ");
          _printer.printText(paramString);
          if (_docTypePublicId != null)
          {
            _printer.printText(" PUBLIC ");
            printDoctypeURL(_docTypePublicId);
            if (_indenting)
            {
              _printer.breakLine();
              for (int i = 0; i < 18 + paramString.length(); i++) {
                _printer.printText(" ");
              }
            }
            else
            {
              _printer.printText(" ");
            }
            printDoctypeURL(_docTypeSystemId);
          }
          else
          {
            _printer.printText(" SYSTEM ");
            printDoctypeURL(_docTypeSystemId);
          }
          if ((str1 != null) && (str1.length() > 0))
          {
            _printer.printText(" [");
            printText(str1, true, true);
            _printer.printText(']');
          }
          _printer.printText(">");
          _printer.breakLine();
        }
        else if ((str1 != null) && (str1.length() > 0))
        {
          _printer.printText("<!DOCTYPE ");
          _printer.printText(paramString);
          _printer.printText(" [");
          printText(str1, true, true);
          _printer.printText("]>");
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
    if (fNamespaces)
    {
      fLocalNSBinder.reset();
      fNSBinder.pushContext();
    }
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
      if (inCData)
      {
        _printer.printText("]]>");
        inCData = false;
      }
      if ((_indenting) && (!preserveSpace) && ((empty) || (afterElement) || (afterComment))) {
        _printer.breakLine();
      }
    }
    fPreserveSpace = preserveSpace;
    int j = 0;
    NamedNodeMap localNamedNodeMap = null;
    if (paramElement.hasAttributes())
    {
      localNamedNodeMap = paramElement.getAttributes();
      j = localNamedNodeMap.getLength();
    }
    int i;
    Attr localAttr;
    String str1;
    String str2;
    if (!fNamespaces)
    {
      _printer.printText('<');
      _printer.printText(str3);
      _printer.indent();
      for (i = 0; i < j; i++)
      {
        localAttr = (Attr)localNamedNodeMap.item(i);
        str1 = localAttr.getName();
        str2 = localAttr.getValue();
        if (str2 == null) {
          str2 = "";
        }
        printAttribute(str1, str2, localAttr.getSpecified(), localAttr);
      }
    }
    else
    {
      String str6;
      boolean bool1;
      for (i = 0; i < j; i++)
      {
        localAttr = (Attr)localNamedNodeMap.item(i);
        str5 = localAttr.getNamespaceURI();
        if ((str5 != null) && (str5.equals(NamespaceContext.XMLNS_URI)))
        {
          str2 = localAttr.getNodeValue();
          if (str2 == null) {
            str2 = XMLSymbols.EMPTY_STRING;
          }
          if (str2.equals(NamespaceContext.XMLNS_URI))
          {
            if (fDOMErrorHandler != null)
            {
              str6 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
              modifyDOMError(str6, (short)2, null, localAttr);
              bool1 = fDOMErrorHandler.handleError(fDOMError);
              if (!bool1) {
                throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
              }
            }
          }
          else
          {
            localObject = localAttr.getPrefix();
            localObject = (localObject == null) || (((String)localObject).length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
            str6 = fSymbolTable.addSymbol(localAttr.getLocalName());
            if (localObject == XMLSymbols.PREFIX_XMLNS)
            {
              str2 = fSymbolTable.addSymbol(str2);
              if (str2.length() != 0) {
                fNSBinder.declarePrefix(str6, str2);
              }
            }
            else
            {
              str2 = fSymbolTable.addSymbol(str2);
              fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, str2);
            }
          }
        }
      }
      String str5 = paramElement.getNamespaceURI();
      Object localObject = paramElement.getPrefix();
      if ((str5 != null) && (localObject != null) && (str5.length() == 0) && (((String)localObject).length() != 0))
      {
        localObject = null;
        _printer.printText('<');
        _printer.printText(paramElement.getLocalName());
        _printer.indent();
      }
      else
      {
        _printer.printText('<');
        _printer.printText(str3);
        _printer.indent();
      }
      if (str5 != null)
      {
        str5 = fSymbolTable.addSymbol(str5);
        localObject = (localObject == null) || (((String)localObject).length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
        if (fNSBinder.getURI((String)localObject) != str5)
        {
          if (fNamespacePrefixes) {
            printNamespaceAttr((String)localObject, str5);
          }
          fLocalNSBinder.declarePrefix((String)localObject, str5);
          fNSBinder.declarePrefix((String)localObject, str5);
        }
      }
      else if (paramElement.getLocalName() == null)
      {
        if (fDOMErrorHandler != null)
        {
          str6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[] { paramElement.getNodeName() });
          modifyDOMError(str6, (short)2, null, paramElement);
          bool1 = fDOMErrorHandler.handleError(fDOMError);
          if (!bool1) {
            throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
          }
        }
      }
      else
      {
        str5 = fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
        if ((str5 != null) && (str5.length() > 0))
        {
          if (fNamespacePrefixes) {
            printNamespaceAttr(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
          }
          fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
          fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
        }
      }
      for (i = 0; i < j; i++)
      {
        localAttr = (Attr)localNamedNodeMap.item(i);
        str2 = localAttr.getValue();
        str1 = localAttr.getNodeName();
        str5 = localAttr.getNamespaceURI();
        if ((str5 != null) && (str5.length() == 0))
        {
          str5 = null;
          str1 = localAttr.getLocalName();
        }
        if (str2 == null) {
          str2 = XMLSymbols.EMPTY_STRING;
        }
        if (str5 != null)
        {
          localObject = localAttr.getPrefix();
          localObject = localObject == null ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
          str6 = fSymbolTable.addSymbol(localAttr.getLocalName());
          if ((str5 != null) && (str5.equals(NamespaceContext.XMLNS_URI)))
          {
            localObject = localAttr.getPrefix();
            localObject = (localObject == null) || (((String)localObject).length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
            str6 = fSymbolTable.addSymbol(localAttr.getLocalName());
            String str4;
            if (localObject == XMLSymbols.PREFIX_XMLNS)
            {
              str4 = fLocalNSBinder.getURI(str6);
              str2 = fSymbolTable.addSymbol(str2);
              if ((str2.length() != 0) && (str4 == null))
              {
                if (fNamespacePrefixes) {
                  printNamespaceAttr(str6, str2);
                }
                fLocalNSBinder.declarePrefix(str6, str2);
              }
            }
            else
            {
              str5 = fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
              str4 = fLocalNSBinder.getURI(XMLSymbols.EMPTY_STRING);
              str2 = fSymbolTable.addSymbol(str2);
              if ((str4 == null) && (fNamespacePrefixes)) {
                printNamespaceAttr(XMLSymbols.EMPTY_STRING, str2);
              }
            }
          }
          else
          {
            str5 = fSymbolTable.addSymbol(str5);
            String str7 = fNSBinder.getURI((String)localObject);
            if ((localObject == XMLSymbols.EMPTY_STRING) || (str7 != str5))
            {
              str1 = localAttr.getNodeName();
              String str8 = fNSBinder.getPrefix(str5);
              if ((str8 != null) && (str8 != XMLSymbols.EMPTY_STRING))
              {
                localObject = str8;
                str1 = (String)localObject + ":" + str6;
              }
              else
              {
                if ((localObject == XMLSymbols.EMPTY_STRING) || (fLocalNSBinder.getURI((String)localObject) != null))
                {
                  int k = 1;
                  for (localObject = fSymbolTable.addSymbol("NS" + k++); fLocalNSBinder.getURI((String)localObject) != null; localObject = fSymbolTable.addSymbol("NS" + k++)) {}
                  str1 = (String)localObject + ":" + str6;
                }
                if (fNamespacePrefixes) {
                  printNamespaceAttr((String)localObject, str5);
                }
                str2 = fSymbolTable.addSymbol(str2);
                fLocalNSBinder.declarePrefix((String)localObject, str2);
                fNSBinder.declarePrefix((String)localObject, str5);
              }
            }
            printAttribute(str1, str2 == null ? XMLSymbols.EMPTY_STRING : str2, localAttr.getSpecified(), localAttr);
          }
        }
        else if (localAttr.getLocalName() == null)
        {
          if (fDOMErrorHandler != null)
          {
            str6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[] { localAttr.getNodeName() });
            modifyDOMError(str6, (short)2, null, localAttr);
            boolean bool2 = fDOMErrorHandler.handleError(fDOMError);
            if (!bool2) {
              throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", null));
            }
          }
          printAttribute(str1, str2, localAttr.getSpecified(), localAttr);
        }
        else
        {
          printAttribute(str1, str2, localAttr.getSpecified(), localAttr);
        }
      }
    }
    if (paramElement.hasChildNodes())
    {
      localElementState = enterElementState(null, null, str3, fPreserveSpace);
      doCData = _format.isCDataElement(str3);
      unescaped = _format.isNonEscapingElement(str3);
      for (Node localNode = paramElement.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
        serializeNode(localNode);
      }
      if (fNamespaces) {
        fNSBinder.popContext();
      }
      endElementIO(null, null, str3);
    }
    else
    {
      if (fNamespaces) {
        fNSBinder.popContext();
      }
      _printer.unindent();
      _printer.printText("/>");
      afterElement = true;
      afterComment = false;
      empty = false;
      if (isDocumentState()) {
        _printer.flush();
      }
    }
  }
  
  private void printNamespaceAttr(String paramString1, String paramString2)
    throws IOException
  {
    _printer.printSpace();
    if (paramString1 == XMLSymbols.EMPTY_STRING) {
      _printer.printText(XMLSymbols.PREFIX_XMLNS);
    } else {
      _printer.printText("xmlns:" + paramString1);
    }
    _printer.printText("=\"");
    printEscaped(paramString2);
    _printer.printText('"');
  }
  
  private void printAttribute(String paramString1, String paramString2, boolean paramBoolean, Attr paramAttr)
    throws IOException
  {
    if ((paramBoolean) || ((features & 0x40) == 0))
    {
      if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x2) != 0))
      {
        int i = fDOMFilter.acceptNode(paramAttr);
        switch (i)
        {
        case 2: 
        case 3: 
          return;
        }
      }
      _printer.printSpace();
      _printer.printText(paramString1);
      _printer.printText("=\"");
      printEscaped(paramString2);
      _printer.printText('"');
    }
    if (paramString1.equals("xml:space")) {
      if (paramString2.equals("preserve")) {
        fPreserveSpace = true;
      } else {
        fPreserveSpace = _format.getPreserveSpace();
      }
    }
  }
  
  protected String getEntityRef(int paramInt)
  {
    switch (paramInt)
    {
    case 60: 
      return "lt";
    case 62: 
      return "gt";
    case 34: 
      return "quot";
    case 39: 
      return "apos";
    case 38: 
      return "amp";
    }
    return null;
  }
  
  private Attributes extractNamespaces(Attributes paramAttributes)
    throws SAXException
  {
    if (paramAttributes == null) {
      return null;
    }
    int j = paramAttributes.getLength();
    AttributesImpl localAttributesImpl = new AttributesImpl(paramAttributes);
    for (int i = j - 1; i >= 0; i--)
    {
      String str = localAttributesImpl.getQName(i);
      if (str.startsWith("xmlns")) {
        if (str.length() == 5)
        {
          startPrefixMapping("", paramAttributes.getValue(i));
          localAttributesImpl.removeAttribute(i);
        }
        else if (str.charAt(5) == ':')
        {
          startPrefixMapping(str.substring(6), paramAttributes.getValue(i));
          localAttributesImpl.removeAttribute(i);
        }
      }
    }
    return localAttributesImpl;
  }
  
  protected void printEscaped(String paramString)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if (!XMLChar.isValid(k))
      {
        j++;
        if (j < i) {
          surrogates(k, paramString.charAt(j), false);
        } else {
          fatalError("The character '" + (char)k + "' is an invalid XML character");
        }
      }
      else if ((k == 10) || (k == 13) || (k == 9))
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
  
  protected void printXMLChar(int paramInt)
    throws IOException
  {
    if (paramInt == 13) {
      printHex(paramInt);
    } else if (paramInt == 60) {
      _printer.printText("&lt;");
    } else if (paramInt == 38) {
      _printer.printText("&amp;");
    } else if (paramInt == 62) {
      _printer.printText("&gt;");
    } else if ((paramInt == 10) || (paramInt == 9) || ((paramInt >= 32) && (_encodingInfo.isPrintable((char)paramInt)))) {
      _printer.printText((char)paramInt);
    } else {
      printHex(paramInt);
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
        if (!XMLChar.isValid(c))
        {
          i++;
          if (i < j) {
            surrogates(c, paramString.charAt(i), true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if (paramBoolean2)
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
        if (!XMLChar.isValid(c))
        {
          i++;
          if (i < j) {
            surrogates(c, paramString.charAt(i), true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if (paramBoolean2)
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
        if (!XMLChar.isValid(c))
        {
          if (paramInt2-- > 0) {
            surrogates(c, paramArrayOfChar[(paramInt1++)], true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if (paramBoolean2) {
          _printer.printText(c);
        } else {
          printXMLChar(c);
        }
      }
    } else {
      while (paramInt2-- > 0)
      {
        char c = paramArrayOfChar[(paramInt1++)];
        if (!XMLChar.isValid(c))
        {
          if (paramInt2-- > 0) {
            surrogates(c, paramArrayOfChar[(paramInt1++)], true);
          } else {
            fatalError("The character '" + c + "' is an invalid XML character");
          }
        }
        else if (paramBoolean2) {
          _printer.printText(c);
        } else {
          printXMLChar(c);
        }
      }
    }
  }
  
  protected void checkUnboundNamespacePrefixedNode(Node paramNode)
    throws IOException
  {
    if (fNamespaces)
    {
      Node localNode;
      for (Object localObject = paramNode.getFirstChild(); localObject != null; localObject = localNode)
      {
        localNode = ((Node)localObject).getNextSibling();
        String str1 = ((Node)localObject).getPrefix();
        str1 = (str1 == null) || (str1.length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol(str1);
        if ((fNSBinder.getURI(str1) == null) && (str1 != null)) {
          fatalError("The replacement text of the entity node '" + paramNode.getNodeName() + "' contains an element node '" + ((Node)localObject).getNodeName() + "' with an undeclared prefix '" + str1 + "'.");
        }
        if (((Node)localObject).getNodeType() == 1)
        {
          NamedNodeMap localNamedNodeMap = ((Node)localObject).getAttributes();
          for (int i = 0; i < localNamedNodeMap.getLength(); i++)
          {
            String str2 = localNamedNodeMap.item(i).getPrefix();
            str2 = (str2 == null) || (str2.length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol(str2);
            if ((fNSBinder.getURI(str2) == null) && (str2 != null)) {
              fatalError("The replacement text of the entity node '" + paramNode.getNodeName() + "' contains an element node '" + ((Node)localObject).getNodeName() + "' with an attribute '" + localNamedNodeMap.item(i).getNodeName() + "' an undeclared prefix '" + str2 + "'.");
            }
          }
        }
        if (((Node)localObject).hasChildNodes()) {
          checkUnboundNamespacePrefixedNode((Node)localObject);
        }
      }
    }
  }
  
  public boolean reset()
  {
    super.reset();
    if (fNSBinder != null)
    {
      fNSBinder.reset();
      fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
    }
    return true;
  }
}
