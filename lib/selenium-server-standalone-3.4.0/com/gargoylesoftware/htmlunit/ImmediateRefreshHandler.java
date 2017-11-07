package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;






























public class ImmediateRefreshHandler
  implements RefreshHandler, Serializable
{
  public ImmediateRefreshHandler() {}
  
  public void handleRefresh(Page page, URL url, int seconds)
    throws IOException
  {
    WebWindow window = page.getEnclosingWindow();
    if (window == null) {
      return;
    }
    WebClient client = window.getWebClient();
    if ((page.getUrl().toExternalForm().equals(url.toExternalForm())) && 
      (HttpMethod.GET == page.getWebResponse().getWebRequest().getHttpMethod())) {
      String msg = "Refresh to " + url + " (" + seconds + "s) aborted by HtmlUnit: " + 
        "Attempted to refresh a page using an ImmediateRefreshHandler " + 
        "which could have caused an OutOfMemoryError " + 
        "Please use WaitingRefreshHandler or ThreadedRefreshHandler instead.";
      throw new RuntimeException(msg);
    }
    client.getPage(window, new WebRequest(url));
  }
}
