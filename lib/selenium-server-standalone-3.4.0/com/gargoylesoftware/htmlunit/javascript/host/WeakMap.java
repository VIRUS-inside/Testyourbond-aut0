package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import java.util.Map;
import java.util.WeakHashMap;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;




















@JsxClass
public class WeakMap
  extends SimpleScriptable
{
  private transient Map<Object, Object> map_ = new WeakHashMap();
  




  public WeakMap() {}
  



  @JsxConstructor
  public WeakMap(Object iterable)
  {
    if (iterable != Undefined.instance) {
      Window window = (Window)ScriptRuntime.getTopCallScope(Context.getCurrentContext());
      if (window.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WEAKMAP_CONSTRUCTOR_ARGUMENT)) {
        if ((iterable instanceof NativeArray)) {
          NativeArray array = (NativeArray)iterable;
          for (int i = 0; i < array.getLength(); i++) {
            Object entryObject = array.get(i);
            if ((entryObject instanceof NativeArray)) {
              Object[] entry = ((NativeArray)entryObject).toArray();
              if (entry.length > 0) {
                Object key = entry[0];
                Object value = entry.length > 1 ? entry[1] : null;
                set(key, value);
              }
            }
            else {
              throw Context.reportRuntimeError("TypeError: object is not iterable (" + 
                entryObject.getClass().getName() + ")");
            }
          }
        }
        else {
          throw Context.reportRuntimeError("TypeError: object is not iterable (" + 
            iterable.getClass().getName() + ")");
        }
      }
    }
  }
  





  @JsxFunction
  public Object get(Object key)
  {
    Object o = map_.get(key);
    if (o == null) {
      o = Undefined.instance;
    }
    return o;
  }
  





  @JsxFunction
  public WeakMap set(Object key, Object value)
  {
    if ((key instanceof Delegator)) {
      key = ((Delegator)key).getDelegee();
    }
    if (!(key instanceof ScriptableObject)) {
      throw Context.reportRuntimeError("TypeError: key is not an object");
    }
    map_.put(key, value);
    return this;
  }
  


  @JsxFunction
  public void clear()
  {
    map_.clear();
  }
  




  @JsxFunction
  public boolean delete(Object key)
  {
    return map_.remove(key) != null;
  }
  




  @JsxFunction
  public boolean has(Object key)
  {
    return map_.remove(key) != null;
  }
}
