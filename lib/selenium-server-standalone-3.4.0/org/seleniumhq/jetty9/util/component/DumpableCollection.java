package org.seleniumhq.jetty9.util.component;

import java.io.IOException;
import java.util.Collection;





















public class DumpableCollection
  implements Dumpable
{
  private final String _name;
  private final Collection<?> _collection;
  
  public DumpableCollection(String name, Collection<?> collection)
  {
    _name = name;
    _collection = collection;
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(_name).append("\n");
    if (_collection != null) {
      ContainerLifeCycle.dump(out, indent, new Collection[] { _collection });
    }
  }
}
