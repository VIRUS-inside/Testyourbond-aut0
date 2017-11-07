package org.seleniumhq.jetty9.util.preventers;

import javax.imageio.ImageIO;
import org.seleniumhq.jetty9.util.log.Logger;

























public class AppContextLeakPreventer
  extends AbstractLeakPreventer
{
  public AppContextLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Pinning classloader for AppContext.getContext() with " + loader, new Object[0]);
    ImageIO.getUseCache();
  }
}
