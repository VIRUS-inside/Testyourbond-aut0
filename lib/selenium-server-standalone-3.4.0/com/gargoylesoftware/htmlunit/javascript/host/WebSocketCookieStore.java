package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


















class WebSocketCookieStore
  implements CookieStore
{
  private final WebClient webClient_;
  
  WebSocketCookieStore(WebClient webClient)
  {
    webClient_ = webClient;
  }
  



  public void add(URI uri, HttpCookie cookie)
  {
    throw new UnsupportedOperationException();
  }
  



  public List<HttpCookie> get(URI uri)
  {
    List<HttpCookie> cookies = new ArrayList();
    try {
      String urlString = uri.toString().replace("ws://", "http://").replace("wss://", "https://");
      URL url = new URL(urlString);
      for (Cookie cookie : webClient_.getCookies(url)) {
        HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
        httpCookie.setVersion(0);
        cookies.add(httpCookie);
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    return cookies;
  }
  



  public List<HttpCookie> getCookies()
  {
    throw new UnsupportedOperationException();
  }
  



  public List<URI> getURIs()
  {
    throw new UnsupportedOperationException();
  }
  



  public boolean remove(URI uri, HttpCookie cookie)
  {
    throw new UnsupportedOperationException();
  }
  



  public boolean removeAll()
  {
    return false;
  }
}
