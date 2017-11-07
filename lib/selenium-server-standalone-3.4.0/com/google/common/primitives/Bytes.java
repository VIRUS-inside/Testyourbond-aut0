package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.Nullable;








































@GwtCompatible
public final class Bytes
{
  private Bytes() {}
  
  public static int hashCode(byte value)
  {
    return value;
  }
  







  public static boolean contains(byte[] array, byte target)
  {
    for (byte value : array) {
      if (value == target) {
        return true;
      }
    }
    return false;
  }
  







  public static int indexOf(byte[] array, byte target)
  {
    return indexOf(array, target, 0, array.length);
  }
  
  private static int indexOf(byte[] array, byte target, int start, int end)
  {
    for (int i = start; i < end; i++) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  










  public static int indexOf(byte[] array, byte[] target)
  {
    Preconditions.checkNotNull(array, "array");
    Preconditions.checkNotNull(target, "target");
    if (target.length == 0) {
      return 0;
    }
    
    label64:
    for (int i = 0; i < array.length - target.length + 1; i++) {
      for (int j = 0; j < target.length; j++) {
        if (array[(i + j)] != target[j]) {
          break label64;
        }
      }
      return i;
    }
    return -1;
  }
  







  public static int lastIndexOf(byte[] array, byte target)
  {
    return lastIndexOf(array, target, 0, array.length);
  }
  
  private static int lastIndexOf(byte[] array, byte target, int start, int end)
  {
    for (int i = end - 1; i >= start; i--) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  







  public static byte[] concat(byte[]... arrays)
  {
    int length = 0;
    for (array : arrays) {
      length += array.length;
    }
    byte[] result = new byte[length];
    int pos = 0;
    byte[][] arrayOfByte2 = arrays;byte[] array = arrayOfByte2.length; for (byte[] arrayOfByte3 = 0; arrayOfByte3 < array; arrayOfByte3++) { byte[] array = arrayOfByte2[arrayOfByte3];
      System.arraycopy(array, 0, result, pos, array.length);
      pos += array.length;
    }
    return result;
  }
  












  public static byte[] ensureCapacity(byte[] array, int minLength, int padding)
  {
    Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
    Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
    return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
  }
  












  public static byte[] toArray(Collection<? extends Number> collection)
  {
    if ((collection instanceof ByteArrayAsList)) {
      return ((ByteArrayAsList)collection).toByteArray();
    }
    
    Object[] boxedArray = collection.toArray();
    int len = boxedArray.length;
    byte[] array = new byte[len];
    for (int i = 0; i < len; i++)
    {
      array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).byteValue();
    }
    return array;
  }
  











  public static List<Byte> asList(byte... backingArray)
  {
    if (backingArray.length == 0) {
      return Collections.emptyList();
    }
    return new ByteArrayAsList(backingArray);
  }
  
  @GwtCompatible
  private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
    final byte[] array;
    final int start;
    final int end;
    private static final long serialVersionUID = 0L;
    
    ByteArrayAsList(byte[] array) {
      this(array, 0, array.length);
    }
    
    ByteArrayAsList(byte[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
    }
    
    public int size()
    {
      return end - start;
    }
    
    public boolean isEmpty()
    {
      return false;
    }
    
    public Byte get(int index)
    {
      Preconditions.checkElementIndex(index, size());
      return Byte.valueOf(array[(start + index)]);
    }
    

    public boolean contains(Object target)
    {
      return ((target instanceof Byte)) && (Bytes.indexOf(array, ((Byte)target).byteValue(), start, end) != -1);
    }
    

    public int indexOf(Object target)
    {
      if ((target instanceof Byte)) {
        int i = Bytes.indexOf(array, ((Byte)target).byteValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    

    public int lastIndexOf(Object target)
    {
      if ((target instanceof Byte)) {
        int i = Bytes.lastIndexOf(array, ((Byte)target).byteValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    
    public Byte set(int index, Byte element)
    {
      Preconditions.checkElementIndex(index, size());
      byte oldValue = array[(start + index)];
      
      array[(start + index)] = ((Byte)Preconditions.checkNotNull(element)).byteValue();
      return Byte.valueOf(oldValue);
    }
    
    public List<Byte> subList(int fromIndex, int toIndex)
    {
      int size = size();
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
      if (fromIndex == toIndex) {
        return Collections.emptyList();
      }
      return new ByteArrayAsList(array, start + fromIndex, start + toIndex);
    }
    
    public boolean equals(@Nullable Object object)
    {
      if (object == this) {
        return true;
      }
      if ((object instanceof ByteArrayAsList)) {
        ByteArrayAsList that = (ByteArrayAsList)object;
        int size = size();
        if (that.size() != size) {
          return false;
        }
        for (int i = 0; i < size; i++) {
          if (array[(start + i)] != array[(start + i)]) {
            return false;
          }
        }
        return true;
      }
      return super.equals(object);
    }
    
    public int hashCode()
    {
      int result = 1;
      for (int i = start; i < end; i++) {
        result = 31 * result + Bytes.hashCode(array[i]);
      }
      return result;
    }
    
    public String toString()
    {
      StringBuilder builder = new StringBuilder(size() * 5);
      builder.append('[').append(array[start]);
      for (int i = start + 1; i < end; i++) {
        builder.append(", ").append(array[i]);
      }
      return ']';
    }
    
    byte[] toByteArray() {
      return Arrays.copyOfRange(array, start, end);
    }
  }
}
