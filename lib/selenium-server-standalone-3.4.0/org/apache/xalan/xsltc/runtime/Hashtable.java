package org.apache.xalan.xsltc.runtime;

import java.util.Enumeration;























































public class Hashtable
{
  private transient HashtableEntry[] table;
  private transient int count;
  private int threshold;
  private float loadFactor;
  
  public Hashtable(int initialCapacity, float loadFactor)
  {
    if (initialCapacity <= 0) initialCapacity = 11;
    if (loadFactor <= 0.0D) loadFactor = 0.75F;
    this.loadFactor = loadFactor;
    table = new HashtableEntry[initialCapacity];
    threshold = ((int)(initialCapacity * loadFactor));
  }
  



  public Hashtable(int initialCapacity)
  {
    this(initialCapacity, 0.75F);
  }
  



  public Hashtable()
  {
    this(101, 0.75F);
  }
  


  public int size()
  {
    return count;
  }
  


  public boolean isEmpty()
  {
    return count == 0;
  }
  


  public Enumeration keys()
  {
    return new HashtableEnumerator(table, true);
  }
  




  public Enumeration elements()
  {
    return new HashtableEnumerator(table, false);
  }
  





  public boolean contains(Object value)
  {
    if (value == null) { throw new NullPointerException();
    }
    

    HashtableEntry[] tab = table;
    
    for (int i = tab.length; i-- > 0;) {
      for (HashtableEntry e = tab[i]; e != null; e = next) {
        if (value.equals(value)) {
          return true;
        }
      }
    }
    return false;
  }
  



  public boolean containsKey(Object key)
  {
    HashtableEntry[] tab = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    for (HashtableEntry e = tab[index]; e != null; e = next) {
      if ((hash == hash) && (key.equals(key)))
        return true;
    }
    return false;
  }
  



  public Object get(Object key)
  {
    HashtableEntry[] tab = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    for (HashtableEntry e = tab[index]; e != null; e = next) {
      if ((hash == hash) && (key.equals(key)))
        return value;
    }
    return null;
  }
  







  protected void rehash()
  {
    int oldCapacity = table.length;
    HashtableEntry[] oldTable = table;
    
    int newCapacity = oldCapacity * 2 + 1;
    HashtableEntry[] newTable = new HashtableEntry[newCapacity];
    
    threshold = ((int)(newCapacity * loadFactor));
    table = newTable;
    
    for (int i = oldCapacity; i-- > 0;) {
      for (old = oldTable[i]; old != null;) {
        HashtableEntry e = old;
        old = next;
        int index = (hash & 0x7FFFFFFF) % newCapacity;
        next = newTable[index];
        newTable[index] = e;
      }
    }
    



    HashtableEntry old;
  }
  



  public Object put(Object key, Object value)
  {
    if (value == null) { throw new NullPointerException();
    }
    

    HashtableEntry[] tab = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    for (HashtableEntry e = tab[index]; e != null; e = next) {
      if ((hash == hash) && (key.equals(key))) {
        Object old = value;
        value = value;
        return old;
      }
    }
    

    if (count >= threshold) {
      rehash();
      return put(key, value);
    }
    

    e = new HashtableEntry();
    hash = hash;
    key = key;
    value = value;
    next = tab[index];
    tab[index] = e;
    count += 1;
    return null;
  }
  




  public Object remove(Object key)
  {
    HashtableEntry[] tab = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    HashtableEntry e = tab[index]; for (HashtableEntry prev = null; e != null; e = next) {
      if ((hash == hash) && (key.equals(key))) {
        if (prev != null) {
          next = next;
        } else
          tab[index] = next;
        count -= 1;
        return value;
      }
      prev = e;
    }
    







    return null;
  }
  


  public void clear()
  {
    HashtableEntry[] tab = table;
    int index = tab.length; for (;;) { index--; if (index < 0) break;
      tab[index] = null; }
    count = 0;
  }
  




  public String toString()
  {
    int max = size() - 1;
    StringBuffer buf = new StringBuffer();
    Enumeration k = keys();
    Enumeration e = elements();
    buf.append("{");
    
    for (int i = 0; i <= max; i++) {
      String s1 = k.nextElement().toString();
      String s2 = e.nextElement().toString();
      buf.append(s1 + "=" + s2);
      if (i < max) buf.append(", ");
    }
    buf.append("}");
    return buf.toString();
  }
  

  static class HashtableEnumerator
    implements Enumeration
  {
    boolean keys;
    int index;
    HashtableEntry[] table;
    HashtableEntry entry;
    
    HashtableEnumerator(HashtableEntry[] table, boolean keys)
    {
      this.table = table;
      this.keys = keys;
      index = table.length;
    }
    
    public boolean hasMoreElements() {
      if (entry != null) {
        return true;
      }
      while (index-- > 0) {
        if ((this.entry = table[index]) != null) {
          return true;
        }
      }
      return false;
    }
    
    public Object nextElement() {
      while ((entry == null) && 
        (index-- > 0) && ((this.entry = table[index]) == null)) {}
      
      if (entry != null) {
        HashtableEntry e = entry;
        entry = next;
        return keys ? key : value;
      }
      return null;
    }
  }
}
