package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.utils.DateUtils;
import org.w3c.dom.css.CSSStyleSheet;


























public class Cache
  implements Serializable
{
  private int maxSize_ = 40;
  
  private static final Pattern DATE_HEADER_PATTERN = Pattern.compile("-?\\d+");
  







  private final Map<String, Entry> entries_ = Collections.synchronizedMap(new HashMap(maxSize_));
  public Cache() {}
  
  private static class Entry implements Comparable<Entry>, Serializable
  {
    private final String key_;
    private WebResponse response_;
    private Object value_;
    private long lastAccess_;
    
    Entry(String key, WebResponse response, Object value)
    {
      key_ = key;
      response_ = response;
      value_ = value;
      lastAccess_ = System.currentTimeMillis();
    }
    



    public int compareTo(Entry other)
    {
      if (lastAccess_ < lastAccess_) {
        return -1;
      }
      if (lastAccess_ == lastAccess_) {
        return 0;
      }
      return 1;
    }
    



    public boolean equals(Object obj)
    {
      return ((obj instanceof Entry)) && (lastAccess_ == lastAccess_);
    }
    



    public int hashCode()
    {
      return Long.valueOf(lastAccess_).hashCode();
    }
    


    public void touch()
    {
      lastAccess_ = System.currentTimeMillis();
    }
  }
  









  public boolean cacheIfPossible(WebRequest request, WebResponse response, Object toCache)
  {
    if (isCacheable(request, response)) {
      URL url = response.getWebRequest().getUrl();
      if (url == null) {
        return false;
      }
      
      Entry entry = new Entry(UrlUtils.normalize(url), response, toCache);
      entries_.put(key_, entry);
      deleteOverflow();
      return true;
    }
    
    return false;
  }
  










  public void cache(String css, CSSStyleSheet styleSheet)
  {
    Entry entry = new Entry(css, null, styleSheet);
    entries_.put(key_, entry);
    deleteOverflow();
  }
  


  protected void deleteOverflow()
  {
    synchronized (entries_) {
      while (entries_.size() > maxSize_) {
        Entry oldestEntry = (Entry)Collections.min(entries_.values());
        entries_.remove(key_);
        if (response_ != null) {
          response_.cleanUp();
        }
      }
    }
  }
  






  protected boolean isCacheable(WebRequest request, WebResponse response)
  {
    return (HttpMethod.GET == response.getWebRequest().getHttpMethod()) && 
      (isCacheableContent(response));
  }
  














  protected boolean isCacheableContent(WebResponse response)
  {
    Date lastModified = parseDateHeader(response, "Last-Modified");
    Date expires = parseDateHeader(response, "Expires");
    
    long delay = 600000L;
    long now = getCurrentTimestamp();
    
    return ((expires != null) && (expires.getTime() - now > 600000L)) || (
      (expires == null) && (lastModified != null) && (now - lastModified.getTime() > 600000L));
  }
  



  protected long getCurrentTimestamp()
  {
    return System.currentTimeMillis();
  }
  







  protected Date parseDateHeader(WebResponse response, String headerName)
  {
    String value = response.getResponseHeaderValue(headerName);
    if (value == null) {
      return null;
    }
    Matcher matcher = DATE_HEADER_PATTERN.matcher(value);
    if (matcher.matches()) {
      return new Date();
    }
    return DateUtils.parseDate(value);
  }
  






  public WebResponse getCachedResponse(WebRequest request)
  {
    Entry cachedEntry = getCacheEntry(request);
    if (cachedEntry == null) {
      return null;
    }
    return response_;
  }
  






  public Object getCachedObject(WebRequest request)
  {
    Entry cachedEntry = getCacheEntry(request);
    if (cachedEntry == null) {
      return null;
    }
    return value_;
  }
  
  private Entry getCacheEntry(WebRequest request) {
    if (HttpMethod.GET != request.getHttpMethod()) {
      return null;
    }
    
    URL url = request.getUrl();
    if (url == null) {
      return null;
    }
    Entry cachedEntry = (Entry)entries_.get(UrlUtils.normalize(url));
    if (cachedEntry == null) {
      return null;
    }
    synchronized (entries_) {
      cachedEntry.touch();
    }
    return cachedEntry;
  }
  






  public CSSStyleSheet getCachedStyleSheet(String css)
  {
    Entry cachedEntry = (Entry)entries_.get(css);
    if (cachedEntry == null) {
      return null;
    }
    synchronized (entries_) {
      cachedEntry.touch();
    }
    return (CSSStyleSheet)value_;
  }
  





  public int getMaxSize()
  {
    return maxSize_;
  }
  





  public void setMaxSize(int maxSize)
  {
    if (maxSize < 0) {
      throw new IllegalArgumentException("Illegal value for maxSize: " + maxSize);
    }
    maxSize_ = maxSize;
    deleteOverflow();
  }
  




  public int getSize()
  {
    return entries_.size();
  }
  


  public void clear()
  {
    synchronized (entries_) {
      for (Entry entry : entries_.values()) {
        if (response_ != null) {
          response_.cleanUp();
        }
      }
      entries_.clear();
    }
  }
}
