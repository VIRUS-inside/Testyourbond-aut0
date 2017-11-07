package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicCommentHandler;
import org.apache.http.impl.cookie.BasicMaxAgeHandler;
import org.apache.http.impl.cookie.BasicSecureHandler;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.impl.cookie.NetscapeDraftHeaderParser;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;









































public class HtmlUnitBrowserCompatCookieSpec
  extends CookieSpecBase
{
  public static final String EMPTY_COOKIE_NAME = "HTMLUNIT_EMPTY_COOKIE";
  public static final String LOCAL_FILESYSTEM_DOMAIN = "LOCAL_FILESYSTEM";
  private static final Comparator<Cookie> COOKIE_COMPARATOR = new CookiePathComparator();
  static final Date DATE_1_1_1970;
  
  static
  {
    Calendar calendar = Calendar.getInstance(Locale.ROOT);
    calendar.setTimeZone(DateUtils.GMT);
    calendar.set(1970, 0, 1, 0, 0, 0);
    calendar.set(14, 0);
    DATE_1_1_1970 = calendar.getTime();
  }
  











  public HtmlUnitBrowserCompatCookieSpec(BrowserVersion browserVersion)
  {
    super(new CommonCookieAttributeHandler[] { new HtmlUnitVersionAttributeHandler(), new HtmlUnitDomainHandler(browserVersion), new HtmlUnitPathHandler(browserVersion), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new HtmlUnitExpiresHandler(browserVersion), new HtmlUnitHttpOnlyHandler() });
  }
  



  public List<Cookie> parse(Header header, CookieOrigin origin)
    throws MalformedCookieException
  {
    String text = header.getValue();
    int endPos = text.indexOf(';');
    if (endPos < 0) {
      endPos = text.indexOf('=');
    }
    else {
      int pos = text.indexOf('=');
      if (pos > endPos) {
        endPos = -1;
      }
      else {
        endPos = pos;
      }
    }
    if (endPos < 0) {
      header = new BasicHeader(header.getName(), "HTMLUNIT_EMPTY_COOKIE=" + header.getValue());
    }
    else if ((endPos == 0) || (StringUtils.isBlank(text.substring(0, endPos)))) {
      header = new BasicHeader(header.getName(), "HTMLUNIT_EMPTY_COOKIE" + header.getValue());
    }
    


    String headername = header.getName();
    if (!headername.equalsIgnoreCase("Set-Cookie")) {
      throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
    }
    HeaderElement[] helems = header.getElements();
    boolean versioned = false;
    boolean netscape = false;
    for (HeaderElement helem : helems) {
      if (helem.getParameterByName("version") != null) {
        versioned = true;
      }
      if (helem.getParameterByName("expires") != null)
        netscape = true; }
    List<Cookie> cookies;
    List<Cookie> cookies;
    if ((netscape) || (!versioned))
    {

      NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
      ParserCursor cursor;
      ParserCursor cursor;
      if ((header instanceof FormattedHeader)) {
        CharArrayBuffer buffer = ((FormattedHeader)header).getBuffer();
        cursor = new ParserCursor(
          ((FormattedHeader)header).getValuePos(), 
          buffer.length());
      }
      else {
        String s = header.getValue();
        if (s == null) {
          throw new MalformedCookieException("Header value is null");
        }
        buffer = new CharArrayBuffer(s.length());
        ((CharArrayBuffer)buffer).append(s);
        cursor = new ParserCursor(0, ((CharArrayBuffer)buffer).length());
      }
      HeaderElement elem = parser.parseHeader((CharArrayBuffer)buffer, cursor);
      String name = elem.getName();
      String value = elem.getValue();
      if ((name == null) || (name.isEmpty())) {
        throw new MalformedCookieException("Cookie name may not be empty");
      }
      BasicClientCookie cookie = new BasicClientCookie(name, value);
      cookie.setPath(getDefaultPath(origin));
      cookie.setDomain(getDefaultDomain(origin));
      

      NameValuePair[] attribs = elem.getParameters();
      for (int j = attribs.length - 1; j >= 0; j--) {
        NameValuePair attrib = attribs[j];
        String s = attrib.getName().toLowerCase(Locale.ROOT);
        cookie.setAttribute(s, attrib.getValue());
        CookieAttributeHandler handler = findAttribHandler(s);
        if (handler != null) {
          handler.parse(cookie, attrib.getValue());
        }
      }
      
      if (netscape) {
        cookie.setVersion(0);
      }
      cookies = Collections.singletonList(cookie);
    }
    else {
      cookies = parse(helems, origin);
    }
    
    for (Object buffer = cookies.iterator(); ((Iterator)buffer).hasNext();) { Cookie c = (Cookie)((Iterator)buffer).next();
      
      if (header.getValue().contains(c.getName() + "=\"" + c.getValue())) {
        ((BasicClientCookie)c).setValue('"' + c.getValue() + '"');
      }
    }
    return cookies;
  }
  
  public List<Header> formatCookies(List<Cookie> cookies)
  {
    Collections.sort(cookies, COOKIE_COMPARATOR);
    
    CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
    buffer.append("Cookie");
    buffer.append(": ");
    for (int i = 0; i < cookies.size(); i++) {
      Cookie cookie = (Cookie)cookies.get(i);
      if (i > 0) {
        buffer.append("; ");
      }
      String cookieName = cookie.getName();
      String cookieValue = cookie.getValue();
      if ((cookie.getVersion() > 0) && (!isQuoteEnclosed(cookieValue))) {
        HtmlUnitBrowserCompatCookieHeaderValueFormatter.INSTANCE.formatHeaderElement(
          buffer, 
          new BasicHeaderElement(cookieName, cookieValue), 
          false);
      }
      else
      {
        buffer.append(cookieName);
        buffer.append("=");
        if (cookieValue != null) {
          buffer.append(cookieValue);
        }
      }
    }
    List<Header> headers = new ArrayList(1);
    headers.add(new BufferedHeader(buffer));
    return headers;
  }
  
  private static boolean isQuoteEnclosed(String s) {
    return (s != null) && 
      (s.length() > 1) && 
      ('"' == s.charAt(0)) && 
      ('"' == s.charAt(s.length() - 1));
  }
  
  public int getVersion()
  {
    return 0;
  }
  
  public Header getVersionHeader()
  {
    return null;
  }
  
  public String toString()
  {
    return "compatibility";
  }
}
