package net.sf.cglib.core;

import net.sf.cglib.asm..Type;

public abstract interface FieldTypeCustomizer
  extends KeyFactoryCustomizer
{
  public abstract void customize(CodeEmitter paramCodeEmitter, int paramInt, .Type paramType);
  
  public abstract .Type getOutType(int paramInt, .Type paramType);
}
