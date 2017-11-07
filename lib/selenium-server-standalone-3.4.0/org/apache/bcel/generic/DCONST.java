package org.apache.bcel.generic;






















public class DCONST
  extends Instruction
  implements ConstantPushInstruction, TypedInstruction
{
  private double value;
  





















  DCONST() {}
  





















  public DCONST(double f)
  {
    super((short)14, (short)1);
    
    if (f == 0.0D) {
      opcode = 14;
    } else if (f == 1.0D) {
      opcode = 15;
    } else {
      throw new ClassGenException("DCONST can be used only for 0.0 and 1.0: " + f);
    }
    value = f;
  }
  
  public Number getValue() { return new Double(value); }
  

  public Type getType(ConstantPoolGen cp)
  {
    return Type.DOUBLE;
  }
  








  public void accept(Visitor v)
  {
    v.visitPushInstruction(this);
    v.visitStackProducer(this);
    v.visitTypedInstruction(this);
    v.visitConstantPushInstruction(this);
    v.visitDCONST(this);
  }
}
