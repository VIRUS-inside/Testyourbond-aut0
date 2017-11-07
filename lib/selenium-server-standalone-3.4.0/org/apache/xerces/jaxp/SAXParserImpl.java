package org.apache.xerces.jaxp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.validation.Schema;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserImpl
  extends javax.xml.parsers.SAXParser
  implements JAXPConstants, PSVIProvider
{
  private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
  private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
  private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
  private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
  private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
  private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  private final JAXPSAXParser xmlReader = new JAXPSAXParser(this);
  private String schemaLanguage = null;
  private final Schema grammar;
  private final XMLComponent fSchemaValidator;
  private final XMLComponentManager fSchemaValidatorComponentManager;
  private final ValidationManager fSchemaValidationManager;
  private final UnparsedEntityHandler fUnparsedEntityHandler;
  private final ErrorHandler fInitErrorHandler;
  private final EntityResolver fInitEntityResolver;
  
  SAXParserImpl(SAXParserFactoryImpl paramSAXParserFactoryImpl, Hashtable paramHashtable)
    throws SAXException
  {
    this(paramSAXParserFactoryImpl, paramHashtable, false);
  }
  
  SAXParserImpl(SAXParserFactoryImpl paramSAXParserFactoryImpl, Hashtable paramHashtable, boolean paramBoolean)
    throws SAXException
  {
    xmlReader.setFeature0("http://xml.org/sax/features/namespaces", paramSAXParserFactoryImpl.isNamespaceAware());
    xmlReader.setFeature0("http://xml.org/sax/features/namespace-prefixes", !paramSAXParserFactoryImpl.isNamespaceAware());
    if (paramSAXParserFactoryImpl.isXIncludeAware()) {
      xmlReader.setFeature0("http://apache.org/xml/features/xinclude", true);
    }
    if (paramBoolean) {
      xmlReader.setProperty0("http://apache.org/xml/properties/security-manager", new SecurityManager());
    }
    setFeatures(paramHashtable);
    if (paramSAXParserFactoryImpl.isValidating())
    {
      fInitErrorHandler = new DefaultValidationErrorHandler();
      xmlReader.setErrorHandler(fInitErrorHandler);
    }
    else
    {
      fInitErrorHandler = xmlReader.getErrorHandler();
    }
    xmlReader.setFeature0("http://xml.org/sax/features/validation", paramSAXParserFactoryImpl.isValidating());
    grammar = paramSAXParserFactoryImpl.getSchema();
    if (grammar != null)
    {
      XMLParserConfiguration localXMLParserConfiguration = xmlReader.getXMLParserConfiguration();
      Object localObject = null;
      if ((grammar instanceof XSGrammarPoolContainer))
      {
        localObject = new XMLSchemaValidator();
        fSchemaValidationManager = new ValidationManager();
        fUnparsedEntityHandler = new UnparsedEntityHandler(fSchemaValidationManager);
        localXMLParserConfiguration.setDTDHandler(fUnparsedEntityHandler);
        fUnparsedEntityHandler.setDTDHandler(xmlReader);
        xmlReader.setDTDSource(fUnparsedEntityHandler);
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
      ((XMLDocumentSource)localObject).setDocumentHandler(xmlReader);
      xmlReader.setDocumentSource((XMLDocumentSource)localObject);
      fSchemaValidator = ((XMLComponent)localObject);
    }
    else
    {
      fSchemaValidationManager = null;
      fUnparsedEntityHandler = null;
      fSchemaValidatorComponentManager = null;
      fSchemaValidator = null;
    }
    fInitEntityResolver = xmlReader.getEntityResolver();
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
        xmlReader.setFeature0(str, bool);
      }
    }
  }
  
  public Parser getParser()
    throws SAXException
  {
    return xmlReader;
  }
  
  public XMLReader getXMLReader()
  {
    return xmlReader;
  }
  
  public boolean isNamespaceAware()
  {
    try
    {
      return xmlReader.getFeature("http://xml.org/sax/features/namespaces");
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
      return xmlReader.getFeature("http://xml.org/sax/features/validation");
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
      return xmlReader.getFeature("http://apache.org/xml/features/xinclude");
    }
    catch (SAXException localSAXException) {}
    return false;
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    xmlReader.setProperty(paramString, paramObject);
  }
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return xmlReader.getProperty(paramString);
  }
  
  public void parse(InputSource paramInputSource, DefaultHandler paramDefaultHandler)
    throws SAXException, IOException
  {
    if (paramInputSource == null) {
      throw new IllegalArgumentException();
    }
    if (paramDefaultHandler != null)
    {
      xmlReader.setContentHandler(paramDefaultHandler);
      xmlReader.setEntityResolver(paramDefaultHandler);
      xmlReader.setErrorHandler(paramDefaultHandler);
      xmlReader.setDTDHandler(paramDefaultHandler);
      xmlReader.setDocumentHandler(null);
    }
    xmlReader.parse(paramInputSource);
  }
  
  public void parse(InputSource paramInputSource, HandlerBase paramHandlerBase)
    throws SAXException, IOException
  {
    if (paramInputSource == null) {
      throw new IllegalArgumentException();
    }
    if (paramHandlerBase != null)
    {
      xmlReader.setDocumentHandler(paramHandlerBase);
      xmlReader.setEntityResolver(paramHandlerBase);
      xmlReader.setErrorHandler(paramHandlerBase);
      xmlReader.setDTDHandler(paramHandlerBase);
      xmlReader.setContentHandler(null);
    }
    xmlReader.parse(paramInputSource);
  }
  
  public Schema getSchema()
  {
    return grammar;
  }
  
  public void reset()
  {
    try
    {
      xmlReader.restoreInitState();
    }
    catch (SAXException localSAXException) {}
    xmlReader.setContentHandler(null);
    xmlReader.setDTDHandler(null);
    if (xmlReader.getErrorHandler() != fInitErrorHandler) {
      xmlReader.setErrorHandler(fInitErrorHandler);
    }
    if (xmlReader.getEntityResolver() != fInitEntityResolver) {
      xmlReader.setEntityResolver(fInitEntityResolver);
    }
  }
  
  public ElementPSVI getElementPSVI()
  {
    return xmlReader.getElementPSVI();
  }
  
  public AttributePSVI getAttributePSVI(int paramInt)
  {
    return xmlReader.getAttributePSVI(paramInt);
  }
  
  public AttributePSVI getAttributePSVIByName(String paramString1, String paramString2)
  {
    return xmlReader.getAttributePSVIByName(paramString1, paramString2);
  }
  
  public static class JAXPSAXParser
    extends org.apache.xerces.parsers.SAXParser
  {
    private final HashMap fInitFeatures = new HashMap();
    private final HashMap fInitProperties = new HashMap();
    private final SAXParserImpl fSAXParser;
    
    public JAXPSAXParser()
    {
      this(null);
    }
    
    JAXPSAXParser(SAXParserImpl paramSAXParserImpl)
    {
      fSAXParser = paramSAXParserImpl;
    }
    
    public synchronized void setFeature(String paramString, boolean paramBoolean)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (paramString.equals("http://javax.xml.XMLConstants/feature/secure-processing"))
      {
        try
        {
          setProperty("http://apache.org/xml/properties/security-manager", paramBoolean ? new SecurityManager() : null);
        }
        catch (SAXNotRecognizedException localSAXNotRecognizedException)
        {
          if (paramBoolean) {
            throw localSAXNotRecognizedException;
          }
        }
        catch (SAXNotSupportedException localSAXNotSupportedException)
        {
          if (paramBoolean) {
            throw localSAXNotSupportedException;
          }
        }
        return;
      }
      if (!fInitFeatures.containsKey(paramString))
      {
        boolean bool = super.getFeature(paramString);
        fInitFeatures.put(paramString, bool ? Boolean.TRUE : Boolean.FALSE);
      }
      if ((fSAXParser != null) && (fSAXParser.fSchemaValidator != null)) {
        setSchemaValidatorFeature(paramString, paramBoolean);
      }
      super.setFeature(paramString, paramBoolean);
    }
    
    public synchronized boolean getFeature(String paramString)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (paramString.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
        try
        {
          return super.getProperty("http://apache.org/xml/properties/security-manager") != null;
        }
        catch (SAXException localSAXException)
        {
          return false;
        }
      }
      return super.getFeature(paramString);
    }
    
    public synchronized void setProperty(String paramString, Object paramObject)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (fSAXParser != null)
      {
        if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(paramString))
        {
          if (fSAXParser.grammar != null) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "schema-already-specified", new Object[] { paramString }));
          }
          if ("http://www.w3.org/2001/XMLSchema".equals(paramObject))
          {
            if (fSAXParser.isValidating())
            {
              fSAXParser.schemaLanguage = "http://www.w3.org/2001/XMLSchema";
              setFeature("http://apache.org/xml/features/validation/schema", true);
              if (!fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
                fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaLanguage", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage"));
              }
              super.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            }
          }
          else if (paramObject == null)
          {
            fSAXParser.schemaLanguage = null;
            setFeature("http://apache.org/xml/features/validation/schema", false);
          }
          else
          {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "schema-not-supported", null));
          }
          return;
        }
        if ("http://java.sun.com/xml/jaxp/properties/schemaSource".equals(paramString))
        {
          if (fSAXParser.grammar != null) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "schema-already-specified", new Object[] { paramString }));
          }
          String str = (String)getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
          if ((str != null) && ("http://www.w3.org/2001/XMLSchema".equals(str)))
          {
            if (!fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
              fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaSource", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource"));
            }
            super.setProperty(paramString, paramObject);
          }
          else
          {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "jaxp-order-not-supported", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://java.sun.com/xml/jaxp/properties/schemaSource" }));
          }
          return;
        }
      }
      if (!fInitProperties.containsKey(paramString)) {
        fInitProperties.put(paramString, super.getProperty(paramString));
      }
      if ((fSAXParser != null) && (fSAXParser.fSchemaValidator != null)) {
        setSchemaValidatorProperty(paramString, paramObject);
      }
      super.setProperty(paramString, paramObject);
    }
    
    public synchronized Object getProperty(String paramString)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if ((fSAXParser != null) && ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(paramString))) {
        return fSAXParser.schemaLanguage;
      }
      return super.getProperty(paramString);
    }
    
    synchronized void restoreInitState()
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
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
    
    public void parse(InputSource paramInputSource)
      throws SAXException, IOException
    {
      if ((fSAXParser != null) && (fSAXParser.fSchemaValidator != null))
      {
        if (fSAXParser.fSchemaValidationManager != null)
        {
          fSAXParser.fSchemaValidationManager.reset();
          fSAXParser.fUnparsedEntityHandler.reset();
        }
        resetSchemaValidator();
      }
      super.parse(paramInputSource);
    }
    
    public void parse(String paramString)
      throws SAXException, IOException
    {
      if ((fSAXParser != null) && (fSAXParser.fSchemaValidator != null))
      {
        if (fSAXParser.fSchemaValidationManager != null)
        {
          fSAXParser.fSchemaValidationManager.reset();
          fSAXParser.fUnparsedEntityHandler.reset();
        }
        resetSchemaValidator();
      }
      super.parse(paramString);
    }
    
    XMLParserConfiguration getXMLParserConfiguration()
    {
      return fConfiguration;
    }
    
    void setFeature0(String paramString, boolean paramBoolean)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      super.setFeature(paramString, paramBoolean);
    }
    
    boolean getFeature0(String paramString)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      return super.getFeature(paramString);
    }
    
    void setProperty0(String paramString, Object paramObject)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      super.setProperty(paramString, paramObject);
    }
    
    Object getProperty0(String paramString)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      return super.getProperty(paramString);
    }
    
    private void setSchemaValidatorFeature(String paramString, boolean paramBoolean)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      try
      {
        fSAXParser.fSchemaValidator.setFeature(paramString, paramBoolean);
      }
      catch (XMLConfigurationException localXMLConfigurationException)
      {
        String str = localXMLConfigurationException.getIdentifier();
        if (localXMLConfigurationException.getType() == 0) {
          throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-not-recognized", new Object[] { str }));
        }
        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-not-supported", new Object[] { str }));
      }
    }
    
    private void setSchemaValidatorProperty(String paramString, Object paramObject)
      throws SAXNotRecognizedException, SAXNotSupportedException
    {
      try
      {
        fSAXParser.fSchemaValidator.setProperty(paramString, paramObject);
      }
      catch (XMLConfigurationException localXMLConfigurationException)
      {
        String str = localXMLConfigurationException.getIdentifier();
        if (localXMLConfigurationException.getType() == 0) {
          throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-recognized", new Object[] { str }));
        }
        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-supported", new Object[] { str }));
      }
    }
    
    private void resetSchemaValidator()
      throws SAXException
    {
      try
      {
        fSAXParser.fSchemaValidator.reset(fSAXParser.fSchemaValidatorComponentManager);
      }
      catch (XMLConfigurationException localXMLConfigurationException)
      {
        throw new SAXException(localXMLConfigurationException);
      }
    }
  }
}
