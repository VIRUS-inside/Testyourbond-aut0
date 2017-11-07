package org.apache.bcel.generic;

import org.apache.bcel.classfile.ConstantPool;





































































public abstract class FieldInstruction
  extends FieldOrMethod
  implements TypedInstruction
{
  FieldInstruction() {}
  
  protected FieldInstruction(short opcode, int index)
  {
    super(opcode, index);
  }
  


  public String toString(ConstantPool cp)
  {
    return org.apache.bcel.Constants.OPCODE_NAMES[opcode] + " " + cp.constantToString(index, (byte)9);
  }
  


  protected int getFieldSize(ConstantPoolGen cpg)
  {
    return getType(cpg).getSize();
  }
  

  public Type getType(ConstantPoolGen cpg)
  {
    return getFieldType(cpg);
  }
  

  public Type getFieldType(ConstantPoolGen cpg)
  {
    return Type.getType(getSignature(cpg));
  }
  

  public String getFieldName(ConstantPoolGen cpg)
  {
    return getName(cpg);
  }
}
