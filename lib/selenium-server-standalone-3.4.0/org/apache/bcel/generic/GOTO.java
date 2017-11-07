package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;



























































public class GOTO
  extends GotoInstruction
  implements VariableLengthInstruction
{
  GOTO() {}
  
  public GOTO(InstructionHandle target)
  {
    super((short)167, target);
  }
  


  public void dump(DataOutputStream out)
    throws IOException
  {
    index = getTargetOffset();
    if (opcode == 167) {
      super.dump(out);
    } else {
      index = getTargetOffset();
      out.writeByte(opcode);
      out.writeInt(index);
    }
  }
  


  protected int updatePosition(int offset, int max_offset)
  {
    int i = getTargetOffset();
    
    position += offset;
    
    if (Math.abs(i) >= 32767 - max_offset) {
      opcode = 200;
      length = 5;
      return 2;
    }
    
    return 0;
  }
  







  public void accept(Visitor v)
  {
    v.visitVariableLengthInstruction(this);
    v.visitUnconditionalBranch(this);
    v.visitBranchInstruction(this);
    v.visitGotoInstruction(this);
    v.visitGOTO(this);
  }
}
