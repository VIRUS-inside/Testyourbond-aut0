package org.apache.xerces.jaxp;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.validation.Schema;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class DocumentBuilderImpl
  extends DocumentBuilder
  implements JAXPConstants
{
  private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
  private static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
  private static final String CREATE_ENTITY_REF_NODES_FEATURE = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
  private static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
  private static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
  private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
  private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
  private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
  private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  private final DOMParser domParser = new DOMParser();
  private final Schema grammar;
  private final XMLComponent fSchemaValidator;
  private final XMLComponentManager fSchemaValidatorComponentManager;
  private final ValidationManager fSchemaValidationManager;
  private final UnparsedEntityHandler fUnparsedEntityHandler;
  private final ErrorHandler fInitErrorHandler;
  private final EntityResolver fInitEntityResolver;
  
  DocumentBuilderImpl(DocumentBuilderFactoryImpl paramDocumentBuilderFactoryImpl, Hashtable paramHashtable1, Hashtable paramHashtable2)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    this(paramDocumentBuilderFactoryImpl, paramHashtable1, paramHashtable2, false);
  }
  
  DocumentBuilderImpl(DocumentBuilderFactoryImpl paramDocumentBuilderFactoryImpl, Hashtable paramHashtable1, Hashtable paramHashtable2, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramDocumentBuilderFactoryImpl.isValidating())
    {
      fInitErrorHandler = new DefaultValidationErrorHandler();
      setErrorHandler(fInitErrorHandler);
    }
    else
    {
      fInitErrorHandler = domParser.getErrorHandler();
    }
    domParser.setFeature("http://xml.org/sax/features/validation", paramDocumentBuilderFactoryImpl.isValidating());
    domParser.setFeature("http://xml.org/sax/features/namespaces", paramDocumentBuilderFactoryImpl.isNamespaceAware());
    domParser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", !paramDocumentBuilderFactoryImpl.isIgnoringElementContentWhitespace());
    domParser.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", !paramDocumentBuilderFactoryImpl.isExpandEntityReferences());
    domParser.setFeature("http://apache.org/xml/features/include-comments", !paramDocumentBuilderFactoryImpl.isIgnoringComments());
    domParser.setFeature("http://apache.org/xml/features/create-cdata-nodes", !paramDocumentBuilderFactoryImpl.isCoalescing());
    if (paramDocumentBuilderFactoryImpl.isXIncludeAware()) {
      domParser.setFeature("http://apache.org/xml/features/xinclude", true);
    }
    if (paramBoolean) {
      domParser.setProperty("http://apache.org/xml/properties/security-manager", new SecurityManager());
    }
    grammar = paramDocumentBuilderFactoryImpl.getSchema();
    if (grammar != null)
    {
      XMLParserConfiguration localXMLParserConfiguration = domParser.getXMLParserConfiguration();
      Object localObject = null;
      if ((grammar instanceof XSGrammarPoolContainer))
      {
        localObject = new XMLSchemaValidator();
        fSchemaValidationManager = new ValidationManager();
        fUnparsedEntityHandler = new UnparsedEntityHandler(fSchemaValidationManager);
        localXMLParserConfiguration.setDTDHandler(fUnparsedEntityHandler);
        fUnparsedEntityHandler.setDTDHandler(domParser);
        domParser.setDTDSource(fUnparsedEntityHandler);
        fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(localXMLParserConfiguration, (XSGrammarPoolContainer)grammar, fSchemaValidationManager);
      }
      else
      {
        localObject = new JAXPValidatorComponent(grammar.newValidatorHandler());
        fSchemaValidationManager = null;
        fUnparsedEntityHandler = null;
        fSchemaValidatorComponentManager = localXMLParserConfiguration;
      }
      localXMLParserConfiguration.addRecognizedFeatures(((XMLComponent)localObject).getRecognizedFeatures());
      localXMLParserConfiguration.addRecognizedProperties(((XMLComponent)localObject).getRecognizedProperties());
      localXMLParserConfiguration.setDocumentHandler((XMLDocumentHandler)localObject);
      ((XMLDocumentSource)localObject).setDocumentHandler(domParser);
      domParser.setDocumentSource((XMLDocumentSource)localObject);
      fSchemaValidator = ((XMLComponent)localObject);
    }
    else
    {
      fSchemaValidationManager = null;
      fUnparsedEntityHandler = null;
      fSchemaValidatorComponentManager = null;
      fSchemaValidator = null;
    }
    setFeatures(paramHashtable2);
    setDocumentBuilderFactoryAttributes(paramHashtable1);
    fInitEntityResolver = domParser.getEntityResolver();
  }
  
  private void setFeatures(Hashtable paramHashtable)
    throws SAXNotSupportedException, SAXNotRecognizedException
  {
    if (paramHashtable != null)
    {
      Iterator localIterator = paramHashtable.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str = (String)localEntry.getKey();
        boolean bool = ((Boolean)localEntry.getValue()).booleanValue();
        domParser.setFeature(str, bool);
      }
    }
  }
  
  private void setDocumentBuilderFactoryAttributes(Hashtable paramHashtable)
    throws SAXNotSupportedException, SAXNotRecognizedException
  {
    if (paramHashtable == null) {
      return;
    }
    Iterator localIterator = paramHashtable.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str1 = (String)localEntry.getKey();
      Object localObject = localEntry.getValue();
      if ((localObject instanceof Boolean)) {
        domParser.setFeature(str1, ((Boolean)localObject).booleanValue());
      } else if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(str1))
      {
        if (("http://www.w3.org/2001/XMLSchema".equals(localObject)) && (isValidating()))
        {
          domParser.setFeature("http://apache.org/xml/features/validation/schema", true);
          domParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        }
      }
      else if ("http://java.sun.com/xml/jaxp/properties/schemaSource".equals(str1))
      {
        if (isValidating())
        {
          String str2 = (String)paramHashtable.get("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
          if ((str2 != null) && ("http://www.w3.org/2001/XMLSchema".equals(str2))) {
            domParser.setProperty(str1, localObject);
          } else {
            throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "jaxp-order-not-supported", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://java.sun.com/xml/jaxp/properties/schemaSource" }));
          }
        }
      }
      else {
        domParser.setProperty(str1, localObject);
      }
    }
  }
  
  public Document newDocument()
  {
    return new DocumentImpl();
  }
  
  public DOMImplementation getDOMImplementation()
  {
    return DOMImplementationImpl.getDOMImplementation();
  }
  
  public Document parse(InputSource paramInputSource)
    throws SAXException, IOException
  {
    if (paramInputSource == null) {
      throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "jaxp-null-input-source", null));
    }
    if (fSchemaValidator != null)
    {
      if (fSchemaValidationManager != null)
      {
        fSchemaValidationManager.reset();
        fUnparsedEntityHandler.reset();
      }
      resetSchemaValidator();
    }
    domParser.parse(paramInputSource);
    Document localDocument = domParser.getDocument();
    domParser.dropDocumentReferences();
    return localDocument;
  }
  
  public boolean isNamespaceAware()
  {
    try
    {
      return domParser.getFeature("http://xml.org/sax/features/namespaces");
    }
    catch (SAXException localSAXException)
    {
      throw new IllegalStateException(localSAXException.getMessage());
    }
  }
  
  public boolean isValidating()
  {
    try
    {
      return domParser.getFeature("http://xml.org/sax/features/validation");
    }
    catch (SAXException localSAXException)
    {
      throw new IllegalStateException(localSAXException.getMessage());
    }
  }
  
  public boolean isXIncludeAware()
  {
    try
    {
      return domParser.getFeature("http://apache.org/xml/features/xinclude");
    }
    catch (SAXException localSAXException) {}
    return false;
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    domParser.setEntityResolver(paramEntityResolver);
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    domParser.setErrorHandler(paramErrorHandler);
  }
  
  public Schema getSchema()
  {
    return grammar;
  }
  
  public void reset()
  {
    if (domParser.getErrorHandler() != fInitErrorHandler) {
      domParser.setErrorHandler(fInitErrorHandler);
    }
    if (domParser.getEntityResolver() != fInitEntityResolver) {
      domParser.setEntityResolver(fInitEntityResolver);
    }
  }
  
  DOMParser getDOMParser()
  {
    return domParser;
  }
  
  private void resetSchemaValidator()
    throws SAXException
  {
    try
    {
      fSchemaValidator.reset(fSchemaValidatorComponentManager);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      throw new SAXException(localXMLConfigurationException);
    }
  }
}
