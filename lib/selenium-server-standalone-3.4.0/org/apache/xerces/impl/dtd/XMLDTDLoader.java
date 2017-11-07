package org.apache.xerces.impl.dtd;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import org.apache.xerces.impl.XMLDTDScannerImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarLoader;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDTDLoader
  extends XMLDTDProcessor
  implements XMLGrammarLoader
{
  protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
  protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
  private static final String[] LOADER_RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/scanner/notify-char-refs", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/validation/balance-syntax-trees" };
  protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
  public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
  public static final String LOCALE = "http://apache.org/xml/properties/locale";
  private static final String[] LOADER_RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/locale" };
  private boolean fStrictURI = false;
  private boolean fBalanceSyntaxTrees = false;
  protected XMLEntityResolver fEntityResolver;
  protected XMLDTDScannerImpl fDTDScanner;
  protected XMLEntityManager fEntityManager;
  protected Locale fLocale;
  
  public XMLDTDLoader()
  {
    this(new SymbolTable());
  }
  
  public XMLDTDLoader(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null);
  }
  
  public XMLDTDLoader(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null, new XMLEntityManager());
  }
  
  XMLDTDLoader(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLErrorReporter paramXMLErrorReporter, XMLEntityResolver paramXMLEntityResolver)
  {
    fSymbolTable = paramSymbolTable;
    fGrammarPool = paramXMLGrammarPool;
    if (paramXMLErrorReporter == null)
    {
      paramXMLErrorReporter = new XMLErrorReporter();
      paramXMLErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", new DefaultErrorHandler());
    }
    fErrorReporter = paramXMLErrorReporter;
    if (fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null)
    {
      XMLMessageFormatter localXMLMessageFormatter = new XMLMessageFormatter();
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", localXMLMessageFormatter);
      fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", localXMLMessageFormatter);
    }
    fEntityResolver = paramXMLEntityResolver;
    if ((fEntityResolver instanceof XMLEntityManager)) {
      fEntityManager = ((XMLEntityManager)fEntityResolver);
    } else {
      fEntityManager = new XMLEntityManager();
    }
    fEntityManager.setProperty("http://apache.org/xml/properties/internal/error-reporter", paramXMLErrorReporter);
    fDTDScanner = createDTDScanner(fSymbolTable, fErrorReporter, fEntityManager);
    fDTDScanner.setDTDHandler(this);
    fDTDScanner.setDTDContentModelHandler(this);
    reset();
  }
  
  public String[] getRecognizedFeatures()
  {
    return (String[])LOADER_RECOGNIZED_FEATURES.clone();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://xml.org/sax/features/validation")) {
      fValidation = paramBoolean;
    } else if (paramString.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
      fWarnDuplicateAttdef = paramBoolean;
    } else if (paramString.equals("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef")) {
      fWarnOnUndeclaredElemdef = paramBoolean;
    } else if (paramString.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
      fDTDScanner.setFeature(paramString, paramBoolean);
    } else if (paramString.equals("http://apache.org/xml/features/standard-uri-conformant")) {
      fStrictURI = paramBoolean;
    } else if (paramString.equals("http://apache.org/xml/features/validation/balance-syntax-trees")) {
      fBalanceSyntaxTrees = paramBoolean;
    } else {
      throw new XMLConfigurationException((short)0, paramString);
    }
  }
  
  public String[] getRecognizedProperties()
  {
    return (String[])LOADER_RECOGNIZED_PROPERTIES.clone();
  }
  
  public Object getProperty(String paramString)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://apache.org/xml/properties/internal/symbol-table")) {
      return fSymbolTable;
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/error-reporter")) {
      return fErrorReporter;
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/error-handler")) {
      return fErrorReporter.getErrorHandler();
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
      return fEntityResolver;
    }
    if (paramString.equals("http://apache.org/xml/properties/locale")) {
      return getLocale();
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
      return fGrammarPool;
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/validator/dtd")) {
      return fValidator;
    }
    throw new XMLConfigurationException((short)0, paramString);
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://apache.org/xml/properties/internal/symbol-table"))
    {
      fSymbolTable = ((SymbolTable)paramObject);
      fDTDScanner.setProperty(paramString, paramObject);
      fEntityManager.setProperty(paramString, paramObject);
    }
    else if (paramString.equals("http://apache.org/xml/properties/internal/error-reporter"))
    {
      fErrorReporter = ((XMLErrorReporter)paramObject);
      if (fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null)
      {
        XMLMessageFormatter localXMLMessageFormatter = new XMLMessageFormatter();
        fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", localXMLMessageFormatter);
        fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", localXMLMessageFormatter);
      }
      fDTDScanner.setProperty(paramString, paramObject);
      fEntityManager.setProperty(paramString, paramObject);
    }
    else if (paramString.equals("http://apache.org/xml/properties/internal/error-handler"))
    {
      fErrorReporter.setProperty(paramString, paramObject);
    }
    else if (paramString.equals("http://apache.org/xml/properties/internal/entity-resolver"))
    {
      fEntityResolver = ((XMLEntityResolver)paramObject);
      fEntityManager.setProperty(paramString, paramObject);
    }
    else if (paramString.equals("http://apache.org/xml/properties/locale"))
    {
      setLocale((Locale)paramObject);
    }
    else if (paramString.equals("http://apache.org/xml/properties/internal/grammar-pool"))
    {
      fGrammarPool = ((XMLGrammarPool)paramObject);
    }
    else
    {
      throw new XMLConfigurationException((short)0, paramString);
    }
  }
  
  public boolean getFeature(String paramString)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://xml.org/sax/features/validation")) {
      return fValidation;
    }
    if (paramString.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
      return fWarnDuplicateAttdef;
    }
    if (paramString.equals("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef")) {
      return fWarnOnUndeclaredElemdef;
    }
    if (paramString.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
      return fDTDScanner.getFeature(paramString);
    }
    if (paramString.equals("http://apache.org/xml/features/standard-uri-conformant")) {
      return fStrictURI;
    }
    if (paramString.equals("http://apache.org/xml/features/validation/balance-syntax-trees")) {
      return fBalanceSyntaxTrees;
    }
    throw new XMLConfigurationException((short)0, paramString);
  }
  
  public void setLocale(Locale paramLocale)
  {
    fLocale = paramLocale;
    fErrorReporter.setLocale(paramLocale);
  }
  
  public Locale getLocale()
  {
    return fLocale;
  }
  
  public void setErrorHandler(XMLErrorHandler paramXMLErrorHandler)
  {
    fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", paramXMLErrorHandler);
  }
  
  public XMLErrorHandler getErrorHandler()
  {
    return fErrorReporter.getErrorHandler();
  }
  
  public void setEntityResolver(XMLEntityResolver paramXMLEntityResolver)
  {
    fEntityResolver = paramXMLEntityResolver;
    fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", paramXMLEntityResolver);
  }
  
  public XMLEntityResolver getEntityResolver()
  {
    return fEntityResolver;
  }
  
  public Grammar loadGrammar(XMLInputSource paramXMLInputSource)
    throws IOException, XNIException
  {
    reset();
    String str = XMLEntityManager.expandSystemId(paramXMLInputSource.getSystemId(), paramXMLInputSource.getBaseSystemId(), fStrictURI);
    XMLDTDDescription localXMLDTDDescription = new XMLDTDDescription(paramXMLInputSource.getPublicId(), paramXMLInputSource.getSystemId(), paramXMLInputSource.getBaseSystemId(), str, null);
    if (!fBalanceSyntaxTrees) {
      fDTDGrammar = new DTDGrammar(fSymbolTable, localXMLDTDDescription);
    } else {
      fDTDGrammar = new BalancedDTDGrammar(fSymbolTable, localXMLDTDDescription);
    }
    fGrammarBucket = new DTDGrammarBucket();
    fGrammarBucket.setStandalone(false);
    fGrammarBucket.setActiveGrammar(fDTDGrammar);
    try
    {
      fDTDScanner.setInputSource(paramXMLInputSource);
      fDTDScanner.scanDTDExternalSubset(true);
    }
    catch (EOFException localEOFException) {}finally
    {
      fEntityManager.closeReaders();
    }
    if ((fDTDGrammar != null) && (fGrammarPool != null)) {
      fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[] { fDTDGrammar });
    }
    return fDTDGrammar;
  }
  
  public void loadGrammarWithContext(XMLDTDValidator paramXMLDTDValidator, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws IOException, XNIException
  {
    DTDGrammarBucket localDTDGrammarBucket = paramXMLDTDValidator.getGrammarBucket();
    DTDGrammar localDTDGrammar = localDTDGrammarBucket.getActiveGrammar();
    if ((localDTDGrammar != null) && (!localDTDGrammar.isImmutable()))
    {
      fGrammarBucket = localDTDGrammarBucket;
      fEntityManager.setScannerVersion(getScannerVersion());
      reset();
      try
      {
        Object localObject1;
        XMLInputSource localXMLInputSource;
        if (paramString5 != null)
        {
          localObject1 = new StringBuffer(paramString5.length() + 2);
          ((StringBuffer)localObject1).append(paramString5).append("]>");
          localXMLInputSource = new XMLInputSource(null, paramString4, null, new StringReader(((StringBuffer)localObject1).toString()), null);
          fEntityManager.startDocumentEntity(localXMLInputSource);
          fDTDScanner.scanDTDInternalSubset(true, false, paramString3 != null);
        }
        if (paramString3 != null)
        {
          localObject1 = new XMLDTDDescription(paramString2, paramString3, paramString4, null, paramString1);
          localXMLInputSource = fEntityManager.resolveEntity((XMLResourceIdentifier)localObject1);
          fDTDScanner.setInputSource(localXMLInputSource);
          fDTDScanner.scanDTDExternalSubset(true);
        }
      }
      catch (EOFException localEOFException) {}finally
      {
        fEntityManager.closeReaders();
      }
    }
  }
  
  protected void reset()
  {
    super.reset();
    fDTDScanner.reset();
    fEntityManager.reset();
    fErrorReporter.setDocumentLocator(fEntityManager.getEntityScanner());
  }
  
  protected XMLDTDScannerImpl createDTDScanner(SymbolTable paramSymbolTable, XMLErrorReporter paramXMLErrorReporter, XMLEntityManager paramXMLEntityManager)
  {
    return new XMLDTDScannerImpl(paramSymbolTable, paramXMLErrorReporter, paramXMLEntityManager);
  }
  
  protected short getScannerVersion()
  {
    return 1;
  }
}
