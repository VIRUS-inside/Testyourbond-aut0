package org.apache.xpath.axes;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public abstract interface ContextNodeList
{
  public abstract Node getCurrentNode();
  
  public abstract int getCurrentPos();
  
  public abstract void reset();
  
  public abstract void setShouldCacheNodes(boolean paramBoolean);
  
  public abstract void runTo(int paramInt);
  
  public abstract void setCurrentPos(int paramInt);
  
  public abstract int size();
  
  public abstract boolean isFresh();
  
  public abstract NodeIterator cloneWithReset()
    throws CloneNotSupportedException;
  
  public abstract Object clone()
    throws CloneNotSupportedException;
  
  public abstract int getLast();
  
  public abstract void setLast(int paramInt);
}
