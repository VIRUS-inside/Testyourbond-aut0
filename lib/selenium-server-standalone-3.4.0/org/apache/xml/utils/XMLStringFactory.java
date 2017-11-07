package org.apache.xml.utils;

public abstract class XMLStringFactory
{
  public XMLStringFactory() {}
  
  public abstract XMLString newstr(String paramString);
  
  public abstract XMLString newstr(FastStringBuffer paramFastStringBuffer, int paramInt1, int paramInt2);
  
  public abstract XMLString newstr(char[] paramArrayOfChar, int paramInt1, int paramInt2);
  
  public abstract XMLString emptystr();
}
