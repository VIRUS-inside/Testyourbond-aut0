package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;



















@GwtCompatible(emulated=true)
final class Platform
{
  private Platform() {}
  
  static char[] charBufferFromThreadLocal()
  {
    return (char[])DEST_TL.get();
  }
  





  private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal()
  {
    protected char[] initialValue()
    {
      return new char['Ѐ'];
    }
  };
}
