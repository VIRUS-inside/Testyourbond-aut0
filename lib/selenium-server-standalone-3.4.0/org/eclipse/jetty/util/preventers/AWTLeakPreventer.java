package org.eclipse.jetty.util.preventers;

import java.awt.Toolkit;
import org.eclipse.jetty.util.log.Logger;































public class AWTLeakPreventer
  extends AbstractLeakPreventer
{
  public AWTLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Pinning classloader for java.awt.EventQueue using " + loader, new Object[0]);
    Toolkit.getDefaultToolkit();
  }
}
