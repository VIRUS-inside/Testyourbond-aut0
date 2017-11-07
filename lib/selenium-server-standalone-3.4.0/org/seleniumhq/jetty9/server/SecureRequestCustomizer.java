package org.seleniumhq.jetty9.server;

import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import org.seleniumhq.jetty9.http.BadMessageException;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpScheme;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.io.ssl.SslConnection;
import org.seleniumhq.jetty9.io.ssl.SslConnection.DecryptedEndPoint;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.ssl.SslContextFactory;
import org.seleniumhq.jetty9.util.ssl.X509;


























public class SecureRequestCustomizer
  implements HttpConfiguration.Customizer
{
  private static final Logger LOG = Log.getLogger(SecureRequestCustomizer.class);
  



  public static final String CACHED_INFO_ATTR = CachedInfo.class.getName();
  
  private String sslSessionAttribute = "org.seleniumhq.jetty9.servlet.request.ssl_session";
  
  private boolean _sniHostCheck;
  private long _stsMaxAge = -1L;
  private boolean _stsIncludeSubDomains;
  private HttpField _stsField;
  
  public SecureRequestCustomizer()
  {
    this(true);
  }
  
  public SecureRequestCustomizer(@Name("sniHostCheck") boolean sniHostCheck)
  {
    this(sniHostCheck, -1L, false);
  }
  








  public SecureRequestCustomizer(@Name("sniHostCheck") boolean sniHostCheck, @Name("stsMaxAgeSeconds") long stsMaxAgeSeconds, @Name("stsIncludeSubdomains") boolean stsIncludeSubdomains)
  {
    _sniHostCheck = sniHostCheck;
    _stsMaxAge = stsMaxAgeSeconds;
    _stsIncludeSubDomains = stsIncludeSubdomains;
    formatSTS();
  }
  



  public boolean isSniHostCheck()
  {
    return _sniHostCheck;
  }
  



  public void setSniHostCheck(boolean sniHostCheck)
  {
    _sniHostCheck = sniHostCheck;
  }
  



  public long getStsMaxAge()
  {
    return _stsMaxAge;
  }
  




  public void setStsMaxAge(long stsMaxAgeSeconds)
  {
    _stsMaxAge = stsMaxAgeSeconds;
    formatSTS();
  }
  





  public void setStsMaxAge(long period, TimeUnit units)
  {
    _stsMaxAge = units.toSeconds(period);
    formatSTS();
  }
  



  public boolean isStsIncludeSubDomains()
  {
    return _stsIncludeSubDomains;
  }
  



  public void setStsIncludeSubDomains(boolean stsIncludeSubDomains)
  {
    _stsIncludeSubDomains = stsIncludeSubDomains;
    formatSTS();
  }
  
  private void formatSTS()
  {
    if (_stsMaxAge < 0L) {
      _stsField = null;
    } else {
      _stsField = new PreEncodedHttpField(HttpHeader.STRICT_TRANSPORT_SECURITY, String.format("max-age=%d%s", new Object[] { Long.valueOf(_stsMaxAge), _stsIncludeSubDomains ? "; includeSubDomains" : "" }));
    }
  }
  
  public void customize(Connector connector, HttpConfiguration channelConfig, Request request)
  {
    EndPoint endp = request.getHttpChannel().getEndPoint();
    if ((endp instanceof SslConnection.DecryptedEndPoint))
    {
      SslConnection.DecryptedEndPoint ssl_endp = (SslConnection.DecryptedEndPoint)endp;
      SslConnection sslConnection = ssl_endp.getSslConnection();
      SSLEngine sslEngine = sslConnection.getSSLEngine();
      customize(sslEngine, request);
      
      if (request.getHttpURI().getScheme() == null) {
        request.setScheme(HttpScheme.HTTPS.asString());
      }
    } else if ((endp instanceof ProxyConnectionFactory.ProxyEndPoint))
    {
      ProxyConnectionFactory.ProxyEndPoint proxy = (ProxyConnectionFactory.ProxyEndPoint)endp;
      if ((request.getHttpURI().getScheme() == null) && (proxy.getAttribute("TLS_VERSION") != null)) {
        request.setScheme(HttpScheme.HTTPS.asString());
      }
    }
    if (HttpScheme.HTTPS.is(request.getScheme())) {
      customizeSecure(request);
    }
  }
  






  protected void customizeSecure(Request request)
  {
    request.setSecure(true);
    
    if (_stsField != null) {
      request.getResponse().getHttpFields().add(_stsField);
    }
  }
  





















  protected void customize(SSLEngine sslEngine, Request request)
  {
    SSLSession sslSession = sslEngine.getSession();
    
    if (_sniHostCheck)
    {
      String name = request.getServerName();
      X509 x509 = (X509)sslSession.getValue("org.seleniumhq.jetty9.util.ssl.snix509");
      
      if ((x509 != null) && (!x509.matches(name)))
      {
        LOG.warn("Host {} does not match SNI {}", new Object[] { name, x509 });
        throw new BadMessageException(400, "Host does not match SNI");
      }
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Host {} matched SNI {}", new Object[] { name, x509 });
      }
    }
    try
    {
      String cipherSuite = sslSession.getCipherSuite();
      



      CachedInfo cachedInfo = (CachedInfo)sslSession.getValue(CACHED_INFO_ATTR);
      String idStr; Integer keySize; X509Certificate[] certs; String idStr; if (cachedInfo != null)
      {
        Integer keySize = cachedInfo.getKeySize();
        X509Certificate[] certs = cachedInfo.getCerts();
        idStr = cachedInfo.getIdStr();
      }
      else
      {
        keySize = Integer.valueOf(SslContextFactory.deduceKeyLength(cipherSuite));
        certs = SslContextFactory.getCertChain(sslSession);
        byte[] bytes = sslSession.getId();
        idStr = TypeUtil.toHexString(bytes);
        cachedInfo = new CachedInfo(keySize, certs, idStr);
        sslSession.putValue(CACHED_INFO_ATTR, cachedInfo);
      }
      
      if (certs != null) {
        request.setAttribute("javax.servlet.request.X509Certificate", certs);
      }
      request.setAttribute("javax.servlet.request.cipher_suite", cipherSuite);
      request.setAttribute("javax.servlet.request.key_size", keySize);
      request.setAttribute("javax.servlet.request.ssl_session_id", idStr);
      String sessionAttribute = getSslSessionAttribute();
      if ((sessionAttribute != null) && (!sessionAttribute.isEmpty())) {
        request.setAttribute(sessionAttribute, sslSession);
      }
    }
    catch (Exception e) {
      LOG.warn("EXCEPTION ", e);
    }
  }
  
  public void setSslSessionAttribute(String attribute)
  {
    sslSessionAttribute = attribute;
  }
  
  public String getSslSessionAttribute()
  {
    return sslSessionAttribute;
  }
  

  public String toString()
  {
    return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
  }
  

  private static class CachedInfo
  {
    private final X509Certificate[] _certs;
    
    private final Integer _keySize;
    
    private final String _idStr;
    

    CachedInfo(Integer keySize, X509Certificate[] certs, String idStr)
    {
      _keySize = keySize;
      _certs = certs;
      _idStr = idStr;
    }
    
    X509Certificate[] getCerts()
    {
      return _certs;
    }
    
    Integer getKeySize()
    {
      return _keySize;
    }
    
    String getIdStr()
    {
      return _idStr;
    }
  }
}
