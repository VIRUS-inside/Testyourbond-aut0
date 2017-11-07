package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.CookieOrigin;































public class CookieManager
  implements Serializable
{
  private boolean cookiesEnabled_;
  private final Set<Cookie> cookies_ = new LinkedHashSet();
  


  public CookieManager()
  {
    cookiesEnabled_ = true;
  }
  



  public synchronized void setCookiesEnabled(boolean enabled)
  {
    cookiesEnabled_ = enabled;
  }
  



  public synchronized boolean isCookiesEnabled()
  {
    return cookiesEnabled_;
  }
  




  public synchronized Set<Cookie> getCookies()
  {
    if (!isCookiesEnabled()) {
      return Collections.emptySet();
    }
    
    Set<Cookie> copy = new LinkedHashSet();
    copy.addAll(cookies_);
    return Collections.unmodifiableSet(copy);
  }
  




  public CookieOrigin buildCookieOrigin(URL url)
  {
    URL normalizedUrl = replaceForCookieIfNecessary(url);
    
    return new CookieOrigin(
      normalizedUrl.getHost(), 
      getPort(normalizedUrl), 
      normalizedUrl.getPath(), 
      "https".equals(normalizedUrl.getProtocol()));
  }
  





  public synchronized boolean clearExpired(Date date)
  {
    if (!isCookiesEnabled()) {
      return false;
    }
    
    if (date == null) {
      return false;
    }
    
    boolean foundExpired = false;
    for (Iterator<Cookie> iter = cookies_.iterator(); iter.hasNext();) {
      Cookie cookie = (Cookie)iter.next();
      if ((cookie.getExpires() != null) && (date.after(cookie.getExpires()))) {
        iter.remove();
        foundExpired = true;
      }
    }
    return foundExpired;
  }
  







  protected int getPort(URL url)
  {
    if (url.getPort() != -1) {
      return url.getPort();
    }
    return url.getDefaultPort();
  }
  







  public URL replaceForCookieIfNecessary(URL url)
  {
    String protocol = url.getProtocol();
    boolean file = "file".equals(protocol);
    if (file) {
      try {
        url = UrlUtils.getUrlWithNewHostAndPort(url, 
          "LOCAL_FILESYSTEM", 0);
      }
      catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }
    return url;
  }
  





  public synchronized Cookie getCookie(String name)
  {
    if (!isCookiesEnabled()) {
      return null;
    }
    
    for (Cookie cookie : cookies_) {
      if (StringUtils.equals(cookie.getName(), name)) {
        return cookie;
      }
    }
    return null;
  }
  




  public synchronized void addCookie(Cookie cookie)
  {
    if (!isCookiesEnabled()) {
      return;
    }
    
    cookies_.remove(cookie);
    

    if ((cookie.getExpires() == null) || (cookie.getExpires().after(new Date()))) {
      cookies_.add(cookie);
    }
  }
  




  public synchronized void removeCookie(Cookie cookie)
  {
    if (!isCookiesEnabled()) {
      return;
    }
    
    cookies_.remove(cookie);
  }
  



  public synchronized void clearCookies()
  {
    if (!isCookiesEnabled()) {
      return;
    }
    
    cookies_.clear();
  }
}
