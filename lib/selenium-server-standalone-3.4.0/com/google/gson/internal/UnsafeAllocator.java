package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;





















public abstract class UnsafeAllocator
{
  public UnsafeAllocator() {}
  
  public abstract <T> T newInstance(Class<T> paramClass)
    throws Exception;
  
  public static UnsafeAllocator create()
  {
    try
    {
      Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
      Field f = unsafeClass.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      final Object unsafe = f.get(null);
      Method allocateInstance = unsafeClass.getMethod("allocateInstance", new Class[] { Class.class });
      new UnsafeAllocator()
      {
        public <T> T newInstance(Class<T> c) throws Exception
        {
          UnsafeAllocator.assertInstantiable(c);
          return val$allocateInstance.invoke(unsafe, new Object[] { c });
        }
        

      };

    }
    catch (Exception localException)
    {

      try
      {

        Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
        getConstructorId.setAccessible(true);
        final int constructorId = ((Integer)getConstructorId.invoke(null, new Object[] { Object.class })).intValue();
        
        Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Integer.TYPE });
        newInstance.setAccessible(true);
        new UnsafeAllocator()
        {
          public <T> T newInstance(Class<T> c) throws Exception
          {
            UnsafeAllocator.assertInstantiable(c);
            return val$newInstance.invoke(null, new Object[] { c, Integer.valueOf(constructorId) });
          }
          

        };

      }
      catch (Exception localException1)
      {

        try
        {

          Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Class.class });
          newInstance.setAccessible(true);
          new UnsafeAllocator()
          {
            public <T> T newInstance(Class<T> c) throws Exception
            {
              UnsafeAllocator.assertInstantiable(c);
              return val$newInstance.invoke(null, new Object[] { c, Object.class });
            }
          };
        }
        catch (Exception localException2) {}
      }
    }
    new UnsafeAllocator()
    {
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c);
      }
    };
  }
  




  private static void assertInstantiable(Class<?> c)
  {
    int modifiers = c.getModifiers();
    if (Modifier.isInterface(modifiers)) {
      throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + c.getName());
    }
    if (Modifier.isAbstract(modifiers)) {
      throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + c.getName());
    }
  }
}
