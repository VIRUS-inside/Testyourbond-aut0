package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.WebClientOptions;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;




































public final class HtmlUnitSSLConnectionSocketFactory
  extends SSLConnectionSocketFactory
{
  private static final String SSL3ONLY = "htmlunit.SSL3Only";
  private final boolean useInsecureSSL_;
  
  public static void setUseSSL3Only(HttpContext httpContext, boolean ssl3Only)
  {
    httpContext.setAttribute("htmlunit.SSL3Only", Boolean.valueOf(ssl3Only));
  }
  
  static boolean isUseSSL3Only(HttpContext context) {
    return "TRUE".equalsIgnoreCase((String)context.getAttribute("htmlunit.SSL3Only"));
  }
  



  public static SSLConnectionSocketFactory buildSSLSocketFactory(WebClientOptions options)
  {
    try
    {
      String[] sslClientProtocols = options.getSSLClientProtocols();
      String[] sslClientCipherSuites = options.getSSLClientCipherSuites();
      
      boolean useInsecureSSL = options.isUseInsecureSSL();
      
      if (!useInsecureSSL) {
        KeyStore keyStore = options.getSSLClientCertificateStore();
        KeyStore trustStore = options.getSSLTrustStore();
        
        return new HtmlUnitSSLConnectionSocketFactory(keyStore, 
          keyStore == null ? null : options.getSSLClientCertificatePassword(), 
          trustStore, useInsecureSSL, 
          sslClientProtocols, sslClientCipherSuites);
      }
      

      String protocol = options.getSSLInsecureProtocol();
      if (protocol == null) {
        protocol = "SSL";
      }
      SSLContext sslContext = SSLContext.getInstance(protocol);
      sslContext.init(getKeyManagers(options), new TrustManager[] { new InsecureTrustManager2() }, null);
      
      return new HtmlUnitSSLConnectionSocketFactory(sslContext, 
        NoopHostnameVerifier.INSTANCE, 
        useInsecureSSL, sslClientProtocols, sslClientCipherSuites);
    }
    catch (GeneralSecurityException e)
    {
      throw new RuntimeException(e);
    }
  }
  

  private HtmlUnitSSLConnectionSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier, boolean useInsecureSSL, String[] supportedProtocols, String[] supportedCipherSuites)
  {
    super(sslContext, supportedProtocols, supportedCipherSuites, hostnameVerifier);
    useInsecureSSL_ = useInsecureSSL;
  }
  




  private HtmlUnitSSLConnectionSocketFactory(KeyStore keystore, char[] keystorePassword, KeyStore truststore, boolean useInsecureSSL, String[] supportedProtocols, String[] supportedCipherSuites)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    super(SSLContexts.custom().loadKeyMaterial(keystore, keystorePassword).loadTrustMaterial(truststore, null).build(), supportedProtocols, supportedCipherSuites, new DefaultHostnameVerifier());
    useInsecureSSL_ = useInsecureSSL;
  }
  
  private static void configureSocket(SSLSocket sslSocket, HttpContext context) {
    if (isUseSSL3Only(context)) {
      sslSocket.setEnabledProtocols(new String[] { "SSLv3" });
    }
  }
  
















  public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context)
    throws IOException
  {
    HttpHost socksProxy = SocksConnectionSocketFactory.getSocksProxy(context);
    if (socksProxy != null) {
      Socket underlying = SocksConnectionSocketFactory.createSocketWithSocksProxy(socksProxy);
      underlying.setReuseAddress(true);
      
      SocketAddress socksProxyAddress = new InetSocketAddress(socksProxy.getHostName(), 
        socksProxy.getPort());
      try
      {
        underlying.connect(remoteAddress, connectTimeout);
      }
      catch (SocketTimeoutException ex) {
        throw new ConnectTimeoutException("Connect to " + socksProxyAddress + " timed out");
      }
      
      Socket sslSocket = getSSLSocketFactory().createSocket(underlying, socksProxy.getHostName(), 
        socksProxy.getPort(), true);
      configureSocket((SSLSocket)sslSocket, context);
      return sslSocket;
    }
    try {
      return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
    }
    catch (IOException e) {
      if ((useInsecureSSL_) && ("handshake alert:  unrecognized_name".equals(e.getMessage()))) {
        setEmptyHostname(host);
        
        return super.connectSocket(connectTimeout, 
          createSocket(context), 
          host, remoteAddress, localAddress, context);
      }
      throw e;
    }
  }
  
  private static void setEmptyHostname(HttpHost host) {
    try {
      Field field = HttpHost.class.getDeclaredField("hostname");
      field.setAccessible(true);
      field.set(host, "");
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private SSLSocketFactory getSSLSocketFactory() {
    try {
      Field field = SSLConnectionSocketFactory.class.getDeclaredField("socketfactory");
      field.setAccessible(true);
      return (SSLSocketFactory)field.get(this);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private static KeyManager[] getKeyManagers(WebClientOptions options) {
    if (options.getSSLClientCertificateStore() == null) {
      return null;
    }
    try {
      KeyStore keyStore = options.getSSLClientCertificateStore();
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keyStore, options.getSSLClientCertificatePassword());
      return keyManagerFactory.getKeyManagers();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
