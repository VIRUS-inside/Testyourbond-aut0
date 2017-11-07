package org.seleniumhq.jetty9.util;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

























public class Uptime
{
  public static final int NOIMPL = -1;
  
  public static class DefaultImpl
    implements Uptime.Impl
  {
    public Object mxBean;
    public Method uptimeMethod;
    
    public DefaultImpl()
    {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      try
      {
        Class<?> mgmtFactory = Class.forName("java.lang.management.ManagementFactory", true, cl);
        Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, cl);
        Class<?>[] noparams = new Class[0];
        Method mxBeanMethod = mgmtFactory.getMethod("getRuntimeMXBean", noparams);
        if (mxBeanMethod == null)
        {
          throw new UnsupportedOperationException("method getRuntimeMXBean() not found");
        }
        mxBean = mxBeanMethod.invoke(mgmtFactory, new Object[0]);
        if (mxBean == null)
        {
          throw new UnsupportedOperationException("getRuntimeMXBean() method returned null");
        }
        uptimeMethod = runtimeClass.getMethod("getUptime", noparams);
        if (mxBean == null)
        {
          throw new UnsupportedOperationException("method getUptime() not found");

        }
        


      }
      catch (ClassNotFoundException|NoClassDefFoundError|NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
      {


        throw new UnsupportedOperationException("Implementation not available in this environment", e);
      }
    }
    

    public long getUptime()
    {
      try
      {
        return ((Long)uptimeMethod.invoke(mxBean, new Object[0])).longValue();
      }
      catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {}
      
      return -1L;
    }
  }
  

  private static final Uptime INSTANCE = new Uptime();
  private Impl impl;
  
  public static Uptime getInstance() {
    return INSTANCE;
  }
  


  private Uptime()
  {
    try
    {
      impl = new DefaultImpl();
    }
    catch (UnsupportedOperationException e)
    {
      System.err.printf("Defaulting Uptime to NOIMPL due to (%s) %s%n", new Object[] { e.getClass().getName(), e.getMessage() });
      impl = null;
    }
  }
  
  public Impl getImpl()
  {
    return impl;
  }
  
  public void setImpl(Impl impl)
  {
    this.impl = impl;
  }
  
  public static long getUptime()
  {
    Uptime u = getInstance();
    if ((u == null) || (impl == null))
    {
      return -1L;
    }
    return impl.getUptime();
  }
  
  public static abstract interface Impl
  {
    public abstract long getUptime();
  }
}
