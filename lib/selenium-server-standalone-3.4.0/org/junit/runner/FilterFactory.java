package org.junit.runner;

import org.junit.runner.manipulation.Filter;







public abstract interface FilterFactory
{
  public abstract Filter createFilter(FilterFactoryParams paramFilterFactoryParams)
    throws FilterFactory.FilterNotCreatedException;
  
  public static class FilterNotCreatedException
    extends Exception
  {
    public FilterNotCreatedException(Exception exception)
    {
      super(exception);
    }
  }
}
