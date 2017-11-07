package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.util.Base64;
import org.apache.xerces.impl.dv.util.ByteListImpl;

public class Base64BinaryDV
  extends TypeValidator
{
  public Base64BinaryDV() {}
  
  public short getAllowedFacets()
  {
    return 2079;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    byte[] arrayOfByte = Base64.decode(paramString);
    if (arrayOfByte == null) {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "base64Binary" });
    }
    return new XBase64(arrayOfByte);
  }
  
  public int getDataLength(Object paramObject)
  {
    return ((XBase64)paramObject).getLength();
  }
  
  private static final class XBase64
    extends ByteListImpl
  {
    public XBase64(byte[] paramArrayOfByte)
    {
      super();
    }
    
    public synchronized String toString()
    {
      if (canonical == null) {
        canonical = Base64.encode(data);
      }
      return canonical;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof XBase64)) {
        return false;
      }
      byte[] arrayOfByte = data;
      int i = data.length;
      if (i != arrayOfByte.length) {
        return false;
      }
      for (int j = 0; j < i; j++) {
        if (data[j] != arrayOfByte[j]) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int i = 0;
      for (int j = 0; j < data.length; j++) {
        i = i * 37 + (data[j] & 0xFF);
      }
      return i;
    }
  }
}
