package org.apache.xerces.impl.dv.util;

import java.util.AbstractList;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.datatypes.ByteList;

public class ByteListImpl
  extends AbstractList
  implements ByteList
{
  protected final byte[] data;
  protected String canonical;
  
  public ByteListImpl(byte[] paramArrayOfByte)
  {
    data = paramArrayOfByte;
  }
  
  public int getLength()
  {
    return data.length;
  }
  
  public boolean contains(byte paramByte)
  {
    for (int i = 0; i < data.length; i++) {
      if (data[i] == paramByte) {
        return true;
      }
    }
    return false;
  }
  
  public byte item(int paramInt)
    throws XSException
  {
    if ((paramInt < 0) || (paramInt > data.length - 1)) {
      throw new XSException((short)2, null);
    }
    return data[paramInt];
  }
  
  public Object get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < data.length)) {
      return new Byte(data[paramInt]);
    }
    throw new IndexOutOfBoundsException("Index: " + paramInt);
  }
  
  public int size()
  {
    return getLength();
  }
  
  public byte[] toByteArray()
  {
    byte[] arrayOfByte = new byte[data.length];
    System.arraycopy(data, 0, arrayOfByte, 0, data.length);
    return arrayOfByte;
  }
}
