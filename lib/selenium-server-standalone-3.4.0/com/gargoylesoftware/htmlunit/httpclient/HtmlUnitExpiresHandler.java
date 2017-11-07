package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicExpiresHandler;

































final class HtmlUnitExpiresHandler
  extends BasicExpiresHandler
{
  private static final String[] DEFAULT_DATE_PATTERNS = {
    "EEE dd MMM yy HH mm ss zzz", 
    "EEE dd MMM yyyy HH mm ss zzz", 
    "EEE MMM d HH mm ss yyyy", 
    "EEE dd MMM yy HH mm ss z ", 
    "EEE dd MMM yyyy HH mm ss z ", 
    "EEE dd MM yy HH mm ss z ", 
    "EEE dd MM yyyy HH mm ss z " };
  

  private static final String[] EXTENDED_DATE_PATTERNS_1 = {
    "EEE dd MMM yy HH mm ss zzz", 
    "EEE dd MMM yyyy HH mm ss zzz", 
    "EEE MMM d HH mm ss yyyy", 
    "EEE dd MMM yy HH mm ss z ", 
    "EEE dd MMM yyyy HH mm ss z ", 
    "EEE dd MM yy HH mm ss z ", 
    "EEE dd MM yyyy HH mm ss z ", 
    "d/M/yyyy" };
  

  private static final String[] EXTENDED_DATE_PATTERNS_2 = {
    "EEE dd MMM yy HH mm ss zzz", 
    "EEE dd MMM yyyy HH mm ss zzz", 
    "EEE MMM d HH mm ss yyyy", 
    "EEE dd MMM yy HH mm ss z ", 
    "EEE dd MMM yyyy HH mm ss z ", 
    "EEE dd MM yy HH mm ss z ", 
    "EEE dd MM yyyy HH mm ss z ", 
    "EEE dd MMM yy HH MM ss z", 
    "MMM dd yy HH mm ss" };
  
  private final BrowserVersion browserVersion_;
  
  HtmlUnitExpiresHandler(BrowserVersion browserVersion)
  {
    super(DEFAULT_DATE_PATTERNS);
    browserVersion_ = browserVersion;
  }
  
  public void parse(SetCookie cookie, String value) throws MalformedCookieException
  {
    if ((value.startsWith("\"")) && (value.endsWith("\""))) {
      value = value.substring(1, value.length() - 1);
    }
    value = value.replaceAll("[ ,:-]+", " ");
    
    Date startDate = null;
    String[] datePatterns = DEFAULT_DATE_PATTERNS;
    
    if (browserVersion_ != null) {
      if (browserVersion_.hasFeature(BrowserVersionFeatures.HTTP_COOKIE_START_DATE_1970)) {
        startDate = HtmlUnitBrowserCompatCookieSpec.DATE_1_1_1970;
      }
      
      if (browserVersion_.hasFeature(BrowserVersionFeatures.HTTP_COOKIE_EXTENDED_DATE_PATTERNS_1)) {
        datePatterns = EXTENDED_DATE_PATTERNS_1;
      }
      
      if (browserVersion_.hasFeature(BrowserVersionFeatures.HTTP_COOKIE_EXTENDED_DATE_PATTERNS_2)) {
        Calendar calendar = Calendar.getInstance(Locale.ROOT);
        calendar.setTimeZone(DateUtils.GMT);
        calendar.set(1969, 0, 1, 0, 0, 0);
        calendar.set(14, 0);
        startDate = calendar.getTime();
        
        datePatterns = EXTENDED_DATE_PATTERNS_2;
      }
    }
    
    Date expiry = DateUtils.parseDate(value, datePatterns, startDate);
    cookie.setExpiryDate(expiry);
  }
}
