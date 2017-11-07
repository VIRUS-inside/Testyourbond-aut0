package net.sf.cglib.core;

import net.sf.cglib.asm..Type;

public abstract interface HashCodeCustomizer
  extends KeyFactoryCustomizer
{
  public abstract boolean customize(CodeEmitter paramCodeEmitter, .Type paramType);
}
