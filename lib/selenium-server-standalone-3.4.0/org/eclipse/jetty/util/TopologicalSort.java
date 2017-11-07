package org.eclipse.jetty.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;



































public class TopologicalSort<T>
  implements Dumpable
{
  private final Map<T, Set<T>> _dependencies = new HashMap();
  


  public TopologicalSort() {}
  

  public void addDependency(T dependent, T dependency)
  {
    Set<T> set = (Set)_dependencies.get(dependent);
    if (set == null)
    {
      set = new HashSet();
      _dependencies.put(dependent, set);
    }
    set.add(dependency);
  }
  





  public void sort(T[] array)
  {
    List<T> sorted = new ArrayList();
    Set<T> visited = new HashSet();
    Comparator<T> comparator = new InitialOrderComparator(array);
    

    for (T t : array) {
      visit(t, visited, sorted, comparator);
    }
    sorted.toArray(array);
  }
  





  public void sort(Collection<T> list)
  {
    List<T> sorted = new ArrayList();
    Set<T> visited = new HashSet();
    Comparator<T> comparator = new InitialOrderComparator(list);
    

    for (T t : list) {
      visit(t, visited, sorted, comparator);
    }
    list.clear();
    list.addAll(sorted);
  }
  







  private void visit(T item, Set<T> visited, List<T> sorted, Comparator<T> comparator)
  {
    if (!visited.contains(item))
    {

      visited.add(item);
      

      Set<T> dependencies = (Set)_dependencies.get(item);
      if (dependencies != null)
      {

        SortedSet<T> ordered_deps = new TreeSet(comparator);
        ordered_deps.addAll(dependencies);
        

        try
        {
          for (T d : ordered_deps) {
            visit(d, visited, sorted, comparator);
          }
        }
        catch (CyclicException e) {
          throw new CyclicException(item, e);
        }
      }
      



      sorted.add(item);
    }
    else if (!sorted.contains(item))
    {

      throw new CyclicException(item);
    }
  }
  




  private static class InitialOrderComparator<T>
    implements Comparator<T>
  {
    private final Map<T, Integer> _indexes = new HashMap();
    
    InitialOrderComparator(T[] initial) {
      int i = 0;
      for (T t : initial) {
        _indexes.put(t, Integer.valueOf(i++));
      }
    }
    
    InitialOrderComparator(Collection<T> initial) {
      int i = 0;
      for (T t : initial) {
        _indexes.put(t, Integer.valueOf(i++));
      }
    }
    
    public int compare(T o1, T o2)
    {
      Integer i1 = (Integer)_indexes.get(o1);
      Integer i2 = (Integer)_indexes.get(o2);
      if ((i1 == null) || (i2 == null) || (i1.equals(o2)))
        return 0;
      if (i1.intValue() < i2.intValue())
        return -1;
      return 1;
    }
  }
  


  public String toString()
  {
    return "TopologicalSort " + _dependencies;
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(String.format("TopologicalSort@%x%n", new Object[] { Integer.valueOf(hashCode()) }));
    ContainerLifeCycle.dump(out, indent, new Collection[] { _dependencies.entrySet() });
  }
  
  private static class CyclicException extends IllegalStateException
  {
    CyclicException(Object item)
    {
      super();
    }
    
    CyclicException(Object item, CyclicException e)
    {
      super(e);
    }
  }
}
