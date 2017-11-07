package com.gargoylesoftware.htmlunit.httpclient;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.X509TrustManager;











































































































































































































































class InsecureTrustManager2
  implements X509TrustManager
{
  private final Set<X509Certificate> acceptedIssuers_ = new HashSet();
  

  InsecureTrustManager2() {}
  
  public void checkClientTrusted(X509Certificate[] chain, String authType)
    throws CertificateException
  {
    acceptedIssuers_.addAll(Arrays.asList(chain));
  }
  



  public void checkServerTrusted(X509Certificate[] chain, String authType)
    throws CertificateException
  {
    acceptedIssuers_.addAll(Arrays.asList(chain));
  }
  







  public X509Certificate[] getAcceptedIssuers()
  {
    if (acceptedIssuers_.isEmpty()) {
      return new X509Certificate[0];
    }
    return (X509Certificate[])acceptedIssuers_.toArray(new X509Certificate[acceptedIssuers_.size()]);
  }
}
