package org.apache.xerces.xs.datatypes;

import java.util.List;
import org.apache.xerces.xs.XSException;

public abstract interface ByteList
  extends List
{
  public abstract int getLength();
  
  public abstract boolean contains(byte paramByte);
  
  public abstract byte item(int paramInt)
    throws XSException;
  
  public abstract byte[] toByteArray();
}
