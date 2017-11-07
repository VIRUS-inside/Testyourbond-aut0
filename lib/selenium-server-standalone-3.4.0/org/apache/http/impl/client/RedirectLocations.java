package org.apache.http.impl.client;

import java.net.URI;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

































public class RedirectLocations
  extends AbstractList<Object>
{
  private final Set<URI> unique;
  private final List<URI> all;
  
  public RedirectLocations()
  {
    unique = new HashSet();
    all = new ArrayList();
  }
  


  public boolean contains(URI uri)
  {
    return unique.contains(uri);
  }
  


  public void add(URI uri)
  {
    unique.add(uri);
    all.add(uri);
  }
  


  public boolean remove(URI uri)
  {
    boolean removed = unique.remove(uri);
    if (removed) {
      Iterator<URI> it = all.iterator();
      while (it.hasNext()) {
        URI current = (URI)it.next();
        if (current.equals(uri)) {
          it.remove();
        }
      }
    }
    return removed;
  }
  






  public List<URI> getAll()
  {
    return new ArrayList(all);
  }
  











  public URI get(int index)
  {
    return (URI)all.get(index);
  }
  








  public int size()
  {
    return all.size();
  }
  





















  public Object set(int index, Object element)
  {
    URI removed = (URI)all.set(index, (URI)element);
    unique.remove(removed);
    unique.add((URI)element);
    if (all.size() != unique.size()) {
      unique.addAll(all);
    }
    return removed;
  }
  





















  public void add(int index, Object element)
  {
    all.add(index, (URI)element);
    unique.add((URI)element);
  }
  













  public URI remove(int index)
  {
    URI removed = (URI)all.remove(index);
    unique.remove(removed);
    if (all.size() != unique.size()) {
      unique.addAll(all);
    }
    return removed;
  }
  










  public boolean contains(Object o)
  {
    return unique.contains(o);
  }
}
