package javax.xml.transform;

public abstract class TransformerFactory
{
  protected TransformerFactory() {}
  
  public static TransformerFactory newInstance()
    throws TransformerFactoryConfigurationError
  {
    try
    {
      return (TransformerFactory)FactoryFinder.find("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new TransformerFactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public static TransformerFactory newInstance(String paramString, ClassLoader paramClassLoader)
    throws TransformerFactoryConfigurationError
  {
    if (paramString == null) {
      throw new TransformerFactoryConfigurationError("factoryClassName cannot be null.");
    }
    if (paramClassLoader == null) {
      paramClassLoader = SecuritySupport.getContextClassLoader();
    }
    try
    {
      return (TransformerFactory)FactoryFinder.newInstance(paramString, paramClassLoader, false);
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new TransformerFactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public abstract Transformer newTransformer(Source paramSource)
    throws TransformerConfigurationException;
  
  public abstract Transformer newTransformer()
    throws TransformerConfigurationException;
  
  public abstract Templates newTemplates(Source paramSource)
    throws TransformerConfigurationException;
  
  public abstract Source getAssociatedStylesheet(Source paramSource, String paramString1, String paramString2, String paramString3)
    throws TransformerConfigurationException;
  
  public abstract void setURIResolver(URIResolver paramURIResolver);
  
  public abstract URIResolver getURIResolver();
  
  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws TransformerConfigurationException;
  
  public abstract boolean getFeature(String paramString);
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract Object getAttribute(String paramString);
  
  public abstract void setErrorListener(ErrorListener paramErrorListener);
  
  public abstract ErrorListener getErrorListener();
}
