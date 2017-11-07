package org.apache.xerces.dom;

import java.util.Hashtable;

class LCount
{
  static Hashtable lCounts = new Hashtable();
  public int captures = 0;
  public int bubbles = 0;
  public int defaults;
  public int total = 0;
  
  LCount() {}
  
  static LCount lookup(String paramString)
  {
    LCount localLCount = (LCount)lCounts.get(paramString);
    if (localLCount == null) {
      lCounts.put(paramString, localLCount = new LCount());
    }
    return localLCount;
  }
}
