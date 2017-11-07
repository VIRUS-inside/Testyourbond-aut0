package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.URL;

























public class NiceRefreshHandler
  extends ImmediateRefreshHandler
{
  private final int maxDelay_;
  
  public NiceRefreshHandler(int maxDelay)
  {
    if (maxDelay <= 0) {
      throw new IllegalArgumentException("Invalid maxDelay: " + maxDelay);
    }
    maxDelay_ = maxDelay;
  }
  







  public void handleRefresh(Page page, URL url, int requestedWait)
    throws IOException
  {
    if (requestedWait > maxDelay_) {
      return;
    }
    
    super.handleRefresh(page, url, requestedWait);
  }
}
