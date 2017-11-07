package org.apache.xerces.impl.dv.xs;

import java.util.AbstractList;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.ObjectList;

public class ListDV
  extends TypeValidator
{
  public ListDV() {}
  
  public short getAllowedFacets()
  {
    return 2079;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    return paramString;
  }
  
  public int getDataLength(Object paramObject)
  {
    return ((ListData)paramObject).getLength();
  }
  
  static final class ListData
    extends AbstractList
    implements ObjectList
  {
    final Object[] data;
    private String canonical;
    
    public ListData(Object[] paramArrayOfObject)
    {
      data = paramArrayOfObject;
    }
    
    public synchronized String toString()
    {
      if (canonical == null)
      {
        int i = data.length;
        StringBuffer localStringBuffer = new StringBuffer();
        if (i > 0) {
          localStringBuffer.append(data[0].toString());
        }
        for (int j = 1; j < i; j++)
        {
          localStringBuffer.append(' ');
          localStringBuffer.append(data[j].toString());
        }
        canonical = localStringBuffer.toString();
      }
      return canonical;
    }
    
    public int getLength()
    {
      return data.length;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof ListData)) {
        return false;
      }
      Object[] arrayOfObject = data;
      int i = data.length;
      if (i != arrayOfObject.length) {
        return false;
      }
      for (int j = 0; j < i; j++) {
        if (!data[j].equals(arrayOfObject[j])) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int i = 0;
      for (int j = 0; j < data.length; j++) {
        i ^= data[j].hashCode();
      }
      return i;
    }
    
    public boolean contains(Object paramObject)
    {
      for (int i = 0; i < data.length; i++) {
        if (paramObject == data[i]) {
          return true;
        }
      }
      return false;
    }
    
    public Object item(int paramInt)
    {
      if ((paramInt < 0) || (paramInt >= data.length)) {
        return null;
      }
      return data[paramInt];
    }
    
    public Object get(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < data.length)) {
        return data[paramInt];
      }
      throw new IndexOutOfBoundsException("Index: " + paramInt);
    }
    
    public int size()
    {
      return getLength();
    }
  }
}
