package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;



























@Beta
@GwtIncompatible
public final class PairedStatsAccumulator
{
  private final StatsAccumulator xStats = new StatsAccumulator();
  private final StatsAccumulator yStats = new StatsAccumulator();
  private double sumOfProductsOfDeltas = 0.0D;
  






  public PairedStatsAccumulator() {}
  





  public void add(double x, double y)
  {
    xStats.add(x);
    if ((Doubles.isFinite(x)) && (Doubles.isFinite(y))) {
      if (xStats.count() > 1L) {
        sumOfProductsOfDeltas += (x - xStats.mean()) * (y - yStats.mean());
      }
    } else {
      sumOfProductsOfDeltas = NaN.0D;
    }
    yStats.add(y);
  }
  



  public void addAll(PairedStats values)
  {
    if (values.count() == 0L) {
      return;
    }
    
    xStats.addAll(values.xStats());
    if (yStats.count() == 0L) {
      sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();



    }
    else
    {


      sumOfProductsOfDeltas = (sumOfProductsOfDeltas + (values.sumOfProductsOfDeltas() + (values.xStats().mean() - xStats.mean()) * (values.yStats().mean() - yStats.mean()) * values.count()));
    }
    yStats.addAll(values.yStats());
  }
  


  public PairedStats snapshot()
  {
    return new PairedStats(xStats.snapshot(), yStats.snapshot(), sumOfProductsOfDeltas);
  }
  


  public long count()
  {
    return xStats.count();
  }
  


  public Stats xStats()
  {
    return xStats.snapshot();
  }
  


  public Stats yStats()
  {
    return yStats.snapshot();
  }
  













  public double populationCovariance()
  {
    Preconditions.checkState(count() != 0L);
    return sumOfProductsOfDeltas / count();
  }
  












  public final double sampleCovariance()
  {
    Preconditions.checkState(count() > 1L);
    return sumOfProductsOfDeltas / (count() - 1L);
  }
  















  public final double pearsonsCorrelationCoefficient()
  {
    Preconditions.checkState(count() > 1L);
    if (Double.isNaN(sumOfProductsOfDeltas)) {
      return NaN.0D;
    }
    double xSumOfSquaresOfDeltas = xStats.sumOfSquaresOfDeltas();
    double ySumOfSquaresOfDeltas = yStats.sumOfSquaresOfDeltas();
    Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0D);
    Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0D);
    


    double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
    return ensureInUnitRange(sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
  }
  






























  public final LinearTransformation leastSquaresFit()
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
  
  private double ensurePositive(double value)
  {
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
}
