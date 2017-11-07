package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePriorityComparator;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;





































@Contract(threading=ThreadingBehavior.SAFE)
public class RFC6265CookieSpec
  implements CookieSpec
{
  private static final char PARAM_DELIMITER = ';';
  private static final char COMMA_CHAR = ',';
  private static final char EQUAL_CHAR = '=';
  private static final char DQUOTE_CHAR = '"';
  private static final char ESCAPE_CHAR = '\\';
  private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(new int[] { 61, 59 });
  private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(new int[] { 59 });
  private static final BitSet SPECIAL_CHARS = TokenParser.INIT_BITSET(new int[] { 32, 34, 44, 59, 92 });
  
  private final CookieAttributeHandler[] attribHandlers;
  
  private final Map<String, CookieAttributeHandler> attribHandlerMap;
  private final TokenParser tokenParser;
  
  protected RFC6265CookieSpec(CommonCookieAttributeHandler... handlers)
  {
    attribHandlers = ((CookieAttributeHandler[])handlers.clone());
    attribHandlerMap = new ConcurrentHashMap(handlers.length);
    for (CommonCookieAttributeHandler handler : handlers) {
      attribHandlerMap.put(handler.getAttributeName().toLowerCase(Locale.ROOT), handler);
    }
    tokenParser = TokenParser.INSTANCE;
  }
  
  static String getDefaultPath(CookieOrigin origin) {
    String defaultPath = origin.getPath();
    int lastSlashIndex = defaultPath.lastIndexOf('/');
    if (lastSlashIndex >= 0) {
      if (lastSlashIndex == 0)
      {
        lastSlashIndex = 1;
      }
      defaultPath = defaultPath.substring(0, lastSlashIndex);
    }
    return defaultPath;
  }
  
  static String getDefaultDomain(CookieOrigin origin) {
    return origin.getHost();
  }
  
  public final List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException
  {
    Args.notNull(header, "Header");
    Args.notNull(origin, "Cookie origin");
    if (!header.getName().equalsIgnoreCase("Set-Cookie"))
      throw new MalformedCookieException("Unrecognized cookie header: '" + header.toString() + "'");
    ParserCursor cursor;
    CharArrayBuffer buffer;
    ParserCursor cursor;
    if ((header instanceof FormattedHeader)) {
      CharArrayBuffer buffer = ((FormattedHeader)header).getBuffer();
      cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
    } else {
      String s = header.getValue();
      if (s == null) {
        throw new MalformedCookieException("Header value is null");
      }
      buffer = new CharArrayBuffer(s.length());
      buffer.append(s);
      cursor = new ParserCursor(0, buffer.length());
    }
    String name = tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
    if (name.length() == 0) {
      return Collections.emptyList();
    }
    if (cursor.atEnd()) {
      return Collections.emptyList();
    }
    int valueDelim = buffer.charAt(cursor.getPos());
    cursor.updatePos(cursor.getPos() + 1);
    if (valueDelim != 61) {
      throw new MalformedCookieException("Cookie value is invalid: '" + header.toString() + "'");
    }
    String value = tokenParser.parseValue(buffer, cursor, VALUE_DELIMS);
    if (!cursor.atEnd()) {
      cursor.updatePos(cursor.getPos() + 1);
    }
    BasicClientCookie cookie = new BasicClientCookie(name, value);
    cookie.setPath(getDefaultPath(origin));
    cookie.setDomain(getDefaultDomain(origin));
    cookie.setCreationDate(new Date());
    
    Map<String, String> attribMap = new LinkedHashMap();
    while (!cursor.atEnd()) {
      String paramName = tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS).toLowerCase(Locale.ROOT);
      
      String paramValue = null;
      if (!cursor.atEnd()) {
        int paramDelim = buffer.charAt(cursor.getPos());
        cursor.updatePos(cursor.getPos() + 1);
        if (paramDelim == 61) {
          paramValue = tokenParser.parseToken(buffer, cursor, VALUE_DELIMS);
          if (!cursor.atEnd()) {
            cursor.updatePos(cursor.getPos() + 1);
          }
        }
      }
      cookie.setAttribute(paramName, paramValue);
      attribMap.put(paramName, paramValue);
    }
    
    if (attribMap.containsKey("max-age")) {
      attribMap.remove("expires");
    }
    
    for (Map.Entry<String, String> entry : attribMap.entrySet()) {
      String paramName = (String)entry.getKey();
      String paramValue = (String)entry.getValue();
      CookieAttributeHandler handler = (CookieAttributeHandler)attribHandlerMap.get(paramName);
      if (handler != null) {
        handler.parse(cookie, paramValue);
      }
    }
    
    return Collections.singletonList(cookie);
  }
  
  public final void validate(Cookie cookie, CookieOrigin origin)
    throws MalformedCookieException
  {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    for (CookieAttributeHandler handler : attribHandlers) {
      handler.validate(cookie, origin);
    }
  }
  
  public final boolean match(Cookie cookie, CookieOrigin origin)
  {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    for (CookieAttributeHandler handler : attribHandlers) {
      if (!handler.match(cookie, origin)) {
        return false;
      }
    }
    return true;
  }
  
  public List<Header> formatCookies(List<Cookie> cookies)
  {
    Args.notEmpty(cookies, "List of cookies");
    List<? extends Cookie> sortedCookies;
    if (cookies.size() > 1)
    {
      List<? extends Cookie> sortedCookies = new ArrayList(cookies);
      Collections.sort(sortedCookies, CookiePriorityComparator.INSTANCE);
    } else {
      sortedCookies = cookies;
    }
    CharArrayBuffer buffer = new CharArrayBuffer(20 * sortedCookies.size());
    buffer.append("Cookie");
    buffer.append(": ");
    for (int n = 0; n < sortedCookies.size(); n++) {
      Cookie cookie = (Cookie)sortedCookies.get(n);
      if (n > 0) {
        buffer.append(';');
        buffer.append(' ');
      }
      buffer.append(cookie.getName());
      String s = cookie.getValue();
      if (s != null) {
        buffer.append('=');
        if (containsSpecialChar(s)) {
          buffer.append('"');
          for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch == '"') || (ch == '\\')) {
              buffer.append('\\');
            }
            buffer.append(ch);
          }
          buffer.append('"');
        } else {
          buffer.append(s);
        }
      }
    }
    List<Header> headers = new ArrayList(1);
    headers.add(new BufferedHeader(buffer));
    return headers;
  }
  
  boolean containsSpecialChar(CharSequence s) {
    return containsChars(s, SPECIAL_CHARS);
  }
  
  boolean containsChars(CharSequence s, BitSet chars) {
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (chars.get(ch)) {
        return true;
      }
    }
    return false;
  }
  
  public final int getVersion()
  {
    return 0;
  }
  
  public final Header getVersionHeader()
  {
    return null;
  }
}
