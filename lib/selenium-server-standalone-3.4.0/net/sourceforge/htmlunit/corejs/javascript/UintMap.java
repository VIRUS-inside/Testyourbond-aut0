package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;







public class UintMap
  implements Serializable
{
  static final long serialVersionUID = 4242698212885848444L;
  private static final int A = -1640531527;
  private static final int EMPTY = -1;
  private static final int DELETED = -2;
  private transient int[] keys;
  private transient Object[] values;
  private int power;
  private int keyCount;
  private transient int occupiedCount;
  private transient int ivaluesShift;
  private static final boolean check = false;
  
  public UintMap()
  {
    this(4);
  }
  
  public UintMap(int initialCapacity) {
    if (initialCapacity < 0) {
      Kit.codeBug();
    }
    int minimalCapacity = initialCapacity * 4 / 3;
    
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
  
  public boolean has(int key) {
    if (key < 0)
      Kit.codeBug();
    return 0 <= findIndex(key);
  }
  




  public Object getObject(int key)
  {
    if (key < 0)
      Kit.codeBug();
    if (values != null) {
      int index = findIndex(key);
      if (0 <= index) {
        return values[index];
      }
    }
    return null;
  }
  




  public int getInt(int key, int defaultValue)
  {
    if (key < 0)
      Kit.codeBug();
    int index = findIndex(key);
    if (0 <= index) {
      if (ivaluesShift != 0) {
        return keys[(ivaluesShift + index)];
      }
      return 0;
    }
    return defaultValue;
  }
  







  public int getExistingInt(int key)
  {
    if (key < 0)
      Kit.codeBug();
    int index = findIndex(key);
    if (0 <= index) {
      if (ivaluesShift != 0) {
        return keys[(ivaluesShift + index)];
      }
      return 0;
    }
    
    Kit.codeBug();
    return 0;
  }
  



  public void put(int key, Object value)
  {
    if (key < 0)
      Kit.codeBug();
    int index = ensureIndex(key, false);
    if (values == null) {
      values = new Object[1 << power];
    }
    values[index] = value;
  }
  



  public void put(int key, int value)
  {
    if (key < 0)
      Kit.codeBug();
    int index = ensureIndex(key, true);
    if (ivaluesShift == 0) {
      int N = 1 << power;
      
      if (keys.length != N * 2) {
        int[] tmp = new int[N * 2];
        System.arraycopy(keys, 0, tmp, 0, N);
        keys = tmp;
      }
      ivaluesShift = N;
    }
    keys[(ivaluesShift + index)] = value;
  }
  
  public void remove(int key) {
    if (key < 0)
      Kit.codeBug();
    int index = findIndex(key);
    if (0 <= index) {
      keys[index] = -2;
      keyCount -= 1;
      

      if (values != null) {
        values[index] = null;
      }
      if (ivaluesShift != 0) {
        keys[(ivaluesShift + index)] = 0;
      }
    }
  }
  
  public void clear() {
    int N = 1 << power;
    if (keys != null) {
      for (int i = 0; i != N; i++) {
        keys[i] = -1;
      }
      if (values != null) {
        for (int i = 0; i != N; i++) {
          values[i] = null;
        }
      }
    }
    ivaluesShift = 0;
    keyCount = 0;
    occupiedCount = 0;
  }
  
  public int[] getKeys()
  {
    int[] keys = this.keys;
    int n = keyCount;
    int[] result = new int[n];
    for (int i = 0; n != 0; i++) {
      int entry = keys[i];
      if ((entry != -1) && (entry != -2)) {
        result[(--n)] = entry;
      }
    }
    return result;
  }
  
  private static int tableLookupStep(int fraction, int mask, int power) {
    int shift = 32 - 2 * power;
    if (shift >= 0) {
      return fraction >>> shift & mask | 0x1;
    }
    return fraction & mask >>> -shift | 0x1;
  }
  
  private int findIndex(int key)
  {
    int[] keys = this.keys;
    if (keys != null) {
      int fraction = key * -1640531527;
      int index = fraction >>> 32 - power;
      int entry = keys[index];
      if (entry == key) {
        return index;
      }
      if (entry != -1)
      {
        int mask = (1 << power) - 1;
        int step = tableLookupStep(fraction, mask, power);
        int n = 0;
        



        do
        {
          index = index + step & mask;
          entry = keys[index];
          if (entry == key) {
            return index;
          }
        } while (entry != -1);
      }
    }
    return -1;
  }
  





  private int insertNewKey(int key)
  {
    int[] keys = this.keys;
    int fraction = key * -1640531527;
    int index = fraction >>> 32 - power;
    if (keys[index] != -1) {
      int mask = (1 << power) - 1;
      int step = tableLookupStep(fraction, mask, power);
      int firstIndex = index;
      
      do
      {
        index = index + step & mask;

      }
      while (keys[index] != -1);
    }
    keys[index] = key;
    occupiedCount += 1;
    keyCount += 1;
    return index;
  }
  
  private void rehashTable(boolean ensureIntSpace) {
    if (keys != null)
    {
      if (keyCount * 2 >= occupiedCount)
      {
        power += 1;
      }
    }
    int N = 1 << power;
    int[] old = keys;
    int oldShift = ivaluesShift;
    if ((oldShift == 0) && (!ensureIntSpace)) {
      keys = new int[N];
    } else {
      ivaluesShift = N;
      keys = new int[N * 2];
    }
    for (int i = 0; i != N; i++) {
      keys[i] = -1;
    }
    
    Object[] oldValues = values;
    if (oldValues != null) {
      values = new Object[N];
    }
    
    int oldCount = keyCount;
    occupiedCount = 0;
    if (oldCount != 0) {
      keyCount = 0;
      int i = 0; for (int remaining = oldCount; remaining != 0; i++) {
        int key = old[i];
        if ((key != -1) && (key != -2)) {
          int index = insertNewKey(key);
          if (oldValues != null) {
            values[index] = oldValues[i];
          }
          if (oldShift != 0) {
            keys[(ivaluesShift + index)] = old[(oldShift + i)];
          }
          remaining--;
        }
      }
    }
  }
  
  private int ensureIndex(int key, boolean intType)
  {
    int index = -1;
    int firstDeleted = -1;
    int[] keys = this.keys;
    if (keys != null) {
      int fraction = key * -1640531527;
      index = fraction >>> 32 - power;
      int entry = keys[index];
      if (entry == key) {
        return index;
      }
      if (entry != -1) {
        if (entry == -2) {
          firstDeleted = index;
        }
        
        int mask = (1 << power) - 1;
        int step = tableLookupStep(fraction, mask, power);
        int n = 0;
        



        do
        {
          index = index + step & mask;
          entry = keys[index];
          if (entry == key) {
            return index;
          }
          if ((entry == -2) && (firstDeleted < 0)) {
            firstDeleted = index;
          }
        } while (entry != -1);
      }
    }
    


    if (firstDeleted >= 0) {
      index = firstDeleted;
    }
    else {
      if ((keys == null) || (occupiedCount * 4 >= (1 << power) * 3))
      {
        rehashTable(intType);
        return insertNewKey(key);
      }
      occupiedCount += 1;
    }
    keys[index] = key;
    keyCount += 1;
    return index;
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    
    int count = keyCount;
    if (count != 0) {
      boolean hasIntValues = ivaluesShift != 0;
      boolean hasObjectValues = values != null;
      out.writeBoolean(hasIntValues);
      out.writeBoolean(hasObjectValues);
      
      for (int i = 0; count != 0; i++) {
        int key = keys[i];
        if ((key != -1) && (key != -2)) {
          count--;
          out.writeInt(key);
          if (hasIntValues) {
            out.writeInt(keys[(ivaluesShift + i)]);
          }
          if (hasObjectValues) {
            out.writeObject(values[i]);
          }
        }
      }
    }
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    int writtenKeyCount = keyCount;
    if (writtenKeyCount != 0) {
      keyCount = 0;
      boolean hasIntValues = in.readBoolean();
      boolean hasObjectValues = in.readBoolean();
      
      int N = 1 << power;
      if (hasIntValues) {
        keys = new int[2 * N];
        ivaluesShift = N;
      } else {
        keys = new int[N];
      }
      for (int i = 0; i != N; i++) {
        keys[i] = -1;
      }
      if (hasObjectValues) {
        values = new Object[N];
      }
      for (int i = 0; i != writtenKeyCount; i++) {
        int key = in.readInt();
        int index = insertNewKey(key);
        if (hasIntValues) {
          int ivalue = in.readInt();
          keys[(ivaluesShift + index)] = ivalue;
        }
        if (hasObjectValues) {
          values[index] = in.readObject();
        }
      }
    }
  }
}
