package com.gargoylesoftware.htmlunit;

import java.net.URL;
























public class AbstractPage
  implements Page
{
  private final WebResponse webResponse_;
  private WebWindow enclosingWindow_;
  
  public AbstractPage(WebResponse webResponse, WebWindow enclosingWindow)
  {
    webResponse_ = webResponse;
    enclosingWindow_ = enclosingWindow;
  }
  





  public void initialize() {}
  




  public void cleanUp()
  {
    if (getEnclosingWindow().getWebClient().getCache().getCachedResponse(webResponse_.getWebRequest()) == null) {
      webResponse_.cleanUp();
    }
  }
  





  public WebResponse getWebResponse()
  {
    return webResponse_;
  }
  





  public WebWindow getEnclosingWindow()
  {
    return enclosingWindow_;
  }
  




  public URL getUrl()
  {
    return getWebResponse().getWebRequest().getUrl();
  }
  
  public boolean isHtmlPage()
  {
    return false;
  }
}
