package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
























public class WebResponseData
  implements Serializable
{
  private static final Log LOG = LogFactory.getLog(WebResponseData.class);
  

  private final int statusCode_;
  

  private final String statusMessage_;
  

  private final List<NameValuePair> responseHeaders_;
  

  private final DownloadedContent downloadedContent_;
  

  public WebResponseData(byte[] body, int statusCode, String statusMessage, List<NameValuePair> responseHeaders)
  {
    this(new DownloadedContent.InMemory(body), statusCode, statusMessage, responseHeaders);
  }
  







  protected WebResponseData(int statusCode, String statusMessage, List<NameValuePair> responseHeaders)
  {
    this(ArrayUtils.EMPTY_BYTE_ARRAY, statusCode, statusMessage, responseHeaders);
  }
  







  public WebResponseData(DownloadedContent downloadedContent, int statusCode, String statusMessage, List<NameValuePair> responseHeaders)
  {
    statusCode_ = statusCode;
    statusMessage_ = statusMessage;
    responseHeaders_ = Collections.unmodifiableList(responseHeaders);
    downloadedContent_ = downloadedContent;
  }
  
  private InputStream getStream(DownloadedContent downloadedContent, List<NameValuePair> headers)
    throws IOException
  {
    InputStream stream = downloadedContent_.getInputStream();
    if (downloadedContent.isEmpty()) {
      return stream;
    }
    
    String encoding = getHeader(headers, "content-encoding");
    if (encoding != null) {
      if (StringUtils.contains(encoding, "gzip")) {
        try {
          stream = new GZIPInputStream(stream);
        }
        catch (IOException e) {
          LOG.error("Reading gzip encodec content failed.", e);
          stream.close();
          stream = IOUtils.toInputStream(
            "<html>\n<head><title>Problem loading page</title></head>\n<body>\n<h1>Content Encoding Error</h1>\n<p>The page you are trying to view cannot be shown because it uses an invalid or unsupported form of compression.</p>\n</body>\n</html>", 
            





            StandardCharsets.ISO_8859_1);
        }
      }
      else if (StringUtils.contains(encoding, "deflate")) {
        boolean zlibHeader = false;
        if (stream.markSupported()) {
          stream.mark(2);
          byte[] buffer = new byte[2];
          stream.read(buffer, 0, 2);
          zlibHeader = ((buffer[0] & 0xFF) << 8 | buffer[1] & 0xFF) == 30876;
          stream.reset();
        }
        if (zlibHeader) {
          stream = new InflaterInputStream(stream);
        }
        else {
          stream = new InflaterInputStream(stream, new Inflater(true));
        }
      }
    }
    return stream;
  }
  
  private static String getHeader(List<NameValuePair> headers, String name) {
    for (NameValuePair header : headers) {
      String headerName = header.getName().trim();
      if (name.equalsIgnoreCase(headerName)) {
        return header.getValue();
      }
    }
    
    return null;
  }
  



  public byte[] getBody()
  {
    try
    {
      return IOUtils.toByteArray(getInputStream());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  



  public InputStream getInputStream()
    throws IOException
  {
    return getStream(downloadedContent_, getResponseHeaders());
  }
  


  public List<NameValuePair> getResponseHeaders()
  {
    return responseHeaders_;
  }
  


  public int getStatusCode()
  {
    return statusCode_;
  }
  


  public String getStatusMessage()
  {
    return statusMessage_;
  }
  



  public long getContentLength()
  {
    return downloadedContent_.length();
  }
  


  public void cleanUp()
  {
    downloadedContent_.cleanUp();
  }
}
