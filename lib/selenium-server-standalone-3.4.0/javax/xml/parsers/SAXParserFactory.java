package javax.xml.parsers;

import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class SAXParserFactory
{
  private boolean validating = false;
  private boolean namespaceAware = false;
  
  protected SAXParserFactory() {}
  
  public static SAXParserFactory newInstance()
  {
    try
    {
      return (SAXParserFactory)FactoryFinder.find("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new FactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public static SAXParserFactory newInstance(String paramString, ClassLoader paramClassLoader)
  {
    if (paramString == null) {
      throw new FactoryConfigurationError("factoryClassName cannot be null.");
    }
    if (paramClassLoader == null) {
      paramClassLoader = SecuritySupport.getContextClassLoader();
    }
    try
    {
      return (SAXParserFactory)FactoryFinder.newInstance(paramString, paramClassLoader, false);
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new FactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public abstract SAXParser newSAXParser()
    throws ParserConfigurationException, SAXException;
  
  public void setNamespaceAware(boolean paramBoolean)
  {
    namespaceAware = paramBoolean;
  }
  
  public void setValidating(boolean paramBoolean)
  {
    validating = paramBoolean;
  }
  
  public boolean isNamespaceAware()
  {
    return namespaceAware;
  }
  
  public boolean isValidating()
  {
    return validating;
  }
  
  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;
  
  public abstract boolean getFeature(String paramString)
    throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;
  
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
