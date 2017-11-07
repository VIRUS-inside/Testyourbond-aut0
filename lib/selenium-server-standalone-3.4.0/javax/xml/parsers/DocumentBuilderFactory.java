package javax.xml.parsers;

import javax.xml.validation.Schema;

public abstract class DocumentBuilderFactory
{
  private boolean validating = false;
  private boolean namespaceAware = false;
  private boolean whitespace = false;
  private boolean expandEntityRef = true;
  private boolean ignoreComments = false;
  private boolean coalescing = false;
  
  protected DocumentBuilderFactory() {}
  
  public static DocumentBuilderFactory newInstance()
  {
    try
    {
      return (DocumentBuilderFactory)FactoryFinder.find("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new FactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public static DocumentBuilderFactory newInstance(String paramString, ClassLoader paramClassLoader)
  {
    if (paramString == null) {
      throw new FactoryConfigurationError("factoryClassName cannot be null.");
    }
    if (paramClassLoader == null) {
      paramClassLoader = SecuritySupport.getContextClassLoader();
    }
    try
    {
      return (DocumentBuilderFactory)FactoryFinder.newInstance(paramString, paramClassLoader, false);
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new FactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public abstract DocumentBuilder newDocumentBuilder()
    throws ParserConfigurationException;
  
  public void setNamespaceAware(boolean paramBoolean)
  {
    namespaceAware = paramBoolean;
  }
  
  public void setValidating(boolean paramBoolean)
  {
    validating = paramBoolean;
  }
  
  public void setIgnoringElementContentWhitespace(boolean paramBoolean)
  {
    whitespace = paramBoolean;
  }
  
  public void setExpandEntityReferences(boolean paramBoolean)
  {
    expandEntityRef = paramBoolean;
  }
  
  public void setIgnoringComments(boolean paramBoolean)
  {
    ignoreComments = paramBoolean;
  }
  
  public void setCoalescing(boolean paramBoolean)
  {
    coalescing = paramBoolean;
  }
  
  public boolean isNamespaceAware()
  {
    return namespaceAware;
  }
  
  public boolean isValidating()
  {
    return validating;
  }
  
  public boolean isIgnoringElementContentWhitespace()
  {
    return whitespace;
  }
  
  public boolean isExpandEntityReferences()
  {
    return expandEntityRef;
  }
  
  public boolean isIgnoringComments()
  {
    return ignoreComments;
  }
  
  public boolean isCoalescing()
  {
    return coalescing;
  }
  
  public abstract void setAttribute(String paramString, Object paramObject)
    throws IllegalArgumentException;
  
  public abstract Object getAttribute(String paramString)
    throws IllegalArgumentException;
  
  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws ParserConfigurationException;
  
  public abstract boolean getFeature(String paramString)
    throws ParserConfigurationException;
  
  public Schema getSchema()
  {
    throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
  
  public void setSchema(Schema paramSchema)
  {
    throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
  
  public void setXIncludeAware(boolean paramBoolean)
  {
    throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
  
  public boolean isXIncludeAware()
  {
    throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
}
