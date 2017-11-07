package org.seleniumhq.jetty9.util.preventers;

import java.lang.reflect.Method;
import org.seleniumhq.jetty9.util.log.Logger;




































public class GCThreadLeakPreventer
  extends AbstractLeakPreventer
{
  public GCThreadLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    try
    {
      Class<?> clazz = Class.forName("sun.misc.GC");
      Method requestLatency = clazz.getMethod("requestLatency", new Class[] { Long.TYPE });
      requestLatency.invoke(null, new Object[] { Long.valueOf(9223372036854775806L) });
    }
    catch (ClassNotFoundException e)
    {
      LOG.ignore(e);
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
}
