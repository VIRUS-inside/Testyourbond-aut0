package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDocumentFragmentScannerImpl
  extends XMLScanner
  implements XMLDocumentScanner, XMLComponent, XMLEntityHandler
{
  protected static final int SCANNER_STATE_START_OF_MARKUP = 1;
  protected static final int SCANNER_STATE_COMMENT = 2;
  protected static final int SCANNER_STATE_PI = 3;
  protected static final int SCANNER_STATE_DOCTYPE = 4;
  protected static final int SCANNER_STATE_ROOT_ELEMENT = 6;
  protected static final int SCANNER_STATE_CONTENT = 7;
  protected static final int SCANNER_STATE_REFERENCE = 8;
  protected static final int SCANNER_STATE_END_OF_INPUT = 13;
  protected static final int SCANNER_STATE_TERMINATED = 14;
  protected static final int SCANNER_STATE_CDATA = 15;
  protected static final int SCANNER_STATE_TEXT_DECL = 16;
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
  protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://apache.org/xml/features/scanner/notify-char-refs" };
  private static final Boolean[] FEATURE_DEFAULTS = { null, null, Boolean.FALSE, Boolean.FALSE };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/entity-resolver" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null };
  private static final boolean DEBUG_SCANNER_STATE = false;
  private static final boolean DEBUG_DISPATCHER = false;
  protected static final boolean DEBUG_CONTENT_SCANNING = false;
  protected XMLDocumentHandler fDocumentHandler;
  protected int[] fEntityStack = new int[4];
  protected int fMarkupDepth;
  protected int fScannerState;
  protected boolean fInScanContent = false;
  protected boolean fHasExternalDTD;
  protected boolean fStandalone;
  protected boolean fIsEntityDeclaredVC;
  protected ExternalSubsetResolver fExternalSubsetResolver;
  protected QName fCurrentElement;
  protected final ElementStack fElementStack = new ElementStack();
  protected boolean fNotifyBuiltInRefs = false;
  protected Dispatcher fDispatcher;
  protected final Dispatcher fContentDispatcher = createContentDispatcher();
  protected final QName fElementQName = new QName();
  protected final QName fAttributeQName = new QName();
  protected final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
  protected final XMLString fTempString = new XMLString();
  protected final XMLString fTempString2 = new XMLString();
  private final String[] fStrings = new String[3];
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
  private final QName fQName = new QName();
  private final char[] fSingleChar = new char[1];
  private boolean fSawSpace;
  private Augmentations fTempAugmentations = null;
  
  public XMLDocumentFragmentScannerImpl() {}
  
  public void setInputSource(XMLInputSource paramXMLInputSource)
    throws IOException
  {
    fEntityManager.setEntityHandler(this);
    fEntityManager.startEntity("$fragment$", paramXMLInputSource, false, true);
  }
  
  public boolean scanDocument(boolean paramBoolean)
    throws IOException, XNIException
  {
    fEntityScanner = fEntityManager.getEntityScanner();
    fEntityManager.setEntityHandler(this);
    do
    {
      if (!fDispatcher.dispatch(paramBoolean)) {
        return false;
      }
    } while (paramBoolean);
    return true;
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    super.reset(paramXMLComponentManager);
    fAttributes.setNamespaces(fNamespaces);
    fMarkupDepth = 0;
    fCurrentElement = null;
    fElementStack.clear();
    fHasExternalDTD = false;
    fStandalone = false;
    fIsEntityDeclaredVC = false;
    fInScanContent = false;
    setScannerState(7);
    setDispatcher(fContentDispatcher);
    if (fParserSettings)
    {
      try
      {
        fNotifyBuiltInRefs = paramXMLComponentManager.getFeature("http://apache.org/xml/features/scanner/notify-builtin-refs");
      }
      catch (XMLConfigurationException localXMLConfigurationException1)
      {
        fNotifyBuiltInRefs = false;
      }
      try
      {
        Object localObject = paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
        fExternalSubsetResolver = ((localObject instanceof ExternalSubsetResolver) ? (ExternalSubsetResolver)localObject : null);
      }
      catch (XMLConfigurationException localXMLConfigurationException2)
      {
        fExternalSubsetResolver = null;
      }
    }
  }
  
  public String[] getRecognizedFeatures()
  {
    return (String[])RECOGNIZED_FEATURES.clone();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    super.setFeature(paramString, paramBoolean);
    if (paramString.startsWith("http://apache.org/xml/features/"))
    {
      int i = paramString.length() - "http://apache.org/xml/features/".length();
      if ((i == "scanner/notify-builtin-refs".length()) && (paramString.endsWith("scanner/notify-builtin-refs"))) {
        fNotifyBuiltInRefs = paramBoolean;
      }
    }
  }
  
  public String[] getRecognizedProperties()
  {
    return (String[])RECOGNIZED_PROPERTIES.clone();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    super.setProperty(paramString, paramObject);
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      int i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/entity-manager".length()) && (paramString.endsWith("internal/entity-manager")))
      {
        fEntityManager = ((XMLEntityManager)paramObject);
        return;
      }
      if ((i == "internal/entity-resolver".length()) && (paramString.endsWith("internal/entity-resolver")))
      {
        fExternalSubsetResolver = ((paramObject instanceof ExternalSubsetResolver) ? (ExternalSubsetResolver)paramObject : null);
        return;
      }
    }
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
  
  public void setDocumentHandler(XMLDocumentHandler paramXMLDocumentHandler)
  {
    fDocumentHandler = paramXMLDocumentHandler;
  }
  
  public XMLDocumentHandler getDocumentHandler()
  {
    return fDocumentHandler;
  }
  
  public void startEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fEntityDepth == fEntityStack.length)
    {
      int[] arrayOfInt = new int[fEntityStack.length * 2];
      System.arraycopy(fEntityStack, 0, arrayOfInt, 0, fEntityStack.length);
      fEntityStack = arrayOfInt;
    }
    fEntityStack[fEntityDepth] = fMarkupDepth;
    super.startEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    if ((fStandalone) && (fEntityManager.isEntityDeclInExternalSubset(paramString1))) {
      reportFatalError("MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { paramString1 });
    }
    if ((fDocumentHandler != null) && (!fScanningAttribute) && (!paramString1.equals("[xml]"))) {
      fDocumentHandler.startGeneralEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void endEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fInScanContent) && (fStringBuffer.length != 0) && (fDocumentHandler != null))
    {
      fDocumentHandler.characters(fStringBuffer, null);
      fStringBuffer.length = 0;
    }
    super.endEntity(paramString, paramAugmentations);
    if (fMarkupDepth != fEntityStack[fEntityDepth]) {
      reportFatalError("MarkupEntityMismatch", null);
    }
    if ((fDocumentHandler != null) && (!fScanningAttribute) && (!paramString.equals("[xml]"))) {
      fDocumentHandler.endGeneralEntity(paramString, paramAugmentations);
    }
  }
  
  protected Dispatcher createContentDispatcher()
  {
    return new FragmentContentDispatcher();
  }
  
  protected void scanXMLDeclOrTextDecl(boolean paramBoolean)
    throws IOException, XNIException
  {
    super.scanXMLDeclOrTextDecl(paramBoolean, fStrings);
    fMarkupDepth -= 1;
    String str1 = fStrings[0];
    String str2 = fStrings[1];
    String str3 = fStrings[2];
    fStandalone = ((str3 != null) && (str3.equals("yes")));
    fEntityManager.setStandalone(fStandalone);
    fEntityScanner.setXMLVersion(str1);
    if (fDocumentHandler != null) {
      if (paramBoolean) {
        fDocumentHandler.textDecl(str1, str2, null);
      } else {
        fDocumentHandler.xmlDecl(str1, str2, str3, null);
      }
    }
    if ((str2 != null) && (!fEntityScanner.fCurrentEntity.isEncodingExternallySpecified())) {
      fEntityScanner.setEncoding(str2);
    }
  }
  
  protected void scanPIData(String paramString, XMLString paramXMLString)
    throws IOException, XNIException
  {
    super.scanPIData(paramString, paramXMLString);
    fMarkupDepth -= 1;
    if (fDocumentHandler != null) {
      fDocumentHandler.processingInstruction(paramString, paramXMLString, null);
    }
  }
  
  protected void scanComment()
    throws IOException, XNIException
  {
    scanComment(fStringBuffer);
    fMarkupDepth -= 1;
    if (fDocumentHandler != null) {
      fDocumentHandler.comment(fStringBuffer, null);
    }
  }
  
  protected boolean scanStartElement()
    throws IOException, XNIException
  {
    if (fNamespaces)
    {
      fEntityScanner.scanQName(fElementQName);
    }
    else
    {
      str = fEntityScanner.scanName();
      fElementQName.setValues(null, str, str, null);
    }
    String str = fElementQName.rawname;
    fCurrentElement = fElementStack.pushElement(fElementQName);
    boolean bool1 = false;
    fAttributes.removeAllAttributes();
    for (;;)
    {
      boolean bool2 = fEntityScanner.skipSpaces();
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
          reportFatalError("ElementUnterminated", new Object[] { str });
        }
        bool1 = true;
        break;
      }
      if (((!isValidNameStartChar(i)) || (!bool2)) && ((!isValidNameStartHighSurrogate(i)) || (!bool2))) {
        reportFatalError("ElementUnterminated", new Object[] { str });
      }
      scanAttribute(fAttributes);
    }
    if (fDocumentHandler != null) {
      if (bool1)
      {
        fMarkupDepth -= 1;
        if (fMarkupDepth < fEntityStack[(fEntityDepth - 1)]) {
          reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
        }
        fDocumentHandler.emptyElement(fElementQName, fAttributes, null);
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
    if (fNamespaces)
    {
      fEntityScanner.scanQName(fElementQName);
    }
    else
    {
      String str = fEntityScanner.scanName();
      fElementQName.setValues(null, str, str, null);
    }
    fSawSpace = fEntityScanner.skipSpaces();
  }
  
  protected boolean scanStartElementAfterName()
    throws IOException, XNIException
  {
    String str = fElementQName.rawname;
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
          reportFatalError("ElementUnterminated", new Object[] { str });
        }
        bool = true;
        break;
      }
      if (((!isValidNameStartChar(i)) || (!fSawSpace)) && ((!isValidNameStartHighSurrogate(i)) || (!fSawSpace))) {
        reportFatalError("ElementUnterminated", new Object[] { str });
      }
      scanAttribute(fAttributes);
      fSawSpace = fEntityScanner.skipSpaces();
    }
    if (fDocumentHandler != null) {
      if (bool)
      {
        fMarkupDepth -= 1;
        if (fMarkupDepth < fEntityStack[(fEntityDepth - 1)]) {
          reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
        }
        fDocumentHandler.emptyElement(fElementQName, fAttributes, null);
        fElementStack.popElement(fElementQName);
      }
      else
      {
        fDocumentHandler.startElement(fElementQName, fAttributes, null);
      }
    }
    return bool;
  }
  
  protected void scanAttribute(XMLAttributes paramXMLAttributes)
    throws IOException, XNIException
  {
    if (fNamespaces)
    {
      fEntityScanner.scanQName(fAttributeQName);
    }
    else
    {
      String str = fEntityScanner.scanName();
      fAttributeQName.setValues(null, str, str, null);
    }
    fEntityScanner.skipSpaces();
    if (!fEntityScanner.skipChar(61)) {
      reportFatalError("EqRequiredInAttribute", new Object[] { fCurrentElement.rawname, fAttributeQName.rawname });
    }
    fEntityScanner.skipSpaces();
    int i = paramXMLAttributes.getLength();
    int j = paramXMLAttributes.addAttribute(fAttributeQName, XMLSymbols.fCDATASymbol, null);
    if (i == paramXMLAttributes.getLength()) {
      reportFatalError("AttributeNotUnique", new Object[] { fCurrentElement.rawname, fAttributeQName.rawname });
    }
    boolean bool = scanAttributeValue(fTempString, fTempString2, fAttributeQName.rawname, fIsEntityDeclaredVC, fCurrentElement.rawname);
    paramXMLAttributes.setValue(j, fTempString.toString());
    if (!bool) {
      paramXMLAttributes.setNonNormalizedValue(j, fTempString2.toString());
    }
    paramXMLAttributes.setSpecified(j, true);
  }
  
  protected int scanContent()
    throws IOException, XNIException
  {
    Object localObject = fTempString;
    int i = fEntityScanner.scanContent((XMLString)localObject);
    if (i == 13)
    {
      fEntityScanner.scanChar();
      fStringBuffer.clear();
      fStringBuffer.append(fTempString);
      fStringBuffer.append((char)i);
      localObject = fStringBuffer;
      i = -1;
    }
    if ((fDocumentHandler != null) && (length > 0)) {
      fDocumentHandler.characters((XMLString)localObject, null);
    }
    if ((i == 93) && (fTempString.length == 0))
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
  
  protected boolean scanCDATASection(boolean paramBoolean)
    throws IOException, XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.startCDATA(null);
    }
    for (;;)
    {
      fStringBuffer.clear();
      int i;
      if (!fEntityScanner.scanData("]]", fStringBuffer))
      {
        if ((fDocumentHandler != null) && (fStringBuffer.length > 0)) {
          fDocumentHandler.characters(fStringBuffer, null);
        }
        for (i = 0; fEntityScanner.skipChar(93); i++) {}
        if ((fDocumentHandler != null) && (i > 0))
        {
          fStringBuffer.clear();
          int j;
          if (i > 2048)
          {
            j = i / 2048;
            int k = i % 2048;
            for (int m = 0; m < 2048; m++) {
              fStringBuffer.append(']');
            }
            for (int n = 0; n < j; n++) {
              fDocumentHandler.characters(fStringBuffer, null);
            }
            if (k != 0)
            {
              fStringBuffer.length = k;
              fDocumentHandler.characters(fStringBuffer, null);
            }
          }
          else
          {
            for (j = 0; j < i; j++) {
              fStringBuffer.append(']');
            }
            fDocumentHandler.characters(fStringBuffer, null);
          }
        }
        if (fEntityScanner.skipChar(62)) {
          break;
        }
        if (fDocumentHandler != null)
        {
          fStringBuffer.clear();
          fStringBuffer.append("]]");
          fDocumentHandler.characters(fStringBuffer, null);
        }
      }
      else
      {
        if (fDocumentHandler != null) {
          fDocumentHandler.characters(fStringBuffer, null);
        }
        i = fEntityScanner.peekChar();
        if ((i != -1) && (isInvalidLiteral(i))) {
          if (XMLChar.isHighSurrogate(i))
          {
            fStringBuffer.clear();
            scanSurrogates(fStringBuffer);
            if (fDocumentHandler != null) {
              fDocumentHandler.characters(fStringBuffer, null);
            }
          }
          else
          {
            reportFatalError("InvalidCharInCDSect", new Object[] { Integer.toString(i, 16) });
            fEntityScanner.scanChar();
          }
        }
      }
    }
    fMarkupDepth -= 1;
    if (fDocumentHandler != null) {
      fDocumentHandler.endCDATA(null);
    }
    return true;
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
    if (fDocumentHandler != null) {
      fDocumentHandler.endElement(fElementQName, null);
    }
    return fMarkupDepth;
  }
  
  protected void scanCharReference()
    throws IOException, XNIException
  {
    fStringBuffer2.clear();
    int i = scanCharReferenceValue(fStringBuffer2, null);
    fMarkupDepth -= 1;
    if ((i != -1) && (fDocumentHandler != null))
    {
      if (fNotifyCharRefs) {
        fDocumentHandler.startGeneralEntity(fCharRefLiteral, null, null, null);
      }
      Augmentations localAugmentations = null;
      if ((fValidation) && (i <= 32))
      {
        if (fTempAugmentations != null) {
          fTempAugmentations.removeAllItems();
        } else {
          fTempAugmentations = new AugmentationsImpl();
        }
        localAugmentations = fTempAugmentations;
        localAugmentations.putItem("CHAR_REF_PROBABLE_WS", Boolean.TRUE);
      }
      fDocumentHandler.characters(fStringBuffer2, localAugmentations);
      if (fNotifyCharRefs) {
        fDocumentHandler.endGeneralEntity(fCharRefLiteral, null);
      }
    }
  }
  
  protected void scanEntityReference()
    throws IOException, XNIException
  {
    String str = fEntityScanner.scanName();
    if (str == null)
    {
      reportFatalError("NameRequiredInReference", null);
      return;
    }
    if (!fEntityScanner.skipChar(59)) {
      reportFatalError("SemicolonRequiredInReference", new Object[] { str });
    }
    fMarkupDepth -= 1;
    if (str == XMLScanner.fAmpSymbol)
    {
      handleCharacter('&', XMLScanner.fAmpSymbol);
    }
    else if (str == XMLScanner.fLtSymbol)
    {
      handleCharacter('<', XMLScanner.fLtSymbol);
    }
    else if (str == XMLScanner.fGtSymbol)
    {
      handleCharacter('>', XMLScanner.fGtSymbol);
    }
    else if (str == XMLScanner.fQuotSymbol)
    {
      handleCharacter('"', XMLScanner.fQuotSymbol);
    }
    else if (str == XMLScanner.fAposSymbol)
    {
      handleCharacter('\'', XMLScanner.fAposSymbol);
    }
    else if (fEntityManager.isUnparsedEntity(str))
    {
      reportFatalError("ReferenceToUnparsedEntity", new Object[] { str });
    }
    else
    {
      if (!fEntityManager.isDeclaredEntity(str)) {
        if (fIsEntityDeclaredVC)
        {
          if (fValidation) {
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { str }, (short)1);
          }
        }
        else {
          reportFatalError("EntityNotDeclared", new Object[] { str });
        }
      }
      fEntityManager.startEntity(str, false);
    }
  }
  
  private void handleCharacter(char paramChar, String paramString)
    throws XNIException
  {
    if (fDocumentHandler != null)
    {
      if (fNotifyBuiltInRefs) {
        fDocumentHandler.startGeneralEntity(paramString, null, null, null);
      }
      fSingleChar[0] = paramChar;
      fTempString.setValues(fSingleChar, 0, 1);
      fDocumentHandler.characters(fTempString, null);
      if (fNotifyBuiltInRefs) {
        fDocumentHandler.endGeneralEntity(paramString, null);
      }
    }
  }
  
  protected int handleEndElement(QName paramQName, boolean paramBoolean)
    throws XNIException
  {
    fMarkupDepth -= 1;
    if (fMarkupDepth < fEntityStack[(fEntityDepth - 1)]) {
      reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
    }
    QName localQName = fQName;
    fElementStack.popElement(localQName);
    if (rawname != rawname) {
      reportFatalError("ETagRequired", new Object[] { rawname });
    }
    if (fNamespaces) {
      uri = uri;
    }
    if ((fDocumentHandler != null) && (!paramBoolean)) {
      fDocumentHandler.endElement(paramQName, null);
    }
    return fMarkupDepth;
  }
  
  protected final void setScannerState(int paramInt)
  {
    fScannerState = paramInt;
  }
  
  protected final void setDispatcher(Dispatcher paramDispatcher)
  {
    fDispatcher = paramDispatcher;
  }
  
  protected String getScannerStateName(int paramInt)
  {
    switch (paramInt)
    {
    case 4: 
      return "SCANNER_STATE_DOCTYPE";
    case 6: 
      return "SCANNER_STATE_ROOT_ELEMENT";
    case 1: 
      return "SCANNER_STATE_START_OF_MARKUP";
    case 2: 
      return "SCANNER_STATE_COMMENT";
    case 3: 
      return "SCANNER_STATE_PI";
    case 7: 
      return "SCANNER_STATE_CONTENT";
    case 8: 
      return "SCANNER_STATE_REFERENCE";
    case 13: 
      return "SCANNER_STATE_END_OF_INPUT";
    case 14: 
      return "SCANNER_STATE_TERMINATED";
    case 15: 
      return "SCANNER_STATE_CDATA";
    case 16: 
      return "SCANNER_STATE_TEXT_DECL";
    }
    return "??? (" + paramInt + ')';
  }
  
  public String getDispatcherName(Dispatcher paramDispatcher)
  {
    return "null";
  }
  
  protected class FragmentContentDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher
  {
    protected FragmentContentDispatcher() {}
    
    public boolean dispatch(boolean paramBoolean)
      throws IOException, XNIException
    {
      try
      {
        int i;
        do
        {
          i = 0;
          switch (fScannerState)
          {
          case 7: 
            if (fEntityScanner.skipChar(60))
            {
              setScannerState(1);
              i = 1;
            }
            else if (fEntityScanner.skipChar(38))
            {
              setScannerState(8);
              i = 1;
            }
            else
            {
              do
              {
                int j = scanContent();
                if (j == 60)
                {
                  fEntityScanner.scanChar();
                  setScannerState(1);
                  break;
                }
                if (j == 38)
                {
                  fEntityScanner.scanChar();
                  setScannerState(8);
                  break;
                }
                if ((j != -1) && (isInvalidLiteral(j))) {
                  if (XMLChar.isHighSurrogate(j))
                  {
                    fStringBuffer.clear();
                    if ((scanSurrogates(fStringBuffer)) && (fDocumentHandler != null)) {
                      fDocumentHandler.characters(fStringBuffer, null);
                    }
                  }
                  else
                  {
                    reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(j, 16) });
                    fEntityScanner.scanChar();
                  }
                }
              } while (paramBoolean);
            }
            break;
          case 1: 
            fMarkupDepth += 1;
            if (fEntityScanner.skipChar(47))
            {
              if ((scanEndElement() == 0) && (elementDepthIsZeroHook())) {
                return true;
              }
              setScannerState(7);
            }
            else if (isValidNameStartChar(fEntityScanner.peekChar()))
            {
              scanStartElement();
              setScannerState(7);
            }
            else if (fEntityScanner.skipChar(33))
            {
              if (fEntityScanner.skipChar(45))
              {
                if (!fEntityScanner.skipChar(45)) {
                  reportFatalError("InvalidCommentStart", null);
                }
                setScannerState(2);
                i = 1;
              }
              else if (fEntityScanner.skipString("[CDATA["))
              {
                setScannerState(15);
                i = 1;
              }
              else if (!scanForDoctypeHook())
              {
                reportFatalError("MarkupNotRecognizedInContent", null);
              }
            }
            else if (fEntityScanner.skipChar(63))
            {
              setScannerState(3);
              i = 1;
            }
            else if (isValidNameStartHighSurrogate(fEntityScanner.peekChar()))
            {
              scanStartElement();
              setScannerState(7);
            }
            else
            {
              reportFatalError("MarkupNotRecognizedInContent", null);
              setScannerState(7);
            }
            break;
          case 2: 
            scanComment();
            setScannerState(7);
            break;
          case 3: 
            scanPI();
            setScannerState(7);
            break;
          case 15: 
            scanCDATASection(paramBoolean);
            setScannerState(7);
            break;
          case 8: 
            fMarkupDepth += 1;
            setScannerState(7);
            if (fEntityScanner.skipChar(35)) {
              scanCharReference();
            } else {
              scanEntityReference();
            }
            break;
          case 16: 
            if (fEntityScanner.skipString("<?xml"))
            {
              fMarkupDepth += 1;
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
                String str = fSymbolTable.addSymbol(fStringBuffer.ch, fStringBuffer.offset, fStringBuffer.length);
                scanPIData(str, fTempString);
              }
              else
              {
                scanXMLDeclOrTextDecl(true);
              }
            }
            fEntityManager.fCurrentEntity.mayReadChunks = true;
            setScannerState(7);
            break;
          case 6: 
            if (scanRootElementHook()) {
              return true;
            }
            setScannerState(7);
            break;
          case 4: 
            reportFatalError("DoctypeIllegalInContent", null);
            setScannerState(7);
          }
        } while ((paramBoolean) || (i != 0));
      }
      catch (MalformedByteSequenceException localMalformedByteSequenceException)
      {
        fErrorReporter.reportError(localMalformedByteSequenceException.getDomain(), localMalformedByteSequenceException.getKey(), localMalformedByteSequenceException.getArguments(), (short)2, localMalformedByteSequenceException);
        return false;
      }
      catch (CharConversionException localCharConversionException)
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, (short)2, localCharConversionException);
        return false;
      }
      catch (EOFException localEOFException)
      {
        endOfFileHook(localEOFException);
        return false;
      }
      return true;
    }
    
    protected boolean scanForDoctypeHook()
      throws IOException, XNIException
    {
      return false;
    }
    
    protected boolean elementDepthIsZeroHook()
      throws IOException, XNIException
    {
      return false;
    }
    
    protected boolean scanRootElementHook()
      throws IOException, XNIException
    {
      return false;
    }
    
    protected void endOfFileHook(EOFException paramEOFException)
      throws IOException, XNIException
    {
      if (fMarkupDepth != 0) {
        reportFatalError("PrematureEOF", null);
      }
    }
  }
  
  protected static abstract interface Dispatcher
  {
    public abstract boolean dispatch(boolean paramBoolean)
      throws IOException, XNIException;
  }
  
  protected static class ElementStack
  {
    protected QName[] fElements = new QName[10];
    protected int fSize;
    
    public ElementStack()
    {
      for (int i = 0; i < fElements.length; i++) {
        fElements[i] = new QName();
      }
    }
    
    public QName pushElement(QName paramQName)
    {
      if (fSize == fElements.length)
      {
        QName[] arrayOfQName = new QName[fElements.length * 2];
        System.arraycopy(fElements, 0, arrayOfQName, 0, fSize);
        fElements = arrayOfQName;
        for (int i = fSize; i < fElements.length; i++) {
          fElements[i] = new QName();
        }
      }
      fElements[fSize].setValues(paramQName);
      return fElements[(fSize++)];
    }
    
    public void popElement(QName paramQName)
    {
      paramQName.setValues(fElements[(--fSize)]);
    }
    
    public void clear()
    {
      fSize = 0;
    }
  }
}
