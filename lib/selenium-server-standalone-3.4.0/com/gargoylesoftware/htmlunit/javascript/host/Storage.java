package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;





























@JsxClass
public class Storage
  extends SimpleScriptable
{
  private static List<String> RESERVED_NAMES_ = Arrays.asList(new String[] { "clear", "key", "getItem", "length", "removeItem", "setItem", "constructor", "toString", "toLocaleString", "valueOf", "hasOwnProperty", "propertyIsEnumerable", "isPrototypeOf", "__defineGetter__", "__defineSetter__", "__lookupGetter__", "__lookupSetter__" });
  

  private final Map<String, String> store_;
  

  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Storage()
  {
    store_ = null;
  }
  




  public Storage(Window window, Map<String, String> store)
  {
    store_ = store;
    setParentScope(window);
    setPrototype(window.getPrototype(Storage.class));
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    boolean isReserved = RESERVED_NAMES_.contains(name);
    if ((store_ == null) || (isReserved)) {
      super.put(name, start, value);
    }
    if ((store_ != null) && ((!isReserved) || (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STORAGE_PRESERVED_INCLUDED)))) {
      setItem(name, Context.toString(value));
    }
  }
  



  public Object get(String name, Scriptable start)
  {
    if ((store_ == null) || (
      (RESERVED_NAMES_.contains(name)) && (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STORAGE_GET_FROM_ITEMS)))) {
      return super.get(name, start);
    }
    Object value = getItem(name);
    if (value != null) {
      return value;
    }
    return super.get(name, start);
  }
  



  @JsxGetter
  public int getLength()
  {
    return getMap().size();
  }
  



  @JsxFunction
  public void removeItem(String key)
  {
    getMap().remove(key);
  }
  




  @JsxFunction
  public String key(int index)
  {
    int counter = 0;
    for (String key : getMap().keySet()) {
      if (counter++ == index) {
        return key;
      }
    }
    return null;
  }
  
  private Map<String, String> getMap() {
    return store_;
  }
  




  @JsxFunction
  public Object getItem(String key)
  {
    return getMap().get(key);
  }
  




  @JsxFunction
  public void setItem(String key, String data)
  {
    getMap().put(key, data);
  }
  


  @JsxFunction
  public void clear()
  {
    getMap().clear();
  }
}
