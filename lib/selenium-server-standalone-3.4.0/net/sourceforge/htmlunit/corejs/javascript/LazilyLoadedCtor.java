package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;












public final class LazilyLoadedCtor
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final int STATE_BEFORE_INIT = 0;
  private static final int STATE_INITIALIZING = 1;
  private static final int STATE_WITH_VALUE = 2;
  private final ScriptableObject scope;
  private final String propertyName;
  private final String className;
  private final boolean sealed;
  private final boolean privileged;
  private Object initializedValue;
  private int state;
  
  public LazilyLoadedCtor(ScriptableObject scope, String propertyName, String className, boolean sealed)
  {
    this(scope, propertyName, className, sealed, false);
  }
  

  LazilyLoadedCtor(ScriptableObject scope, String propertyName, String className, boolean sealed, boolean privileged)
  {
    this.scope = scope;
    this.propertyName = propertyName;
    this.className = className;
    this.sealed = sealed;
    this.privileged = privileged;
    state = 0;
    
    scope.addLazilyInitializedValue(propertyName, 0, this, 2);
  }
  
  void init()
  {
    synchronized (this) {
      if (state == 1)
        throw new IllegalStateException("Recursive initialization for " + propertyName);
      Object value;
      if (state == 0) {
        state = 1;
        

        value = Scriptable.NOT_FOUND;
      }
      try { value = buildValue();
        
        initializedValue = value;
        state = 2;
      }
      finally
      {
        initializedValue = value;
        state = 2;
      }
    }
  }
  
  Object getValue()
  {
    if (state != 2)
      throw new IllegalStateException(propertyName);
    return initializedValue;
  }
  
  private Object buildValue() {
    if (privileged) {
      
        AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            return LazilyLoadedCtor.this.buildValue0();
          }
        });
    }
    return buildValue0();
  }
  
  private Object buildValue0()
  {
    Class<? extends Scriptable> cl = cast(Kit.classOrNull(className));
    if (cl != null) {
      try {
        Object value = ScriptableObject.buildClassCtor(scope, cl, sealed, false);
        
        if (value != null) {
          return value;
        }
        

        value = scope.get(propertyName, scope);
        if (value != Scriptable.NOT_FOUND) {
          return value;
        }
      } catch (InvocationTargetException ex) {
        Throwable target = ex.getTargetException();
        if ((target instanceof RuntimeException)) {
          throw ((RuntimeException)target);
        }
      }
      catch (RhinoException localRhinoException) {}catch (InstantiationException localInstantiationException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (SecurityException localSecurityException) {}
    }
    


    return Scriptable.NOT_FOUND;
  }
  
  private Class<? extends Scriptable> cast(Class<?> cl)
  {
    return cl;
  }
}
