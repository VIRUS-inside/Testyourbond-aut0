package org.seleniumhq.jetty9.http.pathmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.seleniumhq.jetty9.util.ArrayTernaryTrie;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

























@ManagedObject("Path Mappings")
public class PathMappings<E>
  implements Iterable<MappedResource<E>>, Dumpable
{
  private static final Logger LOG = Log.getLogger(PathMappings.class);
  private final Set<MappedResource<E>> _mappings = new TreeSet();
  
  private Trie<MappedResource<E>> _exactMap = new ArrayTernaryTrie(false);
  private Trie<MappedResource<E>> _prefixMap = new ArrayTernaryTrie(false);
  private Trie<MappedResource<E>> _suffixMap = new ArrayTernaryTrie(false);
  
  public PathMappings() {}
  
  public String dump() {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    ContainerLifeCycle.dump(out, indent, new Collection[] { _mappings });
  }
  
  @ManagedAttribute(value="mappings", readonly=true)
  public List<MappedResource<E>> getMappings()
  {
    return new ArrayList(_mappings);
  }
  
  public int size()
  {
    return _mappings.size();
  }
  
  public void reset()
  {
    _mappings.clear();
    _prefixMap.clear();
    _suffixMap.clear();
  }
  
  public void removeIf(Predicate<MappedResource<E>> predicate)
  {
    _mappings.removeIf(predicate);
  }
  






  public List<MappedResource<E>> getMatches(String path)
  {
    boolean isRootPath = "/".equals(path);
    
    List<MappedResource<E>> ret = new ArrayList();
    for (MappedResource<E> mr : _mappings)
    {
      switch (1.$SwitchMap$org$eclipse$jetty$http$pathmap$PathSpecGroup[getPathSpecgroup.ordinal()])
      {
      case 1: 
        if (isRootPath)
          ret.add(mr);
        break;
      case 2: 
        if ((isRootPath) || (mr.getPathSpec().matches(path)))
          ret.add(mr);
        break;
      default: 
        if (mr.getPathSpec().matches(path))
          ret.add(mr);
        break;
      }
    }
    return ret;
  }
  
  public MappedResource<E> getMatch(String path)
  {
    PathSpecGroup last_group = null;
    

    for (MappedResource<E> mr : _mappings)
    {
      PathSpecGroup group = mr.getPathSpec().getGroup();
      if (group != last_group) {
        int i;
        Trie<MappedResource<E>> exact_map;
        switch (1.$SwitchMap$org$eclipse$jetty$http$pathmap$PathSpecGroup[group.ordinal()])
        {

        case 3: 
          i = path.length();
          exact_map = _exactMap;
        case 4: case 5:  while (i >= 0)
          {
            MappedResource<E> candidate = (MappedResource)exact_map.getBest(path, 0, i);
            if (candidate != null)
            {
              if (candidate.getPathSpec().matches(path))
                return candidate;
              i = candidate.getPathSpec().getPrefix().length() - 1;
              continue;
              




              int i = path.length();
              Trie<MappedResource<E>> prefix_map = _prefixMap;
              while (i >= 0)
              {
                MappedResource<E> candidate = (MappedResource)prefix_map.getBest(path, 0, i);
                if (candidate != null)
                {
                  if (candidate.getPathSpec().matches(path))
                    return candidate;
                  i = candidate.getPathSpec().getPrefix().length() - 1;
                  continue;
                  




                  int i = 0;
                  Trie<MappedResource<E>> suffix_map = _suffixMap;
                  while ((i = path.indexOf('.', i + 1)) > 0)
                  {
                    MappedResource<E> candidate = (MappedResource)suffix_map.get(path, i + 1, path.length() - i - 1);
                    if ((candidate != null) && (candidate.getPathSpec().matches(path)))
                      return candidate;
                  }
                }
              }
            }
          }
        }
        
      }
      if (mr.getPathSpec().matches(path)) {
        return mr;
      }
      last_group = group;
    }
    
    return null;
  }
  

  public Iterator<MappedResource<E>> iterator()
  {
    return _mappings.iterator();
  }
  
  public static PathSpec asPathSpec(String pathSpecString)
  {
    if ((pathSpecString == null) || (pathSpecString.length() < 1))
    {
      throw new RuntimeException("Path Spec String must start with '^', '/', or '*.': got [" + pathSpecString + "]");
    }
    return pathSpecString.charAt(0) == '^' ? new RegexPathSpec(pathSpecString) : new ServletPathSpec(pathSpecString);
  }
  
  public boolean put(String pathSpecString, E resource)
  {
    return put(asPathSpec(pathSpecString), resource);
  }
  
  public boolean put(PathSpec pathSpec, E resource)
  {
    MappedResource<E> entry = new MappedResource(pathSpec, resource);
    String exact; switch (1.$SwitchMap$org$eclipse$jetty$http$pathmap$PathSpecGroup[group.ordinal()])
    {
    case 3: 
      exact = pathSpec.getPrefix();
    case 4: case 5:  while ((exact != null) && (!_exactMap.put(exact, entry))) {
        _exactMap = new ArrayTernaryTrie((ArrayTernaryTrie)_exactMap, 1.5D); continue;
        

        String prefix = pathSpec.getPrefix();
        while ((prefix != null) && (!_prefixMap.put(prefix, entry))) {
          _prefixMap = new ArrayTernaryTrie((ArrayTernaryTrie)_prefixMap, 1.5D); continue;
          

          String suffix = pathSpec.getSuffix();
          while ((suffix != null) && (!_suffixMap.put(suffix, entry))) {
            _suffixMap = new ArrayTernaryTrie((ArrayTernaryTrie)_prefixMap, 1.5D);
          }
        }
      }
    }
    boolean added = _mappings.add(entry);
    if (LOG.isDebugEnabled())
      LOG.debug("{} {} to {}", new Object[] { added ? "Added" : "Ignored", entry, this });
    return added;
  }
  

  public boolean remove(PathSpec pathSpec)
  {
    switch (1.$SwitchMap$org$eclipse$jetty$http$pathmap$PathSpecGroup[group.ordinal()])
    {
    case 3: 
      _exactMap.remove(pathSpec.getPrefix());
      break;
    case 4: 
      _prefixMap.remove(pathSpec.getPrefix());
      break;
    case 5: 
      _suffixMap.remove(pathSpec.getSuffix());
    }
    
    
    Iterator<MappedResource<E>> iter = _mappings.iterator();
    boolean removed = false;
    while (iter.hasNext())
    {
      if (((MappedResource)iter.next()).getPathSpec().equals(pathSpec))
      {
        removed = true;
        iter.remove();
      }
    }
    
    if (LOG.isDebugEnabled())
      LOG.debug("{} {} to {}", new Object[] { removed ? "Removed" : "Ignored", pathSpec, this });
    return removed;
  }
  

  public String toString()
  {
    return String.format("%s[size=%d]", new Object[] { getClass().getSimpleName(), Integer.valueOf(_mappings.size()) });
  }
}
