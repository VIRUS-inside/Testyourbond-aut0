package org.seleniumhq.jetty9.util.preventers;

import org.seleniumhq.jetty9.util.log.Logger;




























public class Java2DLeakPreventer
  extends AbstractLeakPreventer
{
  public Java2DLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    try
    {
      Class.forName("sun.java2d.Disposer", true, loader);
    }
    catch (ClassNotFoundException e)
    {
      LOG.ignore(e);
    }
  }
}
