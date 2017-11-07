package net.sf.cglib.core;

import net.sf.cglib.asm..Label;

public abstract interface ProcessSwitchCallback
{
  public abstract void processCase(int paramInt, .Label paramLabel)
    throws Exception;
  
  public abstract void processDefault()
    throws Exception;
}
