package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;




























public class WebResponseWrapper
  extends WebResponse
{
  private final WebResponse wrappedWebResponse_;
  
  public WebResponseWrapper(WebResponse webResponse)
    throws IllegalArgumentException
  {
    super(null, null, 0L);
    if (webResponse == null) {
      throw new IllegalArgumentException("Wrapped WebResponse can't be null");
    }
    wrappedWebResponse_ = webResponse;
  }
  



  public InputStream getContentAsStream()
    throws IOException
  {
    return wrappedWebResponse_.getContentAsStream();
  }
  




  public String getContentAsString()
  {
    return wrappedWebResponse_.getContentAsString(getContentCharset());
  }
  




  @Deprecated
  public String getContentAsString(String encoding)
  {
    return wrappedWebResponse_.getContentAsString(encoding);
  }
  



  @Deprecated
  public String getContentAsString(String encoding, String defaultEncoding)
  {
    return wrappedWebResponse_.getContentAsString(encoding, defaultEncoding);
  }
  




  public String getContentAsString(Charset encoding)
  {
    return wrappedWebResponse_.getContentAsString(encoding);
  }
  




  public Charset getContentCharsetOrNull()
  {
    return wrappedWebResponse_.getContentCharsetOrNull();
  }
  




  public Charset getContentCharset()
  {
    return wrappedWebResponse_.getContentCharset();
  }
  




  public String getContentType()
  {
    return wrappedWebResponse_.getContentType();
  }
  




  public long getLoadTime()
  {
    return wrappedWebResponse_.getLoadTime();
  }
  




  public List<NameValuePair> getResponseHeaders()
  {
    return wrappedWebResponse_.getResponseHeaders();
  }
  




  public String getResponseHeaderValue(String headerName)
  {
    return wrappedWebResponse_.getResponseHeaderValue(headerName);
  }
  




  public int getStatusCode()
  {
    return wrappedWebResponse_.getStatusCode();
  }
  




  public String getStatusMessage()
  {
    return wrappedWebResponse_.getStatusMessage();
  }
  




  public WebRequest getWebRequest()
  {
    return wrappedWebResponse_.getWebRequest();
  }
}
