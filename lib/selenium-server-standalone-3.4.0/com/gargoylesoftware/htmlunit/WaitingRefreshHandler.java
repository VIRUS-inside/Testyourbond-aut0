package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



























public class WaitingRefreshHandler
  implements RefreshHandler
{
  private static final Log LOG = LogFactory.getLog(WaitingRefreshHandler.class);
  



  private final int maxwait_;
  



  public WaitingRefreshHandler(int maxwait)
  {
    maxwait_ = maxwait;
  }
  


  public WaitingRefreshHandler()
  {
    maxwait_ = 0;
  }
  







  public void handleRefresh(Page page, URL url, int requestedWait)
    throws IOException
  {
    int seconds = requestedWait;
    if ((seconds > maxwait_) && (maxwait_ > 0)) {
      seconds = maxwait_;
    }
    try {
      Thread.sleep(seconds * 1000);



    }
    catch (InterruptedException e)
    {


      if (LOG.isDebugEnabled()) {
        LOG.debug("Waiting thread was interrupted. Ignoring interruption to continue navigation.");
      }
    }
    WebWindow window = page.getEnclosingWindow();
    if (window == null) {
      return;
    }
    WebClient client = window.getWebClient();
    client.getPage(window, new WebRequest(url));
  }
}
