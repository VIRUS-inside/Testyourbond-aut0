package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

































public class StringWebResponse
  extends WebResponse
{
  private boolean fromJavascript_;
  
  public StringWebResponse(String content, URL originatingURL)
  {
    this(content, StandardCharsets.UTF_8, originatingURL);
  }
  






  @Deprecated
  public StringWebResponse(String content, String charset, URL originatingURL)
  {
    this(content, Charset.forName(charset), originatingURL);
  }
  





  public StringWebResponse(String content, Charset charset, URL originatingURL)
  {
    super(getWebResponseData(content, charset), buildWebRequest(originatingURL, charset), 0L);
  }
  






  private static WebResponseData getWebResponseData(String contentString, Charset charset)
  {
    byte[] content = TextUtil.stringToByteArray(contentString, charset);
    List<NameValuePair> compiledHeaders = new ArrayList();
    compiledHeaders.add(new NameValuePair("Content-Type", "text/html; charset=" + charset));
    return new WebResponseData(content, 200, "OK", compiledHeaders);
  }
  
  private static WebRequest buildWebRequest(URL originatingURL, Charset charset) {
    WebRequest webRequest = new WebRequest(originatingURL, HttpMethod.GET);
    webRequest.setCharset(charset);
    return webRequest;
  }
  




  public boolean isFromJavascript()
  {
    return fromJavascript_;
  }
  




  public void setFromJavascript(boolean fromJavascript)
  {
    fromJavascript_ = fromJavascript;
  }
}
