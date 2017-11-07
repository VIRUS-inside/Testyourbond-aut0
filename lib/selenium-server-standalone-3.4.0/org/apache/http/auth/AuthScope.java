package org.apache.http.auth;

import java.util.Locale;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;









































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class AuthScope
{
  public static final String ANY_HOST = null;
  



  public static final int ANY_PORT = -1;
  



  public static final String ANY_REALM = null;
  



  public static final String ANY_SCHEME = null;
  





  public static final AuthScope ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
  




  private final String scheme;
  




  private final String realm;
  



  private final String host;
  



  private final int port;
  



  private final HttpHost origin;
  




  public AuthScope(String host, int port, String realm, String schemeName)
  {
    this.host = (host == null ? ANY_HOST : host.toLowerCase(Locale.ROOT));
    this.port = (port < 0 ? -1 : port);
    this.realm = (realm == null ? ANY_REALM : realm);
    scheme = (schemeName == null ? ANY_SCHEME : schemeName.toUpperCase(Locale.ROOT));
    origin = null;
  }
  













  public AuthScope(HttpHost origin, String realm, String schemeName)
  {
    Args.notNull(origin, "Host");
    host = origin.getHostName().toLowerCase(Locale.ROOT);
    port = (origin.getPort() < 0 ? -1 : origin.getPort());
    this.realm = (realm == null ? ANY_REALM : realm);
    scheme = (schemeName == null ? ANY_SCHEME : schemeName.toUpperCase(Locale.ROOT));
    this.origin = origin;
  }
  






  public AuthScope(HttpHost origin)
  {
    this(origin, ANY_REALM, ANY_SCHEME);
  }
  









  public AuthScope(String host, int port, String realm)
  {
    this(host, port, realm, ANY_SCHEME);
  }
  







  public AuthScope(String host, int port)
  {
    this(host, port, ANY_REALM, ANY_SCHEME);
  }
  



  public AuthScope(AuthScope authscope)
  {
    Args.notNull(authscope, "Scope");
    host = authscope.getHost();
    port = authscope.getPort();
    realm = authscope.getRealm();
    scheme = authscope.getScheme();
    origin = authscope.getOrigin();
  }
  




  public HttpHost getOrigin()
  {
    return origin;
  }
  


  public String getHost()
  {
    return host;
  }
  


  public int getPort()
  {
    return port;
  }
  


  public String getRealm()
  {
    return realm;
  }
  


  public String getScheme()
  {
    return scheme;
  }
  






  public int match(AuthScope that)
  {
    int factor = 0;
    if (LangUtils.equals(scheme, scheme)) {
      factor++;
    }
    else if ((scheme != ANY_SCHEME) && (scheme != ANY_SCHEME)) {
      return -1;
    }
    
    if (LangUtils.equals(realm, realm)) {
      factor += 2;
    }
    else if ((realm != ANY_REALM) && (realm != ANY_REALM)) {
      return -1;
    }
    
    if (port == port) {
      factor += 4;
    }
    else if ((port != -1) && (port != -1)) {
      return -1;
    }
    
    if (LangUtils.equals(host, host)) {
      factor += 8;
    }
    else if ((host != ANY_HOST) && (host != ANY_HOST)) {
      return -1;
    }
    
    return factor;
  }
  



  public boolean equals(Object o)
  {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }
    if (!(o instanceof AuthScope)) {
      return super.equals(o);
    }
    AuthScope that = (AuthScope)o;
    return (LangUtils.equals(host, host)) && (port == port) && (LangUtils.equals(realm, realm)) && (LangUtils.equals(scheme, scheme));
  }
  







  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    if (scheme != null) {
      buffer.append(scheme.toUpperCase(Locale.ROOT));
      buffer.append(' ');
    }
    if (realm != null) {
      buffer.append('\'');
      buffer.append(realm);
      buffer.append('\'');
    } else {
      buffer.append("<any realm>");
    }
    if (host != null) {
      buffer.append('@');
      buffer.append(host);
      if (port >= 0) {
        buffer.append(':');
        buffer.append(port);
      }
    }
    return buffer.toString();
  }
  



  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, host);
    hash = LangUtils.hashCode(hash, port);
    hash = LangUtils.hashCode(hash, realm);
    hash = LangUtils.hashCode(hash, scheme);
    return hash;
  }
}
