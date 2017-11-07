package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDTDScannerImpl
  extends XMLScanner
  implements XMLDTDScanner, XMLComponent, XMLEntityHandler
{
  protected static final int SCANNER_STATE_END_OF_INPUT = 0;
  protected static final int SCANNER_STATE_TEXT_DECL = 1;
  protected static final int SCANNER_STATE_MARKUP_DECL = 2;
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-char-refs" };
  private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.FALSE };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null };
  private static final boolean DEBUG_SCANNER_STATE = false;
  protected XMLDTDHandler fDTDHandler;
  protected XMLDTDContentModelHandler fDTDContentModelHandler;
  protected int fScannerState;
  protected boolean fStandalone;
  protected boolean fSeenExternalDTD;
  protected boolean fSeenPEReferences;
  private boolean fStartDTDCalled;
  private int[] fContentStack = new int[5];
  private int fContentDepth;
  private int[] fPEStack = new int[5];
  private boolean[] fPEReport = new boolean[5];
  private int fPEDepth;
  private int fMarkUpDepth;
  private int fExtEntityDepth;
  private int fIncludeSectDepth;
  private final String[] fStrings = new String[3];
  private final XMLString fString = new XMLString();
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
  private final XMLString fLiteral = new XMLString();
  private final XMLString fLiteral2 = new XMLString();
  private String[] fEnumeration = new String[5];
  private int fEnumerationCount;
  private final XMLStringBuffer fIgnoreConditionalBuffer = new XMLStringBuffer(128);
  
  public XMLDTDScannerImpl() {}
  
  public XMLDTDScannerImpl(SymbolTable paramSymbolTable, XMLErrorReporter paramXMLErrorReporter, XMLEntityManager paramXMLEntityManager)
  {
    fSymbolTable = paramSymbolTable;
    fErrorReporter = paramXMLErrorReporter;
    fEntityManager = paramXMLEntityManager;
    paramXMLEntityManager.setProperty("http://apache.org/xml/properties/internal/symbol-table", fSymbolTable);
  }
  
  public void setInputSource(XMLInputSource paramXMLInputSource)
    throws IOException
  {
    if (paramXMLInputSource == null)
    {
      if (fDTDHandler != null)
      {
        fDTDHandler.startDTD(null, null);
        fDTDHandler.endDTD(null);
      }
      return;
    }
    fEntityManager.setEntityHandler(this);
    fEntityManager.startDTDEntity(paramXMLInputSource);
  }
  
  public boolean scanDTDExternalSubset(boolean paramBoolean)
    throws IOException, XNIException
  {
    fEntityManager.setEntityHandler(this);
    if (fScannerState == 1)
    {
      fSeenExternalDTD = true;
      boolean bool = scanTextDecl();
      if (fScannerState == 0) {
        return false;
      }
      setScannerState(2);
      if ((bool) && (!paramBoolean)) {
        return true;
      }
    }
    do
    {
      if (!scanDecls(paramBoolean)) {
        return false;
      }
    } while (paramBoolean);
    return true;
  }
  
  public boolean scanDTDInternalSubset(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws IOException, XNIException
  {
    fEntityScanner = fEntityManager.getEntityScanner();
    fEntityManager.setEntityHandler(this);
    fStandalone = paramBoolean2;
    if (fScannerState == 1)
    {
      if (fDTDHandler != null)
      {
        fDTDHandler.startDTD(fEntityScanner, null);
        fStartDTDCalled = true;
      }
      setScannerState(2);
    }
    do
    {
      if (!scanDecls(paramBoolean1))
      {
        if ((fDTDHandler != null) && (!paramBoolean3)) {
          fDTDHandler.endDTD(null);
        }
        setScannerState(1);
        return false;
      }
    } while (paramBoolean1);
    return true;
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    super.reset(paramXMLComponentManager);
    init();
  }
  
  public void reset()
  {
    super.reset();
    init();
  }
  
  public String[] getRecognizedFeatures()
  {
    return (String[])RECOGNIZED_FEATURES.clone();
  }
  
  public String[] getRecognizedProperties()
  {
    return (String[])RECOGNIZED_PROPERTIES.clone();
  }
  
  public Boolean getFeatureDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
      if (RECOGNIZED_FEATURES[i].equals(paramString)) {
        return FEATURE_DEFAULTS[i];
      }
    }
    return null;
  }
  
  public Object getPropertyDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
      if (RECOGNIZED_PROPERTIES[i].equals(paramString)) {
        return PROPERTY_DEFAULTS[i];
      }
    }
    return null;
  }
  
  public void setDTDHandler(XMLDTDHandler paramXMLDTDHandler)
  {
    fDTDHandler = paramXMLDTDHandler;
  }
  
  public XMLDTDHandler getDTDHandler()
  {
    return fDTDHandler;
  }
  
  public void setDTDContentModelHandler(XMLDTDContentModelHandler paramXMLDTDContentModelHandler)
  {
    fDTDContentModelHandler = paramXMLDTDContentModelHandler;
  }
  
  public XMLDTDContentModelHandler getDTDContentModelHandler()
  {
    return fDTDContentModelHandler;
  }
  
  public void startEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    super.startEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    boolean bool = paramString1.equals("[dtd]");
    if (bool)
    {
      if ((fDTDHandler != null) && (!fStartDTDCalled)) {
        fDTDHandler.startDTD(fEntityScanner, null);
      }
      if (fDTDHandler != null) {
        fDTDHandler.startExternalSubset(paramXMLResourceIdentifier, null);
      }
      fEntityManager.startExternalSubset();
      fExtEntityDepth += 1;
    }
    else if (paramString1.charAt(0) == '%')
    {
      pushPEStack(fMarkUpDepth, fReportEntity);
      if (fEntityScanner.isExternal()) {
        fExtEntityDepth += 1;
      }
    }
    if ((fDTDHandler != null) && (!bool) && (fReportEntity)) {
      fDTDHandler.startParameterEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void endEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    super.endEntity(paramString, paramAugmentations);
    if (fScannerState == 0) {
      return;
    }
    boolean bool = fReportEntity;
    if (paramString.startsWith("%"))
    {
      bool = peekReportEntity();
      int i = popPEStack();
      if ((i == 0) && (i < fMarkUpDepth)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ILL_FORMED_PARAMETER_ENTITY_WHEN_USED_IN_DECL", new Object[] { fEntityManager.fCurrentEntity.name }, (short)2);
      }
      if (i != fMarkUpDepth)
      {
        bool = false;
        if (fValidation) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ImproperDeclarationNesting", new Object[] { paramString }, (short)1);
        }
      }
      if (fEntityScanner.isExternal()) {
        fExtEntityDepth -= 1;
      }
      if ((fDTDHandler != null) && (bool)) {
        fDTDHandler.endParameterEntity(paramString, paramAugmentations);
      }
    }
    else if (paramString.equals("[dtd]"))
    {
      if (fIncludeSectDepth != 0) {
        reportFatalError("IncludeSectUnterminated", null);
      }
      fScannerState = 0;
      fEntityManager.endExternalSubset();
      if (fDTDHandler != null)
      {
        fDTDHandler.endExternalSubset(null);
        fDTDHandler.endDTD(null);
      }
      fExtEntityDepth -= 1;
    }
  }
  
  protected final void setScannerState(int paramInt)
  {
    fScannerState = paramInt;
  }
  
  private static String getScannerStateName(int paramInt)
  {
    return "??? (" + paramInt + ')';
  }
  
  protected final boolean scanningInternalSubset()
  {
    return fExtEntityDepth == 0;
  }
  
  protected void startPE(String paramString, boolean paramBoolean)
    throws IOException, XNIException
  {
    int i = fPEDepth;
    String str = "%" + paramString;
    if (!fSeenPEReferences)
    {
      fSeenPEReferences = true;
      fEntityManager.notifyHasPEReferences();
    }
    if ((fValidation) && (!fEntityManager.isDeclaredEntity(str))) {
      fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { paramString }, (short)1);
    }
    fEntityManager.startEntity(fSymbolTable.addSymbol(str), paramBoolean);
    if ((i != fPEDepth) && (fEntityScanner.isExternal())) {
      scanTextDecl();
    }
  }
  
  protected final boolean scanTextDecl()
    throws IOException, XNIException
  {
    boolean bool = false;
    if (fEntityScanner.skipString("<?xml"))
    {
      fMarkUpDepth += 1;
      String str1;
      if (isValidNameChar(fEntityScanner.peekChar()))
      {
        fStringBuffer.clear();
        fStringBuffer.append("xml");
        if (fNamespaces) {
          while (isValidNCName(fEntityScanner.peekChar())) {
            fStringBuffer.append((char)fEntityScanner.scanChar());
          }
        } else {
          while (isValidNameChar(fEntityScanner.peekChar())) {
            fStringBuffer.append((char)fEntityScanner.scanChar());
          }
        }
        str1 = fSymbolTable.addSymbol(fStringBuffer.ch, fStringBuffer.offset, fStringBuffer.length);
        scanPIData(str1, fString);
      }
      else
      {
        str1 = null;
        String str2 = null;
        scanXMLDeclOrTextDecl(true, fStrings);
        bool = true;
        fMarkUpDepth -= 1;
        str1 = fStrings[0];
        str2 = fStrings[1];
        fEntityScanner.setXMLVersion(str1);
        if (!fEntityScanner.fCurrentEntity.isEncodingExternallySpecified()) {
          fEntityScanner.setEncoding(str2);
        }
        if (fDTDHandler != null) {
          fDTDHandler.textDecl(str1, str2, null);
        }
      }
    }
    fEntityManager.fCurrentEntity.mayReadChunks = true;
    return bool;
  }
  
  protected final void scanPIData(String paramString, XMLString paramXMLString)
    throws IOException, XNIException
  {
    super.scanPIData(paramString, paramXMLString);
    fMarkUpDepth -= 1;
    if (fDTDHandler != null) {
      fDTDHandler.processingInstruction(paramString, paramXMLString, null);
    }
  }
  
  protected final void scanComment()
    throws IOException, XNIException
  {
    fReportEntity = false;
    scanComment(fStringBuffer);
    fMarkUpDepth -= 1;
    if (fDTDHandler != null) {
      fDTDHandler.comment(fStringBuffer, null);
    }
    fReportEntity = true;
  }
  
  protected final void scanElementDecl()
    throws IOException, XNIException
  {
    fReportEntity = false;
    if (!skipSeparator(true, !scanningInternalSubset())) {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ELEMENTDECL", null);
    }
    String str1 = fEntityScanner.scanName();
    if (str1 == null) {
      reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ELEMENTDECL", null);
    }
    if (!skipSeparator(true, !scanningInternalSubset())) {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_CONTENTSPEC_IN_ELEMENTDECL", new Object[] { str1 });
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.startContentModel(str1, null);
    }
    String str2 = null;
    fReportEntity = true;
    if (fEntityScanner.skipString("EMPTY"))
    {
      str2 = "EMPTY";
      if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.empty(null);
      }
    }
    else if (fEntityScanner.skipString("ANY"))
    {
      str2 = "ANY";
      if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.any(null);
      }
    }
    else
    {
      if (!fEntityScanner.skipChar(40)) {
        reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[] { str1 });
      }
      if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.startGroup(null);
      }
      fStringBuffer.clear();
      fStringBuffer.append('(');
      fMarkUpDepth += 1;
      skipSeparator(false, !scanningInternalSubset());
      if (fEntityScanner.skipString("#PCDATA")) {
        scanMixed(str1);
      } else {
        scanChildren(str1);
      }
      str2 = fStringBuffer.toString();
    }
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.endContentModel(null);
    }
    fReportEntity = false;
    skipSeparator(false, !scanningInternalSubset());
    if (!fEntityScanner.skipChar(62)) {
      reportFatalError("ElementDeclUnterminated", new Object[] { str1 });
    }
    fReportEntity = true;
    fMarkUpDepth -= 1;
    if (fDTDHandler != null) {
      fDTDHandler.elementDecl(str1, str2, null);
    }
  }
  
  private final void scanMixed(String paramString)
    throws IOException, XNIException
  {
    String str = null;
    fStringBuffer.append("#PCDATA");
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.pcdata(null);
    }
    skipSeparator(false, !scanningInternalSubset());
    while (fEntityScanner.skipChar(124))
    {
      fStringBuffer.append('|');
      if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.separator((short)0, null);
      }
      skipSeparator(false, !scanningInternalSubset());
      str = fEntityScanner.scanName();
      if (str == null) {
        reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_MIXED_CONTENT", new Object[] { paramString });
      }
      fStringBuffer.append(str);
      if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.element(str, null);
      }
      skipSeparator(false, !scanningInternalSubset());
    }
    if (fEntityScanner.skipString(")*"))
    {
      fStringBuffer.append(")*");
      if (fDTDContentModelHandler != null)
      {
        fDTDContentModelHandler.endGroup(null);
        fDTDContentModelHandler.occurrence((short)3, null);
      }
    }
    else if (str != null)
    {
      reportFatalError("MixedContentUnterminated", new Object[] { paramString });
    }
    else if (fEntityScanner.skipChar(41))
    {
      fStringBuffer.append(')');
      if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.endGroup(null);
      }
    }
    else
    {
      reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[] { paramString });
    }
    fMarkUpDepth -= 1;
  }
  
  private final void scanChildren(String paramString)
    throws IOException, XNIException
  {
    fContentDepth = 0;
    pushContentStack(0);
    int i = 0;
    for (;;)
    {
      if (fEntityScanner.skipChar(40))
      {
        fMarkUpDepth += 1;
        fStringBuffer.append('(');
        if (fDTDContentModelHandler != null) {
          fDTDContentModelHandler.startGroup(null);
        }
        pushContentStack(i);
        i = 0;
        skipSeparator(false, !scanningInternalSubset());
      }
      else
      {
        skipSeparator(false, !scanningInternalSubset());
        String str = fEntityScanner.scanName();
        if (str == null)
        {
          reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[] { paramString });
          return;
        }
        if (fDTDContentModelHandler != null) {
          fDTDContentModelHandler.element(str, null);
        }
        fStringBuffer.append(str);
        int j = fEntityScanner.peekChar();
        short s;
        if ((j == 63) || (j == 42) || (j == 43))
        {
          if (fDTDContentModelHandler != null)
          {
            if (j == 63) {
              s = 2;
            } else if (j == 42) {
              s = 3;
            } else {
              s = 4;
            }
            fDTDContentModelHandler.occurrence(s, null);
          }
          fEntityScanner.scanChar();
          fStringBuffer.append((char)j);
        }
        do
        {
          skipSeparator(false, !scanningInternalSubset());
          j = fEntityScanner.peekChar();
          if ((j == 44) && (i != 124))
          {
            i = j;
            if (fDTDContentModelHandler != null) {
              fDTDContentModelHandler.separator((short)1, null);
            }
            fEntityScanner.scanChar();
            fStringBuffer.append(',');
            break;
          }
          if ((j == 124) && (i != 44))
          {
            i = j;
            if (fDTDContentModelHandler != null) {
              fDTDContentModelHandler.separator((short)0, null);
            }
            fEntityScanner.scanChar();
            fStringBuffer.append('|');
            break;
          }
          if (j != 41) {
            reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[] { paramString });
          }
          if (fDTDContentModelHandler != null) {
            fDTDContentModelHandler.endGroup(null);
          }
          i = popContentStack();
          if (fEntityScanner.skipString(")?"))
          {
            fStringBuffer.append(")?");
            if (fDTDContentModelHandler != null)
            {
              s = 2;
              fDTDContentModelHandler.occurrence(s, null);
            }
          }
          else if (fEntityScanner.skipString(")+"))
          {
            fStringBuffer.append(")+");
            if (fDTDContentModelHandler != null)
            {
              s = 4;
              fDTDContentModelHandler.occurrence(s, null);
            }
          }
          else if (fEntityScanner.skipString(")*"))
          {
            fStringBuffer.append(")*");
            if (fDTDContentModelHandler != null)
            {
              s = 3;
              fDTDContentModelHandler.occurrence(s, null);
            }
          }
          else
          {
            fEntityScanner.scanChar();
            fStringBuffer.append(')');
          }
          fMarkUpDepth -= 1;
        } while (fContentDepth != 0);
        return;
        skipSeparator(false, !scanningInternalSubset());
      }
    }
  }
  
  protected final void scanAttlistDecl()
    throws IOException, XNIException
  {
    fReportEntity = false;
    if (!skipSeparator(true, !scanningInternalSubset())) {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ATTLISTDECL", null);
    }
    String str1 = fEntityScanner.scanName();
    if (str1 == null) {
      reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ATTLISTDECL", null);
    }
    if (fDTDHandler != null) {
      fDTDHandler.startAttlist(str1, null);
    }
    if (!skipSeparator(true, !scanningInternalSubset()))
    {
      if (fEntityScanner.skipChar(62))
      {
        if (fDTDHandler != null) {
          fDTDHandler.endAttlist(null);
        }
        fMarkUpDepth -= 1;
        return;
      }
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTRIBUTE_NAME_IN_ATTDEF", new Object[] { str1 });
    }
    while (!fEntityScanner.skipChar(62))
    {
      String str2 = fEntityScanner.scanName();
      if (str2 == null) {
        reportFatalError("AttNameRequiredInAttDef", new Object[] { str1 });
      }
      if (!skipSeparator(true, !scanningInternalSubset())) {
        reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTTYPE_IN_ATTDEF", new Object[] { str1, str2 });
      }
      String str3 = scanAttType(str1, str2);
      if (!skipSeparator(true, !scanningInternalSubset())) {
        reportFatalError("MSG_SPACE_REQUIRED_BEFORE_DEFAULTDECL_IN_ATTDEF", new Object[] { str1, str2 });
      }
      String str4 = scanAttDefaultDecl(str1, str2, str3, fLiteral, fLiteral2);
      if (fDTDHandler != null)
      {
        String[] arrayOfString = null;
        if (fEnumerationCount != 0)
        {
          arrayOfString = new String[fEnumerationCount];
          System.arraycopy(fEnumeration, 0, arrayOfString, 0, fEnumerationCount);
        }
        if ((str4 != null) && ((str4.equals("#REQUIRED")) || (str4.equals("#IMPLIED")))) {
          fDTDHandler.attributeDecl(str1, str2, str3, arrayOfString, str4, null, null, null);
        } else {
          fDTDHandler.attributeDecl(str1, str2, str3, arrayOfString, str4, fLiteral, fLiteral2, null);
        }
      }
      skipSeparator(false, !scanningInternalSubset());
    }
    if (fDTDHandler != null) {
      fDTDHandler.endAttlist(null);
    }
    fMarkUpDepth -= 1;
    fReportEntity = true;
  }
  
  private final String scanAttType(String paramString1, String paramString2)
    throws IOException, XNIException
  {
    String str1 = null;
    fEnumerationCount = 0;
    if (fEntityScanner.skipString("CDATA"))
    {
      str1 = "CDATA";
    }
    else if (fEntityScanner.skipString("IDREFS"))
    {
      str1 = "IDREFS";
    }
    else if (fEntityScanner.skipString("IDREF"))
    {
      str1 = "IDREF";
    }
    else if (fEntityScanner.skipString("ID"))
    {
      str1 = "ID";
    }
    else if (fEntityScanner.skipString("ENTITY"))
    {
      str1 = "ENTITY";
    }
    else if (fEntityScanner.skipString("ENTITIES"))
    {
      str1 = "ENTITIES";
    }
    else if (fEntityScanner.skipString("NMTOKENS"))
    {
      str1 = "NMTOKENS";
    }
    else if (fEntityScanner.skipString("NMTOKEN"))
    {
      str1 = "NMTOKEN";
    }
    else
    {
      int i;
      String str2;
      if (fEntityScanner.skipString("NOTATION"))
      {
        str1 = "NOTATION";
        if (!skipSeparator(true, !scanningInternalSubset())) {
          reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_IN_NOTATIONTYPE", new Object[] { paramString1, paramString2 });
        }
        i = fEntityScanner.scanChar();
        if (i != 40) {
          reportFatalError("MSG_OPEN_PAREN_REQUIRED_IN_NOTATIONTYPE", new Object[] { paramString1, paramString2 });
        }
        fMarkUpDepth += 1;
        do
        {
          skipSeparator(false, !scanningInternalSubset());
          str2 = fEntityScanner.scanName();
          if (str2 == null)
          {
            reportFatalError("MSG_NAME_REQUIRED_IN_NOTATIONTYPE", new Object[] { paramString1, paramString2 });
            i = skipInvalidEnumerationValue();
            if (i != 124) {
              break;
            }
          }
          else
          {
            ensureEnumerationSize(fEnumerationCount + 1);
            fEnumeration[(fEnumerationCount++)] = str2;
            skipSeparator(false, !scanningInternalSubset());
            i = fEntityScanner.scanChar();
          }
        } while (i == 124);
        if (i != 41) {
          reportFatalError("NotationTypeUnterminated", new Object[] { paramString1, paramString2 });
        }
        fMarkUpDepth -= 1;
      }
      else
      {
        str1 = "ENUMERATION";
        i = fEntityScanner.scanChar();
        if (i != 40) {
          reportFatalError("AttTypeRequiredInAttDef", new Object[] { paramString1, paramString2 });
        }
        fMarkUpDepth += 1;
        do
        {
          skipSeparator(false, !scanningInternalSubset());
          str2 = fEntityScanner.scanNmtoken();
          if (str2 == null)
          {
            reportFatalError("MSG_NMTOKEN_REQUIRED_IN_ENUMERATION", new Object[] { paramString1, paramString2 });
            i = skipInvalidEnumerationValue();
            if (i != 124) {
              break;
            }
          }
          else
          {
            ensureEnumerationSize(fEnumerationCount + 1);
            fEnumeration[(fEnumerationCount++)] = str2;
            skipSeparator(false, !scanningInternalSubset());
            i = fEntityScanner.scanChar();
          }
        } while (i == 124);
        if (i != 41) {
          reportFatalError("EnumerationUnterminated", new Object[] { paramString1, paramString2 });
        }
        fMarkUpDepth -= 1;
      }
    }
    return str1;
  }
  
  protected final String scanAttDefaultDecl(String paramString1, String paramString2, String paramString3, XMLString paramXMLString1, XMLString paramXMLString2)
    throws IOException, XNIException
  {
    String str = null;
    fString.clear();
    paramXMLString1.clear();
    if (fEntityScanner.skipString("#REQUIRED"))
    {
      str = "#REQUIRED";
    }
    else if (fEntityScanner.skipString("#IMPLIED"))
    {
      str = "#IMPLIED";
    }
    else
    {
      if (fEntityScanner.skipString("#FIXED"))
      {
        str = "#FIXED";
        if (!skipSeparator(true, !scanningInternalSubset())) {
          reportFatalError("MSG_SPACE_REQUIRED_AFTER_FIXED_IN_DEFAULTDECL", new Object[] { paramString1, paramString2 });
        }
      }
      boolean bool = (!fStandalone) && ((fSeenExternalDTD) || (fSeenPEReferences));
      scanAttributeValue(paramXMLString1, paramXMLString2, paramString2, bool, paramString1);
    }
    return str;
  }
  
  private final void scanEntityDecl()
    throws IOException, XNIException
  {
    boolean bool1 = false;
    int i = 0;
    fReportEntity = false;
    if (fEntityScanner.skipSpaces())
    {
      if (!fEntityScanner.skipChar(37))
      {
        bool1 = false;
      }
      else if (skipSeparator(true, !scanningInternalSubset()))
      {
        bool1 = true;
      }
      else if (scanningInternalSubset())
      {
        reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_PEDECL", null);
        bool1 = true;
      }
      else if (fEntityScanner.peekChar() == 37)
      {
        skipSeparator(false, !scanningInternalSubset());
        bool1 = true;
      }
      else
      {
        i = 1;
      }
    }
    else if ((scanningInternalSubset()) || (!fEntityScanner.skipChar(37)))
    {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL", null);
      bool1 = false;
    }
    else if (fEntityScanner.skipSpaces())
    {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_PERCENT_IN_PEDECL", null);
      bool1 = false;
    }
    else
    {
      i = 1;
    }
    if (i != 0) {
      for (;;)
      {
        str1 = fEntityScanner.scanName();
        if (str1 == null) {
          reportFatalError("NameRequiredInPEReference", null);
        } else if (!fEntityScanner.skipChar(59)) {
          reportFatalError("SemicolonRequiredInPEReference", new Object[] { str1 });
        } else {
          startPE(str1, false);
        }
        fEntityScanner.skipSpaces();
        if (!fEntityScanner.skipChar(37)) {
          break;
        }
        if (!bool1)
        {
          if (skipSeparator(true, !scanningInternalSubset()))
          {
            bool1 = true;
            break;
          }
          bool1 = fEntityScanner.skipChar(37);
        }
      }
    }
    String str1 = null;
    if (fNamespaces) {
      str1 = fEntityScanner.scanNCName();
    } else {
      str1 = fEntityScanner.scanName();
    }
    if (str1 == null) {
      reportFatalError("MSG_ENTITY_NAME_REQUIRED_IN_ENTITYDECL", null);
    }
    if (!skipSeparator(true, !scanningInternalSubset())) {
      if ((fNamespaces) && (fEntityScanner.peekChar() == 58))
      {
        fEntityScanner.scanChar();
        localObject = new XMLStringBuffer(str1);
        ((XMLStringBuffer)localObject).append(':');
        str2 = fEntityScanner.scanName();
        if (str2 != null) {
          ((XMLStringBuffer)localObject).append(str2);
        }
        reportFatalError("ColonNotLegalWithNS", new Object[] { ((XMLStringBuffer)localObject).toString() });
        if (!skipSeparator(true, !scanningInternalSubset())) {
          reportFatalError("MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL", new Object[] { str1 });
        }
      }
      else
      {
        reportFatalError("MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL", new Object[] { str1 });
      }
    }
    scanExternalID(fStrings, false);
    Object localObject = fStrings[0];
    String str2 = fStrings[1];
    String str3 = null;
    boolean bool2 = skipSeparator(true, !scanningInternalSubset());
    if ((!bool1) && (fEntityScanner.skipString("NDATA")))
    {
      if (!bool2) {
        reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NDATA_IN_UNPARSED_ENTITYDECL", new Object[] { str1 });
      }
      if (!skipSeparator(true, !scanningInternalSubset())) {
        reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_UNPARSED_ENTITYDECL", new Object[] { str1 });
      }
      str3 = fEntityScanner.scanName();
      if (str3 == null) {
        reportFatalError("MSG_NOTATION_NAME_REQUIRED_FOR_UNPARSED_ENTITYDECL", new Object[] { str1 });
      }
    }
    if (localObject == null)
    {
      scanEntityValue(fLiteral, fLiteral2);
      fStringBuffer.clear();
      fStringBuffer2.clear();
      fStringBuffer.append(fLiteral.ch, fLiteral.offset, fLiteral.length);
      fStringBuffer2.append(fLiteral2.ch, fLiteral2.offset, fLiteral2.length);
    }
    skipSeparator(false, !scanningInternalSubset());
    if (!fEntityScanner.skipChar(62)) {
      reportFatalError("EntityDeclUnterminated", new Object[] { str1 });
    }
    fMarkUpDepth -= 1;
    if (bool1) {
      str1 = "%" + str1;
    }
    if (localObject != null)
    {
      String str4 = fEntityScanner.getBaseSystemId();
      if (str3 != null) {
        fEntityManager.addUnparsedEntity(str1, str2, (String)localObject, str4, str3);
      } else {
        fEntityManager.addExternalEntity(str1, str2, (String)localObject, str4);
      }
      if (fDTDHandler != null)
      {
        fResourceIdentifier.setValues(str2, (String)localObject, str4, XMLEntityManager.expandSystemId((String)localObject, str4, false));
        if (str3 != null) {
          fDTDHandler.unparsedEntityDecl(str1, fResourceIdentifier, str3, null);
        } else {
          fDTDHandler.externalEntityDecl(str1, fResourceIdentifier, null);
        }
      }
    }
    else
    {
      fEntityManager.addInternalEntity(str1, fStringBuffer.toString());
      if (fDTDHandler != null) {
        fDTDHandler.internalEntityDecl(str1, fStringBuffer, fStringBuffer2, null);
      }
    }
    fReportEntity = true;
  }
  
  protected final void scanEntityValue(XMLString paramXMLString1, XMLString paramXMLString2)
    throws IOException, XNIException
  {
    int i = fEntityScanner.scanChar();
    if ((i != 39) && (i != 34)) {
      reportFatalError("OpenQuoteMissingInDecl", null);
    }
    int j = fEntityDepth;
    Object localObject1 = fString;
    Object localObject2 = fString;
    if (fEntityScanner.scanLiteral(i, fString) != i)
    {
      fStringBuffer.clear();
      fStringBuffer2.clear();
      do
      {
        fStringBuffer.append(fString);
        fStringBuffer2.append(fString);
        String str;
        if (fEntityScanner.skipChar(38))
        {
          if (fEntityScanner.skipChar(35))
          {
            fStringBuffer2.append("&#");
            scanCharReferenceValue(fStringBuffer, fStringBuffer2);
          }
          else
          {
            fStringBuffer.append('&');
            fStringBuffer2.append('&');
            str = fEntityScanner.scanName();
            if (str == null)
            {
              reportFatalError("NameRequiredInReference", null);
            }
            else
            {
              fStringBuffer.append(str);
              fStringBuffer2.append(str);
            }
            if (!fEntityScanner.skipChar(59))
            {
              reportFatalError("SemicolonRequiredInReference", new Object[] { str });
            }
            else
            {
              fStringBuffer.append(';');
              fStringBuffer2.append(';');
            }
          }
        }
        else if (fEntityScanner.skipChar(37))
        {
          do
          {
            fStringBuffer2.append('%');
            str = fEntityScanner.scanName();
            if (str == null)
            {
              reportFatalError("NameRequiredInPEReference", null);
            }
            else if (!fEntityScanner.skipChar(59))
            {
              reportFatalError("SemicolonRequiredInPEReference", new Object[] { str });
            }
            else
            {
              if (scanningInternalSubset()) {
                reportFatalError("PEReferenceWithinMarkup", new Object[] { str });
              }
              fStringBuffer2.append(str);
              fStringBuffer2.append(';');
            }
            startPE(str, true);
            fEntityScanner.skipSpaces();
          } while (fEntityScanner.skipChar(37));
        }
        else
        {
          int k = fEntityScanner.peekChar();
          if (XMLChar.isHighSurrogate(k))
          {
            scanSurrogates(fStringBuffer2);
          }
          else if (isInvalidLiteral(k))
          {
            reportFatalError("InvalidCharInLiteral", new Object[] { Integer.toHexString(k) });
            fEntityScanner.scanChar();
          }
          else if ((k != i) || (j != fEntityDepth))
          {
            fStringBuffer.append((char)k);
            fStringBuffer2.append((char)k);
            fEntityScanner.scanChar();
          }
        }
      } while (fEntityScanner.scanLiteral(i, fString) != i);
      fStringBuffer.append(fString);
      fStringBuffer2.append(fString);
      localObject1 = fStringBuffer;
      localObject2 = fStringBuffer2;
    }
    paramXMLString1.setValues((XMLString)localObject1);
    paramXMLString2.setValues((XMLString)localObject2);
    if (!fEntityScanner.skipChar(i)) {
      reportFatalError("CloseQuoteMissingInDecl", null);
    }
  }
  
  private final void scanNotationDecl()
    throws IOException, XNIException
  {
    fReportEntity = false;
    if (!skipSeparator(true, !scanningInternalSubset())) {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_NOTATIONDECL", null);
    }
    String str1 = null;
    if (fNamespaces) {
      str1 = fEntityScanner.scanNCName();
    } else {
      str1 = fEntityScanner.scanName();
    }
    if (str1 == null) {
      reportFatalError("MSG_NOTATION_NAME_REQUIRED_IN_NOTATIONDECL", null);
    }
    if (!skipSeparator(true, !scanningInternalSubset())) {
      if ((fNamespaces) && (fEntityScanner.peekChar() == 58))
      {
        fEntityScanner.scanChar();
        localObject = new XMLStringBuffer(str1);
        ((XMLStringBuffer)localObject).append(':');
        ((XMLStringBuffer)localObject).append(fEntityScanner.scanName());
        reportFatalError("ColonNotLegalWithNS", new Object[] { ((XMLStringBuffer)localObject).toString() });
        skipSeparator(true, !scanningInternalSubset());
      }
      else
      {
        reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_NAME_IN_NOTATIONDECL", new Object[] { str1 });
      }
    }
    scanExternalID(fStrings, true);
    Object localObject = fStrings[0];
    String str2 = fStrings[1];
    String str3 = fEntityScanner.getBaseSystemId();
    if ((localObject == null) && (str2 == null)) {
      reportFatalError("ExternalIDorPublicIDRequired", new Object[] { str1 });
    }
    skipSeparator(false, !scanningInternalSubset());
    if (!fEntityScanner.skipChar(62)) {
      reportFatalError("NotationDeclUnterminated", new Object[] { str1 });
    }
    fMarkUpDepth -= 1;
    if (fDTDHandler != null)
    {
      fResourceIdentifier.setValues(str2, (String)localObject, str3, XMLEntityManager.expandSystemId((String)localObject, str3, false));
      fDTDHandler.notationDecl(str1, fResourceIdentifier, null);
    }
    fReportEntity = true;
  }
  
  private final void scanConditionalSect(int paramInt)
    throws IOException, XNIException
  {
    fReportEntity = false;
    skipSeparator(false, !scanningInternalSubset());
    if (fEntityScanner.skipString("INCLUDE"))
    {
      skipSeparator(false, !scanningInternalSubset());
      if ((paramInt != fPEDepth) && (fValidation)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[] { fEntityManager.fCurrentEntity.name }, (short)1);
      }
      if (!fEntityScanner.skipChar(91)) {
        reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
      }
      if (fDTDHandler != null) {
        fDTDHandler.startConditional((short)0, null);
      }
      fIncludeSectDepth += 1;
      fReportEntity = true;
    }
    else
    {
      if (fEntityScanner.skipString("IGNORE"))
      {
        skipSeparator(false, !scanningInternalSubset());
        if ((paramInt != fPEDepth) && (fValidation)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[] { fEntityManager.fCurrentEntity.name }, (short)1);
        }
        if (fDTDHandler != null) {
          fDTDHandler.startConditional((short)1, null);
        }
        if (!fEntityScanner.skipChar(91)) {
          reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
        }
        fReportEntity = true;
        int i = ++fIncludeSectDepth;
        if (fDTDHandler != null) {
          fIgnoreConditionalBuffer.clear();
        }
        for (;;)
        {
          if (fEntityScanner.skipChar(60))
          {
            if (fDTDHandler != null) {
              fIgnoreConditionalBuffer.append('<');
            }
            if (fEntityScanner.skipChar(33)) {
              if (fEntityScanner.skipChar(91))
              {
                if (fDTDHandler != null) {
                  fIgnoreConditionalBuffer.append("![");
                }
                fIncludeSectDepth += 1;
              }
              else if (fDTDHandler != null)
              {
                fIgnoreConditionalBuffer.append("!");
              }
            }
          }
          else if (fEntityScanner.skipChar(93))
          {
            if (fDTDHandler != null) {
              fIgnoreConditionalBuffer.append(']');
            }
            if (fEntityScanner.skipChar(93))
            {
              if (fDTDHandler != null) {
                fIgnoreConditionalBuffer.append(']');
              }
              while (fEntityScanner.skipChar(93)) {
                if (fDTDHandler != null) {
                  fIgnoreConditionalBuffer.append(']');
                }
              }
              if (fEntityScanner.skipChar(62))
              {
                if (fIncludeSectDepth-- == i)
                {
                  fMarkUpDepth -= 1;
                  if (fDTDHandler != null)
                  {
                    fLiteral.setValues(fIgnoreConditionalBuffer.ch, 0, fIgnoreConditionalBuffer.length - 2);
                    fDTDHandler.ignoredCharacters(fLiteral, null);
                    fDTDHandler.endConditional(null);
                  }
                  return;
                }
                if (fDTDHandler != null) {
                  fIgnoreConditionalBuffer.append('>');
                }
              }
            }
          }
          else
          {
            int j = fEntityScanner.scanChar();
            if (fScannerState == 0)
            {
              reportFatalError("IgnoreSectUnterminated", null);
              return;
            }
            if (fDTDHandler != null) {
              fIgnoreConditionalBuffer.append((char)j);
            }
          }
        }
      }
      reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
    }
  }
  
  protected final boolean scanDecls(boolean paramBoolean)
    throws IOException, XNIException
  {
    skipSeparator(false, true);
    boolean bool = true;
    while ((bool) && (fScannerState == 2))
    {
      bool = paramBoolean;
      if (fEntityScanner.skipChar(60))
      {
        fMarkUpDepth += 1;
        if (fEntityScanner.skipChar(63))
        {
          scanPI();
        }
        else if (fEntityScanner.skipChar(33))
        {
          if (fEntityScanner.skipChar(45))
          {
            if (!fEntityScanner.skipChar(45)) {
              reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
            } else {
              scanComment();
            }
          }
          else if (fEntityScanner.skipString("ELEMENT"))
          {
            scanElementDecl();
          }
          else if (fEntityScanner.skipString("ATTLIST"))
          {
            scanAttlistDecl();
          }
          else if (fEntityScanner.skipString("ENTITY"))
          {
            scanEntityDecl();
          }
          else if (fEntityScanner.skipString("NOTATION"))
          {
            scanNotationDecl();
          }
          else if ((fEntityScanner.skipChar(91)) && (!scanningInternalSubset()))
          {
            scanConditionalSect(fPEDepth);
          }
          else
          {
            fMarkUpDepth -= 1;
            reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
          }
        }
        else
        {
          fMarkUpDepth -= 1;
          reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
        }
      }
      else if ((fIncludeSectDepth > 0) && (fEntityScanner.skipChar(93)))
      {
        if ((!fEntityScanner.skipChar(93)) || (!fEntityScanner.skipChar(62))) {
          reportFatalError("IncludeSectUnterminated", null);
        }
        if (fDTDHandler != null) {
          fDTDHandler.endConditional(null);
        }
        fIncludeSectDepth -= 1;
        fMarkUpDepth -= 1;
      }
      else
      {
        if ((scanningInternalSubset()) && (fEntityScanner.peekChar() == 93)) {
          return false;
        }
        if (!fEntityScanner.skipSpaces())
        {
          reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
          int i;
          do
          {
            fEntityScanner.scanChar();
            skipSeparator(false, true);
            i = fEntityScanner.peekChar();
          } while ((i != 60) && (i != 93) && (!XMLChar.isSpace(i)));
        }
      }
      skipSeparator(false, true);
    }
    return fScannerState != 0;
  }
  
  private boolean skipSeparator(boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, XNIException
  {
    int i = fPEDepth;
    boolean bool = fEntityScanner.skipSpaces();
    if ((!paramBoolean2) || (!fEntityScanner.skipChar(37))) {
      return (!paramBoolean1) || (bool) || (i != fPEDepth);
    }
    do
    {
      String str = fEntityScanner.scanName();
      if (str == null) {
        reportFatalError("NameRequiredInPEReference", null);
      } else if (!fEntityScanner.skipChar(59)) {
        reportFatalError("SemicolonRequiredInPEReference", new Object[] { str });
      }
      startPE(str, false);
      fEntityScanner.skipSpaces();
    } while (fEntityScanner.skipChar(37));
    return true;
  }
  
  private final void pushContentStack(int paramInt)
  {
    if (fContentStack.length == fContentDepth)
    {
      int[] arrayOfInt = new int[fContentDepth * 2];
      System.arraycopy(fContentStack, 0, arrayOfInt, 0, fContentDepth);
      fContentStack = arrayOfInt;
    }
    fContentStack[(fContentDepth++)] = paramInt;
  }
  
  private final int popContentStack()
  {
    return fContentStack[(--fContentDepth)];
  }
  
  private final void pushPEStack(int paramInt, boolean paramBoolean)
  {
    if (fPEStack.length == fPEDepth)
    {
      int[] arrayOfInt = new int[fPEDepth * 2];
      System.arraycopy(fPEStack, 0, arrayOfInt, 0, fPEDepth);
      fPEStack = arrayOfInt;
      boolean[] arrayOfBoolean = new boolean[fPEDepth * 2];
      System.arraycopy(fPEReport, 0, arrayOfBoolean, 0, fPEDepth);
      fPEReport = arrayOfBoolean;
    }
    fPEReport[fPEDepth] = paramBoolean;
    fPEStack[(fPEDepth++)] = paramInt;
  }
  
  private final int popPEStack()
  {
    return fPEStack[(--fPEDepth)];
  }
  
  private final boolean peekReportEntity()
  {
    return fPEReport[(fPEDepth - 1)];
  }
  
  private final void ensureEnumerationSize(int paramInt)
  {
    if (fEnumeration.length == paramInt)
    {
      String[] arrayOfString = new String[paramInt * 2];
      System.arraycopy(fEnumeration, 0, arrayOfString, 0, paramInt);
      fEnumeration = arrayOfString;
    }
  }
  
  private void init()
  {
    fStartDTDCalled = false;
    fExtEntityDepth = 0;
    fIncludeSectDepth = 0;
    fMarkUpDepth = 0;
    fPEDepth = 0;
    fStandalone = false;
    fSeenExternalDTD = false;
    fSeenPEReferences = false;
    setScannerState(1);
  }
  
  private int skipInvalidEnumerationValue()
    throws IOException
  {
    int i;
    do
    {
      i = fEntityScanner.scanChar();
    } while ((i != 124) && (i != 41));
    ensureEnumerationSize(fEnumerationCount + 1);
    fEnumeration[(fEnumerationCount++)] = XMLSymbols.EMPTY_STRING;
    return i;
  }
}
