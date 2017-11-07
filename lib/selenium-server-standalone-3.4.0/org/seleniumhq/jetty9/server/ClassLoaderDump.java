package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;


















public class ClassLoaderDump
  implements Dumpable
{
  final ClassLoader _loader;
  
  public ClassLoaderDump(ClassLoader loader)
  {
    _loader = loader;
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    if (_loader == null) {
      out.append("No ClassLoader\n");
    }
    else {
      out.append(String.valueOf(_loader)).append("\n");
      
      Object parent = _loader.getParent();
      if (parent != null)
      {
        if ((_loader instanceof URLClassLoader)) {
          ContainerLifeCycle.dump(out, indent, new Collection[] { TypeUtil.asList(((URLClassLoader)_loader).getURLs()), Collections.singleton(parent.toString()) });
        } else {
          ContainerLifeCycle.dump(out, indent, new Collection[] { Collections.singleton(parent.toString()) });
        }
      }
    }
  }
}
