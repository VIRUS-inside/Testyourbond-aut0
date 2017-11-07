package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
















































































































@Beta
@GwtIncompatible
public final class Quantiles
{
  public Quantiles() {}
  
  public static ScaleAndIndex median()
  {
    return scale(2).index(1);
  }
  


  public static Scale quartiles()
  {
    return scale(4);
  }
  


  public static Scale percentiles()
  {
    return scale(100);
  }
  





  public static Scale scale(int scale)
  {
    return new Scale(scale, null);
  }
  


  public static final class Scale
  {
    private final int scale;
    

    private Scale(int scale)
    {
      Preconditions.checkArgument(scale > 0, "Quantile scale must be positive");
      this.scale = scale;
    }
    




    public Quantiles.ScaleAndIndex index(int index)
    {
      return new Quantiles.ScaleAndIndex(scale, index, null);
    }
    







    public Quantiles.ScaleAndIndexes indexes(int... indexes)
    {
      return new Quantiles.ScaleAndIndexes(scale, (int[])indexes.clone(), null);
    }
    







    public Quantiles.ScaleAndIndexes indexes(Collection<Integer> indexes)
    {
      return new Quantiles.ScaleAndIndexes(scale, Ints.toArray(indexes), null);
    }
  }
  

  public static final class ScaleAndIndex
  {
    private final int scale;
    
    private final int index;
    

    private ScaleAndIndex(int scale, int index)
    {
      Quantiles.checkIndex(index, scale);
      this.scale = scale;
      this.index = index;
    }
    







    public double compute(Collection<? extends Number> dataset)
    {
      return computeInPlace(Doubles.toArray(dataset));
    }
    






    public double compute(double... dataset)
    {
      return computeInPlace((double[])dataset.clone());
    }
    







    public double compute(long... dataset)
    {
      return computeInPlace(Quantiles.longsToDoubles(dataset));
    }
    






    public double compute(int... dataset)
    {
      return computeInPlace(Quantiles.intsToDoubles(dataset));
    }
    






    public double computeInPlace(double... dataset)
    {
      Preconditions.checkArgument(dataset.length > 0, "Cannot calculate quantiles of an empty dataset");
      if (Quantiles.containsNaN(dataset)) {
        return NaN.0D;
      }
      







      long numerator = index * (dataset.length - 1);
      


      int quotient = (int)LongMath.divide(numerator, scale, RoundingMode.DOWN);
      int remainder = (int)(numerator - quotient * scale);
      Quantiles.selectInPlace(quotient, dataset, 0, dataset.length - 1);
      if (remainder == 0) {
        return dataset[quotient];
      }
      Quantiles.selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
      return Quantiles.interpolate(dataset[quotient], dataset[(quotient + 1)], remainder, scale);
    }
  }
  


  public static final class ScaleAndIndexes
  {
    private final int scale;
    
    private final int[] indexes;
    

    private ScaleAndIndexes(int scale, int[] indexes)
    {
      for (int index : indexes) {
        Quantiles.checkIndex(index, scale);
      }
      this.scale = scale;
      this.indexes = indexes;
    }
    








    public Map<Integer, Double> compute(Collection<? extends Number> dataset)
    {
      return computeInPlace(Doubles.toArray(dataset));
    }
    







    public Map<Integer, Double> compute(double... dataset)
    {
      return computeInPlace((double[])dataset.clone());
    }
    








    public Map<Integer, Double> compute(long... dataset)
    {
      return computeInPlace(Quantiles.longsToDoubles(dataset));
    }
    







    public Map<Integer, Double> compute(int... dataset)
    {
      return computeInPlace(Quantiles.intsToDoubles(dataset));
    }
    







    public Map<Integer, Double> computeInPlace(double... dataset)
    {
      Preconditions.checkArgument(dataset.length > 0, "Cannot calculate quantiles of an empty dataset");
      if (Quantiles.containsNaN(dataset)) {
        Map<Integer, Double> nanMap = new HashMap();
        for (int index : indexes) {
          nanMap.put(Integer.valueOf(index), Double.valueOf(NaN.0D));
        }
        return Collections.unmodifiableMap(nanMap);
      }
      





      int[] quotients = new int[indexes.length];
      int[] remainders = new int[indexes.length];
      
      int[] requiredSelections = new int[indexes.length * 2];
      int requiredSelectionsCount = 0;
      for (int i = 0; i < indexes.length; i++)
      {

        long numerator = indexes[i] * (dataset.length - 1);
        



        int quotient = (int)LongMath.divide(numerator, scale, RoundingMode.DOWN);
        int remainder = (int)(numerator - quotient * scale);
        quotients[i] = quotient;
        remainders[i] = remainder;
        requiredSelections[requiredSelectionsCount] = quotient;
        requiredSelectionsCount++;
        if (remainder != 0) {
          requiredSelections[requiredSelectionsCount] = (quotient + 1);
          requiredSelectionsCount++;
        }
      }
      Arrays.sort(requiredSelections, 0, requiredSelectionsCount);
      Quantiles.selectAllInPlace(requiredSelections, 0, requiredSelectionsCount - 1, dataset, 0, dataset.length - 1);
      
      Map<Integer, Double> ret = new HashMap();
      for (int i = 0; i < indexes.length; i++) {
        int quotient = quotients[i];
        int remainder = remainders[i];
        if (remainder == 0) {
          ret.put(Integer.valueOf(indexes[i]), Double.valueOf(dataset[quotient]));
        } else {
          ret.put(
            Integer.valueOf(indexes[i]), Double.valueOf(Quantiles.interpolate(dataset[quotient], dataset[(quotient + 1)], remainder, scale)));
        }
      }
      return Collections.unmodifiableMap(ret);
    }
  }
  


  private static boolean containsNaN(double... dataset)
  {
    for (double value : dataset) {
      if (Double.isNaN(value)) {
        return true;
      }
    }
    return false;
  }
  




  private static double interpolate(double lower, double upper, double remainder, double scale)
  {
    if (lower == Double.NEGATIVE_INFINITY) {
      if (upper == Double.POSITIVE_INFINITY)
      {
        return NaN.0D;
      }
      
      return Double.NEGATIVE_INFINITY;
    }
    if (upper == Double.POSITIVE_INFINITY)
    {
      return Double.POSITIVE_INFINITY;
    }
    return lower + (upper - lower) * remainder / scale;
  }
  
  private static void checkIndex(int index, int scale) {
    if ((index < 0) || (index > scale)) {
      throw new IllegalArgumentException("Quantile indexes must be between 0 and the scale, which is " + scale);
    }
  }
  
  private static double[] longsToDoubles(long[] longs)
  {
    int len = longs.length;
    double[] doubles = new double[len];
    for (int i = 0; i < len; i++) {
      doubles[i] = longs[i];
    }
    return doubles;
  }
  
  private static double[] intsToDoubles(int[] ints) {
    int len = ints.length;
    double[] doubles = new double[len];
    for (int i = 0; i < len; i++) {
      doubles[i] = ints[i];
    }
    return doubles;
  }
  



















  private static void selectInPlace(int required, double[] array, int from, int to)
  {
    if (required == from) {
      int min = from;
      for (int index = from + 1; index <= to; index++) {
        if (array[min] > array[index]) {
          min = index;
        }
      }
      if (min != from) {
        swap(array, min, from);
      }
      return;
    }
    


    while (to > from) {
      int partitionPoint = partition(array, from, to);
      if (partitionPoint >= required) {
        to = partitionPoint - 1;
      }
      if (partitionPoint <= required) {
        from = partitionPoint + 1;
      }
    }
  }
  








  private static int partition(double[] array, int from, int to)
  {
    movePivotToStartOfSlice(array, from, to);
    double pivot = array[from];
    


    int partitionPoint = to;
    for (int i = to; i > from; i--) {
      if (array[i] > pivot) {
        swap(array, partitionPoint, i);
        partitionPoint--;
      }
    }
    



    swap(array, from, partitionPoint);
    return partitionPoint;
  }
  





  private static void movePivotToStartOfSlice(double[] array, int from, int to)
  {
    int mid = from + to >>> 1;
    



    boolean toLessThanMid = array[to] < array[mid];
    boolean midLessThanFrom = array[mid] < array[from];
    boolean toLessThanFrom = array[to] < array[from];
    if (toLessThanMid == midLessThanFrom)
    {
      swap(array, mid, from);
    } else if (toLessThanMid != toLessThanFrom)
    {
      swap(array, from, to);
    }
  }
  








  private static void selectAllInPlace(int[] allRequired, int requiredFrom, int requiredTo, double[] array, int from, int to)
  {
    int requiredChosen = chooseNextSelection(allRequired, requiredFrom, requiredTo, from, to);
    int required = allRequired[requiredChosen];
    

    selectInPlace(required, array, from, to);
    

    int requiredBelow = requiredChosen - 1;
    while ((requiredBelow >= requiredFrom) && (allRequired[requiredBelow] == required)) {
      requiredBelow--;
    }
    if (requiredBelow >= requiredFrom) {
      selectAllInPlace(allRequired, requiredFrom, requiredBelow, array, from, required - 1);
    }
    

    int requiredAbove = requiredChosen + 1;
    while ((requiredAbove <= requiredTo) && (allRequired[requiredAbove] == required)) {
      requiredAbove++;
    }
    if (requiredAbove <= requiredTo) {
      selectAllInPlace(allRequired, requiredAbove, requiredTo, array, required + 1, to);
    }
  }
  









  private static int chooseNextSelection(int[] allRequired, int requiredFrom, int requiredTo, int from, int to)
  {
    if (requiredFrom == requiredTo) {
      return requiredFrom;
    }
    


    int centerFloor = from + to >>> 1;
    





    int low = requiredFrom;
    int high = requiredTo;
    while (high > low + 1) {
      int mid = low + high >>> 1;
      if (allRequired[mid] > centerFloor) {
        high = mid;
      } else if (allRequired[mid] < centerFloor) {
        low = mid;
      } else {
        return mid;
      }
    }
    

    if (from + to - allRequired[low] - allRequired[high] > 0) {
      return high;
    }
    return low;
  }
  



  private static void swap(double[] array, int i, int j)
  {
    double temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
}
