package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

































































public final class ConstantInteger
  extends Constant
  implements ConstantObject
{
  private int bytes;
  
  public ConstantInteger(int bytes)
  {
    super((byte)3);
    this.bytes = bytes;
  }
  


  public ConstantInteger(ConstantInteger c)
  {
    this(c.getBytes());
  }
  





  ConstantInteger(DataInputStream file)
    throws IOException
  {
    this(file.readInt());
  }
  






  public void accept(Visitor v)
  {
    v.visitConstantInteger(this);
  }
  





  public final void dump(DataOutputStream file)
    throws IOException
  {
    file.writeByte(tag);
    file.writeInt(bytes);
  }
  

  public final int getBytes()
  {
    return bytes;
  }
  

  public final void setBytes(int bytes)
  {
    this.bytes = bytes;
  }
  


  public final String toString()
  {
    return super.toString() + "(bytes = " + bytes + ")";
  }
  

  public Object getConstantValue(ConstantPool cp)
  {
    return new Integer(bytes);
  }
}
