package org.w3c.css.sac;

public abstract interface ProcessingInstructionSelector
  extends SimpleSelector
{
  public abstract String getTarget();
  
  public abstract String getData();
}
