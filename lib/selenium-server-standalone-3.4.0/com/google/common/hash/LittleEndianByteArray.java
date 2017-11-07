package com.google.common.hash;

import com.google.common.primitives.Longs;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;


























final class LittleEndianByteArray
{
  private static final LittleEndianBytes byteArray;
  
  static long load64(byte[] input, int offset)
  {
    assert (input.length >= offset + 8);
    
    return byteArray.getLongLittleEndian(input, offset);
  }
  









  static long load64Safely(byte[] input, int offset, int length)
  {
    long result = 0L;
    



    int limit = Math.min(length, 8);
    for (int i = 0; i < limit; i++)
    {
      result |= (input[(offset + i)] & 0xFF) << i * 8;
    }
    return result;
  }
  







  static void store64(byte[] sink, int offset, long value)
  {
    assert ((offset >= 0) && (offset + 8 <= sink.length));
    
    byteArray.putLongLittleEndian(sink, offset, value);
  }
  







  static int load32(byte[] source, int offset)
  {
    return source[offset] & 0xFF | (source[(offset + 1)] & 0xFF) << 8 | (source[(offset + 2)] & 0xFF) << 16 | (source[(offset + 3)] & 0xFF) << 24;
  }
  







  static boolean usingUnsafe()
  {
    return byteArray instanceof UnsafeByteArray;
  }
  


  private LittleEndianByteArray() {}
  


  private static abstract interface LittleEndianBytes
  {
    public abstract long getLongLittleEndian(byte[] paramArrayOfByte, int paramInt);
    


    public abstract void putLongLittleEndian(byte[] paramArrayOfByte, int paramInt, long paramLong);
  }
  

  private static abstract enum UnsafeByteArray
    implements LittleEndianByteArray.LittleEndianBytes
  {
    UNSAFE_LITTLE_ENDIAN, 
    









    UNSAFE_BIG_ENDIAN;
    





    private static final Unsafe theUnsafe;
    




    private static final int BYTE_ARRAY_BASE_OFFSET;
    





    private UnsafeByteArray() {}
    




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
    
    static { theUnsafe = getUnsafe();
      BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset([B.class);
      

      if (theUnsafe.arrayIndexScale([B.class) != 1) {
        throw new AssertionError();
      }
    }
  }
  

  private static abstract enum JavaLittleEndianBytes
    implements LittleEndianByteArray.LittleEndianBytes
  {
    INSTANCE;
    









    private JavaLittleEndianBytes() {}
  }
  









  static
  {
    LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
    








    try
    {
      String arch = System.getProperty("os.arch");
      if (("amd64".equals(arch)) || ("aarch64".equals(arch)))
      {
        theGetter = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
      }
    }
    catch (Throwable localThrowable) {}
    


    byteArray = theGetter;
  }
}
