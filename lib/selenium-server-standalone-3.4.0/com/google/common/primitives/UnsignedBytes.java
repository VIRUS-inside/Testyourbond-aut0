package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import sun.misc.Unsafe;













































@GwtIncompatible
public final class UnsignedBytes
{
  public static final byte MAX_POWER_OF_TWO = -128;
  public static final byte MAX_VALUE = -1;
  private static final int UNSIGNED_MASK = 255;
  
  private UnsignedBytes() {}
  
  public static int toInt(byte value)
  {
    return value & 0xFF;
  }
  







  @CanIgnoreReturnValue
  public static byte checkedCast(long value)
  {
    Preconditions.checkArgument(value >> 8 == 0L, "out of range: %s", value);
    return (byte)(int)value;
  }
  







  public static byte saturatedCast(long value)
  {
    if (value > toInt((byte)-1)) {
      return -1;
    }
    if (value < 0L) {
      return 0;
    }
    return (byte)(int)value;
  }
  









  public static int compare(byte a, byte b)
  {
    return toInt(a) - toInt(b);
  }
  







  public static byte min(byte... array)
  {
    Preconditions.checkArgument(array.length > 0);
    int min = toInt(array[0]);
    for (int i = 1; i < array.length; i++) {
      int next = toInt(array[i]);
      if (next < min) {
        min = next;
      }
    }
    return (byte)min;
  }
  







  public static byte max(byte... array)
  {
    Preconditions.checkArgument(array.length > 0);
    int max = toInt(array[0]);
    for (int i = 1; i < array.length; i++) {
      int next = toInt(array[i]);
      if (next > max) {
        max = next;
      }
    }
    return (byte)max;
  }
  




  @Beta
  public static String toString(byte x)
  {
    return toString(x, 10);
  }
  









  @Beta
  public static String toString(byte x, int radix)
  {
    Preconditions.checkArgument((radix >= 2) && (radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
    



    return Integer.toString(toInt(x), radix);
  }
  








  @Beta
  @CanIgnoreReturnValue
  public static byte parseUnsignedByte(String string)
  {
    return parseUnsignedByte(string, 10);
  }
  











  @Beta
  @CanIgnoreReturnValue
  public static byte parseUnsignedByte(String string, int radix)
  {
    int parse = Integer.parseInt((String)Preconditions.checkNotNull(string), radix);
    
    if (parse >> 8 == 0) {
      return (byte)parse;
    }
    throw new NumberFormatException("out of range: " + parse);
  }
  









  public static String join(String separator, byte... array)
  {
    Preconditions.checkNotNull(separator);
    if (array.length == 0) {
      return "";
    }
    

    StringBuilder builder = new StringBuilder(array.length * (3 + separator.length()));
    builder.append(toInt(array[0]));
    for (int i = 1; i < array.length; i++) {
      builder.append(separator).append(toString(array[i]));
    }
    return builder.toString();
  }
  













  public static Comparator<byte[]> lexicographicalComparator()
  {
    return LexicographicalComparatorHolder.BEST_COMPARATOR;
  }
  
  @VisibleForTesting
  static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
    return UnsignedBytes.LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
  }
  






  @VisibleForTesting
  static class LexicographicalComparatorHolder
  {
    static final String UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class
      .getName() + "$UnsafeComparator";
    
    static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();
    LexicographicalComparatorHolder() {}
    
    @VisibleForTesting
    static enum UnsafeComparator implements Comparator<byte[]> { INSTANCE;
      
      static { BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        






















        theUnsafe = getUnsafe();
        
        BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset([B.class);
        

        if (theUnsafe.arrayIndexScale([B.class) != 1) {
          throw new AssertionError();
        }
      }
      

      static final boolean BIG_ENDIAN;
      static final Unsafe theUnsafe;
      static final int BYTE_ARRAY_BASE_OFFSET;
      private static Unsafe getUnsafe()
      {
        try
        {
          return Unsafe.getUnsafe();
        }
        catch (SecurityException localSecurityException)
        {
          try {
            (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
            {
              public Unsafe run() throws Exception
              {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                  f.setAccessible(true);
                  Object x = f.get(null);
                  if (k.isInstance(x)) {
                    return (Unsafe)k.cast(x);
                  }
                }
                throw new NoSuchFieldError("the Unsafe");
              }
            });
          } catch (PrivilegedActionException e) {
            throw new RuntimeException("Could not initialize intrinsics", e.getCause());
          }
        }
      }
      
      public int compare(byte[] left, byte[] right) {
        int minLength = Math.min(left.length, right.length);
        int minWords = minLength / 8;
        





        for (int i = 0; i < minWords * 8; i += 8) {
          long lw = theUnsafe.getLong(left, BYTE_ARRAY_BASE_OFFSET + i);
          long rw = theUnsafe.getLong(right, BYTE_ARRAY_BASE_OFFSET + i);
          if (lw != rw) {
            if (BIG_ENDIAN) {
              return UnsignedLongs.compare(lw, rw);
            }
            







            int n = Long.numberOfTrailingZeros(lw ^ rw) & 0xFFFFFFF8;
            return (int)(lw >>> n & 0xFF) - (int)(rw >>> n & 0xFF);
          }
        }
        

        for (int i = minWords * 8; i < minLength; i++) {
          int result = UnsignedBytes.compare(left[i], right[i]);
          if (result != 0) {
            return result;
          }
        }
        return left.length - right.length;
      }
      


      public String toString() { return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)"; }
      
      private UnsafeComparator() {}
    }
    
    static enum PureJavaComparator implements Comparator<byte[]> { INSTANCE;
      
      private PureJavaComparator() {}
      
      public int compare(byte[] left, byte[] right) { int minLength = Math.min(left.length, right.length);
        for (int i = 0; i < minLength; i++) {
          int result = UnsignedBytes.compare(left[i], right[i]);
          if (result != 0) {
            return result;
          }
        }
        return left.length - right.length;
      }
      
      public String toString()
      {
        return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
      }
    }
    


    static Comparator<byte[]> getBestComparator()
    {
      try
      {
        Class<?> theClass = Class.forName(UNSAFE_COMPARATOR_NAME);
        


        return (Comparator)theClass.getEnumConstants()[0];
      }
      catch (Throwable t) {}
      return UnsignedBytes.lexicographicalComparatorJavaImpl();
    }
  }
}
