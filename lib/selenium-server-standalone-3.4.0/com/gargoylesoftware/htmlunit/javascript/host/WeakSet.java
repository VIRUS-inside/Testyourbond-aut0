package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class WeakSet
  extends SimpleScriptable
{
  private transient Set<Object> set_ = Collections.newSetFromMap(new WeakHashMap());
  




  public WeakSet() {}
  



  @JsxConstructor
  public WeakSet(Object iterable)
  {
    if (iterable != Undefined.instance) {
      if ((iterable instanceof NativeArray)) {
        NativeArray array = (NativeArray)iterable;
        for (int i = 0; i < array.getLength(); i++) {
          add(array.get(i));
        }
      }
      else {
        throw Context.reportRuntimeError("TypeError: object is not iterablee (" + 
          iterable.getClass().getName() + ")");
      }
    }
  }
  




  @JsxFunction
  public WeakSet add(Object value)
  {
    if ((value instanceof Delegator)) {
      value = ((Delegator)value).getDelegee();
    }
    if (!(value instanceof ScriptableObject)) {
      throw Context.reportRuntimeError("TypeError: key is not an object");
    }
    set_.add(value);
    return this;
  }
  


  @JsxFunction
  public void clear()
  {
    set_.clear();
  }
  




  @JsxFunction
  public boolean delete(Object key)
  {
    return set_.remove(key);
  }
  




  @JsxFunction
  public boolean has(Object value)
  {
    return set_.contains(value);
  }
}
