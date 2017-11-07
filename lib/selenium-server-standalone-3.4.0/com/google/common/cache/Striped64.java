package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;































































@GwtIncompatible
abstract class Striped64
  extends Number
{
  static final class Cell
  {
    volatile long p0;
    volatile long p1;
    volatile long p2;
    volatile long p3;
    volatile long p4;
    volatile long p5;
    volatile long p6;
    volatile long value;
    volatile long q0;
    volatile long q1;
    volatile long q2;
    volatile long q3;
    volatile long q4;
    volatile long q5;
    volatile long q6;
    private static final Unsafe UNSAFE;
    private static final long valueOffset;
    
    Cell(long x)
    {
      value = x;
    }
    
    final boolean cas(long cmp, long val) { return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val); }
    


    static
    {
      try
      {
        UNSAFE = Striped64.access$000();
        Class<?> ak = Cell.class;
        
        valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
      } catch (Exception e) {
        throw new Error(e);
      }
    }
  }
  







  static final ThreadLocal<int[]> threadHashCode = new ThreadLocal();
  



  static final Random rng = new Random();
  

  static final int NCPU = Runtime.getRuntime().availableProcessors();
  


  volatile transient Cell[] cells;
  


  volatile transient long base;
  


  volatile transient int busy;
  


  private static final Unsafe UNSAFE;
  

  private static final long baseOffset;
  

  private static final long busyOffset;
  


  final boolean casBase(long cmp, long val)
  {
    return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
  }
  


  final boolean casBusy()
  {
    return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
  }
  







  final void retryUpdate(long x, int[] hc, boolean wasUncontended)
  {
    int h;
    






    int h;
    





    if (hc == null) {
      threadHashCode.set(hc = new int[1]);
      int r = rng.nextInt();
      h = hc[0] = r == 0 ? 1 : r;
    }
    else {
      h = hc[0]; }
    boolean collide = false;
    for (;;) { Cell[] as;
      int n;
      if (((as = cells) != null) && ((n = as.length) > 0)) { Cell a;
        if ((a = as[(n - 1 & h)]) == null) {
          if (busy == 0) {
            Cell r = new Cell(x);
            if ((busy == 0) && (casBusy())) {
              boolean created = false;
              try { Cell[] rs;
                int m;
                int j; if (((rs = cells) != null) && ((m = rs.length) > 0) && (rs[(j = m - 1 & h)] == null))
                {

                  rs[j] = r;
                  created = true;
                }
              } finally {
                busy = 0;
              }
              if (!created) continue;
              break;
            }
          }
          
          collide = false;
        }
        else if (!wasUncontended) {
          wasUncontended = true; } else { long v;
          if (a.cas(v = value, fn(v, x)))
            break;
          if ((n >= NCPU) || (cells != as)) {
            collide = false;
          } else if (!collide) {
            collide = true;
          } else if ((busy == 0) && (casBusy())) {
            try {
              if (cells == as) {
                Cell[] rs = new Cell[n << 1];
                for (int i = 0; i < n; i++)
                  rs[i] = as[i];
                cells = rs;
              }
            } finally {
              busy = 0;
            }
            collide = false;
            continue;
          } }
        h ^= h << 13;
        h ^= h >>> 17;
        h ^= h << 5;
        hc[0] = h;
      }
      else if ((busy == 0) && (cells == as) && (casBusy())) {
        boolean init = false;
        try {
          if (cells == as) {
            Cell[] rs = new Cell[2];
            rs[(h & 0x1)] = new Cell(x);
            cells = rs;
            init = true;
          }
        } finally {
          busy = 0;
        }
        if (init)
          break;
      } else { long v;
        if (casBase(v = base, fn(v, x))) {
          break;
        }
      }
    }
  }
  
  final void internalReset(long initialValue)
  {
    Cell[] as = cells;
    base = initialValue;
    if (as != null) {
      int n = as.length;
      for (int i = 0; i < n; i++) {
        Cell a = as[i];
        if (a != null) {
          value = initialValue;
        }
      }
    }
  }
  

  static
  {
    try
    {
      UNSAFE = getUnsafe();
      Class<?> sk = Striped64.class;
      
      baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
      
      busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
    } catch (Exception e) {
      throw new Error(e);
    }
  }
  





  private static Unsafe getUnsafe()
  {
    try
    {
      return Unsafe.getUnsafe();
    } catch (SecurityException localSecurityException) {
      try {
        
          (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Unsafe run() throws Exception {
              Class<Unsafe> k = Unsafe.class;
              for (Field f : k.getDeclaredFields()) {
                f.setAccessible(true);
                Object x = f.get(null);
                if (k.isInstance(x))
                  return (Unsafe)k.cast(x);
              }
              throw new NoSuchFieldError("the Unsafe");
            }
          });
      } catch (PrivilegedActionException e) {
        throw new RuntimeException("Could not initialize intrinsics", e.getCause());
      }
    }
  }
  
  Striped64() {}
  
  abstract long fn(long paramLong1, long paramLong2);
}
