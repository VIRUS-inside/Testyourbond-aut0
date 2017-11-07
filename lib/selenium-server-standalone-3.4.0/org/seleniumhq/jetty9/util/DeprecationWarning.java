package org.seleniumhq.jetty9.util;

import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


















public class DeprecationWarning
  implements Decorator
{
  private static final Logger LOG = Log.getLogger(DeprecationWarning.class);
  
  public DeprecationWarning() {}
  
  public <T> T decorate(T o) {
    if (o == null)
    {
      return null;
    }
    
    Class<?> clazz = o.getClass();
    
    try
    {
      Deprecated depr = (Deprecated)clazz.getAnnotation(Deprecated.class);
      if (depr != null)
      {
        LOG.warn("Using @Deprecated Class {}", new Object[] { clazz.getName() });
      }
    }
    catch (Throwable t)
    {
      LOG.ignore(t);
    }
    
    verifyIndirectTypes(clazz.getSuperclass(), clazz, "Class");
    for (Class<?> ifaceClazz : clazz.getInterfaces())
    {
      verifyIndirectTypes(ifaceClazz, clazz, "Interface");
    }
    
    return o;
  }
  

  private void verifyIndirectTypes(Class<?> superClazz, Class<?> clazz, String typeName)
  {
    try
    {
      while ((superClazz != null) && (superClazz != Object.class))
      {
        Deprecated supDepr = (Deprecated)superClazz.getAnnotation(Deprecated.class);
        if (supDepr != null)
        {
          LOG.warn("Using indirect @Deprecated {} {} - (seen from {})", new Object[] { typeName, superClazz.getName(), clazz });
        }
        
        superClazz = superClazz.getSuperclass();
      }
    }
    catch (Throwable t)
    {
      LOG.ignore(t);
    }
  }
  
  public void destroy(Object o) {}
}
