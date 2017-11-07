package com.gargoylesoftware.htmlunit.protocol.data;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;






















public class Handler
  extends URLStreamHandler
{
  public Handler() {}
  
  protected URLConnection openConnection(URL url)
  {
    return new DataURLConnection(url);
  }
}
