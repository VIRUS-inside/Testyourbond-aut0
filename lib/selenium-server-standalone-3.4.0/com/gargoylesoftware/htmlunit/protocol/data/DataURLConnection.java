package com.gargoylesoftware.htmlunit.protocol.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





















public class DataURLConnection
  extends URLConnection
{
  private static final Log LOG = LogFactory.getLog(DataURLConnection.class);
  


  public static final String DATA_PREFIX = "data:";
  

  private final byte[] content_;
  


  public DataURLConnection(URL url)
  {
    super(url);
    
    byte[] data = null;
    try {
      data = DataUrlDecoder.decode(url).getBytes();
    }
    catch (UnsupportedEncodingException e) {
      LOG.error("Exception decoding " + url, e);
    }
    catch (DecoderException e) {
      LOG.error("Exception decoding " + url, e);
    }
    content_ = data;
  }
  





  public void connect() {}
  





  public InputStream getInputStream()
  {
    return new ByteArrayInputStream(content_);
  }
}
