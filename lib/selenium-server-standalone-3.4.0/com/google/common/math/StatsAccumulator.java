package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.util.Iterator;





























@Beta
@GwtIncompatible
public final class StatsAccumulator
{
  private long count = 0L;
  private double mean = 0.0D;
  private double sumOfSquaresOfDeltas = 0.0D;
  private double min = NaN.0D;
  private double max = NaN.0D;
  
  public StatsAccumulator() {}
  
  public void add(double value)
  {
    if (count == 0L) {
      count = 1L;
      mean = value;
      min = value;
      max = value;
      if (!Doubles.isFinite(value)) {
        sumOfSquaresOfDeltas = NaN.0D;
      }
    } else {
      count += 1L;
      if ((Doubles.isFinite(value)) && (Doubles.isFinite(mean)))
      {
        double delta = value - mean;
        mean += delta / count;
        sumOfSquaresOfDeltas += delta * (value - mean);
      } else {
        mean = calculateNewMeanNonFinite(mean, value);
        sumOfSquaresOfDeltas = NaN.0D;
      }
      min = Math.min(min, value);
      max = Math.max(max, value);
    }
  }
  





  public void addAll(Iterable<? extends Number> values)
  {
    for (Number value : values) {
      add(value.doubleValue());
    }
  }
  





  public void addAll(Iterator<? extends Number> values)
  {
    while (values.hasNext()) {
      add(((Number)values.next()).doubleValue());
    }
  }
  




  public void addAll(double... values)
  {
    for (double value : values) {
      add(value);
    }
  }
  




  public void addAll(int... values)
  {
    for (int value : values) {
      add(value);
    }
  }
  





  public void addAll(long... values)
  {
    for (long value : values) {
      add(value);
    }
  }
  



  public void addAll(Stats values)
  {
    if (values.count() == 0L) {
      return;
    }
    
    if (count == 0L) {
      count = values.count();
      mean = values.mean();
      sumOfSquaresOfDeltas = values.sumOfSquaresOfDeltas();
      min = values.min();
      max = values.max();
    } else {
      count += values.count();
      if ((Doubles.isFinite(mean)) && (Doubles.isFinite(values.mean())))
      {
        double delta = values.mean() - mean;
        mean += delta * values.count() / count;
        
        sumOfSquaresOfDeltas = (sumOfSquaresOfDeltas + (values.sumOfSquaresOfDeltas() + delta * (values.mean() - mean) * values.count()));
      } else {
        mean = calculateNewMeanNonFinite(mean, values.mean());
        sumOfSquaresOfDeltas = NaN.0D;
      }
      min = Math.min(min, values.min());
      max = Math.max(max, values.max());
    }
  }
  


  public Stats snapshot()
  {
    return new Stats(count, mean, sumOfSquaresOfDeltas, min, max);
  }
  


  public long count()
  {
    return count;
  }
  

















  public double mean()
  {
    Preconditions.checkState(count != 0L);
    return mean;
  }
  











  public final double sum()
  {
    return mean * count;
  }
  














  public final double populationVariance()
  {
    Preconditions.checkState(count != 0L);
    if (Double.isNaN(sumOfSquaresOfDeltas)) {
      return NaN.0D;
    }
    if (count == 1L) {
      return 0.0D;
    }
    return DoubleUtils.ensureNonNegative(sumOfSquaresOfDeltas) / count;
  }
  















  public final double populationStandardDeviation()
  {
    return Math.sqrt(populationVariance());
  }
  















  public final double sampleVariance()
  {
    Preconditions.checkState(count > 1L);
    if (Double.isNaN(sumOfSquaresOfDeltas)) {
      return NaN.0D;
    }
    return DoubleUtils.ensureNonNegative(sumOfSquaresOfDeltas) / (count - 1L);
  }
  

















  public final double sampleStandardDeviation()
  {
    return Math.sqrt(sampleVariance());
  }
  












  public double min()
  {
    Preconditions.checkState(count != 0L);
    return min;
  }
  












  public double max()
  {
    Preconditions.checkState(count != 0L);
    return max;
  }
  
  double sumOfSquaresOfDeltas() {
    return sumOfSquaresOfDeltas;
  }
  

















  static double calculateNewMeanNonFinite(double previousMean, double value)
  {
    if (Doubles.isFinite(previousMean))
    {
      return value; }
    if ((Doubles.isFinite(value)) || (previousMean == value))
    {
      return previousMean;
    }
    
    return NaN.0D;
  }
}
