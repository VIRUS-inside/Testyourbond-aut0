package org.junit.experimental.categories;

import java.util.ArrayList;
import java.util.List;
import org.junit.internal.Classes;
import org.junit.runner.FilterFactory;
import org.junit.runner.FilterFactory.FilterNotCreatedException;
import org.junit.runner.FilterFactoryParams;
import org.junit.runner.manipulation.Filter;



abstract class CategoryFilterFactory
  implements FilterFactory
{
  CategoryFilterFactory() {}
  
  public Filter createFilter(FilterFactoryParams params)
    throws FilterFactory.FilterNotCreatedException
  {
    try
    {
      return createFilter(parseCategories(params.getArgs()));
    } catch (ClassNotFoundException e) {
      throw new FilterFactory.FilterNotCreatedException(e);
    }
  }
  


  protected abstract Filter createFilter(List<Class<?>> paramList);
  

  private List<Class<?>> parseCategories(String categories)
    throws ClassNotFoundException
  {
    List<Class<?>> categoryClasses = new ArrayList();
    
    for (String category : categories.split(",")) {
      Class<?> categoryClass = Classes.getClass(category);
      
      categoryClasses.add(categoryClass);
    }
    
    return categoryClasses;
  }
}
