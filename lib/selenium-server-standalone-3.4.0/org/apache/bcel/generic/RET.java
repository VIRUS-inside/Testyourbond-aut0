package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;




























































public class RET
  extends Instruction
  implements IndexedInstruction, TypedInstruction
{
  private boolean wide;
  private int index;
  
  RET() {}
  
  public RET(int index)
  {
    super((short)169, (short)2);
    setIndex(index);
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    if (wide) {
      out.writeByte(196);
    }
    out.writeByte(opcode);
    
    if (wide) {
      out.writeShort(index);
    } else
      out.writeByte(index);
  }
  
  private final void setWide() {
    if ((this.wide = index > 255 ? 1 : 0) != 0) {
      length = 4;
    } else {
      length = 2;
    }
  }
  

  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    this.wide = wide;
    
    if (wide) {
      index = bytes.readUnsignedShort();
      length = 4;
    } else {
      index = bytes.readUnsignedByte();
      length = 2;
    }
  }
  

  public final int getIndex()
  {
    return index;
  }
  

  public final void setIndex(int n)
  {
    if (n < 0) {
      throw new ClassGenException("Negative index value: " + n);
    }
    index = n;
    setWide();
  }
  


  public String toString(boolean verbose)
  {
    return super.toString(verbose) + " " + index;
  }
  

  public Type getType(ConstantPoolGen cp)
  {
    return ReturnaddressType.NO_TARGET;
  }
  







  public void accept(Visitor v)
  {
    v.visitRET(this);
  }
}
