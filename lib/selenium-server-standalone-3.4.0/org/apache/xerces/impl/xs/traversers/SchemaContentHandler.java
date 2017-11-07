package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SAXLocatorWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

final class SchemaContentHandler
  implements ContentHandler
{
  private SymbolTable fSymbolTable;
  private SchemaDOMParser fSchemaDOMParser;
  private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
  private NamespaceSupport fNamespaceContext = new NamespaceSupport();
  private boolean fNeedPushNSContext;
  private boolean fNamespacePrefixes = false;
  private boolean fStringsInternalized = false;
  private final QName fElementQName = new QName();
  private final QName fAttributeQName = new QName();
  private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
  private final XMLString fTempString = new XMLString();
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  
  public SchemaContentHandler() {}
  
  public Document getDocument()
  {
    return fSchemaDOMParser.getDocument();
  }
  
  public void setDocumentLocator(Locator paramLocator)
  {
    fSAXLocatorWrapper.setLocator(paramLocator);
  }
  
  public void startDocument()
    throws SAXException
  {
    fNeedPushNSContext = true;
    fNamespaceContext.reset();
    try
    {
      fSchemaDOMParser.startDocument(fSAXLocatorWrapper, null, fNamespaceContext, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
  }
  
  public void endDocument()
    throws SAXException
  {
    fSAXLocatorWrapper.setLocator(null);
    try
    {
      fSchemaDOMParser.endDocument(null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
  }
  
  public void startPrefixMapping(String paramString1, String paramString2)
    throws SAXException
  {
    if (fNeedPushNSContext)
    {
      fNeedPushNSContext = false;
      fNamespaceContext.pushContext();
    }
    if (!fStringsInternalized)
    {
      paramString1 = paramString1 != null ? fSymbolTable.addSymbol(paramString1) : XMLSymbols.EMPTY_STRING;
      paramString2 = (paramString2 != null) && (paramString2.length() > 0) ? fSymbolTable.addSymbol(paramString2) : null;
    }
    else
    {
      if (paramString1 == null) {
        paramString1 = XMLSymbols.EMPTY_STRING;
      }
      if ((paramString2 != null) && (paramString2.length() == 0)) {
        paramString2 = null;
      }
    }
    fNamespaceContext.declarePrefix(paramString1, paramString2);
  }
  
  public void endPrefixMapping(String paramString)
    throws SAXException
  {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (fNeedPushNSContext) {
      fNamespaceContext.pushContext();
    }
    fNeedPushNSContext = true;
    fillQName(fElementQName, paramString1, paramString2, paramString3);
    fillXMLAttributes(paramAttributes);
    if (!fNamespacePrefixes)
    {
      int i = fNamespaceContext.getDeclaredPrefixCount();
      if (i > 0) {
        addNamespaceDeclarations(i);
      }
    }
    try
    {
      fSchemaDOMParser.startElement(fElementQName, fAttributes, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    fillQName(fElementQName, paramString1, paramString2, paramString3);
    try
    {
      fSchemaDOMParser.endElement(fElementQName, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
    finally
    {
      fNamespaceContext.popContext();
    }
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      fTempString.setValues(paramArrayOfChar, paramInt1, paramInt2);
      fSchemaDOMParser.characters(fTempString, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
  }
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      fTempString.setValues(paramArrayOfChar, paramInt1, paramInt2);
      fSchemaDOMParser.ignorableWhitespace(fTempString, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    try
    {
      fTempString.setValues(paramString2.toCharArray(), 0, paramString2.length());
      fSchemaDOMParser.processingInstruction(paramString1, fTempString, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      convertToSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      convertToSAXException(localXNIException);
    }
  }
  
  public void skippedEntity(String paramString)
    throws SAXException
  {}
  
  private void fillQName(QName paramQName, String paramString1, String paramString2, String paramString3)
  {
    if (!fStringsInternalized)
    {
      paramString1 = (paramString1 != null) && (paramString1.length() > 0) ? fSymbolTable.addSymbol(paramString1) : null;
      paramString2 = paramString2 != null ? fSymbolTable.addSymbol(paramString2) : XMLSymbols.EMPTY_STRING;
      paramString3 = paramString3 != null ? fSymbolTable.addSymbol(paramString3) : XMLSymbols.EMPTY_STRING;
    }
    else
    {
      if ((paramString1 != null) && (paramString1.length() == 0)) {
        paramString1 = null;
      }
      if (paramString2 == null) {
        paramString2 = XMLSymbols.EMPTY_STRING;
      }
      if (paramString3 == null) {
        paramString3 = XMLSymbols.EMPTY_STRING;
      }
    }
    String str = XMLSymbols.EMPTY_STRING;
    int i = paramString3.indexOf(':');
    if (i != -1)
    {
      str = fSymbolTable.addSymbol(paramString3.substring(0, i));
      if (paramString2 == XMLSymbols.EMPTY_STRING) {
        paramString2 = fSymbolTable.addSymbol(paramString3.substring(i + 1));
      }
    }
    else if (paramString2 == XMLSymbols.EMPTY_STRING)
    {
      paramString2 = paramString3;
    }
    paramQName.setValues(str, paramString2, paramString3, paramString1);
  }
  
  private void fillXMLAttributes(Attributes paramAttributes)
  {
    fAttributes.removeAllAttributes();
    int i = paramAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      fillQName(fAttributeQName, paramAttributes.getURI(j), paramAttributes.getLocalName(j), paramAttributes.getQName(j));
      String str = paramAttributes.getType(j);
      fAttributes.addAttributeNS(fAttributeQName, str != null ? str : XMLSymbols.fCDATASymbol, paramAttributes.getValue(j));
      fAttributes.setSpecified(j, true);
    }
  }
  
  private void addNamespaceDeclarations(int paramInt)
  {
    String str1 = null;
    Object localObject = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    for (int i = 0; i < paramInt; i++)
    {
      str3 = fNamespaceContext.getDeclaredPrefixAt(i);
      str4 = fNamespaceContext.getURI(str3);
      if (str3.length() > 0)
      {
        str1 = XMLSymbols.PREFIX_XMLNS;
        localObject = str3;
        fStringBuffer.clear();
        fStringBuffer.append(str1);
        fStringBuffer.append(':');
        fStringBuffer.append((String)localObject);
        str2 = fSymbolTable.addSymbol(fStringBuffer.ch, fStringBuffer.offset, fStringBuffer.length);
      }
      else
      {
        str1 = XMLSymbols.EMPTY_STRING;
        localObject = XMLSymbols.PREFIX_XMLNS;
        str2 = XMLSymbols.PREFIX_XMLNS;
      }
      fAttributeQName.setValues(str1, (String)localObject, str2, NamespaceContext.XMLNS_URI);
      fAttributes.addAttribute(fAttributeQName, XMLSymbols.fCDATASymbol, str4 != null ? str4 : XMLSymbols.EMPTY_STRING);
    }
  }
  
  public void reset(SchemaDOMParser paramSchemaDOMParser, SymbolTable paramSymbolTable, boolean paramBoolean1, boolean paramBoolean2)
  {
    fSchemaDOMParser = paramSchemaDOMParser;
    fSymbolTable = paramSymbolTable;
    fNamespacePrefixes = paramBoolean1;
    fStringsInternalized = paramBoolean2;
  }
  
  static void convertToSAXParseException(XMLParseException paramXMLParseException)
    throws SAXException
  {
    Exception localException = paramXMLParseException.getException();
    if (localException == null)
    {
      LocatorImpl localLocatorImpl = new LocatorImpl();
      localLocatorImpl.setPublicId(paramXMLParseException.getPublicId());
      localLocatorImpl.setSystemId(paramXMLParseException.getExpandedSystemId());
      localLocatorImpl.setLineNumber(paramXMLParseException.getLineNumber());
      localLocatorImpl.setColumnNumber(paramXMLParseException.getColumnNumber());
      throw new SAXParseException(paramXMLParseException.getMessage(), localLocatorImpl);
    }
    if ((localException instanceof SAXException)) {
      throw ((SAXException)localException);
    }
    throw new SAXException(localException);
  }
  
  static void convertToSAXException(XNIException paramXNIException)
    throws SAXException
  {
    Exception localException = paramXNIException.getException();
    if (localException == null) {
      throw new SAXException(paramXNIException.getMessage());
    }
    if ((localException instanceof SAXException)) {
      throw ((SAXException)localException);
    }
    throw new SAXException(localException);
  }
}
