package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;






























































public final class StackMapType
  implements Cloneable
{
  private byte type;
  private int index = -1;
  

  private ConstantPool constant_pool;
  


  StackMapType(DataInputStream file, ConstantPool constant_pool)
    throws IOException
  {
    this(file.readByte(), -1, constant_pool);
    
    if (hasIndex()) {
      setIndex(file.readShort());
    }
    setConstantPool(constant_pool);
  }
  



  public StackMapType(byte type, int index, ConstantPool constant_pool)
  {
    setType(type);
    setIndex(index);
    setConstantPool(constant_pool);
  }
  
  public void setType(byte t) {
    if ((t < 0) || (t > 8))
      throw new RuntimeException("Illegal type for StackMapType: " + t);
    type = t;
  }
  
  public byte getType() { return type; }
  public void setIndex(int t) { index = t; }
  

  public int getIndex()
  {
    return index;
  }
  




  public final void dump(DataOutputStream file)
    throws IOException
  {
    file.writeByte(type);
    if (hasIndex()) {
      file.writeShort(getIndex());
    }
  }
  
  public final boolean hasIndex()
  {
    return (type == 7) || (type == 8);
  }
  
  private String printIndex()
  {
    if (type == 7)
      return ", class=" + constant_pool.constantToString(index, (byte)7);
    if (type == 8) {
      return ", offset=" + index;
    }
    return "";
  }
  


  public final String toString()
  {
    return "(type=" + org.apache.bcel.Constants.ITEM_NAMES[type] + printIndex() + ")";
  }
  

  public StackMapType copy()
  {
    try
    {
      return (StackMapType)clone();
    }
    catch (CloneNotSupportedException e) {}
    return null;
  }
  

  public final ConstantPool getConstantPool()
  {
    return constant_pool;
  }
  

  public final void setConstantPool(ConstantPool constant_pool)
  {
    this.constant_pool = constant_pool;
  }
}
