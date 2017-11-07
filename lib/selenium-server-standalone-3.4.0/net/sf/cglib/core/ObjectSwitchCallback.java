package net.sf.cglib.core;

import net.sf.cglib.asm..Label;

public abstract interface ObjectSwitchCallback
{
  public abstract void processCase(Object paramObject, .Label paramLabel)
    throws Exception;
  
  public abstract void processDefault()
    throws Exception;
}
