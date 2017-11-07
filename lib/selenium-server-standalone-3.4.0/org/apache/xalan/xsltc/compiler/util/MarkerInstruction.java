package org.apache.xalan.xsltc.compiler.util;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Visitor;

































abstract class MarkerInstruction
  extends Instruction
{
  public MarkerInstruction()
  {
    super((short)-1, (short)0);
  }
  







  public void accept(Visitor v) {}
  






  public final int consumeStack(ConstantPoolGen cpg)
  {
    return 0;
  }
  






  public final int produceStack(ConstantPoolGen cpg)
  {
    return 0;
  }
  





  public Instruction copy()
  {
    return this;
  }
  
  public final void dump(DataOutputStream out)
    throws IOException
  {}
}
