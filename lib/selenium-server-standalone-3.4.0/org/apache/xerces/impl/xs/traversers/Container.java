package org.apache.xerces.impl.xs.traversers;

abstract class Container
{
  static final int THRESHOLD = 5;
  OneAttr[] values;
  int pos = 0;
  
  Container() {}
  
  static Container getContainer(int paramInt)
  {
    if (paramInt > 5) {
      return new LargeContainer(paramInt);
    }
    return new SmallContainer(paramInt);
  }
  
  abstract void put(String paramString, OneAttr paramOneAttr);
  
  abstract OneAttr get(String paramString);
}
