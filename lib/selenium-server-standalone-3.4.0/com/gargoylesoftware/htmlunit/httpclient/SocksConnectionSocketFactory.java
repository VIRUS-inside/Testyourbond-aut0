package com.gargoylesoftware.htmlunit.httpclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;






















public class SocksConnectionSocketFactory
  extends PlainConnectionSocketFactory
{
  private static final String SOCKS_PROXY = "htmlunit.socksproxy";
  
  public SocksConnectionSocketFactory() {}
  
  public static void setSocksProxy(HttpContext context, HttpHost socksProxy)
  {
    context.setAttribute("htmlunit.socksproxy", socksProxy);
  }
  
  static HttpHost getSocksProxy(HttpContext context) {
    return (HttpHost)context.getAttribute("htmlunit.socksproxy");
  }
  
  static Socket createSocketWithSocksProxy(HttpHost socksProxy) {
    InetSocketAddress address = new InetSocketAddress(socksProxy.getHostName(), socksProxy.getPort());
    Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
    return new Socket(proxy);
  }
  


  public Socket createSocket(HttpContext context)
    throws IOException
  {
    HttpHost socksProxy = getSocksProxy(context);
    if (socksProxy != null) {
      return createSocketWithSocksProxy(socksProxy);
    }
    return super.createSocket(context);
  }
}
