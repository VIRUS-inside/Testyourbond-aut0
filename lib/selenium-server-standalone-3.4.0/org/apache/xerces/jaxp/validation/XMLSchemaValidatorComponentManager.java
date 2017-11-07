package org.apache.xerces.jaxp.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

final class XMLSchemaValidatorComponentManager
  extends ParserConfigurationSettings
  implements XMLComponentManager
{
  private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
  private static final String VALIDATION = "http://xml.org/sax/features/validation";
  private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
  private static final String IGNORE_XSI_TYPE = "http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl";
  private static final String ID_IDREF_CHECKING = "http://apache.org/xml/features/validation/id-idref-checking";
  private static final String UNPARSED_ENTITY_CHECKING = "http://apache.org/xml/features/validation/unparsed-entity-checking";
  private static final String IDENTITY_CONSTRAINT_CHECKING = "http://apache.org/xml/features/validation/identity-constraint-checking";
  private static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
  private static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
  private static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
  private static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
  private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
  private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
  private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
  private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
  private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  private static final String LOCALE = "http://apache.org/xml/properties/locale";
  private boolean fConfigUpdated = true;
  private boolean fUseGrammarPoolOnly;
  private final HashMap fComponents = new HashMap();
  private final XMLEntityManager fEntityManager = new XMLEntityManager();
  private final XMLErrorReporter fErrorReporter;
  private final NamespaceContext fNamespaceContext;
  private final XMLSchemaValidator fSchemaValidator;
  private final ValidationManager fValidationManager;
  private final HashMap fInitFeatures = new HashMap();
  private final HashMap fInitProperties = new HashMap();
  private final SecurityManager fInitSecurityManager;
  private ErrorHandler fErrorHandler = null;
  private LSResourceResolver fResourceResolver = null;
  private Locale fLocale = null;
  
  public XMLSchemaValidatorComponentManager(XSGrammarPoolContainer paramXSGrammarPoolContainer)
  {
    fComponents.put("http://apache.org/xml/properties/internal/entity-manager", fEntityManager);
    fErrorReporter = new XMLErrorReporter();
    fComponents.put("http://apache.org/xml/properties/internal/error-reporter", fErrorReporter);
    fNamespaceContext = new NamespaceSupport();
    fComponents.put("http://apache.org/xml/properties/internal/namespace-context", fNamespaceContext);
    fSchemaValidator = new XMLSchemaValidator();
    fComponents.put("http://apache.org/xml/properties/internal/validator/schema", fSchemaValidator);
    fValidationManager = new ValidationManager();
    fComponents.put("http://apache.org/xml/properties/internal/validation-manager", fValidationManager);
    fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
    fComponents.put("http://apache.org/xml/properties/internal/error-handler", null);
    fComponents.put("http://apache.org/xml/properties/security-manager", null);
    fComponents.put("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable());
    fComponents.put("http://apache.org/xml/properties/internal/grammar-pool", paramXSGrammarPoolContainer.getGrammarPool());
    fUseGrammarPoolOnly = paramXSGrammarPoolContainer.isFullyComposed();
    fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
    String[] arrayOfString = { "http://apache.org/xml/features/disallow-doctype-decl", "http://apache.org/xml/features/validation/schema/normalized-value", "http://apache.org/xml/features/validation/schema/element-default", "http://apache.org/xml/features/validation/schema/augment-psvi" };
    addRecognizedFeatures(arrayOfString);
    fFeatures.put("http://apache.org/xml/features/disallow-doctype-decl", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/validation/schema/normalized-value", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/validation/schema/element-default", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/validation/schema/augment-psvi", Boolean.TRUE);
    addRecognizedParamsAndSetDefaults(fEntityManager, paramXSGrammarPoolContainer);
    addRecognizedParamsAndSetDefaults(fErrorReporter, paramXSGrammarPoolContainer);
    addRecognizedParamsAndSetDefaults(fSchemaValidator, paramXSGrammarPoolContainer);
    Boolean localBoolean = paramXSGrammarPoolContainer.getFeature("http://javax.xml.XMLConstants/feature/secure-processing");
    if (Boolean.TRUE.equals(localBoolean)) {
      fInitSecurityManager = new SecurityManager();
    } else {
      fInitSecurityManager = null;
    }
    fComponents.put("http://apache.org/xml/properties/security-manager", fInitSecurityManager);
    fFeatures.put("http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl", Boolean.FALSE);
    fFeatures.put("http://apache.org/xml/features/validation/id-idref-checking", Boolean.TRUE);
    fFeatures.put("http://apache.org/xml/features/validation/identity-constraint-checking", Boolean.TRUE);
    fFeatures.put("http://apache.org/xml/features/validation/unparsed-entity-checking", Boolean.TRUE);
  }
  
  public boolean getFeature(String paramString)
    throws XMLConfigurationException
  {
    if ("http://apache.org/xml/features/internal/parser-settings".equals(paramString)) {
      return fConfigUpdated;
    }
    if (("http://xml.org/sax/features/validation".equals(paramString)) || ("http://apache.org/xml/features/validation/schema".equals(paramString))) {
      return true;
    }
    if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(paramString)) {
      return fUseGrammarPoolOnly;
    }
    if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(paramString)) {
      return getProperty("http://apache.org/xml/properties/security-manager") != null;
    }
    return super.getFeature(paramString);
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    if ("http://apache.org/xml/features/internal/parser-settings".equals(paramString)) {
      throw new XMLConfigurationException((short)1, paramString);
    }
    if ((!paramBoolean) && (("http://xml.org/sax/features/validation".equals(paramString)) || ("http://apache.org/xml/features/validation/schema".equals(paramString)))) {
      throw new XMLConfigurationException((short)1, paramString);
    }
    if (("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(paramString)) && (paramBoolean != fUseGrammarPoolOnly)) {
      throw new XMLConfigurationException((short)1, paramString);
    }
    if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(paramString))
    {
      setProperty("http://apache.org/xml/properties/security-manager", paramBoolean ? new SecurityManager() : null);
      return;
    }
    fConfigUpdated = true;
    fEntityManager.setFeature(paramString, paramBoolean);
    fErrorReporter.setFeature(paramString, paramBoolean);
    fSchemaValidator.setFeature(paramString, paramBoolean);
    if (!fInitFeatures.containsKey(paramString))
    {
      boolean bool = super.getFeature(paramString);
      fInitFeatures.put(paramString, bool ? Boolean.TRUE : Boolean.FALSE);
    }
    super.setFeature(paramString, paramBoolean);
  }
  
  public Object getProperty(String paramString)
    throws XMLConfigurationException
  {
    if ("http://apache.org/xml/properties/locale".equals(paramString)) {
      return getLocale();
    }
    Object localObject = fComponents.get(paramString);
    if (localObject != null) {
      return localObject;
    }
    if (fComponents.containsKey(paramString)) {
      return null;
    }
    return super.getProperty(paramString);
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    if (("http://apache.org/xml/properties/internal/entity-manager".equals(paramString)) || ("http://apache.org/xml/properties/internal/error-reporter".equals(paramString)) || ("http://apache.org/xml/properties/internal/namespace-context".equals(paramString)) || ("http://apache.org/xml/properties/internal/validator/schema".equals(paramString)) || ("http://apache.org/xml/properties/internal/symbol-table".equals(paramString)) || ("http://apache.org/xml/properties/internal/validation-manager".equals(paramString)) || ("http://apache.org/xml/properties/internal/grammar-pool".equals(paramString))) {
      throw new XMLConfigurationException((short)1, paramString);
    }
    fConfigUpdated = true;
    fEntityManager.setProperty(paramString, paramObject);
    fErrorReporter.setProperty(paramString, paramObject);
    fSchemaValidator.setProperty(paramString, paramObject);
    if (("http://apache.org/xml/properties/internal/entity-resolver".equals(paramString)) || ("http://apache.org/xml/properties/internal/error-handler".equals(paramString)) || ("http://apache.org/xml/properties/security-manager".equals(paramString)))
    {
      fComponents.put(paramString, paramObject);
      return;
    }
    if ("http://apache.org/xml/properties/locale".equals(paramString))
    {
      setLocale((Locale)paramObject);
      fComponents.put(paramString, paramObject);
      return;
    }
    if (!fInitProperties.containsKey(paramString)) {
      fInitProperties.put(paramString, super.getProperty(paramString));
    }
    super.setProperty(paramString, paramObject);
  }
  
  public void addRecognizedParamsAndSetDefaults(XMLComponent paramXMLComponent, XSGrammarPoolContainer paramXSGrammarPoolContainer)
  {
    String[] arrayOfString1 = paramXMLComponent.getRecognizedFeatures();
    addRecognizedFeatures(arrayOfString1);
    String[] arrayOfString2 = paramXMLComponent.getRecognizedProperties();
    addRecognizedProperties(arrayOfString2);
    setFeatureDefaults(paramXMLComponent, arrayOfString1, paramXSGrammarPoolContainer);
    setPropertyDefaults(paramXMLComponent, arrayOfString2);
  }
  
  public void reset()
    throws XNIException
  {
    fNamespaceContext.reset();
    fValidationManager.reset();
    fEntityManager.reset(this);
    fErrorReporter.reset(this);
    fSchemaValidator.reset(this);
    fConfigUpdated = false;
  }
  
  void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    fErrorHandler = paramErrorHandler;
    setProperty("http://apache.org/xml/properties/internal/error-handler", paramErrorHandler != null ? new ErrorHandlerWrapper(paramErrorHandler) : new ErrorHandlerWrapper(DraconianErrorHandler.getInstance()));
  }
  
  ErrorHandler getErrorHandler()
  {
    return fErrorHandler;
  }
  
  void setResourceResolver(LSResourceResolver paramLSResourceResolver)
  {
    fResourceResolver = paramLSResourceResolver;
    setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper(paramLSResourceResolver));
  }
  
  LSResourceResolver getResourceResolver()
  {
    return fResourceResolver;
  }
  
  void setLocale(Locale paramLocale)
  {
    fLocale = paramLocale;
    fErrorReporter.setLocale(paramLocale);
  }
  
  Locale getLocale()
  {
    return fLocale;
  }
  
  void restoreInitialState()
  {
    fConfigUpdated = true;
    fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
    fComponents.put("http://apache.org/xml/properties/internal/error-handler", null);
    fComponents.put("http://apache.org/xml/properties/security-manager", fInitSecurityManager);
    setLocale(null);
    fComponents.put("http://apache.org/xml/properties/locale", null);
    Iterator localIterator;
    Map.Entry localEntry;
    String str;
    if (!fInitFeatures.isEmpty())
    {
      localIterator = fInitFeatures.entrySet().iterator();
      while (localIterator.hasNext())
      {
        localEntry = (Map.Entry)localIterator.next();
        str = (String)localEntry.getKey();
        boolean bool = ((Boolean)localEntry.getValue()).booleanValue();
        super.setFeature(str, bool);
      }
      fInitFeatures.clear();
    }
    if (!fInitProperties.isEmpty())
    {
      localIterator = fInitProperties.entrySet().iterator();
      while (localIterator.hasNext())
      {
        localEntry = (Map.Entry)localIterator.next();
        str = (String)localEntry.getKey();
        Object localObject = localEntry.getValue();
        super.setProperty(str, localObject);
      }
      fInitProperties.clear();
    }
  }
  
  private void setFeatureDefaults(XMLComponent paramXMLComponent, String[] paramArrayOfString, XSGrammarPoolContainer paramXSGrammarPoolContainer)
  {
    if (paramArrayOfString != null) {
      for (int i = 0; i < paramArrayOfString.length; i++)
      {
        String str = paramArrayOfString[i];
        Boolean localBoolean = paramXSGrammarPoolContainer.getFeature(str);
        if (localBoolean == null) {
          localBoolean = paramXMLComponent.getFeatureDefault(str);
        }
        if ((localBoolean != null) && (!fFeatures.containsKey(str)))
        {
          fFeatures.put(str, localBoolean);
          fConfigUpdated = true;
        }
      }
    }
  }
  
  private void setPropertyDefaults(XMLComponent paramXMLComponent, String[] paramArrayOfString)
  {
    if (paramArrayOfString != null) {
      for (int i = 0; i < paramArrayOfString.length; i++)
      {
        String str = paramArrayOfString[i];
        Object localObject = paramXMLComponent.getPropertyDefault(str);
        if ((localObject != null) && (!fProperties.containsKey(str)))
        {
          fProperties.put(str, localObject);
          fConfigUpdated = true;
        }
      }
    }
  }
}
