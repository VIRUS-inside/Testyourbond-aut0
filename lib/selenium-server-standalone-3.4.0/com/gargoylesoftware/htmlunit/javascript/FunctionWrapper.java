package com.gargoylesoftware.htmlunit.javascript;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;






















public class FunctionWrapper
  implements Function
{
  private final Function wrapped_;
  
  public FunctionWrapper(Function wrapped)
  {
    wrapped_ = wrapped;
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    return wrapped_.call(cx, scope, thisObj, args);
  }
  



  public String getClassName()
  {
    return wrapped_.getClassName();
  }
  



  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    return wrapped_.construct(cx, scope, args);
  }
  



  public Object get(String name, Scriptable start)
  {
    return wrapped_.get(name, start);
  }
  



  public Object get(int index, Scriptable start)
  {
    return wrapped_.get(index, start);
  }
  



  public boolean has(String name, Scriptable start)
  {
    return wrapped_.has(name, start);
  }
  



  public boolean has(int index, Scriptable start)
  {
    return wrapped_.has(index, start);
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    wrapped_.put(name, wrapped_, value);
  }
  



  public void put(int index, Scriptable start, Object value)
  {
    wrapped_.put(index, wrapped_, value);
  }
  



  public void delete(String name)
  {
    wrapped_.delete(name);
  }
  



  public void delete(int index)
  {
    wrapped_.delete(index);
  }
  



  public Scriptable getPrototype()
  {
    return wrapped_.getPrototype();
  }
  



  public void setPrototype(Scriptable prototype)
  {
    wrapped_.setPrototype(prototype);
  }
  



  public Scriptable getParentScope()
  {
    return wrapped_.getParentScope();
  }
  



  public void setParentScope(Scriptable parent)
  {
    wrapped_.setParentScope(parent);
  }
  



  public Object[] getIds()
  {
    return wrapped_.getIds();
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    return wrapped_.getDefaultValue(hint);
  }
  



  public boolean hasInstance(Scriptable instance)
  {
    return wrapped_.hasInstance(instance);
  }
}
