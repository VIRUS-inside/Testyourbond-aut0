package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
























class WebResponseFromCache
  extends WebResponseWrapper
{
  private final WebRequest request_;
  
  WebResponseFromCache(WebResponse cachedResponse, WebRequest currentRequest)
  {
    super(cachedResponse);
    request_ = currentRequest;
  }
  



  public WebRequest getWebRequest()
  {
    return request_;
  }
}
