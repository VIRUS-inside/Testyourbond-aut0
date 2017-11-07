package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.annotation.Nullable;



















































































@Beta
public final class BloomFilter<T>
  implements Predicate<T>, Serializable
{
  private final BloomFilterStrategies.BitArray bits;
  private final int numHashFunctions;
  private final Funnel<? super T> funnel;
  private final Strategy strategy;
  
  private BloomFilter(BloomFilterStrategies.BitArray bits, int numHashFunctions, Funnel<? super T> funnel, Strategy strategy)
  {
    Preconditions.checkArgument(numHashFunctions > 0, "numHashFunctions (%s) must be > 0", numHashFunctions);
    Preconditions.checkArgument(numHashFunctions <= 255, "numHashFunctions (%s) must be <= 255", numHashFunctions);
    
    this.bits = ((BloomFilterStrategies.BitArray)Preconditions.checkNotNull(bits));
    this.numHashFunctions = numHashFunctions;
    this.funnel = ((Funnel)Preconditions.checkNotNull(funnel));
    this.strategy = ((Strategy)Preconditions.checkNotNull(strategy));
  }
  





  public BloomFilter<T> copy()
  {
    return new BloomFilter(bits.copy(), numHashFunctions, funnel, strategy);
  }
  



  public boolean mightContain(T object)
  {
    return strategy.mightContain(object, funnel, numHashFunctions, bits);
  }
  




  @Deprecated
  public boolean apply(T input)
  {
    return mightContain(input);
  }
  










  @CanIgnoreReturnValue
  public boolean put(T object)
  {
    return strategy.put(object, funnel, numHashFunctions, bits);
  }
  











  public double expectedFpp()
  {
    return Math.pow(bits.bitCount() / bitSize(), numHashFunctions);
  }
  


  @VisibleForTesting
  long bitSize()
  {
    return bits.bitSize();
  }
  














  public boolean isCompatible(BloomFilter<T> that)
  {
    Preconditions.checkNotNull(that);
    if ((this != that) && (numHashFunctions == numHashFunctions)) {} return 
    
      (bitSize() == that.bitSize()) && 
      (strategy.equals(strategy)) && 
      (funnel.equals(funnel));
  }
  









  public void putAll(BloomFilter<T> that)
  {
    Preconditions.checkNotNull(that);
    Preconditions.checkArgument(this != that, "Cannot combine a BloomFilter with itself.");
    Preconditions.checkArgument(numHashFunctions == numHashFunctions, "BloomFilters must have the same number of hash functions (%s != %s)", numHashFunctions, numHashFunctions);
    



    Preconditions.checkArgument(
      bitSize() == that.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", 
      
      bitSize(), that
      .bitSize());
    Preconditions.checkArgument(strategy
      .equals(strategy), "BloomFilters must have equal strategies (%s != %s)", strategy, strategy);
    


    Preconditions.checkArgument(funnel
      .equals(funnel), "BloomFilters must have equal funnels (%s != %s)", funnel, funnel);
    


    bits.putAll(bits);
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof BloomFilter)) {
      BloomFilter<?> that = (BloomFilter)object;
      return (numHashFunctions == numHashFunctions) && 
        (funnel.equals(funnel)) && 
        (bits.equals(bits)) && 
        (strategy.equals(strategy));
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { Integer.valueOf(numHashFunctions), funnel, strategy, bits });
  }
  




















  public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions, double fpp)
  {
    return create(funnel, expectedInsertions, fpp);
  }
  





















  public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp)
  {
    return create(funnel, expectedInsertions, fpp, BloomFilterStrategies.MURMUR128_MITZ_64);
  }
  
  @VisibleForTesting
  static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy)
  {
    Preconditions.checkNotNull(funnel);
    Preconditions.checkArgument(expectedInsertions >= 0L, "Expected insertions (%s) must be >= 0", expectedInsertions);
    
    Preconditions.checkArgument(fpp > 0.0D, "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
    Preconditions.checkArgument(fpp < 1.0D, "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
    Preconditions.checkNotNull(strategy);
    
    if (expectedInsertions == 0L) {
      expectedInsertions = 1L;
    }
    




    long numBits = optimalNumOfBits(expectedInsertions, fpp);
    int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
    try {
      return new BloomFilter(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, strategy);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
    }
  }
  


















  public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions)
  {
    return create(funnel, expectedInsertions);
  }
  



















  public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions)
  {
    return create(funnel, expectedInsertions, 0.03D);
  }
  





















  @VisibleForTesting
  static int optimalNumOfHashFunctions(long n, long m)
  {
    return Math.max(1, (int)Math.round(m / n * Math.log(2.0D)));
  }
  








  @VisibleForTesting
  static long optimalNumOfBits(long n, double p)
  {
    if (p == 0.0D) {
      p = Double.MIN_VALUE;
    }
    return (-n * Math.log(p) / (Math.log(2.0D) * Math.log(2.0D)));
  }
  

  private Object writeReplace() { return new SerialForm(this); }
  
  private static class SerialForm<T> implements Serializable {
    final long[] data;
    final int numHashFunctions;
    final Funnel<? super T> funnel;
    final BloomFilter.Strategy strategy;
    private static final long serialVersionUID = 1L;
    
    SerialForm(BloomFilter<T> bf) {
      data = bits.data;
      numHashFunctions = numHashFunctions;
      funnel = funnel;
      strategy = strategy;
    }
    
    Object readResolve() {
      return new BloomFilter(new BloomFilterStrategies.BitArray(data), numHashFunctions, funnel, strategy, null);
    }
  }
  












  public void writeTo(OutputStream out)
    throws IOException
  {
    DataOutputStream dout = new DataOutputStream(out);
    dout.writeByte(SignedBytes.checkedCast(strategy.ordinal()));
    dout.writeByte(UnsignedBytes.checkedCast(numHashFunctions));
    dout.writeInt(bits.data.length);
    for (long value : bits.data) {
      dout.writeLong(value);
    }
  }
  









  public static <T> BloomFilter<T> readFrom(InputStream in, Funnel<T> funnel)
    throws IOException
  {
    Preconditions.checkNotNull(in, "InputStream");
    Preconditions.checkNotNull(funnel, "Funnel");
    int strategyOrdinal = -1;
    int numHashFunctions = -1;
    int dataLength = -1;
    try {
      DataInputStream din = new DataInputStream(in);
      


      strategyOrdinal = din.readByte();
      numHashFunctions = UnsignedBytes.toInt(din.readByte());
      dataLength = din.readInt();
      
      Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
      long[] data = new long[dataLength];
      for (int i = 0; i < data.length; i++) {
        data[i] = din.readLong();
      }
      return new BloomFilter(new BloomFilterStrategies.BitArray(data), numHashFunctions, funnel, strategy);
    } catch (RuntimeException e) {
      String message = "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + strategyOrdinal + " numHashFunctions: " + numHashFunctions + " dataLength: " + dataLength;
      






      throw new IOException(message, e);
    }
  }
  
  static abstract interface Strategy
    extends Serializable
  {
    public abstract <T> boolean put(T paramT, Funnel<? super T> paramFunnel, int paramInt, BloomFilterStrategies.BitArray paramBitArray);
    
    public abstract <T> boolean mightContain(T paramT, Funnel<? super T> paramFunnel, int paramInt, BloomFilterStrategies.BitArray paramBitArray);
    
    public abstract int ordinal();
  }
}
