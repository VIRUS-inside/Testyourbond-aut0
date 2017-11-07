package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;









































@GwtIncompatible
public class AtomicDoubleArray
  implements Serializable
{
  private static final long serialVersionUID = 0L;
  private transient AtomicLongArray longs;
  
  public AtomicDoubleArray(int length)
  {
    longs = new AtomicLongArray(length);
  }
  






  public AtomicDoubleArray(double[] array)
  {
    int len = array.length;
    long[] longArray = new long[len];
    for (int i = 0; i < len; i++) {
      longArray[i] = Double.doubleToRawLongBits(array[i]);
    }
    longs = new AtomicLongArray(longArray);
  }
  




  public final int length()
  {
    return longs.length();
  }
  





  public final double get(int i)
  {
    return Double.longBitsToDouble(longs.get(i));
  }
  





  public final void set(int i, double newValue)
  {
    long next = Double.doubleToRawLongBits(newValue);
    longs.set(i, next);
  }
  





  public final void lazySet(int i, double newValue)
  {
    set(i, newValue);
  }
  










  public final double getAndSet(int i, double newValue)
  {
    long next = Double.doubleToRawLongBits(newValue);
    return Double.longBitsToDouble(longs.getAndSet(i, next));
  }
  











  public final boolean compareAndSet(int i, double expect, double update)
  {
    return longs.compareAndSet(i, 
      Double.doubleToRawLongBits(expect), 
      Double.doubleToRawLongBits(update));
  }
  
















  public final boolean weakCompareAndSet(int i, double expect, double update)
  {
    return longs.weakCompareAndSet(i, 
      Double.doubleToRawLongBits(expect), 
      Double.doubleToRawLongBits(update));
  }
  





  @CanIgnoreReturnValue
  public final double getAndAdd(int i, double delta)
  {
    for (;;)
    {
      long current = longs.get(i);
      double currentVal = Double.longBitsToDouble(current);
      double nextVal = currentVal + delta;
      long next = Double.doubleToRawLongBits(nextVal);
      if (longs.compareAndSet(i, current, next)) {
        return currentVal;
      }
    }
  }
  





  @CanIgnoreReturnValue
  public double addAndGet(int i, double delta)
  {
    for (;;)
    {
      long current = longs.get(i);
      double currentVal = Double.longBitsToDouble(current);
      double nextVal = currentVal + delta;
      long next = Double.doubleToRawLongBits(nextVal);
      if (longs.compareAndSet(i, current, next)) {
        return nextVal;
      }
    }
  }
  



  public String toString()
  {
    int iMax = length() - 1;
    if (iMax == -1) {
      return "[]";
    }
    

    StringBuilder b = new StringBuilder(19 * (iMax + 1));
    b.append('[');
    for (int i = 0;; i++) {
      b.append(Double.longBitsToDouble(longs.get(i)));
      if (i == iMax) {
        return ']';
      }
      b.append(',').append(' ');
    }
  }
  





  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    s.defaultWriteObject();
    

    int length = length();
    s.writeInt(length);
    

    for (int i = 0; i < length; i++) {
      s.writeDouble(get(i));
    }
  }
  


  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    s.defaultReadObject();
    

    int length = s.readInt();
    longs = new AtomicLongArray(length);
    

    for (int i = 0; i < length; i++) {
      set(i, s.readDouble());
    }
  }
}
