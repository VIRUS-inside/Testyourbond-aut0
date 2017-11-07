package org.apache.xerces.xs;

import java.util.List;

public abstract interface XSNamespaceItemList
  extends List
{
  public abstract int getLength();
  
  public abstract XSNamespaceItem item(int paramInt);
}
