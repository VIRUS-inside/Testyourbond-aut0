package com.gargoylesoftware.htmlunit.httpclient;

import com.gargoylesoftware.htmlunit.CookieManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.ClientCookie;



























public final class HtmlUnitCookieStore
  implements CookieStore, Serializable
{
  private CookieManager manager_;
  
  public HtmlUnitCookieStore(CookieManager manager)
  {
    manager_ = manager;
  }
  



  public synchronized void addCookie(org.apache.http.cookie.Cookie cookie)
  {
    manager_.addCookie(new com.gargoylesoftware.htmlunit.util.Cookie((ClientCookie)cookie));
  }
  



  public synchronized List<org.apache.http.cookie.Cookie> getCookies()
  {
    if (manager_.isCookiesEnabled()) {
      return com.gargoylesoftware.htmlunit.util.Cookie.toHttpClient(manager_.getCookies());
    }
    return Collections.emptyList();
  }
  



  public synchronized boolean clearExpired(Date date)
  {
    return manager_.clearExpired(date);
  }
  



  public synchronized void clear()
  {
    manager_.clearCookies();
  }
}
