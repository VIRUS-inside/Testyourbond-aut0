package org.eclipse.jetty.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;




























public class IncludeExcludeSet<T, P>
  implements Predicate<P>
{
  private final Set<T> _includes;
  private final Predicate<P> _includePredicate;
  private final Set<T> _excludes;
  private final Predicate<P> _excludePredicate;
  
  private static class SetContainsPredicate<T>
    implements Predicate<T>
  {
    private final Set<T> set;
    
    public SetContainsPredicate(Set<T> set)
    {
      this.set = set;
    }
    

    public boolean test(T item)
    {
      return set.contains(item);
    }
  }
  



  public IncludeExcludeSet()
  {
    this(HashSet.class);
  }
  








  public <SET extends Set<T>> IncludeExcludeSet(Class<SET> setClass)
  {
    try
    {
      _includes = ((Set)setClass.newInstance());
      _excludes = ((Set)setClass.newInstance());
      
      if ((_includes instanceof Predicate))
      {
        _includePredicate = ((Predicate)_includes);
      }
      else
      {
        _includePredicate = new SetContainsPredicate(_includes);
      }
      
      if ((_excludes instanceof Predicate))
      {
        _excludePredicate = ((Predicate)_excludes);
      }
      else
      {
        _excludePredicate = new SetContainsPredicate(_excludes);
      }
    }
    catch (InstantiationException|IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
  }
  









  public <SET extends Set<T>> IncludeExcludeSet(Set<T> includeSet, Predicate<P> includePredicate, Set<T> excludeSet, Predicate<P> excludePredicate)
  {
    Objects.requireNonNull(includeSet, "Include Set");
    Objects.requireNonNull(includePredicate, "Include Predicate");
    Objects.requireNonNull(excludeSet, "Exclude Set");
    Objects.requireNonNull(excludePredicate, "Exclude Predicate");
    
    _includes = includeSet;
    _includePredicate = includePredicate;
    _excludes = excludeSet;
    _excludePredicate = excludePredicate;
  }
  
  public void include(T element)
  {
    _includes.add(element);
  }
  
  public void include(T... element)
  {
    for (T e : element) {
      _includes.add(e);
    }
  }
  
  public void exclude(T element) {
    _excludes.add(element);
  }
  
  public void exclude(T... element)
  {
    for (T e : element) {
      _excludes.add(e);
    }
  }
  
  @Deprecated
  public boolean matches(P t) {
    return test(t);
  }
  

  public boolean test(P t)
  {
    if ((!_includes.isEmpty()) && (!_includePredicate.test(t)))
      return false;
    return !_excludePredicate.test(t);
  }
  





  public Boolean isIncludedAndNotExcluded(P t)
  {
    if (_excludePredicate.test(t))
      return Boolean.FALSE;
    if (_includePredicate.test(t)) {
      return Boolean.TRUE;
    }
    return null;
  }
  
  public boolean hasIncludes()
  {
    return !_includes.isEmpty();
  }
  
  public int size()
  {
    return _includes.size() + _excludes.size();
  }
  
  public Set<T> getIncluded()
  {
    return _includes;
  }
  
  public Set<T> getExcluded()
  {
    return _excludes;
  }
  
  public void clear()
  {
    _includes.clear();
    _excludes.clear();
  }
  

  public String toString()
  {
    return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), _includes, _includePredicate, _excludes, _excludePredicate });
  }
  
  public boolean isEmpty()
  {
    return (_includes.isEmpty()) && (_excludes.isEmpty());
  }
}
