package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XNIException;

public class XMLConfigurationException
  extends XNIException
{
  static final long serialVersionUID = -5437427404547669188L;
  public static final short NOT_RECOGNIZED = 0;
  public static final short NOT_SUPPORTED = 1;
  protected short fType;
  protected String fIdentifier;
  
  public XMLConfigurationException(short paramShort, String paramString)
  {
    super(paramString);
    fType = paramShort;
    fIdentifier = paramString;
  }
  
  public XMLConfigurationException(short paramShort, String paramString1, String paramString2)
  {
    super(paramString2);
    fType = paramShort;
    fIdentifier = paramString1;
  }
  
  public short getType()
  {
    return fType;
  }
  
  public String getIdentifier()
  {
    return fIdentifier;
  }
}
