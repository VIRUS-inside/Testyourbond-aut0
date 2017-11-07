package org.apache.xerces.dom3.as;

import org.w3c.dom.DOMException;

/**
 * @deprecated
 */
public abstract interface DocumentAS
{
  public abstract ASModel getActiveASModel();
  
  public abstract void setActiveASModel(ASModel paramASModel);
  
  public abstract ASObjectList getBoundASModels();
  
  public abstract void setBoundASModels(ASObjectList paramASObjectList);
  
  public abstract ASModel getInternalAS();
  
  public abstract void setInternalAS(ASModel paramASModel);
  
  public abstract void addAS(ASModel paramASModel);
  
  public abstract void removeAS(ASModel paramASModel);
  
  public abstract ASElementDeclaration getElementDeclaration()
    throws DOMException;
  
  public abstract void validate()
    throws DOMASException;
}
