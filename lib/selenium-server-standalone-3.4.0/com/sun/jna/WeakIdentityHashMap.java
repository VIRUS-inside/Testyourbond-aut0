package com.sun.jna;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


































public class WeakIdentityHashMap
  implements Map
{
  private final ReferenceQueue queue = new ReferenceQueue();
  private Map backingStore = new HashMap();
  

  public WeakIdentityHashMap() {}
  

  public void clear()
  {
    backingStore.clear();
    reap();
  }
  
  public boolean containsKey(Object key) {
    reap();
    return backingStore.containsKey(new IdentityWeakReference(key));
  }
  
  public boolean containsValue(Object value) {
    reap();
    return backingStore.containsValue(value);
  }
  
  public Set entrySet() {
    reap();
    Set ret = new HashSet();
    for (Iterator i = backingStore.entrySet().iterator(); i.hasNext();) {
      Map.Entry ref = (Map.Entry)i.next();
      final Object key = ((IdentityWeakReference)ref.getKey()).get();
      final Object value = ref.getValue();
      Map.Entry entry = new Map.Entry() {
        public Object getKey() {
          return key;
        }
        
        public Object getValue() { return value; }
        
        public Object setValue(Object value) {
          throw new UnsupportedOperationException();
        }
      };
      ret.add(entry);
    }
    return Collections.unmodifiableSet(ret);
  }
  
  public Set keySet() { reap();
    Set ret = new HashSet();
    for (Iterator i = backingStore.keySet().iterator(); i.hasNext();) {
      IdentityWeakReference ref = (IdentityWeakReference)i.next();
      ret.add(ref.get());
    }
    return Collections.unmodifiableSet(ret);
  }
  
  public boolean equals(Object o) {
    return backingStore.equals(backingStore);
  }
  
  public Object get(Object key) {
    reap();
    return backingStore.get(new IdentityWeakReference(key));
  }
  
  public Object put(Object key, Object value) { reap();
    return backingStore.put(new IdentityWeakReference(key), value);
  }
  
  public int hashCode() {
    reap();
    return backingStore.hashCode();
  }
  
  public boolean isEmpty() { reap();
    return backingStore.isEmpty();
  }
  
  public void putAll(Map t) { throw new UnsupportedOperationException(); }
  
  public Object remove(Object key) {
    reap();
    return backingStore.remove(new IdentityWeakReference(key));
  }
  
  public int size() { reap();
    return backingStore.size();
  }
  
  public Collection values() { reap();
    return backingStore.values();
  }
  
  private synchronized void reap() {
    Object zombie = queue.poll();
    
    while (zombie != null) {
      IdentityWeakReference victim = (IdentityWeakReference)zombie;
      backingStore.remove(victim);
      zombie = queue.poll();
    }
  }
  
  class IdentityWeakReference extends WeakReference
  {
    int hash;
    
    IdentityWeakReference(Object obj) {
      super(queue);
      hash = System.identityHashCode(obj);
    }
    
    public int hashCode() {
      return hash;
    }
    
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      IdentityWeakReference ref = (IdentityWeakReference)o;
      if (get() == ref.get()) {
        return true;
      }
      return false;
    }
  }
}
