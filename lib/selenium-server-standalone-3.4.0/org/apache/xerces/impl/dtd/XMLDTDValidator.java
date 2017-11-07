package org.apache.xerces.impl.dtd;

import java.io.IOException;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
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
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XMLDTDValidator
  implements XMLComponent, XMLDocumentFilter, XMLDTDValidatorFilter, RevalidationHandler
{
  private static final int TOP_LEVEL_SCOPE = -1;
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String VALIDATION = "http://xml.org/sax/features/validation";
  protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
  protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
  protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
  protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
  protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/balance-syntax-trees" };
  private static final Boolean[] FEATURE_DEFAULTS = { null, null, Boolean.FALSE, Boolean.FALSE };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, null };
  private static final boolean DEBUG_ATTRIBUTES = false;
  private static final boolean DEBUG_ELEMENT_CHILDREN = false;
  protected ValidationManager fValidationManager = null;
  protected final ValidationState fValidationState = new ValidationState();
  protected boolean fNamespaces;
  protected boolean fValidation;
  protected boolean fDTDValidation;
  protected boolean fDynamicValidation;
  protected boolean fBalanceSyntaxTrees;
  protected boolean fWarnDuplicateAttdef;
  protected SymbolTable fSymbolTable;
  protected XMLErrorReporter fErrorReporter;
  protected XMLGrammarPool fGrammarPool;
  protected DTDGrammarBucket fGrammarBucket;
  protected XMLLocator fDocLocation;
  protected NamespaceContext fNamespaceContext = null;
  protected DTDDVFactory fDatatypeValidatorFactory;
  protected XMLDocumentHandler fDocumentHandler;
  protected XMLDocumentSource fDocumentSource;
  protected DTDGrammar fDTDGrammar;
  protected boolean fSeenDoctypeDecl = false;
  private boolean fPerformValidation;
  private String fSchemaType;
  private final QName fCurrentElement = new QName();
  private int fCurrentElementIndex = -1;
  private int fCurrentContentSpecType = -1;
  private final QName fRootElement = new QName();
  private boolean fInCDATASection = false;
  private int[] fElementIndexStack = new int[8];
  private int[] fContentSpecTypeStack = new int[8];
  private QName[] fElementQNamePartsStack = new QName[8];
  private QName[] fElementChildren = new QName[32];
  private int fElementChildrenLength = 0;
  private int[] fElementChildrenOffsetStack = new int[32];
  private int fElementDepth = -1;
  private boolean fSeenRootElement = false;
  private boolean fInElementContent = false;
  private XMLElementDecl fTempElementDecl = new XMLElementDecl();
  private final XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
  private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
  private final QName fTempQName = new QName();
  private final StringBuffer fBuffer = new StringBuffer();
  protected DatatypeValidator fValID;
  protected DatatypeValidator fValIDRef;
  protected DatatypeValidator fValIDRefs;
  protected DatatypeValidator fValENTITY;
  protected DatatypeValidator fValENTITIES;
  protected DatatypeValidator fValNMTOKEN;
  protected DatatypeValidator fValNMTOKENS;
  protected DatatypeValidator fValNOTATION;
  
  public XMLDTDValidator()
  {
    for (int i = 0; i < fElementQNamePartsStack.length; i++) {
      fElementQNamePartsStack[i] = new QName();
    }
    fGrammarBucket = new DTDGrammarBucket();
  }
  
  DTDGrammarBucket getGrammarBucket()
  {
    return fGrammarBucket;
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    fDTDGrammar = null;
    fSeenDoctypeDecl = false;
    fInCDATASection = false;
    fSeenRootElement = false;
    fInElementContent = false;
    fCurrentElementIndex = -1;
    fCurrentContentSpecType = -1;
    fRootElement.clear();
    fValidationState.resetIDTables();
    fGrammarBucket.clear();
    fElementDepth = -1;
    fElementChildrenLength = 0;
    boolean bool;
    try
    {
      bool = paramXMLComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings");
    }
    catch (XMLConfigurationException localXMLConfigurationException1)
    {
      bool = true;
    }
    if (!bool)
    {
      fValidationManager.addValidationState(fValidationState);
      return;
    }
    try
    {
      fNamespaces = paramXMLComponentManager.getFeature("http://xml.org/sax/features/namespaces");
    }
    catch (XMLConfigurationException localXMLConfigurationException2)
    {
      fNamespaces = true;
    }
    try
    {
      fValidation = paramXMLComponentManager.getFeature("http://xml.org/sax/features/validation");
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fValidation = false;
    }
    try
    {
      fDTDValidation = (!paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/schema"));
    }
    catch (XMLConfigurationException localXMLConfigurationException4)
    {
      fDTDValidation = true;
    }
    try
    {
      fDynamicValidation = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/dynamic");
    }
    catch (XMLConfigurationException localXMLConfigurationException5)
    {
      fDynamicValidation = false;
    }
    try
    {
      fBalanceSyntaxTrees = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/balance-syntax-trees");
    }
    catch (XMLConfigurationException localXMLConfigurationException6)
    {
      fBalanceSyntaxTrees = false;
    }
    try
    {
      fWarnDuplicateAttdef = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef");
    }
    catch (XMLConfigurationException localXMLConfigurationException7)
    {
      fWarnDuplicateAttdef = false;
    }
    try
    {
      fSchemaType = ((String)paramXMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage"));
    }
    catch (XMLConfigurationException localXMLConfigurationException8)
    {
      fSchemaType = null;
    }
    fValidationManager = ((ValidationManager)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
    fValidationManager.addValidationState(fValidationState);
    fValidationState.setUsingNamespaces(fNamespaces);
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    try
    {
      fGrammarPool = ((XMLGrammarPool)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool"));
    }
    catch (XMLConfigurationException localXMLConfigurationException9)
    {
      fGrammarPool = null;
    }
    fDatatypeValidatorFactory = ((DTDDVFactory)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/datatype-validator-factory"));
    init();
  }
  
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
  {}
  
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
    if (fGrammarPool != null)
    {
      Grammar[] arrayOfGrammar = fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/TR/REC-xml");
      int i = arrayOfGrammar != null ? arrayOfGrammar.length : 0;
      for (int j = 0; j < i; j++) {
        fGrammarBucket.putGrammar((DTDGrammar)arrayOfGrammar[j]);
      }
    }
    fDocLocation = paramXMLLocator;
    fNamespaceContext = paramNamespaceContext;
    if (fDocumentHandler != null) {
      fDocumentHandler.startDocument(paramXMLLocator, paramString, paramNamespaceContext, paramAugmentations);
    }
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    fGrammarBucket.setStandalone((paramString3 != null) && (paramString3.equals("yes")));
    if (fDocumentHandler != null) {
      fDocumentHandler.xmlDecl(paramString1, paramString2, paramString3, paramAugmentations);
    }
  }
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    fSeenDoctypeDecl = true;
    fRootElement.setValues(null, paramString1, paramString1, null);
    String str = null;
    try
    {
      str = XMLEntityManager.expandSystemId(paramString3, fDocLocation.getExpandedSystemId(), false);
    }
    catch (IOException localIOException) {}
    XMLDTDDescription localXMLDTDDescription = new XMLDTDDescription(paramString2, paramString3, fDocLocation.getExpandedSystemId(), str, paramString1);
    fDTDGrammar = fGrammarBucket.getGrammar(localXMLDTDDescription);
    if ((fDTDGrammar == null) && (fGrammarPool != null) && ((paramString3 != null) || (paramString2 != null))) {
      fDTDGrammar = ((DTDGrammar)fGrammarPool.retrieveGrammar(localXMLDTDDescription));
    }
    if (fDTDGrammar == null)
    {
      if (!fBalanceSyntaxTrees) {
        fDTDGrammar = new DTDGrammar(fSymbolTable, localXMLDTDDescription);
      } else {
        fDTDGrammar = new BalancedDTDGrammar(fSymbolTable, localXMLDTDDescription);
      }
    }
    else {
      fValidationManager.setCachedDTD(true);
    }
    fGrammarBucket.setActiveGrammar(fDTDGrammar);
    if (fDocumentHandler != null) {
      fDocumentHandler.doctypeDecl(paramString1, paramString2, paramString3, paramAugmentations);
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    handleStartElement(paramQName, paramXMLAttributes, paramAugmentations);
    if (fDocumentHandler != null) {
      fDocumentHandler.startElement(paramQName, paramXMLAttributes, paramAugmentations);
    }
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    boolean bool = handleStartElement(paramQName, paramXMLAttributes, paramAugmentations);
    if (fDocumentHandler != null) {
      fDocumentHandler.emptyElement(paramQName, paramXMLAttributes, paramAugmentations);
    }
    if (!bool) {
      handleEndElement(paramQName, paramAugmentations, true);
    }
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    int i = 1;
    int j = 1;
    for (int k = offset; k < offset + length; k++) {
      if (!isSpace(ch[k]))
      {
        j = 0;
        break;
      }
    }
    if ((fInElementContent) && (j != 0) && (!fInCDATASection) && (fDocumentHandler != null))
    {
      fDocumentHandler.ignorableWhitespace(paramXMLString, paramAugmentations);
      i = 0;
    }
    if (fPerformValidation)
    {
      if (fInElementContent)
      {
        if ((fGrammarBucket.getStandalone()) && (fDTDGrammar.getElementDeclIsExternal(fCurrentElementIndex)) && (j != 0)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE", null, (short)1);
        }
        if (j == 0) {
          charDataInContent();
        }
        if ((paramAugmentations != null) && (paramAugmentations.getItem("CHAR_REF_PROBABLE_WS") == Boolean.TRUE)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, fDTDGrammar.getContentSpecAsString(fElementDepth), "character reference" }, (short)1);
        }
      }
      if (fCurrentContentSpecType == 1) {
        charDataInContent();
      }
    }
    if ((i != 0) && (fDocumentHandler != null)) {
      fDocumentHandler.characters(paramXMLString, paramAugmentations);
    }
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.ignorableWhitespace(paramXMLString, paramAugmentations);
    }
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    handleEndElement(paramQName, paramAugmentations, false);
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fPerformValidation) && (fInElementContent)) {
      charDataInContent();
    }
    fInCDATASection = true;
    if (fDocumentHandler != null) {
      fDocumentHandler.startCDATA(paramAugmentations);
    }
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATASection = false;
    if (fDocumentHandler != null) {
      fDocumentHandler.endCDATA(paramAugmentations);
    }
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.endDocument(paramAugmentations);
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fPerformValidation) && (fElementDepth >= 0) && (fDTDGrammar != null))
    {
      fDTDGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
      if (fTempElementDecl.type == 1) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, "EMPTY", "comment" }, (short)1);
      }
    }
    if (fDocumentHandler != null) {
      fDocumentHandler.comment(paramXMLString, paramAugmentations);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fPerformValidation) && (fElementDepth >= 0) && (fDTDGrammar != null))
    {
      fDTDGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
      if (fTempElementDecl.type == 1) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, "EMPTY", "processing instruction" }, (short)1);
      }
    }
    if (fDocumentHandler != null) {
      fDocumentHandler.processingInstruction(paramString, paramXMLString, paramAugmentations);
    }
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fPerformValidation) && (fElementDepth >= 0) && (fDTDGrammar != null))
    {
      fDTDGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
      if (fTempElementDecl.type == 1) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, "EMPTY", "ENTITY" }, (short)1);
      }
      if (fGrammarBucket.getStandalone()) {
        XMLDTDLoader.checkStandaloneEntityRef(paramString1, fDTDGrammar, fEntityDecl, fErrorReporter);
      }
    }
    if (fDocumentHandler != null) {
      fDocumentHandler.startGeneralEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.endGeneralEntity(paramString, paramAugmentations);
    }
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fDocumentHandler != null) {
      fDocumentHandler.textDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public final boolean hasGrammar()
  {
    return fDTDGrammar != null;
  }
  
  public final boolean validate()
  {
    return (fSchemaType != Constants.NS_XMLSCHEMA) && (((!fDynamicValidation) && (fValidation)) || ((fDynamicValidation) && (fSeenDoctypeDecl) && ((fDTDValidation) || (fSeenDoctypeDecl))));
  }
  
  protected void addDTDDefaultAttrsAndValidate(QName paramQName, int paramInt, XMLAttributes paramXMLAttributes)
    throws XNIException
  {
    if ((paramInt == -1) || (fDTDGrammar == null)) {
      return;
    }
    String str3;
    String str6;
    boolean bool;
    for (int i = fDTDGrammar.getFirstAttributeDeclIndex(paramInt); i != -1; i = fDTDGrammar.getNextAttributeDeclIndex(i))
    {
      fDTDGrammar.getAttributeDecl(i, fTempAttDecl);
      String str1 = fTempAttDecl.name.prefix;
      String str2 = fTempAttDecl.name.localpart;
      str3 = fTempAttDecl.name.rawname;
      String str4 = getAttributeTypeName(fTempAttDecl);
      int n = fTempAttDecl.simpleType.defaultType;
      str6 = null;
      if (fTempAttDecl.simpleType.defaultValue != null) {
        str6 = fTempAttDecl.simpleType.defaultValue;
      }
      int i3 = 0;
      bool = n == 2;
      int i4 = str4 == XMLSymbols.fCDATASymbol ? 1 : 0;
      if ((i4 == 0) || (bool) || (str6 != null))
      {
        int i5 = paramXMLAttributes.getLength();
        for (int i7 = 0; i7 < i5; i7++) {
          if (paramXMLAttributes.getQName(i7) == str3)
          {
            i3 = 1;
            break;
          }
        }
      }
      if (i3 == 0)
      {
        Object[] arrayOfObject1;
        if (bool)
        {
          if (fPerformValidation)
          {
            arrayOfObject1 = new Object[] { localpart, str3 };
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED", arrayOfObject1, (short)1);
          }
        }
        else if (str6 != null)
        {
          if ((fPerformValidation) && (fGrammarBucket.getStandalone()) && (fDTDGrammar.getAttributeDeclIsExternal(i)))
          {
            arrayOfObject1 = new Object[] { localpart, str3 };
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED", arrayOfObject1, (short)1);
          }
          if (fNamespaces)
          {
            i6 = str3.indexOf(':');
            if (i6 != -1)
            {
              str1 = str3.substring(0, i6);
              str1 = fSymbolTable.addSymbol(str1);
              str2 = str3.substring(i6 + 1);
              str2 = fSymbolTable.addSymbol(str2);
            }
          }
          fTempQName.setValues(str1, str2, str3, fTempAttDecl.name.uri);
          int i6 = paramXMLAttributes.addAttribute(fTempQName, str4, str6);
        }
      }
    }
    int j = paramXMLAttributes.getLength();
    for (int k = 0; k < j; k++)
    {
      str3 = paramXMLAttributes.getQName(k);
      int m = 0;
      if ((fPerformValidation) && (fGrammarBucket.getStandalone()))
      {
        String str5 = paramXMLAttributes.getNonNormalizedValue(k);
        if (str5 != null)
        {
          str6 = getExternalEntityRefInAttrValue(str5);
          if (str6 != null) {
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { str6 }, (short)1);
          }
        }
      }
      int i1 = -1;
      for (int i2 = fDTDGrammar.getFirstAttributeDeclIndex(paramInt); i2 != -1; i2 = fDTDGrammar.getNextAttributeDeclIndex(i2))
      {
        fDTDGrammar.getAttributeDecl(i2, fTempAttDecl);
        if (fTempAttDecl.name.rawname == str3)
        {
          i1 = i2;
          m = 1;
          break;
        }
      }
      Object localObject;
      if (m == 0)
      {
        if (fPerformValidation)
        {
          localObject = new Object[] { rawname, str3 };
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_NOT_DECLARED", (Object[])localObject, (short)1);
        }
      }
      else
      {
        localObject = getAttributeTypeName(fTempAttDecl);
        paramXMLAttributes.setType(k, (String)localObject);
        paramXMLAttributes.getAugmentations(k).putItem("ATTRIBUTE_DECLARED", Boolean.TRUE);
        bool = false;
        String str7 = paramXMLAttributes.getValue(k);
        String str8 = str7;
        if ((paramXMLAttributes.isSpecified(k)) && (localObject != XMLSymbols.fCDATASymbol))
        {
          bool = normalizeAttrValue(paramXMLAttributes, k);
          str8 = paramXMLAttributes.getValue(k);
          if ((fPerformValidation) && (fGrammarBucket.getStandalone()) && (bool) && (fDTDGrammar.getAttributeDeclIsExternal(i2))) {
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE", new Object[] { str3, str7, str8 }, (short)1);
          }
        }
        if (fPerformValidation)
        {
          if (fTempAttDecl.simpleType.defaultType == 1)
          {
            String str9 = fTempAttDecl.simpleType.defaultValue;
            if (!str8.equals(str9))
            {
              Object[] arrayOfObject2 = { localpart, str3, str8, str9 };
              fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_FIXED_ATTVALUE_INVALID", arrayOfObject2, (short)1);
            }
          }
          if ((fTempAttDecl.simpleType.type == 1) || (fTempAttDecl.simpleType.type == 2) || (fTempAttDecl.simpleType.type == 3) || (fTempAttDecl.simpleType.type == 4) || (fTempAttDecl.simpleType.type == 5) || (fTempAttDecl.simpleType.type == 6)) {
            validateDTDattribute(paramQName, str8, fTempAttDecl);
          }
        }
      }
    }
  }
  
  protected String getExternalEntityRefInAttrValue(String paramString)
  {
    int i = paramString.length();
    for (int j = paramString.indexOf('&'); j != -1; j = paramString.indexOf('&', j + 1)) {
      if ((j + 1 < i) && (paramString.charAt(j + 1) != '#'))
      {
        int k = paramString.indexOf(';', j + 1);
        String str = paramString.substring(j + 1, k);
        str = fSymbolTable.addSymbol(str);
        int m = fDTDGrammar.getEntityDeclIndex(str);
        if (m > -1)
        {
          fDTDGrammar.getEntityDecl(m, fEntityDecl);
          if ((fEntityDecl.inExternal) || ((str = getExternalEntityRefInAttrValue(fEntityDecl.value)) != null)) {
            return str;
          }
        }
      }
    }
    return null;
  }
  
  protected void validateDTDattribute(QName paramQName, String paramString, XMLAttributeDecl paramXMLAttributeDecl)
    throws XNIException
  {
    boolean bool1;
    boolean bool2;
    switch (simpleType.type)
    {
    case 1: 
      bool1 = simpleType.list;
      try
      {
        if (bool1) {
          fValENTITIES.validate(paramString, fValidationState);
        } else {
          fValENTITY.validate(paramString, fValidationState);
        }
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException2)
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", localInvalidDatatypeValueException2.getKey(), localInvalidDatatypeValueException2.getArgs(), (short)1);
      }
    case 2: 
    case 6: 
      bool1 = false;
      String[] arrayOfString = simpleType.enumeration;
      if (arrayOfString == null) {
        bool1 = false;
      } else {
        for (int i = 0; i < arrayOfString.length; i++) {
          if ((paramString == arrayOfString[i]) || (paramString.equals(arrayOfString[i])))
          {
            bool1 = true;
            break;
          }
        }
      }
      if (!bool1)
      {
        StringBuffer localStringBuffer = new StringBuffer();
        if (arrayOfString != null) {
          for (int j = 0; j < arrayOfString.length; j++) {
            localStringBuffer.append(arrayOfString[j] + " ");
          }
        }
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_VALUE_NOT_IN_LIST", new Object[] { name.rawname, paramString, localStringBuffer }, (short)1);
      }
      break;
    case 3: 
      try
      {
        fValID.validate(paramString, fValidationState);
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException1)
      {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", localInvalidDatatypeValueException1.getKey(), localInvalidDatatypeValueException1.getArgs(), (short)1);
      }
    case 4: 
      bool2 = simpleType.list;
      try
      {
        if (bool2) {
          fValIDRefs.validate(paramString, fValidationState);
        } else {
          fValIDRef.validate(paramString, fValidationState);
        }
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException3)
      {
        if (bool2) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDREFSInvalid", new Object[] { paramString }, (short)1);
        } else {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", localInvalidDatatypeValueException3.getKey(), localInvalidDatatypeValueException3.getArgs(), (short)1);
        }
      }
    case 5: 
      bool2 = simpleType.list;
      try
      {
        if (bool2) {
          fValNMTOKENS.validate(paramString, fValidationState);
        } else {
          fValNMTOKEN.validate(paramString, fValidationState);
        }
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException4)
      {
        if (bool2) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENSInvalid", new Object[] { paramString }, (short)1);
        } else {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENInvalid", new Object[] { paramString }, (short)1);
        }
      }
    }
  }
  
  protected boolean invalidStandaloneAttDef(QName paramQName1, QName paramQName2)
  {
    boolean bool = true;
    return bool;
  }
  
  private boolean normalizeAttrValue(XMLAttributes paramXMLAttributes, int paramInt)
  {
    int i = 1;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    String str1 = paramXMLAttributes.getValue(paramInt);
    char[] arrayOfChar = new char[str1.length()];
    fBuffer.setLength(0);
    str1.getChars(0, str1.length(), arrayOfChar, 0);
    for (int i1 = 0; i1 < arrayOfChar.length; i1++) {
      if (arrayOfChar[i1] == ' ')
      {
        if (k != 0)
        {
          j = 1;
          k = 0;
        }
        if ((j != 0) && (i == 0))
        {
          j = 0;
          fBuffer.append(arrayOfChar[i1]);
          m++;
        }
        else if ((i != 0) || (j == 0))
        {
          n++;
        }
      }
      else
      {
        k = 1;
        j = 0;
        i = 0;
        fBuffer.append(arrayOfChar[i1]);
        m++;
      }
    }
    if ((m > 0) && (fBuffer.charAt(m - 1) == ' ')) {
      fBuffer.setLength(m - 1);
    }
    String str2 = fBuffer.toString();
    paramXMLAttributes.setValue(paramInt, str2);
    return !str1.equals(str2);
  }
  
  private final void rootElementSpecified(QName paramQName)
    throws XNIException
  {
    if (fPerformValidation)
    {
      String str1 = fRootElement.rawname;
      String str2 = rawname;
      if ((str1 == null) || (!str1.equals(str2))) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { str1, str2 }, (short)1);
      }
    }
  }
  
  private int checkContent(int paramInt1, QName[] paramArrayOfQName, int paramInt2, int paramInt3)
    throws XNIException
  {
    fDTDGrammar.getElementDecl(paramInt1, fTempElementDecl);
    String str = fCurrentElement.rawname;
    int i = fCurrentContentSpecType;
    if (i == 1)
    {
      if (paramInt3 != 0) {
        return 0;
      }
    }
    else if (i != 0)
    {
      if ((i == 2) || (i == 3))
      {
        ContentModelValidator localContentModelValidator = null;
        localContentModelValidator = fTempElementDecl.contentModelValidator;
        int j = localContentModelValidator.validate(paramArrayOfQName, paramInt2, paramInt3);
        return j;
      }
      if ((i == -1) || (i != 4)) {}
    }
    return -1;
  }
  
  private int getContentSpecType(int paramInt)
  {
    int i = -1;
    if ((paramInt > -1) && (fDTDGrammar.getElementDecl(paramInt, fTempElementDecl))) {
      i = fTempElementDecl.type;
    }
    return i;
  }
  
  private void charDataInContent()
  {
    if (fElementChildren.length <= fElementChildrenLength)
    {
      localObject = new QName[fElementChildren.length * 2];
      System.arraycopy(fElementChildren, 0, localObject, 0, fElementChildren.length);
      fElementChildren = ((QName[])localObject);
    }
    Object localObject = fElementChildren[fElementChildrenLength];
    if (localObject == null)
    {
      for (int i = fElementChildrenLength; i < fElementChildren.length; i++) {
        fElementChildren[i] = new QName();
      }
      localObject = fElementChildren[fElementChildrenLength];
    }
    ((QName)localObject).clear();
    fElementChildrenLength += 1;
  }
  
  private String getAttributeTypeName(XMLAttributeDecl paramXMLAttributeDecl)
  {
    switch (simpleType.type)
    {
    case 1: 
      return simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
    case 2: 
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append('(');
      for (int i = 0; i < simpleType.enumeration.length; i++)
      {
        if (i > 0) {
          localStringBuffer.append('|');
        }
        localStringBuffer.append(simpleType.enumeration[i]);
      }
      localStringBuffer.append(')');
      return fSymbolTable.addSymbol(localStringBuffer.toString());
    case 3: 
      return XMLSymbols.fIDSymbol;
    case 4: 
      return simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
    case 5: 
      return simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
    case 6: 
      return XMLSymbols.fNOTATIONSymbol;
    }
    return XMLSymbols.fCDATASymbol;
  }
  
  protected void init()
  {
    if ((fValidation) || (fDynamicValidation)) {
      try
      {
        fValID = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDSymbol);
        fValIDRef = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSymbol);
        fValIDRefs = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSSymbol);
        fValENTITY = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITYSymbol);
        fValENTITIES = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITIESSymbol);
        fValNMTOKEN = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSymbol);
        fValNMTOKENS = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSSymbol);
        fValNOTATION = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNOTATIONSymbol);
      }
      catch (Exception localException)
      {
        localException.printStackTrace(System.err);
      }
    }
  }
  
  private void ensureStackCapacity(int paramInt)
  {
    if (paramInt == fElementQNamePartsStack.length)
    {
      QName[] arrayOfQName = new QName[paramInt * 2];
      System.arraycopy(fElementQNamePartsStack, 0, arrayOfQName, 0, paramInt);
      fElementQNamePartsStack = arrayOfQName;
      QName localQName = fElementQNamePartsStack[paramInt];
      if (localQName == null) {
        for (int i = paramInt; i < fElementQNamePartsStack.length; i++) {
          fElementQNamePartsStack[i] = new QName();
        }
      }
      int[] arrayOfInt = new int[paramInt * 2];
      System.arraycopy(fElementIndexStack, 0, arrayOfInt, 0, paramInt);
      fElementIndexStack = arrayOfInt;
      arrayOfInt = new int[paramInt * 2];
      System.arraycopy(fContentSpecTypeStack, 0, arrayOfInt, 0, paramInt);
      fContentSpecTypeStack = arrayOfInt;
    }
  }
  
  protected boolean handleStartElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fSeenRootElement)
    {
      fPerformValidation = validate();
      fSeenRootElement = true;
      fValidationManager.setEntityState(fDTDGrammar);
      fValidationManager.setGrammarFound(fSeenDoctypeDecl);
      rootElementSpecified(paramQName);
    }
    if (fDTDGrammar == null)
    {
      if (!fPerformValidation)
      {
        fCurrentElementIndex = -1;
        fCurrentContentSpecType = -1;
        fInElementContent = false;
      }
      if (fPerformValidation) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { rawname }, (short)1);
      }
      if (fDocumentSource != null)
      {
        fDocumentSource.setDocumentHandler(fDocumentHandler);
        if (fDocumentHandler != null) {
          fDocumentHandler.setDocumentSource(fDocumentSource);
        }
        return true;
      }
    }
    else
    {
      fCurrentElementIndex = fDTDGrammar.getElementDeclIndex(paramQName);
      fCurrentContentSpecType = fDTDGrammar.getContentSpecType(fCurrentElementIndex);
      if ((fCurrentContentSpecType == -1) && (fPerformValidation)) {
        fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_NOT_DECLARED", new Object[] { rawname }, (short)1);
      }
      addDTDDefaultAttrsAndValidate(paramQName, fCurrentElementIndex, paramXMLAttributes);
    }
    fInElementContent = (fCurrentContentSpecType == 3);
    fElementDepth += 1;
    if (fPerformValidation)
    {
      if (fElementChildrenOffsetStack.length <= fElementDepth)
      {
        localObject = new int[fElementChildrenOffsetStack.length * 2];
        System.arraycopy(fElementChildrenOffsetStack, 0, localObject, 0, fElementChildrenOffsetStack.length);
        fElementChildrenOffsetStack = ((int[])localObject);
      }
      fElementChildrenOffsetStack[fElementDepth] = fElementChildrenLength;
      if (fElementChildren.length <= fElementChildrenLength)
      {
        localObject = new QName[fElementChildrenLength * 2];
        System.arraycopy(fElementChildren, 0, localObject, 0, fElementChildren.length);
        fElementChildren = ((QName[])localObject);
      }
      Object localObject = fElementChildren[fElementChildrenLength];
      if (localObject == null)
      {
        for (int i = fElementChildrenLength; i < fElementChildren.length; i++) {
          fElementChildren[i] = new QName();
        }
        localObject = fElementChildren[fElementChildrenLength];
      }
      ((QName)localObject).setValues(paramQName);
      fElementChildrenLength += 1;
    }
    fCurrentElement.setValues(paramQName);
    ensureStackCapacity(fElementDepth);
    fElementQNamePartsStack[fElementDepth].setValues(fCurrentElement);
    fElementIndexStack[fElementDepth] = fCurrentElementIndex;
    fContentSpecTypeStack[fElementDepth] = fCurrentContentSpecType;
    startNamespaceScope(paramQName, paramXMLAttributes, paramAugmentations);
    return false;
  }
  
  protected void startNamespaceScope(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations) {}
  
  protected void handleEndElement(QName paramQName, Augmentations paramAugmentations, boolean paramBoolean)
    throws XNIException
  {
    fElementDepth -= 1;
    if (fPerformValidation)
    {
      int i = fCurrentElementIndex;
      if ((i != -1) && (fCurrentContentSpecType != -1))
      {
        QName[] arrayOfQName = fElementChildren;
        int j = fElementChildrenOffsetStack[(fElementDepth + 1)] + 1;
        int k = fElementChildrenLength - j;
        int m = checkContent(i, arrayOfQName, j, k);
        if (m != -1)
        {
          fDTDGrammar.getElementDecl(i, fTempElementDecl);
          if (fTempElementDecl.type == 1)
          {
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID", new Object[] { rawname, "EMPTY" }, (short)1);
          }
          else
          {
            String str2 = m != k ? "MSG_CONTENT_INVALID" : "MSG_CONTENT_INCOMPLETE";
            fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", str2, new Object[] { rawname, fDTDGrammar.getContentSpecAsString(i) }, (short)1);
          }
        }
      }
      fElementChildrenLength = (fElementChildrenOffsetStack[(fElementDepth + 1)] + 1);
    }
    endNamespaceScope(fCurrentElement, paramAugmentations, paramBoolean);
    if (fElementDepth < -1) {
      throw new RuntimeException("FWK008 Element stack underflow");
    }
    if (fElementDepth < 0)
    {
      fCurrentElement.clear();
      fCurrentElementIndex = -1;
      fCurrentContentSpecType = -1;
      fInElementContent = false;
      if (fPerformValidation)
      {
        String str1 = fValidationState.checkIDRefID();
        if (str1 != null) {
          fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_WITH_ID_REQUIRED", new Object[] { str1 }, (short)1);
        }
      }
      return;
    }
    fCurrentElement.setValues(fElementQNamePartsStack[fElementDepth]);
    fCurrentElementIndex = fElementIndexStack[fElementDepth];
    fCurrentContentSpecType = fContentSpecTypeStack[fElementDepth];
    fInElementContent = (fCurrentContentSpecType == 3);
  }
  
  protected void endNamespaceScope(QName paramQName, Augmentations paramAugmentations, boolean paramBoolean)
  {
    if ((fDocumentHandler != null) && (!paramBoolean)) {
      fDocumentHandler.endElement(fCurrentElement, paramAugmentations);
    }
  }
  
  protected boolean isSpace(int paramInt)
  {
    return XMLChar.isSpace(paramInt);
  }
  
  public boolean characterData(String paramString, Augmentations paramAugmentations)
  {
    characters(new XMLString(paramString.toCharArray(), 0, paramString.length()), paramAugmentations);
    return true;
  }
}
