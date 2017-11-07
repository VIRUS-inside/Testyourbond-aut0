package org.apache.xerces.xs;

public abstract interface XSNotationDeclaration
  extends XSObject
{
  public abstract String getSystemId();
  
  public abstract String getPublicId();
  
  public abstract XSAnnotation getAnnotation();
  
  public abstract XSObjectList getAnnotations();
}
