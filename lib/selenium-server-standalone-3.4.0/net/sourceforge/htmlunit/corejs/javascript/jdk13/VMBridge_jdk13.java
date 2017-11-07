package net.sourceforge.htmlunit.corejs.javascript.jdk13;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.InterfaceAdapter;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.VMBridge;

public class VMBridge_jdk13
  extends VMBridge
{
  private ThreadLocal<Object[]> contextLocal = new ThreadLocal();
  




  public VMBridge_jdk13() {}
  



  protected Object getThreadContextHelper()
  {
    Object[] storage = (Object[])contextLocal.get();
    if (storage == null) {
      storage = new Object[1];
      contextLocal.set(storage);
    }
    return storage;
  }
  
  protected Context getContext(Object contextHelper)
  {
    Object[] storage = (Object[])contextHelper;
    return (Context)storage[0];
  }
  
  protected void setContext(Object contextHelper, Context cx)
  {
    Object[] storage = (Object[])contextHelper;
    storage[0] = cx;
  }
  
  protected ClassLoader getCurrentThreadClassLoader()
  {
    return Thread.currentThread().getContextClassLoader();
  }
  
  protected boolean tryToMakeAccessible(Object accessibleObject)
  {
    if (!(accessibleObject instanceof AccessibleObject)) {
      return false;
    }
    AccessibleObject accessible = (AccessibleObject)accessibleObject;
    if (accessible.isAccessible()) {
      return true;
    }
    try {
      accessible.setAccessible(true);
    }
    catch (Exception localException) {}
    
    return accessible.isAccessible();
  }
  



  protected Object getInterfaceProxyHelper(ContextFactory cf, Class<?>[] interfaces)
  {
    ClassLoader loader = interfaces[0].getClassLoader();
    Class<?> cl = Proxy.getProxyClass(loader, interfaces);
    try
    {
      c = cl.getConstructor(new Class[] { InvocationHandler.class });
    } catch (NoSuchMethodException ex) {
      Constructor<?> c;
      throw Kit.initCause(new IllegalStateException(), ex); }
    Constructor<?> c;
    return c;
  }
  


  protected Object newInterfaceProxy(Object proxyHelper, final ContextFactory cf, final InterfaceAdapter adapter, final Object target, final Scriptable topScope)
  {
    Constructor<?> c = (Constructor)proxyHelper;
    
    InvocationHandler handler = new InvocationHandler()
    {

      public Object invoke(Object proxy, Method method, Object[] args)
      {
        if (method.getDeclaringClass() == Object.class) {
          String methodName = method.getName();
          if (methodName.equals("equals")) {
            Object other = args[0];
            







            return Boolean.valueOf(proxy == other);
          }
          if (methodName.equals("hashCode")) {
            return Integer.valueOf(target.hashCode());
          }
          if (methodName.equals("toString")) {
            return "Proxy[" + target.toString() + "]";
          }
        }
        return adapter.invoke(cf, target, topScope, proxy, method, args);
      }
    };
    
    try
    {
      proxy = c.newInstance(new Object[] { handler });
    } catch (InvocationTargetException ex) { Object proxy;
      throw Context.throwAsScriptRuntimeEx(ex);
    }
    catch (IllegalAccessException ex) {
      throw Kit.initCause(new IllegalStateException(), ex);
    }
    catch (InstantiationException ex) {
      throw Kit.initCause(new IllegalStateException(), ex); }
    Object proxy;
    return proxy;
  }
  
  protected boolean isVarArgs(Member member)
  {
    return false;
  }
}
