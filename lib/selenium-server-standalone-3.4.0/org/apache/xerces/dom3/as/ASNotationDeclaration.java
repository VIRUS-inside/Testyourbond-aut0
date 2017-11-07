package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface ASNotationDeclaration
  extends ASObject
{
  public abstract String getSystemId();
  
  public abstract void setSystemId(String paramString);
  
  public abstract String getPublicId();
  
  public abstract void setPublicId(String paramString);
}
