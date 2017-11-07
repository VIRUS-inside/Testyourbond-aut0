package org.apache.html.dom;

class CollectionIndex
{
  private int _index;
  
  int getIndex()
  {
    return _index;
  }
  
  void decrement()
  {
    _index -= 1;
  }
  
  boolean isZero()
  {
    return _index <= 0;
  }
  
  CollectionIndex(int paramInt)
  {
    _index = paramInt;
  }
}
