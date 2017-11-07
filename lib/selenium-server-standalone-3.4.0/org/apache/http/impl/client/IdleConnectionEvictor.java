package org.apache.http.impl.client;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.util.Args;







































public final class IdleConnectionEvictor
{
  private final HttpClientConnectionManager connectionManager;
  private final ThreadFactory threadFactory;
  private final Thread thread;
  private final long sleepTimeMs;
  private final long maxIdleTimeMs;
  private volatile Exception exception;
  
  public IdleConnectionEvictor(final HttpClientConnectionManager connectionManager, ThreadFactory threadFactory, long sleepTime, TimeUnit sleepTimeUnit, long maxIdleTime, TimeUnit maxIdleTimeUnit)
  {
    this.connectionManager = ((HttpClientConnectionManager)Args.notNull(connectionManager, "Connection manager"));
    this.threadFactory = (threadFactory != null ? threadFactory : new DefaultThreadFactory());
    sleepTimeMs = (sleepTimeUnit != null ? sleepTimeUnit.toMillis(sleepTime) : sleepTime);
    maxIdleTimeMs = (maxIdleTimeUnit != null ? maxIdleTimeUnit.toMillis(maxIdleTime) : maxIdleTime);
    thread = this.threadFactory.newThread(new Runnable()
    {
      public void run() {
        try {
          while (!Thread.currentThread().isInterrupted()) {
            Thread.sleep(sleepTimeMs);
            connectionManager.closeExpiredConnections();
            if (maxIdleTimeMs > 0L) {
              connectionManager.closeIdleConnections(maxIdleTimeMs, TimeUnit.MILLISECONDS);
            }
          }
        } catch (Exception ex) {
          exception = ex;
        }
      }
    });
  }
  



  public IdleConnectionEvictor(HttpClientConnectionManager connectionManager, long sleepTime, TimeUnit sleepTimeUnit, long maxIdleTime, TimeUnit maxIdleTimeUnit)
  {
    this(connectionManager, null, sleepTime, sleepTimeUnit, maxIdleTime, maxIdleTimeUnit);
  }
  

  public IdleConnectionEvictor(HttpClientConnectionManager connectionManager, long maxIdleTime, TimeUnit maxIdleTimeUnit)
  {
    this(connectionManager, null, maxIdleTime > 0L ? maxIdleTime : 5L, maxIdleTimeUnit != null ? maxIdleTimeUnit : TimeUnit.SECONDS, maxIdleTime, maxIdleTimeUnit);
  }
  

  public void start()
  {
    thread.start();
  }
  
  public void shutdown() {
    thread.interrupt();
  }
  
  public boolean isRunning() {
    return thread.isAlive();
  }
  
  public void awaitTermination(long time, TimeUnit tunit) throws InterruptedException {
    thread.join((tunit != null ? tunit : TimeUnit.MILLISECONDS).toMillis(time));
  }
  
  static class DefaultThreadFactory implements ThreadFactory {
    DefaultThreadFactory() {}
    
    public Thread newThread(Runnable r) {
      Thread t = new Thread(r, "Connection evictor");
      t.setDaemon(true);
      return t;
    }
  }
}
