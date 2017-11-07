package com.gargoylesoftware.htmlunit.protocol.javascript;

import com.gargoylesoftware.htmlunit.TextUtil;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


























public class JavaScriptURLConnection
  extends URLConnection
{
  public static final String JAVASCRIPT_PREFIX = "javascript:";
  private final String content_;
  
  public JavaScriptURLConnection(URL newUrl)
  {
    super(newUrl);
    content_ = newUrl.toExternalForm().substring("javascript:".length());
  }
  





  public void connect() {}
  





  public InputStream getInputStream()
  {
    return TextUtil.toInputStream(content_);
  }
}
