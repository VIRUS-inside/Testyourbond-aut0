package org.apache.xml.dtm;

public abstract interface DTMIterator
{
  public static final short FILTER_ACCEPT = 1;
  public static final short FILTER_REJECT = 2;
  public static final short FILTER_SKIP = 3;
  
  public abstract DTM getDTM(int paramInt);
  
  public abstract DTMManager getDTMManager();
  
  public abstract int getRoot();
  
  public abstract void setRoot(int paramInt, Object paramObject);
  
  public abstract void reset();
  
  public abstract int getWhatToShow();
  
  public abstract boolean getExpandEntityReferences();
  
  public abstract int nextNode();
  
  public abstract int previousNode();
  
  public abstract void detach();
  
  public abstract void allowDetachToRelease(boolean paramBoolean);
  
  public abstract int getCurrentNode();
  
  public abstract boolean isFresh();
  
  public abstract void setShouldCacheNodes(boolean paramBoolean);
  
  public abstract boolean isMutable();
  
  public abstract int getCurrentPos();
  
  public abstract void runTo(int paramInt);
  
  public abstract void setCurrentPos(int paramInt);
  
  public abstract int item(int paramInt);
  
  public abstract void setItem(int paramInt1, int paramInt2);
  
  public abstract int getLength();
  
  public abstract DTMIterator cloneWithReset()
    throws CloneNotSupportedException;
  
  public abstract Object clone()
    throws CloneNotSupportedException;
  
  public abstract boolean isDocOrdered();
  
  public abstract int getAxis();
}
