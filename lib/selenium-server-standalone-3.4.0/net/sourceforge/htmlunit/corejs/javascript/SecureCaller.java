package net.sourceforge.htmlunit.corejs.javascript;

import java.lang.ref.SoftReference;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Map;
import java.util.WeakHashMap;











public abstract class SecureCaller
{
  private static final byte[] secureCallerImplBytecode = ;
  




  private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers = new WeakHashMap();
  

  public SecureCaller() {}
  

  public abstract Object call(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  

  static Object callSecurely(final CodeSource codeSource, Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    Thread thread = Thread.currentThread();
    


    ClassLoader classLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run() {
        return val$thread.getContextClassLoader();
      }
    });
    
    synchronized (callers) {
      Map<ClassLoader, SoftReference<SecureCaller>> classLoaderMap = (Map)callers.get(codeSource);
      if (classLoaderMap == null) {
        classLoaderMap = new WeakHashMap();
        callers.put(codeSource, classLoaderMap);
      }
    }
    
    synchronized (classLoaderMap) { Map<ClassLoader, SoftReference<SecureCaller>> classLoaderMap;
      SoftReference<SecureCaller> ref = (SoftReference)classLoaderMap.get(classLoader);
      SecureCaller caller; SecureCaller caller; if (ref != null) {
        caller = (SecureCaller)ref.get();
      } else {
        caller = null;
      }
      if (caller == null)
      {
        try
        {
          caller = (SecureCaller)AccessController.doPrivileged(new PrivilegedExceptionAction()
          {
            public Object run() throws Exception
            {
              Class<?> thisClass = getClass();
              ClassLoader effectiveClassLoader; ClassLoader effectiveClassLoader; if (val$classLoader.loadClass(thisClass
                .getName()) != thisClass)
              {

                effectiveClassLoader = thisClass.getClassLoader();
              } else {
                effectiveClassLoader = val$classLoader;
              }
              SecureCaller.SecureClassLoaderImpl secCl = new SecureCaller.SecureClassLoaderImpl(effectiveClassLoader);
              
              Class<?> c = secCl.defineAndLinkClass(SecureCaller.class
                .getName() + "Impl", 
                
                SecureCaller.secureCallerImplBytecode, codeSource);
              
              return c.newInstance();
            }
          });
          classLoaderMap.put(classLoader, new SoftReference(caller));
        }
        catch (PrivilegedActionException ex) {
          throw new UndeclaredThrowableException(ex.getCause());
        } }
    }
    SecureCaller caller;
    return caller.call(callable, cx, scope, thisObj, args);
  }
  
  private static class SecureClassLoaderImpl extends SecureClassLoader {
    SecureClassLoaderImpl(ClassLoader parent) {
      super();
    }
    
    Class<?> defineAndLinkClass(String name, byte[] bytes, CodeSource cs) {
      Class<?> cl = defineClass(name, bytes, 0, bytes.length, cs);
      resolveClass(cl);
      return cl;
    }
  }
  
  private static byte[] loadBytecode() {
    
      (byte[])AccessController.doPrivileged(new PrivilegedAction() {
        public Object run() {
          return SecureCaller.access$100();
        }
      });
  }
  
  /* Error */
  private static byte[] loadBytecodePrivileged()
  {
    // Byte code:
    //   0: ldc 17
    //   2: ldc 30
    //   4: invokevirtual 31	java/lang/Class:getResource	(Ljava/lang/String;)Ljava/net/URL;
    //   7: astore_0
    //   8: aload_0
    //   9: invokevirtual 32	java/net/URL:openStream	()Ljava/io/InputStream;
    //   12: astore_1
    //   13: new 33	java/io/ByteArrayOutputStream
    //   16: dup
    //   17: invokespecial 34	java/io/ByteArrayOutputStream:<init>	()V
    //   20: astore_2
    //   21: aload_1
    //   22: invokevirtual 35	java/io/InputStream:read	()I
    //   25: istore_3
    //   26: iload_3
    //   27: iconst_m1
    //   28: if_icmpne +16 -> 44
    //   31: aload_2
    //   32: invokevirtual 36	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   35: astore 4
    //   37: aload_1
    //   38: invokevirtual 37	java/io/InputStream:close	()V
    //   41: aload 4
    //   43: areturn
    //   44: aload_2
    //   45: iload_3
    //   46: invokevirtual 38	java/io/ByteArrayOutputStream:write	(I)V
    //   49: goto -28 -> 21
    //   52: astore 5
    //   54: aload_1
    //   55: invokevirtual 37	java/io/InputStream:close	()V
    //   58: aload 5
    //   60: athrow
    //   61: astore_1
    //   62: new 23	java/lang/reflect/UndeclaredThrowableException
    //   65: dup
    //   66: aload_1
    //   67: invokespecial 25	java/lang/reflect/UndeclaredThrowableException:<init>	(Ljava/lang/Throwable;)V
    //   70: athrow
    // Line number table:
    //   Java source line #126	-> byte code offset #0
    //   Java source line #128	-> byte code offset #8
    //   Java source line #130	-> byte code offset #13
    //   Java source line #132	-> byte code offset #21
    //   Java source line #133	-> byte code offset #26
    //   Java source line #134	-> byte code offset #31
    //   Java source line #139	-> byte code offset #37
    //   Java source line #134	-> byte code offset #41
    //   Java source line #136	-> byte code offset #44
    //   Java source line #137	-> byte code offset #49
    //   Java source line #139	-> byte code offset #52
    //   Java source line #141	-> byte code offset #61
    //   Java source line #142	-> byte code offset #62
    // Local variable table:
    //   start	length	slot	name	signature
    //   7	2	0	url	java.net.URL
    //   12	43	1	in	java.io.InputStream
    //   61	6	1	e	java.io.IOException
    //   20	25	2	bout	java.io.ByteArrayOutputStream
    //   25	21	3	r	int
    //   52	7	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   13	37	52	finally
    //   44	54	52	finally
    //   8	41	61	java/io/IOException
    //   44	61	61	java/io/IOException
  }
}
