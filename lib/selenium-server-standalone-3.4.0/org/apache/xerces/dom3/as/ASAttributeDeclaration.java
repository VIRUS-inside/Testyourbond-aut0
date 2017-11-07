package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface ASAttributeDeclaration
  extends ASObject
{
  public static final short VALUE_NONE = 0;
  public static final short VALUE_DEFAULT = 1;
  public static final short VALUE_FIXED = 2;
  
  public abstract ASDataType getDataType();
  
  public abstract void setDataType(ASDataType paramASDataType);
  
  public abstract String getDataValue();
  
  public abstract void setDataValue(String paramString);
  
  public abstract String getEnumAttr();
  
  public abstract void setEnumAttr(String paramString);
  
  public abstract ASObjectList getOwnerElements();
  
  public abstract void setOwnerElements(ASObjectList paramASObjectList);
  
  public abstract short getDefaultType();
  
  public abstract void setDefaultType(short paramShort);
}
