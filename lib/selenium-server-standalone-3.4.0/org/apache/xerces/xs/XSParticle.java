package org.apache.xerces.xs;

public abstract interface XSParticle
  extends XSObject
{
  public abstract int getMinOccurs();
  
  public abstract int getMaxOccurs();
  
  public abstract boolean getMaxOccursUnbounded();
  
  public abstract XSTerm getTerm();
  
  public abstract XSObjectList getAnnotations();
}
