package org.apache.xerces.impl.xpath;

public class XPathException
  extends Exception
{
  static final long serialVersionUID = -948482312169512085L;
  private final String fKey;
  
  public XPathException()
  {
    fKey = "c-general-xpath";
  }
  
  public XPathException(String paramString)
  {
    fKey = paramString;
  }
  
  public String getKey()
  {
    return fKey;
  }
}
