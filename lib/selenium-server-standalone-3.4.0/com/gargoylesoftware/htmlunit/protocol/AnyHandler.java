package com.gargoylesoftware.htmlunit.protocol;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;






















public class AnyHandler
  extends URLStreamHandler
{
  public static final AnyHandler INSTANCE = new AnyHandler();
  


  public AnyHandler() {}
  

  protected URLConnection openConnection(URL url)
  {
    throw new RuntimeException("Unsupported protocol for open connection to " + url);
  }
}
