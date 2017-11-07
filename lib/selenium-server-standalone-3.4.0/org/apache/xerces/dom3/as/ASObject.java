package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface ASObject
{
  public static final short AS_ELEMENT_DECLARATION = 1;
  public static final short AS_ATTRIBUTE_DECLARATION = 2;
  public static final short AS_NOTATION_DECLARATION = 3;
  public static final short AS_ENTITY_DECLARATION = 4;
  public static final short AS_CONTENTMODEL = 5;
  public static final short AS_MODEL = 6;
  
  public abstract short getAsNodeType();
  
  public abstract ASModel getOwnerASModel();
  
  public abstract void setOwnerASModel(ASModel paramASModel);
  
  public abstract String getNodeName();
  
  public abstract void setNodeName(String paramString);
  
  public abstract String getPrefix();
  
  public abstract void setPrefix(String paramString);
  
  public abstract String getLocalName();
  
  public abstract void setLocalName(String paramString);
  
  public abstract String getNamespaceURI();
  
  public abstract void setNamespaceURI(String paramString);
  
  public abstract ASObject cloneASObject(boolean paramBoolean);
}
