package com.gargoylesoftware.htmlunit.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;





























public class Cookie
  implements Serializable
{
  private ClientCookie httpClientCookie_;
  
  public Cookie(String domain, String name, String value)
  {
    this(domain, name, value, null, null, false);
  }
  










  public Cookie(String domain, String name, String value, String path, Date expires, boolean secure)
  {
    this(domain, name, value, path, expires, secure, false);
  }
  











  public Cookie(String domain, String name, String value, String path, Date expires, boolean secure, boolean httpOnly)
  {
    if (domain == null) {
      throw new IllegalArgumentException("Cookie domain must be specified");
    }
    
    BasicClientCookie cookie = new BasicClientCookie(name, value != null ? value : "");
    cookie.setDomain(domain);
    cookie.setPath(path);
    cookie.setExpiryDate(expires);
    cookie.setSecure(secure);
    if (httpOnly) {
      cookie.setAttribute("httponly", "true");
    }
    httpClientCookie_ = cookie;
  }
  



  public Cookie(ClientCookie clientCookie)
  {
    httpClientCookie_ = clientCookie;
  }
  












  public Cookie(String domain, String name, String value, String path, int maxAge, boolean secure)
  {
    BasicClientCookie cookie = new BasicClientCookie(name, value != null ? value : "");
    cookie.setDomain(domain);
    cookie.setPath(path);
    cookie.setSecure(secure);
    
    if (maxAge < -1) {
      throw new IllegalArgumentException("invalid max age:  " + maxAge);
    }
    if (maxAge >= 0) {
      cookie.setExpiryDate(new Date(System.currentTimeMillis() + maxAge * 1000));
    }
    
    httpClientCookie_ = cookie;
  }
  



  public String getName()
  {
    return httpClientCookie_.getName();
  }
  



  public String getValue()
  {
    return httpClientCookie_.getValue();
  }
  



  public String getDomain()
  {
    return httpClientCookie_.getDomain();
  }
  



  public String getPath()
  {
    return httpClientCookie_.getPath();
  }
  



  public Date getExpires()
  {
    return httpClientCookie_.getExpiryDate();
  }
  



  public boolean isSecure()
  {
    return httpClientCookie_.isSecure();
  }
  




  public boolean isHttpOnly()
  {
    return httpClientCookie_.getAttribute("httponly") != null;
  }
  



  public String toString()
  {
    return 
    



      getName() + "=" + getValue() + (getDomain() != null ? ";domain=" + getDomain() : "") + (getPath() != null ? ";path=" + getPath() : "") + (getExpires() != null ? ";expires=" + getExpires() : "") + (isSecure() ? ";secure" : "") + (isHttpOnly() ? ";httpOnly" : "");
  }
  



  public boolean equals(Object o)
  {
    if (!(o instanceof Cookie)) {
      return false;
    }
    Cookie other = (Cookie)o;
    String path = getPath() == null ? "/" : getPath();
    String otherPath = other.getPath() == null ? "/" : other.getPath();
    return new EqualsBuilder().append(getName(), other.getName()).append(getDomain(), other.getDomain())
      .append(path, otherPath).isEquals();
  }
  



  public int hashCode()
  {
    String path = getPath() == null ? "/" : getPath();
    return new HashCodeBuilder().append(getName()).append(getDomain()).append(path).toHashCode();
  }
  



  public org.apache.http.cookie.Cookie toHttpClient()
  {
    return httpClientCookie_;
  }
  




  public static List<org.apache.http.cookie.Cookie> toHttpClient(Collection<Cookie> cookies)
  {
    ArrayList<org.apache.http.cookie.Cookie> array = new ArrayList(cookies.size());
    Iterator<Cookie> it = cookies.iterator();
    while (it.hasNext()) {
      array.add(((Cookie)it.next()).toHttpClient());
    }
    return array;
  }
  




  public static List<Cookie> fromHttpClient(List<org.apache.http.cookie.Cookie> cookies)
  {
    List<Cookie> list = new ArrayList(cookies.size());
    for (org.apache.http.cookie.Cookie c : cookies) {
      list.add(new Cookie((ClientCookie)c));
    }
    return list;
  }
}
