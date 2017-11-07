package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;



























public abstract class SimpleScriptableProxy<T extends SimpleScriptable>
  extends Delegator
  implements Serializable
{
  public SimpleScriptableProxy() {}
  
  public abstract T getDelegee();
  
  public Object get(int index, Scriptable start)
  {
    if ((start instanceof SimpleScriptableProxy)) {
      start = ((SimpleScriptableProxy)start).getDelegee();
    }
    return getDelegee().get(index, start);
  }
  



  public Object get(String name, Scriptable start)
  {
    if ((start instanceof SimpleScriptableProxy)) {
      start = ((SimpleScriptableProxy)start).getDelegee();
    }
    return getDelegee().get(name, start);
  }
  



  public boolean has(int index, Scriptable start)
  {
    if ((start instanceof SimpleScriptableProxy)) {
      start = ((SimpleScriptableProxy)start).getDelegee();
    }
    return getDelegee().has(index, start);
  }
  



  public boolean has(String name, Scriptable start)
  {
    if ((start instanceof SimpleScriptableProxy)) {
      start = ((SimpleScriptableProxy)start).getDelegee();
    }
    return getDelegee().has(name, start);
  }
  



  public boolean hasInstance(Scriptable instance)
  {
    if ((instance instanceof SimpleScriptableProxy)) {
      instance = ((SimpleScriptableProxy)instance).getDelegee();
    }
    return getDelegee().hasInstance(instance);
  }
  



  public void put(int index, Scriptable start, Object value)
  {
    if ((start instanceof SimpleScriptableProxy)) {
      start = ((SimpleScriptableProxy)start).getDelegee();
    }
    getDelegee().put(index, start, value);
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    if ((start instanceof SimpleScriptableProxy)) {
      start = ((SimpleScriptableProxy)start).getDelegee();
    }
    getDelegee().put(name, start, value);
  }
  







  public Object getDefaultValue(Class<?> hint)
  {
    return getDelegee().getDefaultValue(hint);
  }
}
