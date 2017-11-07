package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;





































@Deprecated
public class PublicSuffixFilter
  implements CookieAttributeHandler
{
  private final CookieAttributeHandler wrapped;
  private Collection<String> exceptions;
  private Collection<String> suffixes;
  private PublicSuffixMatcher matcher;
  
  public PublicSuffixFilter(CookieAttributeHandler wrapped)
  {
    this.wrapped = wrapped;
  }
  





  public void setPublicSuffixes(Collection<String> suffixes)
  {
    this.suffixes = suffixes;
    matcher = null;
  }
  




  public void setExceptions(Collection<String> exceptions)
  {
    this.exceptions = exceptions;
    matcher = null;
  }
  



  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    if (isForPublicSuffix(cookie)) {
      return false;
    }
    return wrapped.match(cookie, origin);
  }
  
  public void parse(SetCookie cookie, String value) throws MalformedCookieException
  {
    wrapped.parse(cookie, value);
  }
  
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException
  {
    wrapped.validate(cookie, origin);
  }
  
  private boolean isForPublicSuffix(Cookie cookie) {
    if (matcher == null) {
      matcher = new PublicSuffixMatcher(suffixes, exceptions);
    }
    return matcher.matches(cookie.getDomain());
  }
}
