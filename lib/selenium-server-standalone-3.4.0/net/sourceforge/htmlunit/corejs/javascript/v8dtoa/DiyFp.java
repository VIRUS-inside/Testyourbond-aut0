package net.sourceforge.htmlunit.corejs.javascript.v8dtoa;








class DiyFp
{
  private long f;
  






  private int e;
  






  static final int kSignificandSize = 64;
  






  static final long kUint64MSB = Long.MIN_VALUE;
  







  DiyFp()
  {
    f = 0L;
    e = 0;
  }
  
  DiyFp(long f, int e) {
    this.f = f;
    this.e = e;
  }
  
  private static boolean uint64_gte(long a, long b)
  {
    if (a != b) {} return ((a > b ? 1 : 0) ^ (a < 0L ? 1 : 0) ^ (b < 0L ? 1 : 0)) != 0;
  }
  




  void subtract(DiyFp other)
  {
    assert (e == e);
    assert (uint64_gte(f, f));
    f -= f;
  }
  


  static DiyFp minus(DiyFp a, DiyFp b)
  {
    DiyFp result = new DiyFp(f, e);
    result.subtract(b);
    return result;
  }
  





  void multiply(DiyFp other)
  {
    long kM32 = 4294967295L;
    long a = f >>> 32;
    long b = f & 0xFFFFFFFF;
    long c = f >>> 32;
    long d = f & 0xFFFFFFFF;
    long ac = a * c;
    long bc = b * c;
    long ad = a * d;
    long bd = b * d;
    long tmp = (bd >>> 32) + (ad & 0xFFFFFFFF) + (bc & 0xFFFFFFFF);
    

    tmp += 2147483648L;
    long result_f = ac + (ad >>> 32) + (bc >>> 32) + (tmp >>> 32);
    e += e + 64;
    f = result_f;
  }
  
  static DiyFp times(DiyFp a, DiyFp b)
  {
    DiyFp result = new DiyFp(f, e);
    result.multiply(b);
    return result;
  }
  
  void normalize() {
    assert (this.f != 0L);
    long f = this.f;
    int e = this.e;
    



    long k10MSBits = -18014398509481984L;
    while ((f & 0xFFC0000000000000) == 0L) {
      f <<= 10;
      e -= 10;
    }
    while ((f & 0x8000000000000000) == 0L) {
      f <<= 1;
      e--;
    }
    this.f = f;
    this.e = e;
  }
  
  static DiyFp normalize(DiyFp a) {
    DiyFp result = new DiyFp(f, e);
    result.normalize();
    return result;
  }
  
  long f() {
    return f;
  }
  
  int e() {
    return e;
  }
  
  void setF(long new_value) {
    f = new_value;
  }
  
  void setE(int new_value) {
    e = new_value;
  }
  
  public String toString()
  {
    return "[DiyFp f:" + f + ", e:" + e + "]";
  }
}
