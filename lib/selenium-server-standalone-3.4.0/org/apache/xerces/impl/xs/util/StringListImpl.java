package org.apache.xerces.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Vector;
import org.apache.xerces.xs.StringList;

public final class StringListImpl
  extends AbstractList
  implements StringList
{
  public static final StringListImpl EMPTY_LIST = new StringListImpl(new String[0], 0);
  private final String[] fArray;
  private final int fLength;
  private final Vector fVector;
  
  public StringListImpl(Vector paramVector)
  {
    fVector = paramVector;
    fLength = (paramVector == null ? 0 : paramVector.size());
    fArray = null;
  }
  
  public StringListImpl(String[] paramArrayOfString, int paramInt)
  {
    fArray = paramArrayOfString;
    fLength = paramInt;
    fVector = null;
  }
  
  public int getLength()
  {
    return fLength;
  }
  
  public boolean contains(String paramString)
  {
    if (fVector != null) {
      return fVector.contains(paramString);
    }
    int i;
    if (paramString == null) {
      for (i = 0; i < fLength; i++) {
        if (fArray[i] == null) {
          return true;
        }
      }
    } else {
      for (i = 0; i < fLength; i++) {
        if (paramString.equals(fArray[i])) {
          return true;
        }
      }
    }
    return false;
  }
  
  public String item(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    if (fVector != null) {
      return (String)fVector.elementAt(paramInt);
    }
    return fArray[paramInt];
  }
  
  public Object get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < fLength))
    {
      if (fVector != null) {
        return fVector.elementAt(paramInt);
      }
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
    if (fVector != null) {
      return fVector.toArray();
    }
    Object[] arrayOfObject = new Object[fLength];
    toArray0(arrayOfObject);
    return arrayOfObject;
  }
  
  public Object[] toArray(Object[] paramArrayOfObject)
  {
    if (fVector != null) {
      return fVector.toArray(paramArrayOfObject);
    }
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
