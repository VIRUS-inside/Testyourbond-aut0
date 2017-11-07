package org.apache.xerces.impl.xs.traversers;

class OneAttr
{
  public String name;
  public int dvIndex;
  public int valueIndex;
  public Object dfltValue;
  
  public OneAttr(String paramString, int paramInt1, int paramInt2, Object paramObject)
  {
    name = paramString;
    dvIndex = paramInt1;
    valueIndex = paramInt2;
    dfltValue = paramObject;
  }
}
