package org.apache.xerces.dom3.as;

import org.w3c.dom.DOMException;

/**
 * @deprecated
 */
public abstract interface ASModel
  extends ASObject
{
  public abstract boolean getIsNamespaceAware();
  
  public abstract short getUsageLocation();
  
  public abstract String getAsLocation();
  
  public abstract void setAsLocation(String paramString);
  
  public abstract String getAsHint();
  
  public abstract void setAsHint(String paramString);
  
  public abstract ASNamedObjectMap getElementDeclarations();
  
  public abstract ASNamedObjectMap getAttributeDeclarations();
  
  public abstract ASNamedObjectMap getNotationDeclarations();
  
  public abstract ASNamedObjectMap getEntityDeclarations();
  
  public abstract ASNamedObjectMap getContentModelDeclarations();
  
  public abstract void addASModel(ASModel paramASModel);
  
  public abstract ASObjectList getASModels();
  
  public abstract void removeAS(ASModel paramASModel);
  
  public abstract boolean validate();
  
  public abstract ASElementDeclaration createASElementDeclaration(String paramString1, String paramString2)
    throws DOMException;
  
  public abstract ASAttributeDeclaration createASAttributeDeclaration(String paramString1, String paramString2)
    throws DOMException;
  
  public abstract ASNotationDeclaration createASNotationDeclaration(String paramString1, String paramString2, String paramString3, String paramString4)
    throws DOMException;
  
  public abstract ASEntityDeclaration createASEntityDeclaration(String paramString)
    throws DOMException;
  
  public abstract ASContentModel createASContentModel(int paramInt1, int paramInt2, short paramShort)
    throws DOMASException;
}
