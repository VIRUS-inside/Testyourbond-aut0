package org.apache.xalan.xsltc;

public abstract interface NodeIterator
  extends Cloneable
{
  public static final int END = -1;
  
  public abstract int next();
  
  public abstract NodeIterator reset();
  
  public abstract int getLast();
  
  public abstract int getPosition();
  
  public abstract void setMark();
  
  public abstract void gotoMark();
  
  public abstract NodeIterator setStartNode(int paramInt);
  
  public abstract boolean isReverse();
  
  public abstract NodeIterator cloneIterator();
  
  public abstract void setRestartable(boolean paramBoolean);
}
