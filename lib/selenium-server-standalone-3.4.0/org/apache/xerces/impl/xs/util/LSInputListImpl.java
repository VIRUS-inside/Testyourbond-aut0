package org.apache.xerces.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import org.apache.xerces.xs.LSInputList;
import org.w3c.dom.ls.LSInput;

public final class LSInputListImpl
  extends AbstractList
  implements LSInputList
{
  public static final LSInputListImpl EMPTY_LIST = new LSInputListImpl(new LSInput[0], 0);
  private final LSInput[] fArray;
  private final int fLength;
  
  public LSInputListImpl(LSInput[] paramArrayOfLSInput, int paramInt)
  {
    fArray = paramArrayOfLSInput;
    fLength = paramInt;
  }
  
  public int getLength()
  {
    return fLength;
  }
  
  public LSInput item(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fArray[paramInt];
  }
  
  public Object get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < fLength)) {
      return fArray[paramInt];
    }
    throw new IndexOutOfBoundsException("Index: " + paramInt);
  }
  
  public int size()
  {
    return getLength();
  }
  
  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[fLength];
    toArray0(arrayOfObject);
    return arrayOfObject;
  }
  
  public Object[] toArray(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject.length < fLength)
    {
      Class localClass1 = paramArrayOfObject.getClass();
      Class localClass2 = localClass1.getComponentType();
      paramArrayOfObject = (Object[])Array.newInstance(localClass2, fLength);
    }
    toArray0(paramArrayOfObject);
    if (paramArrayOfObject.length > fLength) {
      paramArrayOfObject[fLength] = null;
    }
    return paramArrayOfObject;
  }
  
  private void toArray0(Object[] paramArrayOfObject)
  {
    if (fLength > 0) {
      System.arraycopy(fArray, 0, paramArrayOfObject, 0, fLength);
    }
  }
}
