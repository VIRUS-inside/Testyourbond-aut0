package com.gargoylesoftware.htmlunit.httpclient;

import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.AbstractCookieAttributeHandler;




















final class HtmlUnitVersionAttributeHandler
  extends AbstractCookieAttributeHandler
  implements CommonCookieAttributeHandler
{
  HtmlUnitVersionAttributeHandler() {}
  
  public void parse(SetCookie cookie, String value)
    throws MalformedCookieException
  {
    if (value == null) {
      throw new MalformedCookieException("Missing value for version attribute");
    }
    int version = 0;
    try {
      version = Integer.parseInt(value);
    }
    catch (NumberFormatException localNumberFormatException) {}
    

    cookie.setVersion(version);
  }
  
  public String getAttributeName()
  {
    return "version";
  }
}
