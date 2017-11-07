package org.apache.xml.dtm;



























public abstract class DTMAxisTraverser
{
  public DTMAxisTraverser() {}
  


























  public int first(int context)
  {
    return next(context, context);
  }
  














  public int first(int context, int extendedTypeID)
  {
    return next(context, context, extendedTypeID);
  }
  
  public abstract int next(int paramInt1, int paramInt2);
  
  public abstract int next(int paramInt1, int paramInt2, int paramInt3);
}
