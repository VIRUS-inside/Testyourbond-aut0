package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import javax.annotation.Nullable;























































@Beta
@GwtIncompatible
public final class Stats
  implements Serializable
{
  private final long count;
  private final double mean;
  private final double sumOfSquaresOfDeltas;
  private final double min;
  private final double max;
  static final int BYTES = 40;
  private static final long serialVersionUID = 0L;
  
  Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max)
  {
    this.count = count;
    this.mean = mean;
    this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
    this.min = min;
    this.max = max;
  }
  





  public static Stats of(Iterable<? extends Number> values)
  {
    StatsAccumulator accumulator = new StatsAccumulator();
    accumulator.addAll(values);
    return accumulator.snapshot();
  }
  





  public static Stats of(Iterator<? extends Number> values)
  {
    StatsAccumulator accumulator = new StatsAccumulator();
    accumulator.addAll(values);
    return accumulator.snapshot();
  }
  




  public static Stats of(double... values)
  {
    StatsAccumulator acummulator = new StatsAccumulator();
    acummulator.addAll(values);
    return acummulator.snapshot();
  }
  




  public static Stats of(int... values)
  {
    StatsAccumulator acummulator = new StatsAccumulator();
    acummulator.addAll(values);
    return acummulator.snapshot();
  }
  





  public static Stats of(long... values)
  {
    StatsAccumulator acummulator = new StatsAccumulator();
    acummulator.addAll(values);
    return acummulator.snapshot();
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
  











  public double sum()
  {
    return mean * count;
  }
  














  public double populationVariance()
  {
    Preconditions.checkState(count > 0L);
    if (Double.isNaN(sumOfSquaresOfDeltas)) {
      return NaN.0D;
    }
    if (count == 1L) {
      return 0.0D;
    }
    return DoubleUtils.ensureNonNegative(sumOfSquaresOfDeltas) / count();
  }
  















  public double populationStandardDeviation()
  {
    return Math.sqrt(populationVariance());
  }
  















  public double sampleVariance()
  {
    Preconditions.checkState(count > 1L);
    if (Double.isNaN(sumOfSquaresOfDeltas)) {
      return NaN.0D;
    }
    return DoubleUtils.ensureNonNegative(sumOfSquaresOfDeltas) / (count - 1L);
  }
  

















  public double sampleStandardDeviation()
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
  









  public boolean equals(@Nullable Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Stats other = (Stats)obj;
    return (count == count) && 
      (Double.doubleToLongBits(mean) == Double.doubleToLongBits(mean)) && 
      (Double.doubleToLongBits(sumOfSquaresOfDeltas) == Double.doubleToLongBits(sumOfSquaresOfDeltas)) && 
      (Double.doubleToLongBits(min) == Double.doubleToLongBits(min)) && 
      (Double.doubleToLongBits(max) == Double.doubleToLongBits(max));
  }
  






  public int hashCode()
  {
    return Objects.hashCode(new Object[] { Long.valueOf(count), Double.valueOf(mean), Double.valueOf(sumOfSquaresOfDeltas), Double.valueOf(min), Double.valueOf(max) });
  }
  
  public String toString()
  {
    if (count() > 0L) {
      return 
      




        MoreObjects.toStringHelper(this).add("count", count).add("mean", mean).add("populationStandardDeviation", populationStandardDeviation()).add("min", min).add("max", max).toString();
    }
    return MoreObjects.toStringHelper(this).add("count", count).toString();
  }
  
  double sumOfSquaresOfDeltas()
  {
    return sumOfSquaresOfDeltas;
  }
  









  public static double meanOf(Iterable<? extends Number> values)
  {
    return meanOf(values.iterator());
  }
  









  public static double meanOf(Iterator<? extends Number> values)
  {
    Preconditions.checkArgument(values.hasNext());
    long count = 1L;
    double mean = ((Number)values.next()).doubleValue();
    while (values.hasNext()) {
      double value = ((Number)values.next()).doubleValue();
      count += 1L;
      if ((Doubles.isFinite(value)) && (Doubles.isFinite(mean)))
      {
        mean += (value - mean) / count;
      } else {
        mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
      }
    }
    return mean;
  }
  








  public static double meanOf(double... values)
  {
    Preconditions.checkArgument(values.length > 0);
    double mean = values[0];
    for (int index = 1; index < values.length; index++) {
      double value = values[index];
      if ((Doubles.isFinite(value)) && (Doubles.isFinite(mean)))
      {
        mean += (value - mean) / (index + 1);
      } else {
        mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
      }
    }
    return mean;
  }
  








  public static double meanOf(int... values)
  {
    Preconditions.checkArgument(values.length > 0);
    double mean = values[0];
    for (int index = 1; index < values.length; index++) {
      double value = values[index];
      if ((Doubles.isFinite(value)) && (Doubles.isFinite(mean)))
      {
        mean += (value - mean) / (index + 1);
      } else {
        mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
      }
    }
    return mean;
  }
  









  public static double meanOf(long... values)
  {
    Preconditions.checkArgument(values.length > 0);
    double mean = values[0];
    for (int index = 1; index < values.length; index++) {
      double value = values[index];
      if ((Doubles.isFinite(value)) && (Doubles.isFinite(mean)))
      {
        mean += (value - mean) / (index + 1);
      } else {
        mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
      }
    }
    return mean;
  }
  












  public byte[] toByteArray()
  {
    ByteBuffer buff = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
    writeTo(buff);
    return buff.array();
  }
  









  void writeTo(ByteBuffer buffer)
  {
    Preconditions.checkNotNull(buffer);
    Preconditions.checkArgument(buffer
      .remaining() >= 40, "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
      

      .remaining());
    buffer
      .putLong(count)
      .putDouble(mean)
      .putDouble(sumOfSquaresOfDeltas)
      .putDouble(min)
      .putDouble(max);
  }
  






  public static Stats fromByteArray(byte[] byteArray)
  {
    Preconditions.checkNotNull(byteArray);
    Preconditions.checkArgument(byteArray.length == 40, "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
    



    return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
  }
  









  static Stats readFrom(ByteBuffer buffer)
  {
    Preconditions.checkNotNull(buffer);
    Preconditions.checkArgument(buffer
      .remaining() >= 40, "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
      

      .remaining());
    return new Stats(buffer
      .getLong(), buffer
      .getDouble(), buffer
      .getDouble(), buffer
      .getDouble(), buffer
      .getDouble());
  }
}
