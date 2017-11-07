package org.apache.xml.serializer;

import java.util.Properties;










































public final class OutputPropertyUtils
{
  public OutputPropertyUtils() {}
  
  public static boolean getBooleanProperty(String key, Properties props)
  {
    String s = props.getProperty(key);
    
    if ((null == s) || (!s.equals("yes"))) {
      return false;
    }
    return true;
  }
  













  public static int getIntProperty(String key, Properties props)
  {
    String s = props.getProperty(key);
    
    if (null == s) {
      return 0;
    }
    return Integer.parseInt(s);
  }
}
