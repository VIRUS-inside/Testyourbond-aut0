package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;


























public class ActiveXObjectImpl
  extends SimpleScriptable
{
  private static final Class<?> activeXComponentClass_;
  private static final Method METHOD_getProperty_;
  private final Object object_;
  private static final Method METHOD_callN_;
  private static final Method METHOD_getvt_;
  private static final Method METHOD_getDispatch_;
  
  static
  {
    try
    {
      activeXComponentClass_ = Class.forName("com.jacob.activeX.ActiveXComponent");
      METHOD_getProperty_ = activeXComponentClass_.getMethod("getProperty", new Class[] { String.class });
      Class<?> dispatchClass = Class.forName("com.jacob.com.Dispatch");
      METHOD_callN_ = dispatchClass.getMethod("callN", new Class[] { dispatchClass, String.class, [Ljava.lang.Object.class });
      Class<?> variantClass = Class.forName("com.jacob.com.Variant");
      METHOD_getvt_ = variantClass.getMethod("getvt", new Class[0]);
      METHOD_getDispatch_ = variantClass.getMethod("getDispatch", new Class[0]);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  




  public ActiveXObjectImpl(String activeXName)
    throws Exception
  {
    this(activeXComponentClass_.getConstructor(new Class[] { String.class }).newInstance(new Object[] { activeXName }));
  }
  
  private ActiveXObjectImpl(Object object) {
    object_ = object;
  }
  


  public Object get(final String name, Scriptable start)
  {
    try
    {
      Object variant = METHOD_getProperty_.invoke(object_, new Object[] { name });
      return wrapIfNecessary(variant);
    }
    catch (Exception e) {}
    new Function()
    {
      public Object call(Context arg0, Scriptable arg1, Scriptable arg2, Object[] arg3)
      {
        try {
          Object rv = ActiveXObjectImpl.METHOD_callN_.invoke(null, new Object[] { object_, name, arg3 });
          return ActiveXObjectImpl.wrapIfNecessary(rv);
        }
        catch (Exception ex) {
          throw Context.throwAsScriptRuntimeEx(ex);
        }
      }
      
      public Scriptable construct(Context arg0, Scriptable arg1, Object[] arg2)
      {
        throw new UnsupportedOperationException();
      }
      
      public void delete(String arg0)
      {
        throw new UnsupportedOperationException();
      }
      
      public void delete(int arg0)
      {
        throw new UnsupportedOperationException();
      }
      
      public Object get(String arg0, Scriptable arg1)
      {
        throw new UnsupportedOperationException();
      }
      
      public Object get(int arg0, Scriptable arg1)
      {
        throw new UnsupportedOperationException();
      }
      
      public String getClassName()
      {
        throw new UnsupportedOperationException();
      }
      
      public Object getDefaultValue(Class<?> arg0)
      {
        throw new UnsupportedOperationException();
      }
      
      public Object[] getIds()
      {
        throw new UnsupportedOperationException();
      }
      
      public Scriptable getParentScope()
      {
        throw new UnsupportedOperationException();
      }
      
      public Scriptable getPrototype()
      {
        throw new UnsupportedOperationException();
      }
      
      public boolean has(String arg0, Scriptable arg1)
      {
        throw new UnsupportedOperationException();
      }
      
      public boolean has(int arg0, Scriptable arg1)
      {
        throw new UnsupportedOperationException();
      }
      
      public boolean hasInstance(Scriptable arg0)
      {
        throw new UnsupportedOperationException();
      }
      
      public void put(String arg0, Scriptable arg1, Object arg2)
      {
        throw new UnsupportedOperationException();
      }
      
      public void put(int arg0, Scriptable arg1, Object arg2)
      {
        throw new UnsupportedOperationException();
      }
      
      public void setParentScope(Scriptable arg0)
      {
        throw new UnsupportedOperationException();
      }
      
      public void setPrototype(Scriptable arg0)
      {
        throw new UnsupportedOperationException();
      }
    };
  }
  




  private static Object wrapIfNecessary(Object variant)
    throws Exception
  {
    if (((Short)METHOD_getvt_.invoke(variant, new Object[0])).shortValue() == 9) {
      return new ActiveXObjectImpl(METHOD_getDispatch_.invoke(variant, new Object[0]));
    }
    return variant;
  }
  


  public void put(String name, Scriptable start, Object value)
  {
    try
    {
      Method setMethod = activeXComponentClass_.getMethod("setProperty", new Class[] { String.class, value.getClass() });
      setMethod.invoke(object_, new Object[] { name, Context.toString(value) });
    }
    catch (Exception e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
}
