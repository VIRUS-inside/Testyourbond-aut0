package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;












































































































public final class WeakHashtable
  extends Hashtable
{
  private static final long serialVersionUID = -1546036869799732453L;
  private static final int MAX_CHANGES_BEFORE_PURGE = 100;
  private static final int PARTIAL_PURGE_COUNT = 10;
  private final ReferenceQueue queue = new ReferenceQueue();
  
  private int changeCount = 0;
  




  public WeakHashtable() {}
  



  public boolean containsKey(Object key)
  {
    Referenced referenced = new Referenced(key, null);
    return super.containsKey(referenced);
  }
  


  public Enumeration elements()
  {
    purge();
    return super.elements();
  }
  


  public Set entrySet()
  {
    purge();
    Set referencedEntries = super.entrySet();
    Set unreferencedEntries = new HashSet();
    for (Iterator it = referencedEntries.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry)it.next();
      Referenced referencedKey = (Referenced)entry.getKey();
      Object key = referencedKey.getValue();
      Object value = entry.getValue();
      if (key != null) {
        Entry dereferencedEntry = new Entry(key, value, null);
        unreferencedEntries.add(dereferencedEntry);
      }
    }
    return unreferencedEntries;
  }
  



  public Object get(Object key)
  {
    Referenced referenceKey = new Referenced(key, null);
    return super.get(referenceKey);
  }
  


  public Enumeration keys()
  {
    purge();
    Enumeration enumer = super.keys();
    new Enumeration() { private final Enumeration val$enumer;
      
      public boolean hasMoreElements() { return val$enumer.hasMoreElements(); }
      
      public Object nextElement() {
        WeakHashtable.Referenced nextReference = (WeakHashtable.Referenced)val$enumer.nextElement();
        return nextReference.getValue();
      }
    };
  }
  


  public Set keySet()
  {
    purge();
    Set referencedKeys = super.keySet();
    Set unreferencedKeys = new HashSet();
    for (Iterator it = referencedKeys.iterator(); it.hasNext();) {
      Referenced referenceKey = (Referenced)it.next();
      Object keyValue = referenceKey.getValue();
      if (keyValue != null) {
        unreferencedKeys.add(keyValue);
      }
    }
    return unreferencedKeys;
  }
  



  public synchronized Object put(Object key, Object value)
  {
    if (key == null) {
      throw new NullPointerException("Null keys are not allowed");
    }
    if (value == null) {
      throw new NullPointerException("Null values are not allowed");
    }
    


    if (changeCount++ > 100) {
      purge();
      changeCount = 0;

    }
    else if (changeCount % 10 == 0) {
      purgeOne();
    }
    
    Referenced keyRef = new Referenced(key, queue, null);
    return super.put(keyRef, value);
  }
  

  public void putAll(Map t)
  {
    Iterator it;
    if (t != null) {
      Set entrySet = t.entrySet();
      for (it = entrySet.iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry)it.next();
        put(entry.getKey(), entry.getValue());
      }
    }
  }
  


  public Collection values()
  {
    purge();
    return super.values();
  }
  




  public synchronized Object remove(Object key)
  {
    if (changeCount++ > 100) {
      purge();
      changeCount = 0;

    }
    else if (changeCount % 10 == 0) {
      purgeOne();
    }
    return super.remove(new Referenced(key, null));
  }
  


  public boolean isEmpty()
  {
    purge();
    return super.isEmpty();
  }
  


  public int size()
  {
    purge();
    return super.size();
  }
  


  public String toString()
  {
    purge();
    return super.toString();
  }
  



  protected void rehash()
  {
    purge();
    super.rehash();
  }
  



  private void purge()
  {
    List toRemove = new ArrayList();
    synchronized (queue) {
      WeakKey key;
      while ((key = (WeakKey)queue.poll()) != null) {
        toRemove.add(key.getReferenced());
      }
    }
    



    int size = toRemove.size();
    for (int i = 0; i < size; i++) {
      super.remove(toRemove.get(i));
    }
  }
  



  private void purgeOne()
  {
    synchronized (queue) {
      WeakKey key = (WeakKey)queue.poll();
      if (key != null)
        super.remove(key.getReferenced());
    } }
  
  private static final class Entry implements Map.Entry { private final Object key;
    private final Object value;
    
    Entry(Object x0, Object x1, WeakHashtable.1 x2) { this(x0, x1); }
    


    private Entry(Object key, Object value)
    {
      this.key = key;
      this.value = value;
    }
    
    public boolean equals(Object o) {
      boolean result = false;
      if ((o != null) && ((o instanceof Map.Entry))) {
        Map.Entry entry = (Map.Entry)o;
        result = (getKey() == null ? entry.getKey() == null : getKey().equals(entry.getKey())) && (getValue() == null ? entry.getValue() == null : getValue().equals(entry.getValue()));
      }
      




      return result;
    }
    
    public int hashCode() {
      return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
    }
    
    public Object setValue(Object value)
    {
      throw new UnsupportedOperationException("Entry.setValue is not supported.");
    }
    
    public Object getValue() {
      return value;
    }
    

    public Object getKey() { return key; } }
  
  private static final class Referenced { private final WeakReference reference;
    private final int hashCode;
    
    Referenced(Object x0, WeakHashtable.1 x1) { this(x0); } Referenced(Object x0, ReferenceQueue x1, WeakHashtable.1 x2) { this(x0, x1); }
    






    private Referenced(Object referant)
    {
      reference = new WeakReference(referant);
      

      hashCode = referant.hashCode();
    }
    



    private Referenced(Object key, ReferenceQueue queue)
    {
      reference = new WeakHashtable.WeakKey(key, queue, this, null);
      

      hashCode = key.hashCode();
    }
    
    public int hashCode()
    {
      return hashCode;
    }
    
    private Object getValue() {
      return reference.get();
    }
    
    public boolean equals(Object o) {
      boolean result = false;
      if ((o instanceof Referenced)) {
        Referenced otherKey = (Referenced)o;
        Object thisKeyValue = getValue();
        Object otherKeyValue = otherKey.getValue();
        if (thisKeyValue == null) {
          result = otherKeyValue == null;
          






          result = (result) && (hashCode() == otherKey.hashCode());


        }
        else
        {


          result = thisKeyValue.equals(otherKeyValue);
        }
      }
      return result;
    }
  }
  
  private static final class WeakKey extends WeakReference
  {
    private final WeakHashtable.Referenced referenced;
    
    WeakKey(Object x0, ReferenceQueue x1, WeakHashtable.Referenced x2, WeakHashtable.1 x3) {
      this(x0, x1, x2);
    }
    


    private WeakKey(Object key, ReferenceQueue queue, WeakHashtable.Referenced referenced)
    {
      super(queue);
      this.referenced = referenced;
    }
    
    private WeakHashtable.Referenced getReferenced() {
      return referenced;
    }
  }
}
