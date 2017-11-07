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
public final class Doubles
{
  public static final int BYTES = 8;
  
  private Doubles() {}
  
  public static int hashCode(double value)
  {
    return Double.valueOf(value).hashCode();
  }
  
















  public static int compare(double a, double b)
  {
    return Double.compare(a, b);
  }
  







  public static boolean isFinite(double value)
  {
    return (Double.NEGATIVE_INFINITY < value ? 1 : 0) & (value < Double.POSITIVE_INFINITY ? 1 : 0);
  }
  








  public static boolean contains(double[] array, double target)
  {
    for (double value : array) {
      if (value == target) {
        return true;
      }
    }
    return false;
  }
  








  public static int indexOf(double[] array, double target)
  {
    return indexOf(array, target, 0, array.length);
  }
  
  private static int indexOf(double[] array, double target, int start, int end)
  {
    for (int i = start; i < end; i++) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  












  public static int indexOf(double[] array, double[] target)
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
  








  public static int lastIndexOf(double[] array, double target)
  {
    return lastIndexOf(array, target, 0, array.length);
  }
  
  private static int lastIndexOf(double[] array, double target, int start, int end)
  {
    for (int i = end - 1; i >= start; i--) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }
  








  public static double min(double... array)
  {
    Preconditions.checkArgument(array.length > 0);
    double min = array[0];
    for (int i = 1; i < array.length; i++) {
      min = Math.min(min, array[i]);
    }
    return min;
  }
  








  public static double max(double... array)
  {
    Preconditions.checkArgument(array.length > 0);
    double max = array[0];
    for (int i = 1; i < array.length; i++) {
      max = Math.max(max, array[i]);
    }
    return max;
  }
  












  @Beta
  public static double constrainToRange(double value, double min, double max)
  {
    Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", Double.valueOf(min), Double.valueOf(max));
    return Math.min(Math.max(value, min), max);
  }
  







  public static double[] concat(double[]... arrays)
  {
    int length = 0;
    for (array : arrays) {
      length += array.length;
    }
    double[] result = new double[length];
    int pos = 0;
    double[][] arrayOfDouble2 = arrays;double[] array = arrayOfDouble2.length; for (double[] arrayOfDouble3 = 0; arrayOfDouble3 < array; arrayOfDouble3++) { double[] array = arrayOfDouble2[arrayOfDouble3];
      System.arraycopy(array, 0, result, pos, array.length);
      pos += array.length;
    }
    return result;
  }
  
  private static final class DoubleConverter extends Converter<String, Double> implements Serializable
  {
    static final DoubleConverter INSTANCE = new DoubleConverter();
    
    private DoubleConverter() {}
    
    protected Double doForward(String value) { return Double.valueOf(value); }
    

    protected String doBackward(Double value)
    {
      return value.toString();
    }
    
    public String toString()
    {
      return "Doubles.stringConverter()";
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    



    private static final long serialVersionUID = 1L;
  }
  


  @Beta
  public static Converter<String, Double> stringConverter()
  {
    return DoubleConverter.INSTANCE;
  }
  












  public static double[] ensureCapacity(double[] array, int minLength, int padding)
  {
    Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
    Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
    return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
  }
  











  public static String join(String separator, double... array)
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
  












  public static Comparator<double[]> lexicographicalComparator()
  {
    return LexicographicalComparator.INSTANCE;
  }
  
  private static enum LexicographicalComparator implements Comparator<double[]> {
    INSTANCE;
    
    private LexicographicalComparator() {}
    
    public int compare(double[] left, double[] right) { int minLength = Math.min(left.length, right.length);
      for (int i = 0; i < minLength; i++) {
        int result = Double.compare(left[i], right[i]);
        if (result != 0) {
          return result;
        }
      }
      return left.length - right.length;
    }
    
    public String toString()
    {
      return "Doubles.lexicographicalComparator()";
    }
  }
  












  public static double[] toArray(Collection<? extends Number> collection)
  {
    if ((collection instanceof DoubleArrayAsList)) {
      return ((DoubleArrayAsList)collection).toDoubleArray();
    }
    
    Object[] boxedArray = collection.toArray();
    int len = boxedArray.length;
    double[] array = new double[len];
    for (int i = 0; i < len; i++)
    {
      array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).doubleValue();
    }
    return array;
  }
  














  public static List<Double> asList(double... backingArray)
  {
    if (backingArray.length == 0) {
      return Collections.emptyList();
    }
    return new DoubleArrayAsList(backingArray);
  }
  
  @GwtCompatible
  private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
    final double[] array;
    final int start;
    final int end;
    private static final long serialVersionUID = 0L;
    
    DoubleArrayAsList(double[] array) {
      this(array, 0, array.length);
    }
    
    DoubleArrayAsList(double[] array, int start, int end) {
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
    
    public Double get(int index)
    {
      Preconditions.checkElementIndex(index, size());
      return Double.valueOf(array[(start + index)]);
    }
    

    public boolean contains(Object target)
    {
      return ((target instanceof Double)) && 
        (Doubles.indexOf(array, ((Double)target).doubleValue(), start, end) != -1);
    }
    

    public int indexOf(Object target)
    {
      if ((target instanceof Double)) {
        int i = Doubles.indexOf(array, ((Double)target).doubleValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    

    public int lastIndexOf(Object target)
    {
      if ((target instanceof Double)) {
        int i = Doubles.lastIndexOf(array, ((Double)target).doubleValue(), start, end);
        if (i >= 0) {
          return i - start;
        }
      }
      return -1;
    }
    
    public Double set(int index, Double element)
    {
      Preconditions.checkElementIndex(index, size());
      double oldValue = array[(start + index)];
      
      array[(start + index)] = ((Double)Preconditions.checkNotNull(element)).doubleValue();
      return Double.valueOf(oldValue);
    }
    
    public List<Double> subList(int fromIndex, int toIndex)
    {
      int size = size();
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
      if (fromIndex == toIndex) {
        return Collections.emptyList();
      }
      return new DoubleArrayAsList(array, start + fromIndex, start + toIndex);
    }
    
    public boolean equals(@Nullable Object object)
    {
      if (object == this) {
        return true;
      }
      if ((object instanceof DoubleArrayAsList)) {
        DoubleArrayAsList that = (DoubleArrayAsList)object;
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
        result = 31 * result + Doubles.hashCode(array[i]);
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
    
    double[] toDoubleArray() {
      return Arrays.copyOfRange(array, start, end);
    }
  }
  







  @GwtIncompatible
  static final Pattern FLOATING_POINT_PATTERN = ;
  
  @GwtIncompatible
  private static Pattern fpPattern() {
    String decimal = "(?:\\d++(?:\\.\\d*+)?|\\.\\d++)";
    String completeDec = decimal + "(?:[eE][+-]?\\d++)?[fFdD]?";
    String hex = "(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)";
    String completeHex = "0[xX]" + hex + "[pP][+-]?\\d++[fFdD]?";
    String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
    return Pattern.compile(fpPattern);
  }
  















  @Nullable
  @CheckForNull
  @Beta
  @GwtIncompatible
  public static Double tryParse(String string)
  {
    if (FLOATING_POINT_PATTERN.matcher(string).matches())
    {
      try
      {
        return Double.valueOf(Double.parseDouble(string));
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    

    return null;
  }
}
