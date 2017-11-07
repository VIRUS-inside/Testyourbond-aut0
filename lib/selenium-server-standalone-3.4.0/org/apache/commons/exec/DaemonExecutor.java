package org.apache.commons.exec;















public class DaemonExecutor
  extends DefaultExecutor
{
  public DaemonExecutor() {}
  














  protected Thread createThread(Runnable runnable, String name)
  {
    Thread t = super.createThread(runnable, name);
    t.setDaemon(true);
    return t;
  }
}
