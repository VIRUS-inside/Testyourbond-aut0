package org.apache.http.impl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;


































@Contract(threading=ThreadingBehavior.SAFE)
public class BasicCookieStore
  implements CookieStore, Serializable
{
  private static final long serialVersionUID = -7581093305228232025L;
  private final TreeSet<Cookie> cookies;
  
  public BasicCookieStore()
  {
    cookies = new TreeSet(new CookieIdentityComparator());
  }
  










  public synchronized void addCookie(Cookie cookie)
  {
    if (cookie != null)
    {
      cookies.remove(cookie);
      if (!cookie.isExpired(new Date())) {
        cookies.add(cookie);
      }
    }
  }
  









  public synchronized void addCookies(Cookie[] cookies)
  {
    if (cookies != null) {
      for (Cookie cooky : cookies) {
        addCookie(cooky);
      }
    }
  }
  







  public synchronized List<Cookie> getCookies()
  {
    return new ArrayList(cookies);
  }
  








  public synchronized boolean clearExpired(Date date)
  {
    if (date == null) {
      return false;
    }
    boolean removed = false;
    for (Iterator<Cookie> it = cookies.iterator(); it.hasNext();) {
      if (((Cookie)it.next()).isExpired(date)) {
        it.remove();
        removed = true;
      }
    }
    return removed;
  }
  



  public synchronized void clear()
  {
    cookies.clear();
  }
  
  public synchronized String toString()
  {
    return cookies.toString();
  }
}
