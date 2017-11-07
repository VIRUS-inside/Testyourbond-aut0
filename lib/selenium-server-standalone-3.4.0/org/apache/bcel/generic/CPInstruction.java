package org.apache.bcel.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.util.ByteSequence;




































































public abstract class CPInstruction
  extends Instruction
  implements TypedInstruction, IndexedInstruction
{
  protected int index;
  
  CPInstruction() {}
  
  protected CPInstruction(short opcode, int index)
  {
    super(opcode, (short)3);
    setIndex(index);
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    out.writeByte(opcode);
    out.writeShort(index);
  }
  








  public String toString(boolean verbose)
  {
    return super.toString(verbose) + " " + index;
  }
  


  public String toString(ConstantPool cp)
  {
    Constant c = cp.getConstant(index);
    String str = cp.constantToString(c);
    
    if ((c instanceof ConstantClass)) {
      str = str.replace('.', '/');
    }
    return org.apache.bcel.Constants.OPCODE_NAMES[opcode] + " " + str;
  }
  





  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    setIndex(bytes.readUnsignedShort());
    length = 3;
  }
  

  public final int getIndex()
  {
    return index;
  }
  


  public void setIndex(int index)
  {
    if (index < 0) {
      throw new ClassGenException("Negative index value: " + index);
    }
    this.index = index;
  }
  

  public Type getType(ConstantPoolGen cpg)
  {
    ConstantPool cp = cpg.getConstantPool();
    String name = cp.getConstantString(index, (byte)7);
    
    if (!name.startsWith("[")) {
      name = "L" + name + ";";
    }
    return Type.getType(name);
  }
}
