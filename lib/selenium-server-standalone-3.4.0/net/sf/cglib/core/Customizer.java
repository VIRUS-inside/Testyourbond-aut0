package net.sf.cglib.core;

import net.sf.cglib.asm..Type;

public abstract interface Customizer
  extends KeyFactoryCustomizer
{
  public abstract void customize(CodeEmitter paramCodeEmitter, .Type paramType);
}
