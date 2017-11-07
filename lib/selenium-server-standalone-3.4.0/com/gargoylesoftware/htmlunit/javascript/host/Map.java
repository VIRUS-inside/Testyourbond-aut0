package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;






















@JsxClass
public class Map
  extends SimpleScriptable
{
  private java.util.Map<Object, Object> map_ = new LinkedHashMap();
  




  public Map() {}
  



  @JsxConstructor
  public Map(Object iterable)
  {
    if (iterable != Undefined.instance) {
      Window window = (Window)ScriptRuntime.getTopCallScope(Context.getCurrentContext());
      if (window.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_MAP_CONSTRUCTOR_ARGUMENT)) {
        if ((iterable instanceof NativeArray)) {
          NativeArray array = (NativeArray)iterable;
          for (int i = 0; i < array.getLength(); i++) {
            Object entryObject = array.get(i);
            if ((entryObject instanceof NativeArray)) {
              Object[] entry = toArray((NativeArray)entryObject);
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
        else if ((iterable instanceof Map)) {
          Map map = (Map)iterable;
          map_.putAll(map_);
        }
        else {
          throw Context.reportRuntimeError("TypeError: object is not iterable (" + 
            iterable.getClass().getName() + ")");
        }
      }
    }
  }
  


  private static Object[] toArray(NativeArray narray)
  {
    long longLen = narray.getLength();
    if (longLen > 2147483647L) {
      throw new IllegalStateException();
    }
    
    int len = (int)longLen;
    Object[] arr = new Object[len];
    for (int i = 0; i < len; i++) {
      arr[i] = ScriptableObject.getProperty(narray, i);
    }
    return arr;
  }
  



  @JsxGetter
  public int getSize()
  {
    return map_.size();
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
  public Map set(Object key, Object value)
  {
    if ((key instanceof Delegator)) {
      key = ((Delegator)key).getDelegee();
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
  




  public Object get(String name, Scriptable start)
  {
    if (name.equals("Symbol(Symbol.iterator)")) {
      return super.get("entries", start);
    }
    return super.get(name, start);
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Object entries()
  {
    SimpleScriptable object = new Iterator("Map Iterator", map_.entrySet().iterator());
    object.setParentScope(getParentScope());
    return object;
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Object keys()
  {
    SimpleScriptable object = new Iterator("Map Iterator", map_.keySet().iterator());
    object.setParentScope(getParentScope());
    return object;
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Object values()
  {
    SimpleScriptable object = new Iterator("Map Iterator", map_.values().iterator());
    object.setParentScope(getParentScope());
    return object;
  }
  




  @JsxFunction
  public void forEach(Function callback, Object thisArg)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_MAP_CONSTRUCTOR_ARGUMENT)) { Scriptable thisArgument;
      Scriptable thisArgument;
      if ((thisArg instanceof Scriptable)) {
        thisArgument = (Scriptable)thisArg;
      }
      else {
        thisArgument = getWindow();
      }
      for (Map.Entry<Object, Object> entry : map_.entrySet()) {
        callback.call(Context.getCurrentContext(), getParentScope(), thisArgument, 
          new Object[] { entry.getValue(), entry.getKey(), this });
      }
    }
  }
}
