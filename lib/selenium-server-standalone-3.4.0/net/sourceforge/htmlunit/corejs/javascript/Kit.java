package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Map;














public class Kit
{
  private static Method Throwable_initCause = null;
  
  static
  {
    try {
      Class<?> ThrowableClass = classOrNull("java.lang.Throwable");
      Class<?>[] signature = { ThrowableClass };
      Throwable_initCause = ThrowableClass.getMethod("initCause", signature);
    }
    catch (Exception localException) {}
  }
  
  public static Class<?> classOrNull(String className)
  {
    try
    {
      return Class.forName(className);
    }
    catch (ClassNotFoundException localClassNotFoundException) {}catch (SecurityException localSecurityException) {}catch (LinkageError localLinkageError) {}catch (IllegalArgumentException localIllegalArgumentException) {}
    




    return null;
  }
  


  public static Class<?> classOrNull(ClassLoader loader, String className)
  {
    try
    {
      return loader.loadClass(className);
    }
    catch (ClassNotFoundException localClassNotFoundException) {}catch (SecurityException localSecurityException) {}catch (LinkageError localLinkageError) {}catch (IllegalArgumentException localIllegalArgumentException) {}
    




    return null;
  }
  
  static Object newInstanceOrNull(Class<?> cl) {
    try {
      return cl.newInstance();
    }
    catch (SecurityException localSecurityException) {}catch (LinkageError localLinkageError) {}catch (InstantiationException localInstantiationException) {}catch (IllegalAccessException localIllegalAccessException) {}
    


    return null;
  }
  


  static boolean testIfCanLoadRhinoClasses(ClassLoader loader)
  {
    Class<?> testClass = ScriptRuntime.ContextFactoryClass;
    Class<?> x = classOrNull(loader, testClass.getName());
    if (x != testClass)
    {



      return false;
    }
    return true;
  }
  






  public static RuntimeException initCause(RuntimeException ex, Throwable cause)
  {
    if (Throwable_initCause != null) {
      Object[] args = { cause };
      try {
        Throwable_initCause.invoke(ex, args);
      }
      catch (Exception localException) {}
    }
    
    return ex;
  }
  





  public static int xDigitToInt(int c, int accumulator)
  {
    if (c <= 57) {
      c -= 48;
      if (0 <= c) {
        break label55;
      }
    } else if (c <= 70) {
      if (65 <= c) {
        c -= 55;
        break label55;
      }
    } else if ((c <= 102) && 
      (97 <= c)) {
      c -= 87;
      
      break label55;
    }
    return -1;
    label55:
    return accumulator << 4 | c;
  }
  


















































  public static Object addListener(Object bag, Object listener)
  {
    if (listener == null)
      throw new IllegalArgumentException();
    if ((listener instanceof Object[])) {
      throw new IllegalArgumentException();
    }
    if (bag == null) {
      bag = listener;
    } else if (!(bag instanceof Object[])) {
      bag = new Object[] { bag, listener };
    } else {
      Object[] array = (Object[])bag;
      int L = array.length;
      
      if (L < 2)
        throw new IllegalArgumentException();
      Object[] tmp = new Object[L + 1];
      System.arraycopy(array, 0, tmp, 0, L);
      tmp[L] = listener;
      bag = tmp;
    }
    
    return bag;
  }
  
















  public static Object removeListener(Object bag, Object listener)
  {
    if (listener == null)
      throw new IllegalArgumentException();
    if ((listener instanceof Object[])) {
      throw new IllegalArgumentException();
    }
    if (bag == listener) {
      bag = null;
    } else if ((bag instanceof Object[])) {
      Object[] array = (Object[])bag;
      int L = array.length;
      
      if (L < 2)
        throw new IllegalArgumentException();
      if (L == 2) {
        if (array[1] == listener) {
          bag = array[0];
        } else if (array[0] == listener) {
          bag = array[1];
        }
      } else {
        int i = L;
        do {
          i--;
          if (array[i] == listener) {
            Object[] tmp = new Object[L - 1];
            System.arraycopy(array, 0, tmp, 0, i);
            System.arraycopy(array, i + 1, tmp, i, L - (i + 1));
            bag = tmp;
            break;
          }
        } while (i != 0);
      }
    }
    
    return bag;
  }
  













  public static Object getListener(Object bag, int index)
  {
    if (index == 0) {
      if (bag == null)
        return null;
      if (!(bag instanceof Object[]))
        return bag;
      Object[] array = (Object[])bag;
      
      if (array.length < 2)
        throw new IllegalArgumentException();
      return array[0]; }
    if (index == 1) {
      if (!(bag instanceof Object[])) {
        if (bag == null)
          throw new IllegalArgumentException();
        return null;
      }
      Object[] array = (Object[])bag;
      
      return array[1];
    }
    
    Object[] array = (Object[])bag;
    int L = array.length;
    if (L < 2)
      throw new IllegalArgumentException();
    if (index == L)
      return null;
    return array[index];
  }
  

  static Object initHash(Map<Object, Object> h, Object key, Object initialValue)
  {
    synchronized (h) {
      Object current = h.get(key);
      if (current == null) {
        h.put(key, initialValue);
      } else {
        initialValue = current;
      }
    }
    return initialValue;
  }
  
  private static final class ComplexKey {
    private Object key1;
    private Object key2;
    private int hash;
    
    ComplexKey(Object key1, Object key2) {
      this.key1 = key1;
      this.key2 = key2;
    }
    
    public boolean equals(Object anotherObj)
    {
      if (!(anotherObj instanceof ComplexKey))
        return false;
      ComplexKey another = (ComplexKey)anotherObj;
      return (key1.equals(key1)) && (key2.equals(key2));
    }
    
    public int hashCode()
    {
      if (hash == 0) {
        hash = (key1.hashCode() ^ key2.hashCode());
      }
      return hash;
    }
  }
  
  public static Object makeHashKeyFromPair(Object key1, Object key2) {
    if (key1 == null)
      throw new IllegalArgumentException();
    if (key2 == null)
      throw new IllegalArgumentException();
    return new ComplexKey(key1, key2);
  }
  
  public static String readReader(Reader r) throws IOException {
    char[] buffer = new char['Ȁ'];
    int cursor = 0;
    for (;;) {
      int n = r.read(buffer, cursor, buffer.length - cursor);
      if (n < 0) {
        break;
      }
      cursor += n;
      if (cursor == buffer.length) {
        char[] tmp = new char[buffer.length * 2];
        System.arraycopy(buffer, 0, tmp, 0, cursor);
        buffer = tmp;
      }
    }
    return new String(buffer, 0, cursor);
  }
  
  public static byte[] readStream(InputStream is, int initialBufferCapacity) throws IOException
  {
    if (initialBufferCapacity <= 0) {
      throw new IllegalArgumentException("Bad initialBufferCapacity: " + initialBufferCapacity);
    }
    
    byte[] buffer = new byte[initialBufferCapacity];
    int cursor = 0;
    for (;;) {
      int n = is.read(buffer, cursor, buffer.length - cursor);
      if (n < 0) {
        break;
      }
      cursor += n;
      if (cursor == buffer.length) {
        byte[] tmp = new byte[buffer.length * 2];
        System.arraycopy(buffer, 0, tmp, 0, cursor);
        buffer = tmp;
      }
    }
    if (cursor != buffer.length) {
      byte[] tmp = new byte[cursor];
      System.arraycopy(buffer, 0, tmp, 0, cursor);
      buffer = tmp;
    }
    return buffer;
  }
  




  public static RuntimeException codeBug()
    throws RuntimeException
  {
    RuntimeException ex = new IllegalStateException("FAILED ASSERTION");
    
    ex.printStackTrace(System.err);
    throw ex;
  }
  




  public static RuntimeException codeBug(String msg)
    throws RuntimeException
  {
    msg = "FAILED ASSERTION: " + msg;
    RuntimeException ex = new IllegalStateException(msg);
    
    ex.printStackTrace(System.err);
    throw ex;
  }
  
  public Kit() {}
}
