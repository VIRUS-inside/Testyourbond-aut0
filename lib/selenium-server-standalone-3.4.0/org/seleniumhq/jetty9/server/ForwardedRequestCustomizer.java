package org.seleniumhq.jetty9.server;

import java.net.InetSocketAddress;
import org.seleniumhq.jetty9.http.HostPortHttpField;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpScheme;
import org.seleniumhq.jetty9.http.QuotedCSV;
import org.seleniumhq.jetty9.util.StringUtil;











































public class ForwardedRequestCustomizer
  implements HttpConfiguration.Customizer
{
  private HostPortHttpField _forcedHost;
  private String _forwardedHeader = HttpHeader.FORWARDED.toString();
  private String _forwardedHostHeader = HttpHeader.X_FORWARDED_HOST.toString();
  private String _forwardedServerHeader = HttpHeader.X_FORWARDED_SERVER.toString();
  private String _forwardedForHeader = HttpHeader.X_FORWARDED_FOR.toString();
  private String _forwardedProtoHeader = HttpHeader.X_FORWARDED_PROTO.toString();
  private String _forwardedHttpsHeader = "X-Proxied-Https";
  private String _forwardedCipherSuiteHeader = "Proxy-auth-cert";
  private String _forwardedSslSessionIdHeader = "Proxy-ssl-id";
  private boolean _proxyAsAuthority = false;
  private boolean _sslIsSecure = true;
  


  public ForwardedRequestCustomizer() {}
  

  public boolean getProxyAsAuthority()
  {
    return _proxyAsAuthority;
  }
  




  public void setProxyAsAuthority(boolean proxyAsAuthority)
  {
    _proxyAsAuthority = proxyAsAuthority;
  }
  






  public void setForwardedOnly(boolean rfc7239only)
  {
    if (rfc7239only)
    {
      if (_forwardedHeader == null)
        _forwardedHeader = HttpHeader.FORWARDED.toString();
      _forwardedHostHeader = null;
      _forwardedHostHeader = null;
      _forwardedServerHeader = null;
      _forwardedForHeader = null;
      _forwardedProtoHeader = null;
      _forwardedHttpsHeader = null;
    }
    else
    {
      if (_forwardedHostHeader == null)
        _forwardedHostHeader = HttpHeader.X_FORWARDED_HOST.toString();
      if (_forwardedServerHeader == null)
        _forwardedServerHeader = HttpHeader.X_FORWARDED_SERVER.toString();
      if (_forwardedForHeader == null)
        _forwardedForHeader = HttpHeader.X_FORWARDED_FOR.toString();
      if (_forwardedProtoHeader == null)
        _forwardedProtoHeader = HttpHeader.X_FORWARDED_PROTO.toString();
      if (_forwardedHttpsHeader == null) {
        _forwardedHttpsHeader = "X-Proxied-Https";
      }
    }
  }
  
  public String getForcedHost() {
    return _forcedHost.getValue();
  }
  






  public void setForcedHost(String hostAndPort)
  {
    _forcedHost = new HostPortHttpField(hostAndPort);
  }
  



  public String getForwardedHeader()
  {
    return _forwardedHeader;
  }
  




  public void setForwardedHeader(String forwardedHeader)
  {
    _forwardedHeader = forwardedHeader;
  }
  
  public String getForwardedHostHeader()
  {
    return _forwardedHostHeader;
  }
  




  public void setForwardedHostHeader(String forwardedHostHeader)
  {
    _forwardedHostHeader = forwardedHostHeader;
  }
  



  public String getForwardedServerHeader()
  {
    return _forwardedServerHeader;
  }
  




  public void setForwardedServerHeader(String forwardedServerHeader)
  {
    _forwardedServerHeader = forwardedServerHeader;
  }
  



  public String getForwardedForHeader()
  {
    return _forwardedForHeader;
  }
  




  public void setForwardedForHeader(String forwardedRemoteAddressHeader)
  {
    _forwardedForHeader = forwardedRemoteAddressHeader;
  }
  





  public String getForwardedProtoHeader()
  {
    return _forwardedProtoHeader;
  }
  






  public void setForwardedProtoHeader(String forwardedProtoHeader)
  {
    _forwardedProtoHeader = forwardedProtoHeader;
  }
  



  public String getForwardedCipherSuiteHeader()
  {
    return _forwardedCipherSuiteHeader;
  }
  




  public void setForwardedCipherSuiteHeader(String forwardedCipherSuite)
  {
    _forwardedCipherSuiteHeader = forwardedCipherSuite;
  }
  



  public String getForwardedSslSessionIdHeader()
  {
    return _forwardedSslSessionIdHeader;
  }
  




  public void setForwardedSslSessionIdHeader(String forwardedSslSessionId)
  {
    _forwardedSslSessionIdHeader = forwardedSslSessionId;
  }
  



  public String getForwardedHttpsHeader()
  {
    return _forwardedHttpsHeader;
  }
  



  public void setForwardedHttpsHeader(String forwardedHttpsHeader)
  {
    _forwardedHttpsHeader = forwardedHttpsHeader;
  }
  




  public boolean isSslIsSecure()
  {
    return _sslIsSecure;
  }
  




  public void setSslIsSecure(boolean sslIsSecure)
  {
    _sslIsSecure = sslIsSecure;
  }
  

  public void customize(Connector connector, HttpConfiguration config, Request request)
  {
    HttpFields httpFields = request.getHttpFields();
    
    RFC7239 rfc7239 = null;
    String forwardedHost = null;
    String forwardedServer = null;
    String forwardedFor = null;
    String forwardedProto = null;
    String forwardedHttps = null;
    

    for (HttpField field : httpFields)
    {
      String name = field.getName();
      
      if ((getForwardedCipherSuiteHeader() != null) && (getForwardedCipherSuiteHeader().equalsIgnoreCase(name)))
      {
        request.setAttribute("javax.servlet.request.cipher_suite", field.getValue());
        if (isSslIsSecure())
        {
          request.setSecure(true);
          request.setScheme(config.getSecureScheme());
        }
      }
      
      if ((getForwardedSslSessionIdHeader() != null) && (getForwardedSslSessionIdHeader().equalsIgnoreCase(name)))
      {
        request.setAttribute("javax.servlet.request.ssl_session_id", field.getValue());
        if (isSslIsSecure())
        {
          request.setSecure(true);
          request.setScheme(config.getSecureScheme());
        }
      }
      
      if ((forwardedHost == null) && (_forwardedHostHeader != null) && (_forwardedHostHeader.equalsIgnoreCase(name))) {
        forwardedHost = getLeftMost(field.getValue());
      }
      if ((forwardedServer == null) && (_forwardedServerHeader != null) && (_forwardedServerHeader.equalsIgnoreCase(name))) {
        forwardedServer = getLeftMost(field.getValue());
      }
      if ((forwardedFor == null) && (_forwardedForHeader != null) && (_forwardedForHeader.equalsIgnoreCase(name))) {
        forwardedFor = getLeftMost(field.getValue());
      }
      if ((forwardedProto == null) && (_forwardedProtoHeader != null) && (_forwardedProtoHeader.equalsIgnoreCase(name))) {
        forwardedProto = getLeftMost(field.getValue());
      }
      if ((forwardedHttps == null) && (_forwardedHttpsHeader != null) && (_forwardedHttpsHeader.equalsIgnoreCase(name))) {
        forwardedHttps = getLeftMost(field.getValue());
      }
      if ((_forwardedHeader != null) && (_forwardedHeader.equalsIgnoreCase(name)))
      {
        if (rfc7239 == null)
          rfc7239 = new RFC7239(null);
        rfc7239.addValue(field.getValue());
      }
    }
    

    if (_forcedHost != null)
    {

      httpFields.put(_forcedHost);
      request.setAuthority(_forcedHost.getHost(), _forcedHost.getPort());
    }
    else if ((rfc7239 != null) && (_host != null))
    {
      HostPortHttpField auth = _host;
      httpFields.put(auth);
      request.setAuthority(auth.getHost(), auth.getPort());
    }
    else if (forwardedHost != null)
    {
      HostPortHttpField auth = new HostPortHttpField(forwardedHost);
      httpFields.put(auth);
      request.setAuthority(auth.getHost(), auth.getPort());
    }
    else if (_proxyAsAuthority)
    {
      if ((rfc7239 != null) && (_by != null))
      {
        HostPortHttpField auth = _by;
        httpFields.put(auth);
        request.setAuthority(auth.getHost(), auth.getPort());
      }
      else if (forwardedServer != null)
      {
        request.setAuthority(forwardedServer, request.getServerPort());
      }
    }
    

    if ((rfc7239 != null) && (_for != null))
    {
      request.setRemoteAddr(InetSocketAddress.createUnresolved(_for.getHost(), _for.getPort()));
    }
    else if (forwardedFor != null)
    {
      request.setRemoteAddr(InetSocketAddress.createUnresolved(forwardedFor, request.getRemotePort()));
    }
    

    if ((rfc7239 != null) && (_proto != null))
    {
      request.setScheme(_proto);
      if (_proto.equals(config.getSecureScheme())) {
        request.setSecure(true);
      }
    } else if (forwardedProto != null)
    {
      request.setScheme(forwardedProto);
      if (forwardedProto.equals(config.getSecureScheme())) {
        request.setSecure(true);
      }
    } else if ((forwardedHttps != null) && (("on".equalsIgnoreCase(forwardedHttps)) || ("true".equalsIgnoreCase(forwardedHttps))))
    {
      request.setScheme(HttpScheme.HTTPS.asString());
      if (HttpScheme.HTTPS.asString().equals(config.getSecureScheme())) {
        request.setSecure(true);
      }
    }
  }
  
  protected String getLeftMost(String headerValue)
  {
    if (headerValue == null) {
      return null;
    }
    int commaIndex = headerValue.indexOf(',');
    
    if (commaIndex == -1)
    {

      return headerValue;
    }
    

    return headerValue.substring(0, commaIndex).trim();
  }
  

  public String toString()
  {
    return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
  }
  
  @Deprecated
  public String getHostHeader()
  {
    return _forcedHost.getValue();
  }
  






  @Deprecated
  public void setHostHeader(String hostHeader)
  {
    _forcedHost = new HostPortHttpField(hostHeader);
  }
  
  private final class RFC7239 extends QuotedCSV
  {
    HostPortHttpField _by;
    HostPortHttpField _for;
    HostPortHttpField _host;
    String _proto;
    
    private RFC7239()
    {
      super(new String[0]);
    }
    

    protected void parsedParam(StringBuffer buffer, int valueLength, int paramName, int paramValue)
    {
      if ((valueLength == 0) && (paramValue > paramName))
      {
        String name = StringUtil.asciiToLowerCase(buffer.substring(paramName, paramValue - 1));
        String value = buffer.substring(paramValue);
        switch (name)
        {
        case "by": 
          if ((_by == null) && (!value.startsWith("_")) && (!"unknown".equals(value)))
            _by = new HostPortHttpField(value);
          break;
        case "for": 
          if ((_for == null) && (!value.startsWith("_")) && (!"unknown".equals(value)))
            _for = new HostPortHttpField(value);
          break;
        case "host": 
          if (_host == null)
            _host = new HostPortHttpField(value);
          break;
        case "proto": 
          if (_proto == null) {
            _proto = value;
          }
          break;
        }
      }
    }
  }
}
