package org.apache.xerces.xni.parser;

import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;

public abstract interface XMLParserConfiguration
  extends XMLComponentManager
{
  public abstract void parse(XMLInputSource paramXMLInputSource)
    throws XNIException, IOException;
  
  public abstract void addRecognizedFeatures(String[] paramArrayOfString);
  
  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException;
  
  public abstract boolean getFeature(String paramString)
    throws XMLConfigurationException;
  
  public abstract void addRecognizedProperties(String[] paramArrayOfString);
  
  public abstract void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException;
  
  public abstract Object getProperty(String paramString)
    throws XMLConfigurationException;
  
  public abstract void setErrorHandler(XMLErrorHandler paramXMLErrorHandler);
  
  public abstract XMLErrorHandler getErrorHandler();
  
  public abstract void setDocumentHandler(XMLDocumentHandler paramXMLDocumentHandler);
  
  public abstract XMLDocumentHandler getDocumentHandler();
  
  public abstract void setDTDHandler(XMLDTDHandler paramXMLDTDHandler);
  
  public abstract XMLDTDHandler getDTDHandler();
  
  public abstract void setDTDContentModelHandler(XMLDTDContentModelHandler paramXMLDTDContentModelHandler);
  
  public abstract XMLDTDContentModelHandler getDTDContentModelHandler();
  
  public abstract void setEntityResolver(XMLEntityResolver paramXMLEntityResolver);
  
  public abstract XMLEntityResolver getEntityResolver();
  
  public abstract void setLocale(Locale paramLocale)
    throws XNIException;
  
  public abstract Locale getLocale();
}
