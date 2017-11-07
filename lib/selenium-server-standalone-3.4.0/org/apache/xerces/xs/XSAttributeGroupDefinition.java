package org.apache.xerces.xs;

public abstract interface XSAttributeGroupDefinition
  extends XSObject
{
  public abstract XSObjectList getAttributeUses();
  
  public abstract XSWildcard getAttributeWildcard();
  
  public abstract XSAnnotation getAnnotation();
  
  public abstract XSObjectList getAnnotations();
}
