package org.seleniumhq.jetty9.http.pathmap;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.function.Predicate;






















public class PathSpecSet
  extends AbstractSet<String>
  implements Predicate<String>
{
  private final PathMappings<Boolean> specs = new PathMappings();
  
  public PathSpecSet() {}
  
  public boolean test(String s) {
    return specs.getMatch(s) != null;
  }
  


  public int size()
  {
    return specs.size();
  }
  
  private PathSpec asPathSpec(Object o)
  {
    if (o == null)
    {
      return null;
    }
    if ((o instanceof PathSpec))
    {
      return (PathSpec)o;
    }
    if ((o instanceof String))
    {
      return PathMappings.asPathSpec((String)o);
    }
    return PathMappings.asPathSpec(o.toString());
  }
  

  public boolean add(String s)
  {
    return specs.put(PathMappings.asPathSpec(s), Boolean.TRUE);
  }
  

  public boolean remove(Object o)
  {
    return specs.remove(asPathSpec(o));
  }
  

  public void clear()
  {
    specs.reset();
  }
  


  public Iterator<String> iterator()
  {
    final Iterator<MappedResource<Boolean>> iterator = specs.iterator();
    new Iterator()
    {

      public boolean hasNext()
      {
        return iterator.hasNext();
      }
      

      public String next()
      {
        return ((MappedResource)iterator.next()).getPathSpec().getDeclaration();
      }
    };
  }
}
