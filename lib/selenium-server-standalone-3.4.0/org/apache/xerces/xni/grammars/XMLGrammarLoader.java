package org.apache.xerces.xni.grammars;

import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;

public abstract interface XMLGrammarLoader
{
  public abstract String[] getRecognizedFeatures();
  
  public abstract boolean getFeature(String paramString)
    throws XMLConfigurationException;
  
  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException;
  
  public abstract String[] getRecognizedProperties();
  
  public abstract Object getProperty(String paramString)
    throws XMLConfigurationException;
  
  public abstract void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException;
  
  public abstract void setLocale(Locale paramLocale);
  
  public abstract Locale getLocale();
  
  public abstract void setErrorHandler(XMLErrorHandler paramXMLErrorHandler);
  
  public abstract XMLErrorHandler getErrorHandler();
  
  public abstract void setEntityResolver(XMLEntityResolver paramXMLEntityResolver);
  
  public abstract XMLEntityResolver getEntityResolver();
  
  public abstract Grammar loadGrammar(XMLInputSource paramXMLInputSource)
    throws IOException, XNIException;
}
