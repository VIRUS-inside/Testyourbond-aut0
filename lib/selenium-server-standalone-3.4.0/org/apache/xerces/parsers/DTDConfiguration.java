package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import org.apache.xerces.impl.XMLDTDScannerImpl;
import org.apache.xerces.impl.XMLDocumentScannerImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLNamespaceBinder;
import org.apache.xerces.impl.dtd.XMLDTDProcessor;
import org.apache.xerces.impl.dtd.XMLDTDValidator;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
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

public class DTDConfiguration
  extends BasicParserConfiguration
  implements XMLPullParserConfiguration
{
  protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
  protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
  protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
  protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
  protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
  protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
  protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
  protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
  protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
  protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  protected static final String DTD_PROCESSOR = "http://apache.org/xml/properties/internal/dtd-processor";
  protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
  protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
  protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
  protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
  protected static final String LOCALE = "http://apache.org/xml/properties/locale";
  protected static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
  protected XMLGrammarPool fGrammarPool;
  protected DTDDVFactory fDatatypeValidatorFactory;
  protected XMLErrorReporter fErrorReporter;
  protected XMLEntityManager fEntityManager;
  protected XMLDocumentScanner fScanner;
  protected XMLInputSource fInputSource;
  protected XMLDTDScanner fDTDScanner;
  protected XMLDTDProcessor fDTDProcessor;
  protected XMLDTDValidator fDTDValidator;
  protected XMLNamespaceBinder fNamespaceBinder;
  protected ValidationManager fValidationManager;
  protected XMLLocator fLocator;
  protected boolean fParseInProgress = false;
  
  public DTDConfiguration()
  {
    this(null, null, null);
  }
  
  public DTDConfiguration(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public DTDConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public DTDConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLComponentManager);
    String[] arrayOfString1 = { "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd" };
    addRecognizedFeatures(arrayOfString1);
    setFeature("http://apache.org/xml/features/continue-after-fatal-error", false);
    setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
    String[] arrayOfString2 = { "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/dtd-processor", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/namespace-binder", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/locale" };
    addRecognizedProperties(arrayOfString2);
    fGrammarPool = paramXMLGrammarPool;
    if (fGrammarPool != null) {
      setProperty("http://apache.org/xml/properties/internal/grammar-pool", fGrammarPool);
    }
    fEntityManager = createEntityManager();
    setProperty("http://apache.org/xml/properties/internal/entity-manager", fEntityManager);
    addComponent(fEntityManager);
    fErrorReporter = createErrorReporter();
    fErrorReporter.setDocumentLocator(fEntityManager.getEntityScanner());
    setProperty("http://apache.org/xml/properties/internal/error-reporter", fErrorReporter);
    addComponent(fErrorReporter);
    fScanner = createDocumentScanner();
    setProperty("http://apache.org/xml/properties/internal/document-scanner", fScanner);
    if ((fScanner instanceof XMLComponent)) {
      addComponent((XMLComponent)fScanner);
    }
    fDTDScanner = createDTDScanner();
    if (fDTDScanner != null)
    {
      setProperty("http://apache.org/xml/properties/internal/dtd-scanner", fDTDScanner);
      if ((fDTDScanner instanceof XMLComponent)) {
        addComponent((XMLComponent)fDTDScanner);
      }
    }
    fDTDProcessor = createDTDProcessor();
    if (fDTDProcessor != null)
    {
      setProperty("http://apache.org/xml/properties/internal/dtd-processor", fDTDProcessor);
      addComponent(fDTDProcessor);
    }
    fDTDValidator = createDTDValidator();
    if (fDTDValidator != null)
    {
      setProperty("http://apache.org/xml/properties/internal/validator/dtd", fDTDValidator);
      addComponent(fDTDValidator);
    }
    fNamespaceBinder = createNamespaceBinder();
    if (fNamespaceBinder != null)
    {
      setProperty("http://apache.org/xml/properties/internal/namespace-binder", fNamespaceBinder);
      addComponent(fNamespaceBinder);
    }
    fDatatypeValidatorFactory = createDatatypeValidatorFactory();
    if (fDatatypeValidatorFactory != null) {
      setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", fDatatypeValidatorFactory);
    }
    fValidationManager = createValidationManager();
    if (fValidationManager != null) {
      setProperty("http://apache.org/xml/properties/internal/validation-manager", fValidationManager);
    }
    if (fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null)
    {
      XMLMessageFormatter localXMLMessageFormatter = new XMLMessageFormatter();
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", localXMLMessageFormatter);
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", localXMLMessageFormatter);
    }
    try
    {
      setLocale(Locale.getDefault());
    }
    catch (XNIException localXNIException) {}
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
    if ("http://apache.org/xml/properties/locale".equals(paramString)) {
      setLocale((Locale)paramObject);
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
        reset();
        fScanner.setInputSource(fInputSource);
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
      return fScanner.scanDocument(paramBoolean);
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
  
  protected void reset()
    throws XNIException
  {
    if (fValidationManager != null) {
      fValidationManager.reset();
    }
    configurePipeline();
    super.reset();
  }
  
  protected void configurePipeline()
  {
    if (fDTDValidator != null)
    {
      fScanner.setDocumentHandler(fDTDValidator);
      if (fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE)
      {
        fDTDValidator.setDocumentHandler(fNamespaceBinder);
        fDTDValidator.setDocumentSource(fScanner);
        fNamespaceBinder.setDocumentHandler(fDocumentHandler);
        fNamespaceBinder.setDocumentSource(fDTDValidator);
        fLastComponent = fNamespaceBinder;
      }
      else
      {
        fDTDValidator.setDocumentHandler(fDocumentHandler);
        fDTDValidator.setDocumentSource(fScanner);
        fLastComponent = fDTDValidator;
      }
    }
    else if (fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE)
    {
      fScanner.setDocumentHandler(fNamespaceBinder);
      fNamespaceBinder.setDocumentHandler(fDocumentHandler);
      fNamespaceBinder.setDocumentSource(fScanner);
      fLastComponent = fNamespaceBinder;
    }
    else
    {
      fScanner.setDocumentHandler(fDocumentHandler);
      fLastComponent = fScanner;
    }
    configureDTDPipeline();
  }
  
  protected void configureDTDPipeline()
  {
    if (fDTDScanner != null)
    {
      fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", fDTDScanner);
      if (fDTDProcessor != null)
      {
        fProperties.put("http://apache.org/xml/properties/internal/dtd-processor", fDTDProcessor);
        fDTDScanner.setDTDHandler(fDTDProcessor);
        fDTDProcessor.setDTDSource(fDTDScanner);
        fDTDProcessor.setDTDHandler(fDTDHandler);
        if (fDTDHandler != null) {
          fDTDHandler.setDTDSource(fDTDProcessor);
        }
        fDTDScanner.setDTDContentModelHandler(fDTDProcessor);
        fDTDProcessor.setDTDContentModelSource(fDTDScanner);
        fDTDProcessor.setDTDContentModelHandler(fDTDContentModelHandler);
        if (fDTDContentModelHandler != null) {
          fDTDContentModelHandler.setDTDContentModelSource(fDTDProcessor);
        }
      }
      else
      {
        fDTDScanner.setDTDHandler(fDTDHandler);
        if (fDTDHandler != null) {
          fDTDHandler.setDTDSource(fDTDScanner);
        }
        fDTDScanner.setDTDContentModelHandler(fDTDContentModelHandler);
        if (fDTDContentModelHandler != null) {
          fDTDContentModelHandler.setDTDContentModelSource(fDTDScanner);
        }
      }
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
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      int i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/dtd-scanner".length()) && (paramString.endsWith("internal/dtd-scanner"))) {
        return;
      }
    }
    super.checkProperty(paramString);
  }
  
  protected XMLEntityManager createEntityManager()
  {
    return new XMLEntityManager();
  }
  
  protected XMLErrorReporter createErrorReporter()
  {
    return new XMLErrorReporter();
  }
  
  protected XMLDocumentScanner createDocumentScanner()
  {
    return new XMLDocumentScannerImpl();
  }
  
  protected XMLDTDScanner createDTDScanner()
  {
    return new XMLDTDScannerImpl();
  }
  
  protected XMLDTDProcessor createDTDProcessor()
  {
    return new XMLDTDProcessor();
  }
  
  protected XMLDTDValidator createDTDValidator()
  {
    return new XMLDTDValidator();
  }
  
  protected XMLNamespaceBinder createNamespaceBinder()
  {
    return new XMLNamespaceBinder();
  }
  
  protected DTDDVFactory createDatatypeValidatorFactory()
  {
    return DTDDVFactory.getInstance();
  }
  
  protected ValidationManager createValidationManager()
  {
    return new ValidationManager();
  }
}
