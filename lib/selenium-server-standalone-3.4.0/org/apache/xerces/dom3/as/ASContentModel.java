package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface ASContentModel
  extends ASObject
{
  public static final int AS_UNBOUNDED = Integer.MAX_VALUE;
  public static final short AS_SEQUENCE = 0;
  public static final short AS_CHOICE = 1;
  public static final short AS_ALL = 2;
  public static final short AS_NONE = 3;
  
  public abstract short getListOperator();
  
  public abstract void setListOperator(short paramShort);
  
  public abstract int getMinOccurs();
  
  public abstract void setMinOccurs(int paramInt);
  
  public abstract int getMaxOccurs();
  
  public abstract void setMaxOccurs(int paramInt);
  
  public abstract ASObjectList getSubModels();
  
  public abstract void setSubModels(ASObjectList paramASObjectList);
  
  public abstract void removesubModel(ASObject paramASObject);
  
  public abstract void insertsubModel(ASObject paramASObject)
    throws DOMASException;
  
  public abstract int appendsubModel(ASObject paramASObject)
    throws DOMASException;
}
