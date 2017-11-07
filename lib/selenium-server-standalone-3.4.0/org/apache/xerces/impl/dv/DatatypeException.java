package org.apache.xerces.impl.dv;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DatatypeException
  extends Exception
{
  static final long serialVersionUID = 1940805832730465578L;
  protected final String key;
  protected final Object[] args;
  
  public DatatypeException(String paramString, Object[] paramArrayOfObject)
  {
    super(paramString);
    key = paramString;
    args = paramArrayOfObject;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public Object[] getArgs()
  {
    return args;
  }
  
  public String getMessage()
  {
    ResourceBundle localResourceBundle = null;
    localResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLSchemaMessages");
    if (localResourceBundle == null) {
      throw new MissingResourceException("Property file not found!", "org.apache.xerces.impl.msg.XMLSchemaMessages", key);
    }
    String str = localResourceBundle.getString(key);
    if (str == null)
    {
      str = localResourceBundle.getString("BadMessageKey");
      throw new MissingResourceException(str, "org.apache.xerces.impl.msg.XMLSchemaMessages", key);
    }
    if (args != null) {
      try
      {
        str = MessageFormat.format(str, args);
      }
      catch (Exception localException)
      {
        str = localResourceBundle.getString("FormatFailed");
        str = str + " " + localResourceBundle.getString(key);
      }
    }
    return str;
  }
}
