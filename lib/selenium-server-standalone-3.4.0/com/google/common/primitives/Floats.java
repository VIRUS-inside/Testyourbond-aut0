package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;












































@GwtCompatible(emulated=true)
public final class Floats
{
  public static final int BYTES = 4;
  
  private Floats() {}
  
  public static int hashCode(float value)
  {
    return Float.valueOf(value).hashCode();
  }
  












  public static int compare(float a, float b)
  {
    return Float.compare(a, b);
  }
  







  public static boolean isFinite(float value)
  {
    return (Float.NEGATIVE_INFINITY < value ? 1 : 0) & (value < Float.POSITIVE_INFINITY ? 1 : 0);
  }
  









  public static boolean contains(float[] array, float target)
  {
    for (float value : array) {
      if (value == target) {
        return true;
      }
    }
    return false;
  }
  








  public static int indexOf(float[] array, float target)
  {
    return indexOf(array, target, 0, array.length);
  }
  
  private static int indexOf(float[] array, float target, int start, int end)
  {
    for (int i = start; i < end; i++) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  












  public static int indexOf(float[] array, float[] target)
  {
    Preconditions.checkNotNull(array, "array");
    Preconditions.checkNotNull(target, "target");
    if (target.length == 0) {
      return 0;
    }
    
    label65:
    for (int i = 0; i < array.length - target.length + 1; i++) {
      for (int j = 0; j < target.length; j++) {
        if (array[(i + j)] != target[j]) {
          break label65;
        }
      }
      return i;
    }
    return -1;
  }
  








  public static int lastIndexOf(float[] array, float target)
  {
    return lastIndexOf(array, target, 0, array.length);
  }
  
  private static int lastIndexOf(float[] array, float target, int start, int end)
  {
    for (int i = end - 1; i >= start; i--) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  








  public static float min(float... array)
  {
    Preconditions.checkArgument(array.length > 0);
    float min = array[0];
    for (int i = 1; i < array.length; i++) {
      min = Math.min(min, array[i]);
    }
    return min;
  }
  








  public static float max(float... array)
  {
    Preconditions.checkArgument(array.length > 0);
    float max = array[0];
    for (int i = 1; i < array.length; i++) {
      max = Math.max(max, array[i]);
    }
    return max;
  }
  












  @Beta
  public static float constrainToRange(float value, float min, float max)
  {
    Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", Float.valueOf(min), Float.valueOf(max));
    return Math.min(Math.max(value, min), max);
  }
  







  public static float[] concat(float[]... arrays)
  {
    int length = 0;
    for (array : arrays) {
      length += array.length;
    }
    float[] result = new float[length];
    int pos = 0;
    float[][] arrayOfFloat2 = arrays;float[] array = arrayOfFloat2.length; for (float[] arrayOfFloat3 = 0; arrayOfFloat3 < array; arrayOfFloat3++) { float[] array = arrayOfFloat2[arrayOfFloat3];
      System.arraycopy(array, 0, result, pos, array.length);
      pos += array.length;
    }
    return result;
  }
  
  private static final class FloatConverter extends Converter<String, Float> implements Serializable
  {
    static final FloatConverter INSTANCE = new FloatConverter();
    
    private FloatConverter() {}
    
    protected Float doForward(String value) { return Float.valueOf(value); }
    

    protected String doBackward(Float value)
    {
      return value.toString();
    }
    
    public String toString()
    {
      return "Floats.stringConverter()";
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    



    private static final long serialVersionUID = 1L;
  }
  


  @Beta
  public static Converter<String, Float> stringConverter()
  {
    return FloatConverter.INSTANCE;
  }
  












  public static float[] ensureCapacity(float[] array, int minLength, int padding)
  {
    Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
    Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
    return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
  }
  












  public static String join(String separator, float... array)
  {
    Preconditions.checkNotNull(separator);
    if (array.length == 0) {
      return "";
    }
    

    StringBuilder builder = new StringBuilder(array.length * 12);
    builder.append(array[0]);
    for (int i = 1; i < array.length; i++) {
      builder.append(separator).append(array[i]);
    }
    return builder.toString();
  }
  












  public static Comparator<float[]> lexicographicalComparator()
  {
    return LexicographicalComparator.INSTANCE;
  }
  
  private static enum LexicographicalComparator implements Comparator<float[]> {
    INSTANCE;
    
    private LexicographicalComparator() {}
    
    public int compare(float[] left, float[] right) { int minLength = Math.min(left.length, right.length);
      for (int i = 0; i < minLength; i++) {
        int result = Float.compare(left[i], right[i]);
        if (result != 0) {
          return result;
        }
      }
      return left.length - right.length;
    }
    
    public String toString()
    {
      return "Floats.lexicographicalComparator()";
    }
  }
  












  public static float[] toArray(Collection<? extends Number> collection)
  {
    if ((collection instanceof FloatArrayAsList)) {
      return ((FloatArrayAsList)collection).toFloatArray();
    }
    
    Object[] boxedArray = collection.toArray();
    int len = boxedArray.length;
    float[] array = new float[len];
    for (int i = 0; i < len; i++)
    {
      array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).floatValue();
    }
    return array;
  }
  














  public static List<Float> asList(float... backingArray)
  {
    if (backingArray.length == 0) {
      return Collections.emptyList();
    }
    return new FloatArrayAsList(backingArray);
  }
  
  @GwtCompatible
  private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
    final float[] array;
    final int start;
    final int end;
    private static final long serialVersionUID = 0L;
    
    FloatArrayAsList(float[] array) {
      this(array, 0, array.length);
    }
    
    FloatArrayAsList(float[] array, int start, int end) {
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
    
    public Float get(int index)
    {
      Preconditions.checkElementIndex(index, size());
      return Float.valueOf(array[(start + index)]);
    }
    

    public boolean contains(Object target)
    {
      return ((target instanceof Float)) && (Floats.indexOf(array, ((Float)target).floatValue(), start, end) != -1);
    }
    

    public int indexOf(Object target)
    {
      if ((target instanceof Float)) {
        int i = Floats.indexOf(array, ((Float)target).floatValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    

    public int lastIndexOf(Object target)
    {
      if ((target instanceof Float)) {
        int i = Floats.lastIndexOf(array, ((Float)target).floatValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    
    public Float set(int index, Float element)
    {
      Preconditions.checkElementIndex(index, size());
      float oldValue = array[(start + index)];
      
      array[(start + index)] = ((Float)Preconditions.checkNotNull(element)).floatValue();
      return Float.valueOf(oldValue);
    }
    
    public List<Float> subList(int fromIndex, int toIndex)
    {
      int size = size();
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
      if (fromIndex == toIndex) {
        return Collections.emptyList();
      }
      return new FloatArrayAsList(array, start + fromIndex, start + toIndex);
    }
    
    public boolean equals(@Nullable Object object)
    {
      if (object == this) {
        return true;
      }
      if ((object instanceof FloatArrayAsList)) {
        FloatArrayAsList that = (FloatArrayAsList)object;
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
        result = 31 * result + Floats.hashCode(array[i]);
      }
      return result;
    }
    
    public String toString()
    {
      StringBuilder builder = new StringBuilder(size() * 12);
      builder.append('[').append(array[start]);
      for (int i = start + 1; i < end; i++) {
        builder.append(", ").append(array[i]);
      }
      return ']';
    }
    
    float[] toFloatArray() {
      return Arrays.copyOfRange(array, start, end);
    }
  }
  

















  @Nullable
  @CheckForNull
  @Beta
  @GwtIncompatible
  public static Float tryParse(String string)
  {
    if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches())
    {
      try
      {
        return Float.valueOf(Float.parseFloat(string));
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    

    return null;
  }
}
