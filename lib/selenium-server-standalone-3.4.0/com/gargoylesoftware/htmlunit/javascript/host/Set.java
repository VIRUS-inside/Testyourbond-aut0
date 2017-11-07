package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase;
import java.util.LinkedHashSet;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

























@JsxClass
public class Set
  extends SimpleScriptable
{
  private java.util.Set<Object> set_ = new LinkedHashSet();
  




  public Set() {}
  



  @JsxConstructor
  public Set(Object iterable)
  {
    if (iterable != Undefined.instance) {
      Window window = (Window)ScriptRuntime.getTopCallScope(Context.getCurrentContext());
      if (window.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_MAP_CONSTRUCTOR_ARGUMENT)) {
        if ((iterable instanceof NativeArray)) {
          NativeArray array = (NativeArray)iterable;
          for (int i = 0; i < array.getLength(); i++) {
            add(ScriptableObject.getProperty(array, i));
          }
        }
        else if ((iterable instanceof ArrayBufferViewBase)) {
          ArrayBufferViewBase array = (ArrayBufferViewBase)iterable;
          for (int i = 0; i < array.getLength(); i++) {
            add(ScriptableObject.getProperty(array, i));
          }
        }
        else if ((iterable instanceof String)) {
          String string = (String)iterable;
          for (int i = 0; i < string.length(); i++) {
            add(String.valueOf(string.charAt(i)));
          }
        }
        else if ((iterable instanceof Set)) {
          Set set = (Set)iterable;
          for (Object object : set_) {
            add(object);
          }
        }
        else if ((iterable instanceof Map)) {
          Iterator iterator = (Iterator)((Map)iterable).entries();
          
          SimpleScriptable object = (SimpleScriptable)iterator.next();
          while (Undefined.instance != object.get("value", null)) {
            add(object);
            object = (SimpleScriptable)iterator.next();
          }
        }
        else {
          throw Context.reportRuntimeError("TypeError: object is not iterable (" + 
            iterable.getClass().getName() + ")");
        }
      }
    }
  }
  



  @JsxGetter
  public int getSize()
  {
    return set_.size();
  }
  




  @JsxFunction
  public Set add(Object value)
  {
    if ((value instanceof Delegator)) {
      value = ((Delegator)value).getDelegee();
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
  




  public Object get(String name, Scriptable start)
  {
    if (name.equals("Symbol(Symbol.iterator)")) {
      return super.get("values", start);
    }
    return super.get(name, start);
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Object values()
  {
    SimpleScriptable object = new Iterator("Set Iterator", set_.iterator());
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
      for (Object object : set_) {
        callback.call(Context.getCurrentContext(), getParentScope(), thisArgument, 
          new Object[] { object, object, this });
      }
    }
  }
}
