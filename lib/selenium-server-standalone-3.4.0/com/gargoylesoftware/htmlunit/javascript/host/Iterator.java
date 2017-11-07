package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.util.Map.Entry;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

























public class Iterator
  extends SimpleScriptable
{
  private final java.util.Iterator<?> iterator_;
  
  public Iterator(String className, java.util.Iterator<?> iterator)
  {
    setClassName(className);
    iterator_ = iterator;
  }
  



  public void setParentScope(Scriptable scope)
  {
    super.setParentScope(scope);
    try {
      FunctionObject functionObject = new FunctionObject("next", getClass().getDeclaredMethod("next", new Class[0]), 
        scope);
      defineProperty("next", functionObject, 2);
    }
    catch (Exception e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  



  public Object next()
  {
    SimpleScriptable object = new SimpleScriptable();
    object.setParentScope(getParentScope());
    Object value;
    Object value; if ((iterator_ != null) && (iterator_.hasNext())) {
      Object next = iterator_.next();
      Object value; if ((next instanceof Map.Entry)) {
        final Map.Entry<?, ?> entry = (Map.Entry)next;
        NativeArray array = new NativeArray(new Object[] { entry.getKey(), entry.getValue() })
        {
          public Object getDefaultValue(Class<?> hint)
          {
            return Context.toString(entry.getKey()) + ',' + Context.toString(entry.getValue());
          }
        };
        value = array;
      }
      else {
        value = next;
      }
    }
    else {
      value = Undefined.instance;
    }
    object.defineProperty("value", value, 2);
    return object;
  }
}
