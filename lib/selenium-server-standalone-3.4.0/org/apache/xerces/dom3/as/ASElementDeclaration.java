package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface ASElementDeclaration
  extends ASObject
{
  public static final short EMPTY_CONTENTTYPE = 1;
  public static final short ANY_CONTENTTYPE = 2;
  public static final short MIXED_CONTENTTYPE = 3;
  public static final short ELEMENTS_CONTENTTYPE = 4;
  
  public abstract boolean getStrictMixedContent();
  
  public abstract void setStrictMixedContent(boolean paramBoolean);
  
  public abstract ASDataType getElementType();
  
  public abstract void setElementType(ASDataType paramASDataType);
  
  public abstract boolean getIsPCDataOnly();
  
  public abstract void setIsPCDataOnly(boolean paramBoolean);
  
  public abstract short getContentType();
  
  public abstract void setContentType(short paramShort);
  
  public abstract String getSystemId();
  
  public abstract void setSystemId(String paramString);
  
  public abstract ASContentModel getAsCM();
  
  public abstract void setAsCM(ASContentModel paramASContentModel);
  
  public abstract ASNamedObjectMap getASAttributeDecls();
  
  public abstract void setASAttributeDecls(ASNamedObjectMap paramASNamedObjectMap);
  
  public abstract void addASAttributeDecl(ASAttributeDeclaration paramASAttributeDeclaration);
  
  public abstract ASAttributeDeclaration removeASAttributeDecl(ASAttributeDeclaration paramASAttributeDeclaration);
}
