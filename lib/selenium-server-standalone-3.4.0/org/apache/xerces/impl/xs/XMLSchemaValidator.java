package org.apache.xerces.impl.xs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.DatatypeException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.validation.ConfigurableValidationState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.identity.Field;
import org.apache.xerces.impl.xs.identity.FieldActivator;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.KeyRef;
import org.apache.xerces.impl.xs.identity.Selector;
import org.apache.xerces.impl.xs.identity.Selector.Matcher;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.impl.xs.identity.ValueStore;
import org.apache.xerces.impl.xs.identity.XPathMatcher;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.CMNodeFactory;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XMLSchemaValidator
  implements XMLComponent, XMLDocumentFilter, FieldActivator, RevalidationHandler, XSElementDeclHelper
{
  private static final boolean DEBUG = false;
  protected static final String VALIDATION = "http://xml.org/sax/features/validation";
  protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
  protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
  protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
  protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
  protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
  protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
  protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
  protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
  protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
  protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
  protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
  protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
  protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
  protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
  protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
  protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
  protected static final String IGNORE_XSI_TYPE = "http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl";
  protected static final String ID_IDREF_CHECKING = "http://apache.org/xml/features/validation/id-idref-checking";
  protected static final String UNPARSED_ENTITY_CHECKING = "http://apache.org/xml/features/validation/unparsed-entity-checking";
  protected static final String IDENTITY_CONSTRAINT_CHECKING = "http://apache.org/xml/features/validation/identity-constraint-checking";
  public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
  public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
  protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
  protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
  protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  protected static final String ROOT_TYPE_DEF = "http://apache.org/xml/properties/validation/schema/root-type-definition";
  protected static final String ROOT_ELEMENT_DECL = "http://apache.org/xml/properties/validation/schema/root-element-declaration";
  protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only", "http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl", "http://apache.org/xml/features/validation/id-idref-checking", "http://apache.org/xml/features/validation/identity-constraint-checking", "http://apache.org/xml/features/validation/unparsed-entity-checking", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates" };
  private static final Boolean[] FEATURE_DEFAULTS = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/validation/schema/root-type-definition", "http://apache.org/xml/properties/validation/schema/root-element-declaration", "http://apache.org/xml/properties/internal/validation/schema/dv-factory" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, null, null, null, null, null, null, null };
  protected static final int ID_CONSTRAINT_NUM = 1;
  static final XSAttributeDecl XSI_TYPE = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_TYPE);
  static final XSAttributeDecl XSI_NIL = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NIL);
  static final XSAttributeDecl XSI_SCHEMALOCATION = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
  static final XSAttributeDecl XSI_NONAMESPACESCHEMALOCATION = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
  private static final Hashtable EMPTY_TABLE = new Hashtable();
  protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
  protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
  protected XMLString fDefaultValue;
  protected boolean fDynamicValidation = false;
  protected boolean fSchemaDynamicValidation = false;
  protected boolean fDoValidation = false;
  protected boolean fFullChecking = false;
  protected boolean fNormalizeData = true;
  protected boolean fSchemaElementDefault = true;
  protected boolean fAugPSVI = true;
  protected boolean fIdConstraint = false;
  protected boolean fUseGrammarPoolOnly = false;
  protected boolean fNamespaceGrowth = false;
  private String fSchemaType = null;
  protected boolean fEntityRef = false;
  protected boolean fInCDATA = false;
  protected SymbolTable fSymbolTable;
  private XMLLocator fLocator;
  protected final XSIErrorReporter fXSIErrorReporter = new XSIErrorReporter();
  protected XMLEntityResolver fEntityResolver;
  protected ValidationManager fValidationManager = null;
  protected ConfigurableValidationState fValidationState = new ConfigurableValidationState();
  protected XMLGrammarPool fGrammarPool;
  protected String fExternalSchemas = null;
  protected String fExternalNoNamespaceSchema = null;
  protected Object fJaxpSchemaSource = null;
  protected final XSDDescription fXSDDescription = new XSDDescription();
  protected final Hashtable fLocationPairs = new Hashtable();
  protected final Hashtable fExpandedLocationPairs = new Hashtable();
  protected final ArrayList fUnparsedLocations = new ArrayList();
  protected XMLDocumentHandler fDocumentHandler;
  protected XMLDocumentSource fDocumentSource;
  static final int INITIAL_STACK_SIZE = 8;
  static final int INC_STACK_SIZE = 8;
  private static final boolean DEBUG_NORMALIZATION = false;
  private final XMLString fEmptyXMLStr = new XMLString(null, 0, -1);
  private static final int BUFFER_SIZE = 20;
  private final XMLString fNormalizedStr = new XMLString();
  private boolean fFirstChunk = true;
  private boolean fTrailing = false;
  private short fWhiteSpace = -1;
  private boolean fUnionType = false;
  private final XSGrammarBucket fGrammarBucket = new XSGrammarBucket();
  private final SubstitutionGroupHandler fSubGroupHandler = new SubstitutionGroupHandler(this);
  private final XSSimpleType fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
  private final CMNodeFactory nodeFactory = new CMNodeFactory();
  private final CMBuilder fCMBuilder = new CMBuilder(nodeFactory);
  private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader(fXSIErrorReporter.fErrorReporter, fGrammarBucket, fSubGroupHandler, fCMBuilder);
  private String fValidationRoot;
  private int fSkipValidationDepth;
  private int fNFullValidationDepth;
  private int fNNoneValidationDepth;
  private int fElementDepth;
  private boolean fSubElement;
  private boolean[] fSubElementStack = new boolean[8];
  private XSElementDecl fCurrentElemDecl;
  private XSElementDecl[] fElemDeclStack = new XSElementDecl[8];
  private boolean fNil;
  private boolean[] fNilStack = new boolean[8];
  private XSNotationDecl fNotation;
  private XSNotationDecl[] fNotationStack = new XSNotationDecl[8];
  private XSTypeDefinition fCurrentType;
  private XSTypeDefinition[] fTypeStack = new XSTypeDefinition[8];
  private XSCMValidator fCurrentCM;
  private XSCMValidator[] fCMStack = new XSCMValidator[8];
  private int[] fCurrCMState;
  private int[][] fCMStateStack = new int[8][];
  private boolean fStrictAssess = true;
  private boolean[] fStrictAssessStack = new boolean[8];
  private final StringBuffer fBuffer = new StringBuffer();
  private boolean fAppendBuffer = true;
  private boolean fSawText = false;
  private boolean[] fSawTextStack = new boolean[8];
  private boolean fSawCharacters = false;
  private boolean[] fStringContent = new boolean[8];
  private final org.apache.xerces.xni.QName fTempQName = new org.apache.xerces.xni.QName();
  private javax.xml.namespace.QName fRootTypeQName = null;
  private XSTypeDefinition fRootTypeDefinition = null;
  private javax.xml.namespace.QName fRootElementDeclQName = null;
  private XSElementDecl fRootElementDeclaration = null;
  private int fIgnoreXSITypeDepth;
  private boolean fIDCChecking;
  private ValidatedInfo fValidatedInfo = new ValidatedInfo();
  private ValidationState fState4XsiType = new ValidationState();
  private ValidationState fState4ApplyDefault = new ValidationState();
  protected XPathMatcherStack fMatcherStack = new XPathMatcherStack();
  protected ValueStoreCache fValueStoreCache = new ValueStoreCache();
  
  public String[] getRecognizedFeatures()
  {
    return (String[])RECOGNIZED_FEATURES.clone();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {}
  
  public String[] getRecognizedProperties()
  {
    return (String[])RECOGNIZED_PROPERTIES.clone();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://apache.org/xml/properties/validation/schema/root-type-definition"))
    {
      if (paramObject == null)
      {
        fRootTypeQName = null;
        fRootTypeDefinition = null;
      }
      else if ((paramObject instanceof javax.xml.namespace.QName))
      {
        fRootTypeQName = ((javax.xml.namespace.QName)paramObject);
        fRootTypeDefinition = null;
      }
      else
      {
        fRootTypeDefinition = ((XSTypeDefinition)paramObject);
        fRootTypeQName = null;
      }
    }
    else if (paramString.equals("http://apache.org/xml/properties/validation/schema/root-element-declaration")) {
      if (paramObject == null)
      {
        fRootElementDeclQName = null;
        fRootElementDeclaration = null;
      }
      else if ((paramObject instanceof javax.xml.namespace.QName))
      {
        fRootElementDeclQName = ((javax.xml.namespace.QName)paramObject);
        fRootElementDeclaration = null;
      }
      else
      {
        fRootElementDeclaration = ((XSElementDecl)paramObject);
        fRootElementDeclQName = null;
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
  
  public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource)
  {
    fDocumentSource = paramXMLDocumentSource;
  }
  
  public XMLDocumentSource getDocumentSource()
  {
    return fDocumentSource;
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    fValidationState.setNamespaceSupport(paramNamespaceContext);
    fState4XsiType.setNamespaceSupport(paramNamespaceContext);
    fState4ApplyDefault.setNamespaceSupport(paramNamespaceContext);
    fLocator = paramXMLLocator;
    handleStartDocument(paramXMLLocator, paramString);
    if (fDocumentHandler != null) {
      fDocumentHandler.startDocument(paramXMLLocator, paramString, paramNamespaceContext, paramAugmentations);
    }
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.xmlDecl(paramString1, paramString2, paramString3, paramAugmentations);
    }
  }
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.doctypeDecl(paramString1, paramString2, paramString3, paramAugmentations);
    }
  }
  
  public void startElement(org.apache.xerces.xni.QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    Augmentations localAugmentations = handleStartElement(paramQName, paramXMLAttributes, paramAugmentations);
    if (fDocumentHandler != null) {
      fDocumentHandler.startElement(paramQName, paramXMLAttributes, localAugmentations);
    }
  }
  
  public void emptyElement(org.apache.xerces.xni.QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    Augmentations localAugmentations = handleStartElement(paramQName, paramXMLAttributes, paramAugmentations);
    fDefaultValue = null;
    if (fElementDepth != -2) {
      localAugmentations = handleEndElement(paramQName, localAugmentations);
    }
    if (fDocumentHandler != null) {
      if ((!fSchemaElementDefault) || (fDefaultValue == null))
      {
        fDocumentHandler.emptyElement(paramQName, paramXMLAttributes, localAugmentations);
      }
      else
      {
        fDocumentHandler.startElement(paramQName, paramXMLAttributes, localAugmentations);
        fDocumentHandler.characters(fDefaultValue, null);
        fDocumentHandler.endElement(paramQName, localAugmentations);
      }
    }
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    paramXMLString = handleCharacters(paramXMLString);
    if (fDocumentHandler != null) {
      if ((fNormalizeData) && (fUnionType))
      {
        if (paramAugmentations != null) {
          fDocumentHandler.characters(fEmptyXMLStr, paramAugmentations);
        }
      }
      else {
        fDocumentHandler.characters(paramXMLString, paramAugmentations);
      }
    }
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    handleIgnorableWhitespace(paramXMLString);
    if (fDocumentHandler != null) {
      fDocumentHandler.ignorableWhitespace(paramXMLString, paramAugmentations);
    }
  }
  
  public void endElement(org.apache.xerces.xni.QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    fDefaultValue = null;
    Augmentations localAugmentations = handleEndElement(paramQName, paramAugmentations);
    if (fDocumentHandler != null) {
      if ((!fSchemaElementDefault) || (fDefaultValue == null))
      {
        fDocumentHandler.endElement(paramQName, localAugmentations);
      }
      else
      {
        fDocumentHandler.characters(fDefaultValue, null);
        fDocumentHandler.endElement(paramQName, localAugmentations);
      }
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATA = true;
    if (fDocumentHandler != null) {
      fDocumentHandler.startCDATA(paramAugmentations);
    }
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATA = false;
    if (fDocumentHandler != null) {
      fDocumentHandler.endCDATA(paramAugmentations);
    }
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {
    handleEndDocument();
    if (fDocumentHandler != null) {
      fDocumentHandler.endDocument(paramAugmentations);
    }
    fLocator = null;
  }
  
  public boolean characterData(String paramString, Augmentations paramAugmentations)
  {
    fSawText = ((fSawText) || (paramString.length() > 0));
    if ((fNormalizeData) && (fWhiteSpace != -1) && (fWhiteSpace != 0))
    {
      normalizeWhitespace(paramString, fWhiteSpace == 2);
      fBuffer.append(fNormalizedStr.ch, fNormalizedStr.offset, fNormalizedStr.length);
    }
    else if (fAppendBuffer)
    {
      fBuffer.append(paramString);
    }
    boolean bool = true;
    if ((fCurrentType != null) && (fCurrentType.getTypeCategory() == 15))
    {
      XSComplexTypeDecl localXSComplexTypeDecl = (XSComplexTypeDecl)fCurrentType;
      if (fContentType == 2) {
        for (int i = 0; i < paramString.length(); i++) {
          if (!XMLChar.isSpace(paramString.charAt(i)))
          {
            bool = false;
            fSawCharacters = true;
            break;
          }
        }
      }
    }
    return bool;
  }
  
  public void elementDefault(String paramString) {}
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    fEntityRef = true;
    if (fDocumentHandler != null) {
      fDocumentHandler.startGeneralEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.textDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.comment(paramXMLString, paramAugmentations);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.processingInstruction(paramString, paramXMLString, paramAugmentations);
    }
  }
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    fEntityRef = false;
    if (fDocumentHandler != null) {
      fDocumentHandler.endGeneralEntity(paramString, paramAugmentations);
    }
  }
  
  public XMLSchemaValidator()
  {
    fState4XsiType.setExtraChecking(false);
    fState4ApplyDefault.setFacetChecking(false);
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    fIdConstraint = false;
    fLocationPairs.clear();
    fExpandedLocationPairs.clear();
    fValidationState.resetIDTables();
    fSchemaLoader.reset(paramXMLComponentManager);
    fCurrentElemDecl = null;
    fCurrentCM = null;
    fCurrCMState = null;
    fSkipValidationDepth = -1;
    fNFullValidationDepth = -1;
    fNNoneValidationDepth = -1;
    fElementDepth = -1;
    fSubElement = false;
    fSchemaDynamicValidation = false;
    fEntityRef = false;
    fInCDATA = false;
    fMatcherStack.clear();
    fXSIErrorReporter.reset((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    boolean bool1;
    try
    {
      bool1 = paramXMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
    }
    catch (XMLConfigurationException localXMLConfigurationException1)
    {
      bool1 = true;
    }
    if (!bool1)
    {
      fValidationManager.addValidationState(fValidationState);
      nodeFactory.reset();
      XMLSchemaLoader.processExternalHints(fExternalSchemas, fExternalNoNamespaceSchema, fLocationPairs, fXSIErrorReporter.fErrorReporter);
      return;
    }
    nodeFactory.reset(paramXMLComponentManager);
    SymbolTable localSymbolTable = (SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
    if (localSymbolTable != fSymbolTable) {
      fSymbolTable = localSymbolTable;
    }
    try
    {
      fNamespaceGrowth = paramXMLComponentManager.getFeature("http://apache.org/xml/features/namespace-growth");
    }
    catch (XMLConfigurationException localXMLConfigurationException2)
    {
      fNamespaceGrowth = false;
    }
    try
    {
      fDynamicValidation = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/dynamic");
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fDynamicValidation = false;
    }
    if (fDynamicValidation) {
      fDoValidation = true;
    } else {
      try
      {
        fDoValidation = paramXMLComponentManager.getFeature("http://xml.org/sax/features/validation");
      }
      catch (XMLConfigurationException localXMLConfigurationException4)
      {
        fDoValidation = false;
      }
    }
    if (fDoValidation) {
      try
      {
        fDoValidation = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema");
      }
      catch (XMLConfigurationException localXMLConfigurationException5) {}
    }
    try
    {
      fFullChecking = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema-full-checking");
    }
    catch (XMLConfigurationException localXMLConfigurationException6)
    {
      fFullChecking = false;
    }
    try
    {
      fNormalizeData = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/normalized-value");
    }
    catch (XMLConfigurationException localXMLConfigurationException7)
    {
      fNormalizeData = false;
    }
    try
    {
      fSchemaElementDefault = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/element-default");
    }
    catch (XMLConfigurationException localXMLConfigurationException8)
    {
      fSchemaElementDefault = false;
    }
    try
    {
      fAugPSVI = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/augment-psvi");
    }
    catch (XMLConfigurationException localXMLConfigurationException9)
    {
      fAugPSVI = true;
    }
    try
    {
      fSchemaType = ((String)paramXMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage"));
    }
    catch (XMLConfigurationException localXMLConfigurationException10)
    {
      fSchemaType = null;
    }
    try
    {
      fUseGrammarPoolOnly = paramXMLComponentManager.getFeature("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only");
    }
    catch (XMLConfigurationException localXMLConfigurationException11)
    {
      fUseGrammarPoolOnly = false;
    }
    fEntityResolver = ((XMLEntityResolver)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
    fValidationManager = ((ValidationManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
    fValidationManager.addValidationState(fValidationState);
    fValidationState.setSymbolTable(fSymbolTable);
    try
    {
      Object localObject1 = paramXMLComponentManager.getProperty("http://apache.org/xml/properties/validation/schema/root-type-definition");
      if (localObject1 == null)
      {
        fRootTypeQName = null;
        fRootTypeDefinition = null;
      }
      else if ((localObject1 instanceof javax.xml.namespace.QName))
      {
        fRootTypeQName = ((javax.xml.namespace.QName)localObject1);
        fRootTypeDefinition = null;
      }
      else
      {
        fRootTypeDefinition = ((XSTypeDefinition)localObject1);
        fRootTypeQName = null;
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException12)
    {
      fRootTypeQName = null;
      fRootTypeDefinition = null;
    }
    try
    {
      Object localObject2 = paramXMLComponentManager.getProperty("http://apache.org/xml/properties/validation/schema/root-element-declaration");
      if (localObject2 == null)
      {
        fRootElementDeclQName = null;
        fRootElementDeclaration = null;
      }
      else if ((localObject2 instanceof javax.xml.namespace.QName))
      {
        fRootElementDeclQName = ((javax.xml.namespace.QName)localObject2);
        fRootElementDeclaration = null;
      }
      else
      {
        fRootElementDeclaration = ((XSElementDecl)localObject2);
        fRootElementDeclQName = null;
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException13)
    {
      fRootElementDeclQName = null;
      fRootElementDeclaration = null;
    }
    boolean bool2;
    try
    {
      bool2 = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl");
    }
    catch (XMLConfigurationException localXMLConfigurationException14)
    {
      bool2 = false;
    }
    fIgnoreXSITypeDepth = (bool2 ? 0 : -1);
    try
    {
      fIDCChecking = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/identity-constraint-checking");
    }
    catch (XMLConfigurationException localXMLConfigurationException15)
    {
      fIDCChecking = true;
    }
    try
    {
      fValidationState.setIdIdrefChecking(paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/id-idref-checking"));
    }
    catch (XMLConfigurationException localXMLConfigurationException16)
    {
      fValidationState.setIdIdrefChecking(true);
    }
    try
    {
      fValidationState.setUnparsedEntityChecking(paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/unparsed-entity-checking"));
    }
    catch (XMLConfigurationException localXMLConfigurationException17)
    {
      fValidationState.setUnparsedEntityChecking(true);
    }
    try
    {
      fExternalSchemas = ((String)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/schema/external-schemaLocation"));
      fExternalNoNamespaceSchema = ((String)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation"));
    }
    catch (XMLConfigurationException localXMLConfigurationException18)
    {
      fExternalSchemas = null;
      fExternalNoNamespaceSchema = null;
    }
    XMLSchemaLoader.processExternalHints(fExternalSchemas, fExternalNoNamespaceSchema, fLocationPairs, fXSIErrorReporter.fErrorReporter);
    try
    {
      fJaxpSchemaSource = paramXMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
    }
    catch (XMLConfigurationException localXMLConfigurationException19)
    {
      fJaxpSchemaSource = null;
    }
    try
    {
      fGrammarPool = ((XMLGrammarPool)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool"));
    }
    catch (XMLConfigurationException localXMLConfigurationException20)
    {
      fGrammarPool = null;
    }
    fState4XsiType.setSymbolTable(localSymbolTable);
    fState4ApplyDefault.setSymbolTable(localSymbolTable);
  }
  
  public void startValueScopeFor(IdentityConstraint paramIdentityConstraint, int paramInt)
  {
    ValueStoreBase localValueStoreBase = fValueStoreCache.getValueStoreFor(paramIdentityConstraint, paramInt);
    localValueStoreBase.startValueScope();
  }
  
  public XPathMatcher activateField(Field paramField, int paramInt)
  {
    ValueStoreBase localValueStoreBase = fValueStoreCache.getValueStoreFor(paramField.getIdentityConstraint(), paramInt);
    XPathMatcher localXPathMatcher = paramField.createMatcher(localValueStoreBase);
    fMatcherStack.addMatcher(localXPathMatcher);
    localXPathMatcher.startDocumentFragment();
    return localXPathMatcher;
  }
  
  public void endValueScopeFor(IdentityConstraint paramIdentityConstraint, int paramInt)
  {
    ValueStoreBase localValueStoreBase = fValueStoreCache.getValueStoreFor(paramIdentityConstraint, paramInt);
    localValueStoreBase.endValueScope();
  }
  
  private void activateSelectorFor(IdentityConstraint paramIdentityConstraint)
  {
    Selector localSelector = paramIdentityConstraint.getSelector();
    XMLSchemaValidator localXMLSchemaValidator = this;
    if (localSelector == null) {
      return;
    }
    XPathMatcher localXPathMatcher = localSelector.createMatcher(localXMLSchemaValidator, fElementDepth);
    fMatcherStack.addMatcher(localXPathMatcher);
    localXPathMatcher.startDocumentFragment();
  }
  
  public XSElementDecl getGlobalElementDecl(org.apache.xerces.xni.QName paramQName)
  {
    SchemaGrammar localSchemaGrammar = findSchemaGrammar((short)5, uri, null, paramQName, null);
    if (localSchemaGrammar != null) {
      return localSchemaGrammar.getGlobalElementDecl(localpart);
    }
    return null;
  }
  
  void ensureStackCapacity()
  {
    if (fElementDepth == fElemDeclStack.length)
    {
      int i = fElementDepth + 8;
      boolean[] arrayOfBoolean = new boolean[i];
      System.arraycopy(fSubElementStack, 0, arrayOfBoolean, 0, fElementDepth);
      fSubElementStack = arrayOfBoolean;
      XSElementDecl[] arrayOfXSElementDecl = new XSElementDecl[i];
      System.arraycopy(fElemDeclStack, 0, arrayOfXSElementDecl, 0, fElementDepth);
      fElemDeclStack = arrayOfXSElementDecl;
      arrayOfBoolean = new boolean[i];
      System.arraycopy(fNilStack, 0, arrayOfBoolean, 0, fElementDepth);
      fNilStack = arrayOfBoolean;
      XSNotationDecl[] arrayOfXSNotationDecl = new XSNotationDecl[i];
      System.arraycopy(fNotationStack, 0, arrayOfXSNotationDecl, 0, fElementDepth);
      fNotationStack = arrayOfXSNotationDecl;
      XSTypeDefinition[] arrayOfXSTypeDefinition = new XSTypeDefinition[i];
      System.arraycopy(fTypeStack, 0, arrayOfXSTypeDefinition, 0, fElementDepth);
      fTypeStack = arrayOfXSTypeDefinition;
      XSCMValidator[] arrayOfXSCMValidator = new XSCMValidator[i];
      System.arraycopy(fCMStack, 0, arrayOfXSCMValidator, 0, fElementDepth);
      fCMStack = arrayOfXSCMValidator;
      arrayOfBoolean = new boolean[i];
      System.arraycopy(fSawTextStack, 0, arrayOfBoolean, 0, fElementDepth);
      fSawTextStack = arrayOfBoolean;
      arrayOfBoolean = new boolean[i];
      System.arraycopy(fStringContent, 0, arrayOfBoolean, 0, fElementDepth);
      fStringContent = arrayOfBoolean;
      arrayOfBoolean = new boolean[i];
      System.arraycopy(fStrictAssessStack, 0, arrayOfBoolean, 0, fElementDepth);
      fStrictAssessStack = arrayOfBoolean;
      int[][] arrayOfInt = new int[i][];
      System.arraycopy(fCMStateStack, 0, arrayOfInt, 0, fElementDepth);
      fCMStateStack = arrayOfInt;
    }
  }
  
  void handleStartDocument(XMLLocator paramXMLLocator, String paramString)
  {
    if (fIDCChecking) {
      fValueStoreCache.startDocument();
    }
    if (fAugPSVI)
    {
      fCurrentPSVI.fGrammars = null;
      fCurrentPSVI.fSchemaInformation = null;
    }
  }
  
  void handleEndDocument()
  {
    if (fIDCChecking) {
      fValueStoreCache.endDocument();
    }
  }
  
  XMLString handleCharacters(XMLString paramXMLString)
  {
    if (fSkipValidationDepth >= 0) {
      return paramXMLString;
    }
    fSawText = ((fSawText) || (length > 0));
    if ((fNormalizeData) && (fWhiteSpace != -1) && (fWhiteSpace != 0))
    {
      normalizeWhitespace(paramXMLString, fWhiteSpace == 2);
      paramXMLString = fNormalizedStr;
    }
    if (fAppendBuffer) {
      fBuffer.append(ch, offset, length);
    }
    if ((fCurrentType != null) && (fCurrentType.getTypeCategory() == 15))
    {
      XSComplexTypeDecl localXSComplexTypeDecl = (XSComplexTypeDecl)fCurrentType;
      if (fContentType == 2) {
        for (int i = offset; i < offset + length; i++) {
          if (!XMLChar.isSpace(ch[i]))
          {
            fSawCharacters = true;
            break;
          }
        }
      }
    }
    return paramXMLString;
  }
  
  private void normalizeWhitespace(XMLString paramXMLString, boolean paramBoolean)
  {
    boolean bool1 = paramBoolean;
    int i = 0;
    int j = 0;
    boolean bool2 = false;
    int m = offset + length;
    if ((fNormalizedStr.ch == null) || (fNormalizedStr.ch.length < length + 1)) {
      fNormalizedStr.ch = new char[length + 1];
    }
    fNormalizedStr.offset = 1;
    fNormalizedStr.length = 1;
    for (int n = offset; n < m; n++)
    {
      int k = ch[n];
      if (XMLChar.isSpace(k))
      {
        if (!bool1)
        {
          fNormalizedStr.ch[(fNormalizedStr.length++)] = ' ';
          bool1 = paramBoolean;
        }
        if (i == 0) {
          j = 1;
        }
      }
      else
      {
        fNormalizedStr.ch[(fNormalizedStr.length++)] = k;
        bool1 = false;
        i = 1;
      }
    }
    if (bool1) {
      if (fNormalizedStr.length > 1)
      {
        fNormalizedStr.length -= 1;
        bool2 = true;
      }
      else if ((j != 0) && (!fFirstChunk))
      {
        bool2 = true;
      }
    }
    if ((fNormalizedStr.length > 1) && (!fFirstChunk) && (fWhiteSpace == 2)) {
      if (fTrailing)
      {
        fNormalizedStr.offset = 0;
        fNormalizedStr.ch[0] = ' ';
      }
      else if (j != 0)
      {
        fNormalizedStr.offset = 0;
        fNormalizedStr.ch[0] = ' ';
      }
    }
    fNormalizedStr.length -= fNormalizedStr.offset;
    fTrailing = bool2;
    if ((bool2) || (i != 0)) {
      fFirstChunk = false;
    }
  }
  
  private void normalizeWhitespace(String paramString, boolean paramBoolean)
  {
    boolean bool = paramBoolean;
    int j = paramString.length();
    if ((fNormalizedStr.ch == null) || (fNormalizedStr.ch.length < j)) {
      fNormalizedStr.ch = new char[j];
    }
    fNormalizedStr.offset = 0;
    fNormalizedStr.length = 0;
    for (int k = 0; k < j; k++)
    {
      int i = paramString.charAt(k);
      if (XMLChar.isSpace(i))
      {
        if (!bool)
        {
          fNormalizedStr.ch[(fNormalizedStr.length++)] = ' ';
          bool = paramBoolean;
        }
      }
      else
      {
        fNormalizedStr.ch[(fNormalizedStr.length++)] = i;
        bool = false;
      }
    }
    if ((bool) && (fNormalizedStr.length != 0)) {
      fNormalizedStr.length -= 1;
    }
  }
  
  void handleIgnorableWhitespace(XMLString paramXMLString)
  {
    if (fSkipValidationDepth >= 0) {}
  }
  
  Augmentations handleStartElement(org.apache.xerces.xni.QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
  {
    if ((fElementDepth == -1) && (fValidationManager.isGrammarFound()) && (fSchemaType == null)) {
      fSchemaDynamicValidation = true;
    }
    if (!fUseGrammarPoolOnly)
    {
      localObject1 = paramXMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_SCHEMALOCATION);
      localObject2 = paramXMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
      storeLocations((String)localObject1, (String)localObject2);
    }
    if (fSkipValidationDepth >= 0)
    {
      fElementDepth += 1;
      if (fAugPSVI) {
        paramAugmentations = getEmptyAugs(paramAugmentations);
      }
      return paramAugmentations;
    }
    Object localObject1 = null;
    if (fCurrentCM != null)
    {
      localObject1 = fCurrentCM.oneTransition(paramQName, fCurrCMState, fSubGroupHandler);
      if (fCurrCMState[0] == -1)
      {
        localObject2 = (XSComplexTypeDecl)fCurrentType;
        int j;
        if ((fParticle != null) && ((localObject3 = fCurrentCM.whatCanGoHere(fCurrCMState)).size() > 0))
        {
          localObject4 = expectedStr((Vector)localObject3);
          int[] arrayOfInt = fCurrentCM.occurenceInfo(fCurrCMState);
          if (arrayOfInt != null)
          {
            j = arrayOfInt[0];
            m = arrayOfInt[1];
            int n = arrayOfInt[2];
            if (n < j)
            {
              int i1 = j - n;
              if (i1 > 1) {
                reportSchemaError("cvc-complex-type.2.4.h", new Object[] { rawname, fCurrentCM.getTermName(arrayOfInt[3]), Integer.toString(j), Integer.toString(i1) });
              } else {
                reportSchemaError("cvc-complex-type.2.4.g", new Object[] { rawname, fCurrentCM.getTermName(arrayOfInt[3]), Integer.toString(j) });
              }
            }
            else if ((n >= m) && (m != -1))
            {
              reportSchemaError("cvc-complex-type.2.4.e", new Object[] { rawname, localObject4, Integer.toString(m) });
            }
            else
            {
              reportSchemaError("cvc-complex-type.2.4.a", new Object[] { rawname, localObject4 });
            }
          }
          else
          {
            reportSchemaError("cvc-complex-type.2.4.a", new Object[] { rawname, localObject4 });
          }
        }
        else
        {
          localObject4 = fCurrentCM.occurenceInfo(fCurrCMState);
          if (localObject4 != null)
          {
            int i = localObject4[1];
            j = localObject4[2];
            if ((j >= i) && (i != -1)) {
              reportSchemaError("cvc-complex-type.2.4.f", new Object[] { rawname, Integer.toString(i) });
            } else {
              reportSchemaError("cvc-complex-type.2.4.d", new Object[] { rawname });
            }
          }
          else
          {
            reportSchemaError("cvc-complex-type.2.4.d", new Object[] { rawname });
          }
        }
      }
    }
    if (fElementDepth != -1)
    {
      ensureStackCapacity();
      fSubElementStack[fElementDepth] = true;
      fSubElement = false;
      fElemDeclStack[fElementDepth] = fCurrentElemDecl;
      fNilStack[fElementDepth] = fNil;
      fNotationStack[fElementDepth] = fNotation;
      fTypeStack[fElementDepth] = fCurrentType;
      fStrictAssessStack[fElementDepth] = fStrictAssess;
      fCMStack[fElementDepth] = fCurrentCM;
      fCMStateStack[fElementDepth] = fCurrCMState;
      fSawTextStack[fElementDepth] = fSawText;
      fStringContent[fElementDepth] = fSawCharacters;
    }
    fElementDepth += 1;
    fCurrentElemDecl = null;
    Object localObject2 = null;
    fCurrentType = null;
    fStrictAssess = true;
    fNil = false;
    fNotation = null;
    fBuffer.setLength(0);
    fSawText = false;
    fSawCharacters = false;
    if (localObject1 != null) {
      if ((localObject1 instanceof XSElementDecl)) {
        fCurrentElemDecl = ((XSElementDecl)localObject1);
      } else {
        localObject2 = (XSWildcardDecl)localObject1;
      }
    }
    if ((localObject2 != null) && (fProcessContents == 2))
    {
      fSkipValidationDepth = fElementDepth;
      if (fAugPSVI) {
        paramAugmentations = getEmptyAugs(paramAugmentations);
      }
      return paramAugmentations;
    }
    if (fElementDepth == 0) {
      if (fRootElementDeclaration != null)
      {
        fCurrentElemDecl = fRootElementDeclaration;
        checkElementMatchesRootElementDecl(fCurrentElemDecl, paramQName);
      }
      else if (fRootElementDeclQName != null)
      {
        processRootElementDeclQName(fRootElementDeclQName, paramQName);
      }
      else if (fRootTypeDefinition != null)
      {
        fCurrentType = fRootTypeDefinition;
      }
      else if (fRootTypeQName != null)
      {
        processRootTypeQName(fRootTypeQName);
      }
    }
    if (fCurrentType == null)
    {
      if (fCurrentElemDecl == null)
      {
        localObject3 = findSchemaGrammar((short)5, uri, null, paramQName, paramXMLAttributes);
        if (localObject3 != null) {
          fCurrentElemDecl = ((SchemaGrammar)localObject3).getGlobalElementDecl(localpart);
        }
      }
      if (fCurrentElemDecl != null) {
        fCurrentType = fCurrentElemDecl.fType;
      }
    }
    if ((fElementDepth == fIgnoreXSITypeDepth) && (fCurrentElemDecl == null)) {
      fIgnoreXSITypeDepth += 1;
    }
    Object localObject3 = null;
    if (fElementDepth >= fIgnoreXSITypeDepth) {
      localObject3 = paramXMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_TYPE);
    }
    if ((fCurrentType == null) && (localObject3 == null))
    {
      if (fElementDepth == 0)
      {
        if ((fDynamicValidation) || (fSchemaDynamicValidation))
        {
          if (fDocumentSource != null)
          {
            fDocumentSource.setDocumentHandler(fDocumentHandler);
            if (fDocumentHandler != null) {
              fDocumentHandler.setDocumentSource(fDocumentSource);
            }
            fElementDepth = -2;
            return paramAugmentations;
          }
          fSkipValidationDepth = fElementDepth;
          if (fAugPSVI) {
            paramAugmentations = getEmptyAugs(paramAugmentations);
          }
          return paramAugmentations;
        }
        fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "cvc-elt.1.a", new Object[] { rawname }, (short)1);
      }
      else if ((localObject2 != null) && (fProcessContents == 1))
      {
        reportSchemaError("cvc-complex-type.2.4.c", new Object[] { rawname });
      }
      fCurrentType = SchemaGrammar.fAnyType;
      fStrictAssess = false;
      fNFullValidationDepth = fElementDepth;
      fAppendBuffer = false;
      fXSIErrorReporter.pushContext();
    }
    else
    {
      fXSIErrorReporter.pushContext();
      if (localObject3 != null)
      {
        localObject4 = fCurrentType;
        fCurrentType = getAndCheckXsiType(paramQName, (String)localObject3, paramXMLAttributes);
        if (fCurrentType == null) {
          if (localObject4 == null) {
            fCurrentType = SchemaGrammar.fAnyType;
          } else {
            fCurrentType = ((XSTypeDefinition)localObject4);
          }
        }
      }
      fNNoneValidationDepth = fElementDepth;
      if ((fCurrentElemDecl != null) && (fCurrentElemDecl.getConstraintType() == 2))
      {
        fAppendBuffer = true;
      }
      else if (fCurrentType.getTypeCategory() == 16)
      {
        fAppendBuffer = true;
      }
      else
      {
        localObject4 = (XSComplexTypeDecl)fCurrentType;
        fAppendBuffer = (fContentType == 1);
      }
    }
    if ((fCurrentElemDecl != null) && (fCurrentElemDecl.getAbstract())) {
      reportSchemaError("cvc-elt.2", new Object[] { rawname });
    }
    if (fElementDepth == 0) {
      fValidationRoot = rawname;
    }
    if (fNormalizeData)
    {
      fFirstChunk = true;
      fTrailing = false;
      fUnionType = false;
      fWhiteSpace = -1;
    }
    if (fCurrentType.getTypeCategory() == 15)
    {
      localObject4 = (XSComplexTypeDecl)fCurrentType;
      if (((XSComplexTypeDecl)localObject4).getAbstract()) {
        reportSchemaError("cvc-type.2", new Object[] { rawname });
      }
      if ((fNormalizeData) && (fContentType == 1)) {
        if (fXSSimpleType.getVariety() == 3) {
          fUnionType = true;
        } else {
          try
          {
            fWhiteSpace = fXSSimpleType.getWhitespace();
          }
          catch (DatatypeException localDatatypeException1) {}
        }
      }
    }
    else if (fNormalizeData)
    {
      localObject4 = (XSSimpleType)fCurrentType;
      if (((XSSimpleType)localObject4).getVariety() == 3) {
        fUnionType = true;
      } else {
        try
        {
          fWhiteSpace = ((XSSimpleType)localObject4).getWhitespace();
        }
        catch (DatatypeException localDatatypeException2) {}
      }
    }
    fCurrentCM = null;
    if (fCurrentType.getTypeCategory() == 15) {
      fCurrentCM = ((XSComplexTypeDecl)fCurrentType).getContentModel(fCMBuilder);
    }
    fCurrCMState = null;
    if (fCurrentCM != null) {
      fCurrCMState = fCurrentCM.startContentModel();
    }
    Object localObject4 = paramXMLAttributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NIL);
    if ((localObject4 != null) && (fCurrentElemDecl != null)) {
      fNil = getXsiNil(paramQName, (String)localObject4);
    }
    XSAttributeGroupDecl localXSAttributeGroupDecl = null;
    if (fCurrentType.getTypeCategory() == 15)
    {
      XSComplexTypeDecl localXSComplexTypeDecl = (XSComplexTypeDecl)fCurrentType;
      localXSAttributeGroupDecl = localXSComplexTypeDecl.getAttrGrp();
    }
    if (fIDCChecking)
    {
      fValueStoreCache.startElement();
      fMatcherStack.pushContext();
      if ((fCurrentElemDecl != null) && (fCurrentElemDecl.fIDCPos > 0))
      {
        fIdConstraint = true;
        fValueStoreCache.initValueStoresFor(fCurrentElemDecl, this);
      }
    }
    processAttributes(paramQName, paramXMLAttributes, localXSAttributeGroupDecl);
    if (localXSAttributeGroupDecl != null) {
      addDefaultAttributes(paramQName, paramXMLAttributes, localXSAttributeGroupDecl);
    }
    int k = fMatcherStack.getMatcherCount();
    for (int m = 0; m < k; m++)
    {
      XPathMatcher localXPathMatcher = fMatcherStack.getMatcherAt(m);
      localXPathMatcher.startElement(paramQName, paramXMLAttributes);
    }
    if (fAugPSVI)
    {
      paramAugmentations = getEmptyAugs(paramAugmentations);
      fCurrentPSVI.fValidationContext = fValidationRoot;
      fCurrentPSVI.fDeclaration = fCurrentElemDecl;
      fCurrentPSVI.fTypeDecl = fCurrentType;
      fCurrentPSVI.fNotation = fNotation;
      fCurrentPSVI.fNil = fNil;
    }
    return paramAugmentations;
  }
  
  Augmentations handleEndElement(org.apache.xerces.xni.QName paramQName, Augmentations paramAugmentations)
  {
    if (fSkipValidationDepth >= 0)
    {
      if ((fSkipValidationDepth == fElementDepth) && (fSkipValidationDepth > 0))
      {
        fNFullValidationDepth = (fSkipValidationDepth - 1);
        fSkipValidationDepth = -1;
        fElementDepth -= 1;
        fSubElement = fSubElementStack[fElementDepth];
        fCurrentElemDecl = fElemDeclStack[fElementDepth];
        fNil = fNilStack[fElementDepth];
        fNotation = fNotationStack[fElementDepth];
        fCurrentType = fTypeStack[fElementDepth];
        fCurrentCM = fCMStack[fElementDepth];
        fStrictAssess = fStrictAssessStack[fElementDepth];
        fCurrCMState = fCMStateStack[fElementDepth];
        fSawText = fSawTextStack[fElementDepth];
        fSawCharacters = fStringContent[fElementDepth];
      }
      else
      {
        fElementDepth -= 1;
      }
      if ((fElementDepth == -1) && (fFullChecking) && (!fUseGrammarPoolOnly)) {
        XSConstraints.fullSchemaChecking(fGrammarBucket, fSubGroupHandler, fCMBuilder, fXSIErrorReporter.fErrorReporter);
      }
      if (fAugPSVI) {
        paramAugmentations = getEmptyAugs(paramAugmentations);
      }
      return paramAugmentations;
    }
    processElementContent(paramQName);
    int k;
    if (fIDCChecking)
    {
      int i = fMatcherStack.getMatcherCount();
      for (int j = i - 1; j >= 0; j--)
      {
        XPathMatcher localXPathMatcher1 = fMatcherStack.getMatcherAt(j);
        if (fCurrentElemDecl == null) {
          localXPathMatcher1.endElement(paramQName, fCurrentType, false, fValidatedInfo.actualValue, fValidatedInfo.actualValueType, fValidatedInfo.itemValueTypes);
        } else {
          localXPathMatcher1.endElement(paramQName, fCurrentType, fCurrentElemDecl.getNillable(), fDefaultValue == null ? fValidatedInfo.actualValue : fCurrentElemDecl.fDefault.actualValue, fDefaultValue == null ? fValidatedInfo.actualValueType : fCurrentElemDecl.fDefault.actualValueType, fDefaultValue == null ? fValidatedInfo.itemValueTypes : fCurrentElemDecl.fDefault.itemValueTypes);
        }
      }
      if (fMatcherStack.size() > 0) {
        fMatcherStack.popContext();
      }
      k = fMatcherStack.getMatcherCount();
      Object localObject1;
      Object localObject2;
      for (int m = i - 1; m >= k; m--)
      {
        XPathMatcher localXPathMatcher2 = fMatcherStack.getMatcherAt(m);
        if ((localXPathMatcher2 instanceof Selector.Matcher))
        {
          localObject1 = (Selector.Matcher)localXPathMatcher2;
          if (((localObject2 = ((Selector.Matcher)localObject1).getIdentityConstraint()) != null) && (((IdentityConstraint)localObject2).getCategory() != 2)) {
            fValueStoreCache.transplant((IdentityConstraint)localObject2, ((Selector.Matcher)localObject1).getInitialDepth());
          }
        }
      }
      for (int n = i - 1; n >= k; n--)
      {
        localObject1 = fMatcherStack.getMatcherAt(n);
        if ((localObject1 instanceof Selector.Matcher))
        {
          localObject2 = (Selector.Matcher)localObject1;
          IdentityConstraint localIdentityConstraint;
          if (((localIdentityConstraint = ((Selector.Matcher)localObject2).getIdentityConstraint()) != null) && (localIdentityConstraint.getCategory() == 2))
          {
            ValueStoreBase localValueStoreBase = fValueStoreCache.getValueStoreFor(localIdentityConstraint, ((Selector.Matcher)localObject2).getInitialDepth());
            if (localValueStoreBase != null) {
              localValueStoreBase.endDocumentFragment();
            }
          }
        }
      }
      fValueStoreCache.endElement();
    }
    if (fElementDepth < fIgnoreXSITypeDepth) {
      fIgnoreXSITypeDepth -= 1;
    }
    SchemaGrammar[] arrayOfSchemaGrammar = null;
    if (fElementDepth == 0)
    {
      String str = fValidationState.checkIDRefID();
      fValidationState.resetIDTables();
      if (str != null) {
        reportSchemaError("cvc-id.1", new Object[] { str });
      }
      if ((fFullChecking) && (!fUseGrammarPoolOnly)) {
        XSConstraints.fullSchemaChecking(fGrammarBucket, fSubGroupHandler, fCMBuilder, fXSIErrorReporter.fErrorReporter);
      }
      arrayOfSchemaGrammar = fGrammarBucket.getGrammars();
      if (fGrammarPool != null)
      {
        for (k = 0; k < arrayOfSchemaGrammar.length; k++) {
          arrayOfSchemaGrammar[k].setImmutable(true);
        }
        fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", arrayOfSchemaGrammar);
      }
      paramAugmentations = endElementPSVI(true, arrayOfSchemaGrammar, paramAugmentations);
    }
    else
    {
      paramAugmentations = endElementPSVI(false, arrayOfSchemaGrammar, paramAugmentations);
      fElementDepth -= 1;
      fSubElement = fSubElementStack[fElementDepth];
      fCurrentElemDecl = fElemDeclStack[fElementDepth];
      fNil = fNilStack[fElementDepth];
      fNotation = fNotationStack[fElementDepth];
      fCurrentType = fTypeStack[fElementDepth];
      fCurrentCM = fCMStack[fElementDepth];
      fStrictAssess = fStrictAssessStack[fElementDepth];
      fCurrCMState = fCMStateStack[fElementDepth];
      fSawText = fSawTextStack[fElementDepth];
      fSawCharacters = fStringContent[fElementDepth];
      fWhiteSpace = -1;
      fAppendBuffer = false;
      fUnionType = false;
    }
    return paramAugmentations;
  }
  
  final Augmentations endElementPSVI(boolean paramBoolean, SchemaGrammar[] paramArrayOfSchemaGrammar, Augmentations paramAugmentations)
  {
    if (fAugPSVI)
    {
      paramAugmentations = getEmptyAugs(paramAugmentations);
      fCurrentPSVI.fDeclaration = fCurrentElemDecl;
      fCurrentPSVI.fTypeDecl = fCurrentType;
      fCurrentPSVI.fNotation = fNotation;
      fCurrentPSVI.fValidationContext = fValidationRoot;
      fCurrentPSVI.fNil = fNil;
      if (fElementDepth > fNFullValidationDepth) {
        fCurrentPSVI.fValidationAttempted = 2;
      } else if (fElementDepth > fNNoneValidationDepth) {
        fCurrentPSVI.fValidationAttempted = 0;
      } else {
        fCurrentPSVI.fValidationAttempted = 1;
      }
      if (fNFullValidationDepth == fElementDepth) {
        fNFullValidationDepth = (fElementDepth - 1);
      }
      if (fNNoneValidationDepth == fElementDepth) {
        fNNoneValidationDepth = (fElementDepth - 1);
      }
      if (fDefaultValue != null) {
        fCurrentPSVI.fSpecified = true;
      }
      fCurrentPSVI.fValue.copyFrom(fValidatedInfo);
      if (fStrictAssess)
      {
        String[] arrayOfString = fXSIErrorReporter.mergeContext();
        fCurrentPSVI.fErrors = arrayOfString;
        fCurrentPSVI.fValidity = (arrayOfString == null ? 2 : 1);
      }
      else
      {
        fCurrentPSVI.fValidity = 0;
        fXSIErrorReporter.popContext();
      }
      if (paramBoolean)
      {
        fCurrentPSVI.fGrammars = paramArrayOfSchemaGrammar;
        fCurrentPSVI.fSchemaInformation = null;
      }
    }
    return paramAugmentations;
  }
  
  Augmentations getEmptyAugs(Augmentations paramAugmentations)
  {
    if (paramAugmentations == null)
    {
      paramAugmentations = fAugmentations;
      paramAugmentations.removeAllItems();
    }
    paramAugmentations.putItem("ELEMENT_PSVI", fCurrentPSVI);
    fCurrentPSVI.reset();
    return paramAugmentations;
  }
  
  void storeLocations(String paramString1, String paramString2)
  {
    if (paramString1 != null) {
      if (!XMLSchemaLoader.tokenizeSchemaLocationStr(paramString1, fLocationPairs, fLocator == null ? null : fLocator.getExpandedSystemId())) {
        fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "SchemaLocation", new Object[] { paramString1 }, (short)0);
      }
    }
    if (paramString2 != null)
    {
      XMLSchemaLoader.LocationArray localLocationArray = (XMLSchemaLoader.LocationArray)fLocationPairs.get(XMLSymbols.EMPTY_STRING);
      if (localLocationArray == null)
      {
        localLocationArray = new XMLSchemaLoader.LocationArray();
        fLocationPairs.put(XMLSymbols.EMPTY_STRING, localLocationArray);
      }
      if (fLocator != null) {
        try
        {
          paramString2 = XMLEntityManager.expandSystemId(paramString2, fLocator.getExpandedSystemId(), false);
        }
        catch (URI.MalformedURIException localMalformedURIException) {}
      }
      localLocationArray.addLocation(paramString2);
    }
  }
  
  SchemaGrammar findSchemaGrammar(short paramShort, String paramString, org.apache.xerces.xni.QName paramQName1, org.apache.xerces.xni.QName paramQName2, XMLAttributes paramXMLAttributes)
  {
    SchemaGrammar localSchemaGrammar = null;
    localSchemaGrammar = fGrammarBucket.getGrammar(paramString);
    if (localSchemaGrammar == null)
    {
      fXSDDescription.setNamespace(paramString);
      if (fGrammarPool != null)
      {
        localSchemaGrammar = (SchemaGrammar)fGrammarPool.retrieveGrammar(fXSDDescription);
        if ((localSchemaGrammar != null) && (!fGrammarBucket.putGrammar(localSchemaGrammar, true, fNamespaceGrowth)))
        {
          fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "GrammarConflict", null, (short)0);
          localSchemaGrammar = null;
        }
      }
    }
    if ((!fUseGrammarPoolOnly) && ((localSchemaGrammar == null) || ((fNamespaceGrowth) && (!hasSchemaComponent(localSchemaGrammar, paramShort, paramQName2)))))
    {
      fXSDDescription.reset();
      fXSDDescription.fContextType = paramShort;
      fXSDDescription.setNamespace(paramString);
      fXSDDescription.fEnclosedElementName = paramQName1;
      fXSDDescription.fTriggeringComponent = paramQName2;
      fXSDDescription.fAttributes = paramXMLAttributes;
      if (fLocator != null) {
        fXSDDescription.setBaseSystemId(fLocator.getExpandedSystemId());
      }
      Hashtable localHashtable = fLocationPairs;
      Object localObject = localHashtable.get(paramString == null ? XMLSymbols.EMPTY_STRING : paramString);
      if (localObject != null)
      {
        String[] arrayOfString1 = ((XMLSchemaLoader.LocationArray)localObject).getLocationArray();
        if (arrayOfString1.length != 0) {
          setLocationHints(fXSDDescription, arrayOfString1, localSchemaGrammar);
        }
      }
      if ((localSchemaGrammar == null) || (fXSDDescription.fLocationHints != null))
      {
        int i = 1;
        if (localSchemaGrammar != null) {
          localHashtable = EMPTY_TABLE;
        }
        try
        {
          XMLInputSource localXMLInputSource = XMLSchemaLoader.resolveDocument(fXSDDescription, localHashtable, fEntityResolver);
          if ((localSchemaGrammar != null) && (fNamespaceGrowth)) {
            try
            {
              if (localSchemaGrammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(localXMLInputSource.getSystemId(), localXMLInputSource.getBaseSystemId(), false))) {
                i = 0;
              }
            }
            catch (URI.MalformedURIException localMalformedURIException) {}
          }
          if (i != 0) {
            localSchemaGrammar = fSchemaLoader.loadSchema(fXSDDescription, localXMLInputSource, fLocationPairs);
          }
        }
        catch (IOException localIOException)
        {
          String[] arrayOfString2 = fXSDDescription.getLocationHints();
          fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[] { arrayOfString2 != null ? arrayOfString2[0] : XMLSymbols.EMPTY_STRING }, (short)0, localIOException);
        }
      }
    }
    return localSchemaGrammar;
  }
  
  private boolean hasSchemaComponent(SchemaGrammar paramSchemaGrammar, short paramShort, org.apache.xerces.xni.QName paramQName)
  {
    if ((paramSchemaGrammar != null) && (paramQName != null))
    {
      String str = localpart;
      if ((str != null) && (str.length() > 0)) {
        switch (paramShort)
        {
        case 5: 
          return paramSchemaGrammar.getElementDeclaration(str) != null;
        case 6: 
          return paramSchemaGrammar.getAttributeDeclaration(str) != null;
        case 7: 
          return paramSchemaGrammar.getTypeDefinition(str) != null;
        }
      }
    }
    return false;
  }
  
  private void setLocationHints(XSDDescription paramXSDDescription, String[] paramArrayOfString, SchemaGrammar paramSchemaGrammar)
  {
    int i = paramArrayOfString.length;
    if (paramSchemaGrammar == null)
    {
      fXSDDescription.fLocationHints = new String[i];
      System.arraycopy(paramArrayOfString, 0, fXSDDescription.fLocationHints, 0, i);
    }
    else
    {
      setLocationHints(paramXSDDescription, paramArrayOfString, paramSchemaGrammar.getDocumentLocations());
    }
  }
  
  private void setLocationHints(XSDDescription paramXSDDescription, String[] paramArrayOfString, StringList paramStringList)
  {
    int i = paramArrayOfString.length;
    String[] arrayOfString = new String[i];
    int j = 0;
    for (int k = 0; k < i; k++) {
      if (!paramStringList.contains(paramArrayOfString[k])) {
        arrayOfString[(j++)] = paramArrayOfString[k];
      }
    }
    if (j > 0) {
      if (j == i)
      {
        fXSDDescription.fLocationHints = arrayOfString;
      }
      else
      {
        fXSDDescription.fLocationHints = new String[j];
        System.arraycopy(arrayOfString, 0, fXSDDescription.fLocationHints, 0, j);
      }
    }
  }
  
  XSTypeDefinition getAndCheckXsiType(org.apache.xerces.xni.QName paramQName, String paramString, XMLAttributes paramXMLAttributes)
  {
    org.apache.xerces.xni.QName localQName = null;
    try
    {
      localQName = (org.apache.xerces.xni.QName)fQNameDV.validate(paramString, fValidationState, null);
    }
    catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
    {
      reportSchemaError(localInvalidDatatypeValueException.getKey(), localInvalidDatatypeValueException.getArgs());
      reportSchemaError("cvc-elt.4.1", new Object[] { rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, paramString });
      return null;
    }
    XSTypeDefinition localXSTypeDefinition = null;
    if (uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
      localXSTypeDefinition = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(localpart);
    }
    if (localXSTypeDefinition == null)
    {
      SchemaGrammar localSchemaGrammar = findSchemaGrammar((short)7, uri, paramQName, localQName, paramXMLAttributes);
      if (localSchemaGrammar != null) {
        localXSTypeDefinition = localSchemaGrammar.getGlobalTypeDecl(localpart);
      }
    }
    if (localXSTypeDefinition == null)
    {
      reportSchemaError("cvc-elt.4.2", new Object[] { rawname, paramString });
      return null;
    }
    if (fCurrentType != null)
    {
      short s = 0;
      if (fCurrentElemDecl != null) {
        s = fCurrentElemDecl.fBlock;
      }
      if (fCurrentType.getTypeCategory() == 15) {
        s = (short)(s | fCurrentType).fBlock);
      }
      if (!XSConstraints.checkTypeDerivationOk(localXSTypeDefinition, fCurrentType, s)) {
        reportSchemaError("cvc-elt.4.3", new Object[] { rawname, paramString, fCurrentType.getName() });
      }
    }
    return localXSTypeDefinition;
  }
  
  boolean getXsiNil(org.apache.xerces.xni.QName paramQName, String paramString)
  {
    if ((fCurrentElemDecl != null) && (!fCurrentElemDecl.getNillable()))
    {
      reportSchemaError("cvc-elt.3.1", new Object[] { rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
    }
    else
    {
      String str = XMLChar.trim(paramString);
      if ((str.equals("true")) || (str.equals("1")))
      {
        if ((fCurrentElemDecl != null) && (fCurrentElemDecl.getConstraintType() == 2)) {
          reportSchemaError("cvc-elt.3.2.2", new Object[] { rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
        }
        return true;
      }
    }
    return false;
  }
  
  void processAttributes(org.apache.xerces.xni.QName paramQName, XMLAttributes paramXMLAttributes, XSAttributeGroupDecl paramXSAttributeGroupDecl)
  {
    String str = null;
    int i = paramXMLAttributes.getLength();
    Augmentations localAugmentations = null;
    AttributePSVImpl localAttributePSVImpl = null;
    int j = (fCurrentType == null) || (fCurrentType.getTypeCategory() == 16) ? 1 : 0;
    XSObjectList localXSObjectList = null;
    int k = 0;
    XSWildcardDecl localXSWildcardDecl = null;
    if (j == 0)
    {
      localXSObjectList = paramXSAttributeGroupDecl.getAttributeUses();
      k = localXSObjectList.getLength();
      localXSWildcardDecl = fAttributeWC;
    }
    for (int m = 0; m < i; m++)
    {
      paramXMLAttributes.getName(m, fTempQName);
      if ((fAugPSVI) || (fIdConstraint))
      {
        localAugmentations = paramXMLAttributes.getAugmentations(m);
        localAttributePSVImpl = (AttributePSVImpl)localAugmentations.getItem("ATTRIBUTE_PSVI");
        if (localAttributePSVImpl != null)
        {
          localAttributePSVImpl.reset();
        }
        else
        {
          localAttributePSVImpl = new AttributePSVImpl();
          localAugmentations.putItem("ATTRIBUTE_PSVI", localAttributePSVImpl);
        }
        fValidationContext = fValidationRoot;
      }
      Object localObject;
      if (fTempQName.uri == SchemaSymbols.URI_XSI)
      {
        localObject = null;
        if (fTempQName.localpart == SchemaSymbols.XSI_TYPE) {
          localObject = XSI_TYPE;
        } else if (fTempQName.localpart == SchemaSymbols.XSI_NIL) {
          localObject = XSI_NIL;
        } else if (fTempQName.localpart == SchemaSymbols.XSI_SCHEMALOCATION) {
          localObject = XSI_SCHEMALOCATION;
        } else if (fTempQName.localpart == SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION) {
          localObject = XSI_NONAMESPACESCHEMALOCATION;
        }
        if (localObject != null)
        {
          processOneAttribute(paramQName, paramXMLAttributes, m, (XSAttributeDecl)localObject, null, localAttributePSVImpl);
          continue;
        }
      }
      if ((fTempQName.rawname != XMLSymbols.PREFIX_XMLNS) && (!fTempQName.rawname.startsWith("xmlns:"))) {
        if (j != 0)
        {
          reportSchemaError("cvc-type.3.1.1", new Object[] { rawname, fTempQName.rawname });
        }
        else
        {
          localObject = null;
          for (int n = 0; n < k; n++)
          {
            XSAttributeUseImpl localXSAttributeUseImpl = (XSAttributeUseImpl)localXSObjectList.item(n);
            if ((fAttrDecl.fName == fTempQName.localpart) && (fAttrDecl.fTargetNamespace == fTempQName.uri))
            {
              localObject = localXSAttributeUseImpl;
              break;
            }
          }
          if ((localObject == null) && ((localXSWildcardDecl == null) || (!localXSWildcardDecl.allowNamespace(fTempQName.uri))))
          {
            reportSchemaError("cvc-complex-type.3.2.2", new Object[] { rawname, fTempQName.rawname });
            fNFullValidationDepth = fElementDepth;
          }
          else
          {
            XSAttributeDecl localXSAttributeDecl = null;
            if (localObject != null)
            {
              localXSAttributeDecl = fAttrDecl;
            }
            else
            {
              if (fProcessContents == 2) {
                continue;
              }
              SchemaGrammar localSchemaGrammar = findSchemaGrammar((short)6, fTempQName.uri, paramQName, fTempQName, paramXMLAttributes);
              if (localSchemaGrammar != null) {
                localXSAttributeDecl = localSchemaGrammar.getGlobalAttributeDecl(fTempQName.localpart);
              }
              if (localXSAttributeDecl == null)
              {
                if (fProcessContents != 1) {
                  continue;
                }
                reportSchemaError("cvc-complex-type.3.2.2", new Object[] { rawname, fTempQName.rawname });
                continue;
              }
              if ((fType.getTypeCategory() == 16) && (fType.isIDType())) {
                if (str != null) {
                  reportSchemaError("cvc-complex-type.5.1", new Object[] { rawname, fName, str });
                } else {
                  str = fName;
                }
              }
            }
            processOneAttribute(paramQName, paramXMLAttributes, m, localXSAttributeDecl, (XSAttributeUseImpl)localObject, localAttributePSVImpl);
          }
        }
      }
    }
    if ((j == 0) && (fIDAttrName != null) && (str != null)) {
      reportSchemaError("cvc-complex-type.5.2", new Object[] { rawname, str, fIDAttrName });
    }
  }
  
  void processOneAttribute(org.apache.xerces.xni.QName paramQName, XMLAttributes paramXMLAttributes, int paramInt, XSAttributeDecl paramXSAttributeDecl, XSAttributeUseImpl paramXSAttributeUseImpl, AttributePSVImpl paramAttributePSVImpl)
  {
    String str = paramXMLAttributes.getValue(paramInt);
    fXSIErrorReporter.pushContext();
    XSSimpleType localXSSimpleType = fType;
    Object localObject = null;
    try
    {
      localObject = localXSSimpleType.validate(str, fValidationState, fValidatedInfo);
      if (fNormalizeData) {
        paramXMLAttributes.setValue(paramInt, fValidatedInfo.normalizedValue);
      }
      if ((localXSSimpleType.getVariety() == 1) && (localXSSimpleType.getPrimitiveKind() == 20))
      {
        org.apache.xerces.xni.QName localQName = (org.apache.xerces.xni.QName)localObject;
        SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(uri);
        if (localSchemaGrammar != null) {
          fNotation = localSchemaGrammar.getGlobalNotationDecl(localpart);
        }
      }
    }
    catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
    {
      reportSchemaError(localInvalidDatatypeValueException.getKey(), localInvalidDatatypeValueException.getArgs());
      reportSchemaError("cvc-attribute.3", new Object[] { rawname, fTempQName.rawname, str, (localXSSimpleType instanceof XSSimpleTypeDecl) ? ((XSSimpleTypeDecl)localXSSimpleType).getTypeName() : localXSSimpleType.getName() });
    }
    if ((localObject != null) && (paramXSAttributeDecl.getConstraintType() == 2) && ((!ValidatedInfo.isComparable(fValidatedInfo, fDefault)) || (!localObject.equals(fDefault.actualValue)))) {
      reportSchemaError("cvc-attribute.4", new Object[] { rawname, fTempQName.rawname, str, fDefault.stringValue() });
    }
    if ((localObject != null) && (paramXSAttributeUseImpl != null) && (fConstraintType == 2) && ((!ValidatedInfo.isComparable(fValidatedInfo, fDefault)) || (!localObject.equals(fDefault.actualValue)))) {
      reportSchemaError("cvc-complex-type.3.1", new Object[] { rawname, fTempQName.rawname, str, fDefault.stringValue() });
    }
    if (fIdConstraint) {
      fValue.copyFrom(fValidatedInfo);
    }
    if (fAugPSVI)
    {
      fDeclaration = paramXSAttributeDecl;
      fTypeDecl = localXSSimpleType;
      fValue.copyFrom(fValidatedInfo);
      fValidationAttempted = 2;
      fNNoneValidationDepth = fElementDepth;
      String[] arrayOfString = fXSIErrorReporter.mergeContext();
      fErrors = arrayOfString;
      fValidity = (arrayOfString == null ? 2 : 1);
    }
  }
  
  void addDefaultAttributes(org.apache.xerces.xni.QName paramQName, XMLAttributes paramXMLAttributes, XSAttributeGroupDecl paramXSAttributeGroupDecl)
  {
    XSObjectList localXSObjectList = paramXSAttributeGroupDecl.getAttributeUses();
    int i = localXSObjectList.getLength();
    for (int m = 0; m < i; m++)
    {
      XSAttributeUseImpl localXSAttributeUseImpl = (XSAttributeUseImpl)localXSObjectList.item(m);
      XSAttributeDecl localXSAttributeDecl = fAttrDecl;
      int j = fConstraintType;
      ValidatedInfo localValidatedInfo = fDefault;
      if (j == 0)
      {
        j = localXSAttributeDecl.getConstraintType();
        localValidatedInfo = fDefault;
      }
      int k = paramXMLAttributes.getValue(fTargetNamespace, fName) != null ? 1 : 0;
      if ((fUse == 1) && (k == 0)) {
        reportSchemaError("cvc-complex-type.4", new Object[] { rawname, fName });
      }
      if ((k == 0) && (j != 0))
      {
        org.apache.xerces.xni.QName localQName = new org.apache.xerces.xni.QName(null, fName, fName, fTargetNamespace);
        String str = localValidatedInfo != null ? localValidatedInfo.stringValue() : "";
        Object localObject;
        int n;
        if ((paramXMLAttributes instanceof XMLAttributesImpl))
        {
          localObject = (XMLAttributesImpl)paramXMLAttributes;
          n = ((XMLAttributesImpl)localObject).getLength();
          ((XMLAttributesImpl)localObject).addAttributeNS(localQName, "CDATA", str);
        }
        else
        {
          n = paramXMLAttributes.addAttribute(localQName, "CDATA", str);
        }
        if (fAugPSVI)
        {
          localObject = paramXMLAttributes.getAugmentations(n);
          AttributePSVImpl localAttributePSVImpl = new AttributePSVImpl();
          ((Augmentations)localObject).putItem("ATTRIBUTE_PSVI", localAttributePSVImpl);
          fDeclaration = localXSAttributeDecl;
          fTypeDecl = fType;
          fValue.copyFrom(localValidatedInfo);
          fValidationContext = fValidationRoot;
          fValidity = 2;
          fValidationAttempted = 2;
          fSpecified = true;
        }
      }
    }
  }
  
  void processElementContent(org.apache.xerces.xni.QName paramQName)
  {
    Object localObject;
    if ((fCurrentElemDecl != null) && (fCurrentElemDecl.fDefault != null) && (!fSawText) && (!fSubElement) && (!fNil))
    {
      localObject = fCurrentElemDecl.fDefault.stringValue();
      int i = ((String)localObject).length();
      if ((fNormalizedStr.ch == null) || (fNormalizedStr.ch.length < i)) {
        fNormalizedStr.ch = new char[i];
      }
      ((String)localObject).getChars(0, i, fNormalizedStr.ch, 0);
      fNormalizedStr.offset = 0;
      fNormalizedStr.length = i;
      fDefaultValue = fNormalizedStr;
    }
    fValidatedInfo.normalizedValue = null;
    if ((fNil) && ((fSubElement) || (fSawText))) {
      reportSchemaError("cvc-elt.3.2.1", new Object[] { rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
    }
    fValidatedInfo.reset();
    if ((fCurrentElemDecl != null) && (fCurrentElemDecl.getConstraintType() != 0) && (!fSubElement) && (!fSawText) && (!fNil))
    {
      if ((fCurrentType != fCurrentElemDecl.fType) && (XSConstraints.ElementDefaultValidImmediate(fCurrentType, fCurrentElemDecl.fDefault.stringValue(), fState4XsiType, null) == null)) {
        reportSchemaError("cvc-elt.5.1.1", new Object[] { rawname, fCurrentType.getName(), fCurrentElemDecl.fDefault.stringValue() });
      }
      elementLocallyValidType(paramQName, fCurrentElemDecl.fDefault.stringValue());
    }
    else
    {
      localObject = elementLocallyValidType(paramQName, fBuffer);
      if ((fCurrentElemDecl != null) && (fCurrentElemDecl.getConstraintType() == 2) && (!fNil))
      {
        String str = fBuffer.toString();
        if (fSubElement) {
          reportSchemaError("cvc-elt.5.2.2.1", new Object[] { rawname });
        }
        if (fCurrentType.getTypeCategory() == 15)
        {
          XSComplexTypeDecl localXSComplexTypeDecl = (XSComplexTypeDecl)fCurrentType;
          if (fContentType == 3)
          {
            if (!fCurrentElemDecl.fDefault.normalizedValue.equals(str)) {
              reportSchemaError("cvc-elt.5.2.2.2.1", new Object[] { rawname, str, fCurrentElemDecl.fDefault.normalizedValue });
            }
          }
          else if ((fContentType == 1) && (localObject != null) && ((!ValidatedInfo.isComparable(fValidatedInfo, fCurrentElemDecl.fDefault)) || (!localObject.equals(fCurrentElemDecl.fDefault.actualValue)))) {
            reportSchemaError("cvc-elt.5.2.2.2.2", new Object[] { rawname, str, fCurrentElemDecl.fDefault.stringValue() });
          }
        }
        else if ((fCurrentType.getTypeCategory() == 16) && (localObject != null) && ((!ValidatedInfo.isComparable(fValidatedInfo, fCurrentElemDecl.fDefault)) || (!localObject.equals(fCurrentElemDecl.fDefault.actualValue))))
        {
          reportSchemaError("cvc-elt.5.2.2.2.2", new Object[] { rawname, str, fCurrentElemDecl.fDefault.stringValue() });
        }
      }
    }
    if ((fDefaultValue == null) && (fNormalizeData) && (fDocumentHandler != null) && (fUnionType))
    {
      localObject = fValidatedInfo.normalizedValue;
      if (localObject == null) {
        localObject = fBuffer.toString();
      }
      int j = ((String)localObject).length();
      if ((fNormalizedStr.ch == null) || (fNormalizedStr.ch.length < j)) {
        fNormalizedStr.ch = new char[j];
      }
      ((String)localObject).getChars(0, j, fNormalizedStr.ch, 0);
      fNormalizedStr.offset = 0;
      fNormalizedStr.length = j;
      fDocumentHandler.characters(fNormalizedStr, null);
    }
  }
  
  Object elementLocallyValidType(org.apache.xerces.xni.QName paramQName, Object paramObject)
  {
    if (fCurrentType == null) {
      return null;
    }
    Object localObject = null;
    if (fCurrentType.getTypeCategory() == 16)
    {
      if (fSubElement) {
        reportSchemaError("cvc-type.3.1.2", new Object[] { rawname });
      }
      if (!fNil)
      {
        XSSimpleType localXSSimpleType = (XSSimpleType)fCurrentType;
        try
        {
          if ((!fNormalizeData) || (fUnionType)) {
            fValidationState.setNormalizationRequired(true);
          }
          localObject = localXSSimpleType.validate(paramObject, fValidationState, fValidatedInfo);
        }
        catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
        {
          reportSchemaError(localInvalidDatatypeValueException.getKey(), localInvalidDatatypeValueException.getArgs());
          reportSchemaError("cvc-type.3.1.3", new Object[] { rawname, paramObject });
        }
      }
    }
    else
    {
      localObject = elementLocallyValidComplexType(paramQName, paramObject);
    }
    return localObject;
  }
  
  Object elementLocallyValidComplexType(org.apache.xerces.xni.QName paramQName, Object paramObject)
  {
    Object localObject1 = null;
    XSComplexTypeDecl localXSComplexTypeDecl = (XSComplexTypeDecl)fCurrentType;
    if (!fNil)
    {
      Object localObject2;
      if ((fContentType == 0) && ((fSubElement) || (fSawText)))
      {
        reportSchemaError("cvc-complex-type.2.1", new Object[] { rawname });
      }
      else if (fContentType == 1)
      {
        if (fSubElement) {
          reportSchemaError("cvc-complex-type.2.2", new Object[] { rawname });
        }
        localObject2 = fXSSimpleType;
        try
        {
          if ((!fNormalizeData) || (fUnionType)) {
            fValidationState.setNormalizationRequired(true);
          }
          localObject1 = ((XSSimpleType)localObject2).validate(paramObject, fValidationState, fValidatedInfo);
        }
        catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
        {
          reportSchemaError(localInvalidDatatypeValueException.getKey(), localInvalidDatatypeValueException.getArgs());
          reportSchemaError("cvc-complex-type.2.2", new Object[] { rawname });
        }
      }
      else if ((fContentType == 2) && (fSawCharacters))
      {
        reportSchemaError("cvc-complex-type.2.3", new Object[] { rawname });
      }
      if (((fContentType == 2) || (fContentType == 3)) && (fCurrCMState[0] >= 0) && (!fCurrentCM.endContentModel(fCurrCMState)))
      {
        localObject2 = expectedStr(fCurrentCM.whatCanGoHere(fCurrCMState));
        int[] arrayOfInt = fCurrentCM.occurenceInfo(fCurrCMState);
        if (arrayOfInt != null)
        {
          int i = arrayOfInt[0];
          int j = arrayOfInt[2];
          if (j < i)
          {
            int k = i - j;
            if (k > 1) {
              reportSchemaError("cvc-complex-type.2.4.j", new Object[] { rawname, fCurrentCM.getTermName(arrayOfInt[3]), Integer.toString(i), Integer.toString(k) });
            } else {
              reportSchemaError("cvc-complex-type.2.4.i", new Object[] { rawname, fCurrentCM.getTermName(arrayOfInt[3]), Integer.toString(i) });
            }
          }
          else
          {
            reportSchemaError("cvc-complex-type.2.4.b", new Object[] { rawname, localObject2 });
          }
        }
        else
        {
          reportSchemaError("cvc-complex-type.2.4.b", new Object[] { rawname, localObject2 });
        }
      }
    }
    return localObject1;
  }
  
  void processRootTypeQName(javax.xml.namespace.QName paramQName)
  {
    String str = paramQName.getNamespaceURI();
    if ((str != null) && (str.equals(""))) {
      str = null;
    }
    Object localObject;
    if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(str))
    {
      fCurrentType = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(paramQName.getLocalPart());
    }
    else
    {
      localObject = findSchemaGrammar((short)5, str, null, null, null);
      if (localObject != null) {
        fCurrentType = ((SchemaGrammar)localObject).getGlobalTypeDecl(paramQName.getLocalPart());
      }
    }
    if (fCurrentType == null)
    {
      localObject = paramQName.getPrefix() + ":" + paramQName.getLocalPart();
      reportSchemaError("cvc-type.1", new Object[] { localObject });
    }
  }
  
  void processRootElementDeclQName(javax.xml.namespace.QName paramQName, org.apache.xerces.xni.QName paramQName1)
  {
    String str1 = paramQName.getNamespaceURI();
    if ((str1 != null) && (str1.equals(""))) {
      str1 = null;
    }
    SchemaGrammar localSchemaGrammar = findSchemaGrammar((short)5, str1, null, null, null);
    if (localSchemaGrammar != null) {
      fCurrentElemDecl = localSchemaGrammar.getGlobalElementDecl(paramQName.getLocalPart());
    }
    if (fCurrentElemDecl == null)
    {
      String str2 = paramQName.getPrefix() + ":" + paramQName.getLocalPart();
      reportSchemaError("cvc-elt.1.a", new Object[] { str2 });
    }
    else
    {
      checkElementMatchesRootElementDecl(fCurrentElemDecl, paramQName1);
    }
  }
  
  void checkElementMatchesRootElementDecl(XSElementDecl paramXSElementDecl, org.apache.xerces.xni.QName paramQName)
  {
    if ((localpart != fName) || (uri != fTargetNamespace)) {
      reportSchemaError("cvc-elt.1.b", new Object[] { rawname, fName });
    }
  }
  
  void reportSchemaError(String paramString, Object[] paramArrayOfObject)
  {
    if (fDoValidation) {
      fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)1);
    }
  }
  
  private String expectedStr(Vector paramVector)
  {
    StringBuffer localStringBuffer = new StringBuffer("{");
    int i = paramVector.size();
    for (int j = 0; j < i; j++)
    {
      if (j > 0) {
        localStringBuffer.append(", ");
      }
      localStringBuffer.append(paramVector.elementAt(j).toString());
    }
    localStringBuffer.append('}');
    return localStringBuffer.toString();
  }
  
  protected static final class ShortVector
  {
    private int fLength;
    private short[] fData;
    
    public ShortVector() {}
    
    public ShortVector(int paramInt)
    {
      fData = new short[paramInt];
    }
    
    public int length()
    {
      return fLength;
    }
    
    public void add(short paramShort)
    {
      ensureCapacity(fLength + 1);
      fData[(fLength++)] = paramShort;
    }
    
    public short valueAt(int paramInt)
    {
      return fData[paramInt];
    }
    
    public void clear()
    {
      fLength = 0;
    }
    
    public boolean contains(short paramShort)
    {
      for (int i = 0; i < fLength; i++) {
        if (fData[i] == paramShort) {
          return true;
        }
      }
      return false;
    }
    
    private void ensureCapacity(int paramInt)
    {
      if (fData == null)
      {
        fData = new short[8];
      }
      else if (fData.length <= paramInt)
      {
        short[] arrayOfShort = new short[fData.length * 2];
        System.arraycopy(fData, 0, arrayOfShort, 0, fData.length);
        fData = arrayOfShort;
      }
    }
  }
  
  protected static final class LocalIDKey
  {
    public IdentityConstraint fId;
    public int fDepth;
    
    public LocalIDKey() {}
    
    public LocalIDKey(IdentityConstraint paramIdentityConstraint, int paramInt)
    {
      fId = paramIdentityConstraint;
      fDepth = paramInt;
    }
    
    public int hashCode()
    {
      return fId.hashCode() + fDepth;
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof LocalIDKey))
      {
        LocalIDKey localLocalIDKey = (LocalIDKey)paramObject;
        return (fId == fId) && (fDepth == fDepth);
      }
      return false;
    }
  }
  
  protected class ValueStoreCache
  {
    final XMLSchemaValidator.LocalIDKey fLocalId = new XMLSchemaValidator.LocalIDKey();
    protected final ArrayList fValueStores = new ArrayList();
    protected final HashMap fIdentityConstraint2ValueStoreMap = new HashMap();
    protected final Stack fGlobalMapStack = new Stack();
    protected final HashMap fGlobalIDConstraintMap = new HashMap();
    
    public ValueStoreCache() {}
    
    public void startDocument()
    {
      fValueStores.clear();
      fIdentityConstraint2ValueStoreMap.clear();
      fGlobalIDConstraintMap.clear();
      fGlobalMapStack.removeAllElements();
    }
    
    public void startElement()
    {
      if (fGlobalIDConstraintMap.size() > 0) {
        fGlobalMapStack.push(fGlobalIDConstraintMap.clone());
      } else {
        fGlobalMapStack.push(null);
      }
      fGlobalIDConstraintMap.clear();
    }
    
    public void endElement()
    {
      if (fGlobalMapStack.isEmpty()) {
        return;
      }
      HashMap localHashMap = (HashMap)fGlobalMapStack.pop();
      if (localHashMap == null) {
        return;
      }
      Iterator localIterator = localHashMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        IdentityConstraint localIdentityConstraint = (IdentityConstraint)localEntry.getKey();
        XMLSchemaValidator.ValueStoreBase localValueStoreBase1 = (XMLSchemaValidator.ValueStoreBase)localEntry.getValue();
        if (localValueStoreBase1 != null)
        {
          XMLSchemaValidator.ValueStoreBase localValueStoreBase2 = (XMLSchemaValidator.ValueStoreBase)fGlobalIDConstraintMap.get(localIdentityConstraint);
          if (localValueStoreBase2 == null) {
            fGlobalIDConstraintMap.put(localIdentityConstraint, localValueStoreBase1);
          } else if (localValueStoreBase2 != localValueStoreBase1) {
            localValueStoreBase2.append(localValueStoreBase1);
          }
        }
      }
    }
    
    public void initValueStoresFor(XSElementDecl paramXSElementDecl, FieldActivator paramFieldActivator)
    {
      IdentityConstraint[] arrayOfIdentityConstraint = fIDConstraints;
      int i = fIDCPos;
      for (int j = 0; j < i; j++)
      {
        XMLSchemaValidator.LocalIDKey localLocalIDKey;
        switch (arrayOfIdentityConstraint[j].getCategory())
        {
        case 3: 
          UniqueOrKey localUniqueOrKey1 = (UniqueOrKey)arrayOfIdentityConstraint[j];
          localLocalIDKey = new XMLSchemaValidator.LocalIDKey(localUniqueOrKey1, fElementDepth);
          XMLSchemaValidator.UniqueValueStore localUniqueValueStore = (XMLSchemaValidator.UniqueValueStore)fIdentityConstraint2ValueStoreMap.get(localLocalIDKey);
          if (localUniqueValueStore == null)
          {
            localUniqueValueStore = new XMLSchemaValidator.UniqueValueStore(XMLSchemaValidator.this, localUniqueOrKey1);
            fIdentityConstraint2ValueStoreMap.put(localLocalIDKey, localUniqueValueStore);
          }
          else
          {
            localUniqueValueStore.clear();
          }
          fValueStores.add(localUniqueValueStore);
          XMLSchemaValidator.this.activateSelectorFor(arrayOfIdentityConstraint[j]);
          break;
        case 1: 
          UniqueOrKey localUniqueOrKey2 = (UniqueOrKey)arrayOfIdentityConstraint[j];
          localLocalIDKey = new XMLSchemaValidator.LocalIDKey(localUniqueOrKey2, fElementDepth);
          XMLSchemaValidator.KeyValueStore localKeyValueStore = (XMLSchemaValidator.KeyValueStore)fIdentityConstraint2ValueStoreMap.get(localLocalIDKey);
          if (localKeyValueStore == null)
          {
            localKeyValueStore = new XMLSchemaValidator.KeyValueStore(XMLSchemaValidator.this, localUniqueOrKey2);
            fIdentityConstraint2ValueStoreMap.put(localLocalIDKey, localKeyValueStore);
          }
          else
          {
            localKeyValueStore.clear();
          }
          fValueStores.add(localKeyValueStore);
          XMLSchemaValidator.this.activateSelectorFor(arrayOfIdentityConstraint[j]);
          break;
        case 2: 
          KeyRef localKeyRef = (KeyRef)arrayOfIdentityConstraint[j];
          localLocalIDKey = new XMLSchemaValidator.LocalIDKey(localKeyRef, fElementDepth);
          XMLSchemaValidator.KeyRefValueStore localKeyRefValueStore = (XMLSchemaValidator.KeyRefValueStore)fIdentityConstraint2ValueStoreMap.get(localLocalIDKey);
          if (localKeyRefValueStore == null)
          {
            localKeyRefValueStore = new XMLSchemaValidator.KeyRefValueStore(XMLSchemaValidator.this, localKeyRef, null);
            fIdentityConstraint2ValueStoreMap.put(localLocalIDKey, localKeyRefValueStore);
          }
          else
          {
            localKeyRefValueStore.clear();
          }
          fValueStores.add(localKeyRefValueStore);
          XMLSchemaValidator.this.activateSelectorFor(arrayOfIdentityConstraint[j]);
        }
      }
    }
    
    public XMLSchemaValidator.ValueStoreBase getValueStoreFor(IdentityConstraint paramIdentityConstraint, int paramInt)
    {
      fLocalId.fDepth = paramInt;
      fLocalId.fId = paramIdentityConstraint;
      return (XMLSchemaValidator.ValueStoreBase)fIdentityConstraint2ValueStoreMap.get(fLocalId);
    }
    
    public XMLSchemaValidator.ValueStoreBase getGlobalValueStoreFor(IdentityConstraint paramIdentityConstraint)
    {
      return (XMLSchemaValidator.ValueStoreBase)fGlobalIDConstraintMap.get(paramIdentityConstraint);
    }
    
    public void transplant(IdentityConstraint paramIdentityConstraint, int paramInt)
    {
      fLocalId.fDepth = paramInt;
      fLocalId.fId = paramIdentityConstraint;
      XMLSchemaValidator.ValueStoreBase localValueStoreBase1 = (XMLSchemaValidator.ValueStoreBase)fIdentityConstraint2ValueStoreMap.get(fLocalId);
      if (paramIdentityConstraint.getCategory() == 2) {
        return;
      }
      XMLSchemaValidator.ValueStoreBase localValueStoreBase2 = (XMLSchemaValidator.ValueStoreBase)fGlobalIDConstraintMap.get(paramIdentityConstraint);
      if (localValueStoreBase2 != null)
      {
        localValueStoreBase2.append(localValueStoreBase1);
        fGlobalIDConstraintMap.put(paramIdentityConstraint, localValueStoreBase2);
      }
      else
      {
        fGlobalIDConstraintMap.put(paramIdentityConstraint, localValueStoreBase1);
      }
    }
    
    public void endDocument()
    {
      int i = fValueStores.size();
      for (int j = 0; j < i; j++)
      {
        XMLSchemaValidator.ValueStoreBase localValueStoreBase = (XMLSchemaValidator.ValueStoreBase)fValueStores.get(j);
        localValueStoreBase.endDocument();
      }
    }
    
    public String toString()
    {
      String str = super.toString();
      int i = str.lastIndexOf('$');
      if (i != -1) {
        return str.substring(i + 1);
      }
      int j = str.lastIndexOf('.');
      if (j != -1) {
        return str.substring(j + 1);
      }
      return str;
    }
  }
  
  protected class KeyRefValueStore
    extends XMLSchemaValidator.ValueStoreBase
  {
    protected XMLSchemaValidator.ValueStoreBase fKeyValueStore;
    
    public KeyRefValueStore(KeyRef paramKeyRef, XMLSchemaValidator.KeyValueStore paramKeyValueStore)
    {
      super(paramKeyRef);
      fKeyValueStore = paramKeyValueStore;
    }
    
    public void endDocumentFragment()
    {
      super.endDocumentFragment();
      fKeyValueStore = ((XMLSchemaValidator.ValueStoreBase)fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef)fIdentityConstraint).getKey()));
      String str2;
      if (fKeyValueStore == null)
      {
        String str1 = "KeyRefOutOfScope";
        str2 = fIdentityConstraint.toString();
        reportSchemaError(str1, new Object[] { str2 });
        return;
      }
      int i = fKeyValueStore.contains(this);
      if (i != -1)
      {
        str2 = "KeyNotFound";
        String str3 = toString(fValues, i, fFieldCount);
        String str4 = fIdentityConstraint.getElementName();
        String str5 = fIdentityConstraint.getName();
        reportSchemaError(str2, new Object[] { str5, str3, str4 });
      }
    }
    
    public void endDocument()
    {
      super.endDocument();
    }
  }
  
  protected class KeyValueStore
    extends XMLSchemaValidator.ValueStoreBase
  {
    public KeyValueStore(UniqueOrKey paramUniqueOrKey)
    {
      super(paramUniqueOrKey);
    }
    
    protected void checkDuplicateValues()
    {
      if (contains())
      {
        String str1 = "DuplicateKey";
        String str2 = toString(fLocalValues);
        String str3 = fIdentityConstraint.getElementName();
        String str4 = fIdentityConstraint.getIdentityConstraintName();
        reportSchemaError(str1, new Object[] { str2, str3, str4 });
      }
    }
  }
  
  protected class UniqueValueStore
    extends XMLSchemaValidator.ValueStoreBase
  {
    public UniqueValueStore(UniqueOrKey paramUniqueOrKey)
    {
      super(paramUniqueOrKey);
    }
    
    protected void checkDuplicateValues()
    {
      if (contains())
      {
        String str1 = "DuplicateUnique";
        String str2 = toString(fLocalValues);
        String str3 = fIdentityConstraint.getElementName();
        String str4 = fIdentityConstraint.getIdentityConstraintName();
        reportSchemaError(str1, new Object[] { str2, str3, str4 });
      }
    }
  }
  
  protected abstract class ValueStoreBase
    implements ValueStore
  {
    protected IdentityConstraint fIdentityConstraint;
    protected int fFieldCount = 0;
    protected Field[] fFields = null;
    protected Object[] fLocalValues = null;
    protected short[] fLocalValueTypes = null;
    protected ShortList[] fLocalItemValueTypes = null;
    protected int fValuesCount;
    public final Vector fValues = new Vector();
    public XMLSchemaValidator.ShortVector fValueTypes = null;
    public Vector fItemValueTypes = null;
    private boolean fUseValueTypeVector = false;
    private int fValueTypesLength = 0;
    private short fValueType = 0;
    private boolean fUseItemValueTypeVector = false;
    private int fItemValueTypesLength = 0;
    private ShortList fItemValueType = null;
    final StringBuffer fTempBuffer = new StringBuffer();
    
    protected ValueStoreBase(IdentityConstraint paramIdentityConstraint)
    {
      fIdentityConstraint = paramIdentityConstraint;
      fFieldCount = fIdentityConstraint.getFieldCount();
      fFields = new Field[fFieldCount];
      fLocalValues = new Object[fFieldCount];
      fLocalValueTypes = new short[fFieldCount];
      fLocalItemValueTypes = new ShortList[fFieldCount];
      for (int i = 0; i < fFieldCount; i++) {
        fFields[i] = fIdentityConstraint.getFieldAt(i);
      }
    }
    
    public void clear()
    {
      fValuesCount = 0;
      fUseValueTypeVector = false;
      fValueTypesLength = 0;
      fValueType = 0;
      fUseItemValueTypeVector = false;
      fItemValueTypesLength = 0;
      fItemValueType = null;
      fValues.setSize(0);
      if (fValueTypes != null) {
        fValueTypes.clear();
      }
      if (fItemValueTypes != null) {
        fItemValueTypes.setSize(0);
      }
    }
    
    public void append(ValueStoreBase paramValueStoreBase)
    {
      for (int i = 0; i < fValues.size(); i++) {
        fValues.addElement(fValues.elementAt(i));
      }
    }
    
    public void startValueScope()
    {
      fValuesCount = 0;
      for (int i = 0; i < fFieldCount; i++)
      {
        fLocalValues[i] = null;
        fLocalValueTypes[i] = 0;
        fLocalItemValueTypes[i] = null;
      }
    }
    
    public void endValueScope()
    {
      String str1;
      Object localObject;
      String str2;
      if (fValuesCount == 0)
      {
        if (fIdentityConstraint.getCategory() == 1)
        {
          str1 = "AbsentKeyValue";
          localObject = fIdentityConstraint.getElementName();
          str2 = fIdentityConstraint.getIdentityConstraintName();
          reportSchemaError(str1, new Object[] { localObject, str2 });
        }
        return;
      }
      if (fValuesCount != fFieldCount)
      {
        if (fIdentityConstraint.getCategory() == 1)
        {
          str1 = "KeyNotEnoughValues";
          localObject = (UniqueOrKey)fIdentityConstraint;
          str2 = fIdentityConstraint.getElementName();
          String str3 = ((UniqueOrKey)localObject).getIdentityConstraintName();
          reportSchemaError(str1, new Object[] { str2, str3 });
        }
        return;
      }
    }
    
    public void endDocumentFragment() {}
    
    public void endDocument() {}
    
    public void reportError(String paramString, Object[] paramArrayOfObject)
    {
      reportSchemaError(paramString, paramArrayOfObject);
    }
    
    public void addValue(Field paramField, boolean paramBoolean, Object paramObject, short paramShort, ShortList paramShortList)
    {
      for (int i = fFieldCount - 1; i > -1; i--) {
        if (fFields[i] == paramField) {
          break;
        }
      }
      String str1;
      String str2;
      if (i == -1)
      {
        str1 = "UnknownField";
        str2 = fIdentityConstraint.getElementName();
        String str3 = fIdentityConstraint.getIdentityConstraintName();
        reportSchemaError(str1, new Object[] { paramField.toString(), str2, str3 });
        return;
      }
      if (!paramBoolean)
      {
        str1 = "FieldMultipleMatch";
        str2 = fIdentityConstraint.getIdentityConstraintName();
        reportSchemaError(str1, new Object[] { paramField.toString(), str2 });
      }
      else
      {
        fValuesCount += 1;
      }
      fLocalValues[i] = paramObject;
      fLocalValueTypes[i] = paramShort;
      fLocalItemValueTypes[i] = paramShortList;
      if (fValuesCount == fFieldCount)
      {
        checkDuplicateValues();
        for (i = 0; i < fFieldCount; i++)
        {
          fValues.addElement(fLocalValues[i]);
          addValueType(fLocalValueTypes[i]);
          addItemValueType(fLocalItemValueTypes[i]);
        }
      }
    }
    
    public boolean contains()
    {
      int i = 0;
      int j = fValues.size();
      for (int k = 0; k < j; k = i)
      {
        i = k + fFieldCount;
        for (int m = 0; m < fFieldCount; m++)
        {
          Object localObject1 = fLocalValues[m];
          Object localObject2 = fValues.elementAt(k);
          int n = fLocalValueTypes[m];
          int i1 = getValueTypeAt(k);
          if ((localObject1 == null) || (localObject2 == null) || (n != i1) || (!localObject1.equals(localObject2))) {
            break;
          }
          if ((n == 44) || (n == 43))
          {
            ShortList localShortList1 = fLocalItemValueTypes[m];
            ShortList localShortList2 = getItemValueTypeAt(k);
            if ((localShortList1 == null) || (localShortList2 == null) || (!localShortList1.equals(localShortList2))) {
              break;
            }
          }
          k++;
        }
        return true;
      }
      return false;
    }
    
    public int contains(ValueStoreBase paramValueStoreBase)
    {
      Vector localVector = fValues;
      int i = localVector.size();
      int j;
      int k;
      if (fFieldCount <= 1)
      {
        for (j = 0; j < i; j++)
        {
          k = paramValueStoreBase.getValueTypeAt(j);
          if ((!valueTypeContains(k)) || (!fValues.contains(localVector.elementAt(j)))) {
            return j;
          }
          if ((k == 44) || (k == 43))
          {
            ShortList localShortList1 = paramValueStoreBase.getItemValueTypeAt(j);
            if (!itemValueTypeContains(localShortList1)) {
              return j;
            }
          }
        }
      }
      else
      {
        j = fValues.size();
        k = 0;
        int m;
        while (m < i)
        {
          int n = 0;
          while (n < j)
          {
            for (int i1 = 0; i1 < fFieldCount; i1++)
            {
              Object localObject1 = localVector.elementAt(k + i1);
              Object localObject2 = fValues.elementAt(n + i1);
              int i2 = paramValueStoreBase.getValueTypeAt(k + i1);
              int i3 = getValueTypeAt(n + i1);
              if ((localObject1 != localObject2) && ((i2 != i3) || (localObject1 == null) || (!localObject1.equals(localObject2)))) {
                break;
              }
              if ((i2 == 44) || (i2 == 43))
              {
                ShortList localShortList2 = paramValueStoreBase.getItemValueTypeAt(k + i1);
                ShortList localShortList3 = getItemValueTypeAt(n + i1);
                if ((localShortList2 == null) || (localShortList3 == null) || (!localShortList2.equals(localShortList3))) {
                  break;
                }
              }
            }
            break;
            n += fFieldCount;
          }
          return k;
          k += fFieldCount;
        }
      }
      return -1;
    }
    
    protected void checkDuplicateValues() {}
    
    protected String toString(Object[] paramArrayOfObject)
    {
      int i = paramArrayOfObject.length;
      if (i == 0) {
        return "";
      }
      fTempBuffer.setLength(0);
      for (int j = 0; j < i; j++)
      {
        if (j > 0) {
          fTempBuffer.append(',');
        }
        fTempBuffer.append(paramArrayOfObject[j]);
      }
      return fTempBuffer.toString();
    }
    
    protected String toString(Vector paramVector, int paramInt1, int paramInt2)
    {
      if (paramInt2 == 0) {
        return "";
      }
      if (paramInt2 == 1) {
        return String.valueOf(paramVector.elementAt(paramInt1));
      }
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < paramInt2; i++)
      {
        if (i > 0) {
          localStringBuffer.append(',');
        }
        localStringBuffer.append(paramVector.elementAt(paramInt1 + i));
      }
      return localStringBuffer.toString();
    }
    
    public String toString()
    {
      String str = super.toString();
      int i = str.lastIndexOf('$');
      if (i != -1) {
        str = str.substring(i + 1);
      }
      int j = str.lastIndexOf('.');
      if (j != -1) {
        str = str.substring(j + 1);
      }
      return str + '[' + fIdentityConstraint + ']';
    }
    
    private void addValueType(short paramShort)
    {
      if (fUseValueTypeVector)
      {
        fValueTypes.add(paramShort);
      }
      else if (fValueTypesLength++ == 0)
      {
        fValueType = paramShort;
      }
      else if (fValueType != paramShort)
      {
        fUseValueTypeVector = true;
        if (fValueTypes == null) {
          fValueTypes = new XMLSchemaValidator.ShortVector(fValueTypesLength * 2);
        }
        for (int i = 1; i < fValueTypesLength; i++) {
          fValueTypes.add(fValueType);
        }
        fValueTypes.add(paramShort);
      }
    }
    
    private short getValueTypeAt(int paramInt)
    {
      if (fUseValueTypeVector) {
        return fValueTypes.valueAt(paramInt);
      }
      return fValueType;
    }
    
    private boolean valueTypeContains(short paramShort)
    {
      if (fUseValueTypeVector) {
        return fValueTypes.contains(paramShort);
      }
      return fValueType == paramShort;
    }
    
    private void addItemValueType(ShortList paramShortList)
    {
      if (fUseItemValueTypeVector)
      {
        fItemValueTypes.add(paramShortList);
      }
      else if (fItemValueTypesLength++ == 0)
      {
        fItemValueType = paramShortList;
      }
      else if ((fItemValueType != paramShortList) && ((fItemValueType == null) || (!fItemValueType.equals(paramShortList))))
      {
        fUseItemValueTypeVector = true;
        if (fItemValueTypes == null) {
          fItemValueTypes = new Vector(fItemValueTypesLength * 2);
        }
        for (int i = 1; i < fItemValueTypesLength; i++) {
          fItemValueTypes.add(fItemValueType);
        }
        fItemValueTypes.add(paramShortList);
      }
    }
    
    private ShortList getItemValueTypeAt(int paramInt)
    {
      if (fUseItemValueTypeVector) {
        return (ShortList)fItemValueTypes.elementAt(paramInt);
      }
      return fItemValueType;
    }
    
    private boolean itemValueTypeContains(ShortList paramShortList)
    {
      if (fUseItemValueTypeVector) {
        return fItemValueTypes.contains(paramShortList);
      }
      return (fItemValueType == paramShortList) || ((fItemValueType != null) && (fItemValueType.equals(paramShortList)));
    }
  }
  
  protected static class XPathMatcherStack
  {
    protected XPathMatcher[] fMatchers = new XPathMatcher[4];
    protected int fMatchersCount;
    protected IntStack fContextStack = new IntStack();
    
    public XPathMatcherStack() {}
    
    public void clear()
    {
      for (int i = 0; i < fMatchersCount; i++) {
        fMatchers[i] = null;
      }
      fMatchersCount = 0;
      fContextStack.clear();
    }
    
    public int size()
    {
      return fContextStack.size();
    }
    
    public int getMatcherCount()
    {
      return fMatchersCount;
    }
    
    public void addMatcher(XPathMatcher paramXPathMatcher)
    {
      ensureMatcherCapacity();
      fMatchers[(fMatchersCount++)] = paramXPathMatcher;
    }
    
    public XPathMatcher getMatcherAt(int paramInt)
    {
      return fMatchers[paramInt];
    }
    
    public void pushContext()
    {
      fContextStack.push(fMatchersCount);
    }
    
    public void popContext()
    {
      fMatchersCount = fContextStack.pop();
    }
    
    private void ensureMatcherCapacity()
    {
      if (fMatchersCount == fMatchers.length)
      {
        XPathMatcher[] arrayOfXPathMatcher = new XPathMatcher[fMatchers.length * 2];
        System.arraycopy(fMatchers, 0, arrayOfXPathMatcher, 0, fMatchers.length);
        fMatchers = arrayOfXPathMatcher;
      }
    }
  }
  
  protected final class XSIErrorReporter
  {
    XMLErrorReporter fErrorReporter;
    Vector fErrors = new Vector();
    int[] fContext = new int[8];
    int fContextCount;
    
    protected XSIErrorReporter() {}
    
    public void reset(XMLErrorReporter paramXMLErrorReporter)
    {
      fErrorReporter = paramXMLErrorReporter;
      fErrors.removeAllElements();
      fContextCount = 0;
    }
    
    public void pushContext()
    {
      if (!fAugPSVI) {
        return;
      }
      if (fContextCount == fContext.length)
      {
        int i = fContextCount + 8;
        int[] arrayOfInt = new int[i];
        System.arraycopy(fContext, 0, arrayOfInt, 0, fContextCount);
        fContext = arrayOfInt;
      }
      fContext[(fContextCount++)] = fErrors.size();
    }
    
    public String[] popContext()
    {
      if (!fAugPSVI) {
        return null;
      }
      int i = fContext[(--fContextCount)];
      int j = fErrors.size() - i;
      if (j == 0) {
        return null;
      }
      String[] arrayOfString = new String[j];
      for (int k = 0; k < j; k++) {
        arrayOfString[k] = ((String)fErrors.elementAt(i + k));
      }
      fErrors.setSize(i);
      return arrayOfString;
    }
    
    public String[] mergeContext()
    {
      if (!fAugPSVI) {
        return null;
      }
      int i = fContext[(--fContextCount)];
      int j = fErrors.size() - i;
      if (j == 0) {
        return null;
      }
      String[] arrayOfString = new String[j];
      for (int k = 0; k < j; k++) {
        arrayOfString[k] = ((String)fErrors.elementAt(i + k));
      }
      return arrayOfString;
    }
    
    public void reportError(String paramString1, String paramString2, Object[] paramArrayOfObject, short paramShort)
      throws XNIException
    {
      String str = fErrorReporter.reportError(paramString1, paramString2, paramArrayOfObject, paramShort);
      if (fAugPSVI)
      {
        fErrors.addElement(paramString2);
        fErrors.addElement(str);
      }
    }
    
    public void reportError(XMLLocator paramXMLLocator, String paramString1, String paramString2, Object[] paramArrayOfObject, short paramShort)
      throws XNIException
    {
      String str = fErrorReporter.reportError(paramXMLLocator, paramString1, paramString2, paramArrayOfObject, paramShort);
      if (fAugPSVI)
      {
        fErrors.addElement(paramString2);
        fErrors.addElement(str);
      }
    }
  }
}
