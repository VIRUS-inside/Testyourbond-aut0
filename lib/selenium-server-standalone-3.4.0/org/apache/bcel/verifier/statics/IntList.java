package org.apache.bcel.verifier.statics;

import java.util.ArrayList;




























































public class IntList
{
  private ArrayList theList;
  
  IntList()
  {
    theList = new ArrayList();
  }
  
  void add(int i) {
    theList.add(new Integer(i));
  }
  
  boolean contains(int i) {
    Integer[] ints = new Integer[theList.size()];
    theList.toArray(ints);
    for (int j = 0; j < ints.length; j++) {
      if (i == ints[j].intValue()) return true;
    }
    return false;
  }
}
