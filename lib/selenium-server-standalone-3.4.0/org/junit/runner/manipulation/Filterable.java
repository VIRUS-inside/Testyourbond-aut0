package org.junit.runner.manipulation;

public abstract interface Filterable
{
  public abstract void filter(Filter paramFilter)
    throws NoTestsRemainException;
}
