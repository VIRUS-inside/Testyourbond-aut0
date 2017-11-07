package com.beust.jcommander.defaultprovider;

import com.beust.jcommander.IDefaultProvider;
import com.beust.jcommander.ParameterException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;






















public class PropertyFileDefaultProvider
  implements IDefaultProvider
{
  public static final String DEFAULT_FILE_NAME = "jcommander.properties";
  private Properties m_properties;
  
  public PropertyFileDefaultProvider()
  {
    init("jcommander.properties");
  }
  
  public PropertyFileDefaultProvider(String fileName) {
    init(fileName);
  }
  
  private void init(String fileName) {
    try {
      m_properties = new Properties();
      URL url = ClassLoader.getSystemResource(fileName);
      if (url != null) {
        m_properties.load(url.openStream());
      } else {
        throw new ParameterException("Could not find property file: " + fileName + " on the class path");
      }
    }
    catch (IOException e)
    {
      throw new ParameterException("Could not open property file: " + fileName);
    }
  }
  
  public String getDefaultValueFor(String optionName) {
    int index = 0;
    while ((index < optionName.length()) && (!Character.isLetterOrDigit(optionName.charAt(index)))) {
      index++;
    }
    String key = optionName.substring(index);
    return m_properties.getProperty(key);
  }
}
