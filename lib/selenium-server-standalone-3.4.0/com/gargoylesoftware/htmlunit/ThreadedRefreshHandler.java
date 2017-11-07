package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



























public class ThreadedRefreshHandler
  implements RefreshHandler
{
  private static final Log LOG = LogFactory.getLog(ThreadedRefreshHandler.class);
  



  public ThreadedRefreshHandler() {}
  


  public void handleRefresh(final Page page, final URL url, final int seconds)
  {
    Thread thread = new Thread("ThreadedRefreshHandler Thread")
    {
      public void run() {
        try {
          new WaitingRefreshHandler().handleRefresh(page, url, seconds);
        }
        catch (IOException e) {
          ThreadedRefreshHandler.LOG.error("Unable to refresh page!", e);
          throw new RuntimeException("Unable to refresh page!", e);
        }
      }
    };
    thread.setDaemon(true);
    thread.start();
  }
}
