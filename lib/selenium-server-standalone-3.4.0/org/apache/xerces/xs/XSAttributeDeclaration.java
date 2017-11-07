package org.apache.xerces.xs;

public abstract interface XSAttributeDeclaration
  extends XSObject
{
  public abstract XSSimpleTypeDefinition getTypeDefinition();
  
  public abstract short getScope();
  
  public abstract XSComplexTypeDefinition getEnclosingCTDefinition();
  
  public abstract short getConstraintType();
  
  /**
   * @deprecated
   */
  public abstract String getConstraintValue();
  
  /**
   * @deprecated
   */
  public abstract Object getActualVC()
    throws XSException;
  
  /**
   * @deprecated
   */
  public abstract short getActualVCType()
    throws XSException;
  
  /**
   * @deprecated
   */
  public abstract ShortList getItemValueTypes()
    throws XSException;
  
  public abstract XSValue getValueConstraintValue();
  
  public abstract XSAnnotation getAnnotation();
  
  public abstract XSObjectList getAnnotations();
}
