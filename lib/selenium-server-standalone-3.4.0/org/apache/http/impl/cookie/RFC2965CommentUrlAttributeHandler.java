package org.apache.http.impl.cookie;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;



































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class RFC2965CommentUrlAttributeHandler
  implements CommonCookieAttributeHandler
{
  public RFC2965CommentUrlAttributeHandler() {}
  
  public void parse(SetCookie cookie, String commenturl)
    throws MalformedCookieException
  {
    if ((cookie instanceof SetCookie2)) {
      SetCookie2 cookie2 = (SetCookie2)cookie;
      cookie2.setCommentURL(commenturl);
    }
  }
  

  public void validate(Cookie cookie, CookieOrigin origin)
    throws MalformedCookieException
  {}
  
  public boolean match(Cookie cookie, CookieOrigin origin)
  {
    return true;
  }
  
  public String getAttributeName()
  {
    return "commenturl";
  }
}
