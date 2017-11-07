package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDocumentScannerImpl
  extends XMLDocumentFragmentScannerImpl
{
  protected static final int SCANNER_STATE_XML_DECL = 0;
  protected static final int SCANNER_STATE_PROLOG = 5;
  protected static final int SCANNER_STATE_TRAILING_MISC = 12;
  protected static final int SCANNER_STATE_DTD_INTERNAL_DECLS = 17;
  protected static final int SCANNER_STATE_DTD_EXTERNAL = 18;
  protected static final int SCANNER_STATE_DTD_EXTERNAL_DECLS = 19;
  protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
  protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
  protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
  protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
  private static final String[] RECOGNIZED_FEATURES = { "http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://apache.org/xml/features/disallow-doctype-decl" };
  private static final Boolean[] FEATURE_DEFAULTS = { Boolean.TRUE, Boolean.FALSE };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/internal/namespace-context" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null };
  protected XMLDTDScanner fDTDScanner;
  protected ValidationManager fValidationManager;
  protected boolean fScanningDTD;
  protected String fDoctypeName;
  protected String fDoctypePublicId;
  protected String fDoctypeSystemId;
  protected NamespaceContext fNamespaceContext = new NamespaceSupport();
  protected boolean fLoadExternalDTD = true;
  protected boolean fDisallowDoctype = false;
  protected boolean fSeenDoctypeDecl;
  protected final XMLDocumentFragmentScannerImpl.Dispatcher fXMLDeclDispatcher = new XMLDeclDispatcher();
  protected final XMLDocumentFragmentScannerImpl.Dispatcher fPrologDispatcher = new PrologDispatcher();
  protected final XMLDocumentFragmentScannerImpl.Dispatcher fDTDDispatcher = new DTDDispatcher();
  protected final XMLDocumentFragmentScannerImpl.Dispatcher fTrailingMiscDispatcher = new TrailingMiscDispatcher();
  private final String[] fStrings = new String[3];
  private final XMLString fString = new XMLString();
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  private XMLInputSource fExternalSubsetSource = null;
  private final XMLDTDDescription fDTDDescription = new XMLDTDDescription(null, null, null, null, null);
  
  public XMLDocumentScannerImpl() {}
  
  public void setInputSource(XMLInputSource paramXMLInputSource)
    throws IOException
  {
    fEntityManager.setEntityHandler(this);
    fEntityManager.startDocumentEntity(paramXMLInputSource);
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    super.reset(paramXMLComponentManager);
    fDoctypeName = null;
    fDoctypePublicId = null;
    fDoctypeSystemId = null;
    fSeenDoctypeDecl = false;
    fScanningDTD = false;
    fExternalSubsetSource = null;
    if (!fParserSettings)
    {
      fNamespaceContext.reset();
      setScannerState(0);
      setDispatcher(fXMLDeclDispatcher);
      return;
    }
    try
    {
      fLoadExternalDTD = paramXMLComponentManager.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd");
    }
    catch (XMLConfigurationException localXMLConfigurationException1)
    {
      fLoadExternalDTD = true;
    }
    try
    {
      fDisallowDoctype = paramXMLComponentManager.getFeature("http://apache.org/xml/features/disallow-doctype-decl");
    }
    catch (XMLConfigurationException localXMLConfigurationException2)
    {
      fDisallowDoctype = false;
    }
    fDTDScanner = ((XMLDTDScanner)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/dtd-scanner"));
    try
    {
      fValidationManager = ((ValidationManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fValidationManager = null;
    }
    try
    {
      fNamespaceContext = ((NamespaceContext)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context"));
    }
    catch (XMLConfigurationException localXMLConfigurationException4) {}
    if (fNamespaceContext == null) {
      fNamespaceContext = new NamespaceSupport();
    }
    fNamespaceContext.reset();
    setScannerState(0);
    setDispatcher(fXMLDeclDispatcher);
  }
  
  public String[] getRecognizedFeatures()
  {
    String[] arrayOfString1 = super.getRecognizedFeatures();
    int i = arrayOfString1 != null ? arrayOfString1.length : 0;
    String[] arrayOfString2 = new String[i + RECOGNIZED_FEATURES.length];
    if (arrayOfString1 != null) {
      System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, arrayOfString1.length);
    }
    System.arraycopy(RECOGNIZED_FEATURES, 0, arrayOfString2, i, RECOGNIZED_FEATURES.length);
    return arrayOfString2;
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    super.setFeature(paramString, paramBoolean);
    if (paramString.startsWith("http://apache.org/xml/features/"))
    {
      int i = paramString.length() - "http://apache.org/xml/features/".length();
      if ((i == "nonvalidating/load-external-dtd".length()) && (paramString.endsWith("nonvalidating/load-external-dtd")))
      {
        fLoadExternalDTD = paramBoolean;
        return;
      }
      if ((i == "disallow-doctype-decl".length()) && (paramString.endsWith("disallow-doctype-decl")))
      {
        fDisallowDoctype = paramBoolean;
        return;
      }
    }
  }
  
  public String[] getRecognizedProperties()
  {
    String[] arrayOfString1 = super.getRecognizedProperties();
    int i = arrayOfString1 != null ? arrayOfString1.length : 0;
    String[] arrayOfString2 = new String[i + RECOGNIZED_PROPERTIES.length];
    if (arrayOfString1 != null) {
      System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, arrayOfString1.length);
    }
    System.arraycopy(RECOGNIZED_PROPERTIES, 0, arrayOfString2, i, RECOGNIZED_PROPERTIES.length);
    return arrayOfString2;
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    super.setProperty(paramString, paramObject);
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      int i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/dtd-scanner".length()) && (paramString.endsWith("internal/dtd-scanner"))) {
        fDTDScanner = ((XMLDTDScanner)paramObject);
      }
      if ((i == "internal/namespace-context".length()) && (paramString.endsWith("internal/namespace-context")) && (paramObject != null)) {
        fNamespaceContext = ((NamespaceContext)paramObject);
      }
      return;
    }
  }
  
  public Boolean getFeatureDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
      if (RECOGNIZED_FEATURES[i].equals(paramString)) {
        return FEATURE_DEFAULTS[i];
      }
    }
    return super.getFeatureDefault(paramString);
  }
  
  public Object getPropertyDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
      if (RECOGNIZED_PROPERTIES[i].equals(paramString)) {
        return PROPERTY_DEFAULTS[i];
      }
    }
    return super.getPropertyDefault(paramString);
  }
  
  public void startEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    super.startEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    if ((!paramString1.equals("[xml]")) && (fEntityScanner.isExternal())) {
      setScannerState(16);
    }
    if ((fDocumentHandler != null) && (paramString1.equals("[xml]"))) {
      fDocumentHandler.startDocument(fEntityScanner, paramString2, fNamespaceContext, null);
    }
  }
  
  public void endEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    super.endEntity(paramString, paramAugmentations);
    if ((fDocumentHandler != null) && (paramString.equals("[xml]"))) {
      fDocumentHandler.endDocument(null);
    }
  }
  
  protected XMLDocumentFragmentScannerImpl.Dispatcher createContentDispatcher()
  {
    return new ContentDispatcher();
  }
  
  protected boolean scanDoctypeDecl()
    throws IOException, XNIException
  {
    if (!fEntityScanner.skipSpaces()) {
      reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ROOT_ELEMENT_TYPE_IN_DOCTYPEDECL", null);
    }
    fDoctypeName = fEntityScanner.scanName();
    if (fDoctypeName == null) {
      reportFatalError("MSG_ROOT_ELEMENT_TYPE_REQUIRED", null);
    }
    if (fEntityScanner.skipSpaces())
    {
      scanExternalID(fStrings, false);
      fDoctypeSystemId = fStrings[0];
      fDoctypePublicId = fStrings[1];
      fEntityScanner.skipSpaces();
    }
    fHasExternalDTD = (fDoctypeSystemId != null);
    if ((!fHasExternalDTD) && (fExternalSubsetResolver != null))
    {
      fDTDDescription.setValues(null, null, fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
      fDTDDescription.setRootName(fDoctypeName);
      fExternalSubsetSource = fExternalSubsetResolver.getExternalSubset(fDTDDescription);
      fHasExternalDTD = (fExternalSubsetSource != null);
    }
    if (fDocumentHandler != null) {
      if (fExternalSubsetSource == null) {
        fDocumentHandler.doctypeDecl(fDoctypeName, fDoctypePublicId, fDoctypeSystemId, null);
      } else {
        fDocumentHandler.doctypeDecl(fDoctypeName, fExternalSubsetSource.getPublicId(), fExternalSubsetSource.getSystemId(), null);
      }
    }
    boolean bool = true;
    if (!fEntityScanner.skipChar(91))
    {
      bool = false;
      fEntityScanner.skipSpaces();
      if (!fEntityScanner.skipChar(62)) {
        reportFatalError("DoctypedeclUnterminated", new Object[] { fDoctypeName });
      }
      fMarkupDepth -= 1;
    }
    return bool;
  }
  
  protected String getScannerStateName(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return "SCANNER_STATE_XML_DECL";
    case 5: 
      return "SCANNER_STATE_PROLOG";
    case 12: 
      return "SCANNER_STATE_TRAILING_MISC";
    case 17: 
      return "SCANNER_STATE_DTD_INTERNAL_DECLS";
    case 18: 
      return "SCANNER_STATE_DTD_EXTERNAL";
    case 19: 
      return "SCANNER_STATE_DTD_EXTERNAL_DECLS";
    }
    return super.getScannerStateName(paramInt);
  }
  
  protected final class TrailingMiscDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher
  {
    protected TrailingMiscDispatcher() {}
    
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
          case 12: 
            fEntityScanner.skipSpaces();
            if (fEntityScanner.skipChar(60))
            {
              setScannerState(1);
              i = 1;
            }
            else
            {
              setScannerState(7);
              i = 1;
            }
            break;
          case 1: 
            fMarkupDepth += 1;
            if (fEntityScanner.skipChar(63))
            {
              setScannerState(3);
              i = 1;
            }
            else if (fEntityScanner.skipChar(33))
            {
              setScannerState(2);
              i = 1;
            }
            else if (fEntityScanner.skipChar(47))
            {
              reportFatalError("MarkupNotRecognizedInMisc", null);
              i = 1;
            }
            else if (isValidNameStartChar(fEntityScanner.peekChar()))
            {
              reportFatalError("MarkupNotRecognizedInMisc", null);
              scanStartElement();
              setScannerState(7);
            }
            else if (isValidNameStartHighSurrogate(fEntityScanner.peekChar()))
            {
              reportFatalError("MarkupNotRecognizedInMisc", null);
              scanStartElement();
              setScannerState(7);
            }
            else
            {
              reportFatalError("MarkupNotRecognizedInMisc", null);
            }
            break;
          case 3: 
            scanPI();
            setScannerState(12);
            break;
          case 2: 
            if (!fEntityScanner.skipString("--")) {
              reportFatalError("InvalidCommentStart", null);
            }
            scanComment();
            setScannerState(12);
            break;
          case 7: 
            int j = fEntityScanner.peekChar();
            if (j == -1)
            {
              setScannerState(14);
              return false;
            }
            reportFatalError("ContentIllegalInTrailingMisc", null);
            fEntityScanner.scanChar();
            setScannerState(12);
            break;
          case 8: 
            reportFatalError("ReferenceIllegalInTrailingMisc", null);
            setScannerState(12);
            break;
          case 14: 
            return false;
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
        if (fMarkupDepth != 0)
        {
          reportFatalError("PrematureEOF", null);
          return false;
        }
        setScannerState(14);
        return false;
      }
      return true;
    }
  }
  
  protected class ContentDispatcher
    extends XMLDocumentFragmentScannerImpl.FragmentContentDispatcher
  {
    protected ContentDispatcher()
    {
      super();
    }
    
    protected boolean scanForDoctypeHook()
      throws IOException, XNIException
    {
      if (fEntityScanner.skipString("DOCTYPE"))
      {
        setScannerState(4);
        return true;
      }
      return false;
    }
    
    protected boolean elementDepthIsZeroHook()
      throws IOException, XNIException
    {
      setScannerState(12);
      setDispatcher(fTrailingMiscDispatcher);
      return true;
    }
    
    protected boolean scanRootElementHook()
      throws IOException, XNIException
    {
      if ((fExternalSubsetResolver != null) && (!fSeenDoctypeDecl) && (!fDisallowDoctype) && ((fValidation) || (fLoadExternalDTD)))
      {
        scanStartElementName();
        resolveExternalSubsetAndRead();
        if (scanStartElementAfterName())
        {
          setScannerState(12);
          setDispatcher(fTrailingMiscDispatcher);
          return true;
        }
      }
      else if (scanStartElement())
      {
        setScannerState(12);
        setDispatcher(fTrailingMiscDispatcher);
        return true;
      }
      return false;
    }
    
    protected void endOfFileHook(EOFException paramEOFException)
      throws IOException, XNIException
    {
      reportFatalError("PrematureEOF", null);
    }
    
    protected void resolveExternalSubsetAndRead()
      throws IOException, XNIException
    {
      fDTDDescription.setValues(null, null, fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
      fDTDDescription.setRootName(fElementQName.rawname);
      XMLInputSource localXMLInputSource = fExternalSubsetResolver.getExternalSubset(fDTDDescription);
      if (localXMLInputSource != null)
      {
        fDoctypeName = fElementQName.rawname;
        fDoctypePublicId = localXMLInputSource.getPublicId();
        fDoctypeSystemId = localXMLInputSource.getSystemId();
        if (fDocumentHandler != null) {
          fDocumentHandler.doctypeDecl(fDoctypeName, fDoctypePublicId, fDoctypeSystemId, null);
        }
        try
        {
          if ((fValidationManager == null) || (!fValidationManager.isCachedDTD()))
          {
            fDTDScanner.setInputSource(localXMLInputSource);
            while (fDTDScanner.scanDTDExternalSubset(true)) {}
          }
          else
          {
            fDTDScanner.setInputSource(null);
          }
        }
        finally
        {
          fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
        }
      }
    }
  }
  
  protected final class DTDDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher
  {
    protected DTDDispatcher() {}
    
    public boolean dispatch(boolean paramBoolean)
      throws IOException, XNIException
    {
      fEntityManager.setEntityHandler(null);
      try
      {
        int i;
        do
        {
          i = 0;
          switch (fScannerState)
          {
          case 17: 
            boolean bool1 = true;
            bool3 = ((fValidation) || (fLoadExternalDTD)) && ((fValidationManager == null) || (!fValidationManager.isCachedDTD()));
            bool4 = fDTDScanner.scanDTDInternalSubset(bool1, fStandalone, (fHasExternalDTD) && (bool3));
            if (!bool4)
            {
              if (!fEntityScanner.skipChar(93)) {
                reportFatalError("EXPECTED_SQUARE_BRACKET_TO_CLOSE_INTERNAL_SUBSET", null);
              }
              fEntityScanner.skipSpaces();
              if (!fEntityScanner.skipChar(62)) {
                reportFatalError("DoctypedeclUnterminated", new Object[] { fDoctypeName });
              }
              fMarkupDepth -= 1;
              if (fDoctypeSystemId != null)
              {
                fIsEntityDeclaredVC = (!fStandalone);
                if (bool3)
                {
                  setScannerState(18);
                  continue;
                }
              }
              else if (fExternalSubsetSource != null)
              {
                fIsEntityDeclaredVC = (!fStandalone);
                if (bool3)
                {
                  fDTDScanner.setInputSource(fExternalSubsetSource);
                  fExternalSubsetSource = null;
                  setScannerState(19);
                  continue;
                }
              }
              else
              {
                fIsEntityDeclaredVC = ((fEntityManager.hasPEReferences()) && (!fStandalone));
              }
              setScannerState(5);
              setDispatcher(fPrologDispatcher);
              fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
              boolean bool5 = true;
              return bool5;
            }
            break;
          case 18: 
            fDTDDescription.setValues(fDoctypePublicId, fDoctypeSystemId, null, null);
            fDTDDescription.setRootName(fDoctypeName);
            XMLInputSource localXMLInputSource = fEntityManager.resolveEntity(fDTDDescription);
            fDTDScanner.setInputSource(localXMLInputSource);
            setScannerState(19);
            i = 1;
            break;
          case 19: 
            bool2 = true;
            bool3 = fDTDScanner.scanDTDExternalSubset(bool2);
            if (!bool3)
            {
              setScannerState(5);
              setDispatcher(fPrologDispatcher);
              fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
              bool4 = true;
              return bool4;
            }
            break;
          default: 
            throw new XNIException("DTDDispatcher#dispatch: scanner state=" + fScannerState + " (" + getScannerStateName(fScannerState) + ')');
          }
        } while ((paramBoolean) || (i != 0));
      }
      catch (MalformedByteSequenceException localMalformedByteSequenceException)
      {
        fErrorReporter.reportError(localMalformedByteSequenceException.getDomain(), localMalformedByteSequenceException.getKey(), localMalformedByteSequenceException.getArguments(), (short)2, localMalformedByteSequenceException);
        boolean bool2 = false;
        return bool2;
      }
      catch (CharConversionException localCharConversionException)
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, (short)2, localCharConversionException);
        boolean bool3 = false;
        return bool3;
      }
      catch (EOFException localEOFException)
      {
        reportFatalError("PrematureEOF", null);
        boolean bool4 = false;
        return bool4;
      }
      finally
      {
        fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
      }
      return true;
    }
  }
  
  protected final class PrologDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher
  {
    protected PrologDispatcher() {}
    
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
          case 5: 
            fEntityScanner.skipSpaces();
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
              setScannerState(7);
              i = 1;
            }
            break;
          case 1: 
            fMarkupDepth += 1;
            if (fEntityScanner.skipChar(33))
            {
              if (fEntityScanner.skipChar(45))
              {
                if (!fEntityScanner.skipChar(45)) {
                  reportFatalError("InvalidCommentStart", null);
                }
                setScannerState(2);
                i = 1;
              }
              else if (fEntityScanner.skipString("DOCTYPE"))
              {
                setScannerState(4);
                i = 1;
              }
              else
              {
                reportFatalError("MarkupNotRecognizedInProlog", null);
              }
            }
            else
            {
              if (isValidNameStartChar(fEntityScanner.peekChar()))
              {
                setScannerState(6);
                setDispatcher(fContentDispatcher);
                return true;
              }
              if (fEntityScanner.skipChar(63))
              {
                setScannerState(3);
                i = 1;
              }
              else
              {
                if (isValidNameStartHighSurrogate(fEntityScanner.peekChar()))
                {
                  setScannerState(6);
                  setDispatcher(fContentDispatcher);
                  return true;
                }
                reportFatalError("MarkupNotRecognizedInProlog", null);
              }
            }
            break;
          case 2: 
            scanComment();
            setScannerState(5);
            break;
          case 3: 
            scanPI();
            setScannerState(5);
            break;
          case 4: 
            if (fDisallowDoctype) {
              reportFatalError("DoctypeNotAllowed", null);
            }
            if (fSeenDoctypeDecl) {
              reportFatalError("AlreadySeenDoctype", null);
            }
            fSeenDoctypeDecl = true;
            if (scanDoctypeDecl())
            {
              setScannerState(17);
              setDispatcher(fDTDDispatcher);
              return true;
            }
            if (fDoctypeSystemId != null)
            {
              fIsEntityDeclaredVC = (!fStandalone);
              if (((fValidation) || (fLoadExternalDTD)) && ((fValidationManager == null) || (!fValidationManager.isCachedDTD())))
              {
                setScannerState(18);
                setDispatcher(fDTDDispatcher);
                return true;
              }
            }
            else if (fExternalSubsetSource != null)
            {
              fIsEntityDeclaredVC = (!fStandalone);
              if (((fValidation) || (fLoadExternalDTD)) && ((fValidationManager == null) || (!fValidationManager.isCachedDTD())))
              {
                fDTDScanner.setInputSource(fExternalSubsetSource);
                fExternalSubsetSource = null;
                setScannerState(19);
                setDispatcher(fDTDDispatcher);
                return true;
              }
            }
            fDTDScanner.setInputSource(null);
            setScannerState(5);
            break;
          case 7: 
            reportFatalError("ContentIllegalInProlog", null);
            fEntityScanner.scanChar();
          case 8: 
            reportFatalError("ReferenceIllegalInProlog", null);
          }
        } while ((paramBoolean) || (i != 0));
        if (paramBoolean)
        {
          if (fEntityScanner.scanChar() != 60) {
            reportFatalError("RootElementRequired", null);
          }
          setScannerState(6);
          setDispatcher(fContentDispatcher);
        }
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
        reportFatalError("PrematureEOF", null);
        return false;
      }
      return true;
    }
  }
  
  protected final class XMLDeclDispatcher
    implements XMLDocumentFragmentScannerImpl.Dispatcher
  {
    protected XMLDeclDispatcher() {}
    
    public boolean dispatch(boolean paramBoolean)
      throws IOException, XNIException
    {
      setScannerState(5);
      setDispatcher(fPrologDispatcher);
      try
      {
        if (fEntityScanner.skipString("<?xml"))
        {
          fMarkupDepth += 1;
          if (XMLChar.isName(fEntityScanner.peekChar()))
          {
            fStringBuffer.clear();
            fStringBuffer.append("xml");
            if (fNamespaces) {
              while (XMLChar.isNCName(fEntityScanner.peekChar())) {
                fStringBuffer.append((char)fEntityScanner.scanChar());
              }
            } else {
              while (XMLChar.isName(fEntityScanner.peekChar())) {
                fStringBuffer.append((char)fEntityScanner.scanChar());
              }
            }
            String str = fSymbolTable.addSymbol(fStringBuffer.ch, fStringBuffer.offset, fStringBuffer.length);
            scanPIData(str, fString);
          }
          else
          {
            scanXMLDeclOrTextDecl(false);
          }
        }
        fEntityManager.fCurrentEntity.mayReadChunks = true;
        return true;
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
        reportFatalError("PrematureEOF", null);
      }
      return false;
    }
  }
}
