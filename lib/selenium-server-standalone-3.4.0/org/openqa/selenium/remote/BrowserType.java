package org.openqa.selenium.remote;

public abstract interface BrowserType
{
  public static final String FIREFOX = "firefox";
  public static final String FIREFOX_PROXY = "firefoxproxy";
  public static final String FIREFOX_CHROME = "firefoxchrome";
  public static final String GOOGLECHROME = "googlechrome";
  public static final String SAFARI = "safari";
  @Deprecated
  public static final String OPERA = "opera";
  public static final String OPERA_BLINK = "operablink";
  public static final String EDGE = "MicrosoftEdge";
  public static final String IEXPLORE = "iexplore";
  public static final String IEXPLORE_PROXY = "iexploreproxy";
  public static final String SAFARI_PROXY = "safariproxy";
  public static final String CHROME = "chrome";
  public static final String KONQUEROR = "konqueror";
  public static final String MOCK = "mock";
  public static final String IE_HTA = "iehta";
  public static final String ANDROID = "android";
  public static final String HTMLUNIT = "htmlunit";
  public static final String IE = "internet explorer";
  public static final String IPHONE = "iPhone";
  public static final String IPAD = "iPad";
  public static final String PHANTOMJS = "phantomjs";
}
