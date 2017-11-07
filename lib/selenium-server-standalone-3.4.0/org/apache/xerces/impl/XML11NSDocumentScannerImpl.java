package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.dtd.XMLDTDValidatorFilter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XML11NSDocumentScannerImpl
  extends XML11DocumentScannerImpl
{
  protected boolean fBindNamespaces;
  protected boolean fPerformValidation;
  private XMLDTDValidatorFilter fDTDValidator;
  private boolean fSawSpace;
  
  public XML11NSDocumentScannerImpl() {}
  
  public void setDTDValidator(XMLDTDValidatorFilter paramXMLDTDValidatorFilter)
  {
    fDTDValidator = paramXMLDTDValidatorFilter;
  }
  
  protected boolean scanStartElement()
    throws IOException, XNIException
  {
    fEntityScanner.scanQName(fElementQName);
    String str1 = fElementQName.rawname;
    if (fBindNamespaces)
    {
      fNamespaceContext.pushContext();
      if ((fScannerState == 6) && (fPerformValidation))
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { str1 }, (short)1);
        if ((fDoctypeName == null) || (!fDoctypeName.equals(str1))) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { fDoctypeName, str1 }, (short)1);
        }
      }
    }
    fCurrentElement = fElementStack.pushElement(fElementQName);
    boolean bool1 = false;
    fAttributes.removeAllAttributes();
    int i;
    for (;;)
    {
      boolean bool2 = fEntityScanner.skipSpaces();
      i = fEntityScanner.peekChar();
      if (i == 62)
      {
        fEntityScanner.scanChar();
        break;
      }
      if (i == 47)
      {
        fEntityScanner.scanChar();
        if (!fEntityScanner.skipChar(62)) {
          reportFatalError("ElementUnterminated", new Object[] { str1 });
        }
        bool1 = true;
        break;
      }
      if (((!isValidNameStartChar(i)) || (!bool2)) && ((!isValidNameStartHighSurrogate(i)) || (!bool2))) {
        reportFatalError("ElementUnterminated", new Object[] { str1 });
      }
      scanAttribute(fAttributes);
    }
    if (fBindNamespaces)
    {
      if (fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
        fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { fElementQName.rawname }, (short)2);
      }
      String str2 = fElementQName.prefix != null ? fElementQName.prefix : XMLSymbols.EMPTY_STRING;
      fElementQName.uri = fNamespaceContext.getURI(str2);
      fCurrentElement.uri = fElementQName.uri;
      if ((fElementQName.prefix == null) && (fElementQName.uri != null))
      {
        fElementQName.prefix = XMLSymbols.EMPTY_STRING;
        fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
      }
      if ((fElementQName.prefix != null) && (fElementQName.uri == null)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { fElementQName.prefix, fElementQName.rawname }, (short)2);
      }
      i = fAttributes.getLength();
      Object localObject;
      for (int j = 0; j < i; j++)
      {
        fAttributes.getName(j, fAttributeQName);
        localObject = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
        String str3 = fNamespaceContext.getURI((String)localObject);
        if (((fAttributeQName.uri == null) || (fAttributeQName.uri != str3)) && (localObject != XMLSymbols.EMPTY_STRING))
        {
          fAttributeQName.uri = str3;
          if (str3 == null) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { fElementQName.rawname, fAttributeQName.rawname, localObject }, (short)2);
          }
          fAttributes.setURI(j, str3);
        }
      }
      if (i > 1)
      {
        localObject = fAttributes.checkDuplicatesNS();
        if (localObject != null) {
          if (uri != null) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { fElementQName.rawname, localpart, uri }, (short)2);
          } else {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[] { fElementQName.rawname, rawname }, (short)2);
          }
        }
      }
    }
    if (fDocumentHandler != null) {
      if (bool1)
      {
        fMarkupDepth -= 1;
        if (fMarkupDepth < fEntityStack[(fEntityDepth - 1)]) {
          reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
        }
        fDocumentHandler.emptyElement(fElementQName, fAttributes, null);
        if (fBindNamespaces) {
          fNamespaceContext.popContext();
        }
        fElementStack.popElement(fElementQName);
      }
      else
      {
        fDocumentHandler.startElement(fElementQName, fAttributes, null);
      }
    }
    return bool1;
  }
  
  protected void scanStartElementName()
    throws IOException, XNIException
  {
    fEntityScanner.scanQName(fElementQName);
    fSawSpace = fEntityScanner.skipSpaces();
  }
  
  protected boolean scanStartElementAfterName()
    throws IOException, XNIException
  {
    String str1 = fElementQName.rawname;
    if (fBindNamespaces)
    {
      fNamespaceContext.pushContext();
      if ((fScannerState == 6) && (fPerformValidation))
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { str1 }, (short)1);
        if ((fDoctypeName == null) || (!fDoctypeName.equals(str1))) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { fDoctypeName, str1 }, (short)1);
        }
      }
    }
    fCurrentElement = fElementStack.pushElement(fElementQName);
    boolean bool = false;
    fAttributes.removeAllAttributes();
    for (;;)
    {
      int i = fEntityScanner.peekChar();
      if (i == 62)
      {
        fEntityScanner.scanChar();
        break;
      }
      if (i == 47)
      {
        fEntityScanner.scanChar();
        if (!fEntityScanner.skipChar(62)) {
          reportFatalError("ElementUnterminated", new Object[] { str1 });
        }
        bool = true;
        break;
      }
      if (((!isValidNameStartChar(i)) || (!fSawSpace)) && ((!isValidNameStartHighSurrogate(i)) || (!fSawSpace))) {
        reportFatalError("ElementUnterminated", new Object[] { str1 });
      }
      scanAttribute(fAttributes);
      fSawSpace = fEntityScanner.skipSpaces();
    }
    if (fBindNamespaces)
    {
      if (fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
        fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { fElementQName.rawname }, (short)2);
      }
      String str2 = fElementQName.prefix != null ? fElementQName.prefix : XMLSymbols.EMPTY_STRING;
      fElementQName.uri = fNamespaceContext.getURI(str2);
      fCurrentElement.uri = fElementQName.uri;
      if ((fElementQName.prefix == null) && (fElementQName.uri != null))
      {
        fElementQName.prefix = XMLSymbols.EMPTY_STRING;
        fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
      }
      if ((fElementQName.prefix != null) && (fElementQName.uri == null)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { fElementQName.prefix, fElementQName.rawname }, (short)2);
      }
      int j = fAttributes.getLength();
      Object localObject;
      for (int k = 0; k < j; k++)
      {
        fAttributes.getName(k, fAttributeQName);
        localObject = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
        String str3 = fNamespaceContext.getURI((String)localObject);
        if (((fAttributeQName.uri == null) || (fAttributeQName.uri != str3)) && (localObject != XMLSymbols.EMPTY_STRING))
        {
          fAttributeQName.uri = str3;
          if (str3 == null) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { fElementQName.rawname, fAttributeQName.rawname, localObject }, (short)2);
          }
          fAttributes.setURI(k, str3);
        }
      }
      if (j > 1)
      {
        localObject = fAttributes.checkDuplicatesNS();
        if (localObject != null) {
          if (uri != null) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { fElementQName.rawname, localpart, uri }, (short)2);
          } else {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[] { fElementQName.rawname, rawname }, (short)2);
          }
        }
      }
    }
    if (fDocumentHandler != null) {
      if (bool)
      {
        fMarkupDepth -= 1;
        if (fMarkupDepth < fEntityStack[(fEntityDepth - 1)]) {
          reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
        }
        fDocumentHandler.emptyElement(fElementQName, fAttributes, null);
        if (fBindNamespaces) {
          fNamespaceContext.popContext();
        }
        fElementStack.popElement(fElementQName);
      }
      else
      {
        fDocumentHandler.startElement(fElementQName, fAttributes, null);
      }
    }
    return bool;
  }
  
  protected void scanAttribute(XMLAttributesImpl paramXMLAttributesImpl)
    throws IOException, XNIException
  {
    fEntityScanner.scanQName(fAttributeQName);
    fEntityScanner.skipSpaces();
    if (!fEntityScanner.skipChar(61)) {
      reportFatalError("EqRequiredInAttribute", new Object[] { fCurrentElement.rawname, fAttributeQName.rawname });
    }
    fEntityScanner.skipSpaces();
    int i;
    if (fBindNamespaces)
    {
      i = paramXMLAttributesImpl.getLength();
      paramXMLAttributesImpl.addAttributeNS(fAttributeQName, XMLSymbols.fCDATASymbol, null);
    }
    else
    {
      int j = paramXMLAttributesImpl.getLength();
      i = paramXMLAttributesImpl.addAttribute(fAttributeQName, XMLSymbols.fCDATASymbol, null);
      if (j == paramXMLAttributesImpl.getLength()) {
        reportFatalError("AttributeNotUnique", new Object[] { fCurrentElement.rawname, fAttributeQName.rawname });
      }
    }
    boolean bool = scanAttributeValue(fTempString, fTempString2, fAttributeQName.rawname, fIsEntityDeclaredVC, fCurrentElement.rawname);
    String str1 = fTempString.toString();
    paramXMLAttributesImpl.setValue(i, str1);
    if (!bool) {
      paramXMLAttributesImpl.setNonNormalizedValue(i, fTempString2.toString());
    }
    paramXMLAttributesImpl.setSpecified(i, true);
    if (fBindNamespaces)
    {
      String str2 = fAttributeQName.localpart;
      String str3 = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
      if ((str3 == XMLSymbols.PREFIX_XMLNS) || ((str3 == XMLSymbols.EMPTY_STRING) && (str2 == XMLSymbols.PREFIX_XMLNS)))
      {
        String str4 = fSymbolTable.addSymbol(str1);
        if ((str3 == XMLSymbols.PREFIX_XMLNS) && (str2 == XMLSymbols.PREFIX_XMLNS)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { fAttributeQName }, (short)2);
        }
        if (str4 == NamespaceContext.XMLNS_URI) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { fAttributeQName }, (short)2);
        }
        if (str2 == XMLSymbols.PREFIX_XML)
        {
          if (str4 != NamespaceContext.XML_URI) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { fAttributeQName }, (short)2);
          }
        }
        else if (str4 == NamespaceContext.XML_URI) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { fAttributeQName }, (short)2);
        }
        str3 = str2 != XMLSymbols.PREFIX_XMLNS ? str2 : XMLSymbols.EMPTY_STRING;
        fNamespaceContext.declarePrefix(str3, str4.length() != 0 ? str4 : null);
        paramXMLAttributesImpl.setURI(i, fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS));
      }
      else if (fAttributeQName.prefix != null)
      {
        paramXMLAttributesImpl.setURI(i, fNamespaceContext.getURI(fAttributeQName.prefix));
      }
    }
  }
  
  protected int scanEndElement()
    throws IOException, XNIException
  {
    fElementStack.popElement(fElementQName);
    if (!fEntityScanner.skipString(fElementQName.rawname)) {
      reportFatalError("ETagRequired", new Object[] { fElementQName.rawname });
    }
    fEntityScanner.skipSpaces();
    if (!fEntityScanner.skipChar(62)) {
      reportFatalError("ETagUnterminated", new Object[] { fElementQName.rawname });
    }
    fMarkupDepth -= 1;
    fMarkupDepth -= 1;
    if (fMarkupDepth < fEntityStack[(fEntityDepth - 1)]) {
      reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
    }
    if (fDocumentHandler != null)
    {
      fDocumentHandler.endElement(fElementQName, null);
      if (fBindNamespaces) {
        fNamespaceContext.popContext();
      }
    }
    return fMarkupDepth;
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    super.reset(paramXMLComponentManager);
    fPerformValidation = false;
    fBindNamespaces = false;
  }
  
  protected XMLDocumentFragmentScannerImpl.Dispatcher createContentDispatcher()
  {
    return new NS11ContentDispatcher();
  }
  
  protected final class NS11ContentDispatcher
    extends XMLDocumentScannerImpl.ContentDispatcher
  {
    protected NS11ContentDispatcher()
    {
      super();
    }
    
    protected boolean scanRootElementHook()
      throws IOException, XNIException
    {
      if ((fExternalSubsetResolver != null) && (!fSeenDoctypeDecl) && (!fDisallowDoctype) && ((fValidation) || (fLoadExternalDTD)))
      {
        scanStartElementName();
        resolveExternalSubsetAndRead();
        reconfigurePipeline();
        if (scanStartElementAfterName())
        {
          setScannerState(12);
          setDispatcher(fTrailingMiscDispatcher);
          return true;
        }
      }
      else
      {
        reconfigurePipeline();
        if (scanStartElement())
        {
          setScannerState(12);
          setDispatcher(fTrailingMiscDispatcher);
          return true;
        }
      }
      return false;
    }
    
    private void reconfigurePipeline()
    {
      if (fDTDValidator == null)
      {
        fBindNamespaces = true;
      }
      else if (!fDTDValidator.hasGrammar())
      {
        fBindNamespaces = true;
        fPerformValidation = fDTDValidator.validate();
        XMLDocumentSource localXMLDocumentSource = fDTDValidator.getDocumentSource();
        XMLDocumentHandler localXMLDocumentHandler = fDTDValidator.getDocumentHandler();
        localXMLDocumentSource.setDocumentHandler(localXMLDocumentHandler);
        if (localXMLDocumentHandler != null) {
          localXMLDocumentHandler.setDocumentSource(localXMLDocumentSource);
        }
        fDTDValidator.setDocumentSource(null);
        fDTDValidator.setDocumentHandler(null);
      }
    }
  }
}
