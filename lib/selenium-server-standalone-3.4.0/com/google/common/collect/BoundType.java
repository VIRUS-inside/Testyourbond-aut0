package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
























@GwtCompatible
public enum BoundType
{
  OPEN, 
  







  CLOSED;
  



  private BoundType() {}
  


  static BoundType forBoolean(boolean inclusive)
  {
    return inclusive ? CLOSED : OPEN;
  }
  
  abstract BoundType flip();
}
