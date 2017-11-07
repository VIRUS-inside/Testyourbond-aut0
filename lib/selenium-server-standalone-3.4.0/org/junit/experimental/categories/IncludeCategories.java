package org.junit.experimental.categories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.runner.manipulation.Filter;





















public final class IncludeCategories
  extends CategoryFilterFactory
{
  public IncludeCategories() {}
  
  protected Filter createFilter(List<Class<?>> categories)
  {
    return new IncludesAny(categories);
  }
  
  private static class IncludesAny extends Categories.CategoryFilter {
    public IncludesAny(List<Class<?>> categories) {
      this(new HashSet(categories));
    }
    
    public IncludesAny(Set<Class<?>> categories) {
      super(categories, true, null);
    }
    
    public String describe()
    {
      return "includes " + super.describe();
    }
  }
}
