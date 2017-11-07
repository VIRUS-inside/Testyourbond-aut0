package org.apache.xerces.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public class XSObjectListImpl
  extends AbstractList
  implements XSObjectList
{
  public static final XSObjectListImpl EMPTY_LIST = new XSObjectListImpl(new XSObject[0], 0);
  private static final ListIterator EMPTY_ITERATOR = new ListIterator()
  {
    public boolean hasNext()
    {
      return false;
    }
    
    public Object next()
    {
      throw new NoSuchElementException();
    }
    
    public boolean hasPrevious()
    {
      return false;
    }
    
    public Object previous()
    {
      throw new NoSuchElementException();
    }
    
    public int nextIndex()
    {
      return 0;
    }
    
    public int previousIndex()
    {
      return -1;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    
    public void set(Object paramAnonymousObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public void add(Object paramAnonymousObject)
    {
      throw new UnsupportedOperationException();
    }
  };
  private static final int DEFAULT_SIZE = 4;
  private XSObject[] fArray = null;
  private int fLength = 0;
  
  public XSObjectListImpl()
  {
    fArray = new XSObject[4];
    fLength = 0;
  }
  
  public XSObjectListImpl(XSObject[] paramArrayOfXSObject, int paramInt)
  {
    fArray = paramArrayOfXSObject;
    fLength = paramInt;
  }
  
  public int getLength()
  {
    return fLength;
  }
  
  public XSObject item(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fArray[paramInt];
  }
  
  public void clearXSObjectList()
  {
    for (int i = 0; i < fLength; i++) {
      fArray[i] = null;
    }
    fArray = null;
    fLength = 0;
  }
  
  public void addXSObject(XSObject paramXSObject)
  {
    if (fLength == fArray.length)
    {
      XSObject[] arrayOfXSObject = new XSObject[fLength + 4];
      System.arraycopy(fArray, 0, arrayOfXSObject, 0, fLength);
      fArray = arrayOfXSObject;
    }
    fArray[(fLength++)] = paramXSObject;
  }
  
  public void addXSObject(int paramInt, XSObject paramXSObject)
  {
    fArray[paramInt] = paramXSObject;
  }
  
  public boolean contains(Object paramObject)
  {
    return paramObject == null ? containsNull() : containsObject(paramObject);
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
  
  public Iterator iterator()
  {
    return listIterator0(0);
  }
  
  public ListIterator listIterator()
  {
    return listIterator0(0);
  }
  
  public ListIterator listIterator(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < fLength)) {
      return listIterator0(paramInt);
    }
    throw new IndexOutOfBoundsException("Index: " + paramInt);
  }
  
  private ListIterator listIterator0(int paramInt)
  {
    return fLength == 0 ? EMPTY_ITERATOR : new XSObjectListIterator(paramInt);
  }
  
  private boolean containsObject(Object paramObject)
  {
    for (int i = fLength - 1; i >= 0; i--) {
      if (paramObject.equals(fArray[i])) {
        return true;
      }
    }
    return false;
  }
  
  private boolean containsNull()
  {
    for (int i = fLength - 1; i >= 0; i--) {
      if (fArray[i] == null) {
        return true;
      }
    }
    return false;
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
  
  private final class XSObjectListIterator
    implements ListIterator
  {
    private int index;
    
    public XSObjectListIterator(int paramInt)
    {
      index = paramInt;
    }
    
    public boolean hasNext()
    {
      return index < fLength;
    }
    
    public Object next()
    {
      if (index < fLength) {
        return fArray[(index++)];
      }
      throw new NoSuchElementException();
    }
    
    public boolean hasPrevious()
    {
      return index > 0;
    }
    
    public Object previous()
    {
      if (index > 0) {
        return fArray[(--index)];
      }
      throw new NoSuchElementException();
    }
    
    public int nextIndex()
    {
      return index;
    }
    
    public int previousIndex()
    {
      return index - 1;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    
    public void set(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public void add(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
  }
}
