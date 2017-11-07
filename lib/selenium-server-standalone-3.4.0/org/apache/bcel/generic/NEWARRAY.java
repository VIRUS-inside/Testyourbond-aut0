package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.ExceptionConstants;
import org.apache.bcel.util.ByteSequence;




























































public class NEWARRAY
  extends Instruction
  implements AllocationInstruction, ExceptionThrower, StackProducer
{
  private byte type;
  
  NEWARRAY() {}
  
  public NEWARRAY(byte type)
  {
    super((short)188, (short)2);
    this.type = type;
  }
  
  public NEWARRAY(BasicType type) {
    this(type.getType());
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    out.writeByte(opcode);
    out.writeByte(type);
  }
  

  public final byte getTypecode()
  {
    return type;
  }
  

  public final Type getType()
  {
    return new ArrayType(BasicType.getType(type), 1);
  }
  


  public String toString(boolean verbose)
  {
    return super.toString(verbose) + " " + org.apache.bcel.Constants.TYPE_NAMES[type];
  }
  

  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    type = bytes.readByte();
    length = 2;
  }
  
  public Class[] getExceptions() {
    return new Class[] { ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION };
  }
  








  public void accept(Visitor v)
  {
    v.visitAllocationInstruction(this);
    v.visitExceptionThrower(this);
    v.visitStackProducer(this);
    v.visitNEWARRAY(this);
  }
}
