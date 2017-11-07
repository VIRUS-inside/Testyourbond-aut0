package org.apache.http.impl.cookie;

import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;




































@Contract(threading=ThreadingBehavior.SAFE)
public class DefaultCookieSpec
  implements CookieSpec
{
  private final RFC2965Spec strict;
  private final RFC2109Spec obsoleteStrict;
  private final NetscapeDraftSpec netscapeDraft;
  
  DefaultCookieSpec(RFC2965Spec strict, RFC2109Spec obsoleteStrict, NetscapeDraftSpec netscapeDraft)
  {
    this.strict = strict;
    this.obsoleteStrict = obsoleteStrict;
    this.netscapeDraft = netscapeDraft;
  }
  

  public DefaultCookieSpec(String[] datepatterns, boolean oneHeader)
  {
    strict = new RFC2965Spec(oneHeader, new CommonCookieAttributeHandler[] { new RFC2965VersionAttributeHandler(), new BasicPathHandler(), new RFC2965DomainAttributeHandler(), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler() });
    








    obsoleteStrict = new RFC2109Spec(oneHeader, new CommonCookieAttributeHandler[] { new RFC2109VersionHandler(), new BasicPathHandler(), new RFC2109DomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler() });
    





    netscapeDraft = new NetscapeDraftSpec(new CommonCookieAttributeHandler[] { new BasicDomainHandler(), new BasicPathHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler(new String[] { datepatterns != null ? (String[])datepatterns.clone() : "EEE, dd-MMM-yy HH:mm:ss z" }) });
  }
  





  public DefaultCookieSpec()
  {
    this(null, false);
  }
  

  public List<Cookie> parse(Header header, CookieOrigin origin)
    throws MalformedCookieException
  {
    Args.notNull(header, "Header");
    Args.notNull(origin, "Cookie origin");
    HeaderElement[] helems = header.getElements();
    boolean versioned = false;
    boolean netscape = false;
    for (HeaderElement helem : helems) {
      if (helem.getParameterByName("version") != null) {
        versioned = true;
      }
      if (helem.getParameterByName("expires") != null) {
        netscape = true;
      }
    }
    if ((netscape) || (!versioned))
    {

      NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
      ParserCursor cursor;
      CharArrayBuffer buffer;
      ParserCursor cursor; if ((header instanceof FormattedHeader)) {
        CharArrayBuffer buffer = ((FormattedHeader)header).getBuffer();
        cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
      }
      else
      {
        String s = header.getValue();
        if (s == null) {
          throw new MalformedCookieException("Header value is null");
        }
        buffer = new CharArrayBuffer(s.length());
        buffer.append(s);
        cursor = new ParserCursor(0, buffer.length());
      }
      helems = new HeaderElement[] { parser.parseHeader(buffer, cursor) };
      return netscapeDraft.parse(helems, origin);
    }
    if ("Set-Cookie2".equals(header.getName())) {
      return strict.parse(helems, origin);
    }
    return obsoleteStrict.parse(helems, origin);
  }
  



  public void validate(Cookie cookie, CookieOrigin origin)
    throws MalformedCookieException
  {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    if (cookie.getVersion() > 0) {
      if ((cookie instanceof SetCookie2)) {
        strict.validate(cookie, origin);
      } else {
        obsoleteStrict.validate(cookie, origin);
      }
    } else {
      netscapeDraft.validate(cookie, origin);
    }
  }
  
  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    if (cookie.getVersion() > 0) {
      if ((cookie instanceof SetCookie2)) {
        return strict.match(cookie, origin);
      }
      return obsoleteStrict.match(cookie, origin);
    }
    
    return netscapeDraft.match(cookie, origin);
  }
  

  public List<Header> formatCookies(List<Cookie> cookies)
  {
    Args.notNull(cookies, "List of cookies");
    int version = Integer.MAX_VALUE;
    boolean isSetCookie2 = true;
    for (Cookie cookie : cookies) {
      if (!(cookie instanceof SetCookie2)) {
        isSetCookie2 = false;
      }
      if (cookie.getVersion() < version) {
        version = cookie.getVersion();
      }
    }
    if (version > 0) {
      if (isSetCookie2) {
        return strict.formatCookies(cookies);
      }
      return obsoleteStrict.formatCookies(cookies);
    }
    
    return netscapeDraft.formatCookies(cookies);
  }
  

  public int getVersion()
  {
    return strict.getVersion();
  }
  
  public Header getVersionHeader()
  {
    return null;
  }
  
  public String toString()
  {
    return "default";
  }
}
