package org.eclipse.jetty.util;

import java.security.AccessController;
import java.security.PrivilegedAction;





















public class MemoryUtils
{
  private static final int cacheLineBytes;
  
  static
  {
    int defaultValue = 64;
    int value = 64;
    try
    {
      value = Integer.parseInt((String)AccessController.doPrivileged(new PrivilegedAction()
      {

        public String run()
        {
          return System.getProperty("org.eclipse.jetty.util.cacheLineBytes", String.valueOf(64));
        }
      }));
    }
    catch (Exception localException) {}
    

    cacheLineBytes = value;
  }
  




  public static int getCacheLineBytes()
  {
    return cacheLineBytes;
  }
  
  public static int getIntegersPerCacheLine()
  {
    return getCacheLineBytes() >> 2;
  }
  
  public static int getLongsPerCacheLine()
  {
    return getCacheLineBytes() >> 3;
  }
  
  private MemoryUtils() {}
}
