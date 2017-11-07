package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;



























public class WebConnectionWrapper
  implements WebConnection
{
  private final WebConnection wrappedWebConnection_;
  
  public WebConnectionWrapper(WebConnection webConnection)
    throws IllegalArgumentException
  {
    if (webConnection == null) {
      throw new IllegalArgumentException("Wrapped connection can't be null");
    }
    wrappedWebConnection_ = webConnection;
  }
  




  public WebConnectionWrapper(WebClient webClient)
    throws IllegalArgumentException
  {
    if (webClient == null) {
      throw new IllegalArgumentException("WebClient can't be null");
    }
    wrappedWebConnection_ = webClient.getWebConnection();
    webClient.setWebConnection(this);
  }
  




  public WebResponse getResponse(WebRequest request)
    throws IOException
  {
    return wrappedWebConnection_.getResponse(request);
  }
  



  public WebConnection getWrappedWebConnection()
  {
    return wrappedWebConnection_;
  }
  



  public void close()
    throws Exception
  {
    wrappedWebConnection_.close();
  }
}
