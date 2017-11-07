package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface ASEntityDeclaration
  extends ASObject
{
  public static final short INTERNAL_ENTITY = 1;
  public static final short EXTERNAL_ENTITY = 2;
  
  public abstract short getEntityType();
  
  public abstract void setEntityType(short paramShort);
  
  public abstract String getEntityValue();
  
  public abstract void setEntityValue(String paramString);
  
  public abstract String getSystemId();
  
  public abstract void setSystemId(String paramString);
  
  public abstract String getPublicId();
  
  public abstract void setPublicId(String paramString);
}
