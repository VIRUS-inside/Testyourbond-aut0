package com.gargoylesoftware.htmlunit.httpclient;

import org.apache.http.message.BasicHeaderValueFormatter;




























public class HtmlUnitBrowserCompatCookieHeaderValueFormatter
  extends BasicHeaderValueFormatter
{
  public static final HtmlUnitBrowserCompatCookieHeaderValueFormatter INSTANCE = new HtmlUnitBrowserCompatCookieHeaderValueFormatter();
  

  public HtmlUnitBrowserCompatCookieHeaderValueFormatter() {}
  

  protected boolean isSeparator(char ch)
  {
    return false;
  }
  




  protected boolean isUnsafe(char ch)
  {
    return false;
  }
}
