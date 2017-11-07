package org.apache.bcel.verifier.structurals;

import org.apache.bcel.generic.InstructionHandle;

public abstract interface Subroutine
{
  public abstract InstructionHandle[] getEnteringJsrInstructions();
  
  public abstract InstructionHandle getLeavingRET();
  
  public abstract InstructionHandle[] getInstructions();
  
  public abstract boolean contains(InstructionHandle paramInstructionHandle);
  
  public abstract int[] getAccessedLocalsIndices();
  
  public abstract int[] getRecursivelyAccessedLocalsIndices();
  
  public abstract Subroutine[] subSubs();
}
