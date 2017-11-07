package com.steadystate.css.parser;

import org.w3c.css.sac.Parser;

public abstract interface SACParser
  extends Parser
{
  public abstract void setIeStarHackAccepted(boolean paramBoolean);
  
  public abstract boolean isIeStarHackAccepted();
}
