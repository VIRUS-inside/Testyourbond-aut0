package com.gargoylesoftware.htmlunit;

import java.net.URL;























public class FailingHttpStatusCodeException
  extends RuntimeException
{
  private final WebResponse response_;
  
  public FailingHttpStatusCodeException(WebResponse failingResponse)
  {
    this(buildMessage(failingResponse), failingResponse);
  }
  




  public FailingHttpStatusCodeException(String message, WebResponse failingResponse)
  {
    super(message);
    response_ = failingResponse;
  }
  



  public int getStatusCode()
  {
    return response_.getStatusCode();
  }
  



  public String getStatusMessage()
  {
    return response_.getStatusMessage();
  }
  
  private static String buildMessage(WebResponse failingResponse) {
    int code = failingResponse.getStatusCode();
    String msg = failingResponse.getStatusMessage();
    URL url = failingResponse.getWebRequest().getUrl();
    return code + " " + msg + " for " + url;
  }
  



  public WebResponse getResponse()
  {
    return response_;
  }
}
