package org.apache.xerces.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public class ParserConfigurationSettings
  implements XMLComponentManager
{
  protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
  protected ArrayList fRecognizedProperties = new ArrayList();
  protected HashMap fProperties = new HashMap();
  protected ArrayList fRecognizedFeatures = new ArrayList();
  protected HashMap fFeatures = new HashMap();
  protected XMLComponentManager fParentSettings;
  
  public ParserConfigurationSettings()
  {
    this(null);
  }
  
  public ParserConfigurationSettings(XMLComponentManager paramXMLComponentManager)
  {
    fParentSettings = paramXMLComponentManager;
  }
  
  public void addRecognizedFeatures(String[] paramArrayOfString)
  {
    int i = paramArrayOfString != null ? paramArrayOfString.length : 0;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if (!fRecognizedFeatures.contains(str)) {
        fRecognizedFeatures.add(str);
      }
    }
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    checkFeature(paramString);
    fFeatures.put(paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE);
  }
  
  public void addRecognizedProperties(String[] paramArrayOfString)
  {
    int i = paramArrayOfString != null ? paramArrayOfString.length : 0;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if (!fRecognizedProperties.contains(str)) {
        fRecognizedProperties.add(str);
      }
    }
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    checkProperty(paramString);
    fProperties.put(paramString, paramObject);
  }
  
  public boolean getFeature(String paramString)
    throws XMLConfigurationException
  {
    Boolean localBoolean = (Boolean)fFeatures.get(paramString);
    if (localBoolean == null)
    {
      checkFeature(paramString);
      return false;
    }
    return localBoolean.booleanValue();
  }
  
  public Object getProperty(String paramString)
    throws XMLConfigurationException
  {
    Object localObject = fProperties.get(paramString);
    if (localObject == null) {
      checkProperty(paramString);
    }
    return localObject;
  }
  
  protected void checkFeature(String paramString)
    throws XMLConfigurationException
  {
    if (!fRecognizedFeatures.contains(paramString)) {
      if (fParentSettings != null)
      {
        fParentSettings.getFeature(paramString);
      }
      else
      {
        short s = 0;
        throw new XMLConfigurationException(s, paramString);
      }
    }
  }
  
  protected void checkProperty(String paramString)
    throws XMLConfigurationException
  {
    if (!fRecognizedProperties.contains(paramString)) {
      if (fParentSettings != null)
      {
        fParentSettings.getProperty(paramString);
      }
      else
      {
        short s = 0;
        throw new XMLConfigurationException(s, paramString);
      }
    }
  }
}
