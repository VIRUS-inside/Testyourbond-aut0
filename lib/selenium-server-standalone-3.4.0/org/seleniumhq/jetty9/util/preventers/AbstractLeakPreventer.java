package org.seleniumhq.jetty9.util.preventers;

import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;































public abstract class AbstractLeakPreventer
  extends AbstractLifeCycle
{
  protected static final Logger LOG = Log.getLogger(AbstractLeakPreventer.class);
  

  public AbstractLeakPreventer() {}
  
  public abstract void prevent(ClassLoader paramClassLoader);
  
  protected void doStart()
    throws Exception
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      prevent(getClass().getClassLoader());
      super.doStart();
      


      Thread.currentThread().setContextClassLoader(loader); } finally { Thread.currentThread().setContextClassLoader(loader);
    }
  }
}
