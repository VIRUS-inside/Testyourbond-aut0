package org.apache.bcel.generic;






















public class LCONST
  extends Instruction
  implements ConstantPushInstruction, TypedInstruction
{
  private long value;
  





















  LCONST() {}
  





















  public LCONST(long l)
  {
    super((short)9, (short)1);
    
    if (l == 0L) {
      opcode = 9;
    } else if (l == 1L) {
      opcode = 10;
    } else {
      throw new ClassGenException("LCONST can be used only for 0 and 1: " + l);
    }
    value = l;
  }
  
  public Number getValue() { return new Long(value); }
  

  public Type getType(ConstantPoolGen cp)
  {
    return Type.LONG;
  }
  








  public void accept(Visitor v)
  {
    v.visitPushInstruction(this);
    v.visitStackProducer(this);
    v.visitTypedInstruction(this);
    v.visitConstantPushInstruction(this);
    v.visitLCONST(this);
  }
}
