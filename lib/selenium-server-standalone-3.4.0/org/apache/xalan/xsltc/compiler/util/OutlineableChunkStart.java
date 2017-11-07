package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.Instruction;






































class OutlineableChunkStart
  extends MarkerInstruction
{
  public static final Instruction OUTLINEABLECHUNKSTART = new OutlineableChunkStart();
  





  private OutlineableChunkStart() {}
  





  public String getName()
  {
    return OutlineableChunkStart.class.getName();
  }
  



  public String toString()
  {
    return getName();
  }
  



  public String toString(boolean verbose)
  {
    return getName();
  }
}
