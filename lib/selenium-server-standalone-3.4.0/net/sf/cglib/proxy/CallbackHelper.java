package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.cglib.core.ReflectUtils;
















public abstract class CallbackHelper
  implements CallbackFilter
{
  private Map methodMap = new HashMap();
  private List callbacks = new ArrayList();
  
  public CallbackHelper(Class superclass, Class[] interfaces)
  {
    List methods = new ArrayList();
    Enhancer.getMethods(superclass, interfaces, methods);
    Map indexes = new HashMap();
    int i = 0; for (int size = methods.size(); i < size; i++) {
      Method method = (Method)methods.get(i);
      Object callback = getCallback(method);
      if (callback == null)
        throw new IllegalStateException("getCallback cannot return null");
      boolean isCallback = callback instanceof Callback;
      if ((!isCallback) && (!(callback instanceof Class)))
        throw new IllegalStateException("getCallback must return a Callback or a Class");
      if ((i > 0) && ((callbacks.get(i - 1) instanceof Callback ^ isCallback)))
        throw new IllegalStateException("getCallback must return a Callback or a Class consistently for every Method");
      Integer index = (Integer)indexes.get(callback);
      if (index == null) {
        index = new Integer(callbacks.size());
        indexes.put(callback, index);
      }
      methodMap.put(method, index);
      callbacks.add(callback);
    }
  }
  
  protected abstract Object getCallback(Method paramMethod);
  
  public Callback[] getCallbacks()
  {
    if (callbacks.size() == 0)
      return new Callback[0];
    if ((callbacks.get(0) instanceof Callback)) {
      return (Callback[])callbacks.toArray(new Callback[callbacks.size()]);
    }
    throw new IllegalStateException("getCallback returned classes, not callbacks; call getCallbackTypes instead");
  }
  

  public Class[] getCallbackTypes()
  {
    if (callbacks.size() == 0)
      return new Class[0];
    if ((callbacks.get(0) instanceof Callback)) {
      return ReflectUtils.getClasses(getCallbacks());
    }
    return (Class[])callbacks.toArray(new Class[callbacks.size()]);
  }
  

  public int accept(Method method)
  {
    return ((Integer)methodMap.get(method)).intValue();
  }
  
  public int hashCode()
  {
    return methodMap.hashCode();
  }
  
  public boolean equals(Object o)
  {
    if (o == null)
      return false;
    if (!(o instanceof CallbackHelper))
      return false;
    return methodMap.equals(methodMap);
  }
}
