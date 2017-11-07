package org.apache.commons.exec.util;

import java.io.PrintStream;






































public class DebugUtils
{
  public static final String COMMONS_EXEC_LENIENT = "org.apache.commons.exec.lenient";
  public static final String COMMONS_EXEC_DEBUG = "org.apache.commons.exec.debug";
  
  public DebugUtils() {}
  
  public static void handleException(String msg, Exception e)
  {
    if (isDebugEnabled()) {
      System.err.println(msg);
      e.printStackTrace();
    }
    
    if (!isLenientEnabled()) {
      if ((e instanceof RuntimeException)) {
        throw ((RuntimeException)e);
      }
      
      throw new RuntimeException(e.getMessage());
    }
  }
  





  public static boolean isDebugEnabled()
  {
    String debug = System.getProperty("org.apache.commons.exec.debug", Boolean.FALSE.toString());
    return Boolean.TRUE.toString().equalsIgnoreCase(debug);
  }
  




  public static boolean isLenientEnabled()
  {
    String lenient = System.getProperty("org.apache.commons.exec.lenient", Boolean.TRUE.toString());
    return Boolean.TRUE.toString().equalsIgnoreCase(lenient);
  }
}
