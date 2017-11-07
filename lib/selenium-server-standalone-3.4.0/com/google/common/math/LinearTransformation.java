package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
































@Beta
@GwtIncompatible
public abstract class LinearTransformation
{
  public LinearTransformation() {}
  
  public static LinearTransformationBuilder mapping(double x1, double y1)
  {
    Preconditions.checkArgument((DoubleUtils.isFinite(x1)) && (DoubleUtils.isFinite(y1)));
    return new LinearTransformationBuilder(x1, y1, null);
  }
  


  public static final class LinearTransformationBuilder
  {
    private final double x1;
    

    private final double y1;
    

    private LinearTransformationBuilder(double x1, double y1)
    {
      this.x1 = x1;
      this.y1 = y1;
    }
    





    public LinearTransformation and(double x2, double y2)
    {
      Preconditions.checkArgument((DoubleUtils.isFinite(x2)) && (DoubleUtils.isFinite(y2)));
      if (x2 == x1) {
        Preconditions.checkArgument(y2 != y1);
        return new LinearTransformation.VerticalLinearTransformation(x1);
      }
      return withSlope((y2 - y1) / (x2 - x1));
    }
    





    public LinearTransformation withSlope(double slope)
    {
      Preconditions.checkArgument(!Double.isNaN(slope));
      if (DoubleUtils.isFinite(slope)) {
        double yIntercept = y1 - x1 * slope;
        return new LinearTransformation.RegularLinearTransformation(slope, yIntercept);
      }
      return new LinearTransformation.VerticalLinearTransformation(x1);
    }
  }
  




  public static LinearTransformation vertical(double x)
  {
    Preconditions.checkArgument(DoubleUtils.isFinite(x));
    return new VerticalLinearTransformation(x);
  }
  



  public static LinearTransformation horizontal(double y)
  {
    Preconditions.checkArgument(DoubleUtils.isFinite(y));
    double slope = 0.0D;
    return new RegularLinearTransformation(slope, y);
  }
  





  public static LinearTransformation forNaN()
  {
    return NaNLinearTransformation.INSTANCE;
  }
  



  public abstract boolean isVertical();
  



  public abstract boolean isHorizontal();
  



  public abstract double slope();
  



  public abstract double transform(double paramDouble);
  



  public abstract LinearTransformation inverse();
  


  private static final class RegularLinearTransformation
    extends LinearTransformation
  {
    final double slope;
    

    final double yIntercept;
    

    @LazyInit
    LinearTransformation inverse;
    


    RegularLinearTransformation(double slope, double yIntercept)
    {
      this.slope = slope;
      this.yIntercept = yIntercept;
      inverse = null;
    }
    
    RegularLinearTransformation(double slope, double yIntercept, LinearTransformation inverse) {
      this.slope = slope;
      this.yIntercept = yIntercept;
      this.inverse = inverse;
    }
    
    public boolean isVertical()
    {
      return false;
    }
    
    public boolean isHorizontal()
    {
      return slope == 0.0D;
    }
    
    public double slope()
    {
      return slope;
    }
    
    public double transform(double x)
    {
      return x * slope + yIntercept;
    }
    
    public LinearTransformation inverse()
    {
      LinearTransformation result = inverse;
      return result == null ? (this.inverse = createInverse()) : result;
    }
    
    public String toString()
    {
      return String.format("y = %g * x + %g", new Object[] { Double.valueOf(slope), Double.valueOf(yIntercept) });
    }
    
    private LinearTransformation createInverse() {
      if (slope != 0.0D) {
        return new RegularLinearTransformation(1.0D / slope, -1.0D * yIntercept / slope, this);
      }
      return new LinearTransformation.VerticalLinearTransformation(yIntercept, this);
    }
  }
  
  private static final class VerticalLinearTransformation
    extends LinearTransformation
  {
    final double x;
    @LazyInit
    LinearTransformation inverse;
    
    VerticalLinearTransformation(double x)
    {
      this.x = x;
      inverse = null;
    }
    
    VerticalLinearTransformation(double x, LinearTransformation inverse) {
      this.x = x;
      this.inverse = inverse;
    }
    
    public boolean isVertical()
    {
      return true;
    }
    
    public boolean isHorizontal()
    {
      return false;
    }
    
    public double slope()
    {
      throw new IllegalStateException();
    }
    
    public double transform(double x)
    {
      throw new IllegalStateException();
    }
    
    public LinearTransformation inverse()
    {
      LinearTransformation result = inverse;
      return result == null ? (this.inverse = createInverse()) : result;
    }
    
    public String toString()
    {
      return String.format("x = %g", new Object[] { Double.valueOf(x) });
    }
    
    private LinearTransformation createInverse() {
      return new LinearTransformation.RegularLinearTransformation(0.0D, x, this);
    }
  }
  
  private static final class NaNLinearTransformation extends LinearTransformation
  {
    static final NaNLinearTransformation INSTANCE = new NaNLinearTransformation();
    
    private NaNLinearTransformation() {}
    
    public boolean isVertical() { return false; }
    

    public boolean isHorizontal()
    {
      return false;
    }
    
    public double slope()
    {
      return NaN.0D;
    }
    
    public double transform(double x)
    {
      return NaN.0D;
    }
    
    public LinearTransformation inverse()
    {
      return this;
    }
    
    public String toString()
    {
      return "NaN";
    }
  }
}
