package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;















































@GwtCompatible
public final class Ints
{
  public static final int BYTES = 4;
  public static final int MAX_POWER_OF_TWO = 1073741824;
  
  private Ints() {}
  
  public static int hashCode(int value)
  {
    return value;
  }
  







  public static int checkedCast(long value)
  {
    int result = (int)value;
    Preconditions.checkArgument(result == value, "Out of range: %s", value);
    return result;
  }
  







  public static int saturatedCast(long value)
  {
    if (value > 2147483647L) {
      return Integer.MAX_VALUE;
    }
    if (value < -2147483648L) {
      return Integer.MIN_VALUE;
    }
    return (int)value;
  }
  











  public static int compare(int a, int b)
  {
    return a > b ? 1 : a < b ? -1 : 0;
  }
  







  public static boolean contains(int[] array, int target)
  {
    for (int value : array) {
      if (value == target) {
        return true;
      }
    }
    return false;
  }
  







  public static int indexOf(int[] array, int target)
  {
    return indexOf(array, target, 0, array.length);
  }
  
  private static int indexOf(int[] array, int target, int start, int end)
  {
    for (int i = start; i < end; i++) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  










  public static int indexOf(int[] array, int[] target)
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
  







  public static int lastIndexOf(int[] array, int target)
  {
    return lastIndexOf(array, target, 0, array.length);
  }
  
  private static int lastIndexOf(int[] array, int target, int start, int end)
  {
    for (int i = end - 1; i >= start; i--) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  







  public static int min(int... array)
  {
    Preconditions.checkArgument(array.length > 0);
    int min = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] < min) {
        min = array[i];
      }
    }
    return min;
  }
  







  public static int max(int... array)
  {
    Preconditions.checkArgument(array.length > 0);
    int max = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] > max) {
        max = array[i];
      }
    }
    return max;
  }
  












  @Beta
  public static int constrainToRange(int value, int min, int max)
  {
    Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", min, max);
    return Math.min(Math.max(value, min), max);
  }
  







  public static int[] concat(int[]... arrays)
  {
    int length = 0;
    for (array : arrays) {
      length += array.length;
    }
    int[] result = new int[length];
    int pos = 0;
    int[][] arrayOfInt2 = arrays;int[] array = arrayOfInt2.length; for (int[] arrayOfInt3 = 0; arrayOfInt3 < array; arrayOfInt3++) { int[] array = arrayOfInt2[arrayOfInt3];
      System.arraycopy(array, 0, result, pos, array.length);
      pos += array.length;
    }
    return result;
  }
  








  public static byte[] toByteArray(int value)
  {
    return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
  }
  












  public static int fromByteArray(byte[] bytes)
  {
    Preconditions.checkArgument(bytes.length >= 4, "array too small: %s < %s", bytes.length, 4);
    return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
  }
  





  public static int fromBytes(byte b1, byte b2, byte b3, byte b4)
  {
    return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
  }
  
  private static final class IntConverter extends Converter<String, Integer> implements Serializable
  {
    static final IntConverter INSTANCE = new IntConverter();
    
    private IntConverter() {}
    
    protected Integer doForward(String value) { return Integer.decode(value); }
    

    protected String doBackward(Integer value)
    {
      return value.toString();
    }
    
    public String toString()
    {
      return "Ints.stringConverter()";
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    





    private static final long serialVersionUID = 1L;
  }
  





  @Beta
  public static Converter<String, Integer> stringConverter()
  {
    return IntConverter.INSTANCE;
  }
  












  public static int[] ensureCapacity(int[] array, int minLength, int padding)
  {
    Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
    Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
    return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
  }
  







  public static String join(String separator, int... array)
  {
    Preconditions.checkNotNull(separator);
    if (array.length == 0) {
      return "";
    }
    

    StringBuilder builder = new StringBuilder(array.length * 5);
    builder.append(array[0]);
    for (int i = 1; i < array.length; i++) {
      builder.append(separator).append(array[i]);
    }
    return builder.toString();
  }
  











  public static Comparator<int[]> lexicographicalComparator()
  {
    return LexicographicalComparator.INSTANCE;
  }
  
  private static enum LexicographicalComparator implements Comparator<int[]> {
    INSTANCE;
    
    private LexicographicalComparator() {}
    
    public int compare(int[] left, int[] right) { int minLength = Math.min(left.length, right.length);
      for (int i = 0; i < minLength; i++) {
        int result = Ints.compare(left[i], right[i]);
        if (result != 0) {
          return result;
        }
      }
      return left.length - right.length;
    }
    
    public String toString()
    {
      return "Ints.lexicographicalComparator()";
    }
  }
  












  public static int[] toArray(Collection<? extends Number> collection)
  {
    if ((collection instanceof IntArrayAsList)) {
      return ((IntArrayAsList)collection).toIntArray();
    }
    
    Object[] boxedArray = collection.toArray();
    int len = boxedArray.length;
    int[] array = new int[len];
    for (int i = 0; i < len; i++)
    {
      array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).intValue();
    }
    return array;
  }
  











  public static List<Integer> asList(int... backingArray)
  {
    if (backingArray.length == 0) {
      return Collections.emptyList();
    }
    return new IntArrayAsList(backingArray);
  }
  
  @GwtCompatible
  private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
    final int[] array;
    final int start;
    final int end;
    private static final long serialVersionUID = 0L;
    
    IntArrayAsList(int[] array) {
      this(array, 0, array.length);
    }
    
    IntArrayAsList(int[] array, int start, int end) {
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
    
    public Integer get(int index)
    {
      Preconditions.checkElementIndex(index, size());
      return Integer.valueOf(array[(start + index)]);
    }
    

    public boolean contains(Object target)
    {
      return ((target instanceof Integer)) && (Ints.indexOf(array, ((Integer)target).intValue(), start, end) != -1);
    }
    

    public int indexOf(Object target)
    {
      if ((target instanceof Integer)) {
        int i = Ints.indexOf(array, ((Integer)target).intValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    

    public int lastIndexOf(Object target)
    {
      if ((target instanceof Integer)) {
        int i = Ints.lastIndexOf(array, ((Integer)target).intValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    
    public Integer set(int index, Integer element)
    {
      Preconditions.checkElementIndex(index, size());
      int oldValue = array[(start + index)];
      
      array[(start + index)] = ((Integer)Preconditions.checkNotNull(element)).intValue();
      return Integer.valueOf(oldValue);
    }
    
    public List<Integer> subList(int fromIndex, int toIndex)
    {
      int size = size();
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
      if (fromIndex == toIndex) {
        return Collections.emptyList();
      }
      return new IntArrayAsList(array, start + fromIndex, start + toIndex);
    }
    
    public boolean equals(@Nullable Object object)
    {
      if (object == this) {
        return true;
      }
      if ((object instanceof IntArrayAsList)) {
        IntArrayAsList that = (IntArrayAsList)object;
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
        result = 31 * result + Ints.hashCode(array[i]);
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
    
    int[] toIntArray() {
      return Arrays.copyOfRange(array, start, end);
    }
  }
  

















  @Nullable
  @CheckForNull
  @Beta
  public static Integer tryParse(String string)
  {
    return tryParse(string, 10);
  }
  


















  @Nullable
  @CheckForNull
  @Beta
  public static Integer tryParse(String string, int radix)
  {
    Long result = Longs.tryParse(string, radix);
    if ((result == null) || (result.longValue() != result.intValue())) {
      return null;
    }
    return Integer.valueOf(result.intValue());
  }
}
