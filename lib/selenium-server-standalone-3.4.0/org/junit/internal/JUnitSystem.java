package org.junit.internal;

import java.io.PrintStream;

public abstract interface JUnitSystem
{
  @Deprecated
  public abstract void exit(int paramInt);
  
  public abstract PrintStream out();
}
