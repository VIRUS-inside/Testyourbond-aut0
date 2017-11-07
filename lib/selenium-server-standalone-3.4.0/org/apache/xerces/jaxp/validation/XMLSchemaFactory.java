package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.XMLEventReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMInputSource;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXInputSource;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.StAXInputSource;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

public final class XMLSchemaFactory
  extends SchemaFactory
{
  private static final String JAXP_SOURCE_FEATURE_PREFIX = "http://javax.xml.transform";
  private static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
  private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
  private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
  private final XMLSchemaLoader fXMLSchemaLoader = new XMLSchemaLoader();
  private ErrorHandler fErrorHandler;
  private LSResourceResolver fLSResourceResolver;
  private final DOMEntityResolverWrapper fDOMEntityResolverWrapper = new DOMEntityResolverWrapper();
  private final ErrorHandlerWrapper fErrorHandlerWrapper = new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
  private SecurityManager fSecurityManager;
  private final XMLGrammarPoolWrapper fXMLGrammarPoolWrapper = new XMLGrammarPoolWrapper();
  private boolean fUseGrammarPoolOnly;
  
  public XMLSchemaFactory()
  {
    fXMLSchemaLoader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
    fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", fXMLGrammarPoolWrapper);
    fXMLSchemaLoader.setEntityResolver(fDOMEntityResolverWrapper);
    fXMLSchemaLoader.setErrorHandler(fErrorHandlerWrapper);
    fUseGrammarPoolOnly = true;
  }
  
  public boolean isSchemaLanguageSupported(String paramString)
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "SchemaLanguageNull", null));
    }
    if (paramString.length() == 0) {
      throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "SchemaLanguageLengthZero", null));
    }
    return paramString.equals("http://www.w3.org/2001/XMLSchema");
  }
  
  public LSResourceResolver getResourceResolver()
  {
    return fLSResourceResolver;
  }
  
  public void setResourceResolver(LSResourceResolver paramLSResourceResolver)
  {
    fLSResourceResolver = paramLSResourceResolver;
    fDOMEntityResolverWrapper.setEntityResolver(paramLSResourceResolver);
    fXMLSchemaLoader.setEntityResolver(fDOMEntityResolverWrapper);
  }
  
  public ErrorHandler getErrorHandler()
  {
    return fErrorHandler;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    fErrorHandler = paramErrorHandler;
    fErrorHandlerWrapper.setErrorHandler(paramErrorHandler != null ? paramErrorHandler : DraconianErrorHandler.getInstance());
    fXMLSchemaLoader.setErrorHandler(fErrorHandlerWrapper);
  }
  
  public Schema newSchema(Source[] paramArrayOfSource)
    throws SAXException
  {
    XMLGrammarPoolImplExtension localXMLGrammarPoolImplExtension = new XMLGrammarPoolImplExtension();
    fXMLGrammarPoolWrapper.setGrammarPool(localXMLGrammarPoolImplExtension);
    XMLInputSource[] arrayOfXMLInputSource = new XMLInputSource[paramArrayOfSource.length];
    Object localObject3;
    for (int i = 0; i < paramArrayOfSource.length; i++)
    {
      Source localSource = paramArrayOfSource[i];
      Object localObject1;
      String str;
      if ((localSource instanceof StreamSource))
      {
        localObject1 = (StreamSource)localSource;
        localObject3 = ((StreamSource)localObject1).getPublicId();
        str = ((StreamSource)localObject1).getSystemId();
        InputStream localInputStream = ((StreamSource)localObject1).getInputStream();
        Reader localReader = ((StreamSource)localObject1).getReader();
        XMLInputSource localXMLInputSource = new XMLInputSource((String)localObject3, str, null);
        localXMLInputSource.setByteStream(localInputStream);
        localXMLInputSource.setCharacterStream(localReader);
        arrayOfXMLInputSource[i] = localXMLInputSource;
      }
      else if ((localSource instanceof SAXSource))
      {
        localObject1 = (SAXSource)localSource;
        localObject3 = ((SAXSource)localObject1).getInputSource();
        if (localObject3 == null) {
          throw new SAXException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "SAXSourceNullInputSource", null));
        }
        arrayOfXMLInputSource[i] = new SAXInputSource(((SAXSource)localObject1).getXMLReader(), (InputSource)localObject3);
      }
      else if ((localSource instanceof DOMSource))
      {
        localObject1 = (DOMSource)localSource;
        localObject3 = ((DOMSource)localObject1).getNode();
        str = ((DOMSource)localObject1).getSystemId();
        arrayOfXMLInputSource[i] = new DOMInputSource((Node)localObject3, str);
      }
      else if ((localSource instanceof StAXSource))
      {
        localObject1 = (StAXSource)localSource;
        localObject3 = ((StAXSource)localObject1).getXMLEventReader();
        if (localObject3 != null) {
          arrayOfXMLInputSource[i] = new StAXInputSource((XMLEventReader)localObject3);
        } else {
          arrayOfXMLInputSource[i] = new StAXInputSource(((StAXSource)localObject1).getXMLStreamReader());
        }
      }
      else
      {
        if (localSource == null) {
          throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "SchemaSourceArrayMemberNull", null));
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "SchemaFactorySourceUnrecognized", new Object[] { localSource.getClass().getName() }));
      }
    }
    try
    {
      fXMLSchemaLoader.loadGrammar(arrayOfXMLInputSource);
    }
    catch (XNIException localXNIException)
    {
      throw Util.toSAXException(localXNIException);
    }
    catch (IOException localIOException)
    {
      localObject3 = new SAXParseException(localIOException.getMessage(), null, localIOException);
      if (fErrorHandler != null) {
        fErrorHandler.error((SAXParseException)localObject3);
      }
      throw ((Throwable)localObject3);
    }
    fXMLGrammarPoolWrapper.setGrammarPool(null);
    int j = localXMLGrammarPoolImplExtension.getGrammarCount();
    Object localObject2 = null;
    if (fUseGrammarPoolOnly)
    {
      if (j > 1)
      {
        localObject2 = new XMLSchema(new ReadOnlyGrammarPool(localXMLGrammarPoolImplExtension));
      }
      else if (j == 1)
      {
        localObject3 = localXMLGrammarPoolImplExtension.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
        localObject2 = new SimpleXMLSchema(localObject3[0]);
      }
      else
      {
        localObject2 = new EmptyXMLSchema();
      }
    }
    else {
      localObject2 = new XMLSchema(new ReadOnlyGrammarPool(localXMLGrammarPoolImplExtension), false);
    }
    propagateFeatures((AbstractXMLSchema)localObject2);
    return localObject2;
  }
  
  public Schema newSchema()
    throws SAXException
  {
    WeakReferenceXMLSchema localWeakReferenceXMLSchema = new WeakReferenceXMLSchema();
    propagateFeatures(localWeakReferenceXMLSchema);
    return localWeakReferenceXMLSchema;
  }
  
  public Schema newSchema(XMLGrammarPool paramXMLGrammarPool)
    throws SAXException
  {
    XMLSchema localXMLSchema = fUseGrammarPoolOnly ? new XMLSchema(new ReadOnlyGrammarPool(paramXMLGrammarPool)) : new XMLSchema(paramXMLGrammarPool, false);
    propagateFeatures(localXMLSchema);
    return localXMLSchema;
  }
  
  public boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
    }
    if ((paramString.startsWith("http://javax.xml.transform")) && ((paramString.equals("http://javax.xml.transform.stream.StreamSource/feature")) || (paramString.equals("http://javax.xml.transform.sax.SAXSource/feature")) || (paramString.equals("http://javax.xml.transform.dom.DOMSource/feature")) || (paramString.equals("http://javax.xml.transform.stax.StAXSource/feature")))) {
      return true;
    }
    if (paramString.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
      return fSecurityManager != null;
    }
    if (paramString.equals("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only")) {
      return fUseGrammarPoolOnly;
    }
    try
    {
      return fXMLSchemaLoader.getFeature(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[] { str }));
    }
  }
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
    }
    if (paramString.equals("http://apache.org/xml/properties/security-manager")) {
      return fSecurityManager;
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[] { paramString }));
    }
    try
    {
      return fXMLSchemaLoader.getProperty(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
    }
    if ((paramString.startsWith("http://javax.xml.transform")) && ((paramString.equals("http://javax.xml.transform.stream.StreamSource/feature")) || (paramString.equals("http://javax.xml.transform.sax.SAXSource/feature")) || (paramString.equals("http://javax.xml.transform.dom.DOMSource/feature")) || (paramString.equals("http://javax.xml.transform.stax.StAXSource/feature")))) {
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "feature-read-only", new Object[] { paramString }));
    }
    if (paramString.equals("http://javax.xml.XMLConstants/feature/secure-processing"))
    {
      fSecurityManager = (paramBoolean ? new SecurityManager() : null);
      fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", fSecurityManager);
      return;
    }
    if (paramString.equals("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only"))
    {
      fUseGrammarPoolOnly = paramBoolean;
      return;
    }
    try
    {
      fXMLSchemaLoader.setFeature(paramString, paramBoolean);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[] { str }));
    }
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString == null) {
      throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
    }
    if (paramString.equals("http://apache.org/xml/properties/security-manager"))
    {
      fSecurityManager = ((SecurityManager)paramObject);
      fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", fSecurityManager);
      return;
    }
    if (paramString.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[] { paramString }));
    }
    try
    {
      fXMLSchemaLoader.setProperty(paramString, paramObject);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  private void propagateFeatures(AbstractXMLSchema paramAbstractXMLSchema)
  {
    paramAbstractXMLSchema.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", fSecurityManager != null);
    String[] arrayOfString = fXMLSchemaLoader.getRecognizedFeatures();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      boolean bool = fXMLSchemaLoader.getFeature(arrayOfString[i]);
      paramAbstractXMLSchema.setFeature(arrayOfString[i], bool);
    }
  }
  
  static class XMLGrammarPoolWrapper
    implements XMLGrammarPool
  {
    private XMLGrammarPool fGrammarPool;
    
    XMLGrammarPoolWrapper() {}
    
    public Grammar[] retrieveInitialGrammarSet(String paramString)
    {
      return fGrammarPool.retrieveInitialGrammarSet(paramString);
    }
    
    public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar)
    {
      fGrammarPool.cacheGrammars(paramString, paramArrayOfGrammar);
    }
    
    public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
    {
      return fGrammarPool.retrieveGrammar(paramXMLGrammarDescription);
    }
    
    public void lockPool()
    {
      fGrammarPool.lockPool();
    }
    
    public void unlockPool()
    {
      fGrammarPool.unlockPool();
    }
    
    public void clear()
    {
      fGrammarPool.clear();
    }
    
    void setGrammarPool(XMLGrammarPool paramXMLGrammarPool)
    {
      fGrammarPool = paramXMLGrammarPool;
    }
    
    XMLGrammarPool getGrammarPool()
    {
      return fGrammarPool;
    }
  }
  
  static class XMLGrammarPoolImplExtension
    extends XMLGrammarPoolImpl
  {
    public XMLGrammarPoolImplExtension() {}
    
    public XMLGrammarPoolImplExtension(int paramInt)
    {
      super();
    }
    
    int getGrammarCount()
    {
      return fGrammarCount;
    }
  }
}
