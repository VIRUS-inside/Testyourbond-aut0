package org.apache.xerces.xni.parser;

public abstract interface XMLComponentManager
{
  public abstract boolean getFeature(String paramString)
    throws XMLConfigurationException;
  
  public abstract Object getProperty(String paramString)
    throws XMLConfigurationException;
}
