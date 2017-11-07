package com.gargoylesoftware.htmlunit.protocol.about;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;






















public class Handler
  extends URLStreamHandler
{
  public Handler() {}
  
  protected URLConnection openConnection(URL url)
  {
    return new AboutURLConnection(url);
  }
}
