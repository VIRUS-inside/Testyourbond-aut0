package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





























public class WebResponse
  implements Serializable
{
  private static final Log LOG = LogFactory.getLog(WebResponse.class);
  


  private long loadTime_;
  

  private WebResponseData responseData_;
  

  private WebRequest request_;
  


  public WebResponse(WebResponseData responseData, URL url, HttpMethod requestMethod, long loadTime)
  {
    this(responseData, new WebRequest(url, requestMethod), loadTime);
  }
  







  public WebResponse(WebResponseData responseData, WebRequest request, long loadTime)
  {
    responseData_ = responseData;
    request_ = request;
    loadTime_ = loadTime;
  }
  



  public WebRequest getWebRequest()
  {
    return request_;
  }
  



  public List<NameValuePair> getResponseHeaders()
  {
    return responseData_.getResponseHeaders();
  }
  




  public String getResponseHeaderValue(String headerName)
  {
    for (NameValuePair pair : responseData_.getResponseHeaders()) {
      if (pair.getName().equalsIgnoreCase(headerName)) {
        return pair.getValue();
      }
    }
    return null;
  }
  



  public int getStatusCode()
  {
    return responseData_.getStatusCode();
  }
  



  public String getStatusMessage()
  {
    return responseData_.getStatusMessage();
  }
  



  public String getContentType()
  {
    String contentTypeHeader = getResponseHeaderValue("content-type");
    if (contentTypeHeader == null)
    {
      return "";
    }
    int index = contentTypeHeader.indexOf(';');
    if (index == -1) {
      return contentTypeHeader;
    }
    return contentTypeHeader.substring(0, index);
  }
  



  public Charset getContentCharsetOrNull()
  {
    try
    {
      Object localObject1 = null;Object localObject4 = null; Object localObject3; try { InputStream is = getContentAsStream();
        try { return EncodingSniffer.sniffEncoding(getResponseHeaders(), is);
        } finally { if (is != null) is.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
        }
      }
      return null;
    }
    catch (IOException e)
    {
      LOG.warn("Error trying to sniff encoding.", e);
    }
  }
  









  public Charset getContentCharset()
  {
    Charset charset = getContentCharsetOrNull();
    if (charset == null) {
      String contentType = getContentType();
      

      if ((contentType != null) && 
        (DefaultPageCreator.PageType.XML == DefaultPageCreator.determinePageType(contentType))) {
        return StandardCharsets.UTF_8;
      }
      
      charset = getWebRequest().getCharset();
    }
    if (charset == null) {
      charset = StandardCharsets.ISO_8859_1;
    }
    return charset;
  }
  




  public String getContentAsString()
  {
    return getContentAsString(getContentCharset());
  }
  







  @Deprecated
  public String getContentAsString(String encoding)
  {
    return getContentAsString(encoding, null);
  }
  





  @Deprecated
  public String getContentAsString(String encoding, String defaultEncoding)
  {
    Charset charset;
    



    try
    {
      charset = Charset.forName(encoding);
    } catch (Exception e) {
      Charset charset;
      if (encoding.equals(defaultEncoding)) {
        LOG.warn(e);
        return "";
      }
      charset = defaultEncoding != null ? Charset.forName(defaultEncoding) : getContentCharset();
      LOG.warn("Attempted to use unsupported encoding '" + 
        encoding + "'; using default content charset ('" + charset + "').");
    }
    return getContentAsString(charset);
  }
  





  public String getContentAsString(Charset encoding)
  {
    if (responseData_ != null) {
      try { Object localObject1 = null;Object localObject4 = null; Object localObject3; try { InputStream in = responseData_.getInputStream();
          try { if (in != null)
              return IOUtils.toString(in, encoding);
          } finally {
            if (in != null) in.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
          }
        }
        

        return null;
      }
      catch (IOException e)
      {
        LOG.warn(e);
      }
    }
  }
  




  public long getContentLength()
  {
    if (responseData_ == null) {
      return 0L;
    }
    return responseData_.getContentLength();
  }
  



  public InputStream getContentAsStream()
    throws IOException
  {
    return responseData_.getInputStream();
  }
  



  public long getLoadTime()
  {
    return loadTime_;
  }
  


  public void cleanUp()
  {
    if (responseData_ != null) {
      responseData_.cleanUp();
    }
  }
}
