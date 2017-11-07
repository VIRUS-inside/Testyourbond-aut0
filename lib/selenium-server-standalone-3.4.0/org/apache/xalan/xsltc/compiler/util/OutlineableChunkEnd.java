package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.Instruction;





























class OutlineableChunkEnd
  extends MarkerInstruction
{
  public static final Instruction OUTLINEABLECHUNKEND = new OutlineableChunkEnd();
  





  private OutlineableChunkEnd() {}
  





  public String getName()
  {
    return OutlineableChunkEnd.class.getName();
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
