package org.apache.http;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

























































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class HttpHost
  implements Cloneable, Serializable
{
  private static final long serialVersionUID = -7529410654042457626L;
  public static final String DEFAULT_SCHEME_NAME = "http";
  protected final String hostname;
  protected final String lcHostname;
  protected final int port;
  protected final String schemeName;
  protected final InetAddress address;
  
  public HttpHost(String hostname, int port, String scheme)
  {
    this.hostname = ((String)Args.containsNoBlanks(hostname, "Host name"));
    lcHostname = hostname.toLowerCase(Locale.ROOT);
    if (scheme != null) {
      schemeName = scheme.toLowerCase(Locale.ROOT);
    } else {
      schemeName = "http";
    }
    this.port = port;
    address = null;
  }
  






  public HttpHost(String hostname, int port)
  {
    this(hostname, port, null);
  }
  




  public static HttpHost create(String s)
  {
    Args.containsNoBlanks(s, "HTTP Host");
    String text = s;
    String scheme = null;
    int schemeIdx = text.indexOf("://");
    if (schemeIdx > 0) {
      scheme = text.substring(0, schemeIdx);
      text = text.substring(schemeIdx + 3);
    }
    int port = -1;
    int portIdx = text.lastIndexOf(":");
    if (portIdx > 0) {
      try {
        port = Integer.parseInt(text.substring(portIdx + 1));
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Invalid HTTP host: " + text);
      }
      text = text.substring(0, portIdx);
    }
    return new HttpHost(text, port, scheme);
  }
  




  public HttpHost(String hostname)
  {
    this(hostname, -1, null);
  }
  











  public HttpHost(InetAddress address, int port, String scheme)
  {
    this((InetAddress)Args.notNull(address, "Inet address"), address.getHostName(), port, scheme);
  }
  













  public HttpHost(InetAddress address, String hostname, int port, String scheme)
  {
    this.address = ((InetAddress)Args.notNull(address, "Inet address"));
    this.hostname = ((String)Args.notNull(hostname, "Hostname"));
    lcHostname = this.hostname.toLowerCase(Locale.ROOT);
    if (scheme != null) {
      schemeName = scheme.toLowerCase(Locale.ROOT);
    } else {
      schemeName = "http";
    }
    this.port = port;
  }
  









  public HttpHost(InetAddress address, int port)
  {
    this(address, port, null);
  }
  







  public HttpHost(InetAddress address)
  {
    this(address, -1, null);
  }
  





  public HttpHost(HttpHost httphost)
  {
    Args.notNull(httphost, "HTTP host");
    hostname = hostname;
    lcHostname = lcHostname;
    schemeName = schemeName;
    port = port;
    address = address;
  }
  




  public String getHostName()
  {
    return hostname;
  }
  




  public int getPort()
  {
    return port;
  }
  




  public String getSchemeName()
  {
    return schemeName;
  }
  






  public InetAddress getAddress()
  {
    return address;
  }
  




  public String toURI()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append(schemeName);
    buffer.append("://");
    buffer.append(hostname);
    if (port != -1) {
      buffer.append(':');
      buffer.append(Integer.toString(port));
    }
    return buffer.toString();
  }
  





  public String toHostString()
  {
    if (port != -1)
    {
      StringBuilder buffer = new StringBuilder(hostname.length() + 6);
      buffer.append(hostname);
      buffer.append(":");
      buffer.append(Integer.toString(port));
      return buffer.toString();
    }
    return hostname;
  }
  


  public String toString()
  {
    return toURI();
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if ((obj instanceof HttpHost)) {
      HttpHost that = (HttpHost)obj;
      return (lcHostname.equals(lcHostname)) && (port == port) && (schemeName.equals(schemeName)) && (address == null ? address == null : address.equals(address));
    }
    


    return false;
  }
  




  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, lcHostname);
    hash = LangUtils.hashCode(hash, port);
    hash = LangUtils.hashCode(hash, schemeName);
    if (address != null) {
      hash = LangUtils.hashCode(hash, address);
    }
    return hash;
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
