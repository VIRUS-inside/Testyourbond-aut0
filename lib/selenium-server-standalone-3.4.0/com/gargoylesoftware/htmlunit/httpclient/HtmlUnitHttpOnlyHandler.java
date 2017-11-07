package com.gargoylesoftware.htmlunit.httpclient;

import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicClientCookie;

























final class HtmlUnitHttpOnlyHandler
  implements CommonCookieAttributeHandler
{
  private static final String HTTPONLY_ATTR = "httponly";
  
  HtmlUnitHttpOnlyHandler() {}
  
  public void validate(Cookie cookie, CookieOrigin origin)
    throws MalformedCookieException
  {}
  
  public void parse(SetCookie cookie, String value)
    throws MalformedCookieException
  {
    ((BasicClientCookie)cookie).setAttribute("httponly", "true");
  }
  
  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    return true;
  }
  
  public String getAttributeName()
  {
    return "httponly";
  }
}
