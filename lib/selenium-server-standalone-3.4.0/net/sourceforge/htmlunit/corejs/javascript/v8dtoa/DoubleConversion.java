package net.sourceforge.htmlunit.corejs.javascript.v8dtoa;




public final class DoubleConversion
{
  private static final long kSignMask = Long.MIN_VALUE;
  


  private static final long kExponentMask = 9218868437227405312L;
  


  private static final long kSignificandMask = 4503599627370495L;
  


  private static final long kHiddenBit = 4503599627370496L;
  


  private static final int kPhysicalSignificandSize = 52;
  


  private static final int kSignificandSize = 53;
  


  private static final int kExponentBias = 1075;
  


  private static final int kDenormalExponent = -1074;
  



  private DoubleConversion() {}
  



  private static int exponent(long d64)
  {
    if (isDenormal(d64)) {
      return 64462;
    }
    int biased_e = (int)((d64 & 0x7FF0000000000000) >> 52);
    
    return biased_e - 1075;
  }
  
  private static long significand(long d64) {
    long significand = d64 & 0xFFFFFFFFFFFFF;
    if (!isDenormal(d64)) {
      return significand + 4503599627370496L;
    }
    return significand;
  }
  

  private static boolean isDenormal(long d64)
  {
    return (d64 & 0x7FF0000000000000) == 0L;
  }
  
  private static int sign(long d64) {
    return (d64 & 0x8000000000000000) == 0L ? 1 : -1;
  }
  
  public static int doubleToInt32(double x) {
    int i = (int)x;
    if (i == x) {
      return i;
    }
    long d64 = Double.doubleToLongBits(x);
    int exponent = exponent(d64);
    if ((exponent <= -53) || (exponent > 31)) {
      return 0;
    }
    long s = significand(d64);
    return sign(d64) * (int)(exponent < 0 ? s >> -exponent : s << exponent);
  }
}
