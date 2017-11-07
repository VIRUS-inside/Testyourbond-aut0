package org.junit.runner;

import java.lang.reflect.Constructor;
import org.junit.internal.Classes;
import org.junit.runner.manipulation.Filter;










class FilterFactories
{
  FilterFactories() {}
  
  public static Filter createFilterFromFilterSpec(Request request, String filterSpec)
    throws FilterFactory.FilterNotCreatedException
  {
    Description topLevelDescription = request.getRunner().getDescription();
    String[] tuple;
    String[] tuple;
    if (filterSpec.contains("=")) {
      tuple = filterSpec.split("=", 2);
    } else {
      tuple = new String[] { filterSpec, "" };
    }
    
    return createFilter(tuple[0], new FilterFactoryParams(topLevelDescription, tuple[1]));
  }
  





  public static Filter createFilter(String filterFactoryFqcn, FilterFactoryParams params)
    throws FilterFactory.FilterNotCreatedException
  {
    FilterFactory filterFactory = createFilterFactory(filterFactoryFqcn);
    
    return filterFactory.createFilter(params);
  }
  






  public static Filter createFilter(Class<? extends FilterFactory> filterFactoryClass, FilterFactoryParams params)
    throws FilterFactory.FilterNotCreatedException
  {
    FilterFactory filterFactory = createFilterFactory(filterFactoryClass);
    
    return filterFactory.createFilter(params);
  }
  
  static FilterFactory createFilterFactory(String filterFactoryFqcn) throws FilterFactory.FilterNotCreatedException
  {
    Class<? extends FilterFactory> filterFactoryClass;
    try {
      filterFactoryClass = Classes.getClass(filterFactoryFqcn).asSubclass(FilterFactory.class);
    } catch (Exception e) {
      throw new FilterFactory.FilterNotCreatedException(e);
    }
    
    return createFilterFactory(filterFactoryClass);
  }
  
  static FilterFactory createFilterFactory(Class<? extends FilterFactory> filterFactoryClass) throws FilterFactory.FilterNotCreatedException
  {
    try {
      return (FilterFactory)filterFactoryClass.getConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (Exception e) {
      throw new FilterFactory.FilterNotCreatedException(e);
    }
  }
}
