package org.xml.sax;

/**
 * @deprecated
 */
public abstract interface AttributeList
{
  public abstract int getLength();
  
  public abstract String getName(int paramInt);
  
  public abstract String getType(int paramInt);
  
  public abstract String getValue(int paramInt);
  
  public abstract String getType(String paramString);
  
  public abstract String getValue(String paramString);
}
