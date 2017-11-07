package org.eclipse.jetty.util.preventers;

import java.sql.DriverManager;
import org.eclipse.jetty.util.log.Logger;


























public class DriverManagerLeakPreventer
  extends AbstractLeakPreventer
{
  public DriverManagerLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Pinning DriverManager classloader with " + loader, new Object[0]);
    DriverManager.getDrivers();
  }
}
