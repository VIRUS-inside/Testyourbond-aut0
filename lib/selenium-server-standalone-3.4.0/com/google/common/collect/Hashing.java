package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;






























@GwtCompatible
final class Hashing
{
  private static final int C1 = -862048943;
  private static final int C2 = 461845907;
  private static final int MAX_TABLE_SIZE = 1073741824;
  
  private Hashing() {}
  
  static int smear(int hashCode)
  {
    return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
  }
  
  static int smearedHash(@Nullable Object o) {
    return smear(o == null ? 0 : o.hashCode());
  }
  



  static int closedTableSize(int expectedEntries, double loadFactor)
  {
    expectedEntries = Math.max(expectedEntries, 2);
    int tableSize = Integer.highestOneBit(expectedEntries);
    
    if (expectedEntries > (int)(loadFactor * tableSize)) {
      tableSize <<= 1;
      return tableSize > 0 ? tableSize : 1073741824;
    }
    return tableSize;
  }
  
  static boolean needsResizing(int size, int tableSize, double loadFactor) {
    return (size > loadFactor * tableSize) && (tableSize < 1073741824);
  }
}
