package javax.xml.stream;

import java.io.OutputStream;
import java.io.Writer;
import javax.xml.transform.Result;

public abstract class XMLOutputFactory
{
  public static final String IS_REPAIRING_NAMESPACES = "javax.xml.stream.isRepairingNamespaces";
  private static final String PROPERTY_NAME = "javax.xml.stream.XMLOutputFactory";
  private static final String DEFAULT_FACTORY = "com.ctc.wstx.stax.WstxOutputFactory";
  
  protected XMLOutputFactory() {}
  
  public static XMLOutputFactory newInstance()
    throws FactoryConfigurationError
  {
    try
    {
      return (XMLOutputFactory)FactoryFinder.find("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new FactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public static XMLInputFactory newInstance(String paramString, ClassLoader paramClassLoader)
    throws FactoryConfigurationError
  {
    if (paramClassLoader == null) {
      paramClassLoader = SecuritySupport.getContextClassLoader();
    }
    try
    {
      return (XMLInputFactory)FactoryFinder.find(paramString, paramClassLoader, "com.ctc.wstx.stax.WstxInputFactory");
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new FactoryConfigurationError(localConfigurationError.getException(), localConfigurationError.getMessage());
    }
  }
  
  public abstract XMLStreamWriter createXMLStreamWriter(Writer paramWriter)
    throws XMLStreamException;
  
  public abstract XMLStreamWriter createXMLStreamWriter(OutputStream paramOutputStream)
    throws XMLStreamException;
  
  public abstract XMLStreamWriter createXMLStreamWriter(OutputStream paramOutputStream, String paramString)
    throws XMLStreamException;
  
  public abstract XMLStreamWriter createXMLStreamWriter(Result paramResult)
    throws XMLStreamException;
  
  public abstract XMLEventWriter createXMLEventWriter(Result paramResult)
    throws XMLStreamException;
  
  public abstract XMLEventWriter createXMLEventWriter(OutputStream paramOutputStream)
    throws XMLStreamException;
  
  public abstract XMLEventWriter createXMLEventWriter(OutputStream paramOutputStream, String paramString)
    throws XMLStreamException;
  
  public abstract XMLEventWriter createXMLEventWriter(Writer paramWriter)
    throws XMLStreamException;
  
  public abstract void setProperty(String paramString, Object paramObject)
    throws IllegalArgumentException;
  
  public abstract Object getProperty(String paramString)
    throws IllegalArgumentException;
  
  public abstract boolean isPropertySupported(String paramString);
}
