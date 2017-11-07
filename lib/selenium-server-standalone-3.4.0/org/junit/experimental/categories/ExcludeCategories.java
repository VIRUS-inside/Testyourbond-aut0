package org.junit.experimental.categories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.runner.manipulation.Filter;





















public final class ExcludeCategories
  extends CategoryFilterFactory
{
  public ExcludeCategories() {}
  
  protected Filter createFilter(List<Class<?>> categories)
  {
    return new ExcludesAny(categories);
  }
  
  private static class ExcludesAny extends Categories.CategoryFilter {
    public ExcludesAny(List<Class<?>> categories) {
      this(new HashSet(categories));
    }
    
    public ExcludesAny(Set<Class<?>> categories) {
      super(null, true, categories);
    }
    
    public String describe()
    {
      return "excludes " + super.describe();
    }
  }
}
