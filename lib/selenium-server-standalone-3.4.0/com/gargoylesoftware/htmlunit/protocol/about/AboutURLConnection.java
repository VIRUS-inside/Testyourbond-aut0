package com.gargoylesoftware.htmlunit.protocol.about;

import java.net.URL;
import java.net.URLConnection;






















public class AboutURLConnection
  extends URLConnection
{
  public AboutURLConnection(URL newUrl)
  {
    super(newUrl);
  }
  
  public void connect() {}
}
