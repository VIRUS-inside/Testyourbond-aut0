package org.eclipse.jetty.util.preventers;

import org.eclipse.jetty.util.log.Logger;




























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
