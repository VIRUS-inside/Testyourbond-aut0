package org.apache.xerces.xs.datatypes;

import java.util.List;

public abstract interface ObjectList
  extends List
{
  public abstract int getLength();
  
  public abstract boolean contains(Object paramObject);
  
  public abstract Object item(int paramInt);
}
