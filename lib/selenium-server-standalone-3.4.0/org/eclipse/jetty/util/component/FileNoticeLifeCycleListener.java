package org.eclipse.jetty.util.component;

import java.io.FileWriter;
import java.io.Writer;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
























public class FileNoticeLifeCycleListener
  implements LifeCycle.Listener
{
  private static final Logger LOG = Log.getLogger(FileNoticeLifeCycleListener.class);
  
  private final String _filename;
  
  public FileNoticeLifeCycleListener(String filename)
  {
    _filename = filename;
  }
  
  private void writeState(String action, LifeCycle lifecycle) {
    try {
      Writer out = new FileWriter(_filename, true);Throwable localThrowable3 = null;
      try {
        out.append(action).append(" ").append(lifecycle.toString()).append("\n");
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally {
        if (out != null) if (localThrowable3 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else out.close();
      }
    } catch (Exception e) {
      LOG.warn(e);
    }
  }
  
  public void lifeCycleStarting(LifeCycle event)
  {
    writeState("STARTING", event);
  }
  
  public void lifeCycleStarted(LifeCycle event)
  {
    writeState("STARTED", event);
  }
  
  public void lifeCycleFailure(LifeCycle event, Throwable cause)
  {
    writeState("FAILED", event);
  }
  
  public void lifeCycleStopping(LifeCycle event)
  {
    writeState("STOPPING", event);
  }
  
  public void lifeCycleStopped(LifeCycle event)
  {
    writeState("STOPPED", event);
  }
}
