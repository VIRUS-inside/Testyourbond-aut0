package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.util.AttributesProxy;
import org.apache.xerces.util.SAXLocatorWrapper;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.ItemPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;

final class ValidatorHandlerImpl
  extends ValidatorHandler
  implements DTDHandler, EntityState, PSVIProvider, ValidatorHelper, XMLDocumentHandler
{
  private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
  private static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
  private static final String STRINGS_INTERNED = "http://apache.org/xml/features/internal/strings-interned";
  private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  private static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
  private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
  private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
  private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  private final XMLErrorReporter fErrorReporter;
  private final NamespaceContext fNamespaceContext;
  private final XMLSchemaValidator fSchemaValidator;
  private final SymbolTable fSymbolTable;
  private final ValidationManager fValidationManager;
  private final XMLSchemaValidatorComponentManager fComponentManager;
  private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
  private boolean fNeedPushNSContext = true;
  private HashMap fUnparsedEntities = null;
  private boolean fStringsInternalized = false;
  private final QName fElementQName = new QName();
  private final QName fAttributeQName = new QName();
  private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
  private final AttributesProxy fAttrAdapter = new AttributesProxy(fAttributes);
  private final XMLString fTempString = new XMLString();
  private ContentHandler fContentHandler = null;
  private final XMLSchemaTypeInfoProvider fTypeInfoProvider = new XMLSchemaTypeInfoProvider(null);
  private final ResolutionForwarder fResolutionForwarder = new ResolutionForwarder(null);
  
  public ValidatorHandlerImpl(XSGrammarPoolContainer paramXSGrammarPoolContainer)
  {
    this(new XMLSchemaValidatorComponentManager(paramXSGrammarPoolContainer));
    fComponentManager.addRecognizedFeatures(new String[] { "http://xml.org/sax/features/namespace-prefixes" });
    fComponentManager.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
    setErrorHandler(null);
    setResourceResolver(null);
  }
  
  public ValidatorHandlerImpl(XMLSchemaValidatorComponentManager paramXMLSchemaValidatorComponentManager)
  {
    fComponentManager = paramXMLSchemaValidatorComponentManager;
    fErrorReporter = ((XMLErrorReporter)fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fNamespaceContext = ((NamespaceContext)fComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context"));
    fSchemaValidator = ((XMLSchemaValidator)fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema"));
    fSymbolTable = ((SymbolTable)fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fValidationManager = ((ValidationManager)fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
  }
  
  public void setContentHandler(ContentHandler paramContentHandler)
  {
    fContentHandler = paramContentHandler;
  }
  
  public ContentHandler getContentHandler()
  {
    return fContentHandler;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    fComponentManager.setErrorHandler(paramErrorHandler);
  }
  
  public ErrorHandler getErrorHandler()
  {
    return fComponentManager.getErrorHandler();
  }
  
  public void setResourceResolver(LSResourceResolver paramLSResourceResolver)
  {
    fComponentManager.setResourceResolver(paramLSResourceResolver);
  }
  
  public LSResourceResolver getResourceResolver()
  {
    return fComponentManager.getResourceResolver();
  }
  
  public TypeInfoProvider getTypeInfoProvider()
  {
    return fTypeInfoProvider;
  }
  
  public boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "FeatureNameNull", null));
    }
    if ("http://apache.org/xml/features/internal/strings-interned".equals(paramString)) {
      return fStringsInternalized;
    }
    try
    {
      return fComponentManager.getFeature(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "feature-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "feature-not-supported", new Object[] { str }));
    }
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "FeatureNameNull", null));
    }
    if ("http://apache.org/xml/features/internal/strings-interned".equals(paramString))
    {
      fStringsInternalized = paramBoolean;
      return;
    }
    try
    {
      fComponentManager.setFeature(paramString, paramBoolean);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "feature-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "feature-not-supported", new Object[] { str }));
    }
  }
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "ProperyNameNull", null));
    }
    try
    {
      return fComponentManager.getProperty(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "ProperyNameNull", null));
    }
    try
    {
      fComponentManager.setProperty(paramString, paramObject);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fComponentManager.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  public boolean isEntityDeclared(String paramString)
  {
    return false;
  }
  
  public boolean isEntityUnparsed(String paramString)
  {
    if (fUnparsedEntities != null) {
      return fUnparsedEntities.containsKey(paramString);
    }
    return false;
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null) {
      try
      {
        fContentHandler.startDocument();
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null) {
      try
      {
        fContentHandler.processingInstruction(paramString, paramXMLString.toString());
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null) {
      try
      {
        fTypeInfoProvider.beginStartElement(paramAugmentations, paramXMLAttributes);
        fContentHandler.startElement(uri != null ? uri : XMLSymbols.EMPTY_STRING, localpart, rawname, fAttrAdapter);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
      finally
      {
        fTypeInfoProvider.finishStartElement();
      }
    }
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    startElement(paramQName, paramXMLAttributes, paramAugmentations);
    endElement(paramQName, paramAugmentations);
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null)
    {
      if (length == 0) {
        return;
      }
      try
      {
        fContentHandler.characters(ch, offset, length);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null) {
      try
      {
        fContentHandler.ignorableWhitespace(ch, offset, length);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null) {
      try
      {
        fTypeInfoProvider.beginEndElement(paramAugmentations);
        fContentHandler.endElement(uri != null ? uri : XMLSymbols.EMPTY_STRING, localpart, rawname);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
      finally
      {
        fTypeInfoProvider.finishEndElement();
      }
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fContentHandler != null) {
      try
      {
        fContentHandler.endDocument();
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
  }
  
  public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource) {}
  
  public XMLDocumentSource getDocumentSource()
  {
    return fSchemaValidator;
  }
  
  public void setDocumentLocator(Locator paramLocator)
  {
    fSAXLocatorWrapper.setLocator(paramLocator);
    if (fContentHandler != null) {
      fContentHandler.setDocumentLocator(paramLocator);
    }
  }
  
  public void startDocument()
    throws SAXException
  {
    fComponentManager.reset();
    fSchemaValidator.setDocumentHandler(this);
    fValidationManager.setEntityState(this);
    fTypeInfoProvider.finishStartElement();
    fNeedPushNSContext = true;
    if ((fUnparsedEntities != null) && (!fUnparsedEntities.isEmpty())) {
      fUnparsedEntities.clear();
    }
    fErrorReporter.setDocumentLocator(fSAXLocatorWrapper);
    try
    {
      fSchemaValidator.startDocument(fSAXLocatorWrapper, fSAXLocatorWrapper.getEncoding(), fNamespaceContext, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      throw Util.toSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
    }
  }
  
  public void endDocument()
    throws SAXException
  {
    fSAXLocatorWrapper.setLocator(null);
    try
    {
      fSchemaValidator.endDocument(null);
    }
    catch (XMLParseException localXMLParseException)
    {
      throw Util.toSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
    }
  }
  
  public void startPrefixMapping(String paramString1, String paramString2)
    throws SAXException
  {
    String str1;
    String str2;
    if (!fStringsInternalized)
    {
      str1 = paramString1 != null ? fSymbolTable.addSymbol(paramString1) : XMLSymbols.EMPTY_STRING;
      str2 = (paramString2 != null) && (paramString2.length() > 0) ? fSymbolTable.addSymbol(paramString2) : null;
    }
    else
    {
      str1 = paramString1 != null ? paramString1 : XMLSymbols.EMPTY_STRING;
      str2 = (paramString2 != null) && (paramString2.length() > 0) ? paramString2 : null;
    }
    if (fNeedPushNSContext)
    {
      fNeedPushNSContext = false;
      fNamespaceContext.pushContext();
    }
    fNamespaceContext.declarePrefix(str1, str2);
    if (fContentHandler != null) {
      fContentHandler.startPrefixMapping(paramString1, paramString2);
    }
  }
  
  public void endPrefixMapping(String paramString)
    throws SAXException
  {
    if (fContentHandler != null) {
      fContentHandler.endPrefixMapping(paramString);
    }
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (fNeedPushNSContext) {
      fNamespaceContext.pushContext();
    }
    fNeedPushNSContext = true;
    fillQName(fElementQName, paramString1, paramString2, paramString3);
    if ((paramAttributes instanceof Attributes2)) {
      fillXMLAttributes2((Attributes2)paramAttributes);
    } else {
      fillXMLAttributes(paramAttributes);
    }
    try
    {
      fSchemaValidator.startElement(fElementQName, fAttributes, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      throw Util.toSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
    }
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    fillQName(fElementQName, paramString1, paramString2, paramString3);
    try
    {
      fSchemaValidator.endElement(fElementQName, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      throw Util.toSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
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
      fSchemaValidator.characters(fTempString, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      throw Util.toSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
    }
  }
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      fTempString.setValues(paramArrayOfChar, paramInt1, paramInt2);
      fSchemaValidator.ignorableWhitespace(fTempString, null);
    }
    catch (XMLParseException localXMLParseException)
    {
      throw Util.toSAXParseException(localXMLParseException);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
    }
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    if (fContentHandler != null) {
      fContentHandler.processingInstruction(paramString1, paramString2);
    }
  }
  
  public void skippedEntity(String paramString)
    throws SAXException
  {
    if (fContentHandler != null) {
      fContentHandler.skippedEntity(paramString);
    }
  }
  
  public void notationDecl(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {}
  
  public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (fUnparsedEntities == null) {
      fUnparsedEntities = new HashMap();
    }
    fUnparsedEntities.put(paramString1, paramString1);
  }
  
  public void validate(Source paramSource, Result paramResult)
    throws SAXException, IOException
  {
    if (((paramResult instanceof SAXResult)) || (paramResult == null))
    {
      SAXSource localSAXSource = (SAXSource)paramSource;
      SAXResult localSAXResult = (SAXResult)paramResult;
      LexicalHandler localLexicalHandler = null;
      if (paramResult != null)
      {
        localObject1 = localSAXResult.getHandler();
        localLexicalHandler = localSAXResult.getLexicalHandler();
        if ((localLexicalHandler == null) && ((localObject1 instanceof LexicalHandler))) {
          localLexicalHandler = (LexicalHandler)localObject1;
        }
        setContentHandler((ContentHandler)localObject1);
      }
      Object localObject1 = null;
      try
      {
        localObject1 = localSAXSource.getXMLReader();
        if (localObject1 == null)
        {
          SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
          localSAXParserFactory.setNamespaceAware(true);
          try
          {
            localObject1 = localSAXParserFactory.newSAXParser().getXMLReader();
            if ((localObject1 instanceof org.apache.xerces.parsers.SAXParser))
            {
              Object localObject2 = fComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
              if (localObject2 != null) {
                try
                {
                  ((XMLReader)localObject1).setProperty("http://apache.org/xml/properties/security-manager", localObject2);
                }
                catch (SAXException localSAXException3) {}
              }
            }
          }
          catch (Exception localException1)
          {
            throw new FactoryConfigurationError(localException1);
          }
        }
        try
        {
          fStringsInternalized = ((XMLReader)localObject1).getFeature("http://xml.org/sax/features/string-interning");
        }
        catch (SAXException localSAXException1)
        {
          fStringsInternalized = false;
        }
        ErrorHandler localErrorHandler = fComponentManager.getErrorHandler();
        ((XMLReader)localObject1).setErrorHandler(localErrorHandler != null ? localErrorHandler : DraconianErrorHandler.getInstance());
        ((XMLReader)localObject1).setEntityResolver(fResolutionForwarder);
        fResolutionForwarder.setEntityResolver(fComponentManager.getResourceResolver());
        ((XMLReader)localObject1).setContentHandler(this);
        ((XMLReader)localObject1).setDTDHandler(this);
        try
        {
          ((XMLReader)localObject1).setProperty("http://xml.org/sax/properties/lexical-handler", localLexicalHandler);
        }
        catch (SAXException localSAXException2) {}
        InputSource localInputSource = localSAXSource.getInputSource();
        ((XMLReader)localObject1).parse(localInputSource);
      }
      finally
      {
        setContentHandler(null);
        if (localObject1 != null) {
          try
          {
            ((XMLReader)localObject1).setContentHandler(null);
            ((XMLReader)localObject1).setDTDHandler(null);
            ((XMLReader)localObject1).setErrorHandler(null);
            ((XMLReader)localObject1).setEntityResolver(null);
            fResolutionForwarder.setEntityResolver(null);
            ((XMLReader)localObject1).setProperty("http://xml.org/sax/properties/lexical-handler", null);
          }
          catch (Exception localException2) {}
        }
      }
      return;
    }
    throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { paramSource.getClass().getName(), paramResult.getClass().getName() }));
  }
  
  public ElementPSVI getElementPSVI()
  {
    return fTypeInfoProvider.getElementPSVI();
  }
  
  public AttributePSVI getAttributePSVI(int paramInt)
  {
    return fTypeInfoProvider.getAttributePSVI(paramInt);
  }
  
  public AttributePSVI getAttributePSVIByName(String paramString1, String paramString2)
  {
    return fTypeInfoProvider.getAttributePSVIByName(paramString1, paramString2);
  }
  
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
    if (i != -1) {
      str = fSymbolTable.addSymbol(paramString3.substring(0, i));
    }
    paramQName.setValues(str, paramString2, paramString3, paramString1);
  }
  
  private void fillXMLAttributes(Attributes paramAttributes)
  {
    fAttributes.removeAllAttributes();
    int i = paramAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      fillXMLAttribute(paramAttributes, j);
      fAttributes.setSpecified(j, true);
    }
  }
  
  private void fillXMLAttributes2(Attributes2 paramAttributes2)
  {
    fAttributes.removeAllAttributes();
    int i = paramAttributes2.getLength();
    for (int j = 0; j < i; j++)
    {
      fillXMLAttribute(paramAttributes2, j);
      fAttributes.setSpecified(j, paramAttributes2.isSpecified(j));
      if (paramAttributes2.isDeclared(j)) {
        fAttributes.getAugmentations(j).putItem("ATTRIBUTE_DECLARED", Boolean.TRUE);
      }
    }
  }
  
  private void fillXMLAttribute(Attributes paramAttributes, int paramInt)
  {
    fillQName(fAttributeQName, paramAttributes.getURI(paramInt), paramAttributes.getLocalName(paramInt), paramAttributes.getQName(paramInt));
    String str = paramAttributes.getType(paramInt);
    fAttributes.addAttributeNS(fAttributeQName, str != null ? str : XMLSymbols.fCDATASymbol, paramAttributes.getValue(paramInt));
  }
  
  static final class ResolutionForwarder
    implements EntityResolver2
  {
    private static final String XML_TYPE = "http://www.w3.org/TR/REC-xml";
    protected LSResourceResolver fEntityResolver;
    
    public ResolutionForwarder() {}
    
    public ResolutionForwarder(LSResourceResolver paramLSResourceResolver)
    {
      setEntityResolver(paramLSResourceResolver);
    }
    
    public void setEntityResolver(LSResourceResolver paramLSResourceResolver)
    {
      fEntityResolver = paramLSResourceResolver;
    }
    
    public LSResourceResolver getEntityResolver()
    {
      return fEntityResolver;
    }
    
    public InputSource getExternalSubset(String paramString1, String paramString2)
      throws SAXException, IOException
    {
      return null;
    }
    
    public InputSource resolveEntity(String paramString1, String paramString2, String paramString3, String paramString4)
      throws SAXException, IOException
    {
      if (fEntityResolver != null)
      {
        LSInput localLSInput = fEntityResolver.resolveResource("http://www.w3.org/TR/REC-xml", null, paramString2, paramString4, paramString3);
        if (localLSInput != null)
        {
          String str1 = localLSInput.getPublicId();
          String str2 = localLSInput.getSystemId();
          String str3 = localLSInput.getBaseURI();
          Reader localReader = localLSInput.getCharacterStream();
          InputStream localInputStream = localLSInput.getByteStream();
          String str4 = localLSInput.getStringData();
          String str5 = localLSInput.getEncoding();
          InputSource localInputSource = new InputSource();
          localInputSource.setPublicId(str1);
          localInputSource.setSystemId(str3 != null ? resolveSystemId(str2, str3) : str2);
          if (localReader != null) {
            localInputSource.setCharacterStream(localReader);
          } else if (localInputStream != null) {
            localInputSource.setByteStream(localInputStream);
          } else if ((str4 != null) && (str4.length() != 0)) {
            localInputSource.setCharacterStream(new StringReader(str4));
          }
          localInputSource.setEncoding(str5);
          return localInputSource;
        }
      }
      return null;
    }
    
    public InputSource resolveEntity(String paramString1, String paramString2)
      throws SAXException, IOException
    {
      return resolveEntity(null, paramString1, null, paramString2);
    }
    
    private String resolveSystemId(String paramString1, String paramString2)
    {
      try
      {
        return XMLEntityManager.expandSystemId(paramString1, paramString2, false);
      }
      catch (URI.MalformedURIException localMalformedURIException) {}
      return paramString1;
    }
  }
  
  private class XMLSchemaTypeInfoProvider
    extends TypeInfoProvider
  {
    private Augmentations fElementAugs;
    private XMLAttributes fAttributes;
    private boolean fInStartElement = false;
    private boolean fInEndElement = false;
    
    private XMLSchemaTypeInfoProvider() {}
    
    void beginStartElement(Augmentations paramAugmentations, XMLAttributes paramXMLAttributes)
    {
      fInStartElement = true;
      fElementAugs = paramAugmentations;
      fAttributes = paramXMLAttributes;
    }
    
    void finishStartElement()
    {
      fInStartElement = false;
      fElementAugs = null;
      fAttributes = null;
    }
    
    void beginEndElement(Augmentations paramAugmentations)
    {
      fInEndElement = true;
      fElementAugs = paramAugmentations;
    }
    
    void finishEndElement()
    {
      fInEndElement = false;
      fElementAugs = null;
    }
    
    private void checkStateAttribute()
    {
      if (!fInStartElement) {
        throw new IllegalStateException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "TypeInfoProviderIllegalStateAttribute", null));
      }
    }
    
    private void checkStateElement()
    {
      if ((!fInStartElement) && (!fInEndElement)) {
        throw new IllegalStateException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "TypeInfoProviderIllegalStateElement", null));
      }
    }
    
    public TypeInfo getAttributeTypeInfo(int paramInt)
    {
      checkStateAttribute();
      return getAttributeType(paramInt);
    }
    
    private TypeInfo getAttributeType(int paramInt)
    {
      checkStateAttribute();
      if ((paramInt < 0) || (fAttributes.getLength() <= paramInt)) {
        throw new IndexOutOfBoundsException(Integer.toString(paramInt));
      }
      Augmentations localAugmentations = fAttributes.getAugmentations(paramInt);
      if (localAugmentations == null) {
        return null;
      }
      AttributePSVI localAttributePSVI = (AttributePSVI)localAugmentations.getItem("ATTRIBUTE_PSVI");
      return getTypeInfoFromPSVI(localAttributePSVI);
    }
    
    public TypeInfo getAttributeTypeInfo(String paramString1, String paramString2)
    {
      checkStateAttribute();
      return getAttributeTypeInfo(fAttributes.getIndex(paramString1, paramString2));
    }
    
    public TypeInfo getAttributeTypeInfo(String paramString)
    {
      checkStateAttribute();
      return getAttributeTypeInfo(fAttributes.getIndex(paramString));
    }
    
    public TypeInfo getElementTypeInfo()
    {
      checkStateElement();
      if (fElementAugs == null) {
        return null;
      }
      ElementPSVI localElementPSVI = (ElementPSVI)fElementAugs.getItem("ELEMENT_PSVI");
      return getTypeInfoFromPSVI(localElementPSVI);
    }
    
    private TypeInfo getTypeInfoFromPSVI(ItemPSVI paramItemPSVI)
    {
      if (paramItemPSVI == null) {
        return null;
      }
      if (paramItemPSVI.getValidity() == 2)
      {
        localObject = paramItemPSVI.getMemberTypeDefinition();
        if (localObject != null) {
          return (localObject instanceof TypeInfo) ? (TypeInfo)localObject : null;
        }
      }
      Object localObject = paramItemPSVI.getTypeDefinition();
      if (localObject != null) {
        return (localObject instanceof TypeInfo) ? (TypeInfo)localObject : null;
      }
      return null;
    }
    
    public boolean isIdAttribute(int paramInt)
    {
      checkStateAttribute();
      XSSimpleType localXSSimpleType = (XSSimpleType)getAttributeType(paramInt);
      if (localXSSimpleType == null) {
        return false;
      }
      return localXSSimpleType.isIDType();
    }
    
    public boolean isSpecified(int paramInt)
    {
      checkStateAttribute();
      return fAttributes.isSpecified(paramInt);
    }
    
    ElementPSVI getElementPSVI()
    {
      return fElementAugs != null ? (ElementPSVI)fElementAugs.getItem("ELEMENT_PSVI") : null;
    }
    
    AttributePSVI getAttributePSVI(int paramInt)
    {
      if (fAttributes != null)
      {
        Augmentations localAugmentations = fAttributes.getAugmentations(paramInt);
        if (localAugmentations != null) {
          return (AttributePSVI)localAugmentations.getItem("ATTRIBUTE_PSVI");
        }
      }
      return null;
    }
    
    AttributePSVI getAttributePSVIByName(String paramString1, String paramString2)
    {
      if (fAttributes != null)
      {
        Augmentations localAugmentations = fAttributes.getAugmentations(paramString1, paramString2);
        if (localAugmentations != null) {
          return (AttributePSVI)localAugmentations.getItem("ATTRIBUTE_PSVI");
        }
      }
      return null;
    }
    
    XMLSchemaTypeInfoProvider(ValidatorHandlerImpl.1 param1)
    {
      this();
    }
  }
}
