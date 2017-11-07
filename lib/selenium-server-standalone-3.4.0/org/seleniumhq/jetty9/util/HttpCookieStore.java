package org.seleniumhq.jetty9.util;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Collections;
import java.util.List;






















public class HttpCookieStore
  implements CookieStore
{
  private final CookieStore delegate;
  
  public HttpCookieStore()
  {
    delegate = new CookieManager().getCookieStore();
  }
  

  public void add(URI uri, HttpCookie cookie)
  {
    delegate.add(uri, cookie);
  }
  

  public List<HttpCookie> get(URI uri)
  {
    return delegate.get(uri);
  }
  

  public List<HttpCookie> getCookies()
  {
    return delegate.getCookies();
  }
  

  public List<URI> getURIs()
  {
    return delegate.getURIs();
  }
  

  public boolean remove(URI uri, HttpCookie cookie)
  {
    return delegate.remove(uri, cookie);
  }
  

  public boolean removeAll()
  {
    return delegate.removeAll();
  }
  

  public static class Empty
    implements CookieStore
  {
    public Empty() {}
    
    public void add(URI uri, HttpCookie cookie) {}
    
    public List<HttpCookie> get(URI uri)
    {
      return Collections.emptyList();
    }
    

    public List<HttpCookie> getCookies()
    {
      return Collections.emptyList();
    }
    

    public List<URI> getURIs()
    {
      return Collections.emptyList();
    }
    

    public boolean remove(URI uri, HttpCookie cookie)
    {
      return false;
    }
    

    public boolean removeAll()
    {
      return false;
    }
  }
}
