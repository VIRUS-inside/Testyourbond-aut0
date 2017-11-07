package org.apache.xerces.xs;

import java.util.List;

public abstract interface XSObjectList
  extends List
{
  public abstract int getLength();
  
  public abstract XSObject item(int paramInt);
}
