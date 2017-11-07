package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicDomainHandler;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;





















final class HtmlUnitDomainHandler
  extends BasicDomainHandler
{
  private final BrowserVersion browserVersion_;
  
  HtmlUnitDomainHandler(BrowserVersion browserVersion)
  {
    browserVersion_ = browserVersion;
  }
  
  public void parse(SetCookie cookie, String value)
    throws MalformedCookieException
  {
    Args.notNull(cookie, "Cookie");
    if (TextUtils.isBlank(value)) {
      throw new MalformedCookieException("Blank or null value for domain attribute");
    }
    
    if (value.endsWith(".")) {
      return;
    }
    String domain = value;
    domain = domain.toLowerCase(Locale.ROOT);
    
    int dotIndex = domain.indexOf('.');
    if ((browserVersion_.hasFeature(BrowserVersionFeatures.HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS)) && 
      (dotIndex == 0) && (domain.length() > 1) && (domain.indexOf('.', 1) == -1)) {
      domain = domain.toLowerCase(Locale.ROOT);
      domain = domain.substring(1);
    }
    if (dotIndex > 0) {
      domain = '.' + domain;
    }
    
    cookie.setDomain(domain);
  }
  



  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    String domain = cookie.getDomain();
    if (domain == null) {
      return false;
    }
    
    int dotIndex = domain.indexOf('.');
    if ((dotIndex == 0) && (domain.length() > 1) && (domain.indexOf('.', 1) == -1)) {
      String host = origin.getHost();
      domain = domain.toLowerCase(Locale.ROOT);
      if (browserVersion_.hasFeature(BrowserVersionFeatures.HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS)) {
        domain = domain.substring(1);
      }
      if (host.equals(domain)) {
        return true;
      }
      return false;
    }
    
    if ((dotIndex == -1) && 
      (!"LOCAL_FILESYSTEM".equalsIgnoreCase(domain))) {
      try {
        InetAddress.getByName(domain);
      }
      catch (UnknownHostException e) {
        return false;
      }
    }
    
    return super.match(cookie, origin);
  }
}
