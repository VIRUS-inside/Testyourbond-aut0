package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;














































@GwtCompatible(emulated=true)
public final class IntMath
{
  @VisibleForTesting
  static final int MAX_SIGNED_POWER_OF_TWO = 1073741824;
  @VisibleForTesting
  static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
  
  @Beta
  public static int ceilingPowerOfTwo(int x)
  {
    MathPreconditions.checkPositive("x", x);
    if (x > 1073741824) {
      throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") not representable as an int");
    }
    return 1 << -Integer.numberOfLeadingZeros(x - 1);
  }
  






  @Beta
  public static int floorPowerOfTwo(int x)
  {
    MathPreconditions.checkPositive("x", x);
    return Integer.highestOneBit(x);
  }
  






  public static boolean isPowerOfTwo(int x)
  {
    return (x > 0 ? 1 : 0) & ((x & x - 1) == 0 ? 1 : 0);
  }
  






  @VisibleForTesting
  static int lessThanBranchFree(int x, int y)
  {
    return (x - y ^ 0xFFFFFFFF ^ 0xFFFFFFFF) >>> 31;
  }
  








  public static int log2(int x, RoundingMode mode)
  {
    MathPreconditions.checkPositive("x", x);
    switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
    
    case 2: 
    case 3: 
      return 31 - Integer.numberOfLeadingZeros(x);
    
    case 4: 
    case 5: 
      return 32 - Integer.numberOfLeadingZeros(x - 1);
    

    case 6: 
    case 7: 
    case 8: 
      int leadingZeros = Integer.numberOfLeadingZeros(x);
      int cmp = -1257966797 >>> leadingZeros;
      
      int logFloor = 31 - leadingZeros;
      return logFloor + lessThanBranchFree(cmp, x);
    }
    
    throw new AssertionError();
  }
  











  @GwtIncompatible
  public static int log10(int x, RoundingMode mode)
  {
    MathPreconditions.checkPositive("x", x);
    int logFloor = log10Floor(x);
    int floorPow = powersOf10[logFloor];
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
  







  private static int log10Floor(int x)
  {
    int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
    



    return y - lessThanBranchFree(x, powersOf10[y]);
  }
  

  @VisibleForTesting
  static final byte[] maxLog10ForLeadingZeros = { 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0 };
  



  @VisibleForTesting
  static final int[] powersOf10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
  



  @VisibleForTesting
  static final int[] halfPowersOf10 = { 3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE };
  



  @VisibleForTesting
  static final int FLOOR_SQRT_MAX_INT = 46340;
  




  @GwtIncompatible
  public static int pow(int b, int k)
  {
    MathPreconditions.checkNonNegative("exponent", k);
    switch (b) {
    case 0: 
      return k == 0 ? 1 : 0;
    case 1: 
      return 1;
    case -1: 
      return (k & 0x1) == 0 ? 1 : -1;
    case 2: 
      return k < 32 ? 1 << k : 0;
    case -2: 
      if (k < 32) {
        return (k & 0x1) == 0 ? 1 << k : -(1 << k);
      }
      return 0;
    }
    
    

    for (int accum = 1;; k >>= 1) {
      switch (k) {
      case 0: 
        return accum;
      case 1: 
        return b * accum;
      }
      accum *= ((k & 0x1) == 0 ? 1 : b);
      b *= b;
    }
  }
  








  @GwtIncompatible
  public static int sqrt(int x, RoundingMode mode)
  {
    MathPreconditions.checkNonNegative("x", x);
    int sqrtFloor = sqrtFloor(x);
    switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(sqrtFloor * sqrtFloor == x);
    case 2: 
    case 3: 
      return sqrtFloor;
    case 4: 
    case 5: 
      return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
    case 6: 
    case 7: 
    case 8: 
      int halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
      










      return sqrtFloor + lessThanBranchFree(halfSquare, x);
    }
    throw new AssertionError();
  }
  


  private static int sqrtFloor(int x)
  {
    return (int)Math.sqrt(x);
  }
  







  public static int divide(int p, int q, RoundingMode mode)
  {
    Preconditions.checkNotNull(mode);
    if (q == 0) {
      throw new ArithmeticException("/ by zero");
    }
    int div = p / q;
    int rem = p - q * div;
    
    if (rem == 0) {
      return div;
    }
    







    int signum = 0x1 | (p ^ q) >> 31;
    boolean increment;
    boolean increment; boolean increment; boolean increment; boolean increment; switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(rem == 0);
    
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
      int absRem = Math.abs(rem);
      int cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
      
      boolean increment;
      if (cmpRemToHalfDivisor == 0) {
        if (mode != RoundingMode.HALF_UP) {} increment = ((mode == RoundingMode.HALF_EVEN ? 1 : 0) & ((div & 0x1) != 0 ? 1 : 0)) != 0;
      } else {
        increment = cmpRemToHalfDivisor > 0;
      }
      break;
    default: 
      throw new AssertionError(); }
    boolean increment;
    return increment ? div + signum : div;
  }
  















  public static int mod(int x, int m)
  {
    if (m <= 0) {
      throw new ArithmeticException("Modulus " + m + " must be > 0");
    }
    int result = x % m;
    return result >= 0 ? result : result + m;
  }
  










  public static int gcd(int a, int b)
  {
    MathPreconditions.checkNonNegative("a", a);
    MathPreconditions.checkNonNegative("b", b);
    if (a == 0)
    {

      return b; }
    if (b == 0) {
      return a;
    }
    



    int aTwos = Integer.numberOfTrailingZeros(a);
    a >>= aTwos;
    int bTwos = Integer.numberOfTrailingZeros(b);
    b >>= bTwos;
    while (a != b)
    {






      int delta = a - b;
      
      int minDeltaOrZero = delta & delta >> 31;
      

      a = delta - minDeltaOrZero - minDeltaOrZero;
      

      b += minDeltaOrZero;
      a >>= Integer.numberOfTrailingZeros(a);
    }
    return a << Math.min(aTwos, bTwos);
  }
  




  public static int checkedAdd(int a, int b)
  {
    long result = a + b;
    MathPreconditions.checkNoOverflow(result == (int)result);
    return (int)result;
  }
  




  public static int checkedSubtract(int a, int b)
  {
    long result = a - b;
    MathPreconditions.checkNoOverflow(result == (int)result);
    return (int)result;
  }
  




  public static int checkedMultiply(int a, int b)
  {
    long result = a * b;
    MathPreconditions.checkNoOverflow(result == (int)result);
    return (int)result;
  }
  







  public static int checkedPow(int b, int k)
  {
    MathPreconditions.checkNonNegative("exponent", k);
    switch (b) {
    case 0: 
      return k == 0 ? 1 : 0;
    case 1: 
      return 1;
    case -1: 
      return (k & 0x1) == 0 ? 1 : -1;
    case 2: 
      MathPreconditions.checkNoOverflow(k < 31);
      return 1 << k;
    case -2: 
      MathPreconditions.checkNoOverflow(k < 32);
      return (k & 0x1) == 0 ? 1 << k : -1 << k;
    }
    
    
    int accum = 1;
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
        MathPreconditions.checkNoOverflow((-46340 <= b ? 1 : 0) & (b <= 46340 ? 1 : 0));
        b *= b;
      }
    }
  }
  






  @Beta
  public static int saturatedAdd(int a, int b)
  {
    return Ints.saturatedCast(a + b);
  }
  





  @Beta
  public static int saturatedSubtract(int a, int b)
  {
    return Ints.saturatedCast(a - b);
  }
  





  @Beta
  public static int saturatedMultiply(int a, int b)
  {
    return Ints.saturatedCast(a * b);
  }
  





  @Beta
  public static int saturatedPow(int b, int k)
  {
    MathPreconditions.checkNonNegative("exponent", k);
    switch (b) {
    case 0: 
      return k == 0 ? 1 : 0;
    case 1: 
      return 1;
    case -1: 
      return (k & 0x1) == 0 ? 1 : -1;
    case 2: 
      if (k >= 31) {
        return Integer.MAX_VALUE;
      }
      return 1 << k;
    case -2: 
      if (k >= 32) {
        return Integer.MAX_VALUE + (k & 0x1);
      }
      return (k & 0x1) == 0 ? 1 << k : -1 << k;
    }
    
    
    int accum = 1;
    
    int limit = Integer.MAX_VALUE + (b >>> 31 & k & 0x1);
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
        if (((-46340 > b ? 1 : 0) | (b > 46340 ? 1 : 0)) != 0) {
          return limit;
        }
        b *= b;
      }
    }
  }
  








  public static int factorial(int n)
  {
    MathPreconditions.checkNonNegative("n", n);
    return n < factorials.length ? factorials[n] : Integer.MAX_VALUE;
  }
  
  private static final int[] factorials = { 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600 };
  



















  @GwtIncompatible
  public static int binomial(int n, int k)
  {
    MathPreconditions.checkNonNegative("n", n);
    MathPreconditions.checkNonNegative("k", k);
    Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
    if (k > n >> 1) {
      k = n - k;
    }
    if ((k >= biggestBinomials.length) || (n > biggestBinomials[k])) {
      return Integer.MAX_VALUE;
    }
    switch (k) {
    case 0: 
      return 1;
    case 1: 
      return n;
    }
    long result = 1L;
    for (int i = 0; i < k; i++) {
      result *= (n - i);
      result /= (i + 1);
    }
    return (int)result;
  }
  


  @VisibleForTesting
  static int[] biggestBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33 };
  


























  public static int mean(int x, int y)
  {
    return (x & y) + ((x ^ y) >> 1);
  }
  











  @GwtIncompatible
  @Beta
  public static boolean isPrime(int n)
  {
    return LongMath.isPrime(n);
  }
  
  private IntMath() {}
}
