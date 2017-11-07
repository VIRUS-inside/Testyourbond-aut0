package org.apache.commons.exec.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

































public class MapUtils
{
  public MapUtils() {}
  
  public static <K, V> Map<K, V> copy(Map<K, V> source)
  {
    if (source == null) {
      return null;
    }
    
    Map<K, V> result = new HashMap();
    result.putAll(source);
    return result;
  }
  














  public static <K, V> Map<String, V> prefix(Map<K, V> source, String prefix)
  {
    if (source == null) {
      return null;
    }
    
    Map<String, V> result = new HashMap();
    
    for (Map.Entry<K, V> entry : source.entrySet()) {
      K key = entry.getKey();
      V value = entry.getValue();
      result.put(prefix + '.' + key.toString(), value);
    }
    
    return result;
  }
  













  public static <K, V> Map<K, V> merge(Map<K, V> lhs, Map<K, V> rhs)
  {
    Map<K, V> result = null;
    
    if ((lhs == null) || (lhs.size() == 0)) {
      result = copy(rhs);
    }
    else if ((rhs == null) || (rhs.size() == 0)) {
      result = copy(lhs);
    }
    else {
      result = copy(lhs);
      result.putAll(rhs);
    }
    
    return result;
  }
}
