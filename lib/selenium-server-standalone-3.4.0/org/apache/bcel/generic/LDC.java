package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.ExceptionConstants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.util.ByteSequence;






















































public class LDC
  extends CPInstruction
  implements PushInstruction, ExceptionThrower, TypedInstruction
{
  LDC() {}
  
  public LDC(int index)
  {
    super((short)19, index);
    setSize();
  }
  
  protected final void setSize()
  {
    if (index <= 255) {
      opcode = 18;
      length = 2;
    } else {
      opcode = 19;
      length = 3;
    }
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    out.writeByte(opcode);
    
    if (length == 2) {
      out.writeByte(index);
    } else {
      out.writeShort(index);
    }
  }
  

  public final void setIndex(int index)
  {
    super.setIndex(index);
    setSize();
  }
  



  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    length = 2;
    index = bytes.readUnsignedByte();
  }
  
  public Object getValue(ConstantPoolGen cpg) {
    Constant c = cpg.getConstantPool().getConstant(index);
    
    switch (c.getTag()) {
    case 8: 
      int i = ((ConstantString)c).getStringIndex();
      c = cpg.getConstantPool().getConstant(i);
      return ((ConstantUtf8)c).getBytes();
    
    case 4: 
      return new Float(((ConstantFloat)c).getBytes());
    
    case 3: 
      return new Integer(((ConstantInteger)c).getBytes());
    }
    
    throw new RuntimeException("Unknown or invalid constant type at " + index);
  }
  
  public Type getType(ConstantPoolGen cpg)
  {
    switch (cpg.getConstantPool().getConstant(index).getTag()) {
    case 8:  return Type.STRING;
    case 4:  return Type.FLOAT;
    case 3:  return Type.INT;
    }
    throw new RuntimeException("Unknown or invalid constant type at " + index);
  }
  
  public Class[] getExceptions()
  {
    return ExceptionConstants.EXCS_STRING_RESOLUTION;
  }
  







  public void accept(Visitor v)
  {
    v.visitStackProducer(this);
    v.visitPushInstruction(this);
    v.visitExceptionThrower(this);
    v.visitTypedInstruction(this);
    v.visitCPInstruction(this);
    v.visitLDC(this);
  }
}
