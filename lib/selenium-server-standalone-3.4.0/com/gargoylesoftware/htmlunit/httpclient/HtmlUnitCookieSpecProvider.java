package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.HttpContext;























public final class HtmlUnitCookieSpecProvider
  implements CookieSpecProvider
{
  private final BrowserVersion browserVersion_;
  
  public HtmlUnitCookieSpecProvider(BrowserVersion browserVersion)
  {
    browserVersion_ = browserVersion;
  }
  
  public CookieSpec create(HttpContext context)
  {
    return new HtmlUnitBrowserCompatCookieSpec(browserVersion_);
  }
}
