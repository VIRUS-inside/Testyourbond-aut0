package org.apache.bcel.verifier.structurals;

import java.util.ArrayList;
import org.apache.bcel.generic.InstructionHandle;

public abstract interface InstructionContext
{
  public abstract int getTag();
  
  public abstract void setTag(int paramInt);
  
  public abstract boolean execute(Frame paramFrame, ArrayList paramArrayList, InstConstraintVisitor paramInstConstraintVisitor, ExecutionVisitor paramExecutionVisitor);
  
  public abstract Frame getOutFrame(ArrayList paramArrayList);
  
  public abstract InstructionHandle getInstruction();
  
  public abstract InstructionContext[] getSuccessors();
  
  public abstract ExceptionHandler[] getExceptionHandlers();
}
