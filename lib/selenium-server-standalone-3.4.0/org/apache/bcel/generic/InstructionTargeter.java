package org.apache.bcel.generic;

public abstract interface InstructionTargeter
{
  public abstract boolean containsTarget(InstructionHandle paramInstructionHandle);
  
  public abstract void updateTarget(InstructionHandle paramInstructionHandle1, InstructionHandle paramInstructionHandle2);
}
