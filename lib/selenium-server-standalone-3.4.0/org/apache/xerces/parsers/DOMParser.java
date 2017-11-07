package org.apache.xerces.parsers;

import java.io.CharConversionException;
import java.io.IOException;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.EntityResolver2Wrapper;
import org.apache.xerces.util.EntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.LocatorImpl;

public class DOMParser
  extends AbstractDOMParser
{
  protected static final String USE_ENTITY_RESOLVER2 = "http://xml.org/sax/features/use-entity-resolver2";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/grammar-pool" };
  protected boolean fUseEntityResolver2 = true;
  
  public DOMParser(XMLParserConfiguration paramXMLParserConfiguration)
  {
    super(paramXMLParserConfiguration);
  }
  
  public DOMParser()
  {
    this(null, null);
  }
  
  public DOMParser(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null);
  }
  
  public DOMParser(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    super((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
    fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
    if (paramSymbolTable != null) {
      fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", paramSymbolTable);
    }
    if (paramXMLGrammarPool != null) {
      fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", paramXMLGrammarPool);
    }
  }
  
  public void parse(String paramString)
    throws SAXException, IOException
  {
    XMLInputSource localXMLInputSource = new XMLInputSource(null, paramString, null);
    try
    {
      parse(localXMLInputSource);
    }
    catch (XMLParseException localXMLParseException)
    {
      Exception localException = localXMLParseException.getException();
      if ((localException == null) || ((localException instanceof CharConversionException)))
      {
        localObject = new LocatorImpl();
        ((LocatorImpl)localObject).setPublicId(localXMLParseException.getPublicId());
        ((LocatorImpl)localObject).setSystemId(localXMLParseException.getExpandedSystemId());
        ((LocatorImpl)localObject).setLineNumber(localXMLParseException.getLineNumber());
        ((LocatorImpl)localObject).setColumnNumber(localXMLParseException.getColumnNumber());
        throw (localException == null ? new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject) : new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject, localException));
      }
      if ((localException instanceof SAXException)) {
        throw ((SAXException)localException);
      }
      if ((localException instanceof IOException)) {
        throw ((IOException)localException);
      }
      throw new SAXException(localException);
    }
    catch (XNIException localXNIException)
    {
      localXNIException.printStackTrace();
      Object localObject = localXNIException.getException();
      if (localObject == null) {
        throw new SAXException(localXNIException.getMessage());
      }
      if ((localObject instanceof SAXException)) {
        throw ((SAXException)localObject);
      }
      if ((localObject instanceof IOException)) {
        throw ((IOException)localObject);
      }
      throw new SAXException((Exception)localObject);
    }
  }
  
  public void parse(InputSource paramInputSource)
    throws SAXException, IOException
  {
    try
    {
      XMLInputSource localXMLInputSource = new XMLInputSource(paramInputSource.getPublicId(), paramInputSource.getSystemId(), null);
      localXMLInputSource.setByteStream(paramInputSource.getByteStream());
      localXMLInputSource.setCharacterStream(paramInputSource.getCharacterStream());
      localXMLInputSource.setEncoding(paramInputSource.getEncoding());
      parse(localXMLInputSource);
    }
    catch (XMLParseException localXMLParseException)
    {
      Exception localException = localXMLParseException.getException();
      if ((localException == null) || ((localException instanceof CharConversionException)))
      {
        localObject = new LocatorImpl();
        ((LocatorImpl)localObject).setPublicId(localXMLParseException.getPublicId());
        ((LocatorImpl)localObject).setSystemId(localXMLParseException.getExpandedSystemId());
        ((LocatorImpl)localObject).setLineNumber(localXMLParseException.getLineNumber());
        ((LocatorImpl)localObject).setColumnNumber(localXMLParseException.getColumnNumber());
        throw (localException == null ? new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject) : new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject, localException));
      }
      if ((localException instanceof SAXException)) {
        throw ((SAXException)localException);
      }
      if ((localException instanceof IOException)) {
        throw ((IOException)localException);
      }
      throw new SAXException(localException);
    }
    catch (XNIException localXNIException)
    {
      Object localObject = localXNIException.getException();
      if (localObject == null) {
        throw new SAXException(localXNIException.getMessage());
      }
      if ((localObject instanceof SAXException)) {
        throw ((SAXException)localObject);
      }
      if ((localObject instanceof IOException)) {
        throw ((IOException)localObject);
      }
      throw new SAXException((Exception)localObject);
    }
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    try
    {
      XMLEntityResolver localXMLEntityResolver = (XMLEntityResolver)fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
      Object localObject;
      if ((fUseEntityResolver2) && ((paramEntityResolver instanceof EntityResolver2)))
      {
        if ((localXMLEntityResolver instanceof EntityResolver2Wrapper))
        {
          localObject = (EntityResolver2Wrapper)localXMLEntityResolver;
          ((EntityResolver2Wrapper)localObject).setEntityResolver((EntityResolver2)paramEntityResolver);
        }
        else
        {
          fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)paramEntityResolver));
        }
      }
      else if ((localXMLEntityResolver instanceof EntityResolverWrapper))
      {
        localObject = (EntityResolverWrapper)localXMLEntityResolver;
        ((EntityResolverWrapper)localObject).setEntityResolver(paramEntityResolver);
      }
      else
      {
        fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(paramEntityResolver));
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
  }
  
  public EntityResolver getEntityResolver()
  {
    Object localObject = null;
    try
    {
      XMLEntityResolver localXMLEntityResolver = (XMLEntityResolver)fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
      if (localXMLEntityResolver != null) {
        if ((localXMLEntityResolver instanceof EntityResolverWrapper)) {
          localObject = ((EntityResolverWrapper)localXMLEntityResolver).getEntityResolver();
        } else if ((localXMLEntityResolver instanceof EntityResolver2Wrapper)) {
          localObject = ((EntityResolver2Wrapper)localXMLEntityResolver).getEntityResolver();
        }
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
    return localObject;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    try
    {
      XMLErrorHandler localXMLErrorHandler = (XMLErrorHandler)fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
      if ((localXMLErrorHandler instanceof ErrorHandlerWrapper))
      {
        ErrorHandlerWrapper localErrorHandlerWrapper = (ErrorHandlerWrapper)localXMLErrorHandler;
        localErrorHandlerWrapper.setErrorHandler(paramErrorHandler);
      }
      else
      {
        fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(paramErrorHandler));
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
  }
  
  public ErrorHandler getErrorHandler()
  {
    ErrorHandler localErrorHandler = null;
    try
    {
      XMLErrorHandler localXMLErrorHandler = (XMLErrorHandler)fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
      if ((localXMLErrorHandler != null) && ((localXMLErrorHandler instanceof ErrorHandlerWrapper))) {
        localErrorHandler = ((ErrorHandlerWrapper)localXMLErrorHandler).getErrorHandler();
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
    return localErrorHandler;
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      if (paramString.equals("http://xml.org/sax/features/use-entity-resolver2"))
      {
        if (paramBoolean != fUseEntityResolver2)
        {
          fUseEntityResolver2 = paramBoolean;
          setEntityResolver(getEntityResolver());
        }
        return;
      }
      fConfiguration.setFeature(paramString, paramBoolean);
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
  
  public boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      if (paramString.equals("http://xml.org/sax/features/use-entity-resolver2")) {
        return fUseEntityResolver2;
      }
      return fConfiguration.getFeature(paramString);
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
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      fConfiguration.setProperty(paramString, paramObject);
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
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString.equals("http://apache.org/xml/properties/dom/current-element-node"))
    {
      boolean bool = false;
      try
      {
        bool = getFeature("http://apache.org/xml/features/dom/defer-node-expansion");
      }
      catch (XMLConfigurationException localXMLConfigurationException2) {}
      if (bool) {
        throw new SAXNotSupportedException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "CannotQueryDeferredNode", null));
      }
      return (fCurrentNode != null) && (fCurrentNode.getNodeType() == 1) ? fCurrentNode : null;
    }
    try
    {
      return fConfiguration.getProperty(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException1)
    {
      String str = localXMLConfigurationException1.getIdentifier();
      if (localXMLConfigurationException1.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  public XMLParserConfiguration getXMLParserConfiguration()
  {
    return fConfiguration;
  }
}
