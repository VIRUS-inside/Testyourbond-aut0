package org.seleniumhq.jetty9.util.log;

import java.util.HashSet;
import java.util.Set;

































public class StacklessLogging
  implements AutoCloseable
{
  private final Set<StdErrLog> squelched = new HashSet();
  
  public StacklessLogging(Class<?>... classesToSquelch)
  {
    for (Class<?> clazz : classesToSquelch)
    {
      Logger log = Log.getLogger(clazz);
      
      if (((log instanceof StdErrLog)) && (!log.isDebugEnabled()))
      {
        StdErrLog stdErrLog = (StdErrLog)log;
        if (!stdErrLog.isHideStacks())
        {
          stdErrLog.setHideStacks(true);
          squelched.add(stdErrLog);
        }
      }
    }
  }
  
  public StacklessLogging(Logger... logs)
  {
    for (Logger log : logs)
    {

      if (((log instanceof StdErrLog)) && (!log.isDebugEnabled()))
      {
        StdErrLog stdErrLog = (StdErrLog)log;
        if (!stdErrLog.isHideStacks())
        {
          stdErrLog.setHideStacks(true);
          squelched.add(stdErrLog);
        }
      }
    }
  }
  

  public void close()
  {
    for (StdErrLog log : squelched) {
      log.setHideStacks(false);
    }
  }
}
