package org.apache.xerces.xs;

public abstract interface XSAttributeUse
  extends XSObject
{
  public abstract boolean getRequired();
  
  public abstract XSAttributeDeclaration getAttrDeclaration();
  
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
  
  public abstract XSObjectList getAnnotations();
}
