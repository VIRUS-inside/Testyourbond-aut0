package org.apache.xerces.xs;

public abstract interface XSModelGroup
  extends XSTerm
{
  public static final short COMPOSITOR_SEQUENCE = 1;
  public static final short COMPOSITOR_CHOICE = 2;
  public static final short COMPOSITOR_ALL = 3;
  
  public abstract short getCompositor();
  
  public abstract XSObjectList getParticles();
  
  public abstract XSAnnotation getAnnotation();
  
  public abstract XSObjectList getAnnotations();
}
