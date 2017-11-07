package org.apache.xerces.impl.xs.opti;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import org.apache.xerces.impl.XML11DTDScannerImpl;
import org.apache.xerces.impl.XML11NSDocumentScannerImpl;
import org.apache.xerces.impl.XMLDTDScannerImpl;
import org.apache.xerces.impl.XMLEntityHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLNSDocumentScannerImpl;
import org.apache.xerces.impl.XMLVersionDetector;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.parsers.BasicParserConfiguration;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLPullParserConfiguration;

public class SchemaParsingConfig
  extends BasicParserConfiguration
  implements XMLPullParserConfiguration
{
  protected static final String XML11_DATATYPE_VALIDATOR_FACTORY = "org.apache.xerces.impl.dv.dtd.XML11DTDDVFactoryImpl";
  protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
  protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
  protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
  protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
  protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
  protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
  protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
  protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
  protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
  protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
  protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
  protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
  protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
  protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
  protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  protected static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
  protected static final String LOCALE = "http://apache.org/xml/properties/locale";
  private static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
  protected final DTDDVFactory fDatatypeValidatorFactory;
  protected final XMLNSDocumentScannerImpl fNamespaceScanner;
  protected final XMLDTDScannerImpl fDTDScanner;
  protected DTDDVFactory fXML11DatatypeFactory = null;
  protected XML11NSDocumentScannerImpl fXML11NSDocScanner = null;
  protected XML11DTDScannerImpl fXML11DTDScanner = null;
  protected DTDDVFactory fCurrentDVFactory;
  protected XMLDocumentScanner fCurrentScanner;
  protected XMLDTDScanner fCurrentDTDScanner;
  protected XMLGrammarPool fGrammarPool;
  protected final XMLVersionDetector fVersionDetector;
  protected final XMLErrorReporter fErrorReporter;
  protected final XMLEntityManager fEntityManager;
  protected XMLInputSource fInputSource;
  protected final ValidationManager fValidationManager;
  protected XMLLocator fLocator;
  protected boolean fParseInProgress = false;
  protected boolean fConfigUpdated = false;
  private boolean f11Initialized = false;
  
  public SchemaParsingConfig()
  {
    this(null, null, null);
  }
  
  public SchemaParsingConfig(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public SchemaParsingConfig(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public SchemaParsingConfig(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLComponentManager);
    String[] arrayOfString1 = { "http://apache.org/xml/features/internal/parser-settings", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://apache.org/xml/features/scanner/notify-char-refs", "http://apache.org/xml/features/generate-synthetic-annotations" };
    addRecognizedFeatures(arrayOfString1);
    fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
    fFeatures.put("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/allow-java-encodings", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/continue-after-fatal-error", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.TRUE);
    fFeatures.put("http://apache.org/xml/features/scanner/notify-builtin-refs", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/scanner/notify-char-refs", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/generate-synthetic-annotations", Boolean.FALSE);
    String[] arrayOfString2 = { "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/namespace-binder", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/properties/locale" };
    addRecognizedProperties(arrayOfString2);
    fGrammarPool = paramXMLGrammarPool;
    if (fGrammarPool != null) {
      setProperty("http://apache.org/xml/properties/internal/grammar-pool", fGrammarPool);
    }
    fEntityManager = new XMLEntityManager();
    fProperties.put("http://apache.org/xml/properties/internal/entity-manager", fEntityManager);
    addComponent(fEntityManager);
    fErrorReporter = new XMLErrorReporter();
    fErrorReporter.setDocumentLocator(fEntityManager.getEntityScanner());
    fProperties.put("http://apache.org/xml/properties/internal/error-reporter", fErrorReporter);
    addComponent(fErrorReporter);
    fNamespaceScanner = new XMLNSDocumentScannerImpl();
    fProperties.put("http://apache.org/xml/properties/internal/document-scanner", fNamespaceScanner);
    addRecognizedParamsAndSetDefaults(fNamespaceScanner);
    fDTDScanner = new XMLDTDScannerImpl();
    fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", fDTDScanner);
    addRecognizedParamsAndSetDefaults(fDTDScanner);
    fDatatypeValidatorFactory = DTDDVFactory.getInstance();
    fProperties.put("http://apache.org/xml/properties/internal/datatype-validator-factory", fDatatypeValidatorFactory);
    fValidationManager = new ValidationManager();
    fProperties.put("http://apache.org/xml/properties/internal/validation-manager", fValidationManager);
    fVersionDetector = new XMLVersionDetector();
    Object localObject;
    if (fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null)
    {
      localObject = new XMLMessageFormatter();
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", (MessageFormatter)localObject);
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", (MessageFormatter)localObject);
    }
    if (fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null)
    {
      localObject = new XSMessageFormatter();
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", (MessageFormatter)localObject);
    }
    try
    {
      setLocale(Locale.getDefault());
    }
    catch (XNIException localXNIException) {}
  }
  
  public boolean getFeature(String paramString)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://apache.org/xml/features/internal/parser-settings")) {
      return fConfigUpdated;
    }
    return super.getFeature(paramString);
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    fConfigUpdated = true;
    fNamespaceScanner.setFeature(paramString, paramBoolean);
    fDTDScanner.setFeature(paramString, paramBoolean);
    if (f11Initialized)
    {
      try
      {
        fXML11DTDScanner.setFeature(paramString, paramBoolean);
      }
      catch (Exception localException1) {}
      try
      {
        fXML11NSDocScanner.setFeature(paramString, paramBoolean);
      }
      catch (Exception localException2) {}
    }
    super.setFeature(paramString, paramBoolean);
  }
  
  public Object getProperty(String paramString)
    throws XMLConfigurationException
  {
    if ("http://apache.org/xml/properties/locale".equals(paramString)) {
      return getLocale();
    }
    return super.getProperty(paramString);
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    fConfigUpdated = true;
    if ("http://apache.org/xml/properties/locale".equals(paramString)) {
      setLocale((Locale)paramObject);
    }
    fNamespaceScanner.setProperty(paramString, paramObject);
    fDTDScanner.setProperty(paramString, paramObject);
    if (f11Initialized)
    {
      try
      {
        fXML11DTDScanner.setProperty(paramString, paramObject);
      }
      catch (Exception localException1) {}
      try
      {
        fXML11NSDocScanner.setProperty(paramString, paramObject);
      }
      catch (Exception localException2) {}
    }
    super.setProperty(paramString, paramObject);
  }
  
  public void setLocale(Locale paramLocale)
    throws XNIException
  {
    super.setLocale(paramLocale);
    fErrorReporter.setLocale(paramLocale);
  }
  
  public void setInputSource(XMLInputSource paramXMLInputSource)
    throws XMLConfigurationException, IOException
  {
    fInputSource = paramXMLInputSource;
  }
  
  public boolean parse(boolean paramBoolean)
    throws XNIException, IOException
  {
    if (fInputSource != null) {
      try
      {
        fValidationManager.reset();
        fVersionDetector.reset(this);
        reset();
        short s = fVersionDetector.determineDocVersion(fInputSource);
        if (s == 1)
        {
          configurePipeline();
          resetXML10();
        }
        else if (s == 2)
        {
          initXML11Components();
          configureXML11Pipeline();
          resetXML11();
        }
        else
        {
          return false;
        }
        fConfigUpdated = false;
        fVersionDetector.startDocumentParsing((XMLEntityHandler)fCurrentScanner, s);
        fInputSource = null;
      }
      catch (XNIException localXNIException1)
      {
        throw localXNIException1;
      }
      catch (IOException localIOException1)
      {
        throw localIOException1;
      }
      catch (RuntimeException localRuntimeException1)
      {
        throw localRuntimeException1;
      }
      catch (Exception localException1)
      {
        throw new XNIException(localException1);
      }
    }
    try
    {
      return fCurrentScanner.scanDocument(paramBoolean);
    }
    catch (XNIException localXNIException2)
    {
      throw localXNIException2;
    }
    catch (IOException localIOException2)
    {
      throw localIOException2;
    }
    catch (RuntimeException localRuntimeException2)
    {
      throw localRuntimeException2;
    }
    catch (Exception localException2)
    {
      throw new XNIException(localException2);
    }
  }
  
  public void cleanup()
  {
    fEntityManager.closeReaders();
  }
  
  public void parse(XMLInputSource paramXMLInputSource)
    throws XNIException, IOException
  {
    if (fParseInProgress) {
      throw new XNIException("FWK005 parse may not be called while parsing.");
    }
    fParseInProgress = true;
    try
    {
      setInputSource(paramXMLInputSource);
      parse(true);
    }
    catch (XNIException localXNIException)
    {
      throw localXNIException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (Exception localException)
    {
      throw new XNIException(localException);
    }
    finally
    {
      fParseInProgress = false;
      cleanup();
    }
  }
  
  public void reset()
    throws XNIException
  {
    super.reset();
  }
  
  protected void configurePipeline()
  {
    if (fCurrentDVFactory != fDatatypeValidatorFactory)
    {
      fCurrentDVFactory = fDatatypeValidatorFactory;
      setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", fCurrentDVFactory);
    }
    if (fCurrentScanner != fNamespaceScanner)
    {
      fCurrentScanner = fNamespaceScanner;
      setProperty("http://apache.org/xml/properties/internal/document-scanner", fCurrentScanner);
    }
    fNamespaceScanner.setDocumentHandler(fDocumentHandler);
    if (fDocumentHandler != null) {
      fDocumentHandler.setDocumentSource(fNamespaceScanner);
    }
    fLastComponent = fNamespaceScanner;
    if (fCurrentDTDScanner != fDTDScanner)
    {
      fCurrentDTDScanner = fDTDScanner;
      setProperty("http://apache.org/xml/properties/internal/dtd-scanner", fCurrentDTDScanner);
    }
    fDTDScanner.setDTDHandler(fDTDHandler);
    if (fDTDHandler != null) {
      fDTDHandler.setDTDSource(fDTDScanner);
    }
    fDTDScanner.setDTDContentModelHandler(fDTDContentModelHandler);
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.setDTDContentModelSource(fDTDScanner);
    }
  }
  
  protected void configureXML11Pipeline()
  {
    if (fCurrentDVFactory != fXML11DatatypeFactory)
    {
      fCurrentDVFactory = fXML11DatatypeFactory;
      setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", fCurrentDVFactory);
    }
    if (fCurrentScanner != fXML11NSDocScanner)
    {
      fCurrentScanner = fXML11NSDocScanner;
      setProperty("http://apache.org/xml/properties/internal/document-scanner", fCurrentScanner);
    }
    fXML11NSDocScanner.setDocumentHandler(fDocumentHandler);
    if (fDocumentHandler != null) {
      fDocumentHandler.setDocumentSource(fXML11NSDocScanner);
    }
    fLastComponent = fXML11NSDocScanner;
    if (fCurrentDTDScanner != fXML11DTDScanner)
    {
      fCurrentDTDScanner = fXML11DTDScanner;
      setProperty("http://apache.org/xml/properties/internal/dtd-scanner", fCurrentDTDScanner);
    }
    fXML11DTDScanner.setDTDHandler(fDTDHandler);
    if (fDTDHandler != null) {
      fDTDHandler.setDTDSource(fXML11DTDScanner);
    }
    fXML11DTDScanner.setDTDContentModelHandler(fDTDContentModelHandler);
    if (fDTDContentModelHandler != null) {
      fDTDContentModelHandler.setDTDContentModelSource(fXML11DTDScanner);
    }
  }
  
  protected void checkFeature(String paramString)
    throws XMLConfigurationException
  {
    if (paramString.startsWith("http://apache.org/xml/features/"))
    {
      int i = paramString.length() - "http://apache.org/xml/features/".length();
      if ((i == "validation/dynamic".length()) && (paramString.endsWith("validation/dynamic"))) {
        return;
      }
      short s;
      if ((i == "validation/default-attribute-values".length()) && (paramString.endsWith("validation/default-attribute-values")))
      {
        s = 1;
        throw new XMLConfigurationException(s, paramString);
      }
      if ((i == "validation/validate-content-models".length()) && (paramString.endsWith("validation/validate-content-models")))
      {
        s = 1;
        throw new XMLConfigurationException(s, paramString);
      }
      if ((i == "nonvalidating/load-dtd-grammar".length()) && (paramString.endsWith("nonvalidating/load-dtd-grammar"))) {
        return;
      }
      if ((i == "nonvalidating/load-external-dtd".length()) && (paramString.endsWith("nonvalidating/load-external-dtd"))) {
        return;
      }
      if ((i == "validation/validate-datatypes".length()) && (paramString.endsWith("validation/validate-datatypes")))
      {
        s = 1;
        throw new XMLConfigurationException(s, paramString);
      }
    }
    super.checkFeature(paramString);
  }
  
  protected void checkProperty(String paramString)
    throws XMLConfigurationException
  {
    int i;
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/dtd-scanner".length()) && (paramString.endsWith("internal/dtd-scanner"))) {
        return;
      }
    }
    if (paramString.startsWith("http://java.sun.com/xml/jaxp/properties/"))
    {
      i = paramString.length() - "http://java.sun.com/xml/jaxp/properties/".length();
      if ((i == "schemaSource".length()) && (paramString.endsWith("schemaSource"))) {
        return;
      }
    }
    super.checkProperty(paramString);
  }
  
  private void addRecognizedParamsAndSetDefaults(XMLComponent paramXMLComponent)
  {
    String[] arrayOfString1 = paramXMLComponent.getRecognizedFeatures();
    addRecognizedFeatures(arrayOfString1);
    String[] arrayOfString2 = paramXMLComponent.getRecognizedProperties();
    addRecognizedProperties(arrayOfString2);
    int i;
    String str;
    Object localObject;
    if (arrayOfString1 != null) {
      for (i = 0; i < arrayOfString1.length; i++)
      {
        str = arrayOfString1[i];
        localObject = paramXMLComponent.getFeatureDefault(str);
        if ((localObject != null) && (!fFeatures.containsKey(str)))
        {
          fFeatures.put(str, localObject);
          fConfigUpdated = true;
        }
      }
    }
    if (arrayOfString2 != null) {
      for (i = 0; i < arrayOfString2.length; i++)
      {
        str = arrayOfString2[i];
        localObject = paramXMLComponent.getPropertyDefault(str);
        if ((localObject != null) && (!fProperties.containsKey(str)))
        {
          fProperties.put(str, localObject);
          fConfigUpdated = true;
        }
      }
    }
  }
  
  protected final void resetXML10()
    throws XNIException
  {
    fNamespaceScanner.reset(this);
    fDTDScanner.reset(this);
  }
  
  protected final void resetXML11()
    throws XNIException
  {
    fXML11NSDocScanner.reset(this);
    fXML11DTDScanner.reset(this);
  }
  
  public void resetNodePool() {}
  
  private void initXML11Components()
  {
    if (!f11Initialized)
    {
      fXML11DatatypeFactory = DTDDVFactory.getInstance("org.apache.xerces.impl.dv.dtd.XML11DTDDVFactoryImpl");
      fXML11DTDScanner = new XML11DTDScannerImpl();
      addRecognizedParamsAndSetDefaults(fXML11DTDScanner);
      fXML11NSDocScanner = new XML11NSDocumentScannerImpl();
      addRecognizedParamsAndSetDefaults(fXML11NSDocScanner);
      f11Initialized = true;
    }
  }
}
