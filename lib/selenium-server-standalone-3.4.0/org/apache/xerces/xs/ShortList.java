package org.apache.xerces.xs;

import java.util.List;

public abstract interface ShortList
  extends List
{
  public abstract int getLength();
  
  public abstract boolean contains(short paramShort);
  
  public abstract short item(int paramInt)
    throws XSException;
}
