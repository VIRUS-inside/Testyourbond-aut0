package org.apache.xerces.xs;

public abstract interface XSTypeDefinition
  extends XSObject
{
  public static final short COMPLEX_TYPE = 15;
  public static final short SIMPLE_TYPE = 16;
  
  public abstract short getTypeCategory();
  
  public abstract XSTypeDefinition getBaseType();
  
  public abstract boolean isFinal(short paramShort);
  
  public abstract short getFinal();
  
  public abstract boolean getAnonymous();
  
  public abstract boolean derivedFromType(XSTypeDefinition paramXSTypeDefinition, short paramShort);
  
  public abstract boolean derivedFrom(String paramString1, String paramString2, short paramShort);
}
