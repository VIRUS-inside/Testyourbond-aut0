package org.apache.xerces.impl.xs.util;

import org.apache.xerces.xni.XMLLocator;

public final class SimpleLocator
  implements XMLLocator
{
  private String lsid;
  private String esid;
  private int line;
  private int column;
  private int charOffset;
  
  public SimpleLocator() {}
  
  public SimpleLocator(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    this(paramString1, paramString2, paramInt1, paramInt2, -1);
  }
  
  public void setValues(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    setValues(paramString1, paramString2, paramInt1, paramInt2, -1);
  }
  
  public SimpleLocator(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    line = paramInt1;
    column = paramInt2;
    lsid = paramString1;
    esid = paramString2;
    charOffset = paramInt3;
  }
  
  public void setValues(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    line = paramInt1;
    column = paramInt2;
    lsid = paramString1;
    esid = paramString2;
    charOffset = paramInt3;
  }
  
  public int getLineNumber()
  {
    return line;
  }
  
  public int getColumnNumber()
  {
    return column;
  }
  
  public int getCharacterOffset()
  {
    return charOffset;
  }
  
  public String getPublicId()
  {
    return null;
  }
  
  public String getExpandedSystemId()
  {
    return esid;
  }
  
  public String getLiteralSystemId()
  {
    return lsid;
  }
  
  public String getBaseSystemId()
  {
    return null;
  }
  
  public void setColumnNumber(int paramInt)
  {
    column = paramInt;
  }
  
  public void setLineNumber(int paramInt)
  {
    line = paramInt;
  }
  
  public void setCharacterOffset(int paramInt)
  {
    charOffset = paramInt;
  }
  
  public void setBaseSystemId(String paramString) {}
  
  public void setExpandedSystemId(String paramString)
  {
    esid = paramString;
  }
  
  public void setLiteralSystemId(String paramString)
  {
    lsid = paramString;
  }
  
  public void setPublicId(String paramString) {}
  
  public String getEncoding()
  {
    return null;
  }
  
  public String getXMLVersion()
  {
    return null;
  }
}
