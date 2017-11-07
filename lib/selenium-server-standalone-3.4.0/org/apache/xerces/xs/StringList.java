package org.apache.xerces.xs;

import java.util.List;

public abstract interface StringList
  extends List
{
  public abstract int getLength();
  
  public abstract boolean contains(String paramString);
  
  public abstract String item(int paramInt);
}
