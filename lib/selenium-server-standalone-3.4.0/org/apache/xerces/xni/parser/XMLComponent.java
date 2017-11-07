package org.apache.xerces.xni.parser;

public abstract interface XMLComponent
{
  public abstract void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException;
  
  public abstract String[] getRecognizedFeatures();
  
  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException;
  
  public abstract String[] getRecognizedProperties();
  
  public abstract void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException;
  
  public abstract Boolean getFeatureDefault(String paramString);
  
  public abstract Object getPropertyDefault(String paramString);
}
