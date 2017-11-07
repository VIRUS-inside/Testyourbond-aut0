package net.sf.cglib.transform.impl;

import net.sf.cglib.asm..Type;

public abstract interface InterceptFieldFilter
{
  public abstract boolean acceptRead(.Type paramType, String paramString);
  
  public abstract boolean acceptWrite(.Type paramType, String paramString);
}
