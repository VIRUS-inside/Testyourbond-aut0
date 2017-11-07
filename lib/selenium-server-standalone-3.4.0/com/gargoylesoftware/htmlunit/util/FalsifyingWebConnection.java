package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

























public abstract class FalsifyingWebConnection
  extends WebConnectionWrapper
{
  public FalsifyingWebConnection(WebConnection webConnection)
    throws IllegalArgumentException
  {
    super(webConnection);
  }
  



  public FalsifyingWebConnection(WebClient webClient)
    throws IllegalArgumentException
  {
    super(webClient);
  }
  






  protected WebResponse deliverFromAlternateUrl(WebRequest webRequest, URL url)
    throws IOException
  {
    URL originalUrl = webRequest.getUrl();
    webRequest.setUrl(url);
    WebResponse resp = super.getResponse(webRequest);
    resp.getWebRequest().setUrl(originalUrl);
    return resp;
  }
  





  protected WebResponse replaceContent(WebResponse wr, String newContent)
    throws IOException
  {
    byte[] body = newContent.getBytes(wr.getContentCharset());
    WebResponseData wrd = new WebResponseData(body, wr.getStatusCode(), wr.getStatusMessage(), 
      wr.getResponseHeaders());
    return new WebResponse(wrd, wr.getWebRequest().getUrl(), wr.getWebRequest().getHttpMethod(), 
      wr.getLoadTime());
  }
  







  protected WebResponse createWebResponse(WebRequest wr, String content, String contentType)
    throws IOException
  {
    return createWebResponse(wr, content, contentType, 200, "OK");
  }
  









  protected WebResponse createWebResponse(WebRequest wr, String content, String contentType, int responseCode, String responseMessage)
    throws IOException
  {
    List<NameValuePair> headers = new ArrayList();
    headers.add(new NameValuePair("content-type", contentType + "; charset=" + StandardCharsets.UTF_8));
    byte[] body = content.getBytes(StandardCharsets.UTF_8);
    WebResponseData wrd = new WebResponseData(body, responseCode, responseMessage, headers);
    return new WebResponse(wrd, wr.getUrl(), wr.getHttpMethod(), 0L);
  }
}
