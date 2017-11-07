package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;












public class ObjToIntMap
  implements Serializable
{
  static final long serialVersionUID = -1542220580748809402L;
  private static final int A = -1640531527;
  
  public static class Iterator
  {
    ObjToIntMap master;
    private int cursor;
    private int remaining;
    private Object[] keys;
    private int[] values;
    
    Iterator(ObjToIntMap master)
    {
      this.master = master;
    }
    
    final void init(Object[] keys, int[] values, int keyCount) {
      this.keys = keys;
      this.values = values;
      cursor = -1;
      remaining = keyCount;
    }
    
    public void start() {
      master.initIterator(this);
      next();
    }
    
    public boolean done() {
      return remaining < 0;
    }
    
    public void next() {
      if (remaining == -1)
        Kit.codeBug();
      if (remaining == 0) {
        remaining = -1;
        cursor = -1;
      } else {
        for (cursor += 1;; cursor += 1) {
          Object key = keys[cursor];
          if ((key != null) && (key != ObjToIntMap.DELETED)) {
            remaining -= 1;
            break;
          }
        }
      }
    }
    
    public Object getKey() {
      Object key = keys[cursor];
      if (key == UniqueTag.NULL_VALUE) {
        key = null;
      }
      return key;
    }
    
    public int getValue() {
      return values[cursor];
    }
    
    public void setValue(int value) {
      values[cursor] = value;
    }
  }
  





  public ObjToIntMap()
  {
    this(4);
  }
  
  public ObjToIntMap(int keyCountHint) {
    if (keyCountHint < 0) {
      Kit.codeBug();
    }
    int minimalCapacity = keyCountHint * 4 / 3;
    
    for (int i = 2; 1 << i < minimalCapacity; i++) {}
    
    power = i;
  }
  

  public boolean isEmpty()
  {
    return keyCount == 0;
  }
  
  public int size() {
    return keyCount;
  }
  
  public boolean has(Object key) {
    if (key == null) {
      key = UniqueTag.NULL_VALUE;
    }
    return 0 <= findIndex(key);
  }
  




  public int get(Object key, int defaultValue)
  {
    if (key == null) {
      key = UniqueTag.NULL_VALUE;
    }
    int index = findIndex(key);
    if (0 <= index) {
      return values[index];
    }
    return defaultValue;
  }
  






  public int getExisting(Object key)
  {
    if (key == null) {
      key = UniqueTag.NULL_VALUE;
    }
    int index = findIndex(key);
    if (0 <= index) {
      return values[index];
    }
    
    Kit.codeBug();
    return 0;
  }
  
  public void put(Object key, int value) {
    if (key == null) {
      key = UniqueTag.NULL_VALUE;
    }
    int index = ensureIndex(key);
    values[index] = value;
  }
  




  public Object intern(Object keyArg)
  {
    boolean nullKey = false;
    if (keyArg == null) {
      nullKey = true;
      keyArg = UniqueTag.NULL_VALUE;
    }
    int index = ensureIndex(keyArg);
    values[index] = 0;
    return nullKey ? null : keys[index];
  }
  
  public void remove(Object key) {
    if (key == null) {
      key = UniqueTag.NULL_VALUE;
    }
    int index = findIndex(key);
    if (0 <= index) {
      keys[index] = DELETED;
      keyCount -= 1;
    }
  }
  
  public void clear() {
    int i = keys.length;
    while (i != 0) {
      keys[(--i)] = null;
    }
    keyCount = 0;
    occupiedCount = 0;
  }
  
  public Iterator newIterator() {
    return new Iterator(this);
  }
  


  final void initIterator(Iterator i)
  {
    i.init(keys, values, keyCount);
  }
  
  public Object[] getKeys()
  {
    Object[] array = new Object[keyCount];
    getKeys(array, 0);
    return array;
  }
  
  public void getKeys(Object[] array, int offset) {
    int count = keyCount;
    for (int i = 0; count != 0; i++) {
      Object key = keys[i];
      if ((key != null) && (key != DELETED)) {
        if (key == UniqueTag.NULL_VALUE) {
          key = null;
        }
        array[offset] = key;
        offset++;
        count--;
      }
    }
  }
  
  private static int tableLookupStep(int fraction, int mask, int power) {
    int shift = 32 - 2 * power;
    if (shift >= 0) {
      return fraction >>> shift & mask | 0x1;
    }
    return fraction & mask >>> -shift | 0x1;
  }
  
  private int findIndex(Object key)
  {
    if (keys != null) {
      int hash = key.hashCode();
      int fraction = hash * -1640531527;
      int index = fraction >>> 32 - power;
      Object test = keys[index];
      if (test != null) {
        int N = 1 << power;
        if ((test == key) || ((values[(N + index)] == hash) && 
          (test.equals(key)))) {
          return index;
        }
        
        int mask = N - 1;
        int step = tableLookupStep(fraction, mask, power);
        int n = 0;
        



        do
        {
          index = index + step & mask;
          test = keys[index];
          if (test == null) {
            break;
          }
        } while ((test != key) && ((values[(N + index)] != hash) || 
          (!test.equals(key))));
        return index;
      }
    }
    

    return -1;
  }
  





  private int insertNewKey(Object key, int hash)
  {
    int fraction = hash * -1640531527;
    int index = fraction >>> 32 - power;
    int N = 1 << power;
    if (keys[index] != null) {
      int mask = N - 1;
      int step = tableLookupStep(fraction, mask, power);
      int firstIndex = index;
      
      do
      {
        index = index + step & mask;

      }
      while (keys[index] != null);
    }
    keys[index] = key;
    values[(N + index)] = hash;
    occupiedCount += 1;
    keyCount += 1;
    
    return index;
  }
  
  private void rehashTable() {
    if (keys == null)
    {



      int N = 1 << power;
      keys = new Object[N];
      values = new int[2 * N];
    }
    else {
      if (keyCount * 2 >= occupiedCount)
      {
        power += 1;
      }
      int N = 1 << power;
      Object[] oldKeys = keys;
      int[] oldValues = values;
      int oldN = oldKeys.length;
      keys = new Object[N];
      values = new int[2 * N];
      
      int remaining = keyCount;
      occupiedCount = (this.keyCount = 0);
      for (int i = 0; remaining != 0; i++) {
        Object key = oldKeys[i];
        if ((key != null) && (key != DELETED)) {
          int keyHash = oldValues[(oldN + i)];
          int index = insertNewKey(key, keyHash);
          values[index] = oldValues[i];
          remaining--;
        }
      }
    }
  }
  
  private int ensureIndex(Object key)
  {
    int hash = key.hashCode();
    int index = -1;
    int firstDeleted = -1;
    if (keys != null) {
      int fraction = hash * -1640531527;
      index = fraction >>> 32 - power;
      Object test = keys[index];
      if (test != null) {
        int N = 1 << power;
        if ((test == key) || ((values[(N + index)] == hash) && 
          (test.equals(key)))) {
          return index;
        }
        if (test == DELETED) {
          firstDeleted = index;
        }
        

        int mask = N - 1;
        int step = tableLookupStep(fraction, mask, power);
        int n = 0;
        



        for (;;)
        {
          index = index + step & mask;
          test = keys[index];
          if (test == null) {
            break;
          }
          if ((test == key) || ((values[(N + index)] == hash) && 
            (test.equals(key)))) {
            return index;
          }
          if ((test == DELETED) && (firstDeleted < 0)) {
            firstDeleted = index;
          }
        }
      }
    }
    


    if (firstDeleted >= 0) {
      index = firstDeleted;
    }
    else {
      if ((keys == null) || (occupiedCount * 4 >= (1 << power) * 3))
      {
        rehashTable();
        return insertNewKey(key, hash);
      }
      occupiedCount += 1;
    }
    keys[index] = key;
    values[((1 << power) + index)] = hash;
    keyCount += 1;
    return index;
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    
    int count = keyCount;
    for (int i = 0; count != 0; i++) {
      Object key = keys[i];
      if ((key != null) && (key != DELETED)) {
        count--;
        out.writeObject(key);
        out.writeInt(values[i]);
      }
    }
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    int writtenKeyCount = keyCount;
    if (writtenKeyCount != 0) {
      keyCount = 0;
      int N = 1 << power;
      keys = new Object[N];
      values = new int[2 * N];
      for (int i = 0; i != writtenKeyCount; i++) {
        Object key = in.readObject();
        int hash = key.hashCode();
        int index = insertNewKey(key, hash);
        values[index] = in.readInt();
      }
    }
  }
  




  private static final Object DELETED = new Object();
  private transient Object[] keys;
  private transient int[] values;
  private int power;
  private int keyCount;
  private transient int occupiedCount;
  private static final boolean check = false;
}
