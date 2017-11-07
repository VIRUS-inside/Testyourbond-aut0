package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;







































@GwtCompatible
public abstract class DiscreteDomain<C extends Comparable>
{
  public static DiscreteDomain<Integer> integers()
  {
    return IntegerDomain.INSTANCE;
  }
  
  private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
    private static final IntegerDomain INSTANCE = new IntegerDomain();
    
    private IntegerDomain() {}
    
    public Integer next(Integer value) { int i = value.intValue();
      return i == Integer.MAX_VALUE ? null : Integer.valueOf(i + 1);
    }
    
    public Integer previous(Integer value)
    {
      int i = value.intValue();
      return i == Integer.MIN_VALUE ? null : Integer.valueOf(i - 1);
    }
    
    public long distance(Integer start, Integer end)
    {
      return end.intValue() - start.intValue();
    }
    
    public Integer minValue()
    {
      return Integer.valueOf(Integer.MIN_VALUE);
    }
    
    public Integer maxValue()
    {
      return Integer.valueOf(Integer.MAX_VALUE);
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    
    public String toString()
    {
      return "DiscreteDomain.integers()";
    }
    


    private static final long serialVersionUID = 0L;
  }
  


  public static DiscreteDomain<Long> longs()
  {
    return LongDomain.INSTANCE;
  }
  
  private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
    private static final LongDomain INSTANCE = new LongDomain();
    
    private LongDomain() {}
    
    public Long next(Long value) { long l = value.longValue();
      return l == Long.MAX_VALUE ? null : Long.valueOf(l + 1L);
    }
    
    public Long previous(Long value)
    {
      long l = value.longValue();
      return l == Long.MIN_VALUE ? null : Long.valueOf(l - 1L);
    }
    
    public long distance(Long start, Long end)
    {
      long result = end.longValue() - start.longValue();
      if ((end.longValue() > start.longValue()) && (result < 0L)) {
        return Long.MAX_VALUE;
      }
      if ((end.longValue() < start.longValue()) && (result > 0L)) {
        return Long.MIN_VALUE;
      }
      return result;
    }
    
    public Long minValue()
    {
      return Long.valueOf(Long.MIN_VALUE);
    }
    
    public Long maxValue()
    {
      return Long.valueOf(Long.MAX_VALUE);
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    
    public String toString()
    {
      return "DiscreteDomain.longs()";
    }
    



    private static final long serialVersionUID = 0L;
  }
  



  public static DiscreteDomain<BigInteger> bigIntegers() { return BigIntegerDomain.INSTANCE; }
  
  protected DiscreteDomain() {}
  
  private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
    private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
    
    private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    
    private BigIntegerDomain() {}
    
    public BigInteger next(BigInteger value) { return value.add(BigInteger.ONE); }
    

    public BigInteger previous(BigInteger value)
    {
      return value.subtract(BigInteger.ONE);
    }
    
    public long distance(BigInteger start, BigInteger end)
    {
      return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    
    public String toString()
    {
      return "DiscreteDomain.bigIntegers()";
    }
    










    private static final long serialVersionUID = 0L;
  }
  









  public abstract C next(C paramC);
  









  public abstract C previous(C paramC);
  









  public abstract long distance(C paramC1, C paramC2);
  









  @CanIgnoreReturnValue
  public C minValue()
  {
    throw new NoSuchElementException();
  }
  










  @CanIgnoreReturnValue
  public C maxValue()
  {
    throw new NoSuchElementException();
  }
}
