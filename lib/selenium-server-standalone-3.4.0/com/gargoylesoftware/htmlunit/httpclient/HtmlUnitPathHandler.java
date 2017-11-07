package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicPathHandler;



























final class HtmlUnitPathHandler
  extends BasicPathHandler
{
  private final BrowserVersion browserVersion_;
  
  HtmlUnitPathHandler(BrowserVersion browserVersion)
  {
    browserVersion_ = browserVersion;
  }
  

  public void validate(Cookie cookie, CookieOrigin origin)
    throws MalformedCookieException
  {}
  
  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    CookieOrigin newOrigin = origin;
    String targetpath = origin.getPath();
    if ((browserVersion_.hasFeature(BrowserVersionFeatures.HTTP_COOKIE_EXTRACT_PATH_FROM_LOCATION)) && (!targetpath.isEmpty())) {
      int lastSlashPos = targetpath.lastIndexOf('/');
      if ((lastSlashPos > 1) && (lastSlashPos < targetpath.length())) {
        targetpath = targetpath.substring(0, lastSlashPos);
        newOrigin = new CookieOrigin(origin.getHost(), origin.getPort(), targetpath, origin.isSecure());
      }
    }
    
    return super.match(cookie, newOrigin);
  }
}
