package org.apache.xerces.impl.xs.traversers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaGrammar.BuiltinSchemaGrammar;
import org.apache.xerces.impl.xs.SchemaGrammar.Schema4Annotations;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.impl.xs.opti.SchemaParsingConfig;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.XSInputSource;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.util.DOMInputSource;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXInputSource;
import org.apache.xerces.util.StAXInputSource;
import org.apache.xerces.util.StAXLocationWrapper;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XSDHandler
{
  protected static final String VALIDATION = "http://xml.org/sax/features/validation";
  protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
  protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
  protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
  protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
  protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
  protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
  protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
  protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
  protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
  protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
  private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
  protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
  protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
  protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
  public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
  protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  protected static final String LOCALE = "http://apache.org/xml/properties/locale";
  protected static final boolean DEBUG_NODE_POOL = false;
  static final int ATTRIBUTE_TYPE = 1;
  static final int ATTRIBUTEGROUP_TYPE = 2;
  static final int ELEMENT_TYPE = 3;
  static final int GROUP_TYPE = 4;
  static final int IDENTITYCONSTRAINT_TYPE = 5;
  static final int NOTATION_TYPE = 6;
  static final int TYPEDECL_TYPE = 7;
  public static final String REDEF_IDENTIFIER = "_fn3dktizrknc9pi";
  protected Hashtable fNotationRegistry = new Hashtable();
  protected XSDeclarationPool fDeclPool = null;
  private Hashtable fUnparsedAttributeRegistry = new Hashtable();
  private Hashtable fUnparsedAttributeGroupRegistry = new Hashtable();
  private Hashtable fUnparsedElementRegistry = new Hashtable();
  private Hashtable fUnparsedGroupRegistry = new Hashtable();
  private Hashtable fUnparsedIdentityConstraintRegistry = new Hashtable();
  private Hashtable fUnparsedNotationRegistry = new Hashtable();
  private Hashtable fUnparsedTypeRegistry = new Hashtable();
  private Hashtable fUnparsedAttributeRegistrySub = new Hashtable();
  private Hashtable fUnparsedAttributeGroupRegistrySub = new Hashtable();
  private Hashtable fUnparsedElementRegistrySub = new Hashtable();
  private Hashtable fUnparsedGroupRegistrySub = new Hashtable();
  private Hashtable fUnparsedIdentityConstraintRegistrySub = new Hashtable();
  private Hashtable fUnparsedNotationRegistrySub = new Hashtable();
  private Hashtable fUnparsedTypeRegistrySub = new Hashtable();
  private Hashtable[] fUnparsedRegistriesExt = { null, new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable(), new Hashtable() };
  private Hashtable fXSDocumentInfoRegistry = new Hashtable();
  private Hashtable fDependencyMap = new Hashtable();
  private Hashtable fImportMap = new Hashtable();
  private Vector fAllTNSs = new Vector();
  private Hashtable fLocationPairs = null;
  private static final Hashtable EMPTY_TABLE = new Hashtable();
  Hashtable fHiddenNodes = null;
  private Hashtable fTraversed = new Hashtable();
  private Hashtable fDoc2SystemId = new Hashtable();
  private XSDocumentInfo fRoot = null;
  private Hashtable fDoc2XSDocumentMap = new Hashtable();
  private Hashtable fRedefine2XSDMap = new Hashtable();
  private Hashtable fRedefine2NSSupport = new Hashtable();
  private Hashtable fRedefinedRestrictedAttributeGroupRegistry = new Hashtable();
  private Hashtable fRedefinedRestrictedGroupRegistry = new Hashtable();
  private boolean fLastSchemaWasDuplicate;
  private boolean fValidateAnnotations = false;
  private boolean fHonourAllSchemaLocations = false;
  boolean fNamespaceGrowth = false;
  boolean fTolerateDuplicates = false;
  private XMLErrorReporter fErrorReporter;
  private XMLEntityResolver fEntityResolver;
  private XSAttributeChecker fAttributeChecker;
  private SymbolTable fSymbolTable;
  private XSGrammarBucket fGrammarBucket;
  private XSDDescription fSchemaGrammarDescription;
  private XMLGrammarPool fGrammarPool;
  XSDAttributeGroupTraverser fAttributeGroupTraverser;
  XSDAttributeTraverser fAttributeTraverser;
  XSDComplexTypeTraverser fComplexTypeTraverser;
  XSDElementTraverser fElementTraverser;
  XSDGroupTraverser fGroupTraverser;
  XSDKeyrefTraverser fKeyrefTraverser;
  XSDNotationTraverser fNotationTraverser;
  XSDSimpleTypeTraverser fSimpleTypeTraverser;
  XSDUniqueOrKeyTraverser fUniqueOrKeyTraverser;
  XSDWildcardTraverser fWildCardTraverser;
  SchemaDVFactory fDVFactory;
  SchemaDOMParser fSchemaParser = new SchemaDOMParser(new SchemaParsingConfig());
  SchemaContentHandler fXSContentHandler;
  StAXSchemaParser fStAXSchemaParser;
  XML11Configuration fAnnotationValidator;
  XSAnnotationGrammarPool fGrammarBucketAdapter;
  private static final int INIT_STACK_SIZE = 30;
  private static final int INC_STACK_SIZE = 10;
  private int fLocalElemStackPos = 0;
  private XSParticleDecl[] fParticle = new XSParticleDecl[30];
  private Element[] fLocalElementDecl = new Element[30];
  private XSDocumentInfo[] fLocalElementDecl_schema = new XSDocumentInfo[30];
  private int[] fAllContext = new int[30];
  private XSObject[] fParent = new XSObject[30];
  private String[][] fLocalElemNamespaceContext = new String[30][1];
  private static final int INIT_KEYREF_STACK = 2;
  private static final int INC_KEYREF_STACK_AMOUNT = 2;
  private int fKeyrefStackPos = 0;
  private Element[] fKeyrefs = new Element[2];
  private XSDocumentInfo[] fKeyrefsMapXSDocumentInfo = new XSDocumentInfo[2];
  private XSElementDecl[] fKeyrefElems = new XSElementDecl[2];
  private String[][] fKeyrefNamespaceContext = new String[2][1];
  SymbolHash fGlobalAttrDecls = new SymbolHash();
  SymbolHash fGlobalAttrGrpDecls = new SymbolHash();
  SymbolHash fGlobalElemDecls = new SymbolHash();
  SymbolHash fGlobalGroupDecls = new SymbolHash();
  SymbolHash fGlobalNotationDecls = new SymbolHash();
  SymbolHash fGlobalIDConstraintDecls = new SymbolHash();
  SymbolHash fGlobalTypeDecls = new SymbolHash();
  private static final String[][] NS_ERROR_CODES = { { "src-include.2.1", "src-include.2.1" }, { "src-redefine.3.1", "src-redefine.3.1" }, { "src-import.3.1", "src-import.3.2" }, null, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" } };
  private static final String[] ELE_ERROR_CODES = { "src-include.1", "src-redefine.2", "src-import.2", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4" };
  private Vector fReportedTNS = null;
  private static final String[] COMP_TYPE = { null, "attribute declaration", "attribute group", "element declaration", "group", "identity constraint", "notation", "type definition" };
  private static final String[] CIRCULAR_CODES = { "Internal-Error", "Internal-Error", "src-attribute_group.3", "e-props-correct.6", "mg-props-correct.2", "Internal-Error", "Internal-Error", "st-props-correct.2" };
  private SimpleLocator xl = new SimpleLocator();
  
  private String null2EmptyString(String paramString)
  {
    return paramString == null ? XMLSymbols.EMPTY_STRING : paramString;
  }
  
  private String emptyString2Null(String paramString)
  {
    return paramString == XMLSymbols.EMPTY_STRING ? null : paramString;
  }
  
  private String doc2SystemId(Element paramElement)
  {
    String str = null;
    if ((paramElement.getOwnerDocument() instanceof SchemaDOM)) {
      str = ((SchemaDOM)paramElement.getOwnerDocument()).getDocumentURI();
    }
    return str != null ? str : (String)fDoc2SystemId.get(paramElement);
  }
  
  public XSDHandler() {}
  
  public XSDHandler(XSGrammarBucket paramXSGrammarBucket)
  {
    this();
    fGrammarBucket = paramXSGrammarBucket;
    fSchemaGrammarDescription = new XSDDescription();
  }
  
  public SchemaGrammar parseSchema(XMLInputSource paramXMLInputSource, XSDDescription paramXSDDescription, Hashtable paramHashtable)
    throws IOException
  {
    fLocationPairs = paramHashtable;
    fSchemaParser.resetNodePool();
    SchemaGrammar localSchemaGrammar1 = null;
    String str1 = null;
    short s = paramXSDDescription.getContextType();
    if (s != 3)
    {
      if ((fHonourAllSchemaLocations) && (s == 2) && (isExistingGrammar(paramXSDDescription, fNamespaceGrowth))) {
        localSchemaGrammar1 = fGrammarBucket.getGrammar(paramXSDDescription.getTargetNamespace());
      } else {
        localSchemaGrammar1 = findGrammar(paramXSDDescription, fNamespaceGrowth);
      }
      if (localSchemaGrammar1 != null)
      {
        if (!fNamespaceGrowth) {
          return localSchemaGrammar1;
        }
        try
        {
          if (localSchemaGrammar1.getDocumentLocations().contains(XMLEntityManager.expandSystemId(paramXMLInputSource.getSystemId(), paramXMLInputSource.getBaseSystemId(), false))) {
            return localSchemaGrammar1;
          }
        }
        catch (URI.MalformedURIException localMalformedURIException) {}
      }
      str1 = paramXSDDescription.getTargetNamespace();
      if (str1 != null) {
        str1 = fSymbolTable.addSymbol(str1);
      }
    }
    prepareForParse();
    Element localElement1 = null;
    if ((paramXMLInputSource instanceof DOMInputSource)) {
      localElement1 = getSchemaDocument(str1, (DOMInputSource)paramXMLInputSource, s == 3, s, null);
    } else if ((paramXMLInputSource instanceof SAXInputSource)) {
      localElement1 = getSchemaDocument(str1, (SAXInputSource)paramXMLInputSource, s == 3, s, null);
    } else if ((paramXMLInputSource instanceof StAXInputSource)) {
      localElement1 = getSchemaDocument(str1, (StAXInputSource)paramXMLInputSource, s == 3, s, null);
    } else if ((paramXMLInputSource instanceof XSInputSource)) {
      localElement1 = getSchemaDocument((XSInputSource)paramXMLInputSource, paramXSDDescription);
    } else {
      localElement1 = getSchemaDocument(str1, paramXMLInputSource, s == 3, s, null);
    }
    if (localElement1 == null)
    {
      if ((paramXMLInputSource instanceof XSInputSource)) {
        return fGrammarBucket.getGrammar(paramXSDDescription.getTargetNamespace());
      }
      return localSchemaGrammar1;
    }
    Object localObject;
    if (s == 3)
    {
      localElement2 = localElement1;
      str1 = DOMUtil.getAttrValue(localElement2, SchemaSymbols.ATT_TARGETNAMESPACE);
      if ((str1 != null) && (str1.length() > 0))
      {
        str1 = fSymbolTable.addSymbol(str1);
        paramXSDDescription.setTargetNamespace(str1);
      }
      else
      {
        str1 = null;
      }
      localSchemaGrammar1 = findGrammar(paramXSDDescription, fNamespaceGrowth);
      String str2 = XMLEntityManager.expandSystemId(paramXMLInputSource.getSystemId(), paramXMLInputSource.getBaseSystemId(), false);
      if ((localSchemaGrammar1 != null) && ((!fNamespaceGrowth) || ((str2 != null) && (localSchemaGrammar1.getDocumentLocations().contains(str2))))) {
        return localSchemaGrammar1;
      }
      localObject = new XSDKey(str2, s, str1);
      fTraversed.put(localObject, localElement1);
      if (str2 != null) {
        fDoc2SystemId.put(localElement1, str2);
      }
    }
    prepareForTraverse();
    fRoot = constructTrees(localElement1, paramXMLInputSource.getSystemId(), paramXSDDescription, localSchemaGrammar1 != null);
    if (fRoot == null) {
      return null;
    }
    buildGlobalNameRegistries();
    Element localElement2 = fValidateAnnotations ? new ArrayList() : null;
    traverseSchemas(localElement2);
    traverseLocalElements();
    resolveKeyRefs();
    for (int i = fAllTNSs.size() - 1; i >= 0; i--)
    {
      localObject = (String)fAllTNSs.elementAt(i);
      Vector localVector = (Vector)fImportMap.get(localObject);
      SchemaGrammar localSchemaGrammar2 = fGrammarBucket.getGrammar(emptyString2Null((String)localObject));
      if (localSchemaGrammar2 != null)
      {
        int j = 0;
        for (int k = 0; k < localVector.size(); k++)
        {
          SchemaGrammar localSchemaGrammar3 = fGrammarBucket.getGrammar((String)localVector.elementAt(k));
          if (localSchemaGrammar3 != null) {
            localVector.setElementAt(localSchemaGrammar3, j++);
          }
        }
        localVector.setSize(j);
        localSchemaGrammar2.setImportedGrammars(localVector);
      }
    }
    if ((fValidateAnnotations) && (localElement2.size() > 0)) {
      validateAnnotations(localElement2);
    }
    return fGrammarBucket.getGrammar(fRoot.fTargetNamespace);
  }
  
  private void validateAnnotations(ArrayList paramArrayList)
  {
    if (fAnnotationValidator == null) {
      createAnnotationValidator();
    }
    int i = paramArrayList.size();
    XMLInputSource localXMLInputSource = new XMLInputSource(null, null, null);
    fGrammarBucketAdapter.refreshGrammars(fGrammarBucket);
    for (int j = 0; j < i; j += 2)
    {
      localXMLInputSource.setSystemId((String)paramArrayList.get(j));
      for (XSAnnotationInfo localXSAnnotationInfo = (XSAnnotationInfo)paramArrayList.get(j + 1); localXSAnnotationInfo != null; localXSAnnotationInfo = next)
      {
        localXMLInputSource.setCharacterStream(new StringReader(fAnnotation));
        try
        {
          fAnnotationValidator.parse(localXMLInputSource);
        }
        catch (IOException localIOException) {}
      }
    }
  }
  
  private void createAnnotationValidator()
  {
    fAnnotationValidator = new XML11Configuration();
    fGrammarBucketAdapter = new XSAnnotationGrammarPool(null);
    fAnnotationValidator.setFeature("http://xml.org/sax/features/validation", true);
    fAnnotationValidator.setFeature("http://apache.org/xml/features/validation/schema", true);
    fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/grammar-pool", fGrammarBucketAdapter);
    XMLErrorHandler localXMLErrorHandler = fErrorReporter.getErrorHandler();
    fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", localXMLErrorHandler != null ? localXMLErrorHandler : new DefaultErrorHandler());
    Locale localLocale = fErrorReporter.getLocale();
    fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", localLocale);
  }
  
  SchemaGrammar getGrammar(String paramString)
  {
    return fGrammarBucket.getGrammar(paramString);
  }
  
  protected SchemaGrammar findGrammar(XSDDescription paramXSDDescription, boolean paramBoolean)
  {
    SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(paramXSDDescription.getTargetNamespace());
    if ((localSchemaGrammar == null) && (fGrammarPool != null))
    {
      localSchemaGrammar = (SchemaGrammar)fGrammarPool.retrieveGrammar(paramXSDDescription);
      if ((localSchemaGrammar != null) && (!fGrammarBucket.putGrammar(localSchemaGrammar, true, paramBoolean)))
      {
        reportSchemaWarning("GrammarConflict", null, null);
        localSchemaGrammar = null;
      }
    }
    return localSchemaGrammar;
  }
  
  protected XSDocumentInfo constructTrees(Element paramElement, String paramString, XSDDescription paramXSDDescription, boolean paramBoolean)
  {
    if (paramElement == null) {
      return null;
    }
    String str1 = paramXSDDescription.getTargetNamespace();
    int i = paramXSDDescription.getContextType();
    XSDocumentInfo localXSDocumentInfo = null;
    try
    {
      localXSDocumentInfo = new XSDocumentInfo(paramElement, fAttributeChecker, fSymbolTable);
    }
    catch (XMLSchemaException localXMLSchemaException)
    {
      reportSchemaError(ELE_ERROR_CODES[i], new Object[] { paramString }, paramElement);
      return null;
    }
    if ((fTargetNamespace != null) && (fTargetNamespace.length() == 0))
    {
      reportSchemaWarning("EmptyTargetNamespace", new Object[] { paramString }, paramElement);
      fTargetNamespace = null;
    }
    int j;
    if (str1 != null)
    {
      j = 0;
      if ((i == 0) || (i == 1))
      {
        if (fTargetNamespace == null)
        {
          fTargetNamespace = str1;
          fIsChameleonSchema = true;
        }
        else if (str1 != fTargetNamespace)
        {
          reportSchemaError(NS_ERROR_CODES[i][j], new Object[] { str1, fTargetNamespace }, paramElement);
          return null;
        }
      }
      else if ((i != 3) && (str1 != fTargetNamespace))
      {
        reportSchemaError(NS_ERROR_CODES[i][j], new Object[] { str1, fTargetNamespace }, paramElement);
        return null;
      }
    }
    else if (fTargetNamespace != null)
    {
      if (i == 3)
      {
        paramXSDDescription.setTargetNamespace(fTargetNamespace);
        str1 = fTargetNamespace;
      }
      else
      {
        j = 1;
        reportSchemaError(NS_ERROR_CODES[i][j], new Object[] { str1, fTargetNamespace }, paramElement);
        return null;
      }
    }
    localXSDocumentInfo.addAllowedNS(fTargetNamespace);
    Object localObject1 = null;
    if (paramBoolean)
    {
      localObject2 = fGrammarBucket.getGrammar(fTargetNamespace);
      if (((SchemaGrammar)localObject2).isImmutable())
      {
        localObject1 = new SchemaGrammar((SchemaGrammar)localObject2);
        fGrammarBucket.putGrammar((SchemaGrammar)localObject1);
        updateImportListWith((SchemaGrammar)localObject1);
      }
      else
      {
        localObject1 = localObject2;
      }
      updateImportListFor((SchemaGrammar)localObject1);
    }
    else if ((i == 0) || (i == 1))
    {
      localObject1 = fGrammarBucket.getGrammar(fTargetNamespace);
    }
    else if ((fHonourAllSchemaLocations) && (i == 2))
    {
      localObject1 = findGrammar(paramXSDDescription, false);
      if (localObject1 == null)
      {
        localObject1 = new SchemaGrammar(fTargetNamespace, paramXSDDescription.makeClone(), fSymbolTable);
        fGrammarBucket.putGrammar((SchemaGrammar)localObject1);
      }
    }
    else
    {
      localObject1 = new SchemaGrammar(fTargetNamespace, paramXSDDescription.makeClone(), fSymbolTable);
      fGrammarBucket.putGrammar((SchemaGrammar)localObject1);
    }
    ((SchemaGrammar)localObject1).addDocument(null, (String)fDoc2SystemId.get(fSchemaElement));
    fDoc2XSDocumentMap.put(paramElement, localXSDocumentInfo);
    Object localObject2 = new Vector();
    Element localElement1 = paramElement;
    Element localElement2 = null;
    for (Element localElement3 = DOMUtil.getFirstChildElement(localElement1); localElement3 != null; localElement3 = DOMUtil.getNextSiblingElement(localElement3))
    {
      String str2 = null;
      String str3 = null;
      String str4 = DOMUtil.getLocalName(localElement3);
      short s = -1;
      boolean bool1 = false;
      if (!str4.equals(SchemaSymbols.ELT_ANNOTATION))
      {
        Element localElement4;
        String str5;
        Object localObject4;
        Object localObject5;
        if (str4.equals(SchemaSymbols.ELT_IMPORT))
        {
          s = 2;
          localObject3 = fAttributeChecker.checkAttributes(localElement3, true, localXSDocumentInfo);
          str3 = (String)localObject3[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
          str2 = (String)localObject3[XSAttributeChecker.ATTIDX_NAMESPACE];
          if (str2 != null) {
            str2 = fSymbolTable.addSymbol(str2);
          }
          localElement4 = DOMUtil.getFirstChildElement(localElement3);
          if (localElement4 != null)
          {
            str5 = DOMUtil.getLocalName(localElement4);
            if (str5.equals(SchemaSymbols.ELT_ANNOTATION)) {
              ((SchemaGrammar)localObject1).addAnnotation(fElementTraverser.traverseAnnotationDecl(localElement4, (Object[])localObject3, true, localXSDocumentInfo));
            } else {
              reportSchemaError("s4s-elt-must-match.1", new Object[] { str4, "annotation?", str5 }, localElement3);
            }
            if (DOMUtil.getNextSiblingElement(localElement4) != null) {
              reportSchemaError("s4s-elt-must-match.1", new Object[] { str4, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(localElement4)) }, localElement3);
            }
          }
          else
          {
            str5 = DOMUtil.getSyntheticAnnotation(localElement3);
            if (str5 != null) {
              ((SchemaGrammar)localObject1).addAnnotation(fElementTraverser.traverseSyntheticAnnotation(localElement3, str5, (Object[])localObject3, true, localXSDocumentInfo));
            }
          }
          fAttributeChecker.returnAttrArray((Object[])localObject3, localXSDocumentInfo);
          if (str2 == fTargetNamespace)
          {
            reportSchemaError(str2 != null ? "src-import.1.1" : "src-import.1.2", new Object[] { str2 }, localElement3);
            continue;
          }
          if (localXSDocumentInfo.isAllowedNS(str2))
          {
            if ((!fHonourAllSchemaLocations) && (!fNamespaceGrowth)) {
              continue;
            }
          }
          else {
            localXSDocumentInfo.addAllowedNS(str2);
          }
          str5 = null2EmptyString(fTargetNamespace);
          localObject4 = (Vector)fImportMap.get(str5);
          if (localObject4 == null)
          {
            fAllTNSs.addElement(str5);
            localObject4 = new Vector();
            fImportMap.put(str5, localObject4);
            ((Vector)localObject4).addElement(str2);
          }
          else if (!((Vector)localObject4).contains(str2))
          {
            ((Vector)localObject4).addElement(str2);
          }
          fSchemaGrammarDescription.reset();
          fSchemaGrammarDescription.setContextType((short)2);
          fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(paramElement));
          fSchemaGrammarDescription.setLiteralSystemId(str3);
          fSchemaGrammarDescription.setLocationHints(new String[] { str3 });
          fSchemaGrammarDescription.setTargetNamespace(str2);
          localObject5 = findGrammar(fSchemaGrammarDescription, fNamespaceGrowth);
          if (localObject5 != null) {
            if (fNamespaceGrowth) {
              try
              {
                if (((SchemaGrammar)localObject5).getDocumentLocations().contains(XMLEntityManager.expandSystemId(str3, fSchemaGrammarDescription.getBaseSystemId(), false))) {
                  continue;
                }
                bool1 = true;
              }
              catch (URI.MalformedURIException localMalformedURIException2) {}
            } else {
              if ((!fHonourAllSchemaLocations) || (isExistingGrammar(fSchemaGrammarDescription, false))) {
                continue;
              }
            }
          }
          localElement2 = resolveSchema(fSchemaGrammarDescription, false, localElement3, localObject5 == null);
        }
        else
        {
          if ((!str4.equals(SchemaSymbols.ELT_INCLUDE)) && (!str4.equals(SchemaSymbols.ELT_REDEFINE))) {
            break;
          }
          localObject3 = fAttributeChecker.checkAttributes(localElement3, true, localXSDocumentInfo);
          str3 = (String)localObject3[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
          if (str4.equals(SchemaSymbols.ELT_REDEFINE)) {
            fRedefine2NSSupport.put(localElement3, new SchemaNamespaceSupport(fNamespaceSupport));
          }
          if (str4.equals(SchemaSymbols.ELT_INCLUDE))
          {
            localElement4 = DOMUtil.getFirstChildElement(localElement3);
            if (localElement4 != null)
            {
              str5 = DOMUtil.getLocalName(localElement4);
              if (str5.equals(SchemaSymbols.ELT_ANNOTATION)) {
                ((SchemaGrammar)localObject1).addAnnotation(fElementTraverser.traverseAnnotationDecl(localElement4, (Object[])localObject3, true, localXSDocumentInfo));
              } else {
                reportSchemaError("s4s-elt-must-match.1", new Object[] { str4, "annotation?", str5 }, localElement3);
              }
              if (DOMUtil.getNextSiblingElement(localElement4) != null) {
                reportSchemaError("s4s-elt-must-match.1", new Object[] { str4, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(localElement4)) }, localElement3);
              }
            }
            else
            {
              str5 = DOMUtil.getSyntheticAnnotation(localElement3);
              if (str5 != null) {
                ((SchemaGrammar)localObject1).addAnnotation(fElementTraverser.traverseSyntheticAnnotation(localElement3, str5, (Object[])localObject3, true, localXSDocumentInfo));
              }
            }
          }
          else
          {
            for (localElement4 = DOMUtil.getFirstChildElement(localElement3); localElement4 != null; localElement4 = DOMUtil.getNextSiblingElement(localElement4))
            {
              str5 = DOMUtil.getLocalName(localElement4);
              if (str5.equals(SchemaSymbols.ELT_ANNOTATION))
              {
                ((SchemaGrammar)localObject1).addAnnotation(fElementTraverser.traverseAnnotationDecl(localElement4, (Object[])localObject3, true, localXSDocumentInfo));
                DOMUtil.setHidden(localElement4, fHiddenNodes);
              }
              else
              {
                localObject4 = DOMUtil.getSyntheticAnnotation(localElement3);
                if (localObject4 != null) {
                  ((SchemaGrammar)localObject1).addAnnotation(fElementTraverser.traverseSyntheticAnnotation(localElement3, (String)localObject4, (Object[])localObject3, true, localXSDocumentInfo));
                }
              }
            }
          }
          fAttributeChecker.returnAttrArray((Object[])localObject3, localXSDocumentInfo);
          if (str3 == null) {
            reportSchemaError("s4s-att-must-appear", new Object[] { "<include> or <redefine>", "schemaLocation" }, localElement3);
          }
          boolean bool2 = false;
          s = 0;
          if (str4.equals(SchemaSymbols.ELT_REDEFINE))
          {
            bool2 = nonAnnotationContent(localElement3);
            s = 1;
          }
          fSchemaGrammarDescription.reset();
          fSchemaGrammarDescription.setContextType(s);
          fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(paramElement));
          fSchemaGrammarDescription.setLocationHints(new String[] { str3 });
          fSchemaGrammarDescription.setTargetNamespace(str1);
          boolean bool3 = false;
          localObject4 = resolveSchemaSource(fSchemaGrammarDescription, bool2, localElement3, true);
          if ((fNamespaceGrowth) && (s == 0)) {
            try
            {
              localObject5 = XMLEntityManager.expandSystemId(((XMLInputSource)localObject4).getSystemId(), ((XMLInputSource)localObject4).getBaseSystemId(), false);
              bool3 = ((SchemaGrammar)localObject1).getDocumentLocations().contains((String)localObject5);
            }
            catch (URI.MalformedURIException localMalformedURIException1) {}
          }
          if (!bool3)
          {
            localElement2 = resolveSchema((XMLInputSource)localObject4, fSchemaGrammarDescription, bool2, localElement3);
            str2 = fTargetNamespace;
          }
          else
          {
            fLastSchemaWasDuplicate = true;
          }
        }
        Object localObject3 = null;
        if (fLastSchemaWasDuplicate) {
          localObject3 = localElement2 == null ? null : (XSDocumentInfo)fDoc2XSDocumentMap.get(localElement2);
        } else {
          localObject3 = constructTrees(localElement2, str3, fSchemaGrammarDescription, bool1);
        }
        if ((str4.equals(SchemaSymbols.ELT_REDEFINE)) && (localObject3 != null)) {
          fRedefine2XSDMap.put(localElement3, localObject3);
        }
        if (localElement2 != null)
        {
          if (localObject3 != null) {
            ((Vector)localObject2).addElement(localObject3);
          }
          localElement2 = null;
        }
      }
    }
    fDependencyMap.put(localXSDocumentInfo, localObject2);
    return localXSDocumentInfo;
  }
  
  private boolean isExistingGrammar(XSDDescription paramXSDDescription, boolean paramBoolean)
  {
    SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(paramXSDDescription.getTargetNamespace());
    if (localSchemaGrammar == null) {
      return findGrammar(paramXSDDescription, paramBoolean) != null;
    }
    if (localSchemaGrammar.isImmutable()) {
      return true;
    }
    try
    {
      return localSchemaGrammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(paramXSDDescription.getLiteralSystemId(), paramXSDDescription.getBaseSystemId(), false));
    }
    catch (URI.MalformedURIException localMalformedURIException) {}
    return false;
  }
  
  private void updateImportListFor(SchemaGrammar paramSchemaGrammar)
  {
    Vector localVector = paramSchemaGrammar.getImportedGrammars();
    if (localVector != null) {
      for (int i = 0; i < localVector.size(); i++)
      {
        SchemaGrammar localSchemaGrammar1 = (SchemaGrammar)localVector.elementAt(i);
        SchemaGrammar localSchemaGrammar2 = fGrammarBucket.getGrammar(localSchemaGrammar1.getTargetNamespace());
        if ((localSchemaGrammar2 != null) && (localSchemaGrammar1 != localSchemaGrammar2)) {
          localVector.set(i, localSchemaGrammar2);
        }
      }
    }
  }
  
  private void updateImportListWith(SchemaGrammar paramSchemaGrammar)
  {
    SchemaGrammar[] arrayOfSchemaGrammar = fGrammarBucket.getGrammars();
    for (int i = 0; i < arrayOfSchemaGrammar.length; i++)
    {
      SchemaGrammar localSchemaGrammar1 = arrayOfSchemaGrammar[i];
      if (localSchemaGrammar1 != paramSchemaGrammar)
      {
        Vector localVector = localSchemaGrammar1.getImportedGrammars();
        if (localVector != null) {
          for (int j = 0; j < localVector.size(); j++)
          {
            SchemaGrammar localSchemaGrammar2 = (SchemaGrammar)localVector.elementAt(j);
            if (null2EmptyString(localSchemaGrammar2.getTargetNamespace()).equals(null2EmptyString(paramSchemaGrammar.getTargetNamespace())))
            {
              if (localSchemaGrammar2 == paramSchemaGrammar) {
                break;
              }
              localVector.set(j, paramSchemaGrammar);
              break;
            }
          }
        }
      }
    }
  }
  
  protected void buildGlobalNameRegistries()
  {
    Stack localStack = new Stack();
    localStack.push(fRoot);
    while (!localStack.empty())
    {
      XSDocumentInfo localXSDocumentInfo = (XSDocumentInfo)localStack.pop();
      Element localElement1 = fSchemaElement;
      if (!DOMUtil.isHidden(localElement1, fHiddenNodes))
      {
        Element localElement2 = localElement1;
        int i = 1;
        for (Element localElement3 = DOMUtil.getFirstChildElement(localElement2); localElement3 != null; localElement3 = DOMUtil.getNextSiblingElement(localElement3)) {
          if (!DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_ANNOTATION)) {
            if ((DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_INCLUDE)) || (DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_IMPORT)))
            {
              if (i == 0) {
                reportSchemaError("s4s-elt-invalid-content.3", new Object[] { DOMUtil.getLocalName(localElement3) }, localElement3);
              }
              DOMUtil.setHidden(localElement3, fHiddenNodes);
            }
            else
            {
              String str1;
              String str2;
              if (DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_REDEFINE))
              {
                if (i == 0) {
                  reportSchemaError("s4s-elt-invalid-content.3", new Object[] { DOMUtil.getLocalName(localElement3) }, localElement3);
                }
                for (localObject = DOMUtil.getFirstChildElement(localElement3); localObject != null; localObject = DOMUtil.getNextSiblingElement((Node)localObject))
                {
                  str1 = DOMUtil.getAttrValue((Element)localObject, SchemaSymbols.ATT_NAME);
                  if (str1.length() != 0)
                  {
                    str2 = fTargetNamespace + "," + str1;
                    String str3 = DOMUtil.getLocalName((Node)localObject);
                    String str4;
                    if (str3.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP))
                    {
                      checkForDuplicateNames(str2, 2, fUnparsedAttributeGroupRegistry, fUnparsedAttributeGroupRegistrySub, (Element)localObject, localXSDocumentInfo);
                      str4 = DOMUtil.getAttrValue((Element)localObject, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                      renameRedefiningComponents(localXSDocumentInfo, (Element)localObject, SchemaSymbols.ELT_ATTRIBUTEGROUP, str1, str4);
                    }
                    else if ((str3.equals(SchemaSymbols.ELT_COMPLEXTYPE)) || (str3.equals(SchemaSymbols.ELT_SIMPLETYPE)))
                    {
                      checkForDuplicateNames(str2, 7, fUnparsedTypeRegistry, fUnparsedTypeRegistrySub, (Element)localObject, localXSDocumentInfo);
                      str4 = DOMUtil.getAttrValue((Element)localObject, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                      if (str3.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                        renameRedefiningComponents(localXSDocumentInfo, (Element)localObject, SchemaSymbols.ELT_COMPLEXTYPE, str1, str4);
                      } else {
                        renameRedefiningComponents(localXSDocumentInfo, (Element)localObject, SchemaSymbols.ELT_SIMPLETYPE, str1, str4);
                      }
                    }
                    else if (str3.equals(SchemaSymbols.ELT_GROUP))
                    {
                      checkForDuplicateNames(str2, 4, fUnparsedGroupRegistry, fUnparsedGroupRegistrySub, (Element)localObject, localXSDocumentInfo);
                      str4 = DOMUtil.getAttrValue((Element)localObject, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                      renameRedefiningComponents(localXSDocumentInfo, (Element)localObject, SchemaSymbols.ELT_GROUP, str1, str4);
                    }
                  }
                }
              }
              else
              {
                i = 0;
                localObject = DOMUtil.getAttrValue(localElement3, SchemaSymbols.ATT_NAME);
                if (((String)localObject).length() != 0)
                {
                  str1 = fTargetNamespace + "," + (String)localObject;
                  str2 = DOMUtil.getLocalName(localElement3);
                  if (str2.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                    checkForDuplicateNames(str1, 1, fUnparsedAttributeRegistry, fUnparsedAttributeRegistrySub, localElement3, localXSDocumentInfo);
                  } else if (str2.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                    checkForDuplicateNames(str1, 2, fUnparsedAttributeGroupRegistry, fUnparsedAttributeGroupRegistrySub, localElement3, localXSDocumentInfo);
                  } else if ((str2.equals(SchemaSymbols.ELT_COMPLEXTYPE)) || (str2.equals(SchemaSymbols.ELT_SIMPLETYPE))) {
                    checkForDuplicateNames(str1, 7, fUnparsedTypeRegistry, fUnparsedTypeRegistrySub, localElement3, localXSDocumentInfo);
                  } else if (str2.equals(SchemaSymbols.ELT_ELEMENT)) {
                    checkForDuplicateNames(str1, 3, fUnparsedElementRegistry, fUnparsedElementRegistrySub, localElement3, localXSDocumentInfo);
                  } else if (str2.equals(SchemaSymbols.ELT_GROUP)) {
                    checkForDuplicateNames(str1, 4, fUnparsedGroupRegistry, fUnparsedGroupRegistrySub, localElement3, localXSDocumentInfo);
                  } else if (str2.equals(SchemaSymbols.ELT_NOTATION)) {
                    checkForDuplicateNames(str1, 6, fUnparsedNotationRegistry, fUnparsedNotationRegistrySub, localElement3, localXSDocumentInfo);
                  }
                }
              }
            }
          }
        }
        DOMUtil.setHidden(localElement1, fHiddenNodes);
        Object localObject = (Vector)fDependencyMap.get(localXSDocumentInfo);
        for (int j = 0; j < ((Vector)localObject).size(); j++) {
          localStack.push(((Vector)localObject).elementAt(j));
        }
      }
    }
  }
  
  protected void traverseSchemas(ArrayList paramArrayList)
  {
    setSchemasVisible(fRoot);
    Stack localStack = new Stack();
    localStack.push(fRoot);
    while (!localStack.empty())
    {
      XSDocumentInfo localXSDocumentInfo = (XSDocumentInfo)localStack.pop();
      Element localElement1 = fSchemaElement;
      SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(fTargetNamespace);
      if (!DOMUtil.isHidden(localElement1, fHiddenNodes))
      {
        Element localElement2 = localElement1;
        int i = 0;
        for (Element localElement3 = DOMUtil.getFirstVisibleChildElement(localElement2, fHiddenNodes); localElement3 != null; localElement3 = DOMUtil.getNextVisibleSiblingElement(localElement3, fHiddenNodes))
        {
          DOMUtil.setHidden(localElement3, fHiddenNodes);
          localObject = DOMUtil.getLocalName(localElement3);
          if (DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_REDEFINE))
          {
            localXSDocumentInfo.backupNSSupport((SchemaNamespaceSupport)fRedefine2NSSupport.get(localElement3));
            for (Element localElement4 = DOMUtil.getFirstVisibleChildElement(localElement3, fHiddenNodes); localElement4 != null; localElement4 = DOMUtil.getNextVisibleSiblingElement(localElement4, fHiddenNodes))
            {
              String str = DOMUtil.getLocalName(localElement4);
              DOMUtil.setHidden(localElement4, fHiddenNodes);
              if (str.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                fAttributeGroupTraverser.traverseGlobal(localElement4, localXSDocumentInfo, localSchemaGrammar);
              } else if (str.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                fComplexTypeTraverser.traverseGlobal(localElement4, localXSDocumentInfo, localSchemaGrammar);
              } else if (str.equals(SchemaSymbols.ELT_GROUP)) {
                fGroupTraverser.traverseGlobal(localElement4, localXSDocumentInfo, localSchemaGrammar);
              } else if (str.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                fSimpleTypeTraverser.traverseGlobal(localElement4, localXSDocumentInfo, localSchemaGrammar);
              } else {
                reportSchemaError("s4s-elt-must-match.1", new Object[] { DOMUtil.getLocalName(localElement3), "(annotation | (simpleType | complexType | group | attributeGroup))*", str }, localElement4);
              }
            }
            localXSDocumentInfo.restoreNSSupport();
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_ATTRIBUTE))
          {
            fAttributeTraverser.traverseGlobal(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_ATTRIBUTEGROUP))
          {
            fAttributeGroupTraverser.traverseGlobal(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_COMPLEXTYPE))
          {
            fComplexTypeTraverser.traverseGlobal(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_ELEMENT))
          {
            fElementTraverser.traverseGlobal(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_GROUP))
          {
            fGroupTraverser.traverseGlobal(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_NOTATION))
          {
            fNotationTraverser.traverse(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_SIMPLETYPE))
          {
            fSimpleTypeTraverser.traverseGlobal(localElement3, localXSDocumentInfo, localSchemaGrammar);
          }
          else if (((String)localObject).equals(SchemaSymbols.ELT_ANNOTATION))
          {
            localSchemaGrammar.addAnnotation(fElementTraverser.traverseAnnotationDecl(localElement3, localXSDocumentInfo.getSchemaAttrs(), true, localXSDocumentInfo));
            i = 1;
          }
          else
          {
            reportSchemaError("s4s-elt-invalid-content.1", new Object[] { SchemaSymbols.ELT_SCHEMA, DOMUtil.getLocalName(localElement3) }, localElement3);
          }
        }
        if (i == 0)
        {
          localObject = DOMUtil.getSyntheticAnnotation(localElement2);
          if (localObject != null) {
            localSchemaGrammar.addAnnotation(fElementTraverser.traverseSyntheticAnnotation(localElement2, (String)localObject, localXSDocumentInfo.getSchemaAttrs(), true, localXSDocumentInfo));
          }
        }
        if (paramArrayList != null)
        {
          localObject = localXSDocumentInfo.getAnnotations();
          if (localObject != null)
          {
            paramArrayList.add(doc2SystemId(localElement1));
            paramArrayList.add(localObject);
          }
        }
        localXSDocumentInfo.returnSchemaAttrs();
        DOMUtil.setHidden(localElement1, fHiddenNodes);
        Object localObject = (Vector)fDependencyMap.get(localXSDocumentInfo);
        for (int j = 0; j < ((Vector)localObject).size(); j++) {
          localStack.push(((Vector)localObject).elementAt(j));
        }
      }
    }
  }
  
  private final boolean needReportTNSError(String paramString)
  {
    if (fReportedTNS == null) {
      fReportedTNS = new Vector();
    } else if (fReportedTNS.contains(paramString)) {
      return false;
    }
    fReportedTNS.addElement(paramString);
    return true;
  }
  
  void addGlobalAttributeDecl(XSAttributeDecl paramXSAttributeDecl)
  {
    String str1 = paramXSAttributeDecl.getNamespace();
    String str2 = str1 + "," + paramXSAttributeDecl.getName();
    if (fGlobalAttrDecls.get(str2) == null) {
      fGlobalAttrDecls.put(str2, paramXSAttributeDecl);
    }
  }
  
  void addGlobalAttributeGroupDecl(XSAttributeGroupDecl paramXSAttributeGroupDecl)
  {
    String str1 = paramXSAttributeGroupDecl.getNamespace();
    String str2 = str1 + "," + paramXSAttributeGroupDecl.getName();
    if (fGlobalAttrGrpDecls.get(str2) == null) {
      fGlobalAttrGrpDecls.put(str2, paramXSAttributeGroupDecl);
    }
  }
  
  void addGlobalElementDecl(XSElementDecl paramXSElementDecl)
  {
    String str1 = paramXSElementDecl.getNamespace();
    String str2 = str1 + "," + paramXSElementDecl.getName();
    if (fGlobalElemDecls.get(str2) == null) {
      fGlobalElemDecls.put(str2, paramXSElementDecl);
    }
  }
  
  void addGlobalGroupDecl(XSGroupDecl paramXSGroupDecl)
  {
    String str1 = paramXSGroupDecl.getNamespace();
    String str2 = str1 + "," + paramXSGroupDecl.getName();
    if (fGlobalGroupDecls.get(str2) == null) {
      fGlobalGroupDecls.put(str2, paramXSGroupDecl);
    }
  }
  
  void addGlobalNotationDecl(XSNotationDecl paramXSNotationDecl)
  {
    String str1 = paramXSNotationDecl.getNamespace();
    String str2 = str1 + "," + paramXSNotationDecl.getName();
    if (fGlobalNotationDecls.get(str2) == null) {
      fGlobalNotationDecls.put(str2, paramXSNotationDecl);
    }
  }
  
  void addGlobalTypeDecl(XSTypeDefinition paramXSTypeDefinition)
  {
    String str1 = paramXSTypeDefinition.getNamespace();
    String str2 = str1 + "," + paramXSTypeDefinition.getName();
    if (fGlobalTypeDecls.get(str2) == null) {
      fGlobalTypeDecls.put(str2, paramXSTypeDefinition);
    }
  }
  
  void addIDConstraintDecl(IdentityConstraint paramIdentityConstraint)
  {
    String str1 = paramIdentityConstraint.getNamespace();
    String str2 = str1 + "," + paramIdentityConstraint.getIdentityConstraintName();
    if (fGlobalIDConstraintDecls.get(str2) == null) {
      fGlobalIDConstraintDecls.put(str2, paramIdentityConstraint);
    }
  }
  
  private XSAttributeDecl getGlobalAttributeDecl(String paramString)
  {
    return (XSAttributeDecl)fGlobalAttrDecls.get(paramString);
  }
  
  private XSAttributeGroupDecl getGlobalAttributeGroupDecl(String paramString)
  {
    return (XSAttributeGroupDecl)fGlobalAttrGrpDecls.get(paramString);
  }
  
  private XSElementDecl getGlobalElementDecl(String paramString)
  {
    return (XSElementDecl)fGlobalElemDecls.get(paramString);
  }
  
  private XSGroupDecl getGlobalGroupDecl(String paramString)
  {
    return (XSGroupDecl)fGlobalGroupDecls.get(paramString);
  }
  
  private XSNotationDecl getGlobalNotationDecl(String paramString)
  {
    return (XSNotationDecl)fGlobalNotationDecls.get(paramString);
  }
  
  private XSTypeDefinition getGlobalTypeDecl(String paramString)
  {
    return (XSTypeDefinition)fGlobalTypeDecls.get(paramString);
  }
  
  private IdentityConstraint getIDConstraintDecl(String paramString)
  {
    return (IdentityConstraint)fGlobalIDConstraintDecls.get(paramString);
  }
  
  protected Object getGlobalDecl(XSDocumentInfo paramXSDocumentInfo, int paramInt, QName paramQName, Element paramElement)
  {
    if ((uri != null) && (uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (paramInt == 7))
    {
      localObject1 = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(localpart);
      if (localObject1 != null) {
        return localObject1;
      }
    }
    if ((!paramXSDocumentInfo.isAllowedNS(uri)) && (paramXSDocumentInfo.needReportTNSError(uri)))
    {
      localObject1 = uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
      reportSchemaError((String)localObject1, new Object[] { fDoc2SystemId.get(fSchemaElement), uri, rawname }, paramElement);
    }
    Object localObject1 = fGrammarBucket.getGrammar(uri);
    if (localObject1 == null)
    {
      if (needReportTNSError(uri)) {
        reportSchemaError("src-resolve", new Object[] { rawname, COMP_TYPE[paramInt] }, paramElement);
      }
      return null;
    }
    Object localObject2 = getGlobalDeclFromGrammar((SchemaGrammar)localObject1, paramInt, localpart);
    String str1 = uri + "," + localpart;
    if (!fTolerateDuplicates)
    {
      if (localObject2 != null) {
        return localObject2;
      }
    }
    else
    {
      localObject3 = getGlobalDecl(str1, paramInt);
      if (localObject3 != null) {
        return localObject3;
      }
    }
    Object localObject3 = null;
    Element localElement = null;
    XSDocumentInfo localXSDocumentInfo = null;
    switch (paramInt)
    {
    case 1: 
      localElement = (Element)fUnparsedAttributeRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedAttributeRegistrySub.get(str1);
      break;
    case 2: 
      localElement = (Element)fUnparsedAttributeGroupRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedAttributeGroupRegistrySub.get(str1);
      break;
    case 3: 
      localElement = (Element)fUnparsedElementRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedElementRegistrySub.get(str1);
      break;
    case 4: 
      localElement = (Element)fUnparsedGroupRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedGroupRegistrySub.get(str1);
      break;
    case 5: 
      localElement = (Element)fUnparsedIdentityConstraintRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedIdentityConstraintRegistrySub.get(str1);
      break;
    case 6: 
      localElement = (Element)fUnparsedNotationRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedNotationRegistrySub.get(str1);
      break;
    case 7: 
      localElement = (Element)fUnparsedTypeRegistry.get(str1);
      localXSDocumentInfo = (XSDocumentInfo)fUnparsedTypeRegistrySub.get(str1);
      break;
    default: 
      reportSchemaError("Internal-Error", new Object[] { "XSDHandler asked to locate component of type " + paramInt + "; it does not recognize this type!" }, paramElement);
    }
    if (localElement == null)
    {
      if (localObject2 == null) {
        reportSchemaError("src-resolve", new Object[] { rawname, COMP_TYPE[paramInt] }, paramElement);
      }
      return localObject2;
    }
    localObject3 = findXSDocumentForDecl(paramXSDocumentInfo, localElement, localXSDocumentInfo);
    String str2;
    if (localObject3 == null)
    {
      if (localObject2 == null)
      {
        str2 = uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
        reportSchemaError(str2, new Object[] { fDoc2SystemId.get(fSchemaElement), uri, rawname }, paramElement);
      }
      return localObject2;
    }
    if (DOMUtil.isHidden(localElement, fHiddenNodes))
    {
      if (localObject2 == null)
      {
        str2 = CIRCULAR_CODES[paramInt];
        if ((paramInt == 7) && (SchemaSymbols.ELT_COMPLEXTYPE.equals(DOMUtil.getLocalName(localElement)))) {
          str2 = "ct-props-correct.3";
        }
        reportSchemaError(str2, new Object[] { prefix + ":" + localpart }, paramElement);
      }
      return localObject2;
    }
    return traverseGlobalDecl(paramInt, localElement, (XSDocumentInfo)localObject3, (SchemaGrammar)localObject1);
  }
  
  protected Object getGlobalDecl(String paramString, int paramInt)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 1: 
      localObject = getGlobalAttributeDecl(paramString);
      break;
    case 2: 
      localObject = getGlobalAttributeGroupDecl(paramString);
      break;
    case 3: 
      localObject = getGlobalElementDecl(paramString);
      break;
    case 4: 
      localObject = getGlobalGroupDecl(paramString);
      break;
    case 5: 
      localObject = getIDConstraintDecl(paramString);
      break;
    case 6: 
      localObject = getGlobalNotationDecl(paramString);
      break;
    case 7: 
      localObject = getGlobalTypeDecl(paramString);
    }
    return localObject;
  }
  
  protected Object getGlobalDeclFromGrammar(SchemaGrammar paramSchemaGrammar, int paramInt, String paramString)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 1: 
      localObject = paramSchemaGrammar.getGlobalAttributeDecl(paramString);
      break;
    case 2: 
      localObject = paramSchemaGrammar.getGlobalAttributeGroupDecl(paramString);
      break;
    case 3: 
      localObject = paramSchemaGrammar.getGlobalElementDecl(paramString);
      break;
    case 4: 
      localObject = paramSchemaGrammar.getGlobalGroupDecl(paramString);
      break;
    case 5: 
      localObject = paramSchemaGrammar.getIDConstraintDecl(paramString);
      break;
    case 6: 
      localObject = paramSchemaGrammar.getGlobalNotationDecl(paramString);
      break;
    case 7: 
      localObject = paramSchemaGrammar.getGlobalTypeDecl(paramString);
    }
    return localObject;
  }
  
  protected Object getGlobalDeclFromGrammar(SchemaGrammar paramSchemaGrammar, int paramInt, String paramString1, String paramString2)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 1: 
      localObject = paramSchemaGrammar.getGlobalAttributeDecl(paramString1, paramString2);
      break;
    case 2: 
      localObject = paramSchemaGrammar.getGlobalAttributeGroupDecl(paramString1, paramString2);
      break;
    case 3: 
      localObject = paramSchemaGrammar.getGlobalElementDecl(paramString1, paramString2);
      break;
    case 4: 
      localObject = paramSchemaGrammar.getGlobalGroupDecl(paramString1, paramString2);
      break;
    case 5: 
      localObject = paramSchemaGrammar.getIDConstraintDecl(paramString1, paramString2);
      break;
    case 6: 
      localObject = paramSchemaGrammar.getGlobalNotationDecl(paramString1, paramString2);
      break;
    case 7: 
      localObject = paramSchemaGrammar.getGlobalTypeDecl(paramString1, paramString2);
    }
    return localObject;
  }
  
  protected Object traverseGlobalDecl(int paramInt, Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object localObject = null;
    DOMUtil.setHidden(paramElement, fHiddenNodes);
    SchemaNamespaceSupport localSchemaNamespaceSupport = null;
    Element localElement = DOMUtil.getParent(paramElement);
    if (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_REDEFINE)) {
      localSchemaNamespaceSupport = (SchemaNamespaceSupport)fRedefine2NSSupport.get(localElement);
    }
    paramXSDocumentInfo.backupNSSupport(localSchemaNamespaceSupport);
    switch (paramInt)
    {
    case 7: 
      if (DOMUtil.getLocalName(paramElement).equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
        localObject = fComplexTypeTraverser.traverseGlobal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      } else {
        localObject = fSimpleTypeTraverser.traverseGlobal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      }
      break;
    case 1: 
      localObject = fAttributeTraverser.traverseGlobal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      break;
    case 3: 
      localObject = fElementTraverser.traverseGlobal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      break;
    case 2: 
      localObject = fAttributeGroupTraverser.traverseGlobal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      break;
    case 4: 
      localObject = fGroupTraverser.traverseGlobal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      break;
    case 6: 
      localObject = fNotationTraverser.traverse(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
      break;
    }
    paramXSDocumentInfo.restoreNSSupport();
    return localObject;
  }
  
  public String schemaDocument2SystemId(XSDocumentInfo paramXSDocumentInfo)
  {
    return (String)fDoc2SystemId.get(fSchemaElement);
  }
  
  Object getGrpOrAttrGrpRedefinedByRestriction(int paramInt, QName paramQName, XSDocumentInfo paramXSDocumentInfo, Element paramElement)
  {
    String str1 = "," + localpart;
    String str2 = null;
    switch (paramInt)
    {
    case 2: 
      str2 = (String)fRedefinedRestrictedAttributeGroupRegistry.get(str1);
      break;
    case 4: 
      str2 = (String)fRedefinedRestrictedGroupRegistry.get(str1);
      break;
    default: 
      return null;
    }
    if (str2 == null) {
      return null;
    }
    int i = str2.indexOf(",");
    QName localQName = new QName(XMLSymbols.EMPTY_STRING, str2.substring(i + 1), str2.substring(i), i == 0 ? null : str2.substring(0, i));
    Object localObject = getGlobalDecl(paramXSDocumentInfo, paramInt, localQName, paramElement);
    if (localObject == null)
    {
      switch (paramInt)
      {
      case 2: 
        reportSchemaError("src-redefine.7.2.1", new Object[] { localpart }, paramElement);
        break;
      case 4: 
        reportSchemaError("src-redefine.6.2.1", new Object[] { localpart }, paramElement);
      }
      return null;
    }
    return localObject;
  }
  
  protected void resolveKeyRefs()
  {
    for (int i = 0; i < fKeyrefStackPos; i++)
    {
      XSDocumentInfo localXSDocumentInfo = fKeyrefsMapXSDocumentInfo[i];
      fNamespaceSupport.makeGlobal();
      fNamespaceSupport.setEffectiveContext(fKeyrefNamespaceContext[i]);
      SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(fTargetNamespace);
      DOMUtil.setHidden(fKeyrefs[i], fHiddenNodes);
      fKeyrefTraverser.traverse(fKeyrefs[i], fKeyrefElems[i], localXSDocumentInfo, localSchemaGrammar);
    }
  }
  
  protected Hashtable getIDRegistry()
  {
    return fUnparsedIdentityConstraintRegistry;
  }
  
  protected Hashtable getIDRegistry_sub()
  {
    return fUnparsedIdentityConstraintRegistrySub;
  }
  
  protected void storeKeyRef(Element paramElement, XSDocumentInfo paramXSDocumentInfo, XSElementDecl paramXSElementDecl)
  {
    String str = DOMUtil.getAttrValue(paramElement, SchemaSymbols.ATT_NAME);
    Object localObject;
    if (str.length() != 0)
    {
      localObject = fTargetNamespace + "," + str;
      checkForDuplicateNames((String)localObject, 5, fUnparsedIdentityConstraintRegistry, fUnparsedIdentityConstraintRegistrySub, paramElement, paramXSDocumentInfo);
    }
    if (fKeyrefStackPos == fKeyrefs.length)
    {
      localObject = new Element[fKeyrefStackPos + 2];
      System.arraycopy(fKeyrefs, 0, localObject, 0, fKeyrefStackPos);
      fKeyrefs = ((Element[])localObject);
      XSElementDecl[] arrayOfXSElementDecl = new XSElementDecl[fKeyrefStackPos + 2];
      System.arraycopy(fKeyrefElems, 0, arrayOfXSElementDecl, 0, fKeyrefStackPos);
      fKeyrefElems = arrayOfXSElementDecl;
      String[][] arrayOfString; = new String[fKeyrefStackPos + 2][];
      System.arraycopy(fKeyrefNamespaceContext, 0, arrayOfString;, 0, fKeyrefStackPos);
      fKeyrefNamespaceContext = arrayOfString;;
      XSDocumentInfo[] arrayOfXSDocumentInfo = new XSDocumentInfo[fKeyrefStackPos + 2];
      System.arraycopy(fKeyrefsMapXSDocumentInfo, 0, arrayOfXSDocumentInfo, 0, fKeyrefStackPos);
      fKeyrefsMapXSDocumentInfo = arrayOfXSDocumentInfo;
    }
    fKeyrefs[fKeyrefStackPos] = paramElement;
    fKeyrefElems[fKeyrefStackPos] = paramXSElementDecl;
    fKeyrefNamespaceContext[fKeyrefStackPos] = fNamespaceSupport.getEffectiveLocalContext();
    fKeyrefsMapXSDocumentInfo[(fKeyrefStackPos++)] = paramXSDocumentInfo;
  }
  
  private Element resolveSchema(XSDDescription paramXSDDescription, boolean paramBoolean1, Element paramElement, boolean paramBoolean2)
  {
    XMLInputSource localXMLInputSource = null;
    try
    {
      Hashtable localHashtable = paramBoolean2 ? fLocationPairs : EMPTY_TABLE;
      localXMLInputSource = XMLSchemaLoader.resolveDocument(paramXSDDescription, localHashtable, fEntityResolver);
    }
    catch (IOException localIOException)
    {
      if (paramBoolean1) {
        reportSchemaError("schema_reference.4", new Object[] { paramXSDDescription.getLocationHints()[0] }, paramElement);
      } else {
        reportSchemaWarning("schema_reference.4", new Object[] { paramXSDDescription.getLocationHints()[0] }, paramElement);
      }
    }
    if ((localXMLInputSource instanceof DOMInputSource)) {
      return getSchemaDocument(paramXSDDescription.getTargetNamespace(), (DOMInputSource)localXMLInputSource, paramBoolean1, paramXSDDescription.getContextType(), paramElement);
    }
    if ((localXMLInputSource instanceof SAXInputSource)) {
      return getSchemaDocument(paramXSDDescription.getTargetNamespace(), (SAXInputSource)localXMLInputSource, paramBoolean1, paramXSDDescription.getContextType(), paramElement);
    }
    if ((localXMLInputSource instanceof StAXInputSource)) {
      return getSchemaDocument(paramXSDDescription.getTargetNamespace(), (StAXInputSource)localXMLInputSource, paramBoolean1, paramXSDDescription.getContextType(), paramElement);
    }
    if ((localXMLInputSource instanceof XSInputSource)) {
      return getSchemaDocument((XSInputSource)localXMLInputSource, paramXSDDescription);
    }
    return getSchemaDocument(paramXSDDescription.getTargetNamespace(), localXMLInputSource, paramBoolean1, paramXSDDescription.getContextType(), paramElement);
  }
  
  private Element resolveSchema(XMLInputSource paramXMLInputSource, XSDDescription paramXSDDescription, boolean paramBoolean, Element paramElement)
  {
    if ((paramXMLInputSource instanceof DOMInputSource)) {
      return getSchemaDocument(paramXSDDescription.getTargetNamespace(), (DOMInputSource)paramXMLInputSource, paramBoolean, paramXSDDescription.getContextType(), paramElement);
    }
    if ((paramXMLInputSource instanceof SAXInputSource)) {
      return getSchemaDocument(paramXSDDescription.getTargetNamespace(), (SAXInputSource)paramXMLInputSource, paramBoolean, paramXSDDescription.getContextType(), paramElement);
    }
    if ((paramXMLInputSource instanceof StAXInputSource)) {
      return getSchemaDocument(paramXSDDescription.getTargetNamespace(), (StAXInputSource)paramXMLInputSource, paramBoolean, paramXSDDescription.getContextType(), paramElement);
    }
    if ((paramXMLInputSource instanceof XSInputSource)) {
      return getSchemaDocument((XSInputSource)paramXMLInputSource, paramXSDDescription);
    }
    return getSchemaDocument(paramXSDDescription.getTargetNamespace(), paramXMLInputSource, paramBoolean, paramXSDDescription.getContextType(), paramElement);
  }
  
  private XMLInputSource resolveSchemaSource(XSDDescription paramXSDDescription, boolean paramBoolean1, Element paramElement, boolean paramBoolean2)
  {
    XMLInputSource localXMLInputSource = null;
    try
    {
      Hashtable localHashtable = paramBoolean2 ? fLocationPairs : EMPTY_TABLE;
      localXMLInputSource = XMLSchemaLoader.resolveDocument(paramXSDDescription, localHashtable, fEntityResolver);
    }
    catch (IOException localIOException)
    {
      if (paramBoolean1) {
        reportSchemaError("schema_reference.4", new Object[] { paramXSDDescription.getLocationHints()[0] }, paramElement);
      } else {
        reportSchemaWarning("schema_reference.4", new Object[] { paramXSDDescription.getLocationHints()[0] }, paramElement);
      }
    }
    return localXMLInputSource;
  }
  
  private Element getSchemaDocument(String paramString, XMLInputSource paramXMLInputSource, boolean paramBoolean, short paramShort, Element paramElement)
  {
    boolean bool = true;
    Object localObject = null;
    Element localElement = null;
    try
    {
      if ((paramXMLInputSource != null) && ((paramXMLInputSource.getSystemId() != null) || (paramXMLInputSource.getByteStream() != null) || (paramXMLInputSource.getCharacterStream() != null)))
      {
        XSDKey localXSDKey = null;
        String str = null;
        if (paramShort != 3)
        {
          str = XMLEntityManager.expandSystemId(paramXMLInputSource.getSystemId(), paramXMLInputSource.getBaseSystemId(), false);
          localXSDKey = new XSDKey(str, paramShort, paramString);
          if ((localElement = (Element)fTraversed.get(localXSDKey)) != null)
          {
            fLastSchemaWasDuplicate = true;
            return localElement;
          }
        }
        fSchemaParser.parse(paramXMLInputSource);
        Document localDocument = fSchemaParser.getDocument();
        localElement = localDocument != null ? DOMUtil.getRoot(localDocument) : null;
        return getSchemaDocument0(localXSDKey, str, localElement);
      }
      bool = false;
    }
    catch (IOException localIOException)
    {
      localObject = localIOException;
    }
    return getSchemaDocument1(paramBoolean, bool, paramXMLInputSource, paramElement, localObject);
  }
  
  private Element getSchemaDocument(String paramString, SAXInputSource paramSAXInputSource, boolean paramBoolean, short paramShort, Element paramElement)
  {
    Object localObject1 = paramSAXInputSource.getXMLReader();
    InputSource localInputSource = paramSAXInputSource.getInputSource();
    boolean bool1 = true;
    Object localObject2 = null;
    Element localElement = null;
    try
    {
      if ((localInputSource != null) && ((localInputSource.getSystemId() != null) || (localInputSource.getByteStream() != null) || (localInputSource.getCharacterStream() != null)))
      {
        XSDKey localXSDKey = null;
        String str = null;
        if (paramShort != 3)
        {
          str = XMLEntityManager.expandSystemId(localInputSource.getSystemId(), paramSAXInputSource.getBaseSystemId(), false);
          localXSDKey = new XSDKey(str, paramShort, paramString);
          if ((localElement = (Element)fTraversed.get(localXSDKey)) != null)
          {
            fLastSchemaWasDuplicate = true;
            return localElement;
          }
        }
        boolean bool2 = false;
        if (localObject1 != null)
        {
          try
          {
            bool2 = ((XMLReader)localObject1).getFeature("http://xml.org/sax/features/namespace-prefixes");
          }
          catch (SAXException localSAXException2) {}
        }
        else
        {
          try
          {
            localObject1 = XMLReaderFactory.createXMLReader();
          }
          catch (SAXException localSAXException3)
          {
            localObject1 = new SAXParser();
          }
          try
          {
            ((XMLReader)localObject1).setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            bool2 = true;
            if ((localObject1 instanceof SAXParser))
            {
              Object localObject3 = fSchemaParser.getProperty("http://apache.org/xml/properties/security-manager");
              if (localObject3 != null) {
                ((XMLReader)localObject1).setProperty("http://apache.org/xml/properties/security-manager", localObject3);
              }
            }
          }
          catch (SAXException localSAXException4) {}
        }
        boolean bool3 = false;
        try
        {
          bool3 = ((XMLReader)localObject1).getFeature("http://xml.org/sax/features/string-interning");
        }
        catch (SAXException localSAXException5) {}
        if (fXSContentHandler == null) {
          fXSContentHandler = new SchemaContentHandler();
        }
        fXSContentHandler.reset(fSchemaParser, fSymbolTable, bool2, bool3);
        ((XMLReader)localObject1).setContentHandler(fXSContentHandler);
        ((XMLReader)localObject1).setErrorHandler(fErrorReporter.getSAXErrorHandler());
        ((XMLReader)localObject1).parse(localInputSource);
        try
        {
          ((XMLReader)localObject1).setContentHandler(null);
          ((XMLReader)localObject1).setErrorHandler(null);
        }
        catch (Exception localException) {}
        Document localDocument = fXSContentHandler.getDocument();
        localElement = localDocument != null ? DOMUtil.getRoot(localDocument) : null;
        return getSchemaDocument0(localXSDKey, str, localElement);
      }
      bool1 = false;
    }
    catch (SAXParseException localSAXParseException)
    {
      throw SAX2XNIUtil.createXMLParseException0(localSAXParseException);
    }
    catch (SAXException localSAXException1)
    {
      throw SAX2XNIUtil.createXNIException0(localSAXException1);
    }
    catch (IOException localIOException)
    {
      localObject2 = localIOException;
    }
    return getSchemaDocument1(paramBoolean, bool1, paramSAXInputSource, paramElement, localObject2);
  }
  
  private Element getSchemaDocument(String paramString, DOMInputSource paramDOMInputSource, boolean paramBoolean, short paramShort, Element paramElement)
  {
    boolean bool = true;
    Object localObject1 = null;
    Object localObject2 = null;
    Element localElement = null;
    Node localNode1 = paramDOMInputSource.getNode();
    int i = -1;
    if (localNode1 != null)
    {
      i = localNode1.getNodeType();
      if (i == 9) {
        localElement = DOMUtil.getRoot((Document)localNode1);
      } else if (i == 1) {
        localElement = (Element)localNode1;
      }
    }
    try
    {
      if (localElement != null)
      {
        XSDKey localXSDKey = null;
        String str = null;
        if (paramShort != 3)
        {
          str = XMLEntityManager.expandSystemId(paramDOMInputSource.getSystemId(), paramDOMInputSource.getBaseSystemId(), false);
          int j = i == 9 ? 1 : 0;
          if (j == 0)
          {
            Node localNode2 = localElement.getParentNode();
            if (localNode2 != null) {
              j = localNode2.getNodeType() == 9 ? 1 : 0;
            }
          }
          if (j != 0)
          {
            localXSDKey = new XSDKey(str, paramShort, paramString);
            if ((localObject2 = (Element)fTraversed.get(localXSDKey)) != null)
            {
              fLastSchemaWasDuplicate = true;
              return localObject2;
            }
          }
        }
        localObject2 = localElement;
        return getSchemaDocument0(localXSDKey, str, (Element)localObject2);
      }
      bool = false;
    }
    catch (IOException localIOException)
    {
      localObject1 = localIOException;
    }
    return getSchemaDocument1(paramBoolean, bool, paramDOMInputSource, paramElement, localObject1);
  }
  
  private Element getSchemaDocument(String paramString, StAXInputSource paramStAXInputSource, boolean paramBoolean, short paramShort, Element paramElement)
  {
    Object localObject1 = null;
    Element localElement = null;
    try
    {
      boolean bool1 = paramStAXInputSource.shouldConsumeRemainingContent();
      localObject2 = paramStAXInputSource.getXMLStreamReader();
      localObject3 = paramStAXInputSource.getXMLEventReader();
      XSDKey localXSDKey = null;
      String str = null;
      if (paramShort != 3)
      {
        str = XMLEntityManager.expandSystemId(paramStAXInputSource.getSystemId(), paramStAXInputSource.getBaseSystemId(), false);
        boolean bool2 = bool1;
        if (!bool2) {
          if (localObject2 != null) {
            bool2 = ((XMLStreamReader)localObject2).getEventType() == 7;
          } else {
            bool2 = ((XMLEventReader)localObject3).peek().isStartDocument();
          }
        }
        if (bool2)
        {
          localXSDKey = new XSDKey(str, paramShort, paramString);
          if ((localElement = (Element)fTraversed.get(localXSDKey)) != null)
          {
            fLastSchemaWasDuplicate = true;
            return localElement;
          }
        }
      }
      if (fStAXSchemaParser == null) {
        fStAXSchemaParser = new StAXSchemaParser();
      }
      fStAXSchemaParser.reset(fSchemaParser, fSymbolTable);
      if (localObject2 != null)
      {
        fStAXSchemaParser.parse((XMLStreamReader)localObject2);
        if (bool1) {
          while (((XMLStreamReader)localObject2).hasNext()) {
            ((XMLStreamReader)localObject2).next();
          }
        }
      }
      else
      {
        fStAXSchemaParser.parse((XMLEventReader)localObject3);
        if (bool1) {
          while (((XMLEventReader)localObject3).hasNext()) {
            ((XMLEventReader)localObject3).nextEvent();
          }
        }
      }
      Document localDocument = fStAXSchemaParser.getDocument();
      localElement = localDocument != null ? DOMUtil.getRoot(localDocument) : null;
      return getSchemaDocument0(localXSDKey, str, localElement);
    }
    catch (XMLStreamException localXMLStreamException)
    {
      Object localObject3;
      Object localObject2 = localXMLStreamException.getNestedException();
      if ((localObject2 instanceof IOException))
      {
        localObject1 = (IOException)localObject2;
      }
      else
      {
        localObject3 = new StAXLocationWrapper();
        ((StAXLocationWrapper)localObject3).setLocation(localXMLStreamException.getLocation());
        throw new XMLParseException((XMLLocator)localObject3, localXMLStreamException.getMessage(), localXMLStreamException);
      }
    }
    catch (IOException localIOException)
    {
      localObject1 = localIOException;
    }
    return getSchemaDocument1(paramBoolean, true, paramStAXInputSource, paramElement, (IOException)localObject1);
  }
  
  private Element getSchemaDocument0(XSDKey paramXSDKey, String paramString, Element paramElement)
  {
    if (paramXSDKey != null) {
      fTraversed.put(paramXSDKey, paramElement);
    }
    if (paramString != null) {
      fDoc2SystemId.put(paramElement, paramString);
    }
    fLastSchemaWasDuplicate = false;
    return paramElement;
  }
  
  private Element getSchemaDocument1(boolean paramBoolean1, boolean paramBoolean2, XMLInputSource paramXMLInputSource, Element paramElement, IOException paramIOException)
  {
    if (paramBoolean1)
    {
      if (paramBoolean2) {
        reportSchemaError("schema_reference.4", new Object[] { paramXMLInputSource.getSystemId() }, paramElement, paramIOException);
      } else {
        reportSchemaError("schema_reference.4", new Object[] { paramXMLInputSource == null ? "" : paramXMLInputSource.getSystemId() }, paramElement, paramIOException);
      }
    }
    else if (paramBoolean2) {
      reportSchemaWarning("schema_reference.4", new Object[] { paramXMLInputSource.getSystemId() }, paramElement, paramIOException);
    }
    fLastSchemaWasDuplicate = false;
    return null;
  }
  
  private Element getSchemaDocument(XSInputSource paramXSInputSource, XSDDescription paramXSDDescription)
  {
    SchemaGrammar[] arrayOfSchemaGrammar = paramXSInputSource.getGrammars();
    int i = paramXSDDescription.getContextType();
    Object localObject;
    if ((arrayOfSchemaGrammar != null) && (arrayOfSchemaGrammar.length > 0))
    {
      localObject = expandGrammars(arrayOfSchemaGrammar);
      if ((fNamespaceGrowth) || (!existingGrammars((Vector)localObject)))
      {
        addGrammars((Vector)localObject);
        if (i == 3) {
          paramXSDDescription.setTargetNamespace(arrayOfSchemaGrammar[0].getTargetNamespace());
        }
      }
    }
    else
    {
      localObject = paramXSInputSource.getComponents();
      if ((localObject != null) && (localObject.length > 0))
      {
        Hashtable localHashtable = new Hashtable();
        Vector localVector = expandComponents((XSObject[])localObject, localHashtable);
        if ((fNamespaceGrowth) || (canAddComponents(localVector)))
        {
          addGlobalComponents(localVector, localHashtable);
          if (i == 3) {
            paramXSDDescription.setTargetNamespace(localObject[0].getNamespace());
          }
        }
      }
    }
    return null;
  }
  
  private Vector expandGrammars(SchemaGrammar[] paramArrayOfSchemaGrammar)
  {
    Vector localVector1 = new Vector();
    for (int i = 0; i < paramArrayOfSchemaGrammar.length; i++) {
      if (!localVector1.contains(paramArrayOfSchemaGrammar[i])) {
        localVector1.add(paramArrayOfSchemaGrammar[i]);
      }
    }
    for (int j = 0; j < localVector1.size(); j++)
    {
      SchemaGrammar localSchemaGrammar1 = (SchemaGrammar)localVector1.elementAt(j);
      Vector localVector2 = localSchemaGrammar1.getImportedGrammars();
      if (localVector2 != null) {
        for (int k = localVector2.size() - 1; k >= 0; k--)
        {
          SchemaGrammar localSchemaGrammar2 = (SchemaGrammar)localVector2.elementAt(k);
          if (!localVector1.contains(localSchemaGrammar2)) {
            localVector1.addElement(localSchemaGrammar2);
          }
        }
      }
    }
    return localVector1;
  }
  
  private boolean existingGrammars(Vector paramVector)
  {
    int i = paramVector.size();
    XSDDescription localXSDDescription = new XSDDescription();
    for (int j = 0; j < i; j++)
    {
      SchemaGrammar localSchemaGrammar1 = (SchemaGrammar)paramVector.elementAt(j);
      localXSDDescription.setNamespace(localSchemaGrammar1.getTargetNamespace());
      SchemaGrammar localSchemaGrammar2 = findGrammar(localXSDDescription, false);
      if (localSchemaGrammar2 != null) {
        return true;
      }
    }
    return false;
  }
  
  private boolean canAddComponents(Vector paramVector)
  {
    int i = paramVector.size();
    XSDDescription localXSDDescription = new XSDDescription();
    for (int j = 0; j < i; j++)
    {
      XSObject localXSObject = (XSObject)paramVector.elementAt(j);
      if (!canAddComponent(localXSObject, localXSDDescription)) {
        return false;
      }
    }
    return true;
  }
  
  private boolean canAddComponent(XSObject paramXSObject, XSDDescription paramXSDDescription)
  {
    paramXSDDescription.setNamespace(paramXSObject.getNamespace());
    SchemaGrammar localSchemaGrammar = findGrammar(paramXSDDescription, false);
    if (localSchemaGrammar == null) {
      return true;
    }
    if (localSchemaGrammar.isImmutable()) {
      return false;
    }
    int i = paramXSObject.getType();
    String str = paramXSObject.getName();
    switch (i)
    {
    case 3: 
      if (localSchemaGrammar.getGlobalTypeDecl(str) == paramXSObject) {
        return true;
      }
      break;
    case 1: 
      if (localSchemaGrammar.getGlobalAttributeDecl(str) == paramXSObject) {
        return true;
      }
      break;
    case 5: 
      if (localSchemaGrammar.getGlobalAttributeDecl(str) == paramXSObject) {
        return true;
      }
      break;
    case 2: 
      if (localSchemaGrammar.getGlobalElementDecl(str) == paramXSObject) {
        return true;
      }
      break;
    case 6: 
      if (localSchemaGrammar.getGlobalGroupDecl(str) == paramXSObject) {
        return true;
      }
      break;
    case 11: 
      if (localSchemaGrammar.getGlobalNotationDecl(str) == paramXSObject) {
        return true;
      }
      break;
    case 4: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    default: 
      return true;
    }
    return false;
  }
  
  private void addGrammars(Vector paramVector)
  {
    int i = paramVector.size();
    XSDDescription localXSDDescription = new XSDDescription();
    for (int j = 0; j < i; j++)
    {
      SchemaGrammar localSchemaGrammar1 = (SchemaGrammar)paramVector.elementAt(j);
      localXSDDescription.setNamespace(localSchemaGrammar1.getTargetNamespace());
      SchemaGrammar localSchemaGrammar2 = findGrammar(localXSDDescription, fNamespaceGrowth);
      if (localSchemaGrammar1 != localSchemaGrammar2) {
        addGrammarComponents(localSchemaGrammar1, localSchemaGrammar2);
      }
    }
  }
  
  private void addGrammarComponents(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    if (paramSchemaGrammar2 == null)
    {
      createGrammarFrom(paramSchemaGrammar1);
      return;
    }
    SchemaGrammar localSchemaGrammar = paramSchemaGrammar2;
    if (localSchemaGrammar.isImmutable()) {
      localSchemaGrammar = createGrammarFrom(paramSchemaGrammar2);
    }
    addNewGrammarLocations(paramSchemaGrammar1, localSchemaGrammar);
    addNewImportedGrammars(paramSchemaGrammar1, localSchemaGrammar);
    addNewGrammarComponents(paramSchemaGrammar1, localSchemaGrammar);
  }
  
  private SchemaGrammar createGrammarFrom(SchemaGrammar paramSchemaGrammar)
  {
    SchemaGrammar localSchemaGrammar = new SchemaGrammar(paramSchemaGrammar);
    fGrammarBucket.putGrammar(localSchemaGrammar);
    updateImportListWith(localSchemaGrammar);
    updateImportListFor(localSchemaGrammar);
    return localSchemaGrammar;
  }
  
  private void addNewGrammarLocations(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    StringList localStringList1 = paramSchemaGrammar1.getDocumentLocations();
    int i = localStringList1.size();
    StringList localStringList2 = paramSchemaGrammar2.getDocumentLocations();
    for (int j = 0; j < i; j++)
    {
      String str = localStringList1.item(j);
      if (!localStringList2.contains(str)) {
        paramSchemaGrammar2.addDocument(null, str);
      }
    }
  }
  
  private void addNewImportedGrammars(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    Vector localVector1 = paramSchemaGrammar1.getImportedGrammars();
    if (localVector1 != null)
    {
      Vector localVector2 = paramSchemaGrammar2.getImportedGrammars();
      if (localVector2 == null)
      {
        localVector2 = (Vector)localVector1.clone();
        paramSchemaGrammar2.setImportedGrammars(localVector2);
      }
      else
      {
        updateImportList(localVector1, localVector2);
      }
    }
  }
  
  private void updateImportList(Vector paramVector1, Vector paramVector2)
  {
    int i = paramVector1.size();
    for (int j = 0; j < i; j++)
    {
      SchemaGrammar localSchemaGrammar = (SchemaGrammar)paramVector1.elementAt(j);
      if (!containedImportedGrammar(paramVector2, localSchemaGrammar)) {
        paramVector2.add(localSchemaGrammar);
      }
    }
  }
  
  private void addNewGrammarComponents(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    paramSchemaGrammar2.resetComponents();
    addGlobalElementDecls(paramSchemaGrammar1, paramSchemaGrammar2);
    addGlobalAttributeDecls(paramSchemaGrammar1, paramSchemaGrammar2);
    addGlobalAttributeGroupDecls(paramSchemaGrammar1, paramSchemaGrammar2);
    addGlobalGroupDecls(paramSchemaGrammar1, paramSchemaGrammar2);
    addGlobalTypeDecls(paramSchemaGrammar1, paramSchemaGrammar2);
    addGlobalNotationDecls(paramSchemaGrammar1, paramSchemaGrammar2);
  }
  
  private void addGlobalElementDecls(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    // Byte code:
    //   0: aload_1
    //   1: iconst_2
    //   2: invokevirtual 495	org/apache/xerces/impl/xs/SchemaGrammar:getComponents	(S)Lorg/apache/xerces/xs/XSNamedMap;
    //   5: astore_3
    //   6: aload_3
    //   7: invokeinterface 496 1 0
    //   12: istore 4
    //   14: iconst_0
    //   15: istore 7
    //   17: goto +51 -> 68
    //   20: aload_3
    //   21: iload 7
    //   23: invokeinterface 497 2 0
    //   28: checkcast 63	org/apache/xerces/impl/xs/XSElementDecl
    //   31: astore 5
    //   33: aload_2
    //   34: aload 5
    //   36: invokevirtual 306	org/apache/xerces/impl/xs/XSElementDecl:getName	()Ljava/lang/String;
    //   39: invokevirtual 355	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalElementDecl	(Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSElementDecl;
    //   42: astore 6
    //   44: aload 6
    //   46: ifnonnull +12 -> 58
    //   49: aload_2
    //   50: aload 5
    //   52: invokevirtual 498	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalElementDecl	(Lorg/apache/xerces/impl/xs/XSElementDecl;)V
    //   55: goto +10 -> 65
    //   58: aload 6
    //   60: aload 5
    //   62: if_acmpeq +3 -> 65
    //   65: iinc 7 1
    //   68: iload 7
    //   70: iload 4
    //   72: if_icmplt -52 -> 20
    //   75: aload_1
    //   76: iconst_2
    //   77: invokevirtual 499	org/apache/xerces/impl/xs/SchemaGrammar:getComponentsExt	(S)Lorg/apache/xerces/xs/datatypes/ObjectList;
    //   80: astore 8
    //   82: aload 8
    //   84: invokeinterface 500 1 0
    //   89: istore 4
    //   91: iconst_0
    //   92: istore 9
    //   94: goto +104 -> 198
    //   97: aload 8
    //   99: iload 9
    //   101: invokeinterface 501 2 0
    //   106: checkcast 8	java/lang/String
    //   109: astore 10
    //   111: aload 10
    //   113: bipush 44
    //   115: invokevirtual 502	java/lang/String:indexOf	(I)I
    //   118: istore 11
    //   120: aload 10
    //   122: iconst_0
    //   123: iload 11
    //   125: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   128: astore 12
    //   130: aload 10
    //   132: iload 11
    //   134: iconst_1
    //   135: iadd
    //   136: aload 10
    //   138: invokevirtual 115	java/lang/String:length	()I
    //   141: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   144: astore 13
    //   146: aload 8
    //   148: iload 9
    //   150: iconst_1
    //   151: iadd
    //   152: invokeinterface 501 2 0
    //   157: checkcast 63	org/apache/xerces/impl/xs/XSElementDecl
    //   160: astore 5
    //   162: aload_2
    //   163: aload 13
    //   165: aload 12
    //   167: invokevirtual 362	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalElementDecl	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSElementDecl;
    //   170: astore 6
    //   172: aload 6
    //   174: ifnonnull +14 -> 188
    //   177: aload_2
    //   178: aload 5
    //   180: aload 12
    //   182: invokevirtual 503	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalElementDecl	(Lorg/apache/xerces/impl/xs/XSElementDecl;Ljava/lang/String;)V
    //   185: goto +10 -> 195
    //   188: aload 6
    //   190: aload 5
    //   192: if_acmpeq +3 -> 195
    //   195: iinc 9 2
    //   198: iload 9
    //   200: iload 4
    //   202: if_icmplt -105 -> 97
    //   205: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	206	0	this	XSDHandler
    //   0	206	1	paramSchemaGrammar1	SchemaGrammar
    //   0	206	2	paramSchemaGrammar2	SchemaGrammar
    //   5	16	3	localXSNamedMap	XSNamedMap
    //   12	191	4	i	int
    //   31	160	5	localXSElementDecl1	XSElementDecl
    //   42	147	6	localXSElementDecl2	XSElementDecl
    //   15	58	7	j	int
    //   80	67	8	localObjectList	ObjectList
    //   92	111	9	k	int
    //   109	28	10	str1	String
    //   118	18	11	m	int
    //   128	53	12	str2	String
    //   144	20	13	str3	String
  }
  
  private void addGlobalAttributeDecls(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    // Byte code:
    //   0: aload_1
    //   1: iconst_1
    //   2: invokevirtual 495	org/apache/xerces/impl/xs/SchemaGrammar:getComponents	(S)Lorg/apache/xerces/xs/XSNamedMap;
    //   5: astore_3
    //   6: aload_3
    //   7: invokeinterface 496 1 0
    //   12: istore 4
    //   14: iconst_0
    //   15: istore 7
    //   17: goto +72 -> 89
    //   20: aload_3
    //   21: iload 7
    //   23: invokeinterface 497 2 0
    //   28: checkcast 316	org/apache/xerces/impl/xs/XSAttributeDecl
    //   31: astore 5
    //   33: aload_2
    //   34: aload 5
    //   36: invokevirtual 300	org/apache/xerces/impl/xs/XSAttributeDecl:getName	()Ljava/lang/String;
    //   39: invokevirtual 353	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalAttributeDecl	(Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSAttributeDecl;
    //   42: astore 6
    //   44: aload 6
    //   46: ifnonnull +12 -> 58
    //   49: aload_2
    //   50: aload 5
    //   52: invokevirtual 504	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalAttributeDecl	(Lorg/apache/xerces/impl/xs/XSAttributeDecl;)V
    //   55: goto +31 -> 86
    //   58: aload 6
    //   60: aload 5
    //   62: if_acmpeq +24 -> 86
    //   65: aload_0
    //   66: getfield 47	org/apache/xerces/impl/xs/traversers/XSDHandler:fTolerateDuplicates	Z
    //   69: ifne +17 -> 86
    //   72: aload_0
    //   73: aload 5
    //   75: invokevirtual 299	org/apache/xerces/impl/xs/XSAttributeDecl:getNamespace	()Ljava/lang/String;
    //   78: aload 5
    //   80: invokevirtual 300	org/apache/xerces/impl/xs/XSAttributeDecl:getName	()Ljava/lang/String;
    //   83: invokespecial 505	org/apache/xerces/impl/xs/traversers/XSDHandler:reportSharingError	(Ljava/lang/String;Ljava/lang/String;)V
    //   86: iinc 7 1
    //   89: iload 7
    //   91: iload 4
    //   93: if_icmplt -73 -> 20
    //   96: aload_1
    //   97: iconst_1
    //   98: invokevirtual 499	org/apache/xerces/impl/xs/SchemaGrammar:getComponentsExt	(S)Lorg/apache/xerces/xs/datatypes/ObjectList;
    //   101: astore 8
    //   103: aload 8
    //   105: invokeinterface 500 1 0
    //   110: istore 4
    //   112: iconst_0
    //   113: istore 9
    //   115: goto +104 -> 219
    //   118: aload 8
    //   120: iload 9
    //   122: invokeinterface 501 2 0
    //   127: checkcast 8	java/lang/String
    //   130: astore 10
    //   132: aload 10
    //   134: bipush 44
    //   136: invokevirtual 502	java/lang/String:indexOf	(I)I
    //   139: istore 11
    //   141: aload 10
    //   143: iconst_0
    //   144: iload 11
    //   146: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   149: astore 12
    //   151: aload 10
    //   153: iload 11
    //   155: iconst_1
    //   156: iadd
    //   157: aload 10
    //   159: invokevirtual 115	java/lang/String:length	()I
    //   162: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   165: astore 13
    //   167: aload 8
    //   169: iload 9
    //   171: iconst_1
    //   172: iadd
    //   173: invokeinterface 501 2 0
    //   178: checkcast 316	org/apache/xerces/impl/xs/XSAttributeDecl
    //   181: astore 5
    //   183: aload_2
    //   184: aload 13
    //   186: aload 12
    //   188: invokevirtual 360	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalAttributeDecl	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSAttributeDecl;
    //   191: astore 6
    //   193: aload 6
    //   195: ifnonnull +14 -> 209
    //   198: aload_2
    //   199: aload 5
    //   201: aload 12
    //   203: invokevirtual 506	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalAttributeDecl	(Lorg/apache/xerces/impl/xs/XSAttributeDecl;Ljava/lang/String;)V
    //   206: goto +10 -> 216
    //   209: aload 6
    //   211: aload 5
    //   213: if_acmpeq +3 -> 216
    //   216: iinc 9 2
    //   219: iload 9
    //   221: iload 4
    //   223: if_icmplt -105 -> 118
    //   226: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	227	0	this	XSDHandler
    //   0	227	1	paramSchemaGrammar1	SchemaGrammar
    //   0	227	2	paramSchemaGrammar2	SchemaGrammar
    //   5	16	3	localXSNamedMap	XSNamedMap
    //   12	212	4	i	int
    //   31	181	5	localXSAttributeDecl1	XSAttributeDecl
    //   42	168	6	localXSAttributeDecl2	XSAttributeDecl
    //   15	79	7	j	int
    //   101	67	8	localObjectList	ObjectList
    //   113	111	9	k	int
    //   130	28	10	str1	String
    //   139	18	11	m	int
    //   149	53	12	str2	String
    //   165	20	13	str3	String
  }
  
  private void addGlobalAttributeGroupDecls(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    // Byte code:
    //   0: aload_1
    //   1: iconst_5
    //   2: invokevirtual 495	org/apache/xerces/impl/xs/SchemaGrammar:getComponents	(S)Lorg/apache/xerces/xs/XSNamedMap;
    //   5: astore_3
    //   6: aload_3
    //   7: invokeinterface 496 1 0
    //   12: istore 4
    //   14: iconst_0
    //   15: istore 7
    //   17: goto +72 -> 89
    //   20: aload_3
    //   21: iload 7
    //   23: invokeinterface 497 2 0
    //   28: checkcast 317	org/apache/xerces/impl/xs/XSAttributeGroupDecl
    //   31: astore 5
    //   33: aload_2
    //   34: aload 5
    //   36: invokevirtual 304	org/apache/xerces/impl/xs/XSAttributeGroupDecl:getName	()Ljava/lang/String;
    //   39: invokevirtual 354	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalAttributeGroupDecl	(Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSAttributeGroupDecl;
    //   42: astore 6
    //   44: aload 6
    //   46: ifnonnull +12 -> 58
    //   49: aload_2
    //   50: aload 5
    //   52: invokevirtual 507	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalAttributeGroupDecl	(Lorg/apache/xerces/impl/xs/XSAttributeGroupDecl;)V
    //   55: goto +31 -> 86
    //   58: aload 6
    //   60: aload 5
    //   62: if_acmpeq +24 -> 86
    //   65: aload_0
    //   66: getfield 47	org/apache/xerces/impl/xs/traversers/XSDHandler:fTolerateDuplicates	Z
    //   69: ifne +17 -> 86
    //   72: aload_0
    //   73: aload 5
    //   75: invokevirtual 303	org/apache/xerces/impl/xs/XSAttributeGroupDecl:getNamespace	()Ljava/lang/String;
    //   78: aload 5
    //   80: invokevirtual 304	org/apache/xerces/impl/xs/XSAttributeGroupDecl:getName	()Ljava/lang/String;
    //   83: invokespecial 505	org/apache/xerces/impl/xs/traversers/XSDHandler:reportSharingError	(Ljava/lang/String;Ljava/lang/String;)V
    //   86: iinc 7 1
    //   89: iload 7
    //   91: iload 4
    //   93: if_icmplt -73 -> 20
    //   96: aload_1
    //   97: iconst_5
    //   98: invokevirtual 499	org/apache/xerces/impl/xs/SchemaGrammar:getComponentsExt	(S)Lorg/apache/xerces/xs/datatypes/ObjectList;
    //   101: astore 8
    //   103: aload 8
    //   105: invokeinterface 500 1 0
    //   110: istore 4
    //   112: iconst_0
    //   113: istore 9
    //   115: goto +104 -> 219
    //   118: aload 8
    //   120: iload 9
    //   122: invokeinterface 501 2 0
    //   127: checkcast 8	java/lang/String
    //   130: astore 10
    //   132: aload 10
    //   134: bipush 44
    //   136: invokevirtual 502	java/lang/String:indexOf	(I)I
    //   139: istore 11
    //   141: aload 10
    //   143: iconst_0
    //   144: iload 11
    //   146: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   149: astore 12
    //   151: aload 10
    //   153: iload 11
    //   155: iconst_1
    //   156: iadd
    //   157: aload 10
    //   159: invokevirtual 115	java/lang/String:length	()I
    //   162: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   165: astore 13
    //   167: aload 8
    //   169: iload 9
    //   171: iconst_1
    //   172: iadd
    //   173: invokeinterface 501 2 0
    //   178: checkcast 317	org/apache/xerces/impl/xs/XSAttributeGroupDecl
    //   181: astore 5
    //   183: aload_2
    //   184: aload 13
    //   186: aload 12
    //   188: invokevirtual 361	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalAttributeGroupDecl	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSAttributeGroupDecl;
    //   191: astore 6
    //   193: aload 6
    //   195: ifnonnull +14 -> 209
    //   198: aload_2
    //   199: aload 5
    //   201: aload 12
    //   203: invokevirtual 508	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalAttributeGroupDecl	(Lorg/apache/xerces/impl/xs/XSAttributeGroupDecl;Ljava/lang/String;)V
    //   206: goto +10 -> 216
    //   209: aload 6
    //   211: aload 5
    //   213: if_acmpeq +3 -> 216
    //   216: iinc 9 2
    //   219: iload 9
    //   221: iload 4
    //   223: if_icmplt -105 -> 118
    //   226: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	227	0	this	XSDHandler
    //   0	227	1	paramSchemaGrammar1	SchemaGrammar
    //   0	227	2	paramSchemaGrammar2	SchemaGrammar
    //   5	16	3	localXSNamedMap	XSNamedMap
    //   12	212	4	i	int
    //   31	181	5	localXSAttributeGroupDecl1	XSAttributeGroupDecl
    //   42	168	6	localXSAttributeGroupDecl2	XSAttributeGroupDecl
    //   15	79	7	j	int
    //   101	67	8	localObjectList	ObjectList
    //   113	111	9	k	int
    //   130	28	10	str1	String
    //   139	18	11	m	int
    //   149	53	12	str2	String
    //   165	20	13	str3	String
  }
  
  private void addGlobalNotationDecls(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    // Byte code:
    //   0: aload_1
    //   1: bipush 11
    //   3: invokevirtual 495	org/apache/xerces/impl/xs/SchemaGrammar:getComponents	(S)Lorg/apache/xerces/xs/XSNamedMap;
    //   6: astore_3
    //   7: aload_3
    //   8: invokeinterface 496 1 0
    //   13: istore 4
    //   15: iconst_0
    //   16: istore 7
    //   18: goto +72 -> 90
    //   21: aload_3
    //   22: iload 7
    //   24: invokeinterface 497 2 0
    //   29: checkcast 319	org/apache/xerces/impl/xs/XSNotationDecl
    //   32: astore 5
    //   34: aload_2
    //   35: aload 5
    //   37: invokevirtual 310	org/apache/xerces/impl/xs/XSNotationDecl:getName	()Ljava/lang/String;
    //   40: invokevirtual 358	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalNotationDecl	(Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSNotationDecl;
    //   43: astore 6
    //   45: aload 6
    //   47: ifnonnull +12 -> 59
    //   50: aload_2
    //   51: aload 5
    //   53: invokevirtual 509	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalNotationDecl	(Lorg/apache/xerces/impl/xs/XSNotationDecl;)V
    //   56: goto +31 -> 87
    //   59: aload 6
    //   61: aload 5
    //   63: if_acmpeq +24 -> 87
    //   66: aload_0
    //   67: getfield 47	org/apache/xerces/impl/xs/traversers/XSDHandler:fTolerateDuplicates	Z
    //   70: ifne +17 -> 87
    //   73: aload_0
    //   74: aload 5
    //   76: invokevirtual 309	org/apache/xerces/impl/xs/XSNotationDecl:getNamespace	()Ljava/lang/String;
    //   79: aload 5
    //   81: invokevirtual 310	org/apache/xerces/impl/xs/XSNotationDecl:getName	()Ljava/lang/String;
    //   84: invokespecial 505	org/apache/xerces/impl/xs/traversers/XSDHandler:reportSharingError	(Ljava/lang/String;Ljava/lang/String;)V
    //   87: iinc 7 1
    //   90: iload 7
    //   92: iload 4
    //   94: if_icmplt -73 -> 21
    //   97: aload_1
    //   98: bipush 11
    //   100: invokevirtual 499	org/apache/xerces/impl/xs/SchemaGrammar:getComponentsExt	(S)Lorg/apache/xerces/xs/datatypes/ObjectList;
    //   103: astore 8
    //   105: aload 8
    //   107: invokeinterface 500 1 0
    //   112: istore 4
    //   114: iconst_0
    //   115: istore 9
    //   117: goto +104 -> 221
    //   120: aload 8
    //   122: iload 9
    //   124: invokeinterface 501 2 0
    //   129: checkcast 8	java/lang/String
    //   132: astore 10
    //   134: aload 10
    //   136: bipush 44
    //   138: invokevirtual 502	java/lang/String:indexOf	(I)I
    //   141: istore 11
    //   143: aload 10
    //   145: iconst_0
    //   146: iload 11
    //   148: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   151: astore 12
    //   153: aload 10
    //   155: iload 11
    //   157: iconst_1
    //   158: iadd
    //   159: aload 10
    //   161: invokevirtual 115	java/lang/String:length	()I
    //   164: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   167: astore 13
    //   169: aload 8
    //   171: iload 9
    //   173: iconst_1
    //   174: iadd
    //   175: invokeinterface 501 2 0
    //   180: checkcast 319	org/apache/xerces/impl/xs/XSNotationDecl
    //   183: astore 5
    //   185: aload_2
    //   186: aload 13
    //   188: aload 12
    //   190: invokevirtual 365	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalNotationDecl	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSNotationDecl;
    //   193: astore 6
    //   195: aload 6
    //   197: ifnonnull +14 -> 211
    //   200: aload_2
    //   201: aload 5
    //   203: aload 12
    //   205: invokevirtual 510	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalNotationDecl	(Lorg/apache/xerces/impl/xs/XSNotationDecl;Ljava/lang/String;)V
    //   208: goto +10 -> 218
    //   211: aload 6
    //   213: aload 5
    //   215: if_acmpeq +3 -> 218
    //   218: iinc 9 2
    //   221: iload 9
    //   223: iload 4
    //   225: if_icmplt -105 -> 120
    //   228: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	229	0	this	XSDHandler
    //   0	229	1	paramSchemaGrammar1	SchemaGrammar
    //   0	229	2	paramSchemaGrammar2	SchemaGrammar
    //   6	16	3	localXSNamedMap	XSNamedMap
    //   13	213	4	i	int
    //   32	182	5	localXSNotationDecl1	XSNotationDecl
    //   43	169	6	localXSNotationDecl2	XSNotationDecl
    //   16	79	7	j	int
    //   103	67	8	localObjectList	ObjectList
    //   115	111	9	k	int
    //   132	28	10	str1	String
    //   141	18	11	m	int
    //   151	53	12	str2	String
    //   167	20	13	str3	String
  }
  
  private void addGlobalGroupDecls(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    // Byte code:
    //   0: aload_1
    //   1: bipush 6
    //   3: invokevirtual 495	org/apache/xerces/impl/xs/SchemaGrammar:getComponents	(S)Lorg/apache/xerces/xs/XSNamedMap;
    //   6: astore_3
    //   7: aload_3
    //   8: invokeinterface 496 1 0
    //   13: istore 4
    //   15: iconst_0
    //   16: istore 7
    //   18: goto +72 -> 90
    //   21: aload_3
    //   22: iload 7
    //   24: invokeinterface 497 2 0
    //   29: checkcast 318	org/apache/xerces/impl/xs/XSGroupDecl
    //   32: astore 5
    //   34: aload_2
    //   35: aload 5
    //   37: invokevirtual 308	org/apache/xerces/impl/xs/XSGroupDecl:getName	()Ljava/lang/String;
    //   40: invokevirtual 356	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalGroupDecl	(Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSGroupDecl;
    //   43: astore 6
    //   45: aload 6
    //   47: ifnonnull +12 -> 59
    //   50: aload_2
    //   51: aload 5
    //   53: invokevirtual 511	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalGroupDecl	(Lorg/apache/xerces/impl/xs/XSGroupDecl;)V
    //   56: goto +31 -> 87
    //   59: aload 5
    //   61: aload 6
    //   63: if_acmpeq +24 -> 87
    //   66: aload_0
    //   67: getfield 47	org/apache/xerces/impl/xs/traversers/XSDHandler:fTolerateDuplicates	Z
    //   70: ifne +17 -> 87
    //   73: aload_0
    //   74: aload 5
    //   76: invokevirtual 307	org/apache/xerces/impl/xs/XSGroupDecl:getNamespace	()Ljava/lang/String;
    //   79: aload 5
    //   81: invokevirtual 308	org/apache/xerces/impl/xs/XSGroupDecl:getName	()Ljava/lang/String;
    //   84: invokespecial 505	org/apache/xerces/impl/xs/traversers/XSDHandler:reportSharingError	(Ljava/lang/String;Ljava/lang/String;)V
    //   87: iinc 7 1
    //   90: iload 7
    //   92: iload 4
    //   94: if_icmplt -73 -> 21
    //   97: aload_1
    //   98: bipush 6
    //   100: invokevirtual 499	org/apache/xerces/impl/xs/SchemaGrammar:getComponentsExt	(S)Lorg/apache/xerces/xs/datatypes/ObjectList;
    //   103: astore 8
    //   105: aload 8
    //   107: invokeinterface 500 1 0
    //   112: istore 4
    //   114: iconst_0
    //   115: istore 9
    //   117: goto +104 -> 221
    //   120: aload 8
    //   122: iload 9
    //   124: invokeinterface 501 2 0
    //   129: checkcast 8	java/lang/String
    //   132: astore 10
    //   134: aload 10
    //   136: bipush 44
    //   138: invokevirtual 502	java/lang/String:indexOf	(I)I
    //   141: istore 11
    //   143: aload 10
    //   145: iconst_0
    //   146: iload 11
    //   148: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   151: astore 12
    //   153: aload 10
    //   155: iload 11
    //   157: iconst_1
    //   158: iadd
    //   159: aload 10
    //   161: invokevirtual 115	java/lang/String:length	()I
    //   164: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   167: astore 13
    //   169: aload 8
    //   171: iload 9
    //   173: iconst_1
    //   174: iadd
    //   175: invokeinterface 501 2 0
    //   180: checkcast 318	org/apache/xerces/impl/xs/XSGroupDecl
    //   183: astore 5
    //   185: aload_2
    //   186: aload 13
    //   188: aload 12
    //   190: invokevirtual 363	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalGroupDecl	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/xerces/impl/xs/XSGroupDecl;
    //   193: astore 6
    //   195: aload 6
    //   197: ifnonnull +14 -> 211
    //   200: aload_2
    //   201: aload 5
    //   203: aload 12
    //   205: invokevirtual 512	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalGroupDecl	(Lorg/apache/xerces/impl/xs/XSGroupDecl;Ljava/lang/String;)V
    //   208: goto +10 -> 218
    //   211: aload 6
    //   213: aload 5
    //   215: if_acmpeq +3 -> 218
    //   218: iinc 9 2
    //   221: iload 9
    //   223: iload 4
    //   225: if_icmplt -105 -> 120
    //   228: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	229	0	this	XSDHandler
    //   0	229	1	paramSchemaGrammar1	SchemaGrammar
    //   0	229	2	paramSchemaGrammar2	SchemaGrammar
    //   6	16	3	localXSNamedMap	XSNamedMap
    //   13	213	4	i	int
    //   32	182	5	localXSGroupDecl1	XSGroupDecl
    //   43	169	6	localXSGroupDecl2	XSGroupDecl
    //   16	79	7	j	int
    //   103	67	8	localObjectList	ObjectList
    //   115	111	9	k	int
    //   132	28	10	str1	String
    //   141	18	11	m	int
    //   151	53	12	str2	String
    //   167	20	13	str3	String
  }
  
  private void addGlobalTypeDecls(SchemaGrammar paramSchemaGrammar1, SchemaGrammar paramSchemaGrammar2)
  {
    // Byte code:
    //   0: aload_1
    //   1: iconst_3
    //   2: invokevirtual 495	org/apache/xerces/impl/xs/SchemaGrammar:getComponents	(S)Lorg/apache/xerces/xs/XSNamedMap;
    //   5: astore_3
    //   6: aload_3
    //   7: invokeinterface 496 1 0
    //   12: istore 4
    //   14: iconst_0
    //   15: istore 7
    //   17: goto +78 -> 95
    //   20: aload_3
    //   21: iload 7
    //   23: invokeinterface 497 2 0
    //   28: checkcast 320	org/apache/xerces/xs/XSTypeDefinition
    //   31: astore 5
    //   33: aload_2
    //   34: aload 5
    //   36: invokeinterface 513 1 0
    //   41: invokevirtual 359	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalTypeDecl	(Ljava/lang/String;)Lorg/apache/xerces/xs/XSTypeDefinition;
    //   44: astore 6
    //   46: aload 6
    //   48: ifnonnull +12 -> 60
    //   51: aload_2
    //   52: aload 5
    //   54: invokevirtual 514	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalTypeDecl	(Lorg/apache/xerces/xs/XSTypeDefinition;)V
    //   57: goto +35 -> 92
    //   60: aload 6
    //   62: aload 5
    //   64: if_acmpeq +28 -> 92
    //   67: aload_0
    //   68: getfield 47	org/apache/xerces/impl/xs/traversers/XSDHandler:fTolerateDuplicates	Z
    //   71: ifne +21 -> 92
    //   74: aload_0
    //   75: aload 5
    //   77: invokeinterface 515 1 0
    //   82: aload 5
    //   84: invokeinterface 516 1 0
    //   89: invokespecial 505	org/apache/xerces/impl/xs/traversers/XSDHandler:reportSharingError	(Ljava/lang/String;Ljava/lang/String;)V
    //   92: iinc 7 1
    //   95: iload 7
    //   97: iload 4
    //   99: if_icmplt -79 -> 20
    //   102: aload_1
    //   103: iconst_3
    //   104: invokevirtual 499	org/apache/xerces/impl/xs/SchemaGrammar:getComponentsExt	(S)Lorg/apache/xerces/xs/datatypes/ObjectList;
    //   107: astore 8
    //   109: aload 8
    //   111: invokeinterface 500 1 0
    //   116: istore 4
    //   118: iconst_0
    //   119: istore 9
    //   121: goto +104 -> 225
    //   124: aload 8
    //   126: iload 9
    //   128: invokeinterface 501 2 0
    //   133: checkcast 8	java/lang/String
    //   136: astore 10
    //   138: aload 10
    //   140: bipush 44
    //   142: invokevirtual 502	java/lang/String:indexOf	(I)I
    //   145: istore 11
    //   147: aload 10
    //   149: iconst_0
    //   150: iload 11
    //   152: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   155: astore 12
    //   157: aload 10
    //   159: iload 11
    //   161: iconst_1
    //   162: iadd
    //   163: aload 10
    //   165: invokevirtual 115	java/lang/String:length	()I
    //   168: invokevirtual 371	java/lang/String:substring	(II)Ljava/lang/String;
    //   171: astore 13
    //   173: aload 8
    //   175: iload 9
    //   177: iconst_1
    //   178: iadd
    //   179: invokeinterface 501 2 0
    //   184: checkcast 320	org/apache/xerces/xs/XSTypeDefinition
    //   187: astore 5
    //   189: aload_2
    //   190: aload 13
    //   192: aload 12
    //   194: invokevirtual 366	org/apache/xerces/impl/xs/SchemaGrammar:getGlobalTypeDecl	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/xerces/xs/XSTypeDefinition;
    //   197: astore 6
    //   199: aload 6
    //   201: ifnonnull +14 -> 215
    //   204: aload_2
    //   205: aload 5
    //   207: aload 12
    //   209: invokevirtual 517	org/apache/xerces/impl/xs/SchemaGrammar:addGlobalTypeDecl	(Lorg/apache/xerces/xs/XSTypeDefinition;Ljava/lang/String;)V
    //   212: goto +10 -> 222
    //   215: aload 6
    //   217: aload 5
    //   219: if_acmpeq +3 -> 222
    //   222: iinc 9 2
    //   225: iload 9
    //   227: iload 4
    //   229: if_icmplt -105 -> 124
    //   232: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	233	0	this	XSDHandler
    //   0	233	1	paramSchemaGrammar1	SchemaGrammar
    //   0	233	2	paramSchemaGrammar2	SchemaGrammar
    //   5	16	3	localXSNamedMap	XSNamedMap
    //   12	218	4	i	int
    //   31	187	5	localXSTypeDefinition1	XSTypeDefinition
    //   44	172	6	localXSTypeDefinition2	XSTypeDefinition
    //   15	85	7	j	int
    //   107	67	8	localObjectList	ObjectList
    //   119	111	9	k	int
    //   136	28	10	str1	String
    //   145	18	11	m	int
    //   155	53	12	str2	String
    //   171	20	13	str3	String
  }
  
  private Vector expandComponents(XSObject[] paramArrayOfXSObject, Hashtable paramHashtable)
  {
    Vector localVector = new Vector();
    for (int i = 0; i < paramArrayOfXSObject.length; i++) {
      if (!localVector.contains(paramArrayOfXSObject[i])) {
        localVector.add(paramArrayOfXSObject[i]);
      }
    }
    for (int j = 0; j < localVector.size(); j++)
    {
      XSObject localXSObject = (XSObject)localVector.elementAt(j);
      expandRelatedComponents(localXSObject, localVector, paramHashtable);
    }
    return localVector;
  }
  
  private void expandRelatedComponents(XSObject paramXSObject, Vector paramVector, Hashtable paramHashtable)
  {
    int i = paramXSObject.getType();
    switch (i)
    {
    case 3: 
      expandRelatedTypeComponents((XSTypeDefinition)paramXSObject, paramVector, paramXSObject.getNamespace(), paramHashtable);
      break;
    case 1: 
      expandRelatedAttributeComponents((XSAttributeDeclaration)paramXSObject, paramVector, paramXSObject.getNamespace(), paramHashtable);
      break;
    case 5: 
      expandRelatedAttributeGroupComponents((XSAttributeGroupDefinition)paramXSObject, paramVector, paramXSObject.getNamespace(), paramHashtable);
    case 2: 
      expandRelatedElementComponents((XSElementDeclaration)paramXSObject, paramVector, paramXSObject.getNamespace(), paramHashtable);
      break;
    case 6: 
      expandRelatedModelGroupDefinitionComponents((XSModelGroupDefinition)paramXSObject, paramVector, paramXSObject.getNamespace(), paramHashtable);
    }
  }
  
  private void expandRelatedAttributeComponents(XSAttributeDeclaration paramXSAttributeDeclaration, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    addRelatedType(paramXSAttributeDeclaration.getTypeDefinition(), paramVector, paramString, paramHashtable);
  }
  
  private void expandRelatedElementComponents(XSElementDeclaration paramXSElementDeclaration, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    addRelatedType(paramXSElementDeclaration.getTypeDefinition(), paramVector, paramString, paramHashtable);
    XSElementDeclaration localXSElementDeclaration = paramXSElementDeclaration.getSubstitutionGroupAffiliation();
    if (localXSElementDeclaration != null) {
      addRelatedElement(localXSElementDeclaration, paramVector, paramString, paramHashtable);
    }
  }
  
  private void expandRelatedTypeComponents(XSTypeDefinition paramXSTypeDefinition, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    if ((paramXSTypeDefinition instanceof XSComplexTypeDecl)) {
      expandRelatedComplexTypeComponents((XSComplexTypeDecl)paramXSTypeDefinition, paramVector, paramString, paramHashtable);
    } else if ((paramXSTypeDefinition instanceof XSSimpleTypeDecl)) {
      expandRelatedSimpleTypeComponents((XSSimpleTypeDefinition)paramXSTypeDefinition, paramVector, paramString, paramHashtable);
    }
  }
  
  private void expandRelatedModelGroupDefinitionComponents(XSModelGroupDefinition paramXSModelGroupDefinition, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    expandRelatedModelGroupComponents(paramXSModelGroupDefinition.getModelGroup(), paramVector, paramString, paramHashtable);
  }
  
  private void expandRelatedAttributeGroupComponents(XSAttributeGroupDefinition paramXSAttributeGroupDefinition, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    expandRelatedAttributeUsesComponents(paramXSAttributeGroupDefinition.getAttributeUses(), paramVector, paramString, paramHashtable);
  }
  
  private void expandRelatedComplexTypeComponents(XSComplexTypeDecl paramXSComplexTypeDecl, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    addRelatedType(paramXSComplexTypeDecl.getBaseType(), paramVector, paramString, paramHashtable);
    expandRelatedAttributeUsesComponents(paramXSComplexTypeDecl.getAttributeUses(), paramVector, paramString, paramHashtable);
    XSParticle localXSParticle = paramXSComplexTypeDecl.getParticle();
    if (localXSParticle != null) {
      expandRelatedParticleComponents(localXSParticle, paramVector, paramString, paramHashtable);
    }
  }
  
  private void expandRelatedSimpleTypeComponents(XSSimpleTypeDefinition paramXSSimpleTypeDefinition, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    XSTypeDefinition localXSTypeDefinition = paramXSSimpleTypeDefinition.getBaseType();
    if (localXSTypeDefinition != null) {
      addRelatedType(localXSTypeDefinition, paramVector, paramString, paramHashtable);
    }
    XSSimpleTypeDefinition localXSSimpleTypeDefinition1 = paramXSSimpleTypeDefinition.getItemType();
    if (localXSSimpleTypeDefinition1 != null) {
      addRelatedType(localXSSimpleTypeDefinition1, paramVector, paramString, paramHashtable);
    }
    XSSimpleTypeDefinition localXSSimpleTypeDefinition2 = paramXSSimpleTypeDefinition.getPrimitiveType();
    if (localXSSimpleTypeDefinition2 != null) {
      addRelatedType(localXSSimpleTypeDefinition2, paramVector, paramString, paramHashtable);
    }
    XSObjectList localXSObjectList = paramXSSimpleTypeDefinition.getMemberTypes();
    if (localXSObjectList.size() > 0) {
      for (int i = 0; i < localXSObjectList.size(); i++) {
        addRelatedType((XSTypeDefinition)localXSObjectList.item(i), paramVector, paramString, paramHashtable);
      }
    }
  }
  
  private void expandRelatedAttributeUsesComponents(XSObjectList paramXSObjectList, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    int i = paramXSObjectList == null ? 0 : paramXSObjectList.size();
    for (int j = 0; j < i; j++) {
      expandRelatedAttributeUseComponents((XSAttributeUse)paramXSObjectList.item(j), paramVector, paramString, paramHashtable);
    }
  }
  
  private void expandRelatedAttributeUseComponents(XSAttributeUse paramXSAttributeUse, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    addRelatedAttribute(paramXSAttributeUse.getAttrDeclaration(), paramVector, paramString, paramHashtable);
  }
  
  private void expandRelatedParticleComponents(XSParticle paramXSParticle, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    XSTerm localXSTerm = paramXSParticle.getTerm();
    switch (localXSTerm.getType())
    {
    case 2: 
      addRelatedElement((XSElementDeclaration)localXSTerm, paramVector, paramString, paramHashtable);
      break;
    case 7: 
      expandRelatedModelGroupComponents((XSModelGroup)localXSTerm, paramVector, paramString, paramHashtable);
      break;
    }
  }
  
  private void expandRelatedModelGroupComponents(XSModelGroup paramXSModelGroup, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    XSObjectList localXSObjectList = paramXSModelGroup.getParticles();
    int i = localXSObjectList == null ? 0 : localXSObjectList.getLength();
    for (int j = 0; j < i; j++) {
      expandRelatedParticleComponents((XSParticle)localXSObjectList.item(j), paramVector, paramString, paramHashtable);
    }
  }
  
  private void addRelatedType(XSTypeDefinition paramXSTypeDefinition, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    if (!paramXSTypeDefinition.getAnonymous())
    {
      if ((!paramXSTypeDefinition.getNamespace().equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && (!paramVector.contains(paramXSTypeDefinition)))
      {
        Vector localVector = findDependentNamespaces(paramString, paramHashtable);
        addNamespaceDependency(paramString, paramXSTypeDefinition.getNamespace(), localVector);
        paramVector.add(paramXSTypeDefinition);
      }
    }
    else {
      expandRelatedTypeComponents(paramXSTypeDefinition, paramVector, paramString, paramHashtable);
    }
  }
  
  private void addRelatedElement(XSElementDeclaration paramXSElementDeclaration, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    if (paramXSElementDeclaration.getScope() == 1)
    {
      if (!paramVector.contains(paramXSElementDeclaration))
      {
        Vector localVector = findDependentNamespaces(paramString, paramHashtable);
        addNamespaceDependency(paramString, paramXSElementDeclaration.getNamespace(), localVector);
        paramVector.add(paramXSElementDeclaration);
      }
    }
    else {
      expandRelatedElementComponents(paramXSElementDeclaration, paramVector, paramString, paramHashtable);
    }
  }
  
  private void addRelatedAttribute(XSAttributeDeclaration paramXSAttributeDeclaration, Vector paramVector, String paramString, Hashtable paramHashtable)
  {
    if (paramXSAttributeDeclaration.getScope() == 1)
    {
      if (!paramVector.contains(paramXSAttributeDeclaration))
      {
        Vector localVector = findDependentNamespaces(paramString, paramHashtable);
        addNamespaceDependency(paramString, paramXSAttributeDeclaration.getNamespace(), localVector);
        paramVector.add(paramXSAttributeDeclaration);
      }
    }
    else {
      expandRelatedAttributeComponents(paramXSAttributeDeclaration, paramVector, paramString, paramHashtable);
    }
  }
  
  private void addGlobalComponents(Vector paramVector, Hashtable paramHashtable)
  {
    XSDDescription localXSDDescription = new XSDDescription();
    int i = paramVector.size();
    for (int j = 0; j < i; j++) {
      addGlobalComponent((XSObject)paramVector.elementAt(j), localXSDDescription);
    }
    updateImportDependencies(paramHashtable);
  }
  
  private void addGlobalComponent(XSObject paramXSObject, XSDDescription paramXSDDescription)
  {
    String str1 = paramXSObject.getNamespace();
    paramXSDDescription.setNamespace(str1);
    SchemaGrammar localSchemaGrammar = getSchemaGrammar(paramXSDDescription);
    int i = paramXSObject.getType();
    String str2 = paramXSObject.getName();
    switch (i)
    {
    case 3: 
      if (!((XSTypeDefinition)paramXSObject).getAnonymous())
      {
        if (localSchemaGrammar.getGlobalTypeDecl(str2) == null) {
          localSchemaGrammar.addGlobalTypeDecl((XSTypeDefinition)paramXSObject);
        }
        if (localSchemaGrammar.getGlobalTypeDecl(str2, "") == null) {
          localSchemaGrammar.addGlobalTypeDecl((XSTypeDefinition)paramXSObject, "");
        }
      }
      break;
    case 1: 
      if (((XSAttributeDecl)paramXSObject).getScope() == 1)
      {
        if (localSchemaGrammar.getGlobalAttributeDecl(str2) == null) {
          localSchemaGrammar.addGlobalAttributeDecl((XSAttributeDecl)paramXSObject);
        }
        if (localSchemaGrammar.getGlobalAttributeDecl(str2, "") == null) {
          localSchemaGrammar.addGlobalAttributeDecl((XSAttributeDecl)paramXSObject, "");
        }
      }
      break;
    case 5: 
      if (localSchemaGrammar.getGlobalAttributeDecl(str2) == null) {
        localSchemaGrammar.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)paramXSObject);
      }
      if (localSchemaGrammar.getGlobalAttributeDecl(str2, "") == null) {
        localSchemaGrammar.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)paramXSObject, "");
      }
      break;
    case 2: 
      if (((XSElementDecl)paramXSObject).getScope() == 1)
      {
        localSchemaGrammar.addGlobalElementDeclAll((XSElementDecl)paramXSObject);
        if (localSchemaGrammar.getGlobalElementDecl(str2) == null) {
          localSchemaGrammar.addGlobalElementDecl((XSElementDecl)paramXSObject);
        }
        if (localSchemaGrammar.getGlobalElementDecl(str2, "") == null) {
          localSchemaGrammar.addGlobalElementDecl((XSElementDecl)paramXSObject, "");
        }
      }
      break;
    case 6: 
      if (localSchemaGrammar.getGlobalGroupDecl(str2) == null) {
        localSchemaGrammar.addGlobalGroupDecl((XSGroupDecl)paramXSObject);
      }
      if (localSchemaGrammar.getGlobalGroupDecl(str2, "") == null) {
        localSchemaGrammar.addGlobalGroupDecl((XSGroupDecl)paramXSObject, "");
      }
      break;
    case 11: 
      if (localSchemaGrammar.getGlobalNotationDecl(str2) == null) {
        localSchemaGrammar.addGlobalNotationDecl((XSNotationDecl)paramXSObject);
      }
      if (localSchemaGrammar.getGlobalNotationDecl(str2, "") == null) {
        localSchemaGrammar.addGlobalNotationDecl((XSNotationDecl)paramXSObject, "");
      }
      break;
    }
  }
  
  private void updateImportDependencies(Hashtable paramHashtable)
  {
    Enumeration localEnumeration = paramHashtable.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      Vector localVector = (Vector)paramHashtable.get(null2EmptyString(str));
      if (localVector.size() > 0) {
        expandImportList(str, localVector);
      }
    }
  }
  
  private void expandImportList(String paramString, Vector paramVector)
  {
    SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(paramString);
    if (localSchemaGrammar != null)
    {
      Vector localVector = localSchemaGrammar.getImportedGrammars();
      if (localVector == null)
      {
        localVector = new Vector();
        addImportList(localSchemaGrammar, localVector, paramVector);
        localSchemaGrammar.setImportedGrammars(localVector);
      }
      else
      {
        updateImportList(localSchemaGrammar, localVector, paramVector);
      }
    }
  }
  
  private void addImportList(SchemaGrammar paramSchemaGrammar, Vector paramVector1, Vector paramVector2)
  {
    int i = paramVector2.size();
    for (int j = 0; j < i; j++)
    {
      SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar((String)paramVector2.elementAt(j));
      if (localSchemaGrammar != null) {
        paramVector1.add(localSchemaGrammar);
      }
    }
  }
  
  private void updateImportList(SchemaGrammar paramSchemaGrammar, Vector paramVector1, Vector paramVector2)
  {
    int i = paramVector2.size();
    for (int j = 0; j < i; j++)
    {
      SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar((String)paramVector2.elementAt(j));
      if ((localSchemaGrammar != null) && (!containedImportedGrammar(paramVector1, localSchemaGrammar))) {
        paramVector1.add(localSchemaGrammar);
      }
    }
  }
  
  private boolean containedImportedGrammar(Vector paramVector, SchemaGrammar paramSchemaGrammar)
  {
    int i = paramVector.size();
    for (int j = 0; j < i; j++)
    {
      SchemaGrammar localSchemaGrammar = (SchemaGrammar)paramVector.elementAt(j);
      if (null2EmptyString(localSchemaGrammar.getTargetNamespace()).equals(null2EmptyString(paramSchemaGrammar.getTargetNamespace()))) {
        return true;
      }
    }
    return false;
  }
  
  private SchemaGrammar getSchemaGrammar(XSDDescription paramXSDDescription)
  {
    SchemaGrammar localSchemaGrammar = findGrammar(paramXSDDescription, fNamespaceGrowth);
    if (localSchemaGrammar == null)
    {
      localSchemaGrammar = new SchemaGrammar(paramXSDDescription.getNamespace(), paramXSDDescription.makeClone(), fSymbolTable);
      fGrammarBucket.putGrammar(localSchemaGrammar);
    }
    else if (localSchemaGrammar.isImmutable())
    {
      localSchemaGrammar = createGrammarFrom(localSchemaGrammar);
    }
    return localSchemaGrammar;
  }
  
  private Vector findDependentNamespaces(String paramString, Hashtable paramHashtable)
  {
    String str = null2EmptyString(paramString);
    Vector localVector = (Vector)paramHashtable.get(str);
    if (localVector == null)
    {
      localVector = new Vector();
      paramHashtable.put(str, localVector);
    }
    return localVector;
  }
  
  private void addNamespaceDependency(String paramString1, String paramString2, Vector paramVector)
  {
    String str1 = null2EmptyString(paramString1);
    String str2 = null2EmptyString(paramString2);
    if ((!str1.equals(str2)) && (!paramVector.contains(str2))) {
      paramVector.add(str2);
    }
  }
  
  private void reportSharingError(String paramString1, String paramString2)
  {
    String str = paramString1 + "," + paramString2;
    reportSchemaError("sch-props-correct.2", new Object[] { str }, null);
  }
  
  private void createTraversers()
  {
    fAttributeChecker = new XSAttributeChecker(this);
    fAttributeGroupTraverser = new XSDAttributeGroupTraverser(this, fAttributeChecker);
    fAttributeTraverser = new XSDAttributeTraverser(this, fAttributeChecker);
    fComplexTypeTraverser = new XSDComplexTypeTraverser(this, fAttributeChecker);
    fElementTraverser = new XSDElementTraverser(this, fAttributeChecker);
    fGroupTraverser = new XSDGroupTraverser(this, fAttributeChecker);
    fKeyrefTraverser = new XSDKeyrefTraverser(this, fAttributeChecker);
    fNotationTraverser = new XSDNotationTraverser(this, fAttributeChecker);
    fSimpleTypeTraverser = new XSDSimpleTypeTraverser(this, fAttributeChecker);
    fUniqueOrKeyTraverser = new XSDUniqueOrKeyTraverser(this, fAttributeChecker);
    fWildCardTraverser = new XSDWildcardTraverser(this, fAttributeChecker);
  }
  
  void prepareForParse()
  {
    fTraversed.clear();
    fDoc2SystemId.clear();
    fHiddenNodes.clear();
    fLastSchemaWasDuplicate = false;
  }
  
  void prepareForTraverse()
  {
    fUnparsedAttributeRegistry.clear();
    fUnparsedAttributeGroupRegistry.clear();
    fUnparsedElementRegistry.clear();
    fUnparsedGroupRegistry.clear();
    fUnparsedIdentityConstraintRegistry.clear();
    fUnparsedNotationRegistry.clear();
    fUnparsedTypeRegistry.clear();
    fUnparsedAttributeRegistrySub.clear();
    fUnparsedAttributeGroupRegistrySub.clear();
    fUnparsedElementRegistrySub.clear();
    fUnparsedGroupRegistrySub.clear();
    fUnparsedIdentityConstraintRegistrySub.clear();
    fUnparsedNotationRegistrySub.clear();
    fUnparsedTypeRegistrySub.clear();
    for (int i = 1; i <= 7; i++) {
      fUnparsedRegistriesExt[i].clear();
    }
    fXSDocumentInfoRegistry.clear();
    fDependencyMap.clear();
    fDoc2XSDocumentMap.clear();
    fRedefine2XSDMap.clear();
    fRedefine2NSSupport.clear();
    fAllTNSs.removeAllElements();
    fImportMap.clear();
    fRoot = null;
    for (int j = 0; j < fLocalElemStackPos; j++)
    {
      fParticle[j] = null;
      fLocalElementDecl[j] = null;
      fLocalElementDecl_schema[j] = null;
      fLocalElemNamespaceContext[j] = null;
    }
    fLocalElemStackPos = 0;
    for (int k = 0; k < fKeyrefStackPos; k++)
    {
      fKeyrefs[k] = null;
      fKeyrefElems[k] = null;
      fKeyrefNamespaceContext[k] = null;
      fKeyrefsMapXSDocumentInfo[k] = null;
    }
    fKeyrefStackPos = 0;
    if (fAttributeChecker == null) {
      createTraversers();
    }
    Locale localLocale = fErrorReporter.getLocale();
    fAttributeChecker.reset(fSymbolTable);
    fAttributeGroupTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fAttributeTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fComplexTypeTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fElementTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fGroupTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fKeyrefTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fNotationTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fSimpleTypeTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fUniqueOrKeyTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fWildCardTraverser.reset(fSymbolTable, fValidateAnnotations, localLocale);
    fRedefinedRestrictedAttributeGroupRegistry.clear();
    fRedefinedRestrictedGroupRegistry.clear();
    fGlobalAttrDecls.clear();
    fGlobalAttrGrpDecls.clear();
    fGlobalElemDecls.clear();
    fGlobalGroupDecls.clear();
    fGlobalNotationDecls.clear();
    fGlobalIDConstraintDecls.clear();
    fGlobalTypeDecls.clear();
  }
  
  public void setDeclPool(XSDeclarationPool paramXSDeclarationPool)
  {
    fDeclPool = paramXSDeclarationPool;
  }
  
  public void setDVFactory(SchemaDVFactory paramSchemaDVFactory)
  {
    fDVFactory = paramSchemaDVFactory;
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
  {
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fEntityResolver = ((XMLEntityResolver)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
    XMLEntityResolver localXMLEntityResolver = (XMLEntityResolver)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
    if (localXMLEntityResolver != null) {
      fSchemaParser.setEntityResolver(localXMLEntityResolver);
    }
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    try
    {
      XMLErrorHandler localXMLErrorHandler = fErrorReporter.getErrorHandler();
      if (localXMLErrorHandler != fSchemaParser.getProperty("http://apache.org/xml/properties/internal/error-handler"))
      {
        fSchemaParser.setProperty("http://apache.org/xml/properties/internal/error-handler", localXMLErrorHandler != null ? localXMLErrorHandler : new DefaultErrorHandler());
        if (fAnnotationValidator != null) {
          fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", localXMLErrorHandler != null ? localXMLErrorHandler : new DefaultErrorHandler());
        }
      }
      Locale localLocale = fErrorReporter.getLocale();
      if (localLocale != fSchemaParser.getProperty("http://apache.org/xml/properties/locale"))
      {
        fSchemaParser.setProperty("http://apache.org/xml/properties/locale", localLocale);
        if (fAnnotationValidator != null) {
          fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", localLocale);
        }
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException1) {}
    try
    {
      fValidateAnnotations = paramXMLComponentManager.getFeature("http://apache.org/xml/features/validate-annotations");
    }
    catch (XMLConfigurationException localXMLConfigurationException2)
    {
      fValidateAnnotations = false;
    }
    try
    {
      fHonourAllSchemaLocations = paramXMLComponentManager.getFeature("http://apache.org/xml/features/honour-all-schemaLocations");
    }
    catch (XMLConfigurationException localXMLConfigurationException3)
    {
      fHonourAllSchemaLocations = false;
    }
    try
    {
      fNamespaceGrowth = paramXMLComponentManager.getFeature("http://apache.org/xml/features/namespace-growth");
    }
    catch (XMLConfigurationException localXMLConfigurationException4)
    {
      fNamespaceGrowth = false;
    }
    try
    {
      fTolerateDuplicates = paramXMLComponentManager.getFeature("http://apache.org/xml/features/internal/tolerate-duplicates");
    }
    catch (XMLConfigurationException localXMLConfigurationException5)
    {
      fTolerateDuplicates = false;
    }
    try
    {
      fSchemaParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", fErrorReporter.getFeature("http://apache.org/xml/features/continue-after-fatal-error"));
    }
    catch (XMLConfigurationException localXMLConfigurationException6) {}
    try
    {
      fSchemaParser.setFeature("http://apache.org/xml/features/allow-java-encodings", paramXMLComponentManager.getFeature("http://apache.org/xml/features/allow-java-encodings"));
    }
    catch (XMLConfigurationException localXMLConfigurationException7) {}
    try
    {
      fSchemaParser.setFeature("http://apache.org/xml/features/standard-uri-conformant", paramXMLComponentManager.getFeature("http://apache.org/xml/features/standard-uri-conformant"));
    }
    catch (XMLConfigurationException localXMLConfigurationException8) {}
    try
    {
      fGrammarPool = ((XMLGrammarPool)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool"));
    }
    catch (XMLConfigurationException localXMLConfigurationException9)
    {
      fGrammarPool = null;
    }
    try
    {
      fSchemaParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", paramXMLComponentManager.getFeature("http://apache.org/xml/features/disallow-doctype-decl"));
    }
    catch (XMLConfigurationException localXMLConfigurationException10) {}
    try
    {
      Object localObject = paramXMLComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
      if (localObject != null) {
        fSchemaParser.setProperty("http://apache.org/xml/properties/security-manager", localObject);
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException11) {}
  }
  
  void traverseLocalElements()
  {
    fElementTraverser.fDeferTraversingLocalElements = false;
    for (int i = 0; i < fLocalElemStackPos; i++)
    {
      Element localElement = fLocalElementDecl[i];
      XSDocumentInfo localXSDocumentInfo = fLocalElementDecl_schema[i];
      SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(fTargetNamespace);
      fElementTraverser.traverseLocal(fParticle[i], localElement, localXSDocumentInfo, localSchemaGrammar, fAllContext[i], fParent[i], fLocalElemNamespaceContext[i]);
      if (fParticle[i].fType == 0)
      {
        XSModelGroupImpl localXSModelGroupImpl = null;
        if ((fParent[i] instanceof XSComplexTypeDecl))
        {
          XSParticle localXSParticle = ((XSComplexTypeDecl)fParent[i]).getParticle();
          if (localXSParticle != null) {
            localXSModelGroupImpl = (XSModelGroupImpl)localXSParticle.getTerm();
          }
        }
        else
        {
          localXSModelGroupImpl = fParent[i]).fModelGroup;
        }
        if (localXSModelGroupImpl != null) {
          removeParticle(localXSModelGroupImpl, fParticle[i]);
        }
      }
    }
  }
  
  private boolean removeParticle(XSModelGroupImpl paramXSModelGroupImpl, XSParticleDecl paramXSParticleDecl)
  {
    for (int i = 0; i < fParticleCount; i++)
    {
      XSParticleDecl localXSParticleDecl = fParticles[i];
      if (localXSParticleDecl == paramXSParticleDecl)
      {
        for (int j = i; j < fParticleCount - 1; j++) {
          fParticles[j] = fParticles[(j + 1)];
        }
        fParticleCount -= 1;
        return true;
      }
      if ((fType == 3) && (removeParticle((XSModelGroupImpl)fValue, paramXSParticleDecl))) {
        return true;
      }
    }
    return false;
  }
  
  void fillInLocalElemInfo(Element paramElement, XSDocumentInfo paramXSDocumentInfo, int paramInt, XSObject paramXSObject, XSParticleDecl paramXSParticleDecl)
  {
    if (fParticle.length == fLocalElemStackPos)
    {
      XSParticleDecl[] arrayOfXSParticleDecl = new XSParticleDecl[fLocalElemStackPos + 10];
      System.arraycopy(fParticle, 0, arrayOfXSParticleDecl, 0, fLocalElemStackPos);
      fParticle = arrayOfXSParticleDecl;
      Element[] arrayOfElement = new Element[fLocalElemStackPos + 10];
      System.arraycopy(fLocalElementDecl, 0, arrayOfElement, 0, fLocalElemStackPos);
      fLocalElementDecl = arrayOfElement;
      XSDocumentInfo[] arrayOfXSDocumentInfo = new XSDocumentInfo[fLocalElemStackPos + 10];
      System.arraycopy(fLocalElementDecl_schema, 0, arrayOfXSDocumentInfo, 0, fLocalElemStackPos);
      fLocalElementDecl_schema = arrayOfXSDocumentInfo;
      int[] arrayOfInt = new int[fLocalElemStackPos + 10];
      System.arraycopy(fAllContext, 0, arrayOfInt, 0, fLocalElemStackPos);
      fAllContext = arrayOfInt;
      XSObject[] arrayOfXSObject = new XSObject[fLocalElemStackPos + 10];
      System.arraycopy(fParent, 0, arrayOfXSObject, 0, fLocalElemStackPos);
      fParent = arrayOfXSObject;
      String[][] arrayOfString; = new String[fLocalElemStackPos + 10][];
      System.arraycopy(fLocalElemNamespaceContext, 0, arrayOfString;, 0, fLocalElemStackPos);
      fLocalElemNamespaceContext = arrayOfString;;
    }
    fParticle[fLocalElemStackPos] = paramXSParticleDecl;
    fLocalElementDecl[fLocalElemStackPos] = paramElement;
    fLocalElementDecl_schema[fLocalElemStackPos] = paramXSDocumentInfo;
    fAllContext[fLocalElemStackPos] = paramInt;
    fParent[fLocalElemStackPos] = paramXSObject;
    fLocalElemNamespaceContext[(fLocalElemStackPos++)] = fNamespaceSupport.getEffectiveLocalContext();
  }
  
  void checkForDuplicateNames(String paramString, int paramInt, Hashtable paramHashtable1, Hashtable paramHashtable2, Element paramElement, XSDocumentInfo paramXSDocumentInfo)
  {
    Object localObject = null;
    if ((localObject = paramHashtable1.get(paramString)) == null)
    {
      if ((fNamespaceGrowth) && (!fTolerateDuplicates)) {
        checkForDuplicateNames(paramString, paramInt, paramElement);
      }
      paramHashtable1.put(paramString, paramElement);
      paramHashtable2.put(paramString, paramXSDocumentInfo);
    }
    else
    {
      Element localElement1 = (Element)localObject;
      XSDocumentInfo localXSDocumentInfo1 = (XSDocumentInfo)paramHashtable2.get(paramString);
      if (localElement1 == paramElement) {
        return;
      }
      Element localElement2 = null;
      XSDocumentInfo localXSDocumentInfo2 = null;
      int i = 1;
      if (DOMUtil.getLocalName(localElement2 = DOMUtil.getParent(localElement1)).equals(SchemaSymbols.ELT_REDEFINE))
      {
        localXSDocumentInfo2 = (XSDocumentInfo)fRedefine2XSDMap.get(localElement2);
      }
      else if (DOMUtil.getLocalName(DOMUtil.getParent(paramElement)).equals(SchemaSymbols.ELT_REDEFINE))
      {
        localXSDocumentInfo2 = localXSDocumentInfo1;
        i = 0;
      }
      if (localXSDocumentInfo2 != null)
      {
        if (localXSDocumentInfo1 == paramXSDocumentInfo)
        {
          reportSchemaError("sch-props-correct.2", new Object[] { paramString }, paramElement);
          return;
        }
        String str = paramString.substring(paramString.lastIndexOf(',') + 1) + "_fn3dktizrknc9pi";
        if (localXSDocumentInfo2 == paramXSDocumentInfo)
        {
          paramElement.setAttribute(SchemaSymbols.ATT_NAME, str);
          if (fTargetNamespace == null)
          {
            paramHashtable1.put("," + str, paramElement);
            paramHashtable2.put("," + str, paramXSDocumentInfo);
          }
          else
          {
            paramHashtable1.put(fTargetNamespace + "," + str, paramElement);
            paramHashtable2.put(fTargetNamespace + "," + str, paramXSDocumentInfo);
          }
          if (fTargetNamespace == null) {
            checkForDuplicateNames("," + str, paramInt, paramHashtable1, paramHashtable2, paramElement, paramXSDocumentInfo);
          } else {
            checkForDuplicateNames(fTargetNamespace + "," + str, paramInt, paramHashtable1, paramHashtable2, paramElement, paramXSDocumentInfo);
          }
        }
        else if (i != 0)
        {
          if (fTargetNamespace == null) {
            checkForDuplicateNames("," + str, paramInt, paramHashtable1, paramHashtable2, paramElement, paramXSDocumentInfo);
          } else {
            checkForDuplicateNames(fTargetNamespace + "," + str, paramInt, paramHashtable1, paramHashtable2, paramElement, paramXSDocumentInfo);
          }
        }
        else
        {
          reportSchemaError("sch-props-correct.2", new Object[] { paramString }, paramElement);
        }
      }
      else if ((!fTolerateDuplicates) || (fUnparsedRegistriesExt[paramInt].get(paramString) == paramXSDocumentInfo))
      {
        reportSchemaError("sch-props-correct.2", new Object[] { paramString }, paramElement);
      }
    }
    if (fTolerateDuplicates) {
      fUnparsedRegistriesExt[paramInt].put(paramString, paramXSDocumentInfo);
    }
  }
  
  void checkForDuplicateNames(String paramString, int paramInt, Element paramElement)
  {
    int i = paramString.indexOf(',');
    String str = paramString.substring(0, i);
    SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(emptyString2Null(str));
    if (localSchemaGrammar != null)
    {
      Object localObject = getGlobalDeclFromGrammar(localSchemaGrammar, paramInt, paramString.substring(i + 1));
      if (localObject != null) {
        reportSchemaError("sch-props-correct.2", new Object[] { paramString }, paramElement);
      }
    }
  }
  
  private void renameRedefiningComponents(XSDocumentInfo paramXSDocumentInfo, Element paramElement, String paramString1, String paramString2, String paramString3)
  {
    Object localObject1;
    Object localObject2;
    Object localObject3;
    Object localObject4;
    if (paramString1.equals(SchemaSymbols.ELT_SIMPLETYPE))
    {
      localObject1 = DOMUtil.getFirstChildElement(paramElement);
      if (localObject1 == null)
      {
        reportSchemaError("src-redefine.5.a.a", null, paramElement);
      }
      else
      {
        localObject2 = DOMUtil.getLocalName((Node)localObject1);
        if (((String)localObject2).equals(SchemaSymbols.ELT_ANNOTATION)) {
          localObject1 = DOMUtil.getNextSiblingElement((Node)localObject1);
        }
        if (localObject1 == null)
        {
          reportSchemaError("src-redefine.5.a.a", null, paramElement);
        }
        else
        {
          localObject2 = DOMUtil.getLocalName((Node)localObject1);
          if (!((String)localObject2).equals(SchemaSymbols.ELT_RESTRICTION))
          {
            reportSchemaError("src-redefine.5.a.b", new Object[] { localObject2 }, paramElement);
          }
          else
          {
            localObject3 = fAttributeChecker.checkAttributes((Element)localObject1, false, paramXSDocumentInfo);
            localObject4 = (QName)localObject3[XSAttributeChecker.ATTIDX_BASE];
            if ((localObject4 == null) || (uri != fTargetNamespace) || (!localpart.equals(paramString2))) {
              reportSchemaError("src-redefine.5.a.c", new Object[] { localObject2, (fTargetNamespace == null ? "" : fTargetNamespace) + "," + paramString2 }, paramElement);
            } else if ((prefix != null) && (prefix.length() > 0)) {
              ((Element)localObject1).setAttribute(SchemaSymbols.ATT_BASE, prefix + ":" + paramString3);
            } else {
              ((Element)localObject1).setAttribute(SchemaSymbols.ATT_BASE, paramString3);
            }
            fAttributeChecker.returnAttrArray((Object[])localObject3, paramXSDocumentInfo);
          }
        }
      }
    }
    else if (paramString1.equals(SchemaSymbols.ELT_COMPLEXTYPE))
    {
      localObject1 = DOMUtil.getFirstChildElement(paramElement);
      if (localObject1 == null)
      {
        reportSchemaError("src-redefine.5.b.a", null, paramElement);
      }
      else
      {
        if (DOMUtil.getLocalName((Node)localObject1).equals(SchemaSymbols.ELT_ANNOTATION)) {
          localObject1 = DOMUtil.getNextSiblingElement((Node)localObject1);
        }
        if (localObject1 == null)
        {
          reportSchemaError("src-redefine.5.b.a", null, paramElement);
        }
        else
        {
          localObject2 = DOMUtil.getFirstChildElement((Node)localObject1);
          if (localObject2 == null)
          {
            reportSchemaError("src-redefine.5.b.b", null, (Element)localObject1);
          }
          else
          {
            localObject3 = DOMUtil.getLocalName((Node)localObject2);
            if (((String)localObject3).equals(SchemaSymbols.ELT_ANNOTATION)) {
              localObject2 = DOMUtil.getNextSiblingElement((Node)localObject2);
            }
            if (localObject2 == null)
            {
              reportSchemaError("src-redefine.5.b.b", null, (Element)localObject1);
            }
            else
            {
              localObject3 = DOMUtil.getLocalName((Node)localObject2);
              if ((!((String)localObject3).equals(SchemaSymbols.ELT_RESTRICTION)) && (!((String)localObject3).equals(SchemaSymbols.ELT_EXTENSION)))
              {
                reportSchemaError("src-redefine.5.b.c", new Object[] { localObject3 }, (Element)localObject2);
              }
              else
              {
                localObject4 = fAttributeChecker.checkAttributes((Element)localObject2, false, paramXSDocumentInfo);
                QName localQName = (QName)localObject4[XSAttributeChecker.ATTIDX_BASE];
                if ((localQName == null) || (uri != fTargetNamespace) || (!localpart.equals(paramString2))) {
                  reportSchemaError("src-redefine.5.b.d", new Object[] { localObject3, (fTargetNamespace == null ? "" : fTargetNamespace) + "," + paramString2 }, (Element)localObject2);
                } else if ((prefix != null) && (prefix.length() > 0)) {
                  ((Element)localObject2).setAttribute(SchemaSymbols.ATT_BASE, prefix + ":" + paramString3);
                } else {
                  ((Element)localObject2).setAttribute(SchemaSymbols.ATT_BASE, paramString3);
                }
              }
            }
          }
        }
      }
    }
    else
    {
      int i;
      if (paramString1.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP))
      {
        localObject1 = fTargetNamespace + "," + paramString2;
        i = changeRedefineGroup((String)localObject1, paramString1, paramString3, paramElement, paramXSDocumentInfo);
        if (i > 1) {
          reportSchemaError("src-redefine.7.1", new Object[] { new Integer(i) }, paramElement);
        } else if (i != 1) {
          if (fTargetNamespace == null) {
            fRedefinedRestrictedAttributeGroupRegistry.put(localObject1, "," + paramString3);
          } else {
            fRedefinedRestrictedAttributeGroupRegistry.put(localObject1, fTargetNamespace + "," + paramString3);
          }
        }
      }
      else if (paramString1.equals(SchemaSymbols.ELT_GROUP))
      {
        localObject1 = fTargetNamespace + "," + paramString2;
        i = changeRedefineGroup((String)localObject1, paramString1, paramString3, paramElement, paramXSDocumentInfo);
        if (i > 1) {
          reportSchemaError("src-redefine.6.1.1", new Object[] { new Integer(i) }, paramElement);
        } else if (i != 1) {
          if (fTargetNamespace == null) {
            fRedefinedRestrictedGroupRegistry.put(localObject1, "," + paramString3);
          } else {
            fRedefinedRestrictedGroupRegistry.put(localObject1, fTargetNamespace + "," + paramString3);
          }
        }
      }
      else
      {
        reportSchemaError("Internal-Error", new Object[] { "could not handle this particular <redefine>; please submit your schemas and instance document in a bug report!" }, paramElement);
      }
    }
  }
  
  private String findQName(String paramString, XSDocumentInfo paramXSDocumentInfo)
  {
    SchemaNamespaceSupport localSchemaNamespaceSupport = fNamespaceSupport;
    int i = paramString.indexOf(':');
    String str1 = XMLSymbols.EMPTY_STRING;
    if (i > 0) {
      str1 = paramString.substring(0, i);
    }
    String str2 = localSchemaNamespaceSupport.getURI(fSymbolTable.addSymbol(str1));
    String str3 = i == 0 ? paramString : paramString.substring(i + 1);
    if ((str1 == XMLSymbols.EMPTY_STRING) && (str2 == null) && (fIsChameleonSchema)) {
      str2 = fTargetNamespace;
    }
    if (str2 == null) {
      return "," + str3;
    }
    return str2 + "," + str3;
  }
  
  private int changeRedefineGroup(String paramString1, String paramString2, String paramString3, Element paramElement, XSDocumentInfo paramXSDocumentInfo)
  {
    int i = 0;
    for (Element localElement = DOMUtil.getFirstChildElement(paramElement); localElement != null; localElement = DOMUtil.getNextSiblingElement(localElement))
    {
      String str1 = DOMUtil.getLocalName(localElement);
      if (!str1.equals(paramString2))
      {
        i += changeRedefineGroup(paramString1, paramString2, paramString3, localElement, paramXSDocumentInfo);
      }
      else
      {
        String str2 = localElement.getAttribute(SchemaSymbols.ATT_REF);
        if (str2.length() != 0)
        {
          String str3 = findQName(str2, paramXSDocumentInfo);
          if (paramString1.equals(str3))
          {
            String str4 = XMLSymbols.EMPTY_STRING;
            int j = str2.indexOf(":");
            if (j > 0)
            {
              str4 = str2.substring(0, j);
              localElement.setAttribute(SchemaSymbols.ATT_REF, str4 + ":" + paramString3);
            }
            else
            {
              localElement.setAttribute(SchemaSymbols.ATT_REF, paramString3);
            }
            i++;
            if (paramString2.equals(SchemaSymbols.ELT_GROUP))
            {
              String str5 = localElement.getAttribute(SchemaSymbols.ATT_MINOCCURS);
              String str6 = localElement.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
              if (((str6.length() != 0) && (!str6.equals("1"))) || ((str5.length() != 0) && (!str5.equals("1")))) {
                reportSchemaError("src-redefine.6.1.2", new Object[] { str2 }, localElement);
              }
            }
          }
        }
      }
    }
    return i;
  }
  
  private XSDocumentInfo findXSDocumentForDecl(XSDocumentInfo paramXSDocumentInfo1, Element paramElement, XSDocumentInfo paramXSDocumentInfo2)
  {
    XSDocumentInfo localXSDocumentInfo1 = paramXSDocumentInfo2;
    if (localXSDocumentInfo1 == null) {
      return null;
    }
    XSDocumentInfo localXSDocumentInfo2 = (XSDocumentInfo)localXSDocumentInfo1;
    return localXSDocumentInfo2;
  }
  
  private boolean nonAnnotationContent(Element paramElement)
  {
    for (Element localElement = DOMUtil.getFirstChildElement(paramElement); localElement != null; localElement = DOMUtil.getNextSiblingElement(localElement)) {
      if (!DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION)) {
        return true;
      }
    }
    return false;
  }
  
  private void setSchemasVisible(XSDocumentInfo paramXSDocumentInfo)
  {
    if (DOMUtil.isHidden(fSchemaElement, fHiddenNodes))
    {
      DOMUtil.setVisible(fSchemaElement, fHiddenNodes);
      Vector localVector = (Vector)fDependencyMap.get(paramXSDocumentInfo);
      for (int i = 0; i < localVector.size(); i++) {
        setSchemasVisible((XSDocumentInfo)localVector.elementAt(i));
      }
    }
  }
  
  public SimpleLocator element2Locator(Element paramElement)
  {
    if (!(paramElement instanceof ElementImpl)) {
      return null;
    }
    SimpleLocator localSimpleLocator = new SimpleLocator();
    return element2Locator(paramElement, localSimpleLocator) ? localSimpleLocator : null;
  }
  
  public boolean element2Locator(Element paramElement, SimpleLocator paramSimpleLocator)
  {
    if (paramSimpleLocator == null) {
      return false;
    }
    if ((paramElement instanceof ElementImpl))
    {
      ElementImpl localElementImpl = (ElementImpl)paramElement;
      Document localDocument = localElementImpl.getOwnerDocument();
      String str = (String)fDoc2SystemId.get(DOMUtil.getRoot(localDocument));
      int i = localElementImpl.getLineNumber();
      int j = localElementImpl.getColumnNumber();
      paramSimpleLocator.setValues(str, str, i, j, localElementImpl.getCharacterOffset());
      return true;
    }
    return false;
  }
  
  void reportSchemaError(String paramString, Object[] paramArrayOfObject, Element paramElement)
  {
    reportSchemaError(paramString, paramArrayOfObject, paramElement, null);
  }
  
  void reportSchemaError(String paramString, Object[] paramArrayOfObject, Element paramElement, Exception paramException)
  {
    if (element2Locator(paramElement, xl)) {
      fErrorReporter.reportError(xl, "http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)1, paramException);
    } else {
      fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)1, paramException);
    }
  }
  
  void reportSchemaWarning(String paramString, Object[] paramArrayOfObject, Element paramElement)
  {
    reportSchemaWarning(paramString, paramArrayOfObject, paramElement, null);
  }
  
  void reportSchemaWarning(String paramString, Object[] paramArrayOfObject, Element paramElement, Exception paramException)
  {
    if (element2Locator(paramElement, xl)) {
      fErrorReporter.reportError(xl, "http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)0, paramException);
    } else {
      fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)0, paramException);
    }
  }
  
  public void setGenerateSyntheticAnnotations(boolean paramBoolean)
  {
    fSchemaParser.setFeature("http://apache.org/xml/features/generate-synthetic-annotations", paramBoolean);
  }
  
  private static final class SAX2XNIUtil
    extends ErrorHandlerWrapper
  {
    private SAX2XNIUtil() {}
    
    public static XMLParseException createXMLParseException0(SAXParseException paramSAXParseException)
    {
      return ErrorHandlerWrapper.createXMLParseException(paramSAXParseException);
    }
    
    public static XNIException createXNIException0(SAXException paramSAXException)
    {
      return ErrorHandlerWrapper.createXNIException(paramSAXException);
    }
  }
  
  private static class XSDKey
  {
    String systemId;
    short referType;
    String referNS;
    
    XSDKey(String paramString1, short paramShort, String paramString2)
    {
      systemId = paramString1;
      referType = paramShort;
      referNS = paramString2;
    }
    
    public int hashCode()
    {
      return referNS == null ? 0 : referNS.hashCode();
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof XSDKey)) {
        return false;
      }
      XSDKey localXSDKey = (XSDKey)paramObject;
      if (referNS != referNS) {
        return false;
      }
      return (systemId != null) && (systemId.equals(systemId));
    }
  }
  
  private static class XSAnnotationGrammarPool
    implements XMLGrammarPool
  {
    private XSGrammarBucket fGrammarBucket;
    private Grammar[] fInitialGrammarSet;
    
    private XSAnnotationGrammarPool() {}
    
    public Grammar[] retrieveInitialGrammarSet(String paramString)
    {
      if (paramString == "http://www.w3.org/2001/XMLSchema")
      {
        if (fInitialGrammarSet == null) {
          if (fGrammarBucket == null)
          {
            fInitialGrammarSet = new Grammar[] { SchemaGrammar.Schema4Annotations.INSTANCE };
          }
          else
          {
            SchemaGrammar[] arrayOfSchemaGrammar = fGrammarBucket.getGrammars();
            for (int i = 0; i < arrayOfSchemaGrammar.length; i++) {
              if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(arrayOfSchemaGrammar[i].getTargetNamespace()))
              {
                fInitialGrammarSet = arrayOfSchemaGrammar;
                return fInitialGrammarSet;
              }
            }
            Grammar[] arrayOfGrammar = new Grammar[arrayOfSchemaGrammar.length + 1];
            System.arraycopy(arrayOfSchemaGrammar, 0, arrayOfGrammar, 0, arrayOfSchemaGrammar.length);
            arrayOfGrammar[(arrayOfGrammar.length - 1)] = SchemaGrammar.Schema4Annotations.INSTANCE;
            fInitialGrammarSet = arrayOfGrammar;
          }
        }
        return fInitialGrammarSet;
      }
      return new Grammar[0];
    }
    
    public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar) {}
    
    public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
    {
      if (paramXMLGrammarDescription.getGrammarType() == "http://www.w3.org/2001/XMLSchema")
      {
        String str = ((XMLSchemaDescription)paramXMLGrammarDescription).getTargetNamespace();
        if (fGrammarBucket != null)
        {
          SchemaGrammar localSchemaGrammar = fGrammarBucket.getGrammar(str);
          if (localSchemaGrammar != null) {
            return localSchemaGrammar;
          }
        }
        if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(str)) {
          return SchemaGrammar.Schema4Annotations.INSTANCE;
        }
      }
      return null;
    }
    
    public void refreshGrammars(XSGrammarBucket paramXSGrammarBucket)
    {
      fGrammarBucket = paramXSGrammarBucket;
      fInitialGrammarSet = null;
    }
    
    public void lockPool() {}
    
    public void unlockPool() {}
    
    public void clear() {}
    
    XSAnnotationGrammarPool(XSDHandler.1 param1)
    {
      this();
    }
  }
}
