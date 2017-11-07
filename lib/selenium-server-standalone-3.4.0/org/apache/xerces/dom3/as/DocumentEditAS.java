package org.apache.xerces.dom3.as;

/**
 * @deprecated
 */
public abstract interface DocumentEditAS
  extends NodeEditAS
{
  public abstract boolean getContinuousValidityChecking();
  
  public abstract void setContinuousValidityChecking(boolean paramBoolean);
}
