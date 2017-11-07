package com.gargoylesoftware.htmlunit;

import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.security.KeyStore;





















public class WebClientOptions
  implements Serializable
{
  private boolean javaScriptEnabled_ = true;
  private boolean cssEnabled_ = true;
  private boolean printContentOnFailingStatusCode_ = true;
  private boolean throwExceptionOnFailingStatusCode_ = true;
  private boolean throwExceptionOnScriptError_ = true;
  private boolean appletEnabled_;
  private boolean popupBlockerEnabled_;
  private boolean isRedirectEnabled_ = true;
  private KeyStore sslClientCertificateStore_;
  private char[] sslClientCertificatePassword_;
  private KeyStore sslTrustStore_;
  private String[] sslClientProtocols_;
  private String[] sslClientCipherSuites_;
  private boolean geolocationEnabled_;
  private boolean doNotTrackEnabled_;
  private boolean activeXNative_;
  private String homePage_ = "http://htmlunit.sf.net/";
  private ProxyConfig proxyConfig_;
  private int timeout_ = 90000;
  
  private boolean useInsecureSSL_;
  private String sslInsecureProtocol_;
  private int maxInMemory_ = 512000;
  private int historySizeLimit_ = 50;
  private int historyPageCacheLimit_ = Integer.MAX_VALUE;
  private InetAddress localAddress_;
  private boolean downloadImages_;
  private int screenWidth_ = 1024;
  private int screenHeight_ = 768;
  


  public WebClientOptions() {}
  

  public void setUseInsecureSSL(boolean useInsecureSSL)
  {
    useInsecureSSL_ = useInsecureSSL;
  }
  



  public boolean isUseInsecureSSL()
  {
    return useInsecureSSL_;
  }
  




  public void setRedirectEnabled(boolean enabled)
  {
    isRedirectEnabled_ = enabled;
  }
  




  public boolean isRedirectEnabled()
  {
    return isRedirectEnabled_;
  }
  












  public void setSSLClientCertificate(InputStream certificateInputStream, String certificatePassword, String certificateType)
  {
    try
    {
      sslClientCertificateStore_ = getKeyStore(certificateInputStream, certificatePassword, certificateType);
      sslClientCertificatePassword_ = (certificatePassword == null ? null : certificatePassword.toCharArray());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  











  public void setSSLClientCertificate(URL certificateUrl, String certificatePassword, String certificateType)
  {
    try
    {
      Object localObject1 = null;Object localObject4 = null; Object localObject3; label95: try { is = certificateUrl.openStream();
      } finally {
        InputStream is;
        localObject3 = localThrowable; break label95; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
      }
    } catch (Exception e) { throw new RuntimeException(e);
    }
  }
  
  void setSSLClientCertificateStore(KeyStore keyStore) {
    sslClientCertificateStore_ = keyStore;
  }
  



  public KeyStore getSSLClientCertificateStore()
  {
    return sslClientCertificateStore_;
  }
  



  public char[] getSSLClientCertificatePassword()
  {
    return sslClientCertificatePassword_;
  }
  




  public String[] getSSLClientProtocols()
  {
    return sslClientProtocols_;
  }
  







  public void setSSLClientProtocols(String[] sslClientProtocols)
  {
    sslClientProtocols_ = sslClientProtocols;
  }
  




  public String[] getSSLClientCipherSuites()
  {
    return sslClientCipherSuites_;
  }
  







  public void setSSLClientCipherSuites(String[] sslClientCipherSuites)
  {
    sslClientCipherSuites_ = sslClientCipherSuites;
  }
  




  public void setJavaScriptEnabled(boolean enabled)
  {
    javaScriptEnabled_ = enabled;
  }
  




  public boolean isJavaScriptEnabled()
  {
    return javaScriptEnabled_;
  }
  




  public void setCssEnabled(boolean enabled)
  {
    cssEnabled_ = enabled;
  }
  




  public boolean isCssEnabled()
  {
    return cssEnabled_;
  }
  







  public void setAppletEnabled(boolean enabled)
  {
    appletEnabled_ = enabled;
  }
  




  public boolean isAppletEnabled()
  {
    return appletEnabled_;
  }
  






  public void setPopupBlockerEnabled(boolean enabled)
  {
    popupBlockerEnabled_ = enabled;
  }
  




  public boolean isPopupBlockerEnabled()
  {
    return popupBlockerEnabled_;
  }
  




  public void setGeolocationEnabled(boolean enabled)
  {
    geolocationEnabled_ = enabled;
  }
  




  public boolean isGeolocationEnabled()
  {
    return geolocationEnabled_;
  }
  




  public void setDoNotTrackEnabled(boolean enabled)
  {
    doNotTrackEnabled_ = enabled;
  }
  




  public boolean isDoNotTrackEnabled()
  {
    return doNotTrackEnabled_;
  }
  






  public void setPrintContentOnFailingStatusCode(boolean enabled)
  {
    printContentOnFailingStatusCode_ = enabled;
  }
  







  public boolean getPrintContentOnFailingStatusCode()
  {
    return printContentOnFailingStatusCode_;
  }
  






  public void setThrowExceptionOnFailingStatusCode(boolean enabled)
  {
    throwExceptionOnFailingStatusCode_ = enabled;
  }
  




  public boolean isThrowExceptionOnFailingStatusCode()
  {
    return throwExceptionOnFailingStatusCode_;
  }
  





  public boolean isThrowExceptionOnScriptError()
  {
    return throwExceptionOnScriptError_;
  }
  



  public void setThrowExceptionOnScriptError(boolean enabled)
  {
    throwExceptionOnScriptError_ = enabled;
  }
  






  public void setActiveXNative(boolean allow)
  {
    activeXNative_ = allow;
  }
  



  public boolean isActiveXNative()
  {
    return activeXNative_;
  }
  



  public String getHomePage()
  {
    return homePage_;
  }
  



  public void setHomePage(String homePage)
  {
    homePage_ = homePage;
  }
  



  public ProxyConfig getProxyConfig()
  {
    return proxyConfig_;
  }
  



  public void setProxyConfig(ProxyConfig proxyConfig)
  {
    WebAssert.notNull("proxyConfig", proxyConfig);
    proxyConfig_ = proxyConfig;
  }
  





  public int getTimeout()
  {
    return timeout_;
  }
  







  public void setTimeout(int timeout)
  {
    timeout_ = timeout;
  }
  




  public void setSSLInsecureProtocol(String sslInsecureProtocol)
  {
    sslInsecureProtocol_ = sslInsecureProtocol;
  }
  



  public String getSSLInsecureProtocol()
  {
    return sslInsecureProtocol_;
  }
  








  public void setSSLTrustStore(URL sslTrustStoreUrl, String sslTrustStorePassword, String sslTrustStoreType)
  {
    try
    {
      Object localObject1 = null;Object localObject4 = null; Object localObject3; label79: try { is = sslTrustStoreUrl.openStream();
      } finally { InputStream is;
        localObject3 = localThrowable; break label79; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
      }
    } catch (Exception e) { throw new RuntimeException(e);
    }
  }
  
  void setSSLTrustStore(KeyStore keyStore) {
    sslTrustStore_ = keyStore;
  }
  



  public KeyStore getSSLTrustStore()
  {
    return sslTrustStore_;
  }
  
  private static KeyStore getKeyStore(InputStream inputStream, String keystorePassword, String keystoreType) throws Exception
  {
    if (inputStream == null) {
      return null;
    }
    
    KeyStore keyStore = KeyStore.getInstance(keystoreType);
    char[] passwordChars = keystorePassword != null ? keystorePassword.toCharArray() : null;
    keyStore.load(inputStream, passwordChars);
    return keyStore;
  }
  



  public int getMaxInMemory()
  {
    return maxInMemory_;
  }
  




  public void setMaxInMemory(int maxInMemory)
  {
    maxInMemory_ = maxInMemory;
  }
  



  public int getHistorySizeLimit()
  {
    return historySizeLimit_;
  }
  






  public void setHistorySizeLimit(int historySizeLimit)
  {
    historySizeLimit_ = historySizeLimit;
  }
  



  public int getHistoryPageCacheLimit()
  {
    return historyPageCacheLimit_;
  }
  









  public void setHistoryPageCacheLimit(int historyPageCacheLimit)
  {
    historyPageCacheLimit_ = historyPageCacheLimit;
  }
  









  public InetAddress getLocalAddress()
  {
    return localAddress_;
  }
  







  public void setLocalAddress(InetAddress localAddress)
  {
    localAddress_ = localAddress;
  }
  



  public void setDownloadImages(boolean downloadImages)
  {
    downloadImages_ = downloadImages;
  }
  



  public boolean isDownloadImages()
  {
    return downloadImages_;
  }
  




  public void setScreenWidth(int screenWidth)
  {
    screenWidth_ = screenWidth;
  }
  




  public int getScreenWidth()
  {
    return screenWidth_;
  }
  




  public void setScreenHeight(int screenHeight)
  {
    screenHeight_ = screenHeight;
  }
  




  public int getScreenHeight()
  {
    return screenHeight_;
  }
}
