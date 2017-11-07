package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nullable;



































@Beta
@GwtIncompatible
public final class PairedStats
  implements Serializable
{
  private final Stats xStats;
  private final Stats yStats;
  private final double sumOfProductsOfDeltas;
  private static final int BYTES = 88;
  private static final long serialVersionUID = 0L;
  
  PairedStats(Stats xStats, Stats yStats, double sumOfProductsOfDeltas)
  {
    this.xStats = xStats;
    this.yStats = yStats;
    this.sumOfProductsOfDeltas = sumOfProductsOfDeltas;
  }
  


  public long count()
  {
    return xStats.count();
  }
  


  public Stats xStats()
  {
    return xStats;
  }
  


  public Stats yStats()
  {
    return yStats;
  }
  













  public double populationCovariance()
  {
    Preconditions.checkState(count() != 0L);
    return sumOfProductsOfDeltas / count();
  }
  












  public double sampleCovariance()
  {
    Preconditions.checkState(count() > 1L);
    return sumOfProductsOfDeltas / (count() - 1L);
  }
  















  public double pearsonsCorrelationCoefficient()
  {
    Preconditions.checkState(count() > 1L);
    if (Double.isNaN(sumOfProductsOfDeltas)) {
      return NaN.0D;
    }
    double xSumOfSquaresOfDeltas = xStats().sumOfSquaresOfDeltas();
    double ySumOfSquaresOfDeltas = yStats().sumOfSquaresOfDeltas();
    Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0D);
    Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0D);
    


    double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
    return ensureInUnitRange(sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
  }
  






























  public LinearTransformation leastSquaresFit()
  {
    Preconditions.checkState(count() > 1L);
    if (Double.isNaN(sumOfProductsOfDeltas)) {
      return LinearTransformation.forNaN();
    }
    double xSumOfSquaresOfDeltas = xStats.sumOfSquaresOfDeltas();
    if (xSumOfSquaresOfDeltas > 0.0D) {
      if (yStats.sumOfSquaresOfDeltas() > 0.0D) {
        return 
          LinearTransformation.mapping(xStats.mean(), yStats.mean()).withSlope(sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
      }
      return LinearTransformation.horizontal(yStats.mean());
    }
    
    Preconditions.checkState(yStats.sumOfSquaresOfDeltas() > 0.0D);
    return LinearTransformation.vertical(xStats.mean());
  }
  










  public boolean equals(@Nullable Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PairedStats other = (PairedStats)obj;
    if ((xStats.equals(xStats)) && 
      (yStats.equals(yStats))) {}
    return 
    

      Double.doubleToLongBits(sumOfProductsOfDeltas) == Double.doubleToLongBits(sumOfProductsOfDeltas);
  }
  






  public int hashCode()
  {
    return Objects.hashCode(new Object[] { xStats, yStats, Double.valueOf(sumOfProductsOfDeltas) });
  }
  
  public String toString()
  {
    if (count() > 0L) {
      return 
      


        MoreObjects.toStringHelper(this).add("xStats", xStats).add("yStats", yStats).add("populationCovariance", populationCovariance()).toString();
    }
    return 
    

      MoreObjects.toStringHelper(this).add("xStats", xStats).add("yStats", yStats).toString();
  }
  
  double sumOfProductsOfDeltas()
  {
    return sumOfProductsOfDeltas;
  }
  
  private static double ensurePositive(double value) {
    if (value > 0.0D) {
      return value;
    }
    return Double.MIN_VALUE;
  }
  
  private static double ensureInUnitRange(double value)
  {
    if (value >= 1.0D) {
      return 1.0D;
    }
    if (value <= -1.0D) {
      return -1.0D;
    }
    return value;
  }
  












  public byte[] toByteArray()
  {
    ByteBuffer buffer = ByteBuffer.allocate(88).order(ByteOrder.LITTLE_ENDIAN);
    xStats.writeTo(buffer);
    yStats.writeTo(buffer);
    buffer.putDouble(sumOfProductsOfDeltas);
    return buffer.array();
  }
  






  public static PairedStats fromByteArray(byte[] byteArray)
  {
    Preconditions.checkNotNull(byteArray);
    Preconditions.checkArgument(byteArray.length == 88, "Expected PairedStats.BYTES = %s, got %s", 88, byteArray.length);
    



    ByteBuffer buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
    Stats xStats = Stats.readFrom(buffer);
    Stats yStats = Stats.readFrom(buffer);
    double sumOfProductsOfDeltas = buffer.getDouble();
    return new PairedStats(xStats, yStats, sumOfProductsOfDeltas);
  }
}
