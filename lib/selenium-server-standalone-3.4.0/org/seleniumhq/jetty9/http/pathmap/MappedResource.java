package org.seleniumhq.jetty9.http.pathmap;

import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;


















@ManagedObject("Mapped Resource")
public class MappedResource<E>
  implements Comparable<MappedResource<E>>
{
  private final PathSpec pathSpec;
  private final E resource;
  
  public MappedResource(PathSpec pathSpec, E resource)
  {
    this.pathSpec = pathSpec;
    this.resource = resource;
  }
  




  public int compareTo(MappedResource<E> other)
  {
    return pathSpec.compareTo(pathSpec);
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    MappedResource<?> other = (MappedResource)obj;
    if (pathSpec == null)
    {
      if (pathSpec != null)
      {
        return false;
      }
    }
    else if (!pathSpec.equals(pathSpec))
    {
      return false;
    }
    return true;
  }
  
  @ManagedAttribute(value="path spec", readonly=true)
  public PathSpec getPathSpec()
  {
    return pathSpec;
  }
  
  @ManagedAttribute(value="resource", readonly=true)
  public E getResource()
  {
    return resource;
  }
  

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (pathSpec == null ? 0 : pathSpec.hashCode());
    return result;
  }
  

  public String toString()
  {
    return String.format("MappedResource[pathSpec=%s,resource=%s]", new Object[] { pathSpec, resource });
  }
}
