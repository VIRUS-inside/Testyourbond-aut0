package org.apache.xerces.impl.xs;

import java.util.AbstractList;
import org.apache.xerces.xs.StringList;

final class PSVIErrorList
  extends AbstractList
  implements StringList
{
  private final String[] fArray;
  private final int fLength;
  private final int fOffset;
  
  public PSVIErrorList(String[] paramArrayOfString, boolean paramBoolean)
  {
    fArray = paramArrayOfString;
    fLength = (fArray.length >> 1);
    fOffset = (paramBoolean ? 0 : 1);
  }
  
  public boolean contains(String paramString)
  {
    int i;
    if (paramString == null) {
      for (i = 0; i < fLength; i++) {
        if (fArray[((i << 1) + fOffset)] == null) {
          return true;
        }
      }
    } else {
      for (i = 0; i < fLength; i++) {
        if (paramString.equals(fArray[((i << 1) + fOffset)])) {
          return true;
        }
      }
    }
    return false;
  }
  
  public int getLength()
  {
    return fLength;
  }
  
  public String item(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fArray[((paramInt << 1) + fOffset)];
  }
  
  public Object get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < fLength)) {
      return fArray[((paramInt << 1) + fOffset)];
    }
    throw new IndexOutOfBoundsException("Index: " + paramInt);
  }
  
  public int size()
  {
    return getLength();
  }
}
