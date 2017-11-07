package org.seleniumhq.jetty9.servlet.listener;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.seleniumhq.jetty9.util.Loader;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





























public class ELContextCleaner
  implements ServletContextListener
{
  private static final Logger LOG = Log.getLogger(ELContextCleaner.class);
  

  public ELContextCleaner() {}
  

  public void contextInitialized(ServletContextEvent sce) {}
  
  public void contextDestroyed(ServletContextEvent sce)
  {
    try
    {
      Class<?> beanELResolver = Loader.loadClass("javax.el.BeanELResolver");
      

      Field field = getField(beanELResolver);
      

      purgeEntries(field);
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("javax.el.BeanELResolver purged", new Object[0]);

      }
      

    }
    catch (ClassNotFoundException localClassNotFoundException) {}catch (SecurityException|IllegalArgumentException|IllegalAccessException e)
    {

      LOG.warn("Cannot purge classes from javax.el.BeanELResolver", e);
    }
    catch (NoSuchFieldException e)
    {
      LOG.debug("Not cleaning cached beans: no such field javax.el.BeanELResolver.properties", new Object[0]);
    }
  }
  


  protected Field getField(Class<?> beanELResolver)
    throws SecurityException, NoSuchFieldException
  {
    if (beanELResolver == null) {
      return null;
    }
    return beanELResolver.getDeclaredField("properties");
  }
  
  protected void purgeEntries(Field properties)
    throws IllegalArgumentException, IllegalAccessException
  {
    if (properties == null) {
      return;
    }
    if (!properties.isAccessible()) {
      properties.setAccessible(true);
    }
    Map map = (Map)properties.get(null);
    if (map == null) {
      return;
    }
    Iterator<Class<?>> itor = map.keySet().iterator();
    while (itor.hasNext())
    {
      Class<?> clazz = (Class)itor.next();
      if (LOG.isDebugEnabled())
        LOG.debug("Clazz: " + clazz + " loaded by " + clazz.getClassLoader(), new Object[0]);
      if (Thread.currentThread().getContextClassLoader().equals(clazz.getClassLoader()))
      {
        itor.remove();
        if (LOG.isDebugEnabled()) {
          LOG.debug("removed", new Object[0]);
        }
        
      }
      else if (LOG.isDebugEnabled()) {
        LOG.debug("not removed: contextclassloader=" + Thread.currentThread().getContextClassLoader() + "clazz's classloader=" + clazz.getClassLoader(), new Object[0]);
      }
    }
  }
}
