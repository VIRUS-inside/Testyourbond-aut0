package org.apache.bcel.generic;

public abstract interface ConstantPushInstruction
  extends PushInstruction, TypedInstruction
{
  public abstract Number getValue();
}
