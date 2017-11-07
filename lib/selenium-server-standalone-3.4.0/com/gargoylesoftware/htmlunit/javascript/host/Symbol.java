package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticGetter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;





















@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class Symbol
  extends SimpleScriptable
{
  static final String ITERATOR_STRING = "Symbol(Symbol.iterator)";
  private static Map<BrowserVersion, Map<String, Symbol>> SYMBOL_MAP_ = new HashMap();
  


  private Object name_;
  



  public Symbol() {}
  


  @JsxConstructor
  public Symbol(Object name)
  {
    name_ = name;
    for (StackTraceElement stackElement : new Throwable().getStackTrace()) {
      if ((stackElement.getClassName().contains("BaseFunction")) && 
        ("construct".equals(stackElement.getMethodName()))) {
        throw ScriptRuntime.typeError("Symbol is not a constructor");
      }
    }
  }
  




  @JsxStaticGetter
  public static Symbol getIterator(Scriptable thisObj)
  {
    return getSymbol(thisObj, "iterator");
  }
  
  private static Symbol getSymbol(Scriptable thisObj, String name) {
    SimpleScriptable scope = (SimpleScriptable)thisObj.getParentScope();
    BrowserVersion browserVersion = scope.getBrowserVersion();
    
    Map<String, Symbol> map = (Map)SYMBOL_MAP_.get(browserVersion);
    if (map == null) {
      map = new HashMap();
      SYMBOL_MAP_.put(browserVersion, map);
    }
    
    Symbol symbol = (Symbol)map.get(name);
    if (symbol == null) {
      symbol = new Symbol();
      name_ = name;
      symbol.setParentScope(scope);
      symbol.setPrototype(scope.getPrototype(symbol.getClass()));
      map.put(name, symbol);
    }
    
    return symbol;
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getUnscopables(Scriptable thisObj)
  {
    return getSymbol(thisObj, "unscopables");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getIsConcatSpreadable(Scriptable thisObj)
  {
    return getSymbol(thisObj, "isConcatSpreadable");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static Symbol getToPrimitive(Scriptable thisObj)
  {
    return getSymbol(thisObj, "toPrimitive");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getToStringTag(Scriptable thisObj)
  {
    return getSymbol(thisObj, "toStringTag");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getMatch(Scriptable thisObj)
  {
    return getSymbol(thisObj, "match");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getHasInstance(Scriptable thisObj)
  {
    return getSymbol(thisObj, "hasInstance");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getReplace(Scriptable thisObj)
  {
    return getSymbol(thisObj, "replace");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getSearch(Scriptable thisObj)
  {
    return getSymbol(thisObj, "search");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getSplit(Scriptable thisObj)
  {
    return getSymbol(thisObj, "split");
  }
  




  @JsxStaticGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public static Symbol getSpecies(Scriptable thisObj)
  {
    return getSymbol(thisObj, "species");
  }
  









  @JsxStaticFunction(functionName="for")
  public static Symbol forFunction(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    String key = Context.toString(args.length != 0 ? args[0] : Undefined.instance);
    
    Symbol symbol = (Symbol)((ScriptableObject)thisObj).get(key);
    if (symbol == null) {
      SimpleScriptable parentScope = (SimpleScriptable)thisObj.getParentScope();
      
      symbol = new Symbol();
      name_ = key;
      symbol.setParentScope(parentScope);
      symbol.setPrototype(parentScope.getPrototype(symbol.getClass()));
      thisObj.put(key, thisObj, symbol);
    }
    return symbol;
  }
  



  public String getTypeOf()
  {
    return "symbol";
  }
  

  @JsxFunction
  public String toString()
  {
    String name;
    
    String name;
    if (name_ == Undefined.instance) {
      name = "";
    }
    else {
      name = Context.toString(name_);
      ClassConfiguration config = 
        AbstractJavaScriptConfiguration.getClassConfiguration(getClass(), getBrowserVersion());
      

      Iterator localIterator = config.getStaticPropertyEntries().iterator();
      while (localIterator.hasNext()) {
        Map.Entry<String, ClassConfiguration.PropertyInfo> propertyEntry = (Map.Entry)localIterator.next();
        if (((String)propertyEntry.getKey()).equals(name)) {
          name = "Symbol." + name;
          break;
        }
      }
    }
    return "Symbol(" + name + ')';
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    if ((String.class.equals(hint)) || (hint == null)) {
      return toString();
    }
    return super.getDefaultValue(hint);
  }
  

  public static void remove(Window window)
  {
    Iterator<Symbol> it;
    
    for (Iterator localIterator = SYMBOL_MAP_.values().iterator(); localIterator.hasNext(); 
        it.hasNext())
    {
      Map<String, Symbol> symbols = (Map)localIterator.next();
      it = symbols.values().iterator(); continue;
      if (((Symbol)it.next()).getParentScope() == window) {
        it.remove();
      }
    }
  }
}
