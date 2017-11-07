package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;






















public class StorageHolder
  implements Serializable
{
  public StorageHolder() {}
  
  public static enum Type
  {
    GLOBAL_STORAGE, 
    
    LOCAL_STORAGE, 
    
    SESSION_STORAGE;
  }
  

  private Map<String, Map<String, String>> globalStorage_ = new HashMap();
  
  private Map<String, Map<String, String>> localStorage_ = new HashMap();
  
  private transient Map<String, Map<String, String>> sessionStorage_ = new HashMap();
  





  public Map<String, String> getStore(Type storageType, Page page)
  {
    String key = getKey(storageType, page);
    Map<String, Map<String, String>> storage = getStorage(storageType);
    
    synchronized (storage) {
      Map<String, String> map = (Map)storage.get(key);
      if (map == null) {
        map = new LinkedHashMap();
        storage.put(key, map);
      }
      return map;
    }
  }
  
  private static String getKey(Type type, Page page) {
    switch (type) {
    case GLOBAL_STORAGE: 
      return page.getUrl().getHost();
    
    case LOCAL_STORAGE: 
      URL url = page.getUrl();
      return url.getProtocol() + "://" + url.getHost() + ':' + 
        url.getProtocol();
    
    case SESSION_STORAGE: 
      WebWindow topWindow = page.getEnclosingWindow()
        .getTopWindow();
      return Integer.toHexString(topWindow.hashCode());
    }
    
    return null;
  }
  
  private Map<String, Map<String, String>> getStorage(Type type)
  {
    switch (type) {
    case GLOBAL_STORAGE: 
      return globalStorage_;
    
    case LOCAL_STORAGE: 
      return localStorage_;
    
    case SESSION_STORAGE: 
      return sessionStorage_;
    }
    
    return null;
  }
}
