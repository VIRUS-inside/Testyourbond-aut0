package org.apache.xerces.xs;

public abstract interface XSWildcard
  extends XSTerm
{
  public static final short NSCONSTRAINT_ANY = 1;
  public static final short NSCONSTRAINT_NOT = 2;
  public static final short NSCONSTRAINT_LIST = 3;
  public static final short PC_STRICT = 1;
  public static final short PC_SKIP = 2;
  public static final short PC_LAX = 3;
  
  public abstract short getConstraintType();
  
  public abstract StringList getNsConstraintList();
  
  public abstract short getProcessContents();
  
  public abstract XSAnnotation getAnnotation();
  
  public abstract XSObjectList getAnnotations();
}
