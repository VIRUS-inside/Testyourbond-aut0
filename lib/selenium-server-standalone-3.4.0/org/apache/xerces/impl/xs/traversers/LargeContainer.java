package org.apache.xerces.impl.xs.traversers;

import java.util.Hashtable;

class LargeContainer
  extends Container
{
  Hashtable items;
  
  LargeContainer(int paramInt)
  {
    items = new Hashtable(paramInt * 2 + 1);
    values = new OneAttr[paramInt];
  }
  
  void put(String paramString, OneAttr paramOneAttr)
  {
    items.put(paramString, paramOneAttr);
    values[(pos++)] = paramOneAttr;
  }
  
  OneAttr get(String paramString)
  {
    OneAttr localOneAttr = (OneAttr)items.get(paramString);
    return localOneAttr;
  }
}
