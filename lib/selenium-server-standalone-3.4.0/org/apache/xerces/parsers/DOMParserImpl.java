package org.apache.xerces.parsers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMStringListImpl;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSResourceResolver;

public class DOMParserImpl
  extends AbstractDOMParser
  implements LSParser, DOMConfiguration
{
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
  protected static final String XMLSCHEMA = "http://apache.org/xml/features/validation/schema";
  protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
  protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
  protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
  protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
  protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
  protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
  protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String PSVI_AUGMENT = "http://apache.org/xml/features/validation/schema/augment-psvi";
  protected boolean fNamespaceDeclarations = true;
  protected String fSchemaType = null;
  protected boolean fBusy = false;
  private boolean abortNow = false;
  private Thread currentThread;
  protected static final boolean DEBUG = false;
  private String fSchemaLocation = null;
  private DOMStringList fRecognizedParameters;
  private boolean fNullFilterInUse = false;
  private AbortHandler abortHandler = null;
  
  public DOMParserImpl(String paramString1, String paramString2)
  {
    this((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", paramString1));
    if (paramString2 != null) {
      if (paramString2.equals(Constants.NS_DTD))
      {
        fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
        fSchemaType = Constants.NS_DTD;
      }
      else if (paramString2.equals(Constants.NS_XMLSCHEMA))
      {
        fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
      }
    }
  }
  
  public DOMParserImpl(XMLParserConfiguration paramXMLParserConfiguration)
  {
    super(paramXMLParserConfiguration);
    String[] arrayOfString = { "canonical-form", "cdata-sections", "charset-overrides-xml-encoding", "infoset", "namespace-declarations", "split-cdata-sections", "supported-media-types-only", "certified", "well-formed", "ignore-unknown-character-denormalizations" };
    fConfiguration.addRecognizedFeatures(arrayOfString);
    fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
    fConfiguration.setFeature("namespace-declarations", true);
    fConfiguration.setFeature("well-formed", true);
    fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
    fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
    fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
    fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
    fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
    fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
    fConfiguration.setFeature("canonical-form", false);
    fConfiguration.setFeature("charset-overrides-xml-encoding", true);
    fConfiguration.setFeature("split-cdata-sections", true);
    fConfiguration.setFeature("supported-media-types-only", false);
    fConfiguration.setFeature("ignore-unknown-character-denormalizations", true);
    fConfiguration.setFeature("certified", true);
    try
    {
      fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
  }
  
  public DOMParserImpl(SymbolTable paramSymbolTable)
  {
    this((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
    fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", paramSymbolTable);
  }
  
  public DOMParserImpl(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
    fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", paramSymbolTable);
    fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", paramXMLGrammarPool);
  }
  
  public void reset()
  {
    super.reset();
    fNamespaceDeclarations = fConfiguration.getFeature("namespace-declarations");
    if (fNullFilterInUse)
    {
      fDOMFilter = null;
      fNullFilterInUse = false;
    }
    if (fSkippedElemStack != null) {
      fSkippedElemStack.removeAllElements();
    }
    fRejectedElementDepth = 0;
    fFilterReject = false;
    fSchemaType = null;
  }
  
  public DOMConfiguration getDomConfig()
  {
    return this;
  }
  
  public LSParserFilter getFilter()
  {
    return !fNullFilterInUse ? fDOMFilter : null;
  }
  
  public void setFilter(LSParserFilter paramLSParserFilter)
  {
    if ((fBusy) && (paramLSParserFilter == null) && (fDOMFilter != null))
    {
      fNullFilterInUse = true;
      fDOMFilter = NullLSParserFilter.INSTANCE;
    }
    else
    {
      fDOMFilter = paramLSParserFilter;
    }
    if (fSkippedElemStack == null) {
      fSkippedElemStack = new Stack();
    }
  }
  
  public void setParameter(String paramString, Object paramObject)
    throws DOMException
  {
    if ((paramObject instanceof Boolean))
    {
      boolean bool = ((Boolean)paramObject).booleanValue();
      try
      {
        if (paramString.equalsIgnoreCase("comments"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/include-comments", bool);
        }
        else if (paramString.equalsIgnoreCase("datatype-normalization"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", bool);
        }
        else if (paramString.equalsIgnoreCase("entities"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", bool);
        }
        else if (paramString.equalsIgnoreCase("disallow-doctype"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/disallow-doctype-decl", bool);
        }
        else if ((paramString.equalsIgnoreCase("supported-media-types-only")) || (paramString.equalsIgnoreCase("normalize-characters")) || (paramString.equalsIgnoreCase("check-character-normalization")) || (paramString.equalsIgnoreCase("canonical-form")))
        {
          if (bool) {
            throw newFeatureNotSupportedError(paramString);
          }
        }
        else if (paramString.equalsIgnoreCase("namespaces"))
        {
          fConfiguration.setFeature("http://xml.org/sax/features/namespaces", bool);
        }
        else if (paramString.equalsIgnoreCase("infoset"))
        {
          if (bool)
          {
            fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
            fConfiguration.setFeature("namespace-declarations", true);
            fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
            fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
            fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
            fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
            fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
          }
        }
        else if (paramString.equalsIgnoreCase("cdata-sections"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", bool);
        }
        else if (paramString.equalsIgnoreCase("namespace-declarations"))
        {
          fConfiguration.setFeature("namespace-declarations", bool);
        }
        else if ((paramString.equalsIgnoreCase("well-formed")) || (paramString.equalsIgnoreCase("ignore-unknown-character-denormalizations")))
        {
          if (!bool) {
            throw newFeatureNotSupportedError(paramString);
          }
        }
        else if (paramString.equalsIgnoreCase("validate"))
        {
          fConfiguration.setFeature("http://xml.org/sax/features/validation", bool);
          if (fSchemaType != Constants.NS_DTD)
          {
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", bool);
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", bool);
          }
          if (bool) {
            fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
          }
        }
        else if (paramString.equalsIgnoreCase("validate-if-schema"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", bool);
          if (bool) {
            fConfiguration.setFeature("http://xml.org/sax/features/validation", false);
          }
        }
        else if (paramString.equalsIgnoreCase("element-content-whitespace"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", bool);
        }
        else if (paramString.equalsIgnoreCase("psvi"))
        {
          fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
          fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "org.apache.xerces.dom.PSVIDocumentImpl");
        }
        else
        {
          String str2;
          if (paramString.equalsIgnoreCase("http://apache.org/xml/features/honour-all-schemaLocations")) {
            str2 = "http://apache.org/xml/features/honour-all-schemaLocations";
          } else if (paramString.equals("http://apache.org/xml/features/namespace-growth")) {
            str2 = "http://apache.org/xml/features/namespace-growth";
          } else if (paramString.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) {
            str2 = "http://apache.org/xml/features/internal/tolerate-duplicates";
          } else {
            str2 = paramString.toLowerCase(Locale.ENGLISH);
          }
          fConfiguration.setFeature(str2, bool);
        }
      }
      catch (XMLConfigurationException localXMLConfigurationException5)
      {
        throw newFeatureNotFoundError(paramString);
      }
    }
    else if (paramString.equalsIgnoreCase("error-handler"))
    {
      if (((paramObject instanceof DOMErrorHandler)) || (paramObject == null)) {
        try
        {
          fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler)paramObject);
          fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", fErrorHandler);
        }
        catch (XMLConfigurationException localXMLConfigurationException1) {}
      } else {
        throw newTypeMismatchError(paramString);
      }
    }
    else if (paramString.equalsIgnoreCase("resource-resolver"))
    {
      if (((paramObject instanceof LSResourceResolver)) || (paramObject == null)) {
        try
        {
          fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper((LSResourceResolver)paramObject));
        }
        catch (XMLConfigurationException localXMLConfigurationException2) {}
      } else {
        throw newTypeMismatchError(paramString);
      }
    }
    else if (paramString.equalsIgnoreCase("schema-location"))
    {
      if (((paramObject instanceof String)) || (paramObject == null)) {
        try
        {
          if (paramObject == null)
          {
            fSchemaLocation = null;
            fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
          }
          else
          {
            fSchemaLocation = ((String)paramObject);
            StringTokenizer localStringTokenizer = new StringTokenizer(fSchemaLocation, " \n\t\r");
            if (localStringTokenizer.hasMoreTokens())
            {
              ArrayList localArrayList = new ArrayList();
              localArrayList.add(localStringTokenizer.nextToken());
              while (localStringTokenizer.hasMoreTokens()) {
                localArrayList.add(localStringTokenizer.nextToken());
              }
              fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", localArrayList.toArray());
            }
            else
            {
              fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", paramObject);
            }
          }
        }
        catch (XMLConfigurationException localXMLConfigurationException3) {}
      } else {
        throw newTypeMismatchError(paramString);
      }
    }
    else if (paramString.equalsIgnoreCase("schema-type"))
    {
      if (((paramObject instanceof String)) || (paramObject == null)) {
        try
        {
          if (paramObject == null)
          {
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", false);
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
            fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null);
            fSchemaType = null;
          }
          else if (paramObject.equals(Constants.NS_XMLSCHEMA))
          {
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
            fSchemaType = Constants.NS_XMLSCHEMA;
          }
          else if (paramObject.equals(Constants.NS_DTD))
          {
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", false);
            fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
            fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
            fSchemaType = Constants.NS_DTD;
          }
        }
        catch (XMLConfigurationException localXMLConfigurationException4) {}
      } else {
        throw newTypeMismatchError(paramString);
      }
    }
    else if (paramString.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name"))
    {
      fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", paramObject);
    }
    else
    {
      String str1 = paramString.toLowerCase(Locale.ENGLISH);
      try
      {
        fConfiguration.setProperty(str1, paramObject);
        return;
      }
      catch (XMLConfigurationException localXMLConfigurationException6)
      {
        try
        {
          if (paramString.equalsIgnoreCase("http://apache.org/xml/features/honour-all-schemaLocations")) {
            str1 = "http://apache.org/xml/features/honour-all-schemaLocations";
          } else if (paramString.equals("http://apache.org/xml/features/namespace-growth")) {
            str1 = "http://apache.org/xml/features/namespace-growth";
          } else if (paramString.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) {
            str1 = "http://apache.org/xml/features/internal/tolerate-duplicates";
          }
          fConfiguration.getFeature(str1);
          throw newTypeMismatchError(paramString);
        }
        catch (XMLConfigurationException localXMLConfigurationException7)
        {
          throw newFeatureNotFoundError(paramString);
        }
      }
    }
  }
  
  public Object getParameter(String paramString)
    throws DOMException
  {
    if (paramString.equalsIgnoreCase("comments")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/include-comments") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("datatype-normalization")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/validation/schema/normalized-value") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("entities")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("namespaces")) {
      return fConfiguration.getFeature("http://xml.org/sax/features/namespaces") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("validate")) {
      return fConfiguration.getFeature("http://xml.org/sax/features/validation") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("validate-if-schema")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/validation/dynamic") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("element-content-whitespace")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("disallow-doctype")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/disallow-doctype-decl") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("infoset"))
    {
      int i = (fConfiguration.getFeature("http://xml.org/sax/features/namespaces")) && (fConfiguration.getFeature("namespace-declarations")) && (fConfiguration.getFeature("http://apache.org/xml/features/include-comments")) && (fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace")) && (!fConfiguration.getFeature("http://apache.org/xml/features/validation/dynamic")) && (!fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes")) && (!fConfiguration.getFeature("http://apache.org/xml/features/validation/schema/normalized-value")) && (!fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes")) ? 1 : 0;
      return i != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("cdata-sections")) {
      return fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes") ? Boolean.TRUE : Boolean.FALSE;
    }
    if ((paramString.equalsIgnoreCase("check-character-normalization")) || (paramString.equalsIgnoreCase("normalize-characters"))) {
      return Boolean.FALSE;
    }
    if ((paramString.equalsIgnoreCase("namespace-declarations")) || (paramString.equalsIgnoreCase("well-formed")) || (paramString.equalsIgnoreCase("ignore-unknown-character-denormalizations")) || (paramString.equalsIgnoreCase("canonical-form")) || (paramString.equalsIgnoreCase("supported-media-types-only")) || (paramString.equalsIgnoreCase("split-cdata-sections")) || (paramString.equalsIgnoreCase("charset-overrides-xml-encoding"))) {
      return fConfiguration.getFeature(paramString.toLowerCase(Locale.ENGLISH)) ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("error-handler"))
    {
      if (fErrorHandler != null) {
        return fErrorHandler.getErrorHandler();
      }
      return null;
    }
    if (paramString.equalsIgnoreCase("resource-resolver"))
    {
      try
      {
        XMLEntityResolver localXMLEntityResolver = (XMLEntityResolver)fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
        if ((localXMLEntityResolver != null) && ((localXMLEntityResolver instanceof DOMEntityResolverWrapper))) {
          return ((DOMEntityResolverWrapper)localXMLEntityResolver).getEntityResolver();
        }
      }
      catch (XMLConfigurationException localXMLConfigurationException1) {}
      return null;
    }
    if (paramString.equalsIgnoreCase("schema-type")) {
      return fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
    }
    if (paramString.equalsIgnoreCase("schema-location")) {
      return fSchemaLocation;
    }
    if (paramString.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
      return fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table");
    }
    if (paramString.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
      return fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name");
    }
    String str;
    if (paramString.equalsIgnoreCase("http://apache.org/xml/features/honour-all-schemaLocations")) {
      str = "http://apache.org/xml/features/honour-all-schemaLocations";
    } else if (paramString.equals("http://apache.org/xml/features/namespace-growth")) {
      str = "http://apache.org/xml/features/namespace-growth";
    } else if (paramString.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) {
      str = "http://apache.org/xml/features/internal/tolerate-duplicates";
    } else {
      str = paramString.toLowerCase(Locale.ENGLISH);
    }
    try
    {
      return fConfiguration.getFeature(str) ? Boolean.TRUE : Boolean.FALSE;
    }
    catch (XMLConfigurationException localXMLConfigurationException2)
    {
      try
      {
        return fConfiguration.getProperty(str);
      }
      catch (XMLConfigurationException localXMLConfigurationException3)
      {
        throw newFeatureNotFoundError(paramString);
      }
    }
  }
  
  public boolean canSetParameter(String paramString, Object paramObject)
  {
    if (paramObject == null) {
      return true;
    }
    if ((paramObject instanceof Boolean))
    {
      boolean bool = ((Boolean)paramObject).booleanValue();
      if ((paramString.equalsIgnoreCase("supported-media-types-only")) || (paramString.equalsIgnoreCase("normalize-characters")) || (paramString.equalsIgnoreCase("check-character-normalization")) || (paramString.equalsIgnoreCase("canonical-form"))) {
        return !bool;
      }
      if ((paramString.equalsIgnoreCase("well-formed")) || (paramString.equalsIgnoreCase("ignore-unknown-character-denormalizations"))) {
        return bool;
      }
      if ((paramString.equalsIgnoreCase("cdata-sections")) || (paramString.equalsIgnoreCase("charset-overrides-xml-encoding")) || (paramString.equalsIgnoreCase("comments")) || (paramString.equalsIgnoreCase("datatype-normalization")) || (paramString.equalsIgnoreCase("disallow-doctype")) || (paramString.equalsIgnoreCase("entities")) || (paramString.equalsIgnoreCase("infoset")) || (paramString.equalsIgnoreCase("namespaces")) || (paramString.equalsIgnoreCase("namespace-declarations")) || (paramString.equalsIgnoreCase("validate")) || (paramString.equalsIgnoreCase("validate-if-schema")) || (paramString.equalsIgnoreCase("element-content-whitespace")) || (paramString.equalsIgnoreCase("xml-declaration"))) {
        return true;
      }
      try
      {
        String str;
        if (paramString.equalsIgnoreCase("http://apache.org/xml/features/honour-all-schemaLocations")) {
          str = "http://apache.org/xml/features/honour-all-schemaLocations";
        } else if (paramString.equalsIgnoreCase("http://apache.org/xml/features/namespace-growth")) {
          str = "http://apache.org/xml/features/namespace-growth";
        } else if (paramString.equalsIgnoreCase("http://apache.org/xml/features/internal/tolerate-duplicates")) {
          str = "http://apache.org/xml/features/internal/tolerate-duplicates";
        } else {
          str = paramString.toLowerCase(Locale.ENGLISH);
        }
        fConfiguration.getFeature(str);
        return true;
      }
      catch (XMLConfigurationException localXMLConfigurationException2)
      {
        return false;
      }
    }
    if (paramString.equalsIgnoreCase("error-handler")) {
      return ((paramObject instanceof DOMErrorHandler)) || (paramObject == null);
    }
    if (paramString.equalsIgnoreCase("resource-resolver")) {
      return ((paramObject instanceof LSResourceResolver)) || (paramObject == null);
    }
    if (paramString.equalsIgnoreCase("schema-type")) {
      return (((paramObject instanceof String)) && ((paramObject.equals(Constants.NS_XMLSCHEMA)) || (paramObject.equals(Constants.NS_DTD)))) || (paramObject == null);
    }
    if (paramString.equalsIgnoreCase("schema-location")) {
      return ((paramObject instanceof String)) || (paramObject == null);
    }
    if (paramString.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
      return true;
    }
    try
    {
      fConfiguration.getProperty(paramString.toLowerCase(Locale.ENGLISH));
      return true;
    }
    catch (XMLConfigurationException localXMLConfigurationException1) {}
    return false;
  }
  
  public DOMStringList getParameterNames()
  {
    if (fRecognizedParameters == null)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("namespaces");
      localArrayList.add("cdata-sections");
      localArrayList.add("canonical-form");
      localArrayList.add("namespace-declarations");
      localArrayList.add("split-cdata-sections");
      localArrayList.add("entities");
      localArrayList.add("validate-if-schema");
      localArrayList.add("validate");
      localArrayList.add("datatype-normalization");
      localArrayList.add("charset-overrides-xml-encoding");
      localArrayList.add("check-character-normalization");
      localArrayList.add("supported-media-types-only");
      localArrayList.add("ignore-unknown-character-denormalizations");
      localArrayList.add("normalize-characters");
      localArrayList.add("well-formed");
      localArrayList.add("infoset");
      localArrayList.add("disallow-doctype");
      localArrayList.add("element-content-whitespace");
      localArrayList.add("comments");
      localArrayList.add("error-handler");
      localArrayList.add("resource-resolver");
      localArrayList.add("schema-location");
      localArrayList.add("schema-type");
      fRecognizedParameters = new DOMStringListImpl(localArrayList);
    }
    return fRecognizedParameters;
  }
  
  public Document parseURI(String paramString)
    throws LSException
  {
    if (fBusy) {
      throw newInvalidStateError();
    }
    XMLInputSource localXMLInputSource = new XMLInputSource(null, paramString, null);
    try
    {
      currentThread = Thread.currentThread();
      fBusy = true;
      parse(localXMLInputSource);
      fBusy = false;
      if ((abortNow) && (currentThread.isInterrupted()))
      {
        abortNow = false;
        Thread.interrupted();
      }
    }
    catch (Exception localException)
    {
      fBusy = false;
      if ((abortNow) && (currentThread.isInterrupted())) {
        Thread.interrupted();
      }
      if (abortNow)
      {
        abortNow = false;
        restoreHandlers();
        return null;
      }
      if (localException != AbstractDOMParser.Abort.INSTANCE)
      {
        if ((!(localException instanceof XMLParseException)) && (fErrorHandler != null))
        {
          DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
          fException = localException;
          fMessage = localException.getMessage();
          fSeverity = 3;
          fErrorHandler.getErrorHandler().handleError(localDOMErrorImpl);
        }
        throw ((LSException)DOMUtil.createLSException((short)81, localException).fillInStackTrace());
      }
    }
    Document localDocument = getDocument();
    dropDocumentReferences();
    return localDocument;
  }
  
  public Document parse(LSInput paramLSInput)
    throws LSException
  {
    XMLInputSource localXMLInputSource = dom2xmlInputSource(paramLSInput);
    if (fBusy) {
      throw newInvalidStateError();
    }
    try
    {
      currentThread = Thread.currentThread();
      fBusy = true;
      parse(localXMLInputSource);
      fBusy = false;
      if ((abortNow) && (currentThread.isInterrupted()))
      {
        abortNow = false;
        Thread.interrupted();
      }
    }
    catch (Exception localException)
    {
      fBusy = false;
      if ((abortNow) && (currentThread.isInterrupted())) {
        Thread.interrupted();
      }
      if (abortNow)
      {
        abortNow = false;
        restoreHandlers();
        return null;
      }
      if (localException != AbstractDOMParser.Abort.INSTANCE)
      {
        if ((!(localException instanceof XMLParseException)) && (fErrorHandler != null))
        {
          DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
          fException = localException;
          fMessage = localException.getMessage();
          fSeverity = 3;
          fErrorHandler.getErrorHandler().handleError(localDOMErrorImpl);
        }
        throw ((LSException)DOMUtil.createLSException((short)81, localException).fillInStackTrace());
      }
    }
    Document localDocument = getDocument();
    dropDocumentReferences();
    return localDocument;
  }
  
  private void restoreHandlers()
  {
    fConfiguration.setDocumentHandler(this);
    fConfiguration.setDTDHandler(this);
    fConfiguration.setDTDContentModelHandler(this);
  }
  
  public Node parseWithContext(LSInput paramLSInput, Node paramNode, short paramShort)
    throws DOMException, LSException
  {
    throw new DOMException((short)9, "Not supported");
  }
  
  XMLInputSource dom2xmlInputSource(LSInput paramLSInput)
  {
    XMLInputSource localXMLInputSource = null;
    if (paramLSInput.getCharacterStream() != null)
    {
      localXMLInputSource = new XMLInputSource(paramLSInput.getPublicId(), paramLSInput.getSystemId(), paramLSInput.getBaseURI(), paramLSInput.getCharacterStream(), "UTF-16");
    }
    else if (paramLSInput.getByteStream() != null)
    {
      localXMLInputSource = new XMLInputSource(paramLSInput.getPublicId(), paramLSInput.getSystemId(), paramLSInput.getBaseURI(), paramLSInput.getByteStream(), paramLSInput.getEncoding());
    }
    else if ((paramLSInput.getStringData() != null) && (paramLSInput.getStringData().length() > 0))
    {
      localXMLInputSource = new XMLInputSource(paramLSInput.getPublicId(), paramLSInput.getSystemId(), paramLSInput.getBaseURI(), new StringReader(paramLSInput.getStringData()), "UTF-16");
    }
    else if (((paramLSInput.getSystemId() != null) && (paramLSInput.getSystemId().length() > 0)) || ((paramLSInput.getPublicId() != null) && (paramLSInput.getPublicId().length() > 0)))
    {
      localXMLInputSource = new XMLInputSource(paramLSInput.getPublicId(), paramLSInput.getSystemId(), paramLSInput.getBaseURI());
    }
    else
    {
      if (fErrorHandler != null)
      {
        DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
        fType = "no-input-specified";
        fMessage = "no-input-specified";
        fSeverity = 3;
        fErrorHandler.getErrorHandler().handleError(localDOMErrorImpl);
      }
      throw new LSException((short)81, "no-input-specified");
    }
    return localXMLInputSource;
  }
  
  public boolean getAsync()
  {
    return false;
  }
  
  public boolean getBusy()
  {
    return fBusy;
  }
  
  public void abort()
  {
    if (fBusy)
    {
      fBusy = false;
      if (currentThread != null)
      {
        abortNow = true;
        if (abortHandler == null) {
          abortHandler = new AbortHandler(null);
        }
        fConfiguration.setDocumentHandler(abortHandler);
        fConfiguration.setDTDHandler(abortHandler);
        fConfiguration.setDTDContentModelHandler(abortHandler);
        if (currentThread == Thread.currentThread()) {
          throw AbstractDOMParser.Abort.INSTANCE;
        }
        currentThread.interrupt();
      }
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
  {
    if ((!fNamespaceDeclarations) && (fNamespaceAware))
    {
      int i = paramXMLAttributes.getLength();
      for (int j = i - 1; j >= 0; j--) {
        if ((XMLSymbols.PREFIX_XMLNS == paramXMLAttributes.getPrefix(j)) || (XMLSymbols.PREFIX_XMLNS == paramXMLAttributes.getQName(j))) {
          paramXMLAttributes.removeAttributeAt(j);
        }
      }
    }
    super.startElement(paramQName, paramXMLAttributes, paramAugmentations);
  }
  
  private static DOMException newInvalidStateError()
  {
    String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null);
    throw new DOMException((short)11, str);
  }
  
  private static DOMException newFeatureNotSupportedError(String paramString)
  {
    String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { paramString });
    return new DOMException((short)9, str);
  }
  
  private static DOMException newFeatureNotFoundError(String paramString)
  {
    String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { paramString });
    return new DOMException((short)8, str);
  }
  
  private static DOMException newTypeMismatchError(String paramString)
  {
    String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { paramString });
    return new DOMException((short)17, str);
  }
  
  private static final class AbortHandler
    implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler
  {
    private XMLDocumentSource documentSource;
    private XMLDTDContentModelSource dtdContentSource;
    private XMLDTDSource dtdSource;
    
    private AbortHandler() {}
    
    public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endElement(QName paramQName, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startCDATA(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endCDATA(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endDocument(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource)
    {
      documentSource = paramXMLDocumentSource;
    }
    
    public XMLDocumentSource getDocumentSource()
    {
      return documentSource;
    }
    
    public void startDTD(XMLLocator paramXMLLocator, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endParameterEntity(String paramString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endExternalSubset(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startAttlist(String paramString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endAttlist(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startConditional(short paramShort, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void ignoredCharacters(XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endConditional(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endDTD(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void setDTDSource(XMLDTDSource paramXMLDTDSource)
    {
      dtdSource = paramXMLDTDSource;
    }
    
    public XMLDTDSource getDTDSource()
    {
      return dtdSource;
    }
    
    public void startContentModel(String paramString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void any(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void empty(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void startGroup(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void pcdata(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void element(String paramString, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void separator(short paramShort, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void occurrence(short paramShort, Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endGroup(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void endContentModel(Augmentations paramAugmentations)
      throws XNIException
    {
      throw AbstractDOMParser.Abort.INSTANCE;
    }
    
    public void setDTDContentModelSource(XMLDTDContentModelSource paramXMLDTDContentModelSource)
    {
      dtdContentSource = paramXMLDTDContentModelSource;
    }
    
    public XMLDTDContentModelSource getDTDContentModelSource()
    {
      return dtdContentSource;
    }
    
    AbortHandler(DOMParserImpl.1 param1)
    {
      this();
    }
  }
  
  static final class NullLSParserFilter
    implements LSParserFilter
  {
    static final NullLSParserFilter INSTANCE = new NullLSParserFilter();
    
    private NullLSParserFilter() {}
    
    public short acceptNode(Node paramNode)
    {
      return 1;
    }
    
    public int getWhatToShow()
    {
      return -1;
    }
    
    public short startElement(Element paramElement)
    {
      return 1;
    }
  }
}
