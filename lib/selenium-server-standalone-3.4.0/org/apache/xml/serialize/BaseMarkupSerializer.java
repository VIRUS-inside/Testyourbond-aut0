package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.XMLChar;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * @deprecated
 */
public abstract class BaseMarkupSerializer
  implements ContentHandler, DocumentHandler, LexicalHandler, DTDHandler, DeclHandler, DOMSerializer, Serializer
{
  protected short features = -1;
  protected DOMErrorHandler fDOMErrorHandler;
  protected final DOMErrorImpl fDOMError = new DOMErrorImpl();
  protected LSSerializerFilter fDOMFilter;
  protected EncodingInfo _encodingInfo;
  private ElementState[] _elementStates = new ElementState[10];
  private int _elementStateCount;
  private Vector _preRoot;
  protected boolean _started;
  private boolean _prepared;
  protected Hashtable _prefixes;
  protected String _docTypePublicId;
  protected String _docTypeSystemId;
  protected OutputFormat _format;
  protected Printer _printer;
  protected boolean _indenting;
  protected final StringBuffer fStrBuffer = new StringBuffer(40);
  private Writer _writer;
  private OutputStream _output;
  protected Node fCurrentNode = null;
  
  protected BaseMarkupSerializer(OutputFormat paramOutputFormat)
  {
    for (int i = 0; i < _elementStates.length; i++) {
      _elementStates[i] = new ElementState();
    }
    _format = paramOutputFormat;
  }
  
  public DocumentHandler asDocumentHandler()
    throws IOException
  {
    prepare();
    return this;
  }
  
  public ContentHandler asContentHandler()
    throws IOException
  {
    prepare();
    return this;
  }
  
  public DOMSerializer asDOMSerializer()
    throws IOException
  {
    prepare();
    return this;
  }
  
  public void setOutputByteStream(OutputStream paramOutputStream)
  {
    if (paramOutputStream == null)
    {
      String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[] { "output" });
      throw new NullPointerException(str);
    }
    _output = paramOutputStream;
    _writer = null;
    reset();
  }
  
  public void setOutputCharStream(Writer paramWriter)
  {
    if (paramWriter == null)
    {
      String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[] { "writer" });
      throw new NullPointerException(str);
    }
    _writer = paramWriter;
    _output = null;
    reset();
  }
  
  public void setOutputFormat(OutputFormat paramOutputFormat)
  {
    if (paramOutputFormat == null)
    {
      String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[] { "format" });
      throw new NullPointerException(str);
    }
    _format = paramOutputFormat;
    reset();
  }
  
  public boolean reset()
  {
    if (_elementStateCount > 1)
    {
      String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResetInMiddle", null);
      throw new IllegalStateException(str);
    }
    _prepared = false;
    fCurrentNode = null;
    fStrBuffer.setLength(0);
    return true;
  }
  
  protected void cleanup()
  {
    fCurrentNode = null;
  }
  
  protected void prepare()
    throws IOException
  {
    if (_prepared) {
      return;
    }
    if ((_writer == null) && (_output == null))
    {
      localObject = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null);
      throw new IOException((String)localObject);
    }
    _encodingInfo = _format.getEncodingInfo();
    if (_output != null) {
      _writer = _encodingInfo.getWriter(_output);
    }
    if (_format.getIndenting())
    {
      _indenting = true;
      _printer = new IndentPrinter(_writer, _format);
    }
    else
    {
      _indenting = false;
      _printer = new Printer(_writer, _format);
    }
    _elementStateCount = 0;
    Object localObject = _elementStates[0];
    namespaceURI = null;
    localName = null;
    rawName = null;
    preserveSpace = _format.getPreserveSpace();
    empty = true;
    afterElement = false;
    afterComment = false;
    doCData = (((ElementState)localObject).inCData = 0);
    prefixes = null;
    _docTypePublicId = _format.getDoctypePublic();
    _docTypeSystemId = _format.getDoctypeSystem();
    _started = false;
    _prepared = true;
  }
  
  public void serialize(Element paramElement)
    throws IOException
  {
    reset();
    prepare();
    serializeNode(paramElement);
    cleanup();
    _printer.flush();
    if (_printer.getException() != null) {
      throw _printer.getException();
    }
  }
  
  public void serialize(DocumentFragment paramDocumentFragment)
    throws IOException
  {
    reset();
    prepare();
    serializeNode(paramDocumentFragment);
    cleanup();
    _printer.flush();
    if (_printer.getException() != null) {
      throw _printer.getException();
    }
  }
  
  public void serialize(Document paramDocument)
    throws IOException
  {
    reset();
    prepare();
    serializeNode(paramDocument);
    serializePreRoot();
    cleanup();
    _printer.flush();
    if (_printer.getException() != null) {
      throw _printer.getException();
    }
  }
  
  public void startDocument()
    throws SAXException
  {
    try
    {
      prepare();
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException.toString());
    }
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
          else if (!XMLChar.isValid(c))
          {
            k++;
            if (k < j) {
              surrogates(c, paramArrayOfChar[k], true);
            } else {
              fatalError("The character '" + c + "' is an invalid XML character");
            }
          }
          else if (((c >= ' ') && (_encodingInfo.isPrintable(c)) && (c != '')) || (c == '\n') || (c == '\r') || (c == '\t'))
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
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      content();
      if (_indenting)
      {
        _printer.setThisIndent(0);
        for (int i = paramInt1; paramInt2-- > 0; i++) {
          _printer.printText(paramArrayOfChar[i]);
        }
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public final void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    try
    {
      processingInstructionIO(paramString1, paramString2);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void processingInstructionIO(String paramString1, String paramString2)
    throws IOException
  {
    ElementState localElementState = content();
    int i = paramString1.indexOf("?>");
    if (i >= 0) {
      fStrBuffer.append("<?").append(paramString1.substring(0, i));
    } else {
      fStrBuffer.append("<?").append(paramString1);
    }
    if (paramString2 != null)
    {
      fStrBuffer.append(' ');
      i = paramString2.indexOf("?>");
      if (i >= 0) {
        fStrBuffer.append(paramString2.substring(0, i));
      } else {
        fStrBuffer.append(paramString2);
      }
    }
    fStrBuffer.append("?>");
    if (isDocumentState())
    {
      if (_preRoot == null) {
        _preRoot = new Vector();
      }
      _preRoot.addElement(fStrBuffer.toString());
    }
    else
    {
      _printer.indent();
      printText(fStrBuffer.toString(), true, true);
      _printer.unindent();
      if (_indenting) {
        afterElement = true;
      }
    }
    fStrBuffer.setLength(0);
  }
  
  public void comment(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      comment(new String(paramArrayOfChar, paramInt1, paramInt2));
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void comment(String paramString)
    throws IOException
  {
    if (_format.getOmitComments()) {
      return;
    }
    ElementState localElementState = content();
    int i = paramString.indexOf("-->");
    if (i >= 0) {
      fStrBuffer.append("<!--").append(paramString.substring(0, i)).append("-->");
    } else {
      fStrBuffer.append("<!--").append(paramString).append("-->");
    }
    if (isDocumentState())
    {
      if (_preRoot == null) {
        _preRoot = new Vector();
      }
      _preRoot.addElement(fStrBuffer.toString());
    }
    else
    {
      if ((_indenting) && (!preserveSpace)) {
        _printer.breakLine();
      }
      _printer.indent();
      printText(fStrBuffer.toString(), true, true);
      _printer.unindent();
      if (_indenting) {
        afterElement = true;
      }
    }
    fStrBuffer.setLength(0);
    afterComment = true;
    afterElement = false;
  }
  
  public void startCDATA()
  {
    ElementState localElementState = getElementState();
    doCData = true;
  }
  
  public void endCDATA()
  {
    ElementState localElementState = getElementState();
    doCData = false;
  }
  
  public void startNonEscaping()
  {
    ElementState localElementState = getElementState();
    unescaped = true;
  }
  
  public void endNonEscaping()
  {
    ElementState localElementState = getElementState();
    unescaped = false;
  }
  
  public void startPreserving()
  {
    ElementState localElementState = getElementState();
    preserveSpace = true;
  }
  
  public void endPreserving()
  {
    ElementState localElementState = getElementState();
    preserveSpace = false;
  }
  
  public void endDocument()
    throws SAXException
  {
    try
    {
      serializePreRoot();
      _printer.flush();
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void startEntity(String paramString) {}
  
  public void endEntity(String paramString) {}
  
  public void setDocumentLocator(Locator paramLocator) {}
  
  public void skippedEntity(String paramString)
    throws SAXException
  {
    try
    {
      endCDATA();
      content();
      _printer.printText('&');
      _printer.printText(paramString);
      _printer.printText(';');
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void startPrefixMapping(String paramString1, String paramString2)
    throws SAXException
  {
    if (_prefixes == null) {
      _prefixes = new Hashtable();
    }
    _prefixes.put(paramString2, paramString1 == null ? "" : paramString1);
  }
  
  public void endPrefixMapping(String paramString)
    throws SAXException
  {}
  
  public final void startDTD(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      _docTypePublicId = paramString2;
      _docTypeSystemId = paramString3;
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void endDTD() {}
  
  public void elementDecl(String paramString1, String paramString2)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      _printer.printText("<!ELEMENT ");
      _printer.printText(paramString1);
      _printer.printText(' ');
      _printer.printText(paramString2);
      _printer.printText('>');
      if (_indenting) {
        _printer.breakLine();
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void attributeDecl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      _printer.printText("<!ATTLIST ");
      _printer.printText(paramString1);
      _printer.printText(' ');
      _printer.printText(paramString2);
      _printer.printText(' ');
      _printer.printText(paramString3);
      if (paramString4 != null)
      {
        _printer.printText(' ');
        _printer.printText(paramString4);
      }
      if (paramString5 != null)
      {
        _printer.printText(" \"");
        printEscaped(paramString5);
        _printer.printText('"');
      }
      _printer.printText('>');
      if (_indenting) {
        _printer.breakLine();
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void internalEntityDecl(String paramString1, String paramString2)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      _printer.printText("<!ENTITY ");
      _printer.printText(paramString1);
      _printer.printText(" \"");
      printEscaped(paramString2);
      _printer.printText("\">");
      if (_indenting) {
        _printer.breakLine();
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void externalEntityDecl(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      unparsedEntityDecl(paramString1, paramString2, paramString3, null);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      if (paramString2 == null)
      {
        _printer.printText("<!ENTITY ");
        _printer.printText(paramString1);
        _printer.printText(" SYSTEM ");
        printDoctypeURL(paramString3);
      }
      else
      {
        _printer.printText("<!ENTITY ");
        _printer.printText(paramString1);
        _printer.printText(" PUBLIC ");
        printDoctypeURL(paramString2);
        _printer.printText(' ');
        printDoctypeURL(paramString3);
      }
      if (paramString4 != null)
      {
        _printer.printText(" NDATA ");
        _printer.printText(paramString4);
      }
      _printer.printText('>');
      if (_indenting) {
        _printer.breakLine();
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void notationDecl(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    try
    {
      _printer.enterDTD();
      if (paramString2 != null)
      {
        _printer.printText("<!NOTATION ");
        _printer.printText(paramString1);
        _printer.printText(" PUBLIC ");
        printDoctypeURL(paramString2);
        if (paramString3 != null)
        {
          _printer.printText(' ');
          printDoctypeURL(paramString3);
        }
      }
      else
      {
        _printer.printText("<!NOTATION ");
        _printer.printText(paramString1);
        _printer.printText(" SYSTEM ");
        printDoctypeURL(paramString3);
      }
      _printer.printText('>');
      if (_indenting) {
        _printer.breakLine();
      }
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  protected void serializeNode(Node paramNode)
    throws IOException
  {
    fCurrentNode = paramNode;
    Object localObject1;
    int j;
    int i;
    Object localObject3;
    Object localObject2;
    switch (paramNode.getNodeType())
    {
    case 3: 
      localObject1 = paramNode.getNodeValue();
      if (localObject1 != null)
      {
        if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x4) != 0)) {
          j = fDOMFilter.acceptNode(paramNode);
        }
        switch (j)
        {
        case 2: 
        case 3: 
          break;
        default: 
          characters((String)localObject1);
          break;
          if ((!_indenting) || (getElementStatepreserveSpace) || (((String)localObject1).replace('\n', ' ').trim().length() != 0)) {
            characters((String)localObject1);
          }
          break;
        }
      }
      break;
    case 4: 
      localObject1 = paramNode.getNodeValue();
      if ((features & 0x8) != 0)
      {
        if (localObject1 != null)
        {
          if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x8) != 0))
          {
            j = fDOMFilter.acceptNode(paramNode);
            switch (j)
            {
            case 2: 
            case 3: 
              return;
            }
          }
          startCDATA();
          characters((String)localObject1);
          endCDATA();
        }
      }
      else {
        characters((String)localObject1);
      }
      break;
    case 8: 
      if (!_format.getOmitComments())
      {
        localObject1 = paramNode.getNodeValue();
        if (localObject1 != null)
        {
          if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x80) != 0))
          {
            j = fDOMFilter.acceptNode(paramNode);
            switch (j)
            {
            case 2: 
            case 3: 
              return;
            }
          }
          comment((String)localObject1);
        }
      }
      break;
    case 5: 
      endCDATA();
      content();
      if (((features & 0x4) != 0) || (paramNode.getFirstChild() == null))
      {
        if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x10) != 0))
        {
          j = fDOMFilter.acceptNode(paramNode);
          switch (j)
          {
          case 2: 
            return;
          case 3: 
            for (localObject1 = paramNode.getFirstChild(); localObject1 != null; localObject1 = ((Node)localObject1).getNextSibling()) {
              serializeNode((Node)localObject1);
            }
            return;
          }
        }
        checkUnboundNamespacePrefixedNode(paramNode);
        _printer.printText("&");
        _printer.printText(paramNode.getNodeName());
        _printer.printText(";");
      }
      else
      {
        for (localObject1 = paramNode.getFirstChild(); localObject1 != null; localObject1 = ((Node)localObject1).getNextSibling()) {
          serializeNode((Node)localObject1);
        }
      }
      break;
    case 7: 
      if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x40) != 0))
      {
        i = fDOMFilter.acceptNode(paramNode);
        switch (i)
        {
        case 2: 
        case 3: 
          return;
        }
      }
      processingInstructionIO(paramNode.getNodeName(), paramNode.getNodeValue());
      break;
    case 1: 
      if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x1) != 0))
      {
        i = fDOMFilter.acceptNode(paramNode);
        switch (i)
        {
        case 2: 
          return;
        case 3: 
          for (localObject3 = paramNode.getFirstChild(); localObject3 != null; localObject3 = ((Node)localObject3).getNextSibling()) {
            serializeNode((Node)localObject3);
          }
          return;
        }
      }
      serializeElement((Element)paramNode);
      break;
    case 9: 
      localObject2 = ((Document)paramNode).getDoctype();
      if (localObject2 != null) {
        try
        {
          _printer.enterDTD();
          _docTypePublicId = ((DocumentType)localObject2).getPublicId();
          _docTypeSystemId = ((DocumentType)localObject2).getSystemId();
          localObject3 = ((DocumentType)localObject2).getInternalSubset();
          if ((localObject3 != null) && (((String)localObject3).length() > 0)) {
            _printer.printText((String)localObject3);
          }
          endDTD();
        }
        catch (NoSuchMethodError localNoSuchMethodError)
        {
          Class localClass = localObject2.getClass();
          String str1 = null;
          String str2 = null;
          try
          {
            Method localMethod1 = localClass.getMethod("getPublicId", (Class[])null);
            if (localMethod1.getReturnType().equals(String.class)) {
              str1 = (String)localMethod1.invoke(localObject2, (Object[])null);
            }
          }
          catch (Exception localException1) {}
          try
          {
            Method localMethod2 = localClass.getMethod("getSystemId", (Class[])null);
            if (localMethod2.getReturnType().equals(String.class)) {
              str2 = (String)localMethod2.invoke(localObject2, (Object[])null);
            }
          }
          catch (Exception localException2) {}
          _printer.enterDTD();
          _docTypePublicId = str1;
          _docTypeSystemId = str2;
          endDTD();
        }
      }
    case 11: 
      localObject2 = paramNode.getFirstChild();
      for (;;)
      {
        serializeNode((Node)localObject2);
        localObject2 = ((Node)localObject2).getNextSibling();
        if (localObject2 == null) {
          break;
        }
      }
    }
  }
  
  protected ElementState content()
    throws IOException
  {
    ElementState localElementState = getElementState();
    if (!isDocumentState())
    {
      if ((inCData) && (!doCData))
      {
        _printer.printText("]]>");
        inCData = false;
      }
      if (empty)
      {
        _printer.printText('>');
        empty = false;
      }
      afterElement = false;
      afterComment = false;
    }
    return localElementState;
  }
  
  protected void characters(String paramString)
    throws IOException
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
      printCDATAText(paramString);
      _printer.setNextIndent(i);
    }
    else if (preserveSpace)
    {
      i = _printer.getNextIndent();
      _printer.setNextIndent(0);
      printText(paramString, true, unescaped);
      _printer.setNextIndent(i);
    }
    else
    {
      printText(paramString, false, unescaped);
    }
  }
  
  protected abstract String getEntityRef(int paramInt);
  
  protected abstract void serializeElement(Element paramElement)
    throws IOException;
  
  protected void serializePreRoot()
    throws IOException
  {
    if (_preRoot != null)
    {
      for (int i = 0; i < _preRoot.size(); i++)
      {
        printText((String)_preRoot.elementAt(i), true, true);
        if (_indenting) {
          _printer.breakLine();
        }
      }
      _preRoot.removeAllElements();
    }
  }
  
  protected void printCDATAText(String paramString)
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
          if ((features & 0x10) == 0)
          {
            str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", null);
            if ((features & 0x2) != 0)
            {
              modifyDOMError(str, (short)3, "wf-invalid-character", fCurrentNode);
              fDOMErrorHandler.handleError(fDOMError);
              throw new LSException((short)82, str);
            }
            modifyDOMError(str, (short)2, "cdata-section-not-splitted", fCurrentNode);
            if (!fDOMErrorHandler.handleError(fDOMError)) {
              throw new LSException((short)82, str);
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
      else if (!XMLChar.isValid(c))
      {
        j++;
        if (j < i) {
          surrogates(c, paramString.charAt(j), true);
        } else {
          fatalError("The character '" + c + "' is an invalid XML character");
        }
      }
      else if (((c >= ' ') && (_encodingInfo.isPrintable(c)) && (c != '')) || (c == '\n') || (c == '\r') || (c == '\t'))
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
  
  protected void surrogates(int paramInt1, int paramInt2, boolean paramBoolean)
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
        if (!XMLChar.isValid(i))
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
  
  protected void printText(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    if (paramBoolean1) {
      while (paramInt2-- > 0)
      {
        c = paramArrayOfChar[paramInt1];
        paramInt1++;
        if ((c == '\n') || (c == '\r') || (paramBoolean2)) {
          _printer.printText(c);
        } else {
          printEscaped(c);
        }
      }
    } else {
      while (paramInt2-- > 0)
      {
        char c = paramArrayOfChar[paramInt1];
        paramInt1++;
        if ((c == ' ') || (c == '\f') || (c == '\t') || (c == '\n') || (c == '\r')) {
          _printer.printSpace();
        } else if (paramBoolean2) {
          _printer.printText(c);
        } else {
          printEscaped(c);
        }
      }
    }
  }
  
  protected void printText(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    int i;
    char c;
    if (paramBoolean1) {
      for (i = 0; i < paramString.length(); i++)
      {
        c = paramString.charAt(i);
        if ((c == '\n') || (c == '\r') || (paramBoolean2)) {
          _printer.printText(c);
        } else {
          printEscaped(c);
        }
      }
    } else {
      for (i = 0; i < paramString.length(); i++)
      {
        c = paramString.charAt(i);
        if ((c == ' ') || (c == '\f') || (c == '\t') || (c == '\n') || (c == '\r')) {
          _printer.printSpace();
        } else if (paramBoolean2) {
          _printer.printText(c);
        } else {
          printEscaped(c);
        }
      }
    }
  }
  
  protected void printDoctypeURL(String paramString)
    throws IOException
  {
    _printer.printText('"');
    for (int i = 0; i < paramString.length(); i++) {
      if ((paramString.charAt(i) == '"') || (paramString.charAt(i) < ' ') || (paramString.charAt(i) > ''))
      {
        _printer.printText('%');
        _printer.printText(Integer.toHexString(paramString.charAt(i)));
      }
      else
      {
        _printer.printText(paramString.charAt(i));
      }
    }
    _printer.printText('"');
  }
  
  protected void printEscaped(int paramInt)
    throws IOException
  {
    String str = getEntityRef(paramInt);
    if (str != null)
    {
      _printer.printText('&');
      _printer.printText(str);
      _printer.printText(';');
    }
    else if (((paramInt >= 32) && (_encodingInfo.isPrintable((char)paramInt)) && (paramInt != 127)) || (paramInt == 10) || (paramInt == 13) || (paramInt == 9))
    {
      if (paramInt < 65536)
      {
        _printer.printText((char)paramInt);
      }
      else
      {
        _printer.printText((char)((paramInt - 65536 >> 10) + 55296));
        _printer.printText((char)((paramInt - 65536 & 0x3FF) + 56320));
      }
    }
    else
    {
      printHex(paramInt);
    }
  }
  
  final void printHex(int paramInt)
    throws IOException
  {
    _printer.printText("&#x");
    _printer.printText(Integer.toHexString(paramInt));
    _printer.printText(';');
  }
  
  protected void printEscaped(String paramString)
    throws IOException
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i);
      if (((j & 0xFC00) == 55296) && (i + 1 < paramString.length()))
      {
        int k = paramString.charAt(i + 1);
        if ((k & 0xFC00) == 56320)
        {
          j = 65536 + (j - 55296 << 10) + k - 56320;
          i++;
        }
      }
      printEscaped(j);
    }
  }
  
  protected ElementState getElementState()
  {
    return _elementStates[_elementStateCount];
  }
  
  protected ElementState enterElementState(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    if (_elementStateCount + 1 == _elementStates.length)
    {
      ElementState[] arrayOfElementState = new ElementState[_elementStates.length + 10];
      for (int i = 0; i < _elementStates.length; i++) {
        arrayOfElementState[i] = _elementStates[i];
      }
      for (int j = _elementStates.length; j < arrayOfElementState.length; j++) {
        arrayOfElementState[j] = new ElementState();
      }
      _elementStates = arrayOfElementState;
    }
    _elementStateCount += 1;
    ElementState localElementState = _elementStates[_elementStateCount];
    namespaceURI = paramString1;
    localName = paramString2;
    rawName = paramString3;
    preserveSpace = paramBoolean;
    empty = true;
    afterElement = false;
    afterComment = false;
    doCData = (localElementState.inCData = 0);
    unescaped = false;
    prefixes = _prefixes;
    _prefixes = null;
    return localElementState;
  }
  
  protected ElementState leaveElementState()
  {
    if (_elementStateCount > 0)
    {
      _prefixes = null;
      _elementStateCount -= 1;
      return _elementStates[_elementStateCount];
    }
    String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "Internal", null);
    throw new IllegalStateException(str);
  }
  
  protected boolean isDocumentState()
  {
    return _elementStateCount == 0;
  }
  
  final void clearDocumentState()
  {
    _elementStateCount = 0;
  }
  
  protected String getPrefix(String paramString)
  {
    String str;
    if (_prefixes != null)
    {
      str = (String)_prefixes.get(paramString);
      if (str != null) {
        return str;
      }
    }
    if (_elementStateCount == 0) {
      return null;
    }
    for (int i = _elementStateCount; i > 0; i--) {
      if (_elementStates[i].prefixes != null)
      {
        str = (String)_elementStates[i].prefixes.get(paramString);
        if (str != null) {
          return str;
        }
      }
    }
    return null;
  }
  
  protected DOMError modifyDOMError(String paramString1, short paramShort, String paramString2, Node paramNode)
  {
    fDOMError.reset();
    fDOMError.fMessage = paramString1;
    fDOMError.fType = paramString2;
    fDOMError.fSeverity = paramShort;
    fDOMError.fLocator = new DOMLocatorImpl(-1, -1, -1, paramNode, null);
    return fDOMError;
  }
  
  protected void fatalError(String paramString)
    throws IOException
  {
    if (fDOMErrorHandler != null)
    {
      modifyDOMError(paramString, (short)3, null, fCurrentNode);
      fDOMErrorHandler.handleError(fDOMError);
    }
    else
    {
      throw new IOException(paramString);
    }
  }
  
  protected void checkUnboundNamespacePrefixedNode(Node paramNode)
    throws IOException
  {}
  
  public abstract void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException;
  
  public abstract void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException;
  
  public abstract void endElement(String paramString)
    throws SAXException;
  
  public abstract void startElement(String paramString, AttributeList paramAttributeList)
    throws SAXException;
}
