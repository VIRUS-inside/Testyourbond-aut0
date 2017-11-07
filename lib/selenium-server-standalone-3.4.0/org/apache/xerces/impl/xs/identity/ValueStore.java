package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.xs.ShortList;

public abstract interface ValueStore
{
  public abstract void addValue(Field paramField, boolean paramBoolean, Object paramObject, short paramShort, ShortList paramShortList);
  
  public abstract void reportError(String paramString, Object[] paramArrayOfObject);
}
