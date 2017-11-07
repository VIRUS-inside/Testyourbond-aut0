package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedLongs;
import java.math.RoundingMode;














































@GwtCompatible(emulated=true)
public final class LongMath
{
  @VisibleForTesting
  static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
  @VisibleForTesting
  static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
  
  @Beta
  public static long ceilingPowerOfTwo(long x)
  {
    MathPreconditions.checkPositive("x", x);
    if (x > 4611686018427387904L) {
      throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") is not representable as a long");
    }
    return 1L << -Long.numberOfLeadingZeros(x - 1L);
  }
  






  @Beta
  public static long floorPowerOfTwo(long x)
  {
    MathPreconditions.checkPositive("x", x);
    


    return 1L << 63 - Long.numberOfLeadingZeros(x);
  }
  





  public static boolean isPowerOfTwo(long x)
  {
    return (x > 0L ? 1 : 0) & ((x & x - 1L) == 0L ? 1 : 0);
  }
  





  @VisibleForTesting
  static int lessThanBranchFree(long x, long y)
  {
    return (int)((x - y ^ 0xFFFFFFFFFFFFFFFF ^ 0xFFFFFFFFFFFFFFFF) >>> 63);
  }
  








  public static int log2(long x, RoundingMode mode)
  {
    MathPreconditions.checkPositive("x", x);
    switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
    
    case 2: 
    case 3: 
      return 63 - Long.numberOfLeadingZeros(x);
    
    case 4: 
    case 5: 
      return 64 - Long.numberOfLeadingZeros(x - 1L);
    

    case 6: 
    case 7: 
    case 8: 
      int leadingZeros = Long.numberOfLeadingZeros(x);
      long cmp = -5402926248376769404L >>> leadingZeros;
      
      int logFloor = 63 - leadingZeros;
      return logFloor + lessThanBranchFree(cmp, x);
    }
    
    throw new AssertionError("impossible");
  }
  












  @GwtIncompatible
  public static int log10(long x, RoundingMode mode)
  {
    MathPreconditions.checkPositive("x", x);
    int logFloor = log10Floor(x);
    long floorPow = powersOf10[logFloor];
    switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(x == floorPow);
    
    case 2: 
    case 3: 
      return logFloor;
    case 4: 
    case 5: 
      return logFloor + lessThanBranchFree(floorPow, x);
    
    case 6: 
    case 7: 
    case 8: 
      return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
    }
    throw new AssertionError();
  }
  







  @GwtIncompatible
  static int log10Floor(long x)
  {
    int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
    



    return y - lessThanBranchFree(x, powersOf10[y]);
  }
  

  @VisibleForTesting
  static final byte[] maxLog10ForLeadingZeros = { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
  




  @GwtIncompatible
  @VisibleForTesting
  static final long[] powersOf10 = { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
  





















  @GwtIncompatible
  @VisibleForTesting
  static final long[] halfPowersOf10 = { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
  











  @VisibleForTesting
  static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
  












  @GwtIncompatible
  public static long pow(long b, int k)
  {
    MathPreconditions.checkNonNegative("exponent", k);
    if ((-2L <= b) && (b <= 2L)) {
      switch ((int)b) {
      case 0: 
        return k == 0 ? 1L : 0L;
      case 1: 
        return 1L;
      case -1: 
        return (k & 0x1) == 0 ? 1L : -1L;
      case 2: 
        return k < 64 ? 1L << k : 0L;
      case -2: 
        if (k < 64) {
          return (k & 0x1) == 0 ? 1L << k : -(1L << k);
        }
        return 0L;
      }
      
      throw new AssertionError();
    }
    
    for (long accum = 1L;; k >>= 1) {
      switch (k) {
      case 0: 
        return accum;
      case 1: 
        return accum * b;
      }
      accum *= ((k & 0x1) == 0 ? 1L : b);
      b *= b;
    }
  }
  








  @GwtIncompatible
  public static long sqrt(long x, RoundingMode mode)
  {
    MathPreconditions.checkNonNegative("x", x);
    if (fitsInInt(x)) {
      return IntMath.sqrt((int)x, mode);
    }
    














    long guess = Math.sqrt(x);
    
    long guessSquared = guess * guess;
    

    switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(guessSquared == x);
      return guess;
    case 2: 
    case 3: 
      if (x < guessSquared) {
        return guess - 1L;
      }
      return guess;
    case 4: 
    case 5: 
      if (x > guessSquared) {
        return guess + 1L;
      }
      return guess;
    case 6: 
    case 7: 
    case 8: 
      long sqrtFloor = guess - (x < guessSquared ? 1 : 0);
      long halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
      










      return sqrtFloor + lessThanBranchFree(halfSquare, x);
    }
    throw new AssertionError();
  }
  








  @GwtIncompatible
  public static long divide(long p, long q, RoundingMode mode)
  {
    Preconditions.checkNotNull(mode);
    long div = p / q;
    long rem = p - q * div;
    
    if (rem == 0L) {
      return div;
    }
    







    int signum = 0x1 | (int)((p ^ q) >> 63);
    boolean increment;
    boolean increment; boolean increment; boolean increment; boolean increment; switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(rem == 0L);
    
    case 2: 
      increment = false;
      break;
    case 4: 
      increment = true;
      break;
    case 5: 
      increment = signum > 0;
      break;
    case 3: 
      increment = signum < 0;
      break;
    case 6: 
    case 7: 
    case 8: 
      long absRem = Math.abs(rem);
      long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
      
      boolean increment;
      if (cmpRemToHalfDivisor == 0L) {
        increment = (mode == RoundingMode.HALF_UP ? 1 : 0) | (mode == RoundingMode.HALF_EVEN ? 1 : 0) & ((div & 1L) != 0L ? 1 : 0);
      } else {
        increment = cmpRemToHalfDivisor > 0L;
      }
      break;
    default: 
      throw new AssertionError(); }
    boolean increment;
    return increment ? div + signum : div;
  }
  


















  @GwtIncompatible
  public static int mod(long x, int m)
  {
    return (int)mod(x, m);
  }
  

















  @GwtIncompatible
  public static long mod(long x, long m)
  {
    if (m <= 0L) {
      throw new ArithmeticException("Modulus must be positive");
    }
    long result = x % m;
    return result >= 0L ? result : result + m;
  }
  










  public static long gcd(long a, long b)
  {
    MathPreconditions.checkNonNegative("a", a);
    MathPreconditions.checkNonNegative("b", b);
    if (a == 0L)
    {

      return b; }
    if (b == 0L) {
      return a;
    }
    



    int aTwos = Long.numberOfTrailingZeros(a);
    a >>= aTwos;
    int bTwos = Long.numberOfTrailingZeros(b);
    b >>= bTwos;
    while (a != b)
    {






      long delta = a - b;
      
      long minDeltaOrZero = delta & delta >> 63;
      

      a = delta - minDeltaOrZero - minDeltaOrZero;
      

      b += minDeltaOrZero;
      a >>= Long.numberOfTrailingZeros(a);
    }
    return a << Math.min(aTwos, bTwos);
  }
  




  @GwtIncompatible
  public static long checkedAdd(long a, long b)
  {
    long result = a + b;
    MathPreconditions.checkNoOverflow(((a ^ b) < 0L ? 1 : 0) | ((a ^ result) >= 0L ? 1 : 0));
    return result;
  }
  




  @GwtIncompatible
  public static long checkedSubtract(long a, long b)
  {
    long result = a - b;
    MathPreconditions.checkNoOverflow(((a ^ b) >= 0L ? 1 : 0) | ((a ^ result) >= 0L ? 1 : 0));
    return result;
  }
  









  @GwtIncompatible
  public static long checkedMultiply(long a, long b)
  {
    int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFF) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFF);
    









    if (leadingZeros > 65) {
      return a * b;
    }
    MathPreconditions.checkNoOverflow(leadingZeros >= 64);
    MathPreconditions.checkNoOverflow((a >= 0L ? 1 : 0) | (b != Long.MIN_VALUE ? 1 : 0));
    long result = a * b;
    MathPreconditions.checkNoOverflow((a == 0L) || (result / a == b));
    return result;
  }
  





  @GwtIncompatible
  public static long checkedPow(long b, int k)
  {
    MathPreconditions.checkNonNegative("exponent", k);
    if (((b >= -2L ? 1 : 0) & (b <= 2L ? 1 : 0)) != 0) {
      switch ((int)b) {
      case 0: 
        return k == 0 ? 1L : 0L;
      case 1: 
        return 1L;
      case -1: 
        return (k & 0x1) == 0 ? 1L : -1L;
      case 2: 
        MathPreconditions.checkNoOverflow(k < 63);
        return 1L << k;
      case -2: 
        MathPreconditions.checkNoOverflow(k < 64);
        return (k & 0x1) == 0 ? 1L << k : -1L << k;
      }
      throw new AssertionError();
    }
    
    long accum = 1L;
    for (;;) {
      switch (k) {
      case 0: 
        return accum;
      case 1: 
        return checkedMultiply(accum, b);
      }
      if ((k & 0x1) != 0) {
        accum = checkedMultiply(accum, b);
      }
      k >>= 1;
      if (k > 0) {
        MathPreconditions.checkNoOverflow((-3037000499L <= b) && (b <= 3037000499L));
        b *= b;
      }
    }
  }
  






  @Beta
  public static long saturatedAdd(long a, long b)
  {
    long naiveSum = a + b;
    if ((((a ^ b) < 0L ? 1 : 0) | ((a ^ naiveSum) >= 0L ? 1 : 0)) != 0)
    {

      return naiveSum;
    }
    
    return Long.MAX_VALUE + (naiveSum >>> 63 ^ 1L);
  }
  





  @Beta
  public static long saturatedSubtract(long a, long b)
  {
    long naiveDifference = a - b;
    if ((((a ^ b) >= 0L ? 1 : 0) | ((a ^ naiveDifference) >= 0L ? 1 : 0)) != 0)
    {

      return naiveDifference;
    }
    
    return Long.MAX_VALUE + (naiveDifference >>> 63 ^ 1L);
  }
  










  @Beta
  public static long saturatedMultiply(long a, long b)
  {
    int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFF) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFF);
    if (leadingZeros > 65) {
      return a * b;
    }
    
    long limit = Long.MAX_VALUE + ((a ^ b) >>> 63);
    if (((leadingZeros < 64 ? 1 : 0) | (a < 0L ? 1 : 0) & (b == Long.MIN_VALUE ? 1 : 0)) != 0)
    {
      return limit;
    }
    long result = a * b;
    if ((a == 0L) || (result / a == b)) {
      return result;
    }
    return limit;
  }
  





  @Beta
  public static long saturatedPow(long b, int k)
  {
    MathPreconditions.checkNonNegative("exponent", k);
    if (((b >= -2L ? 1 : 0) & (b <= 2L ? 1 : 0)) != 0) {
      switch ((int)b) {
      case 0: 
        return k == 0 ? 1L : 0L;
      case 1: 
        return 1L;
      case -1: 
        return (k & 0x1) == 0 ? 1L : -1L;
      case 2: 
        if (k >= 63) {
          return Long.MAX_VALUE;
        }
        return 1L << k;
      case -2: 
        if (k >= 64) {
          return Long.MAX_VALUE + (k & 0x1);
        }
        return (k & 0x1) == 0 ? 1L << k : -1L << k;
      }
      throw new AssertionError();
    }
    
    long accum = 1L;
    
    long limit = Long.MAX_VALUE + (b >>> 63 & k & 0x1);
    for (;;) {
      switch (k) {
      case 0: 
        return accum;
      case 1: 
        return saturatedMultiply(accum, b);
      }
      if ((k & 0x1) != 0) {
        accum = saturatedMultiply(accum, b);
      }
      k >>= 1;
      if (k > 0) {
        if (((-3037000499L > b ? 1 : 0) | (b > 3037000499L ? 1 : 0)) != 0) {
          return limit;
        }
        b *= b;
      }
    }
  }
  








  @GwtIncompatible
  public static long factorial(int n)
  {
    MathPreconditions.checkNonNegative("n", n);
    return n < factorials.length ? factorials[n] : Long.MAX_VALUE;
  }
  
  static final long[] factorials = { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
  



























  public static long binomial(int n, int k)
  {
    MathPreconditions.checkNonNegative("n", n);
    MathPreconditions.checkNonNegative("k", k);
    Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
    if (k > n >> 1) {
      k = n - k;
    }
    switch (k) {
    case 0: 
      return 1L;
    case 1: 
      return n;
    }
    if (n < factorials.length)
      return factorials[n] / (factorials[k] * factorials[(n - k)]);
    if ((k >= biggestBinomials.length) || (n > biggestBinomials[k]))
      return Long.MAX_VALUE;
    if ((k < biggestSimpleBinomials.length) && (n <= biggestSimpleBinomials[k]))
    {
      long result = n--;
      for (int i = 2; i <= k; i++) {
        result *= n;
        result /= i;n--;
      }
      
      return result;
    }
    int nBits = log2(n, RoundingMode.CEILING);
    
    long result = 1L;
    long numerator = n--;
    long denominator = 1L;
    
    int numeratorBits = nBits;
    






    for (int i = 2; i <= k; n--) {
      if (numeratorBits + nBits < 63)
      {
        numerator *= n;
        denominator *= i;
        numeratorBits += nBits;
      }
      else
      {
        result = multiplyFraction(result, numerator, denominator);
        numerator = n;
        denominator = i;
        numeratorBits = nBits;
      }
      i++;
    }
    












    return multiplyFraction(result, numerator, denominator);
  }
  




  static long multiplyFraction(long x, long numerator, long denominator)
  {
    if (x == 1L) {
      return numerator / denominator;
    }
    long commonDivisor = gcd(x, denominator);
    x /= commonDivisor;
    denominator /= commonDivisor;
    

    return x * (numerator / denominator);
  }
  



  static final int[] biggestBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
  







































  @VisibleForTesting
  static final int[] biggestSimpleBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
  















  private static final int SIEVE_30 = -545925251;
  
















  static boolean fitsInInt(long x)
  {
    return (int)x == x;
  }
  








  public static long mean(long x, long y)
  {
    return (x & y) + ((x ^ y) >> 1);
  }
  




















  @GwtIncompatible
  @Beta
  public static boolean isPrime(long n)
  {
    if (n < 2L) {
      MathPreconditions.checkNonNegative("n", n);
      return false;
    }
    if ((n == 2L) || (n == 3L) || (n == 5L) || (n == 7L) || (n == 11L) || (n == 13L)) {
      return true;
    }
    
    if ((0xDF75D77D & 1 << (int)(n % 30L)) != 0) {
      return false;
    }
    if ((n % 7L == 0L) || (n % 11L == 0L) || (n % 13L == 0L)) {
      return false;
    }
    if (n < 289L) {
      return true;
    }
    
    for (long[] baseSet : millerRabinBaseSets) {
      if (n <= baseSet[0]) {
        for (int i = 1; i < baseSet.length; i++) {
          if (!MillerRabinTester.test(baseSet[i], n)) {
            return false;
          }
        }
        return true;
      }
    }
    throw new AssertionError();
  }
  







  private static final long[][] millerRabinBaseSets = { { 291830L, 126401071349994536L }, { 885594168L, 725270293939359937L, 3569819667048198375L }, { 273919523040L, 15L, 7363882082L, 992620450144556L }, { 47636622961200L, 2L, 2570940L, 211991001L, 3749873356L }, { 7999252175582850L, 2L, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L }, { 585226005592931976L, 2L, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L }, { Long.MAX_VALUE, 2L, 325L, 9375L, 28178L, 450775L, 9780504L, 1795265022L } };
  











  private LongMath() {}
  










  private static abstract enum MillerRabinTester
  {
    SMALL, 
    


















    LARGE;
    





































    private MillerRabinTester() {}
    





































    static boolean test(long base, long n)
    {
      return (n <= 3037000499L ? SMALL : LARGE).testWitness(base, n);
    }
    



    abstract long mulMod(long paramLong1, long paramLong2, long paramLong3);
    



    abstract long squareMod(long paramLong1, long paramLong2);
    


    private long powMod(long a, long p, long m)
    {
      long res = 1L;
      for (; p != 0L; p >>= 1) {
        if ((p & 1L) != 0L) {
          res = mulMod(res, a, m);
        }
        a = squareMod(a, m);
      }
      return res;
    }
    


    private boolean testWitness(long base, long n)
    {
      int r = Long.numberOfTrailingZeros(n - 1L);
      long d = n - 1L >> r;
      base %= n;
      if (base == 0L) {
        return true;
      }
      
      long a = powMod(base, d, n);
      


      if (a == 1L) {
        return true;
      }
      int j = 0;
      while (a != n - 1L) {
        j++; if (j == r) {
          return false;
        }
        a = squareMod(a, n);
      }
      return true;
    }
  }
}
