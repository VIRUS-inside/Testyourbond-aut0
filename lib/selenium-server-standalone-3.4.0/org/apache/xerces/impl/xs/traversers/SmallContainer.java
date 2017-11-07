package org.apache.xerces.impl.xs.traversers;

class SmallContainer
  extends Container
{
  String[] keys;
  
  SmallContainer(int paramInt)
  {
    keys = new String[paramInt];
    values = new OneAttr[paramInt];
  }
  
  void put(String paramString, OneAttr paramOneAttr)
  {
    keys[pos] = paramString;
    values[(pos++)] = paramOneAttr;
  }
  
  OneAttr get(String paramString)
  {
    for (int i = 0; i < pos; i++) {
      if (keys[i].equals(paramString)) {
        return values[i];
      }
    }
    return null;
  }
}
