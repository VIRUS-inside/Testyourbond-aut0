package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


























public class MockWebConnection
  implements WebConnection
{
  private static final Log LOG = LogFactory.getLog(MockWebConnection.class);
  
  public MockWebConnection() {}
  
  public static class RawResponseData
  {
    private final List<NameValuePair> headers_;
    private final byte[] byteContent_;
    private final String stringContent_;
    private final int statusCode_;
    private final String statusMessage_;
    private Charset charset_;
    
    RawResponseData(byte[] byteContent, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
    {
      byteContent_ = byteContent;
      stringContent_ = null;
      statusCode_ = statusCode;
      statusMessage_ = statusMessage;
      headers_ = compileHeaders(headers, contentType);
    }
    
    RawResponseData(String stringContent, Charset charset, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
    {
      byteContent_ = null;
      charset_ = charset;
      stringContent_ = stringContent;
      statusCode_ = statusCode;
      statusMessage_ = statusMessage;
      headers_ = compileHeaders(headers, contentType);
    }
    
    private static List<NameValuePair> compileHeaders(List<NameValuePair> headers, String contentType) {
      List<NameValuePair> compiledHeaders = new ArrayList();
      if (headers != null) {
        compiledHeaders.addAll(headers);
      }
      if (contentType != null) {
        compiledHeaders.add(new NameValuePair("Content-Type", contentType));
      }
      return compiledHeaders;
    }
    
    WebResponseData asWebResponseData() { byte[] content;
      byte[] content;
      if (byteContent_ != null) {
        content = byteContent_;
      } else { byte[] content;
        if (stringContent_ == null) {
          content = new byte[0];
        }
        else
          content = TextUtil.stringToByteArray(stringContent_, charset_);
      }
      return new WebResponseData(content, statusCode_, statusMessage_, headers_);
    }
    



    public List<NameValuePair> getHeaders()
    {
      return headers_;
    }
    



    public byte[] getByteContent()
    {
      return byteContent_;
    }
    



    public String getStringContent()
    {
      return stringContent_;
    }
    



    public int getStatusCode()
    {
      return statusCode_;
    }
    



    public String getStatusMessage()
    {
      return statusMessage_;
    }
    



    public Charset getCharset()
    {
      return charset_;
    }
  }
  
  private final Map<String, RawResponseData> responseMap_ = new HashMap(10);
  private RawResponseData defaultResponse_;
  private WebRequest lastRequest_;
  private int requestCount_ = 0;
  private final List<URL> requestedUrls_ = Collections.synchronizedList(new ArrayList());
  


  public WebResponse getResponse(WebRequest request)
    throws IOException
  {
    RawResponseData rawResponse = getRawResponse(request);
    return new WebResponse(rawResponse.asWebResponseData(), request, 0L);
  }
  




  public RawResponseData getRawResponse(WebRequest request)
  {
    URL url = request.getUrl();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Getting response for " + url.toExternalForm());
    }
    
    lastRequest_ = request;
    requestCount_ += 1;
    requestedUrls_.add(url);
    
    RawResponseData rawResponse = (RawResponseData)responseMap_.get(url.toExternalForm());
    if (rawResponse == null) {
      rawResponse = defaultResponse_;
      if (rawResponse == null) {
        throw new IllegalStateException("No response specified that can handle URL [" + 
          url.toExternalForm() + 
          "]");
      }
    }
    
    return rawResponse;
  }
  




  public List<String> getRequestedUrls(URL relativeTo)
  {
    String baseUrl = relativeTo.toString();
    List<String> response = new ArrayList();
    for (URL url : requestedUrls_) {
      String s = url.toString();
      if (s.startsWith(baseUrl)) {
        s = s.substring(baseUrl.length());
      }
      response.add(s);
    }
    
    return response;
  }
  




  public HttpMethod getLastMethod()
  {
    return lastRequest_.getHttpMethod();
  }
  




  public List<NameValuePair> getLastParameters()
  {
    return lastRequest_.getRequestParameters();
  }
  











  public void setResponse(URL url, String content, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
  {
    setResponse(
      url, 
      content, 
      statusCode, 
      statusMessage, 
      contentType, 
      StandardCharsets.ISO_8859_1, 
      headers);
  }
  












  public void setResponse(URL url, String content, int statusCode, String statusMessage, String contentType, Charset charset, List<NameValuePair> headers)
  {
    RawResponseData responseEntry = buildRawResponseData(content, charset, statusCode, statusMessage, 
      contentType, headers);
    responseMap_.put(url.toExternalForm(), responseEntry);
  }
  











  public void setResponse(URL url, byte[] content, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
  {
    RawResponseData responseEntry = buildRawResponseData(content, statusCode, statusMessage, contentType, 
      headers);
    responseMap_.put(url.toExternalForm(), responseEntry);
  }
  
  private static RawResponseData buildRawResponseData(byte[] content, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
  {
    return new RawResponseData(content, statusCode, statusMessage, contentType, headers);
  }
  

  private static RawResponseData buildRawResponseData(String content, Charset charset, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
  {
    if (charset == null) {
      charset = StandardCharsets.ISO_8859_1;
    }
    return new RawResponseData(content, charset, statusCode, statusMessage, contentType, headers);
  }
  







  public void setResponse(URL url, String content)
  {
    setResponse(url, content, 200, "OK", "text/html", null);
  }
  








  public void setResponse(URL url, String content, String contentType)
  {
    setResponse(url, content, 200, "OK", contentType, null);
  }
  









  public void setResponse(URL url, String content, String contentType, Charset charset)
  {
    setResponse(url, content, 200, "OK", contentType, charset, null);
  }
  







  public void setResponseAsGenericHtml(URL url, String title)
  {
    String content = "<html><head><title>" + title + "</title></head><body></body></html>";
    setResponse(url, content);
  }
  










  public void setDefaultResponse(String content, int statusCode, String statusMessage, String contentType)
  {
    defaultResponse_ = buildRawResponseData(content, null, statusCode, statusMessage, contentType, null);
  }
  










  public void setDefaultResponse(byte[] content, int statusCode, String statusMessage, String contentType)
  {
    defaultResponse_ = buildRawResponseData(content, statusCode, statusMessage, contentType, null);
  }
  





  public void setDefaultResponse(String content)
  {
    setDefaultResponse(content, 200, "OK", "text/html");
  }
  






  public void setDefaultResponse(String content, String contentType)
  {
    setDefaultResponse(content, 200, "OK", contentType, null);
  }
  







  public void setDefaultResponse(String content, String contentType, Charset charset)
  {
    setDefaultResponse(content, 200, "OK", contentType, charset, null);
  }
  










  public void setDefaultResponse(String content, int statusCode, String statusMessage, String contentType, List<NameValuePair> headers)
  {
    defaultResponse_ = buildRawResponseData(content, null, statusCode, statusMessage, contentType, headers);
  }
  











  public void setDefaultResponse(String content, int statusCode, String statusMessage, String contentType, Charset charset, List<NameValuePair> headers)
  {
    defaultResponse_ = buildRawResponseData(content, charset, statusCode, statusMessage, contentType, headers);
  }
  





  public Map<String, String> getLastAdditionalHeaders()
  {
    return lastRequest_.getAdditionalHeaders();
  }
  





  public WebRequest getLastWebRequest()
  {
    return lastRequest_;
  }
  



  public int getRequestCount()
  {
    return requestCount_;
  }
  




  public boolean hasResponse(URL url)
  {
    return responseMap_.containsKey(url.toExternalForm());
  }
  
  public void close() {}
}
