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
import net.sourceforge.htmlunit.corejs.classfile.ClassFileWriter;


















public class PolicySecurityController
  extends SecurityController
{
  private static final byte[] secureCallerImplBytecode = ;
  




  private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers = new WeakHashMap();
  
  public PolicySecurityController() {}
  
  public Class<?> getStaticSecurityDomainClassInternal() { return CodeSource.class; }
  
  public static abstract class SecureCaller { public SecureCaller() {}
    
    public abstract Object call(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject); }
  
  private static class Loader extends SecureClassLoader implements GeneratedClassLoader { private final CodeSource codeSource;
    
    Loader(ClassLoader parent, CodeSource codeSource) { super();
      this.codeSource = codeSource;
    }
    
    public Class<?> defineClass(String name, byte[] data) {
      return defineClass(name, data, 0, data.length, codeSource);
    }
    
    public void linkClass(Class<?> cl) {
      resolveClass(cl);
    }
  }
  

  public GeneratedClassLoader createClassLoader(final ClassLoader parent, final Object securityDomain)
  {
    
      (Loader)AccessController.doPrivileged(new PrivilegedAction() {
        public Object run() {
          return new PolicySecurityController.Loader(parent, (CodeSource)securityDomain);
        }
      });
  }
  


  public Object getDynamicSecurityDomain(Object securityDomain)
  {
    return securityDomain;
  }
  





  public Object callWithDomain(Object securityDomain, final Context cx, Callable callable, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    final ClassLoader classLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
      public Object run() {
        return cx.getApplicationClassLoader();
      }
    });
    final CodeSource codeSource = (CodeSource)securityDomain;
    
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
            public Object run() throws Exception {
              PolicySecurityController.Loader loader = new PolicySecurityController.Loader(classLoader, codeSource);
              
              Class<?> c = loader.defineClass(PolicySecurityController.SecureCaller.class
                .getName() + "Impl", 
                
                PolicySecurityController.secureCallerImplBytecode);
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
  




  private static byte[] loadBytecode()
  {
    String secureCallerClassName = SecureCaller.class.getName();
    ClassFileWriter cfw = new ClassFileWriter(secureCallerClassName + "Impl", secureCallerClassName, "<generated>");
    

    cfw.startMethod("<init>", "()V", (short)1);
    cfw.addALoad(0);
    cfw.addInvoke(183, secureCallerClassName, "<init>", "()V");
    
    cfw.add(177);
    cfw.stopMethod((short)1);
    String callableCallSig = "Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;";
    



    cfw.startMethod("call", "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;" + callableCallSig, (short)17);
    



    for (int i = 1; i < 6; i++) {
      cfw.addALoad(i);
    }
    cfw.addInvoke(185, "net/sourceforge/htmlunit/corejs/javascript/Callable", "call", "(" + callableCallSig);
    

    cfw.add(176);
    cfw.stopMethod((short)6);
    return cfw.toByteArray();
  }
}
